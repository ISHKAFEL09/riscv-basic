package cod

import chisel3._
import Interfaces._
import chisel3.util._
import Const._

class Control extends Module {
  val io = IO(new CtrlIO)

  io := DontCare

  val forwardUnit = Module(new ForwardUnit)
  val decoder = Module(new Decoder)

  val f2id = forwardUnit.io.f2id
  val f2mem = forwardUnit.io.f2mem
  val f2wb = forwardUnit.io.f2wb
  f2id.instr <> io.idStage.instr
  f2id.rs1Data <> io.idStage.rs1Data
  f2id.rs2Data <> io.idStage.rs2Data
  f2id.fwdRs1 <> io.idStage.fwdRs1
  f2id.fwdRs2 <> io.idStage.fwdRs2
  f2mem.ctrl <> io.memStage.ctrl
  f2mem.instr <> io.memStage.instr
  f2mem.wbData <> io.memStage.wbData
  f2wb.ctrl <> io.wbStage.ctrl
  f2wb.instr <> io.wbStage.instr
  f2wb.wbData <> io.wbStage.wbData

  decoder.io.valid <> io.idStage.valid
  decoder.io.instr <> io.idStage.instr
  decoder.io.decode <> io.idStage.decode

  io.ifStage.pcSel := MuxCase(PCSel.npc, Seq(
    io.ifStage.flush -> PCSel.branch
  ))
}

object Control extends App {
  generate(new Control)
}