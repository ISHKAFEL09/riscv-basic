package cod

import chisel3._
import chisel3.util._
import chisel3.experimental.chiselName
import Interfaces._

@chiselName
case class CacheMem() extends MultiIOModule {
  val request = IO(Flipped(ValidIO(CacheDataRequest())))
  val tagRequest = IO(Flipped(ValidIO(CacheTagRequest())))
  val response = IO(Output(CacheResponse()))

  val tagMem = SyncReadMem(1024, TagFormat())
  val dataMem = SyncReadMem(1024, Vec(4, UInt(32.W)))

  val address = request.bits.addr.asTypeOf(AddrFormat())
  val line = address.line
  response.readTag := tagMem.read(line)
  response.readData := dataMem.read(line).asUInt()

  when (request.valid && request.bits.wr) {
    dataMem.write(line, request.bits.writeData.asTypeOf(Vec(4, UInt(32.W))), request.bits.mask)
  }

  when (tagRequest.valid) {
    tagMem.write(line, tagRequest.bits.writeTag)
  }
}

@chiselName
case class Cache2() extends MultiIOModule {
  val cpuRequest = IO(Flipped(DecoupledIO(CpuRequest())))
  val cpuResponse = IO(ValidIO(CpuResponse()))
  val memRequest = IO(ValidIO(MemRequest()))
  val memResponse = IO(Flipped(ValidIO(MemResponse())))

  val cacheMem = Module(CacheMem())
  cacheMem.request.bits.addr := cpuRequest.bits.addr

  val stall = Wire(Bool())
  val cpuRequestReg = RegEnable(cpuRequest.bits, cpuRequest.fire())
  val cpuRequestValid = RegEnable(cpuRequest.fire(), !stall)
  val cpuRequestReg2 = RegNext(cpuRequestReg)
  val cpuRequestValid2 = RegNext(cpuRequestValid)
  cpuRequest.ready := !cpuRequestValid

  val cacheReadTag = cacheMem.response.readTag
  val cacheReadData = cacheMem.response.readData
  val cacheReadDataReg = RegNext(cacheReadData)
  val cacheReadDataValid = RegNext(cpuRequestValid)

  val hit = cacheReadTag.tag === cpuRequest.bits.addr.asTypeOf(AddrFormat()).tag
  val valid = cacheReadTag.valid
  val dirty = cacheReadTag.dirty
  val hitReg = RegEnable(hit, cpuRequestValid)
  val validReg = RegEnable(valid, cpuRequestValid)
  val dirtyReg = RegEnable(dirty, cpuRequestValid)

  val needReadMem = !valid || (!cpuRequestReg.wr && !hit) || (cpuRequestReg.wr && !hit && dirty)
  val needWriteMem = valid && !hit && dirty && cpuRequestReg.wr
  val needReadMemReg = RegNext(needReadMem, cpuRequestValid)

  val memReadEnable = Wire(Bool())
  val memWriteEnable = Wire(Bool())
  val memReadDone = Wire(Bool())
  val memWriteDone = Wire(Bool())
  val memReadData = Wire(UInt(128.W))
  val memWriteAddr = Cat(cacheReadTag.tag, cpuRequestReg.addr.asTypeOf(AddrFormat()).line, 0.U(4.W))
  val memReadAddr = Cat(cpuRequestReg.addr(31, 4), 0.U(4.W))
  assert (!(memReadEnable && memWriteEnable))
  memRequest.valid := memReadEnable || memWriteEnable
  memRequest.bits.wr := memWriteEnable
  memRequest.bits.addr := Mux(memWriteEnable, memWriteAddr, memReadAddr)
  memRequest.bits.data := cacheReadData
  memReadData := memResponse.bits.data

  val cpuRespSel = validReg && hitReg // 1 for cache, 0 for mem
  val cacheWriteSel = !needReadMemReg // 1 for cache, 0 for mem
  val wordIndexReg = RegNext(cpuRequestReg.addr.asTypeOf(AddrFormat()).word)
  cpuResponse.bits.wr := cpuRequestReg2.wr
  cpuResponse.valid := Mux(cpuRespSel, cacheReadDataValid, memReadDone)
  cpuResponse.bits.data := Mux(cpuRespSel, cacheReadDataReg, memReadData).asTypeOf(Vec(4, UInt(32.W)))(wordIndexReg)

  val memWriteMask = Wire(Vec(4, Bool()))
  (0 to 3) foreach { i =>
    when (cpuRequestReg2.addr.asTypeOf(AddrFormat()).word === i.U) {
      memWriteMask(i) := true.B
    }.otherwise {
      memWriteMask(i) := false.B
    }
  }
  cacheMem.request.valid := Mux(cacheWriteSel, cpuRequestValid2, memReadDone)
  cacheMem.request.bits.wr := cpuRequestReg2.wr || needReadMemReg
  cacheMem.request.bits.writeData := Mux(cacheWriteSel, Fill(4, cpuRequestReg2.data), memReadData)
  cacheMem.request.bits.mask := Mux(cacheWriteSel, memWriteMask, Fill(4, true.B).asTypeOf(Vec(4, Bool())))

  cacheMem.tagRequest.valid := cacheMem.request.valid
  cacheMem.tagRequest.bits.addr := cpuRequestReg2.addr
  cacheMem.tagRequest.bits.writeTag := Cat(true.B, cpuRequestReg2.wr, cpuRequestReg2.addr.asTypeOf(AddrFormat()).tag).asTypeOf(TagFormat())

  val idleState :: writeReqState :: writeRespState :: readReqState :: readRespState :: finishState :: Nil = Enum(6)
  val stateReg = RegInit(idleState)
  switch (stateReg) {
    is (idleState) {
      when (cpuRequestValid) {
        when (needWriteMem) { stateReg := writeReqState }
          .elsewhen(needReadMem) { stateReg := readReqState }
          .otherwise { stateReg := finishState }
      }
    }
    is (writeReqState) {
      when (memRequest.fire()) { stateReg := writeRespState }
    }
    is (writeRespState) {
      when (memResponse.fire()) { stateReg := readReqState }
    }
    is (readReqState) {
      when (memRequest.fire()) { stateReg := readRespState }
    }
    is (readRespState) {
      when (memResponse.fire()) { stateReg := finishState }
    }
    is (finishState) {
      when (cpuResponse.fire()) { stateReg := idleState }
    }
  }
  memReadDone := stateReg === readRespState && memResponse.fire()
  stall := cpuRequestValid && needReadMem && (!memReadDone)
  memReadEnable := stateReg === readReqState
  memWriteDone := stateReg === writeRespState && memResponse.fire()
  memWriteEnable := stateReg === writeReqState
}

object Cache2 extends App {
  generate(Cache2())
}
