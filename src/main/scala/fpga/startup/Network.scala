package fpga.startup

import chisel3._
import chisel3.util._
import fpga.FpgaBasic

// Bus Definition
case class Rgmii() extends Bundle {
  val txClock = Output(Bool())
  val txData = Output(UInt(4.W))
  val txCtrl = Output(Bool())
  val rxClock = Input(Bool())
  val rxData = Input(UInt(4.W))
  val rxCtrl = Input(Bool())
//  val mdc = Output(Bool())
//  val mdi = Input(Bool())
//  val mdo = Output(Bool())
//  val mdoEn = Output(Bool())
  val ereset = Output(Bool())
}

case class CrcBif() extends Bundle {
  val crcData = Input(UInt(8.W))
  val crcEn = Input(Bool())
  val crcRst = Input(Bool())
  val crcResult = Output(UInt(32.W))
}

case class MacRcvBif() extends Bundle {
  val ipData = ValidIO(UInt(8.W))
  val arpData = ValidIO(UInt(8.W))
  val error = Output(Bool())
  val dataLen = Output(UInt(16.W))
  val macDestAddress = Output(UInt(48.W))
  val macSrcAddress = Output(UInt(48.W))
}

// 250M
class RgmiiTransfer extends Module {
  val io = IO(new Bundle() {
    val rgmii = Rgmii()
    val rx = ValidIO(UInt(8.W))
    val tx = Flipped(ValidIO(UInt(8.W)))
  })

  io := DontCare

  io.rx.valid := false.B
  io.rgmii.ereset := ~reset.asBool()

  val rxReg = Reg(UInt(4.W))
  val idle :: s0 :: s1 :: _ = Enum(10)
  val sReg = RegInit(idle)
  switch (sReg) {
    is (idle) {
      when (io.rgmii.rxClock && io.rgmii.rxCtrl) {
        rxReg := io.rgmii.rxData
        sReg := s0
      }
    }
    is (s0) {
      io.rx.valid := true.B
      io.rx.bits := Cat(rxReg, io.rgmii.rxData)
      sReg := idle
    }
  }
}

class Network extends FpgaBasic {
  val rgmii = IO(Rgmii())

  rgmii := DontCare

  withClockAndReset(clock, reset_n) {
    val rxMac = Wire(ValidIO(UInt(8.W)))
    //  val txMac = Wire(Flipped(ValidIO(UInt(8.W))))
    val rgmiiTransfer = Module(new RgmiiTransfer)

    rgmiiTransfer.io := DontCare
    rgmiiTransfer.io.rgmii <> rgmii
    rgmiiTransfer.io.rx <> rxMac
//    rgmiiTransfer.io.tx <> txMac

    debug(rxMac.valid)
    debug(rxMac.bits)
  }
}

object Network extends App {
  generate(new Network)
}
