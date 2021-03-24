package cod

import chisel3._
import chisel3.util._
import Const._
import chisel3.experimental.chiselName
import Interfaces._

@chiselName
case class Decoder() extends Module {
  val io = IO(new Bundle() {
    val valid = Input(Bool())
    val instr = Input(UInt(32.W))
    val decode = Output(new DecodeIO)
  })

  io.decode.valid := io.valid

  val decLevel0 = ListLookup(io.instr,
      //   instrType        aluOpType      flag
      List(InstrType.typeN, AluOpType.add, InstrFlag.isBranch),
    Array(
      Instruction.BUBBLE -> List(InstrType.typeR, AluOpType.add, InstrFlag.nop),

      Instruction.FENCE -> List(InstrType.typeR, AluOpType.add, InstrFlag.isFence),
      Instruction.FENCE_I -> List(InstrType.typeR, AluOpType.add, InstrFlag.isFence),

      Instruction.ADDI -> List(InstrType.typeI, AluOpType.add, InstrFlag.nop),
      Instruction.SLTI -> List(InstrType.typeI, AluOpType.comp, InstrFlag.nop),
      Instruction.SLTIU -> List(InstrType.typeI, AluOpType.compu, InstrFlag.nop),
      Instruction.ANDI -> List(InstrType.typeI, AluOpType.and, InstrFlag.nop),
      Instruction.ORI -> List(InstrType.typeI, AluOpType.or, InstrFlag.nop),
      Instruction.XORI -> List(InstrType.typeI, AluOpType.xor, InstrFlag.nop),

      Instruction.SLLI -> List(InstrType.typeI, AluOpType.lshift, InstrFlag.nop),
      Instruction.SRLI -> List(InstrType.typeI, AluOpType.rshift, InstrFlag.nop),
      Instruction.SRAI -> List(InstrType.typeI, AluOpType.rshifta, InstrFlag.nop),

      Instruction.LUI -> List(InstrType.typeU, AluOpType.bypass2, InstrFlag.nop),
      Instruction.AUIPC -> List(InstrType.typeU, AluOpType.bypass2, InstrFlag.nop),

      Instruction.ADD -> List(InstrType.typeR, AluOpType.add, InstrFlag.nop),
      Instruction.SLT -> List(InstrType.typeR, AluOpType.comp, InstrFlag.nop),
      Instruction.SLTU -> List(InstrType.typeR, AluOpType.compu, InstrFlag.nop),
      Instruction.AND -> List(InstrType.typeR, AluOpType.and, InstrFlag.nop),
      Instruction.OR -> List(InstrType.typeR, AluOpType.or, InstrFlag.nop),
      Instruction.XOR -> List(InstrType.typeR, AluOpType.xor, InstrFlag.nop),
      Instruction.SLL -> List(InstrType.typeR, AluOpType.lshift, InstrFlag.nop),
      Instruction.SRL -> List(InstrType.typeR, AluOpType.rshift, InstrFlag.nop),
      Instruction.SUB -> List(InstrType.typeR, AluOpType.sub, InstrFlag.nop),
      Instruction.SRA -> List(InstrType.typeR, AluOpType.rshifta, InstrFlag.nop),

      Instruction.JAL -> List(InstrType.typeJ, AluOpType.bypass2, InstrFlag.isJump),
      Instruction.JALR -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isJump),

      Instruction.BEQ -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),
      Instruction.BNE -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),
      Instruction.BLT -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),
      Instruction.BLTU -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),
      Instruction.BGE -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),
      Instruction.BGEU -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),

      Instruction.LW -> List(InstrType.typeI, AluOpType.add, InstrFlag.isLoad),
      Instruction.SW -> List(InstrType.typeS, AluOpType.add, InstrFlag.isStore),
      Instruction.LH -> List(InstrType.typeI, AluOpType.add, InstrFlag.isLoad | InstrFlag.isHalfWord),
      Instruction.SH -> List(InstrType.typeS, AluOpType.add, InstrFlag.isStore | InstrFlag.isHalfWord),
      Instruction.LB -> List(InstrType.typeI, AluOpType.add, InstrFlag.isLoad | InstrFlag.isByte),
      Instruction.SB -> List(InstrType.typeS, AluOpType.add, InstrFlag.isStore | InstrFlag.isByte),
      Instruction.LHU -> List(InstrType.typeI, AluOpType.add, InstrFlag.isLoad | InstrFlag.isHalfWord | InstrFlag.isUnsigned),
      Instruction.LBU -> List(InstrType.typeI, AluOpType.add, InstrFlag.isLoad | InstrFlag.isByte | InstrFlag.isUnsigned),

      Instruction.ECALL -> List(InstrType.typeI, AluOpType.nop, InstrFlag.isSystem),
      Instruction.EBREAK -> List(InstrType.typeI, AluOpType.nop, InstrFlag.notReady),
      Instruction.MRET -> List(InstrType.typeI, AluOpType.nop, InstrFlag.notReady),

      Instruction.CSRRW -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isCsr),
      Instruction.CSRRS -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isCsr),
      Instruction.CSRRC -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isCsr),
      Instruction.CSRRWI -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isCsr),
      Instruction.CSRRSI -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isCsr),
      Instruction.CSRRCI -> List(InstrType.typeI, AluOpType.bypass2, InstrFlag.isCsr)
    )
  )

  val instrType :: aluOpType :: instrFlag :: Nil = decLevel0
//  rtlDebug("[Decoder] valid: %x, instr: %x, type: %x, aluOpType: %x, instrFlag: %x\n",
//    io.valid, io.instr, instrType, aluOpType, instrFlag)
  when (io.valid) { assert(instrType =/= InstrType.typeN, "Unknown instr %x", io.instr) }

  def hasFlag(flag: UInt): Bool = {
    (instrFlag & flag).orR()
  }

//  io.decode.pcSrc := MuxCase(PCSel.plus4, Seq(
//    (io.branchEval && hasFlag(InstrFlag.isBranch)) -> PCSel.jump
//  ))

  io.decode.brTarget := MuxCase(BranchTarget.nop, Seq(
    (io.instr === Instruction.JAL) -> BranchTarget.jal,
    (io.instr === Instruction.JALR) -> BranchTarget.jalr,
    hasFlag(InstrFlag.isBranch) -> BranchTarget.branch
  ))

  io.decode.aluOp := aluOpType

  io.decode.brType := MuxCase(BranchSel.nop, Seq(
    hasFlag(InstrFlag.isJump) -> BranchSel.jump,
    (io.instr === Instruction.BEQ) -> BranchSel.beq,
    (io.instr === Instruction.BNE) -> BranchSel.bne,
    (io.instr === Instruction.BLT) -> BranchSel.blt,
    (io.instr === Instruction.BLTU) -> BranchSel.bltu,
    (io.instr === Instruction.BGE) -> BranchSel.bge,
    (io.instr === Instruction.BGEU) -> BranchSel.bgeu
  ))

  io.decode.memRen := hasFlag(InstrFlag.isLoad)
  io.decode.memWen := hasFlag(InstrFlag.isStore)

  io.decode.rfWen := !(instrType === InstrType.typeB || instrType === InstrType.typeS)

  val noUseRs1 = instrType === InstrType.typeU || instrType === InstrType.typeJ
  io.decode.aluSrc1 := Mux(noUseRs1, AluSrc.nop, AluSrc.rf)
  io.decode.aluSrc2 := MuxCase(AluSrc.rf, Seq(
    hasFlag(InstrFlag.isStore) -> AluSrc.imm,
    hasFlag(InstrFlag.isJump) -> AluSrc.pc,
    hasFlag(InstrFlag.isCsr) -> AluSrc.csr,
    (instrType === InstrType.typeI) -> AluSrc.imm,
    (instrType === InstrType.typeU) -> AluSrc.imm,
    (instrType === InstrType.typeJ) -> AluSrc.imm
  ))

  io.decode.wbSrc := Mux(io.decode.memRen, WbSrc.mem, WbSrc.alu)

  io.decode.isCsr := hasFlag(InstrFlag.isCsr)

  io.decode.immType := MuxCase(ImmType.typeN, Seq(
    (io.instr === Instruction.AUIPC) -> ImmType.addPc,
    (instrType === InstrType.typeI) -> ImmType.typeI,
    (instrType === InstrType.typeS) -> ImmType.typeS,
    (instrType === InstrType.typeB) -> ImmType.typeB,
    (instrType === InstrType.typeU) -> ImmType.typeU,
    (instrType === InstrType.typeJ) -> ImmType.typeJ
  ))

  when (hasFlag(InstrFlag.notReady)) {
    io.decode := 0.U.asTypeOf(DecodeIO())
  }

  io.decode.isSystem := hasFlag(InstrFlag.isSystem)

  io.decode.useRs1 := !noUseRs1
  io.decode.useRs2 := io.decode.aluSrc2 === AluSrc.rf || hasFlag(InstrFlag.isStore)

  io.decode.isHalfWord := hasFlag(InstrFlag.isHalfWord)
  io.decode.isByte := hasFlag(InstrFlag.isByte)
  io.decode.isUnsigned := hasFlag(InstrFlag.isUnsigned)

  io.decode.fence := hasFlag(InstrFlag.isFence)
}

object Decoder extends App {
  new stage.ChiselStage().emitVerilog(Decoder(), Array("--target-dir", "generated"))
}