package fpga.startup

import chisel3._
import fpga.FpgaBasic

class Key extends FpgaBasic {
  val key = IO(Input(UInt(4.W)))
  val led = IO(Output(UInt(4.W)))

  withClockAndReset(clock, reset_n) {
    led := key
  }

  debug(key)
  debug(led)
}

object Key extends App {
  generate(new Key)
}
