package COD

import chisel3._
import chisel3.experimental.chiselName

class IDMiscIO extends Bundle {
  val wbAddr = Input(UInt(xprWidth))
  val wbData = Input(UInt(xprWidth))
  val wbEn = Input(Bool())
  val pcJump = Output(UInt(xprWidth))
  val stall = Input(Bool())
  val flush = Input(Bool())
}

class IDPipeIO extends Bundle {
  val pc = Output(UInt(xprWidth))
  val instr = Output(UInt(32.W))
  val aluOp1 = Output(UInt(xprWidth))
  val aluOp2 = Output(UInt(xprWidth))
}

class IDCtrlIO extends Bundle {
  val instr = Output(UInt(32.W))
  val branchEval = Output(Bool())
  val decode = Input(new DecodeIO)
  val forward = Flipped(new Forward2IDIO)
}

@chiselName
class StageID(implicit conf: GenConfig) extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(new IFPipeIO)
    val ctrl = new IDCtrlIO
    val misc = new IDMiscIO
    val pipe = new IDPipeIO
    val pipeCtrl = Output(new DecodeIO)
  })
  io := DontCare

  val valid = io.lastPipe.valid

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

  // pipe regs: ctrl
  val stall = io.misc.stall
  val flush = io.misc.flush
  val regPipeCtrl = RegInit(0.U.asTypeOf(new DecodeIO()))
  when (stall | flush) {
    regPipeCtrl := 0.U.asTypeOf(new DecodeIO())
  } otherwise {
    regPipeCtrl := decode
  }

  // forward unit
  forward.instr := instruction
  forward.rs1Data := rs1Data
  forward.rs2Data := rs2Data
  val aluOp1 = forward.aluOp1
  val aluOp2 = forward.aluOp2

  // pipe regs: data
  val regPipe = RegInit(0.U.asTypeOf(new IDPipeIO()))
  when (flush) {
    regPipe := 0.U.asTypeOf(new IDPipeIO())
    regPipe.instr := Const.Instruction.BUBBLE
  } .elsewhen (!stall) {
    regPipe.instr := instruction
    regPipe.pc := io.lastPipe.pc
    regPipe.aluOp1 := aluOp1
    regPipe.aluOp2 := aluOp2
  }

  // pipeline io assign
  io.pipeCtrl := regPipeCtrl
  io.pipe := regPipe
}

object StageID extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageID(), Array("--target-dir", "generated"))
}
