package fpga.startup

import chisel3._
import chisel3.util._
import fpga.FpgaBasic

class Debounce(last: BigInt) extends FpgaBasic {
  val dataIn = IO(Input(Bool()))
  val dataOut = IO(Output(Bool()))
  val dataPos = IO(Output(Bool()))
  val dataNeg = IO(Output(Bool()))

  withClockAndReset(clock, reset_n) {
    val width = log2Up(last)
    println(width)
    val dataInReg = RegNext(dataIn)
    val diff = dataIn =/= dataInReg
    val cntReg = RegInit(0.U(width.W))
    when(diff) {
      cntReg := 0.U
    }.elsewhen(cntReg =/= last.U) {
      cntReg := cntReg + 1.U
    }

    val dataReg = RegEnable(dataIn, !diff && cntReg === last.U)

    dataOut := dataReg
    dataPos := dataReg && !RegNext(dataReg)
    dataNeg := !dataReg && RegNext(dataReg)

    debug(dataIn)
    debug(dataOut)
    debug(dataPos)
    debug(dataNeg)
    debug(cntReg)
  }
}

object Debounce extends App {
  generate(new Debounce(1000))
}
