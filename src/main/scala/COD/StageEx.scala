package cod

import chisel3._
import chisel3.util._
import chisel3.experimental.chiselName
import Interfaces._
import chisel3.util.MuxLookup
import Const._

@chiselName
case class StageEx() extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(IdPipeIO())
    val ctrl = ExCtrlIO()
    val pipe = ExPipeIO()
  })

  io.ctrl.exception := false.B

  val pipeReg = RegInit(0.U.asTypeOf(ExPipeIO()))

  val needTrim = io.lastPipe.decode.isWord && !io.lastPipe.decode.memRen && !io.lastPipe.decode.memWen
  // alu
  val aluOp1 = Mux(needTrim, io.lastPipe.aluOp1(31, 0), io.lastPipe.aluOp1)
  val aluOp2 = Mux(needTrim, io.lastPipe.aluOp2(31, 0), io.lastPipe.aluOp2)
  val shiftVal = Mux(needTrim, aluOp2(4, 0), aluOp2(log2Up(xprWidth.get) - 1, 0))
  val aluOut = signExtend(MuxLookup(io.lastPipe.decode.aluOp, 0.U, Seq(
    AluOpType.and -> (aluOp1 & aluOp2),
    AluOpType.add -> (aluOp1 + aluOp2),
    AluOpType.addu -> (aluOp1 + aluOp2),
    AluOpType.bypass1 -> aluOp1,
    AluOpType.bypass2 -> aluOp2,
    AluOpType.comp -> (aluOp1.asSInt() < aluOp2.asSInt()),
    AluOpType.compu -> (aluOp1 < aluOp2),
    AluOpType.or -> (aluOp1 | aluOp2),
    AluOpType.nop -> 0.U,
    AluOpType.sub -> (aluOp1 - aluOp2),
    AluOpType.xor -> (aluOp1 ^ aluOp2),
    AluOpType.lshift -> (aluOp1 << shiftVal),
    AluOpType.rshift -> (aluOp1 >> shiftVal),
    AluOpType.rshifta -> (signExtend(aluOp1, 32, xprWidth.get, needTrim).asSInt() >> shiftVal).asUInt()
  )).asUInt(), 32, xprWidth.get, needTrim)

  io.ctrl.instr := io.lastPipe.instr
  io.ctrl.rfWen := io.lastPipe.decode.rfWen
  when (!io.ctrl.stall) {
    when (io.ctrl.flush) {
      pipeReg := 0.U.asTypeOf(ExPipeIO())
    } otherwise {
      pipeReg.aluOut := aluOut
      pipeReg.pc := io.lastPipe.pc
      pipeReg.instr := io.lastPipe.instr
      pipeReg.decode := io.lastPipe.decode
      pipeReg.memWdata := io.lastPipe.memWdata
    }
  } otherwise {
    pipeReg.decode := 0.U.asTypeOf(DecodeIO())
  }

  io.pipe := pipeReg
  io.ctrl.fence := io.lastPipe.decode.fence
}

object StageEx extends App {
  generate(StageEx())
}
