package cod

import chisel3._
import Interfaces._

case class Debug() extends Module {
  val io = IO(DebugIO())

  io.d2rf.rfIndex := io.debugRf.rfIndex
  io.debugRf.rfData := io.d2rf.rfIndex
}
