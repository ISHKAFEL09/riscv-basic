package rocket2

import chisel3._
import chisel3.util._

class Core2IMemIO extends Bundle {
  val req = Flipped(DecoupledIO(UInt(32.W)))
  val resp = ValidIO(UInt(32.W))
}

class ICache {

}
