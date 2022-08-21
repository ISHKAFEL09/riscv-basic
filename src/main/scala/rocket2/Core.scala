package rocket2

import chisel3._

class HostIO extends Bundle {
  val start = Input(Bool())
  val fromWen = Input(Bool())
  val from = Input(UInt(32.W))
  val to = Output(UInt(32.W))
}

class DebugIO extends Bundle {
  val errorMode = Output(Bool())
  val logCtrl = Output(Bool())
}

class Core {

}
