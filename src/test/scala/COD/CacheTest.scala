package cod

import SimLib._
import SimLib.implClass._
import chisel3._
import chisel3.util._
import Const._
import chisel3.experimental.BundleLiterals.AddBundleLiteralConstructor
import chisel3.util.Cat
import org.scalatest.flatspec.AnyFlatSpec
import chiseltest._
import chiseltest.experimental.TestOptionBuilder.ChiselScalatestOptionBuilder
import chiseltest.internal.WriteVcdAnnotation
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.util.Random

case class ReqPkg(wr: Boolean, addr: BigInt, data: BigInt) extends Package {
  override def toString: String = f" ${if (wr) "write" else "read"} addr: ${addr}%x, data: ${data}%x "
}
case class RespPkg(wr: Boolean, data: BigInt) extends Package {
  override def toString: String = f" ${if (wr) "write" else "read"} data: ${data}%x "
}

case class CacheReqDriver(bus: DecoupledIO[CacheRequest], clk: Clock) extends Driver[ReqPkg](bus, clk) {
  val monitorQueue = mutable.Queue[ReqPkg]()

  override def send(pkg: ReqPkg): Unit = {
    clk.waitSamplingWhere(bus.ready)
    bus.bits.wr.poke(pkg.wr.B)
    bus.bits.addr.poke(pkg.addr.U)
    bus.bits.data.poke(pkg.data.U)
    bus.valid.poke(true.B)
//    clk.waitSamplingWhere(bus.ready)
    clk.step()
    bus.valid.poke(false.B)
    simDebug(f"[Cache Req Driver] send package, ${if (pkg.wr) "write" else "read"} addr: ${pkg.addr}%x, data: ${pkg.data}%x")
    monitorQueue.enqueue(pkg)
  }
}

case class CacheRespMonitor(bus: DecoupledIO[CacheResponse], clk: Clock) extends Monitor[RespPkg](bus, clk) {
  override def sample(): RespPkg = {
    clk.waitSamplingWhere(bus.valid)
    RespPkg(bus.bits.wr.peek().litToBoolean, bus.bits.data.peek().litValue())
  }
}

case class MemReqMonitor(bus: Valid[MemRequest], clk: Clock) extends Monitor[ReqPkg](bus, clk) {
  override def sample(): ReqPkg = {
    clk.waitSamplingWhere(bus.valid)
    ReqPkg(bus.bits.wr.peek().litToBoolean, bus.bits.addr.peek().litValue(), bus.bits.data.peek().litValue())
  }
}

case class MemRespDriver(bus: Valid[MemResponse], clk: Clock) extends Driver[ReqPkg](bus, clk) {
  val Mem = mutable.Map[BigInt, BigInt]()

  override def setup(): Unit = {
    for (i <- 0 to 1023) {
      Mem(i) = (Random.nextInt(Int.MaxValue): BigInt) * 2 + (if (Random.nextBoolean()) 1 else 0)
    }
    bus.valid.poke(false.B)
  }

  override def send(pkg: ReqPkg): Unit = {
    if (pkg.wr) {
      Mem(pkg.addr >> 2) = pkg.data
    } else {
      val addr = pkg.addr >> 4 << 2
      bus.bits.data.poke(((Mem(addr + 3) << 96) + (Mem(addr + 2) << 64) + (Mem(addr + 1) << 32) + Mem(addr)).U)
    }
    bus.valid.poke(true.B)
    clk.step()
    bus.valid.poke(false.B)
  }
}

case class Rm(qin: mutable.Queue[ReqPkg],
              clk: Clock,
              mem: mutable.Map[BigInt, BigInt]) extends ReferenceModel[ReqPkg, RespPkg](qin, clk) {
  val cache = mutable.Map[BigInt, BigInt]()

  override def setup(): Unit = {
    clk.step(1)
    for (i <- 0 to 1023) cache(i) = mem(i)
  }

  override def calculate(pkg: ReqPkg): Option[RespPkg] = {
    simDebug(f"[RM] got new package $pkg, mem: ${mem(pkg.addr >> 2)}%x")
    if (pkg.wr) { cache(pkg.addr >> 2) = pkg.data; None}
    else Some(RespPkg(wr = false, cache(pkg.addr >> 2)))
  }
}

case class CacheScoreboard(q0: mutable.Queue[RespPkg], q1: mutable.Queue[RespPkg], clk: Clock)
  extends ScoreBoard[RespPkg](q0, q1, clk) {
  override def isValid(i: RespPkg): Boolean = !i.wr
  override def compare(i: RespPkg, j: RespPkg): Boolean = {
    simDebug(s"[ScoreBoard] Dut: ${i}, RM: ${j}")
    i == j
  }
}

case class RandomSequence(drv: CacheReqDriver, n: Int = 100) extends Sequence[ReqPkg](drv) {
  override def run(): Unit = {
    (0 to n).foreach(_ -> drv.q.enqueue(ReqPkg(Random.nextBoolean, Random.nextInt(1023) >> 2 << 2, Random.nextInt.abs)))
    simDebug(s"[Random Sequence] wait done begin. driver queue length: ${drv.q.length}")
    drv.waitDone()
    simDebug("[Random Sequence] all package send done!")
  }
}

case class RepeatSequence(drv: CacheReqDriver, n: Int = 100) extends Sequence[ReqPkg](drv) {
  override def run(): Unit = {
    (0 to n).foreach(_ -> drv.q.enqueue(ReqPkg(Random.nextBoolean, Random.nextInt(16) >> 2 << 2, Random.nextInt.abs)))
    simDebug(s"[Random Sequence] wait done begin. driver queue length: ${drv.q.length}")
    drv.waitDone()
    simDebug("[Random Sequence] all package send done!")
  }
}

case class Env(dut: Cache, clk: Clock) extends Environment(dut) {
  def connect[T](q0: mutable.Queue[T], q1: mutable.Queue[T], clk: Clock) = {
    while (true) {
      if (q0.nonEmpty) q1.enqueue(q0.dequeue())
      clk.step()
    }
  }

  val cacheDriver = CacheReqDriver(dut.io.cacheReq, clk)
  val cacheMonitor = CacheRespMonitor(dut.io.cacheResp, clk)
  val memMonitor = MemReqMonitor(dut.io.memReq, clk)
  val memDriver = MemRespDriver(dut.io.memResp, clk)
  val rm = Rm(cacheDriver.monitorQueue, clk, memDriver.Mem)
  val sb = CacheScoreboard(cacheMonitor.q, rm.q, clk)

  override def run(): Unit = {
    val cacheMonitorThread = fork (cacheMonitor.run())
    val memMonitorThread = fork (memMonitor.run())
    val memDriverThread = fork (memDriver.run())
    val cacheDriverThread = fork (cacheDriver.run())
    val connectThread = fork (connect(memMonitor.q, memDriver.q, clk))
    val rmThread = fork (rm.run())
    val sbThread = fork (sb.run())
  }

  override def report(): Unit = {
    sb.report()
    rm.report()
  }

  override def setup(): Unit = {}
}

case class CacheRandomTest(env: Env, n: Int = 100) extends TestBench(env) {
  override def runSeq(): Unit = RandomSequence(env.cacheDriver, n).run()
}

case class CacheRepeatTest(env: Env, n: Int = 100) extends TestBench(env) {
  override def runSeq(): Unit = RepeatSequence(env.cacheDriver, n).run()
}

class CacheTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "CacheTest"

  it should "pass" in {
    test(Cache()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val packages = 1000
      var cycles = 0
      fork { while (true) { dut.clock.step(); cycles += 1 }}

      dut.io.cacheResp.ready.poke(true.B)
      val clk = dut.clock
      clk.step(10)

      val env = Env(dut, clk)
      val randomTb = CacheRandomTest(env, packages)
      val repeatTb = CacheRepeatTest(env, packages)
//      tb.run()
      repeatTb.run()
      println(s"${packages} package sent in ${cycles} cycles")
    }
  }
}
