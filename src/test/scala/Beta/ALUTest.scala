package Beta

import scala.util.Random

class ALUTest(dut: ALU) extends PeekPokeTester(dut)
{
  def getOut(x: Int): Int = {
    if (x < 0 ) {
      intToUnsignedBigInt(x)
    }
    else {
      x
    }
  }
  poke(dut.io.opcode, OP_AND)
  var op1 = Random.nextInt()
  var op2 = Random.nextInt()
  poke(dut.io.op1, op1)
  poke(dut.io.op2, op2)
  step(1)
  expect(dut.io.aluOut, getOut(op1 & op2))
  poke(dut.io.opcode, OP_SUB)
  op1 = Random.nextInt()
  op2 = Random.nextInt()
  poke(dut.io.op1, op1)
  poke(dut.io.op2, op2)
  step(1)
  expect(dut.io.aluOut, getOut(op1 - op2))
  poke(dut.io.opcode, OP_OR)
  op1 = Random.nextInt()
  op2 = Random.nextInt()
  poke(dut.io.op1, op1)
  poke(dut.io.op2, op2)
  step(1)
  expect(dut.io.aluOut, getOut(op1 | op2))
  poke(dut.io.opcode, OP_PLUS)
  op1 = Random.nextInt()
  op2 = Random.nextInt()
  poke(dut.io.op1, op1)
  poke(dut.io.op2, op2)
  step(1)
  expect(dut.io.aluOut, getOut(op1 + op2))
}


object ALUTester extends App {
  iotesters.Driver.execute(
    Array("--generate-vcd-output", "on"),
    () => new ALU
  ) {
    c =>new ALUTest(c)
  }
}
