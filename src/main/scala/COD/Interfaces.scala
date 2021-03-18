package cod

import chisel3._
import chisel3.util._

trait ControlIO extends Bundle {
  val stall = Input(Bool())
//  val fullStall = Input(Bool())
  val flush = Input(Bool())
//  val valid = Output(Bool())
  val instr = Output(UInt(32.W))
}

trait PipeLineIO extends Bundle {
//  val valid = Output(Bool())
  val pc = Output(UInt(xprWidth))
  val instr = Output(UInt(32.W))
}

object Interfaces {
  case class MemReq() extends Bundle {
    val wr = Bool()
    val addr = UInt(xprWidth)
    val wdata = UInt(xprWidth)
  }

  case class MemResp() extends Bundle {
    val rdata = UInt(xprWidth)
  }

  case class MemoryIO() extends Bundle {
    val req = Flipped(ValidIO(MemReq()))
    val resp = ValidIO(MemResp())
  }

  /* instruction fetch stage interface*/
  class IfCtrlIO extends ControlIO {
    val pcSel = Input(UInt(ctrlSize))
  }

  case class IfPipeIO() extends PipeLineIO {
    val taken = Output(Bool())
    val npc = Output(UInt(xprWidth))
  }

  class IfMiscIO extends Bundle {
    val branchCheck = Flipped(IfBranchUpdate())
    val imem = Flipped(MemoryIO())
  }

  /* instruction decode stage interface */
  case class IfBranchUpdate() extends Bundle {
    val update = Output(Bool())
    val taken = Output(Bool())
    val pcIndex = Output(UInt(btbWidth))
    val pcBranch = Output(UInt(xprWidth))
  }

  case class CsrReq() extends Bundle {
    val cmd = UInt(3.W)
    val csr = UInt(12.W)
    val rs = UInt(xprWidth)
    val imm = UInt(5.W)
  }

  case class CsrResp() extends Bundle {
    val rd = UInt(xprWidth)
  }

  case class CsrIO() extends Bundle {
    val req = Flipped(ValidIO(CsrReq()))
    val resp = Output(CsrResp())
  }

  case class IdRfWriteIO() extends Bundle {
    val enable = Input(Bool())
    val addr = Input(UInt(xprWidth))
    val data = Input(UInt(xprWidth))
  }

  class IdMiscIO extends Bundle {
    val rf = IdRfWriteIO()
    val branchCheck = IfBranchUpdate()
    val csr = Flipped(CsrIO())
  }

  case class IdPipeIO() extends PipeLineIO {
    val aluOp1 = Output(UInt(xprWidth))
    val aluOp2 = Output(UInt(xprWidth))
    val memWdata = Output(UInt(xprWidth))
    val decode = Output(new DecodeIO)
  }

  class IdCtrlIO extends ControlIO {
    val branchErr = Output(Bool())
    val decode = Input(new DecodeIO)
    val rs1Data = Output(UInt(xprWidth))
    val rs2Data = Output(UInt(xprWidth))
    val fwdRs1 = Input(UInt(xprWidth))
    val fwdRs2 = Input(UInt(xprWidth))
  }

  /* register file interface */
  class RegFileIo(implicit conf: GenConfig) extends Bundle {
    val regWidth = conf.xprlen.W
    val rs1 = Input(UInt(5.W))
    val rs2 = Input(UInt(5.W))
    val rd = Input(UInt(5.W))
    val dataRs1 = Output(UInt(regWidth))
    val dataRs2 = Output(UInt(regWidth))
    val dataRd = Input(UInt(regWidth))
    val wren = Input(Bool())
    override def cloneType = { new RegFileIo().asInstanceOf[this.type] }
  }

  /* npc gen interface */
  class NpcBundle extends Bundle {
    val pc = Input(UInt(xprWidth))
    val pcIndex = Input(UInt(btbWidth))
    val update = Input(Bool())
    val taken = Output(Bool())
    val npc = Output(UInt(xprWidth))
  }

  /* forward unit interface */
  class Forward2IDIO extends Bundle {
    val instr = Input(UInt(32.W))
    val rs1Data = Input(UInt(xprWidth))
    val rs2Data = Input(UInt(xprWidth))
    val fwdRs1 = Output(UInt(xprWidth))
    val fwdRs2 = Output(UInt(xprWidth))
  }

  class Forward2MemWBIO extends Bundle {
    val instr = Input(UInt(32.W))
    val ctrl = Input(new DecodeIO)
    val wbData = Input(UInt(xprWidth))
  }

  class ForwardIO extends Bundle {
    val f2id = new Forward2IDIO
    val f2mem = new Forward2MemWBIO
    val f2wb = new Forward2MemWBIO
  }

  /* controller interface */
  class DecodeIO extends Bundle {
    val pcSrc = UInt(ctrlSize)
    val aluSrc1 = UInt(ctrlSize)
    val aluSrc2 = UInt(ctrlSize)
    val aluOp = UInt(ctrlSize)
    val rfWen = Bool()
    val memWen = Bool()
    val memRen = Bool()
    val wbSrc = UInt(ctrlSize)
    val brType = UInt(ctrlSize)
    val isCsr = Bool()
    val immType = UInt(ctrlSize)
    val brTarget = UInt(ctrlSize)
  }

  class CtrlIO extends Bundle {
    val ifStage = Flipped(new IfCtrlIO())
    val idStage = Flipped(new IdCtrlIO())
    val exStage = Flipped(ExCtrlIO())
    val memStage = Flipped(MemCtrlIO())
    val wbStage = Flipped(WbCtrlIO())
  }

  /* cache interface */
  case class TagFormat() extends Bundle {
    val valid = Bool()
    val dirty = Bool()
    val tag = UInt(18.W)
  }

  case class AddrFormat() extends Bundle {
    val tag = UInt(18.W)
    val line = UInt(10.W)
    val word = UInt(2.W)
    val byte = UInt(2.W)
  }

  case class CpuRequest() extends Bundle {
    val wr = Bool()
    val addr = UInt(32.W)
    val data = UInt(32.W)
  }

  case class CpuResponse() extends Bundle {
    val wr = Bool()
    val data = UInt(32.W)
  }

  case class MemRequest() extends Bundle {
    val wr = Bool()
    val addr = UInt(32.W)
    val data = UInt(128.W)
  }

  case class MemResponse() extends Bundle {
    val data = UInt(128.W)
  }

  case class CacheDataRequest() extends Bundle {
    val wr = Bool()
    val addr = UInt(32.W)
    val writeData = UInt(128.W)
    val mask = Vec(4, Bool())
  }

  case class CacheTagRequest() extends Bundle {
    val addr = UInt(32.W)
    val writeTag = TagFormat()
  }

  case class CacheResponse() extends Bundle {
    val readData = UInt(128.W)
    val readTag = TagFormat()
  }

  case class CacheReadRequest() extends Bundle {
    val addr = UInt(32.W)
  }

  case class CacheReadResponse() extends Bundle {
    val readTag = TagFormat()
    val readData = UInt(128.W)
  }

  case class CacheWriteRequest() extends Bundle {
    val addr = UInt(32.W)
    val writeData = Vec(4, UInt(32.W))
    val writeTag = TagFormat()
    val mask = Vec(4, Bool())
  }
  
  // exe stage interface
  case class ExCtrlIO() extends ControlIO {
    val rfWen = Output(Bool())
  }

  // mem stage interface
  case class MemCtrlIO() extends ControlIO {
    val ctrl = Output(new DecodeIO)
    val wbData = Output(UInt(xprWidth))
  }

  // wb stage interface
  case class WbCtrlIO() extends ControlIO {
    val ctrl = Output(new DecodeIO)
    val wbData = Output(UInt(xprWidth))
  }
}
