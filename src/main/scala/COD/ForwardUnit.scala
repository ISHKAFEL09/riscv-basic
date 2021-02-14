package COD

import chisel3._
import chisel3.util.{MuxCase, MuxLookup}

class Forward2IDIO extends Bundle {
  val instr = Input(UInt(xprWidth))
  val rs1Data = Input(UInt(xprWidth))
  val rs2Data = Input(UInt(xprWidth))
  val aluOp1 = Output(UInt(xprWidth))
  val aluOp2 = Output(UInt(xprWidth))
}

class Forward2MemWBIO extends Bundle {
  val instr = Input(UInt(xprWidth))
  val ctrl = Input(new DecodeIO)
  val wbData = Input(UInt(xprWidth))
}

class ForwardIO extends Bundle {
  val f2id = new Forward2IDIO
  val f2mem = new Forward2MemWBIO
  val f2wb = new Forward2MemWBIO
}

class ForwardUnit extends Module {
  val io = IO(new ForwardIO)

  val rs1 = io.f2id.instr(19, 15)
  val rs2 = io.f2id.instr(24, 20)
  val rdMem = io.f2mem.instr(11, 7)
  val rdWB = io.f2wb.instr(11, 7)
  val wrEnMem = io.f2mem.ctrl.regWr
  val wrEnWB = io.f2wb.ctrl.regWr
  val isLoad = io.f2mem.ctrl.memRd

  val hazardRs1Mem = (rs1 === rdMem) && wrEnMem && (!isLoad)
  val hazardRs2Mem = (rs2 === rdMem) && wrEnMem && (!isLoad)
  val hazardRs1WB = (rs1 === rdWB) && wrEnWB
  val hazardRs2WB = (rs2 === rdWB) && wrEnWB

  io.f2id.aluOp1 := MuxCase(io.f2id.rs1Data, Seq(
    hazardRs1Mem -> io.f2mem.wbData,
    hazardRs1WB -> io.f2wb.wbData
  ))

  io.f2id.aluOp2 := MuxCase(io.f2id.rs2Data, Seq(
    hazardRs2Mem -> io.f2mem.wbData,
    hazardRs2WB -> io.f2wb.wbData
  ))
}
