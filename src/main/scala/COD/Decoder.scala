package COD

import chisel3._
import chisel3.util._
import Const._
import chisel3.internal.naming.chiselName

@chiselName
class Decoder extends Module {
  val io = IO(new Bundle() {
    val valid = Input(Bool())
    val instr = Input(UInt(32.W))
    val branchEval = Input(Bool())
    val decode = Output(new DecodeIO)
  })

  io := DontCare

  val decLevel0 = ListLookup(io.instr,
      //   instrType        aluOpType      flag
      List(InstrType.typeN, AluOpType.add, InstrFlag.isBranch),
    Array(
      Instruction.BUBBLE -> List(InstrType.typeR, AluOpType.add, 0.U),
      Instruction.ADD -> List(InstrType.typeR, AluOpType.add, 0.U),
      Instruction.ADDI -> List(InstrType.typeI, AluOpType.add, 0.U),
      Instruction.LW -> List(InstrType.typeI, AluOpType.add, InstrFlag.isLoad),
      Instruction.SW -> List(InstrType.typeS, AluOpType.add, InstrFlag.isStore),
      Instruction.BEQ -> List(InstrType.typeB, AluOpType.nop, InstrFlag.isBranch),
      Instruction.LUI -> List(InstrType.typeU, AluOpType.bypass2, 0.U),
      Instruction.JAL -> List(InstrType.typeJ, AluOpType.add, InstrFlag.isJump)
    )
  )

  val instrType :: aluOpType :: instrFlag :: Nil = decLevel0
  when (io.valid) { assert(instrType =/= InstrType.typeN, "Unknown instr") }

  def hasFlag(flag: UInt): Bool = {
    (instrFlag & flag).orR()
  }

  io.decode.pcSrc := MuxCase(PCSel.plus4, Seq(
    (io.branchEval && hasFlag(InstrFlag.isBranch)) -> PCSel.jump
  ))

  io.decode.aluOp := aluOpType

  io.decode.brType := MuxCase(BranchSel.nop, Seq(
    (io.instr === Instruction.BEQ) -> BranchSel.beq
  ))

  io.decode.memRen := hasFlag(InstrFlag.isLoad)
  io.decode.memWen := hasFlag(InstrFlag.isStore)

  io.decode.rfWen := !(instrType === InstrType.typeB || instrType === InstrType.typeS)

  io.decode.aluSrc1 := AluSrc.rf
  io.decode.aluSrc2 := MuxCase(AluSrc.rf, Seq(
    (instrType === InstrType.typeI) -> AluSrc.imm,
    (instrType === InstrType.typeU) -> AluSrc.imm,
    (instrType === InstrType.typeJ) -> AluSrc.imm,
    hasFlag(InstrFlag.isJump) -> AluSrc.pc,
    hasFlag(InstrFlag.isCsr) -> AluSrc.csr
  ))

  io.decode.wbSrc := Mux(io.decode.memRen, WbSrc.mem, WbSrc.alu)

  io.decode.isCsr := hasFlag(InstrFlag.isCsr)
}
