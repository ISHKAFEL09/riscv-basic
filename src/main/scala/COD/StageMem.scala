package cod

import chisel3._
import chisel3.experimental.chiselName
import chisel3.util._
import cod.Interfaces._

@chiselName
case class StageMem() extends Module {
  val io = IO(new Bundle() {
    val lastPipe = Flipped(ExPipeIO())
    val ctrl = MemCtrlIO()
    val pipe = MemPipeIO()
    val misc = MemMiscIO()
  })

  io.ctrl.exception := false.B

  val pipeReg = RegInit(0.U.asTypeOf(MemPipeIO()))

  io.ctrl.instr := io.lastPipe.instr
  io.ctrl.decode := io.lastPipe.decode
  io.ctrl.rdData := io.lastPipe.aluOut

  def maskData(data: UInt): UInt = {
    MuxCase(data, Seq(
      (io.ctrl.decode.isUnsigned & io.ctrl.decode.isHalfWord) -> data(15, 0),
      (io.ctrl.decode.isUnsigned & io.ctrl.decode.isByte) -> data(7, 0),
      (!io.ctrl.decode.isUnsigned & io.ctrl.decode.isHalfWord) -> Cat(Fill(16, data(15)), data(15, 0)),
      (!io.ctrl.decode.isUnsigned & io.ctrl.decode.isByte) -> Cat(Fill(24, data(7)), data(7, 0))
    ))
  }

  io.misc.dmm.req.valid := io.lastPipe.decode.memRen || io.lastPipe.decode.memWen
  val memReq = io.misc.dmm.req.bits
  memReq.wr := io.lastPipe.decode.memWen
  memReq.addr := io.lastPipe.aluOut
  memReq.wdata := maskData(io.lastPipe.memWdata)

  when (!io.ctrl.stall) {
    when (io.ctrl.flush) {
      pipeReg := 0.U.asTypeOf(MemPipeIO())
    } otherwise {
      pipeReg.pc := io.lastPipe.pc
      pipeReg.instr := io.lastPipe.instr
      pipeReg.aluOut := io.lastPipe.aluOut
      pipeReg.decode := io.lastPipe.decode
      pipeReg.memRdata := maskData(io.misc.dmm.resp.bits.rdata)
    }
  } otherwise {
    pipeReg.decode := 0.U.asTypeOf(DecodeIO())
  }

  io.pipe := pipeReg
}

object StageMem extends App {
  generate(StageMem())
}


