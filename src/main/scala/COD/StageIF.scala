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
    val ctrl = new IfCtrlIO()
    val pipe = Output(new IfPipeIO())
    val misc = new IfMiscIO()
  })

  val pc = RegInit(StartAddress)
  val npc = Module(new NpcGen)
  val pcMux = MuxLookup(io.ctrl.pcSel, npc.io.npc, Seq(
    PCSel.branch -> io.misc.branchCheck.pcBranch,
    PCSel.exception -> 0.U
  ))
  val pipeBundle = Wire(new IfPipeIO)
  val pipeRegs = RegInit(0.U.asTypeOf(new IfPipeIO))
  val stall = io.ctrl.stall || io.ctrl.fullStall
  annotate(new ChiselAnnotation {
    override def toFirrtl: Annotation = AttributeAnnotation(stall.toTarget, "mark_debug = true")
  })

  pipeBundle.taken := npc.io.taken
  pipeBundle.npc := npc.io.npc
  pipeBundle.pc := pc
  pipeBundle.instr := io.misc.instr
  pipeBundle.valid := !(io.ctrl.flush || stall)

  when (io.ctrl.flush) {
    pipeRegs.instr := BUBBLE.U
  } .elsewhen(!stall) {
    pipeRegs := pipeBundle
  }

  npc.io.pc := pcMux
  npc.io.update := io.misc.branchCheck.update
  npc.io.pcIndex := io.misc.branchCheck.pcIndex

  when (!stall) {
    pc := pcMux
  }

  io.pipe := pipeRegs
  io.misc.pc.bits := pc
  io.misc.pc.valid := RegNext(!(io.ctrl.stall || io.ctrl.fullStall || io.ctrl.flush))

  io.ctrl.instr := DontCare
  io.ctrl.valid := DontCare

  rtlDebug("pc: %x, instr: %x\n", io.pipe.pc, io.pipe.instr)
}

object StageIF extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageIF(), Array("--target-dir", "generated"))
}
