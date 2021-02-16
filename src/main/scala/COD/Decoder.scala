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
      Instruction.JAL -> List(InstrType.typeJ, AluOpType.add, InstrFlag.isBranch)
    )
  )

  val instrType :: aluOpType :: instrFlag :: Nil = decLevel0
  when (io.valid) { assert(instrType =/= InstrType.typeN, "Unknown instr") }

  io.decode.pcSrc := MuxCase(PCSel.plus4, Seq(
    (io.branchEval && (instrFlag & InstrFlag.isBranch).orR()) -> PCSel.jump
  ))

  io.decode.aluOp := aluOpType

  io.decode.brType := MuxCase(BranchSel.nop, Seq(
    (io.instr === Instruction.BEQ) -> BranchSel.beq
  ))

  io.decode.memRen := (instrFlag & InstrFlag.isLoad).orR()
  io.decode.memWen := (instrFlag & InstrFlag.isStore).orR()

  io.decode.rfWen := !(instrType === InstrType.typeB || instrType === InstrType.typeS)

  io.decode.aluSrc1 := AluSrc.rf
  io.decode.aluSrc2 := MuxLookup(instrType, AluSrc.rf, Seq(
    InstrType.typeI -> AluSrc.imm,
    InstrType.typeU -> AluSrc.imm,
    InstrType.typeJ -> AluSrc.imm
  ))

  io.decode.wbSrc := Mux(io.decode.memRen, WbSrc.mem, WbSrc.alu)
}
