package COD

import chisel3._
import chisel3.util._
import Const._

class MemPortIo(data_width: Int)(implicit val conf: GenConfig) extends Bundle
{
   val req    = new DecoupledIO(new MemReq(data_width))
   val resp   = Flipped(new ValidIO(new MemResp(data_width)))
  override def cloneType = { new MemPortIo(data_width).asInstanceOf[this.type] }
}

class MemReq(data_width: Int)(implicit val conf: GenConfig) extends Bundle
{
   val addr = Output(UInt(conf.xprlen.W))
   val data = Output(UInt(data_width.W))
   val fcn  = Output(UInt(MemoryOp.m_x.getWidth.W))  // memory function code
   val typ  = Output(UInt(MemoryOp.mt_x.getWidth.W)) // memory type
  override def cloneType = { new MemReq(data_width).asInstanceOf[this.type] }
}

class MemResp(data_width: Int) extends Bundle
{
   val data = Output(UInt(data_width.W))
  override def cloneType = { new MemResp(data_width).asInstanceOf[this.type] }
}
