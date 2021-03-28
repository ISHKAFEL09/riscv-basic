package cod

import chisel3._
import chisel3.util._
import Interfaces._
import Const._
import Const.CSRs._

case class Csr() extends Module {
  val io = IO(CsrIO())
  
  val readReq = io.readReq.bits
  val exWrite = io.writeReq(0)
  val memWrite = io.writeReq(1)
  val wbWrite = io.writeReq(2)
  val writeReq = wbWrite.bits

  val baseCsr = Array(mepc, mcause, mhartid, mtvec, medeleg, mideleg, mstatus, mscratch, cycle)
  
  val csrMap = collection.mutable.HashMap.empty[Int, UInt]
  val initMap = collection.mutable.HashMap.empty[Int, UInt]
  val maskMap = collection.mutable.HashMap.empty[Int, UInt]

  initMap(mstatus) = "h2_0000_0000".U
  maskMap(mstatus) = "h2_0000_0000".U
  baseCsr foreach { i => csrMap += (i -> RegInit(initMap.getOrElseUpdate(i, 0.U))) }
  
  // read
  def hasHazard(read: ValidIO[CsrReq], write: ValidIO[CsrWriteReq]): Bool = {
    read.valid && write.valid && read.bits.csr === write.bits.csr
  }

  val readIndex: UInt = readReq.csr
  io.resp.rd := 0.U
  io.resp.wd := 0.U
  csrMap foreach { i =>
    val (k, csr) = i
    when(io.readReq.valid && readIndex === k.U) {
      io.resp.rd := MuxCase(csr, io.writeReq.map(i => hasHazard(io.readReq, i) -> i.bits.data))

      val csrWire = WireInit(0.U(xprWidth))
      switch(readReq.cmd) {
        is(CsrCmd.csrRw) {
          csrWire := readReq.rs
        }
        is(CsrCmd.csrRs) {
          csrWire := readReq.rs | csr
        }
        is(CsrCmd.csrRc) {
          csrWire := (~readReq.rs).asUInt() & csr
        }
        is(CsrCmd.csrRwI) {
          csrWire := readReq.imm
        }
        is(CsrCmd.csrRsI) {
          csrWire := readReq.imm | csr
        }
        is(CsrCmd.csrRcI) {
          csrWire := (~readReq.imm).asUInt() & csr
        }
      }
      val mask = maskMap.getOrElseUpdate(k, Fill(xprWidth.get, false.B))
      io.resp.wd := (csr & mask) + (csrWire & (~mask).asUInt())
    }
  }

  // write
  csrMap foreach { i =>
    val (k, csr) = i
    when(wbWrite.valid && writeReq.csr === k.U) {
      csr := writeReq.data
    }
  }
}

object Csr extends App {
  generate(Csr())
}