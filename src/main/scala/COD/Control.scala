package cod

import chisel3._
import Interfaces._
import chisel3.util._
import Const._

class Control extends Module {
  val io = IO(new CtrlIO)

  val forwardUnit = Module(new ForwardUnit)
  val decoder = Module(new Decoder)
  val hazard = Module(HazardUnit())
  val fullStall = Wire(Bool())

  val f2id = forwardUnit.io.f2id
  val f2mem = forwardUnit.io.f2mem
  val f2wb = forwardUnit.io.f2wb
  val misc = io.misc

  f2id.instr <> io.idStage.instr
  f2id.rs1Data <> io.idStage.rs1Data
  f2id.rs2Data <> io.idStage.rs2Data
  f2id.fwdRs1 <> io.idStage.fwdRs1
  f2id.fwdRs2 <> io.idStage.fwdRs2
  f2mem.ctrl <> io.memStage.decode
  f2mem.instr <> io.memStage.instr
  f2mem.wbData <> io.memStage.rdData
  f2wb.ctrl <> io.wbStage.decode
  f2wb.instr <> io.wbStage.instr
  f2wb.wbData <> io.wbStage.rdData

  hazard.io.branchErr <> io.idStage.branchErr
  hazard.io.instr <> io.idStage.instr
  hazard.io.exInstr <> io.exStage.instr
  hazard.io.memInstr <> io.memStage.instr
  hazard.io.exRfWen <> io.exStage.rfWen
  hazard.io.memRfWen <> io.memStage.decode.rfWen
  hazard.io.memRen <> io.memStage.decode.memRen
  hazard.io.opSel1 <> decoder.io.decode.aluSrc1
  hazard.io.opSel2 <> decoder.io.decode.aluSrc2
  val idStall = hazard.io.idStall
  val ifFlush = hazard.io.ifFlush
  
  def flush4Exception(n: Int): Bool = {
    val e = misc.exception.drop(n)
    e.reduce(_ || _)
  }

  io.ifStage.stall := idStall || fullStall
  io.idStage.stall := idStall || fullStall
  io.exStage.stall := false.B
  io.memStage.stall := false.B
  io.wbStage.stall := false.B

  io.ifStage.flush := ifFlush || flush4Exception(0)
  io.idStage.flush := flush4Exception(1)
  io.exStage.flush := flush4Exception(2)
  io.memStage.flush := flush4Exception(3)
  io.wbStage.flush := flush4Exception(4)

  decoder.io.instr <> io.idStage.instr
  decoder.io.decode <> io.idStage.decode
  decoder.io.valid := true.B

  def stall4Mem(reqFire: Bool, respFire: Bool): Bool = {
    val validReg = RegInit(false.B)
    when(reqFire) {
      validReg := true.B
    }.elsewhen(respFire) {
      validReg := false.B
    }
    validReg && !respFire
  }
  val immStall = stall4Mem(misc.immReqFire, misc.immRespFire)
  val dmmStall = stall4Mem(misc.dmmReqFire, misc.dmmRespFire)
  fullStall := immStall || dmmStall

  val hasException = misc.exception.reduce(_ || _)
  io.ifStage.pcSel := MuxCase(PCSel.npc, Seq(
    io.ifStage.flush -> PCSel.branch,
    hasException -> PCSel.exception
  ))
}

object Control extends App {
  generate(new Control)
}