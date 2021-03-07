package cod

import chisel3._
import chisel3.util.{MuxCase, MuxLookup}
import Interfaces._

class ForwardUnit extends Module {
  val io = IO(new ForwardIO)

  val rs1 = io.f2id.instr(19, 15)
  val rs2 = io.f2id.instr(24, 20)
  val rdMem = io.f2mem.instr(11, 7)
  val rdWB = io.f2wb.instr(11, 7)
  val wrEnMem = io.f2mem.ctrl.rfWen
  val wrEnWB = io.f2wb.ctrl.rfWen
  val isLoad = io.f2mem.ctrl.memRen

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
