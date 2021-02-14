package COD

import chisel3._

class DecodeIO extends Bundle {
  val pcSrc = UInt(ctrlSize)
  val aluSrc = UInt(ctrlSize)
  val aluOp = UInt(ctrlSize)
  val regWr = Bool()
  val memWr = Bool()
  val memRd = Bool()
  val regSrc = UInt(ctrlSize)
  val brSrc = UInt(ctrlSize)
}

//class HazardIO extends Bundle {}

class CtrlIO extends Bundle {
  val instr = Input(UInt(xprWidth))
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

  val forwardUnit = new ForwardUnit
  forwardUnit.io.f2wb <> io.forward.f2wb
  forwardUnit.io.f2mem <> io.forward.f2mem
  forwardUnit.io.f2id <> io.forward.f2id
}
