package cod

import chisel3._
import Interfaces._

class Registers() extends Module {
  val regWidth = xprWidth
  val io = IO(new RegFileIo())
  val regFile = Mem(32, UInt(regWidth))

  io.dataRs1 := 0.U
  io.dataRs2 := 0.U
  when (io.rs1 =/= 0.U) {
    io.dataRs1 := regFile(io.rs1)
  }
  when (io.rs2 =/= 0.U) {
    io.dataRs2 := regFile(io.rs2)
  }

  when (io.wren && io.rd =/= 0.U) {
    regFile(io.rd) := io.dataRd
  }
}

object Registers extends App {
  new stage.ChiselStage().emitVerilog(new Registers(), Array("--target-dir", "generated"))
}