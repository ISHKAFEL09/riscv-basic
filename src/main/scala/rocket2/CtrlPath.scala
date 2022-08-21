package rocket2

import chisel3._

class Ctrl2DataIO extends Bundle {
  val selPC = Output(UInt(3.W))
  val wenBtb = Output(Bool())
  val stallFetch = Output(Bool())
  val stallDec = Output(Bool())
  val killFetch = Output(Bool())
  val killDec = Output(Bool())
  val ren1 = Output(Bool())
  val ren2 = Output(Bool())
  val selAlu1 = Output(Bool())
  val selAlu2 = Output(UInt(2.W))
  val aluDW = Output(UInt(1.W))
  val aluFun = Output(UInt(4.W))
  val mulVal = Output(Bool())
  val mulWb = Output(Bool())
  val mulFun = Output(UInt(3.W))
  val divVal = Output(Bool())
  val divWb = Output(Bool())
  val divFun = Output(UInt(4.W))
  val wen = Output(Bool())
  val selWa = Output(Bool())
  val selWb = Output(UInt(3.W))
  val renPcr = Output(Bool())
  val wenPcr = Output(Bool())
  val xcptIllegal = Output(Bool())
  val xcptFPU = Output(Bool())
  val xcptSysCall = Output(Bool())
  val eret = Output(Bool())
}

class CtrlPath {

}
