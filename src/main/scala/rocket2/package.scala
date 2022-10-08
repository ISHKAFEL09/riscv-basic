import chisel3._

package object rocket2 {
  def generate(gen: => Module): Unit = {
    (new stage.ChiselStage).emitVerilog(gen, Array("--target-dir", "generated/rocket2"))
  }

  val MTVEC = 0x100
  val START_ADDR = MTVEC + 0x100
  val HART_ID = 0
}
