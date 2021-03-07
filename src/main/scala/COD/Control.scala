package cod

import chisel3._
import Interfaces._

class Control extends Module {
  val io = IO(new CtrlIO)

  io := DontCare

  val forwardUnit = Module(new ForwardUnit)
  forwardUnit.io.f2wb <> io.forward.f2wb
  forwardUnit.io.f2mem <> io.forward.f2mem
  forwardUnit.io.f2id <> io.forward.f2id

  val decoder = Module(new Decoder)
  decoder.io.valid <> io.valid
  decoder.io.instr <> io.instr
  decoder.io.branchEval <> io.branchEval
  decoder.io.decode <> io.decode
}
