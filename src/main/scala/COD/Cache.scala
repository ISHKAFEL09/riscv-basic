package cod

import chisel3._
import chisel3.experimental.chiselName
import chisel3.util._
import Interfaces._

case class CacheWriteBundle() extends Bundle {
  val valid = Bool()
//  val sel = Bool() // 1: mem, 0: request
  val addr = UInt(32.W)
  val line = Vec(4, UInt(32.W))
  val mask = Vec(4, Bool())
}

@chiselName
case class Cache() extends Module {
  val io = IO(new Bundle() {
    val cacheReq = Flipped(Decoupled(new CpuRequest))
    val cacheResp = Decoupled(new CpuResponse)
    val memReq = Valid(new MemRequest)
    val memResp = Flipped(Valid(new MemResponse))
    val cacheWrite = Output(new CacheWriteBundle)
  })

  val tags = SyncReadMem(1024, TagFormat())
  val datas = SyncReadMem(1024, Vec(4, UInt(32.W)))

  val reqReg = RegEnable(io.cacheReq.bits, io.cacheReq.fire()).asTypeOf(new CpuRequest)
  val valid = RegInit(false.B)
  io.cacheReq.ready := valid === false.B
  when (io.cacheReq.fire()) {valid := true.B}

  val addrReq = io.cacheReq.bits.addr.asTypeOf(new AddrFormat)
  val addrReg = reqReg.addr.asTypeOf(new AddrFormat)
  val tag = tags.read(addrReq.line, io.cacheReq.fire())
  val data = datas.read(addrReq.line, io.cacheReq.fire())

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
  io.cacheWrite := cacheWrite

  when (cacheWrite.valid) {
    val addr = cacheWrite.addr.asTypeOf(new AddrFormat)
    datas.write(addr.line, cacheWrite.line, cacheWrite.mask)

    val tagData = Wire(TagFormat())
    tagData.tag := reqReg.addr.asTypeOf(new AddrFormat).tag
    tagData.valid := true.B
    tagData.dirty := true.B
    tags.write(addr.line, tagData)
  }
  cacheWrite.line := io.memResp.bits.data.asTypeOf(Vec(4, UInt(32.W)))
  (0 to 3) foreach (i => cacheWrite.mask(i) := true.B )

  val compareTag :: memWriteReq :: memWriteResp :: memReadReq :: memReadResp :: finish :: Nil = Enum(6)
  val state = RegInit(compareTag)
  val jump2finish = WireInit(false.B)
  val hitWrite = reqReg.wr && state === finish
  when (hitWrite) {
    cacheWrite.line(addrReg.word) := reqReg.data
    (0 to 3 ) foreach (i => {
      when (addrReg.word === i.U) {
        cacheWrite.mask(i) := true.B
      }.otherwise(
        cacheWrite.mask(i) := false.B
      )
    })
    //    printf("write data: %x, cacheWrite line: %x\n", reqReg.data, cacheWrite.line.asUInt())
  }
  cacheWrite.addr := reqReg.addr
  switch (state) {
    is (compareTag) {
      when (valid) {
        when (hit) {
          state := finish
          valid := false.B
          jump2finish := true.B
        }.elsewhen(tag.valid && tag.dirty) { state := memWriteReq }
          .otherwise {
            state := memReadReq
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
  cacheWrite.valid := state === memReadResp || hitWrite
  io.cacheResp.valid := state === finish
  io.cacheResp.bits.data := RegNext(Mux(state === memReadResp,
    io.memResp.bits.data.asTypeOf(Vec(4, UInt(32.W)))(addrReg.word),
    data(addrReg.word)))
  io.cacheResp.bits.wr := reqReg.wr
  writeMem := state === memWriteReq || state === memWriteResp
  readMem := state === memReadReq || state === memReadResp

  when (io.memResp.fire()) {
//    printf("get mem resp: addr: %x, data: %x\n", reqReg.addr, io.memResp.bits.data)
  }
}

object Cache extends App {
  new stage.ChiselStage().emitVerilog(new Cache(), Array("--target-dir", "generated"))
}