package fpga.startup

import chisel3._
import chisel3.experimental.Analog
import chisel3.util._
import fpga.FpgaBasic

case class Request() extends Bundle {
  val wr = Bool()
  val dev = UInt(8.W)
  val addr = UInt(8.W)
  val wdata = UInt(8.W)
}

class I2C extends Module {
  val io = IO(new Bundle() {
    val sclClk = Input(Bool())
    val scl = Output(Bool())
    val sdaIn = Input(Bool())
    val sdaOut = Output(Bool())
    val sdaOutEn = Output(Bool())
    val req = Flipped(DecoupledIO(Request()))
    val resp = DecoupledIO(UInt(8.W))
  })

  io := DontCare

  val reqQueue = Queue(io.req, 16)
  reqQueue.ready := false.B
  io.resp.valid := false.B

  val sclEn = Wire(Bool())
  sclEn := true.B
  io.scl := Mux(sclEn, io.sclClk, true.B)
  io.sdaOut := true.B
  io.sdaOutEn := true.B

  val cnt = Counter(32)
  val readReg = RegInit(0.U(8.W))
  val (idle :: dev :: cmd :: cmdAck :: addr :: addrAck :: writeData :: start :: devAgain ::
    readCmd :: readCmdAck :: readData :: stop :: empty :: _) = Enum(20)
  val stateReg = RegInit(idle)
  val req = RegInit(0.U.asTypeOf(Request()))

  def transfer(data: UInt, nextState: UInt) = {
    io.sdaOut := data(7)
    when (cnt.value < 7.U) {
      data := Cat(data(6, 0), data(7))
      cnt.inc()
    } otherwise {
      cnt.reset()
      stateReg := nextState
    }
  }

  def ack(nextState: UInt) = {
    io.sdaOutEn := false.B
    stateReg := nextState
  }

  val dev0, dev1 = RegInit(0.U(8.W))
  switch (stateReg) {
    is (idle) {
      sclEn := false.B
      when (reqQueue.valid) {
        reqQueue.ready := true.B
        req := reqQueue.bits
        dev0 := Cat(reqQueue.bits.dev(7, 1), false.B)
        dev1 := Cat(reqQueue.bits.dev(7, 1), true.B)
        io.sdaOut := false.B
        cnt.reset()
        stateReg := dev
      }
    }
    is (dev) {
      transfer(dev0, cmdAck)
    }
    is (cmd) {
      io.sdaOut := false.B
      stateReg := cmdAck
    }
    is (cmdAck) {
      ack(addr)
    }
    is (addr) {
      transfer(req.addr, addrAck)
    }
    is (addrAck) {
      when(req.wr === false.B) {
        ack(writeData)
      } otherwise {
        cnt.reset()
        ack(start)
      }
    }
    is (writeData) {
      transfer(req.wdata, stop)
    }
    is (start) {
      when (cnt.value < 1.U) {
        cnt.inc()
        sclEn := false.B
        io.sdaOut := true.B
      } otherwise {
        cnt.reset()
        sclEn := false.B
        io.sdaOut := false.B
        stateReg := devAgain
      }
    }
    is (devAgain) {
      transfer(dev1, readCmd)
    }
    is (readCmd) {
      readReg := 0.U
      ack(readData)
    }
    is (readData) {
      io.sdaOutEn := false.B
      io.sdaOut := false.B
      when (cnt.value < 7.U) {
        readReg := Cat(readReg(6, 0), io.sdaIn)
        cnt.inc()
      } otherwise {
        cnt.reset()
        stateReg := stop
      }
    }
    is (stop) {
      when(cnt.value < 1.U) {
        io.sdaOutEn := false.B
        io.sdaOut := false.B
        cnt.inc()
//      }.elsewhen(cnt.value < 2.U) {
//        io.sdaOut := false.B
//        io.sdaOutEn := true.B
//        cnt.inc()
      }.elsewhen(cnt.value < 2.U) {
        cnt.inc()
        sclEn := false.B
        io.sdaOut := false.B
        io.sdaOutEn := true.B
      } otherwise {
        sclEn := false.B
        io.sdaOut := true.B
        cnt.reset()
        stateReg := empty
        when(req.wr === true.B) {
          io.resp.valid := true.B
          io.resp.bits := readReg
        }
      }
    }
    is (empty) {
      sclEn := false.B
      io.sdaOut := true.B
      when (cnt.value < 2.U) {
        cnt.inc()
      } otherwise {
        cnt.reset()
        stateReg := idle
      }
    }
  }
}

class I2CBench extends Module {
  val io = IO(new Bundle() {
    val sclClk = Input(Bool())
    val scl = Output(Bool())
    val sdaIn = Input(Bool())
    val sdaOut = Output(Bool())
    val sdaOutEn = Output(Bool())
    val key = Input(UInt(4.W))
  })

  val i2c = Module(new I2C())
  i2c.io.scl <> io.scl
  i2c.io.sdaOut <> io.sdaOut
  i2c.io.sdaIn <> io.sdaIn
  i2c.io.sdaOutEn <> io.sdaOutEn
  i2c.io.sclClk <> io.sclClk
  i2c.io.resp.ready := true.B

  i2c.io.req := DontCare

  val addr = Counter(255)
  val data = Counter(255)

  val write :: read :: _ = Enum(10)
  val stateReg = RegInit(write)
  switch (stateReg) {
    is (write) {
      i2c.io.req.valid := true.B
      i2c.io.req.bits.wr := 0.U
      i2c.io.req.bits.dev := "hA0".U
      i2c.io.req.bits.addr := addr.value
//      i2c.io.req.bits.addr := 0.U
      i2c.io.req.bits.wdata := data.value
//      i2c.io.req.bits.wdata := "h55".U
      when (i2c.io.req.fire()) {
        addr.inc()
        data.inc()
      }
      when (!io.key(0)) {
        addr.reset()
        data.reset()
        stateReg := read
      }
    }
    is (read) {
      i2c.io.req.valid := true.B
      i2c.io.req.bits.wr := 1.U
      i2c.io.req.bits.dev := "hA0".U
      i2c.io.req.bits.addr := addr.value
//      i2c.io.req.bits.addr := 0.U
      i2c.io.req.bits.wdata := 0.U
      when (i2c.io.req.fire()) {
        addr.inc()
        data.inc()
      }
      when (!io.key(0)) {
        addr.reset()
        data.reset()
        stateReg := write
      }
    }
  }
}

class I2CTrans extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val scl = Output(Bool())
    val sda = Analog(1.W)
    val i_scl = Input(Bool())
    val i_sdaOut = Input(Bool())
    val i_sdaOutEn = Input(Bool())
    val i_sdaIn = Output(Bool())
  })
  setInline("I2CTrans.v",
    """module I2CTrans(
      |    output scl,
      |    inout sda,
      |    input i_scl,
      |    input i_sdaOut,
      |    input i_sdaOutEn,
      |    output i_sdaIn
      |);
      | assign scl = i_scl;
      | assign sda = (i_sdaOutEn) ? i_sdaOut : 'bz;
      | assign i_sdaIn = sda;
      |endmodule
    """.stripMargin)
}

class I2CWrap extends FpgaBasic {
  val scl = IO(Output(Bool()))
  val sda = IO(Analog(1.W))
  val key = IO(Input(UInt(4.W)))

  val freq = 400 // kHz

  withClockAndReset(clock, reset_n.asAsyncReset()) {
    val clkReg = RegInit(false.B)
    val clkScl = RegInit(false.B)
    val cnt = Counter(25000 / freq)
    when(cnt.inc()) {
      clkReg := ~clkReg
    }
    when (cnt.value === (25000 / 2 / freq).U) {
      clkScl := clkReg
    }


    withClock(clkReg.asClock()) {
      val i2cTrans = Module(new I2CTrans())
      val i2cBench = Module(new I2CBench())

      i2cBench.io.sclClk := clkScl
      i2cBench.io.key <> key

      scl <> i2cTrans.io.scl
      sda <> i2cTrans.io.sda

      i2cTrans.io.i_scl <> i2cBench.io.scl
      i2cTrans.io.i_sdaIn <> i2cBench.io.sdaIn
      i2cTrans.io.i_sdaOut <> i2cBench.io.sdaOut
      i2cTrans.io.i_sdaOutEn <> i2cBench.io.sdaOutEn

      debug(i2cBench.i2c.io.scl)
      debug(i2cBench.i2c.io.sdaIn)
      debug(i2cBench.i2c.io.sdaOut)
      debug(i2cBench.i2c.io.sdaOutEn)
      debug(i2cBench.i2c.stateReg)
      debug(i2cBench.i2c.readReg)
      debug(i2cBench.i2c.io.resp.valid)
      debug(i2cBench.i2c.io.resp.bits)
      debug(key)
    }
  }
}

object I2C extends App {
  generate(new I2CWrap)
}