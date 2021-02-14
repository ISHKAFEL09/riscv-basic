package Beta


class MemoryTest(dut: Memory) extends PeekPokeTester(dut)
{
  poke(dut.io.wen, 0.U)
  step(1)
  for (i <- 0 to 100) {
    poke(dut.io.raddr, i.U)
    step(1)
  }
}


object MemoryTester extends App {
  iotesters.Driver.execute(
    Array("--generate-vcd-output", "on"),
    () => new Memory("D:\\Work\\Code\\ChiselPrj\\src\\test\\resources\\test.hex")
  ) {
    c =>new MemoryTest(c)
  }
}
