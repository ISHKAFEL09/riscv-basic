package fpga

import chisel3._

package object startup {
  def generate(dut: => RawModule) = {
    new stage.ChiselStage().emitVerilog(dut, Array("--target-dir", "generated/startup"))
  }
}
