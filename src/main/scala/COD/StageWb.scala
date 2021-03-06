package cod

import chisel3._
import chisel3.experimental.chiselName
import chisel3.util._
import cod.Interfaces._
import Const._

@chiselName
case class StageWb() extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(MemPipeIO())
    val ctrl = WbCtrlIO()
    val misc = WbMiscIO()
  })

  io.ctrl.exception := false.B

  val rdData = MuxLookup(io.ctrl.decode.wbSrc, io.lastPipe.aluOut, Seq(
    WbSrc.mem -> io.lastPipe.memRdata
  ))

  io.ctrl.decode := io.lastPipe.decode
  io.ctrl.rdData := rdData
  io.ctrl.instr := io.lastPipe.instr

  val rfWrite = io.misc.rfWrite
  rfWrite.enable := io.ctrl.decode.rfWen
  rfWrite.addr := io.lastPipe.instr(11, 7)
  rfWrite.data := rdData

  val cycles = RegInit(0.U(xprWidth))

  when (io.lastPipe.decode.valid && io.lastPipe.pc =/= BUBBLE.U) {
    cycles := cycles + 1.U
    rtlDebug("[Stage Wb] cycle: %d, pc: %x, instr: %x\n", cycles, io.lastPipe.pc, io.lastPipe.instr)
  }
  io.misc.cycles := cycles

  io.misc.csrWrite <> io.lastPipe.csrWrite
}

object StageWb extends App {
  generate(StageWb())
}




