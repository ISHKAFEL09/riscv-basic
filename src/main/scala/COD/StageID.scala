package cod

import chisel3._
import chisel3.experimental.chiselName
import Interfaces._

@chiselName
class StageID(implicit conf: GenConfig) extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(new IfPipeIO)
    val ctrl = new IdCtrlIO
    val misc = new IdMiscIO
    val pipe = new IdPipeIO
  })

  io.ctrl.valid := io.lastPipe.valid

  // registers file
  val regFile = Module(new Registers())

  // get srd\des reg from If stage
  val instruction = io.lastPipe.instr
  val rs1 = instruction(19, 15)
  val rs2 = instruction(24, 20)
  val rd = instruction(11, 7)

  // read/write registers file
  val rs1Data = regFile.io.dataRs1
  val rs2Data = regFile.io.dataRs2
  regFile.io.rs1 := rs1
  regFile.io.rs2 := rs2
  regFile.io.rd := io.misc.wbAddr
  regFile.io.dataRd := io.misc.wbData
  regFile.io.wren := io.misc.wbEn

  // ctrl signals
  io.ctrl.instr := instruction
  val decode = io.ctrl.decode

  // forward unit
  io.ctrl.rs1Data := rs1Data
  io.ctrl.rs2Data := rs2Data
  val aluOp1 = io.ctrl.fwdRs1
  val aluOp2 = io.ctrl.fwdRs2

  // pipe regs
  val regPipe = RegInit(0.U.asTypeOf(IdPipeIO()))
  when (io.ctrl.flush) {
    regPipe := 0.U.asTypeOf(IdPipeIO())
  } .elsewhen(io.ctrl.stall || io.ctrl.fullStall) {
    regPipe.decode := 0.U.asTypeOf(new DecodeIO)
    regPipe.valid := false.B
  } .otherwise {
    regPipe.instr := instruction
    regPipe.pc := io.lastPipe.pc
    regPipe.aluOp1 := aluOp1
    regPipe.aluOp2 := aluOp2
    regPipe.decode := decode
    regPipe.valid := io.lastPipe.valid
  }

  // pipeline io assign
  io.pipe := regPipe
}

object StageID extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageID(), Array("--target-dir", "generated"))
}
