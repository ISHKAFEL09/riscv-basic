package cod

import chisel3._
import chisel3.util._
import Interfaces._
import chisel3.experimental.chiselName

case class CacheModule() extends Module {
  val io = IO(new Bundle() {
    val readReq = Flipped(ValidIO(CacheReadRequest()))
    val readResp = ValidIO(CacheReadResponse())
    val writeReq = Flipped(ValidIO(CacheWriteRequest()))
  })

  val tagMem = SyncReadMem(1024, TagFormat())
  val dataMem = SyncReadMem(1024, Vec(4, UInt(32.W)))

  val readAddr = io.readReq.bits.addr.asTypeOf(AddrFormat())
  val writeAddr = io.writeReq.bits.addr.asTypeOf(AddrFormat())

  io.readResp.bits.readTag := tagMem.read(readAddr.line, io.readReq.fire())
  io.readResp.bits.readData := dataMem.read(readAddr.line, io.readReq.fire()).asUInt()
  io.readResp.valid := RegNext(io.readReq.fire())

  when (io.writeReq.fire()) {
    dataMem.write(writeAddr.line, io.writeReq.bits.writeData.asTypeOf(Vec(4, UInt(32.W))), io.writeReq.bits.mask)
    tagMem.write(writeAddr.line, io.writeReq.bits.writeTag)
  }
}

@chiselName
case class Cache3() extends CacheBase {
  val cacheWriteSel, stall, writeValid, respValid, memAddressSel, memWr, memValid, cpuRespSel = Wire(Bool())
  val hit, tagDirty = Wire(Bool())

  val cpuRequestReg = RegEnable(io.cpuRequest.bits, io.cpuRequest.fire())
  io.cpuRequest.ready := !stall

  val cache = Module(CacheModule())
  cache.io.readReq.valid := io.cpuRequest.fire()
  cache.io.readReq.bits.addr := io.cpuRequest.bits.addr
  cache.io.writeReq.valid := writeValid
  cache.io.writeReq.bits.addr := cpuRequestReg.addr
  cache.io.writeReq.bits.writeData := Mux(cacheWriteSel,
    Fill(4, cpuRequestReg.data),
    io.memResponse.bits.data).asTypeOf(Vec(4, UInt(32.W)))
  val mask = Wire(Vec(4, Bool()))
  val formatAddr = cpuRequestReg.addr.asTypeOf(AddrFormat())
  for (i <- 0 to 3) {
    when (i.U === formatAddr.word) {
      mask(i) := true.B
    }.otherwise {
      mask(i) := false.B
    }
  }
  cache.io.writeReq.bits.mask := Mux(cacheWriteSel, mask, Fill(4, true.B).asTypeOf(Vec(4, Bool())))
  cache.io.writeReq.bits.writeTag.tag := formatAddr.tag
  cache.io.writeReq.bits.writeTag.valid := true.B
  cache.io.writeReq.bits.writeTag.dirty := true.B

  val readTag = cache.io.readResp.bits.readTag
  hit := formatAddr.tag === readTag.tag && readTag.valid
  tagDirty := readTag.dirty
  val needWriteMem = !hit && tagDirty

  io.memRequest.valid := memValid
  io.memRequest.bits.wr := memWr
  val memWriteAddr = Cat(readTag.tag, formatAddr.line, 0.U(4.W))
  val memReadAddr = Cat(cpuRequestReg.addr(31, 4), 0.U(4.W))
  io.memRequest.bits.addr := Mux(memAddressSel, memWriteAddr, memReadAddr)
  io.memRequest.bits.data := cache.io.readResp.bits.readData
  val memReadData = io.memResponse.bits.data.asTypeOf(Vec(4, UInt(32.W)))(formatAddr.word)

  io.cpuResponse.valid := respValid
  io.cpuResponse.bits.wr := cpuRequestReg.wr
  val cpuRespData = Mux(cpuRespSel, cache.io.readResp.bits.readData, io.memResponse.bits.data)
  io.cpuResponse.bits.data := cpuRespData.asTypeOf(Vec(4, UInt(32.W)))(formatAddr.word)

  val idle :: compareTag :: writeReq :: writeResp :: readReq :: readResp :: finish :: Nil = Enum(7)
  val stateReg = RegInit(idle)
  val lastState = RegNext(stateReg)
  switch (stateReg) {
    is (idle) { when (io.cpuRequest.fire()) { stateReg := compareTag } }
    is (compareTag) {
      when (hit) { stateReg := idle }
        .elsewhen(needWriteMem) { stateReg := writeReq }
        .otherwise { stateReg := readReq }
    }
    is (writeReq) { when (io.memRequest.fire()) { stateReg := writeResp } }
    is (writeResp) { when (io.memResponse.fire()) { stateReg := readReq } }
    is (readReq) { when (io.memRequest.fire()) { stateReg := readResp } }
    is (readResp) { when (io.memResponse.fire()) { stateReg := idle } }
  }

  def matchState(ls: UInt, s: UInt): Bool = { ls === lastState && s === stateReg }
  def conditionState(s: UInt, c: Bool): Bool = { stateReg === s && c}
  def jump2State(s: UInt): Bool = { s === stateReg && lastState =/= s }

  respValid := conditionState(compareTag, hit) || conditionState(readResp, io.memResponse.fire())
  writeValid := (cpuRequestReg.wr && jump2State(idle)) || conditionState(readResp, io.memResponse.fire())
  memWr := stateReg === writeReq
  stall := stateReg =/= idle
  cacheWriteSel := !(stateReg === readResp)
  cpuRespSel := !(stateReg === readResp)
  memValid := stateReg === writeReq || stateReg === readReq
  memAddressSel := stateReg === writeReq || stateReg === writeResp
}

object Cache3 extends App {
  generate(Cache3())
}