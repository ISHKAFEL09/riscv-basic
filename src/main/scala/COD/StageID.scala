package cod

import chisel3._
import chisel3.experimental.chiselName
import Interfaces._

@chiselName
class StageID(implicit conf: GenConfig) extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(new IFPipeIO)
    val ctrl = new IDCtrlIO
    val misc = new IDMiscIO
    val pipe = new IDPipeIO
  })

  io.ctrl.valid := io.lastPipe.valid

  // registers file
  val regFile = Module(new Registers())

  // get srd\des reg from IF stage
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
  val ctrl = io.ctrl
  ctrl.instr := instruction
  val decode = io.ctrl.decode
  val forward = io.ctrl.forward

  // forward unit
  forward.instr := instruction
  forward.rs1Data := rs1Data
  forward.rs2Data := rs2Data
  val aluOp1 = forward.aluOp1
  val aluOp2 = forward.aluOp2

  // pipe regs: data
  val regPipe = RegInit(0.U.asTypeOf(new IDPipeIO()))
  val stall = io.ctrl.stall
  when (stall) {
    regPipe.control := 0.U.asTypeOf(new DecodeIO)
  } .otherwise {
    regPipe.instr := instruction
    regPipe.pc := io.lastPipe.pc
    regPipe.aluOp1 := aluOp1
    regPipe.aluOp2 := aluOp2
    regPipe.control := decode
  }

  // pipeline io assign
  io.pipe := regPipe
}

object StageID extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageID(), Array("--target-dir", "generated"))
}
