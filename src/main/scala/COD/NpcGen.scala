package cod

import chisel3._

class NpcBundle extends Bundle {
  val pc = Input(UInt(xprWidth))
  val pcIndex = Input(UInt(btbWidth))
  val update = Input(Bool())
  val taken = Output(Bool())
  val npc = Output(UInt(xprWidth))
}

class NpcGen extends Module {
  val io = IO(new NpcBundle)

  val taken = RegInit(false.B)
  val npc = RegNext(io.pc + 4.U)

  // TODO  fake btb
  io.taken := taken
  io.npc := npc
}
