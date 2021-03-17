package cod

import chisel3._
import chisel3.util._
import Interfaces._
import Const._

case class Csr() extends Module {
  val io = IO(CsrIO())

  val csrRegs = Reg(Vec(64, UInt(xprWidth)))

  val index = io.req.bits.csr

  io.resp.rd := csrRegs(index)

  when (io.req.valid) {
    switch (io.req.bits.cmd) {
      is (CsrCmd.csrRw) {
        csrRegs(index) := io.req.bits.rs
      }
      is (CsrCmd.csrRs) {
        csrRegs(index) := io.req.bits.rs | csrRegs(index)
      }
      is (CsrCmd.csrRc) {
        csrRegs(index) := io.req.bits.rs & csrRegs(index)
      }
      is (CsrCmd.csrRwI) {
        csrRegs(index) := io.req.bits.imm
      }
      is (CsrCmd.csrRsI) {
        csrRegs(index) := io.req.bits.imm | csrRegs(index)
      }
      is (CsrCmd.csrRcI) {
        csrRegs(index) := io.req.bits.imm & csrRegs(index)
      }
    }
  }
}

object Csr extends App {
  generate(Csr())
}