package cod

import chisel3._
import Const._
import Interfaces._
import chisel3.experimental.{ChiselAnnotation, annotate, chiselName}
import chisel3.util.MuxLookup
import firrtl.annotations._
import firrtl.AttributeAnnotation

@chiselName
class StageIF(implicit conf: GenConfig) extends Module
{
  val io = IO(new Bundle() {
    val ctrl = IfCtrlIO()
    val pipe = IfPipeIO()
    val misc = IfMiscIO()
  })

  io.ctrl.exception := false.B

  val pc = RegInit(StartAddress)
  val npc = Module(new NpcGen)
  val pcMux = MuxLookup(io.ctrl.pcSel, npc.io.npc, Seq(
    PCSel.branch -> io.misc.branchCheck.pcBranch,
    PCSel.exception -> 0.U
  ))
  val pipeBundle = Wire(new IfPipeIO)
  val pipeRegs = RegInit(0.U.asTypeOf(new IfPipeIO))
//  annotate(new ChiselAnnotation {
//    override def toFirrtl: Annotation = AttributeAnnotation(io.ctrl.stall.toTarget, "mark_debug = true")
//  })

  pipeBundle.taken := npc.io.taken
  pipeBundle.npc := npc.io.npc
  pipeBundle.pc := pc
  pipeBundle.instr := io.misc.imem.resp.bits.rdata

  when (!io.ctrl.stall) {
    when (io.ctrl.flush) {
      pipeRegs.instr := BUBBLE.U
    } otherwise {
      pipeRegs := pipeBundle
    }
    pc := pcMux
  }

  npc.io.pc := pcMux
  npc.io.update := io.misc.branchCheck.update
  npc.io.pcIndex := io.misc.branchCheck.pcIndex

  io.pipe := pipeRegs

  io.misc.imem.req.valid := RegNext(!(io.ctrl.stall || io.ctrl.flush))
  io.misc.imem.req.bits.wr := false.B
  io.misc.imem.req.bits.addr := pc
  io.misc.imem.req.bits.wdata := DontCare
  io.ctrl.instr := DontCare

  rtlDebug("pc: %x, instr: %x\n", io.pipe.pc, io.pipe.instr)
}

object StageIF extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageIF(), Array("--target-dir", "generated"))
}
