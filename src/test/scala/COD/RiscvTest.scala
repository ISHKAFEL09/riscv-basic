package cod

import chisel3._
import chisel3.util._
import org.scalatest.flatspec.AnyFlatSpec
import chiseltest._
import chiseltest.experimental.TestOptionBuilder.ChiselScalatestOptionBuilder
import chiseltest.internal.WriteVcdAnnotation
import org.scalatest.matchers.should.Matchers
import Interfaces._
import SimLib._
import java.io._

import scala.collection.mutable
import scala.io.Source
import scala.util.Random

object Config {
  var cycles = 0
  val passPc: Int = 0x80000600
  val failPc: Int = 0x800005f0
}

case class MemoryPkg(wr: Boolean, addr: BigInt, data: BigInt) extends Package {
  override def toString: String = f" ${if (wr) "write" else "read"} addr: ${addr}%x, data: ${data}%x "
}

abstract class MemoryAgent(val bus: MemoryIO)(implicit val clk: Clock) {
  def memory: collection.mutable.Map[BigInt, Byte]
  private def req = bus.req.bits
  private def resp = bus.resp.bits
  def run() = {
    while (true) {
      bus.resp.valid.poke(false.B)
      clk.waitSampling(bus.req.valid)
      bus.resp.valid.poke(true.B)
      val addr = req.addr.peek().litValue() >> 2 << 2
      if (req.wr.peek().litToBoolean) {
        for (i <- 0 to 3) {
          memory(addr + i) = (req.wdata.peek().litValue() >> (8 * i)).toByte
        }
      } else {
        var rdata: BigInt = 0
        for (i <- (0 to 3).reverse) {
          val m: BigInt = memory(addr + i)
          rdata = (rdata << 8) + (if (m >= 0) m else m + 256)
        }
        resp.rdata.poke(rdata.U)
      }
      clk.step()
    }
  }
}

class RiscvTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "Riscv Test"

  def getBytes(f: String): Seq[Byte] = {
    val file = new File(f)
    val in = new FileInputStream(file)
    val bytes = new Array[Byte](file.length.toInt)
    in.read(bytes)
    bytes
  }

  it should "pass riscv test" in {
    test(Tile()).withAnnotations(Seq(WriteVcdAnnotation)) { dut => {
      implicit val clk = dut.clock

      val imm = collection.mutable.Map.empty[BigInt, Byte]
      val dmm = collection.mutable.Map.empty[BigInt, Byte]
      val imageBytes = getBytes(genConfig.testCase)
      for (i <- imageBytes.indices) imm(i + BigInt("80000000", 16) - BigInt("1000", 16)) = imageBytes(i)

      case class ImmAgent(memory: collection.mutable.Map[BigInt, Byte]) extends MemoryAgent(dut.io.imm)
      case class DmmAgent(memory: collection.mutable.Map[BigInt, Byte]) extends MemoryAgent(dut.io.dmm)

      fork {
        ImmAgent(imm).run()
      }

      fork {
        DmmAgent(dmm).run()
      }

      fork {
        var finish = false
        while (!finish) {
          val pc = dut.io.imm.req.bits.addr.peek().litValue()
          if (pc.toInt == Config.failPc) { assert(false, "FAIL!!!"); finish = true }
          if (pc.toInt == Config.passPc) { finish = true }
          clk.step()
          Config.cycles += 1
        }
      }.join()
    }}
  }
}
