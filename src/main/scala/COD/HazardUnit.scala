package cod

import chisel3._
import chisel3.util._
import cod.Interfaces._
import Const._

case class HazardUnit() extends Module {
  val io = IO(HazardIO())

  val rs1 = io.instr(19, 15)
  val rs2 = io.instr(24, 20)
  val rdEx = io.exInstr(11, 7)
  val rdMem = io.memInstr(11, 7)
  val rfWenEx = io.exRfWen
  val rfWenMem = io.memRfWen
  val isLoad = io.memRen

  val hazardRs1Ex = (io.opSel1 === AluSrc.rf) && (rs1 === rdEx) && rfWenEx
  val hazardRs2Ex = (io.opSel2 === AluSrc.rf) && (rs2 === rdEx) && rfWenEx
  val hazardRs1Mem = (io.opSel1 === AluSrc.rf) && (rs1 === rdMem) && rfWenMem && isLoad
  val hazardRs2Mem = (io.opSel2 === AluSrc.rf) && (rs2 === rdMem) && rfWenMem && isLoad

  io.idStall := hazardRs1Ex || hazardRs2Ex || hazardRs1Mem || hazardRs2Mem
  io.ifFlush := io.branchErr
}

object HazardUnit extends App {
  generate(HazardUnit())
}
