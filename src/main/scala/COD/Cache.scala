package COD

import chisel3._
import chisel3.util._

class CacheReqBundle extends Bundle {
  val wr = Bool()
  val addr = UInt(32.W)
  val data = UInt(32.W)
}

class CacheRespBundle extends Bundle {
  val data = UInt(32.W)
}

class MemReqBundle extends Bundle {
  val wr = Bool()
  val addr = UInt(32.W)
  val data = UInt(128.W)
}

class MemRespBundle extends Bundle {
  val data = UInt(128.W)
}

class TagBundle extends Bundle {
  val valid = Bool()
  val dirty = Bool()
  val tag = UInt(18.W)
}

class AddrBundle extends Bundle {
  val tag = UInt(18.W)
  val line = UInt(10.W)
  val word = UInt(2.W)
  val byte = UInt(2.W)
}

class CacheWriteBundle extends Bundle {
  val valid = Bool()
  val sel = Bool() // 1: line, 0: word
  val addr = UInt(32.W)
  val word = UInt(32.W)
  val line = Vec(4, UInt(32.W))
}

class Cache extends Module {
  val io = IO(new Bundle() {
    val cacheReq = Flipped(Decoupled(new CacheReqBundle))
    val cacheResp = Decoupled(new CacheRespBundle)
    val memReq = Valid(new MemReqBundle)
    val memResp = Flipped(Valid(new MemRespBundle))
    val data = Output(Vec(4, UInt(32.W)))
  })

  val tags = SyncReadMem(1024, new TagBundle)
  val datas = SyncReadMem(1024, Vec(4, UInt(32.W)))

  val reqReg = RegEnable(io.cacheReq.bits, io.cacheReq.fire()).asTypeOf(new CacheReqBundle)
  val valid = RegInit(false.B)
  io.cacheReq.ready := valid === false.B
  when (io.cacheReq.fire()) {valid := true.B}

  val addrReq = io.cacheReq.bits.addr.asTypeOf(new AddrBundle)
  val addrReg = reqReg.addr.asTypeOf(new AddrBundle)
  val tag = tags.read(addrReq.line, io.cacheReq.fire())
  val data = datas.read(addrReq.line, io.cacheReq.fire())
  io.data := data

  val hit = tag.valid && tag.tag === addrReg.tag

  val writeMem = WireInit(false.B)
  val readMem = WireInit(false.B)
  val writeMemAddr = Cat(tag.tag, addrReg.line, 0.U(4.W))
  val readMemAddr = Cat(addrReg.tag, addrReg.line, 0.U(4.W))
  io.memReq.bits.wr := writeMem
  io.memReq.bits.addr := Mux(writeMem, writeMemAddr, readMemAddr)
  io.memReq.bits.data := data.asUInt()

  val writeCacheSel = false.B
  val cacheWrite = Wire(new CacheWriteBundle)
  when (cacheWrite.valid) {
    val line = tags(cacheWrite.addr.asTypeOf(new AddrBundle).line)
    line.tag := reqReg.addr.asTypeOf(new AddrBundle).tag
    line.valid := true.B
    line.dirty := true.B
  }

  when (cacheWrite.valid) {
    val addr = cacheWrite.addr.asTypeOf(new AddrBundle)
    when (cacheWrite.sel) {
      datas(addr.line) := cacheWrite.line
    }
      .otherwise(datas(addr.line)(addr.word) := cacheWrite.word)
  }
  cacheWrite.line := io.memResp.bits.data.asTypeOf(Vec(4, UInt(32.W)))
  when (reqReg.wr) {
    cacheWrite.line(addrReg.word) := reqReg.data
    printf("write data: %x, cachwrite line: %x\n", reqReg.data, cacheWrite.line.asUInt())
  }
  cacheWrite.word := reqReg.data
  cacheWrite.addr := reqReg.addr

  val compareTag :: memWriteReq :: memWriteResp :: memReadReq :: memReadResp :: finish :: Nil = Enum(6)
  val state = RegInit(compareTag)
  val jump2finish = WireInit(false.B)
  switch (state) {
    is (compareTag) {
      when (valid) {
        when (hit) {
          state := finish
          valid := false.B
          jump2finish := true.B
        }.elsewhen(tag.valid && tag.dirty) { state := memWriteReq }
          .elsewhen(reqReg.wr === false.B) {state := memReadReq}
          .otherwise {
            state := finish
            jump2finish := true.B
          }
      }
    }
    is (memWriteReq) { state := memWriteResp }
    is (memWriteResp) { when (io.memResp.fire()) {state := memReadReq}}
    is (memReadReq) {state := memReadResp}
    is (memReadResp) {
      when (io.memResp.fire()) {
        state := finish
      }
    }
    is (finish) {when (io.cacheResp.fire()) {
      state := compareTag
      valid := false.B
    }}
  }

  io.memReq.valid := state === memWriteReq || state === memReadReq
  cacheWrite.valid := state === memReadResp || (reqReg.wr && jump2finish)
  cacheWrite.sel := state === memReadResp
  io.cacheResp.valid := state === finish
  io.cacheResp.bits.data := RegNext(Mux(state === memReadResp,
    io.memResp.bits.data.asTypeOf(Vec(4, UInt(32.W)))(addrReg.word),
    data(addrReg.word)))
  writeMem := state === memWriteReq || state === memWriteResp
  readMem := state === memReadReq || state === memReadResp

  when (io.memResp.fire()) {
    printf("get mem resp: addr: %x, data: %x\n", reqReg.addr, io.memResp.bits.data)
  }
}

object Cache extends App {
  new stage.ChiselStage().emitVerilog(new Cache(), Array("--target-dir", "generated"))
}