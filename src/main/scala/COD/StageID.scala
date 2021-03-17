package cod

import chisel3._
import chisel3.experimental.chiselName
import Interfaces._
import chisel3.util.MuxLookup
import Const._

@chiselName
class StageID(implicit conf: GenConfig) extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(new IfPipeIO)
    val ctrl = new IdCtrlIO
    val pipe = new IdPipeIO
    val misc = new IdMiscIO
  })

  // registers file
  val regFile = Module(new Registers())
  val instruction = io.lastPipe.instr
  val rs1 = instruction(19, 15)
  val rs2 = instruction(24, 20)
  val rd = instruction(11, 7)
  val rs1Data = regFile.io.dataRs1
  val rs2Data = regFile.io.dataRs2
  regFile.io.rs1 := rs1
  regFile.io.rs2 := rs2
  regFile.io.wren := io.misc.rf.enable
  regFile.io.rd := io.misc.rf.addr
  regFile.io.dataRd := io.misc.rf.data

  val immData = Wire(UInt(xprWidth))
  immData := 0.U

  // ALU OP
  io.ctrl.rs1Data := rs1Data
  io.ctrl.rs2Data := rs2Data
  val aluOp1 = MuxLookup(io.ctrl.decode.aluSrc1, io.ctrl.fwdRs1, Seq(
    AluSrc.rf -> io.ctrl.fwdRs1,
    AluSrc.pc -> io.lastPipe.pc
  ))
  val aluOp2 = MuxLookup(io.ctrl.decode.aluSrc2, io.ctrl.fwdRs2, Seq(
    AluSrc.rf -> io.ctrl.fwdRs2,
    AluSrc.imm -> immData,
    AluSrc.pc -> io.lastPipe.pc,
    AluSrc.csr -> io.misc.csr.resp.rd
  ))

  // pipe regs
  val regPipe = RegInit(0.U.asTypeOf(IdPipeIO()))
  when (!io.ctrl.stall) {
    when (io.ctrl.flush) {
      regPipe := 0.U.asTypeOf(IdPipeIO())
    } otherwise {
      regPipe.instr := instruction
      regPipe.pc := io.lastPipe.pc
      regPipe.aluOp1 := aluOp1
      regPipe.aluOp2 := aluOp2
      regPipe.decode := io.ctrl.decode
      regPipe.memWdata := io.ctrl.fwdRs2
    }
  } otherwise {
    regPipe.decode := 0.U.asTypeOf(new DecodeIO)
  }

  io.ctrl.instr := io.lastPipe.instr
  // pipeline io assign
  io.pipe := regPipe
}

object StageID extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageID(), Array("--target-dir", "generated"))
}
