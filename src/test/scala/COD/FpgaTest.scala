package cod

import chisel3._
import chiseltest._
import chiseltest.experimental.TestOptionBuilder._
import chiseltest.internal.WriteVcdAnnotation
import cod.Interfaces._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import fpga.FpgaBasic

import scala.util.Random

class FpgaTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "Fpga Test"
  it should s"pass Fpga Test" in {
    test(new FpgaBasic()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      fork {
        1 to 100 foreach { i =>
          dut.din.poke(new Random().nextBoolean().asBool())
          dut.clock.step()
          dut.clock2.poke(((i % 2) == 0).asBool())
        }
      }.join()
    }
  }
}
