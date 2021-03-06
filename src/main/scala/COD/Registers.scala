package cod

import chisel3._

class RegFileIo(implicit conf: GenConfig) extends Bundle {
  val regWidth = conf.xprlen.W
  val rs1 = Input(UInt(5.W))
  val rs2 = Input(UInt(5.W))
  val rd = Input(UInt(5.W))
  val dataRs1 = Output(UInt(regWidth))
  val dataRs2 = Output(UInt(regWidth))
  val dataRd = Input(UInt(regWidth))
  val wren = Input(Bool())
  override def cloneType = { new RegFileIo().asInstanceOf[this.type] }
}

class Registers(implicit conf: GenConfig) extends Module {
  val regWidth = conf.xprlen.W
  val io = IO(new RegFileIo())
  val regFile = Mem(conf.nxpr, UInt(regWidth))

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
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new Registers(), Array("--target-dir", "generated"))
}