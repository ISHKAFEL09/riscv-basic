package COD

import chisel3._
import Const._

class IFCtrlIO extends Bundle {
  val flush = Input(Bool())
  val stall = Input(Bool())
}

class IFPipeIO extends Bundle {
  val taken = Bool()
  val pc = UInt(xprWidth)
  val npc = UInt(xprWidth)
  val instr = UInt(32.W)
}

class IFMiscIO extends Bundle {
  val pcBranch = Input(UInt(xprWidth))
  val pcIndex = Input(UInt(btbWidth))
  val update = Input(Bool())
  val instr = Input(UInt(32.W))
  val pc = Output(UInt(xprWidth))
}

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
  val pipeBundle = new IFPipeIO
  val pipeRegs = RegInit(0.U.asTypeOf(new IFPipeIO))

  pipeBundle.taken := npc.io.taken
  pipeBundle.npc := npc.io.npc
  pipeBundle.pc := pc
  pipeBundle.instr := io.misc.instr
  when (io.ctrl.flush) {
    pipeRegs.instr := BUBBLE.U
  } .elsewhen(!io.ctrl.stall) {
    pipeRegs := pipeBundle
  }

  npc.io.pc := pcMux
  npc.io.update := io.misc.update
  npc.io.pcIndex := io.misc.pcIndex


  when (!io.ctrl.stall) {
    pc := pcMux
  }

  io.pipe := pipeRegs

  debug("pc: %x, instr: %x\n", io.pipe.pc, io.pipe.instr)
}

object StageIF extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageIF(), Array("--target-dir", "generated"))
}
