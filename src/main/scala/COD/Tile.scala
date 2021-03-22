package cod

import chisel3._
import chisel3.util._
import cod.Interfaces._

case class Tile() extends Module {
  val io = IO(new Bundle() {
    val imm = Flipped(MemoryIO())
    val dmm = Flipped(MemoryIO())
    val debug = Flipped(DebugRegfileIO())
  })

  val control = Module(Control())
  val csr = Module(Csr())

  val stageIf = Module(StageIF())
  val stageId = Module(StageID())
  val stageEx = Module(StageEx())
  val stageMem = Module(StageMem())
  val stageWb = Module(StageWb())

  stageIf.io.ctrl <> control.io.ifStage
  stageIf.io.pipe <> stageId.io.lastPipe
  stageIf.io.misc.imem <> io.imm
  stageIf.io.misc.branchCheck <> stageId.io.misc.branchCheck

  stageId.io.ctrl <> control.io.idStage
  stageId.io.misc.rf <> stageWb.io.misc.rfWrite
  stageId.io.misc.csr <> csr.io
  stageId.io.misc.debug <> io.debug
  stageId.io.pipe <> stageEx.io.lastPipe

  stageEx.io.ctrl <> control.io.exStage
  stageEx.io.pipe <> stageMem.io.lastPipe

  stageMem.io.ctrl <> control.io.memStage
  stageMem.io.misc.dmm <> io.dmm
  stageMem.io.pipe <> stageWb.io.lastPipe

  stageWb.io.ctrl <> control.io.wbStage

  control.io.misc.exception(0) := stageIf.io.ctrl.exception
  control.io.misc.exception(1) := stageId.io.ctrl.exception
  control.io.misc.exception(2) := stageEx.io.ctrl.exception
  control.io.misc.exception(3) := stageMem.io.ctrl.exception
  control.io.misc.exception(4) := stageWb.io.ctrl.exception
  control.io.misc.immReqFire := io.imm.req.fire()
  control.io.misc.immRespFire := io.imm.resp.fire()
  control.io.misc.dmmReqFire := io.dmm.req.fire()
  control.io.misc.dmmRespFire := io.dmm.resp.fire()
}

object Tile extends App {
  generate(Tile())
}
