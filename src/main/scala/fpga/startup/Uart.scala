package fpga.startup

import fpga.FpgaBasic
import chisel3._
import chisel3.util._

class UartTx(baud: Int) extends Module {
  val io = IO(new Bundle() {
    val txData = Flipped(DecoupledIO(UInt(8.W)))
    val txPin = Output(Bool())
    val busy = Output(Bool())
  })

  val sampleEnable = Wire(Bool())
  val (_, sample) = Counter(sampleEnable, 50 * 1000000 / baud)

  val idle :: start :: send :: stop :: _ = Enum(6)
  val state = RegInit(idle)
  val (_, done) = Counter(sample && state === send, 8)
  switch (state) {
    is (idle) {
      when (io.txData.fire()) { state := start }
    }
    is (start) {
      when (sample) { state := send }
    }
    is (send) {
      when (done) { state := stop }
    }
    is (stop) {
      when (sample) { state := idle }
    }
  }

  val sendData = RegEnable(io.txData.bits, io.txData.fire())
  when (state === send && sample) { sendData := Cat(0.U, sendData(7, 1))}
  io.txData.ready := state === idle
  sampleEnable := state =/= idle
  io.txPin := MuxCase(true.B, Seq(
    (state === idle) -> true.B,
    (state === start) -> false.B,
    (state === send) -> sendData(0),
    (state === stop) -> true.B
  ))
  io.busy := state =/= idle
}

class UartRx(baud: Int) extends Module {
  val io = IO(new Bundle() {
    val rxPin = Input(Bool())
    val rxData = DecoupledIO(UInt(8.W))
    val busy = Output(Bool())
  })

  val cntVal = 50 * 1000000 / baud

  val cntHalfEnable = Wire(Bool())
  val (_, cntHalf) = Counter(cntHalfEnable, cntVal / 2)
  val cntSampleEnable = Wire(Bool())
  val (_, cntSample) = Counter(cntSampleEnable, cntVal)
  val cntReceiveEnable = Wire(Bool())
  val (_, cntReceive) = Counter(cntReceiveEnable, 8)

  val idle :: start :: receive :: stop :: _ = Enum(6)
  val stateReg = RegInit(idle)
  switch (stateReg) {
    is (idle) {
      when (cntHalf) { stateReg := start }
    }
    is (start) {
      when (cntSample) { stateReg := receive }
    }
    is (receive) {
      when (cntReceive) { stateReg := idle }
    }
    is (stop) {
      when (cntSample) { stateReg := idle }
    }
  }

  cntHalfEnable := stateReg === idle && !io.rxPin
  cntSampleEnable := stateReg =/= idle
  cntReceiveEnable := stateReg === receive && cntSample

  val receiveReg = Reg(UInt(8.W))
  when (stateReg === idle) {
    receiveReg := 0.U
  }.elsewhen(cntSample) {
    receiveReg := Cat(io.rxPin, receiveReg(7, 1))
  }
  io.rxData.bits := receiveReg
  io.rxData.valid := cntReceive
  io.busy := stateReg =/= idle
}

class Uart extends FpgaBasic {
  val txPin = IO(Output(Bool()))
  val rxPin = IO(Input(Bool()))
  val led = IO(Output(UInt(4.W)))

  withClockAndReset(clock, reset_n) {
    val uartTx = Module(new UartTx(115200))
    val uartRx = Module(new UartRx(115200))

    uartTx.io.txPin <> txPin
    uartRx.io.rxPin <> rxPin

    uartTx.io.txData <> uartRx.io.rxData

    led := Fill(4, !(uartRx.io.busy || uartTx.io.busy))

    debug(uartTx.state)
    debug(uartTx.sample)
    debug(uartTx.done)
    debug(uartTx.sendData)
    debug(uartTx.io.txPin)
    debug(uartRx.stateReg)
    debug(uartRx.cntSample)
    debug(uartRx.cntHalf)
    debug(uartRx.cntReceive)
    debug(uartRx.receiveReg)
    debug(uartRx.io.rxPin)
    debug(uartRx.io.rxData.valid)
    debug(uartRx.io.rxData.ready)
    debug(uartRx.io.rxData.bits)
  }
}

object Uart extends App {
  generate(new Uart)
}
