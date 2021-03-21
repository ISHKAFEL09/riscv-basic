package cod

import chisel3._
import chisel3.util._
import chisel3.experimental.chiselName
import Interfaces._
import chisel3.util.MuxLookup
import Const._

@chiselName
case class StageID() extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(new IfPipeIO)
    val ctrl = new IdCtrlIO
    val pipe = new IdPipeIO
    val misc = new IdMiscIO
  })

  io.ctrl.exception := false.B

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

  // immediate
  val immData = MuxLookup(io.ctrl.decode.immType, 0.U, Seq(
    ImmType.typeI -> Cat(Fill(21, instruction(31)), instruction(30, 20)),
    ImmType.typeS -> Cat(Fill(21, instruction(31)), instruction(30, 25), instruction(11, 7)),
    ImmType.typeB -> Cat(Fill(20, instruction(31)), instruction(7), instruction(30, 25), instruction(11, 8), false.B),
    ImmType.typeU -> Cat(instruction(31, 12), 0.U(12.W)),
    ImmType.typeJ -> Cat(Fill(12, instruction(31)), instruction(19, 12), instruction(20), instruction(30, 21), false.B)
  ))

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
    AluSrc.pc -> (io.lastPipe.pc + 4.U),
    AluSrc.csr -> io.misc.csr.resp.rd
  ))

  // branch check
  val taken = MuxLookup(io.ctrl.decode.brType, false.B, Seq(
    BranchSel.jump -> true.B,
    BranchSel.beq -> (aluOp1 === aluOp2),
    BranchSel.bne -> (aluOp1 =/= aluOp2),
    BranchSel.bge -> (aluOp1.asSInt() >= aluOp2.asSInt()),
    BranchSel.bgeu -> (aluOp1 >= aluOp2),
    BranchSel.blt -> (aluOp1.asSInt() < aluOp2.asSInt()),
    BranchSel.bltu -> (aluOp1 < aluOp2)
  ))
  val branchPc = MuxLookup(io.ctrl.decode.brTarget, io.lastPipe.pc + 4.U, Seq(
    BranchTarget.jal -> (io.lastPipe.pc + immData),
    BranchTarget.branch -> (io.lastPipe.pc + immData),
    BranchTarget.jalr -> Cat((aluOp1 + immData)(xprWidth.get - 1, 1), false.B)
  ))
  io.ctrl.branchErr := taken =/= io.lastPipe.taken
  io.misc.branchCheck.update := io.ctrl.branchErr
  io.misc.branchCheck.taken := taken
  io.misc.branchCheck.pcIndex := io.lastPipe.pc(btbWidth.get - 1, 0)
  io.misc.branchCheck.pcBranch := branchPc

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

  // csr
  val csr = io.misc.csr
  csr.req.valid := io.ctrl.decode.isCsr
  csr.req.bits.csr := instruction(31, 20)
  csr.req.bits.imm := instruction(19, 15)
  csr.req.bits.rs := aluOp1
  csr.req.bits.cmd := instruction(14, 12)

  io.ctrl.instr := io.lastPipe.instr
  // pipeline io assign
  io.pipe := regPipe
}

object StageID extends App {
  new stage.ChiselStage().emitVerilog(new StageID(), Array("--target-dir", "generated"))
}
