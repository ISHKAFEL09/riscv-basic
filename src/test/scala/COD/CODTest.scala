package COD

import chisel3._
import Const._
import chisel3.experimental.BundleLiterals.AddBundleLiteralConstructor
import chisel3.util.Cat
import org.scalatest.flatspec.AnyFlatSpec
import chiseltest._
import chiseltest.experimental.TestOptionBuilder.ChiselScalatestOptionBuilder
import chiseltest.internal.WriteVcdAnnotation
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class CODTest extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  behavior of "CODTest"

  implicit val conf = GenConfig()
  it should "pass" in {
    test(new StageIF) { c =>
      c.io.ctrl.flush.poke(0.B)
      c.io.ctrl.stall.poke(0.B)
      println(c.pc.peek().litValue())
      c.clock.step(100)
    }
  }

  it should "pass cache test" in {
    test(new Cache).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      val Mem = scala.collection.mutable.Seq.fill(1024 * 1024){
        val ret = Random.nextInt()
        if(ret < 0) -ret
        else ret
      }

      def memHandleReq: Unit = {
        val req = c.io.memReq
        val resp = c.io.memResp
        if (req.valid.peek().litToBoolean) {
          c.clock.step(1)
          val reqBits = req.bits.peek()
          val wr = reqBits.wr.litToBoolean
          val addr = (reqBits.addr.litValue().toInt & ~0xf) >> 2
          val data = reqBits.data.litValue().toInt
          if (wr) {
            Mem(addr) = data
            println(f"bt: write addr: ${addr}%x, data: ${data}%x")
            resp.enqueueNow(chiselTypeOf(c.io.memResp.bits).Lit(_.data -> 0.U))
            memHandleReq
          }
          else {
            val d0: BigInt = Mem(addr)
            val d1: BigInt = Mem(addr + 1)
            val d2: BigInt = Mem(addr + 2)
            val d3: BigInt = Mem(addr + 3)
            val readData = (d3 << 96) + (d2 << 64) + (d1 << 32) + d0
            resp.enqueueNow(chiselTypeOf(c.io.memResp.bits).Lit(
              _.data -> readData.U
            ))
          }
        }
      }

      c.io.cacheResp.initSink().setSinkClock(c.clock)
      c.io.cacheReq.initSource().setSourceClock(c.clock)
      c.io.memResp.initSource().setSourceClock(c.clock)
      c.io.memReq.initSink().setSinkClock(c.clock)

      c.io.cacheResp.ready.poke(true.B)

      var stop = false
      fork {
        while (!stop) {
          memHandleReq
          c.clock.step(1)
        }
      }.fork {
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h0".U, _.wr -> true.B, _.data -> "habcd".U))
        c.clock.step(1)
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h0".U, _.wr -> false.B, _.data -> 0.U))
        c.io.cacheResp.expectDequeue(chiselTypeOf(c.io.cacheResp.bits).Lit(_.data -> "habcd".U))
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h8000".U, _.wr -> true.B, _.data -> "h55aa".U))
        c.clock.step(1)
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h8000".U, _.wr -> false.B, _.data -> 0.U))
        c.io.cacheResp.expectDequeue(chiselTypeOf(c.io.cacheResp.bits).Lit(_.data -> "h55aa".U))
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h54".U, _.wr -> false.B, _.data -> 0.U))
        c.io.cacheResp.expectDequeue(chiselTypeOf(c.io.cacheResp.bits).Lit(_.data -> Mem(0x54 >> 2).U))
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h54".U, _.wr -> false.B, _.data -> 0.U))
        c.io.cacheResp.expectDequeue(chiselTypeOf(c.io.cacheResp.bits).Lit(_.data -> Mem(0x54 >> 2).U))
        c.io.cacheReq.enqueue(chiselTypeOf(c.io.cacheReq.bits).Lit(_.addr -> "h0".U, _.wr -> false.B, _.data -> 0.U))
        c.io.cacheResp.expectDequeue(chiselTypeOf(c.io.cacheResp.bits).Lit(_.data -> "habcd".U))
        stop = true
      }.join()
    }
  }

  it should "pass control test" in {
    test(new Control).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      val instr = "h04c0006f".U
//      val instr = Const.BUBBLE.U
      c.io.instr.poke(instr)
      c.io.valid.poke(true.B)
      c.io.branchEval.poke(true.B)
      c.clock.step(1)
      val decode = c.io.decode.peek()
      println(f"instr: ${instr.litValue()}%x")
      println(f"aluOp: ${decode.aluOp.litValue()}%x")
      println(f"pcSrc: ${decode.pcSrc.litValue()}%x")
      println(f"wbSrc: ${decode.wbSrc.litValue()}%x")
      println(f"aluSrc1: ${decode.aluSrc1.litValue()}%x")
      println(f"aluSrc2: ${decode.aluSrc2.litValue()}%x")
      println(f"rfWen: ${decode.rfWen.litValue()}%x")
      println(f"memRen: ${decode.memRen.litValue()}%x")
      println(f"memWen: ${decode.memWen.litValue()}%x")
      println(f"brType: ${decode.brType.litValue()}%x")
    }
  }
}
