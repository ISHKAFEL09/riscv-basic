package SpinalBeta

import spinal.core._
import spinal.core.sim._
import scala.util.Random


object ALUTester {
  def main(args: Array[String]): Unit = {
    val aluConfig = SpinalConfig(defaultClockDomainFrequency = FixedFrequency(10 MHz))
    SimConfig
      .withWave
      .compile(new ALU)
      .doSim {dut =>
//        dut.clockDomain.doStimulus(10)
        for (i <- 1 to 20) {
          val opcode = Random.nextInt(7)
          val op1 = Random.nextInt(1024)
          val op2 = Random.nextInt(1024)
          var out = 0
          if (opcode == 0) out = op1 & op2
          if (opcode == 1) out = op1 | op2
          if (opcode == 2) out = op1 + op2
          if (opcode == 6) out = op1 - op2
          if (opcode == 5) out = op1 | op2
          dut.io.opcode #= opcode
          dut.io.op1 #= op1
          dut.io.op2 #= op2
          sleep(1)
          printf("opcode: %d, op1: %d, op2: %d, aluout: %d, expected: %d\n", opcode, op1, op2, dut.io.aluOut.toBigInt, out)
          assert(dut.io.aluOut.toBigInt == {
            if (out < 0) {
              4294967296L + out
            } else {
              out
            }
          })
        }
        simSuccess()
    }
  }
}
