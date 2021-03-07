package cod

import chisel3._
import chisel3.util._

object Interfaces {
  /* instruction fetch stage interface*/
  class IFCtrlIO extends Bundle {
    val flush = Input(Bool())
    val stall = Input(Bool())
    val fullStall = Input(Bool())
  }

  class IFPipeIO extends Bundle {
    val valid = Bool()
    val taken = Bool()
    val pc = UInt(xprWidth)
    val npc = UInt(xprWidth)
    val instr = UInt(32.W)
  }

  class IFMiscIO extends Bundle {
    val pcBranch = Input(UInt(xprWidth))
    val pcIndex = Input(UInt(btbWidth))
    val update = Input(Bool())
    val instr = Input(UInt(32.W))
    val pc = Output(UInt(xprWidth))
  }

  /* instruction decode stage interface */
  class IDMiscIO extends Bundle {
    val wbAddr = Input(UInt(xprWidth))
    val wbData = Input(UInt(xprWidth))
    val wbEn = Input(Bool())
    val pcJump = Output(UInt(xprWidth))
    val stall = Input(Bool())
    val flush = Input(Bool())
  }

  class IDPipeIO extends Bundle {
    val pc = Output(UInt(xprWidth))
    val instr = Output(UInt(32.W))
    val aluOp1 = Output(UInt(xprWidth))
    val aluOp2 = Output(UInt(xprWidth))
  }

  class IDCtrlIO extends Bundle {
    val instr = Output(UInt(32.W))
    val branchEval = Output(Bool())
    val decode = Input(new DecodeIO)
    val forward = Flipped(new Forward2IDIO)
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
    val aluOp1 = Output(UInt(xprWidth))
    val aluOp2 = Output(UInt(xprWidth))
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
  }

  class CtrlIO extends Bundle {
    val valid = Input(Bool())
    val instr = Input(UInt(32.W))
    val branchEval = Input(Bool())

    val decode = Output(new DecodeIO())
    val forward = new ForwardIO
    //  val hazard = new HazardIO
    val ifFlush = Output(Bool())
    val ifStall = Output(Bool())
    val idStall = Output(Bool())
    val idFlush = Output(Bool())
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
}
