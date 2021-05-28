package cod

import SimLib._
import chisel3._
import chiseltest._
import chiseltest.experimental.TestOptionBuilder.ChiselScalatestOptionBuilder
import chiseltest.internal.WriteVcdAnnotation
import cod.Interfaces._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io._

case class MemoryPkg(wr: Boolean, addr: BigInt, data: BigInt) extends Package {
  override def toString: String = f" ${if (wr) "write" else "read"} addr: $addr%x, data: $data%x "
}

abstract class MemoryAgent(val bus: MemoryIO)(implicit val clk: Clock) {
  def memory: collection.mutable.Map[BigInt, Byte]
  private def req = bus.req.bits
  private def resp = bus.resp.bits
  def run(): Unit = {
    while (true) {
      bus.resp.valid.poke(false.B)
      clk.waitSampling(bus.req.valid)
      bus.resp.valid.poke(true.B)
      val addr = req.addr.peek().litValue()// >> 2 << 2
      if (req.wr.peek().litToBoolean) {
        for {
          i <- 0 until genConfig.xprlen / 8
          if req.mask(i).peek().litToBoolean
        } {
          memory(addr + i) = (req.wdata.peek().litValue() >> (8 * i)).toByte
        }
      } else {
        var rdata: BigInt = 0
        if (memory.contains(addr)) {
          for (i <- (0 until genConfig.xprlen / 8).reverse) {
            val m: BigInt = memory(addr + i)
            rdata = (rdata << 8) + (if (m >= 0) m else m + 256)
          }
        } else {
          rdata = BigInt(Const.BUBBLE.dropWhile(_ > '1'), 2)
        }
        resp.rdata.poke(rdata.U)
      }
      clk.step()
    }
  }
}

class RiscvTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "Riscv Test"

  object Statics {
    var cycles = 0
    var validCycles = 0
    var finished = 0

    override def toString: String = f"All test finished, total $cycles cycles, valid $validCycles, " +
      f"cpi is ${cycles.toFloat / validCycles}"
  }

  def getBytes(f: String): Seq[Byte] = {
    val file = new File(f)
    val in = new FileInputStream(file)
    val bytes = new Array[Byte](file.length.toInt)
    in.read(bytes)
    bytes
  }

  def regressTest(config: GenConfig): Unit = {
    genConfig = config

    val testCases: Array[String] = for {
      f <- new File("tests/isa").listFiles()
      testCasePattern = config.testPattern.findFirstIn(f.getPath)
      if testCasePattern.isDefined
      testCase = testCasePattern.get
      //    if testCase contains "rv64mi-p-csr"
    } yield testCase

    for (testCase <- testCases) {
      it should s"pass riscv $testCase" in {
        test(Tile()).withAnnotations(Seq(WriteVcdAnnotation)) { dut => {
          implicit val clk: Clock = dut.clock

          val imm = collection.mutable.Map.empty[BigInt, Byte]
          val dmm = collection.mutable.Map.empty[BigInt, Byte]
          val imageBytes = getBytes(testCase)
          for (i <- imageBytes.indices) {
            imm(i + BigInt("80000000", 16) - BigInt("1000", 16)) = imageBytes(i)
            dmm(i + BigInt("80000000", 16) - BigInt("1000", 16)) = imageBytes(i)
          }

          case class ImmAgent(memory: collection.mutable.Map[BigInt, Byte]) extends MemoryAgent(dut.io.imm)
          case class DmmAgent(memory: collection.mutable.Map[BigInt, Byte]) extends MemoryAgent(dut.io.dmm)

          fork {
            ImmAgent(imm).run()
          }

          fork {
            DmmAgent(dmm).run()
          }

          fork {
            while (true) {
              if (dut.io.dmm.req.bits.fence.peek().litToBoolean) {
                dmm foreach (i => imm(i._1) = i._2)
              }
              clk.step()
            }
          }

          fork {
            var finish = false
            var cycles = 1
            while (!finish) {
              clk.step()
              val pc = dut.io.imm.req.bits.addr.peek().litValue()
              if (pc.toInt == genConfig.SystemCallAddress.toInt) {
                finish = true
              }
              cycles += 1
              Statics.cycles += 1
              assert(cycles < 1000, "Timeout!!!")
            }
            dut.io.debug.debugRf.rfIndex.poke(3.U)
            clk.step(3)
            val gp = dut.io.debug.debugRf.rfData.peek().litValue()
            Statics.validCycles += dut.io.debug.status.instrRetire.peek().litValue().toInt
            dut.reset.poke(true.B)
            clk.step()
            assert(gp == 1, f"gp is $gp, error code is ${gp >> 1}")
            assert(cycles > 60)
            println(f"finish test in $cycles cycles")
            Statics.finished += 1
            if (Statics.finished == testCases.length) {
              println(Statics)
            }
          }.join()
        }
        }
      }
    }
  }

//  regressTest(Rv32uiConfig())
//  regressTest(Rv64uiConfig())
  regressTest(Rv64miConfig())
}
