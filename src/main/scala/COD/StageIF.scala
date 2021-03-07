package cod

import chisel3._
import Const._
import Interfaces._
import chisel3.internal.naming.chiselName

@chiselName
class StageIF(implicit conf: GenConfig) extends Module
{
  val io = IO(new Bundle() {
    val ctrl = new IFCtrlIO()
    val pipe = Output(new IFPipeIO())
    val misc = new IFMiscIO()
  })

  io := DontCare

  val pc = RegInit(StartAddress)
  val npc = Module(new NpcGen)
  val pcMux = Mux(io.ctrl.flush, io.misc.pcBranch, npc.io.npc)
  val pipeBundle = Wire(new IFPipeIO)
  val pipeRegs = RegInit(0.U.asTypeOf(new IFPipeIO))
  val stall = io.ctrl.stall || io.ctrl.fullStall

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
  npc.io.update := io.misc.update
  npc.io.pcIndex := io.misc.pcIndex

  when (!stall) {
    pc := pcMux
  }

  io.pipe := pipeRegs

  rtlDebug("pc: %x, instr: %x\n", io.pipe.pc, io.pipe.instr)
}

object StageIF extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageIF(), Array("--target-dir", "generated"))
}
