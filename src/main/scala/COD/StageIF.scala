package COD

import chisel3._
import chisel3.util._
import Const._
import Const.Instruction._

class IFCtrlIO extends Bundle {
  val flush = Input(Bool())
  val stall = Input(Bool())
  val pcSel = Input(UInt(4.W))
}

class IFPipeIO extends Bundle {
  val pc = Output(UInt(xprWidth))
  val instr = Output(UInt(xprWidth))
}

class IFMiscIO extends Bundle {
  val pcJump = Input(UInt(xprWidth))
  val pcExc = Input(UInt(xprWidth))
}

class StageIF(implicit conf: GenConfig) extends Module
{
  val io = IO(new Bundle() {
    val ctrlIO = new IFCtrlIO()
    val pipeIO = new IFPipeIO()
    val IFMiscIO = new IFMiscIO()
    val memPortIO = new MemPortIo(conf.xprlen)
  })

  io := DontCare

  val pc = RegInit(StartAddress)

  val pcPlus4 = pc + 4.U
  val pcNext = MuxLookup(io.ctrlIO.pcSel, StartAddress, Seq(
    PCSel.jump -> io.IFMiscIO.pcJump,
    PCSel.plus4 -> pcPlus4,
    PCSel.excp -> io.IFMiscIO.pcExc
  ))

  when (!io.ctrlIO.stall) {
    pc := pcNext
  }

  val instr = Wire(UInt(xprWidth))
  if (conf.innerIMem) {
    val bs = (0 until 100).map(i => i.U)
    val iMem = VecInit(bs)
    instr := iMem(pc)
  } else {
    io.memPortIO.req.bits.addr := pc
    instr := io.memPortIO.resp.bits.data
  }

  val regPipe = RegInit({
    val pipeIO = Wire(new IFPipeIO())
    pipeIO.pc := 0.U
    pipeIO.instr := BUBBLE
    pipeIO
  })

  when (io.ctrlIO.flush) {
    regPipe.pc := 0.U
    regPipe.instr := BUBBLE
  } .elsewhen (!io.ctrlIO.stall) {
    regPipe.pc := pc
    regPipe.instr := instr
  }

  io.pipeIO := regPipe

  printf("pc: %x, instr: %x\n", pc, instr)
}

object StageIF extends App {
  implicit val conf = GenConfig()
  new stage.ChiselStage().emitVerilog(new StageIF(), Array("--target-dir", "generated"))
}
