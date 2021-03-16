package cod

import SimLib._
import SimLib.implClass._
import chisel3._
import chisel3.util._
import org.scalatest.flatspec.AnyFlatSpec
import chiseltest._
import chiseltest.experimental.TestOptionBuilder.ChiselScalatestOptionBuilder
import chiseltest.internal.WriteVcdAnnotation
import org.scalatest.matchers.should.Matchers
import Interfaces._

import scala.collection.mutable
import scala.util.Random

object SimConfig {
  val packages = 1000
  val maxAddress = 1024 * 1024
  val rng = new Random()
}

case class ReqPkg(wr: Boolean, addr: BigInt, data: BigInt) extends Package {
  override def toString: String = f" ${if (wr) "write" else "read"} addr: ${addr}%x, data: ${data}%x "
}
case class RespPkg(wr: Boolean, data: BigInt) extends Package {
  override def toString: String = f" ${if (wr) "write" else "read"} data: ${data}%x "
}

case class CacheReqDriver(bus: DecoupledIO[CpuRequest], clk: Clock) extends Driver[ReqPkg](bus, clk) {
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

case class CacheRespMonitor(bus: ValidIO[CpuResponse], clk: Clock) extends Monitor[RespPkg](bus, clk) {
  override def sample(): RespPkg = {
    clk.waitSamplingWhere(bus.valid)
    RespPkg(bus.bits.wr.peek().litToBoolean, bus.bits.data.peek().litValue())
  }
}

case class MemReqMonitor(bus: ValidIO[MemRequest], clk: Clock) extends Monitor[ReqPkg](bus, clk) {
  override def sample(): ReqPkg = {
    clk.waitSamplingWhere(bus.valid)
    ReqPkg(bus.bits.wr.peek().litToBoolean, bus.bits.addr.peek().litValue(), bus.bits.data.peek().litValue())
  }
}

case class MemRespDriver(bus: ValidIO[MemResponse], clk: Clock, n: Int) extends Driver[ReqPkg](bus, clk) {
  val Mem = mutable.Map[BigInt, BigInt]()

  override def setup(): Unit = {
    for (i <- 0 until n) {
      Mem(i) = (SimConfig.rng.nextInt(Int.MaxValue): BigInt) * 2 + (if (SimConfig.rng.nextBoolean()) 1 else 0)
    }
    bus.valid.poke(false.B)
  }

  override def send(pkg: ReqPkg): Unit = {
    val addr = pkg.addr >> 4 << 2
    val b32Mask = BigInt("FFFFFFFF", 16)
    if (pkg.wr) {
      (0 to 3) foreach (i => Mem(addr + i) = (pkg.data >> (32 * i)) & b32Mask)
      simDebug(f"[MemRespDriver] got $pkg, write ${addr}%x ${Mem(addr)}%x")
    } else {
      bus.bits.data.poke(((Mem(addr + 3) << 96) + (Mem(addr + 2) << 64) + (Mem(addr + 1) << 32) + Mem(addr)).U)
    }
    bus.valid.poke(true.B)
    clk.step()
    bus.valid.poke(false.B)
  }
}

case class Rm(qin: mutable.Queue[ReqPkg],
              clk: Clock,
              mem: mutable.Map[BigInt, BigInt],
              n: Int) extends ReferenceModel[ReqPkg, RespPkg](qin, clk) {
  val cache = mutable.Map[BigInt, BigInt]()

  override def setup(): Unit = {
    clk.step(1)
    for (i <- 0 until n) cache(i) = mem(i)
  }

  override def calculate(pkg: ReqPkg): Option[RespPkg] = {
    simDebug(f"[RM] got new package $pkg, mem: ${mem(pkg.addr >> 2)}%x cache: ${cache(pkg.addr >> 2)}%x")
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

case class RandomSequence(drv: CacheReqDriver, n: Int, maxAddress: Int) extends Sequence[ReqPkg](drv) {
  override def run(): Unit = {
    (0 to n).foreach(_ -> drv.q.enqueue(ReqPkg(SimConfig.rng.nextBoolean, SimConfig.rng.nextInt(maxAddress - 1) >> 2 << 2, SimConfig.rng.nextInt.abs)))
    simDebug(s"[Random Sequence] wait done begin. driver queue length: ${drv.q.length}")
    drv.waitDone()
    simDebug("[Random Sequence] all package send done!")
  }
}

case class RepeatSequence(drv: CacheReqDriver, n: Int = 100) extends Sequence[ReqPkg](drv) {
  override def run(): Unit = {
    (0 to n).foreach(_ -> drv.q.enqueue(ReqPkg(SimConfig.rng.nextBoolean, SimConfig.rng.nextInt(16) >> 2 << 2, SimConfig.rng.nextInt.abs)))
    simDebug(s"[Random Sequence] wait done begin. driver queue length: ${drv.q.length}")
    drv.waitDone()
    simDebug("[Random Sequence] all package send done!")
  }
}

case class Env(dut: CacheBase, clk: Clock, n: Int) extends Environment(dut) {
  def connect[T](q0: mutable.Queue[T], q1: mutable.Queue[T], clk: Clock) = {
    while (true) {
      if (q0.nonEmpty) q1.enqueue(q0.dequeue())
      clk.step()
    }
  }

//  val cacheDriver = CacheReqDriver(dut.io.cacheReq, clk)
//  val cacheMonitor = CacheRespMonitor(dut.io.cacheResp, clk)
//  val memMonitor = MemReqMonitor(dut.io.memReq, clk)
//  val memDriver = MemRespDriver(dut.io.memResp, clk)
  val cacheDriver = CacheReqDriver(dut.io.cpuRequest, clk)
  val cacheMonitor = CacheRespMonitor(dut.io.cpuResponse, clk)
  val memMonitor = MemReqMonitor(dut.io.memRequest, clk)
  val memDriver = MemRespDriver(dut.io.memResponse, clk, n)
  val rm = Rm(cacheDriver.monitorQueue, clk, memDriver.Mem, n)
  val sb = CacheScoreboard(cacheMonitor.q, rm.q, clk)

  override def run(): Unit = {
    val cacheMonitorThread = fork.withRegion(Monitor) (cacheMonitor.run())
    val memMonitorThread = fork.withRegion(Monitor) (memMonitor.run())
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

case class CacheRandomTest(env: Env, n: Int, a: Int) extends TestBench(env) {
  override def runSeq(): Unit = RandomSequence(env.cacheDriver, n, a).run()
}

case class CacheRepeatTest(env: Env, n: Int, a: Int) extends TestBench(env) {
  override def runSeq(): Unit = RepeatSequence(env.cacheDriver, n).run()
}

class CacheTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "CacheTest"

  it should "cache3 pass" in {
    test(Cache3()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val packages = SimConfig.packages
      var cycles = 0
      fork { while (true) { dut.clock.step(); cycles += 1 }}

      //      dut.io.cacheResp.ready.poke(true.B)
      val clk = dut.clock
      clk.step(10)

      val env = Env(dut, clk, SimConfig.maxAddress)
      val randomTb = CacheRandomTest(env, packages, SimConfig.maxAddress)
      val repeatTb = CacheRepeatTest(env, packages, SimConfig.maxAddress)
      //      repeatTb.run()
//      fork.withRegion(Monitor) {
//        while (true) {
//          if (dut.io.memRequest.bits.addr.peek().litValue() == 0x43d0) {
//            clk.step(10)
//            assert(false, "got")
//          }
//          clk.step()
//        }
//      }
      randomTb.run()
      println(s"${packages} package sent in ${cycles} cycles")
    }
  }
}
