package rocket2

import chisel3._

class DataMiscIO extends Bundle {
  val bitHit = Output(Bool())
  val inst = Output(UInt(32.W))
  val rs1 = Output(UInt(32.W))
  val rs2 = Output(UInt(32.W))
  val brEq = Output(Bool())
  val brLt = Output(Bool())
  val brLtu = Output(Bool())
  val divResultVal = Output(Bool())
  val divRdy = Output(Bool())
  val mulResultVal = Output(Bool())
  val wen = Output(Bool())
  val aluOut = Output(UInt(64.W))
  val exception = Output(Bool())
  val status = Output(UInt(8.W))
}

class Data2IMemIO extends Bundle {
  val reqAddr = Output(UInt(32.W))
  val respData = Input(UInt(32.W))
}

class DataPathIO extends Bundle {
  val misc = new DataMiscIO
  val host = new HostIO
  val ctrl = Flipped(new Ctrl2DataIO)
  val debug = new DebugIO
  val wb = new WB2DataIO
  val iMem = new Data2IMemIO
}

class DataPath extends Module {
  val io = IO(new DataPathIO)


}
