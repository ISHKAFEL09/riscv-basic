package COD

import chisel3._

class DecodeIO extends Bundle {
  val pcSrc = UInt(ctrlSize)
  val aluSrc1 = UInt(ctrlSize)
  val aluSrc2 = UInt(ctrlSize)
  val aluOp = UInt(ctrlSize)
  val rfWen = Bool()
  val memWen = Bool()
  val memRen = Bool()
  val wbSrc = UInt(ctrlSize)
  val brType = UInt(ctrlSize)
  val isCsr = Bool()
  val immType = UInt(ctrlSize)
}

//class HazardIO extends Bundle {}

class CtrlIO extends Bundle {
  val valid = Input(Bool())
  val instr = Input(UInt(32.W))
  val branchEval = Input(Bool())

  val decode = Output(new DecodeIO())
  val forward = new ForwardIO
//  val hazard = new HazardIO
  val ifFlush = Output(Bool())
  val ifStall = Output(Bool())
  val idStall = Output(Bool())
  val idFlush = Output(Bool())
}

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
