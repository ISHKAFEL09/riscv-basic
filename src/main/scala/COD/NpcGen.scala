package cod

import chisel3._
import Interfaces._

class NpcGen extends Module {
  val io = IO(new NpcBundle)

  val taken = RegInit(false.B)
  val npc = RegNext(io.pc + 4.U)

  // TODO  fake btb
  io.taken := taken
  io.npc := npc
}
