package fpga.startup

import fpga.FpgaBasic
import chisel3._
import chisel3.util._

class LedFlash extends FpgaBasic {
  val dout = IO(Output(UInt(4.W)))

  withClockAndReset(clock, reset_n) {
    val shiftReg = RegInit(1.U(4.W))
    val (_, done) = Counter(true.B, 50 * 1000000)
    when(done) {
      shiftReg := Cat(shiftReg(2, 0), shiftReg(3))
    }

    dout := shiftReg
  }
}

object LedFlash extends App {
  generate(new LedFlash)
}
