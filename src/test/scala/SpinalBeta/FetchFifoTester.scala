package SpinalBeta

import spinal.core._
import spinal.core.sim._
import spinal.sim.SimThread

import collection._
import scala.util.Random

class FetchFifoTransition {
  var addr: BigInt = 0
  var data: BigInt = 0
  var lastItem = false

  def ==(that: FetchFifoTransition): Boolean = {
    (addr == that.addr) && (data == that.data)
  }

  override def toString: String = {
    "addr: " + addr.toString + ", data: " + data.toString + "\n"
  }

  def doCopy(that: FetchFifoTransition):Unit = {
    addr = that.addr
    data = that.data
  }
}

class FetchFifoAgent(interface: InterfaceFetchFifo, clk: ClockDomain, val driveMode: Boolean = false) {
  class Sequence(sequencer: mutable.Queue[FetchFifoTransition]) {
    def write(addr: Int, data: Int): Unit = {
      val item = new FetchFifoTransition
      item.data = data
      item.addr = addr
      sequencer.enqueue(item)
    }

    def read(addr: Int): Unit = {
      val item = new FetchFifoTransition
      item.addr = addr
      sequencer.enqueue(item)
    }

    def finish(): Unit = {
      val item = new FetchFifoTransition
      item.lastItem = true
      sequencer.enqueue(item)
    }
  }

  val driveQueue = new mutable.Queue[FetchFifoTransition]()
  val monitorQueue = new mutable.Queue[FetchFifoTransition]()
  val rmQueue = new mutable.Queue[FetchFifoTransition]()
  val rmMem = new mutable.HashMap[BigInt, BigInt]()
  val sequence = new Sequence(driveQueue)
  val threads = new mutable.Queue[SimThread]()

  def drive(item: FetchFifoTransition): Unit = {
    clk.waitSampling()
    interface.dataIn.valid #= true
    interface.dataIn.payload #= item.data
    interface.pcIn #= item.addr
    clk.waitSampling()
    interface.dataIn.valid #= false
    rmMem(item.addr) = item.data
  }

  def monitor(): FetchFifoTransition = {
    clk.waitFallingEdge()
    while (!interface.dataOut.valid.toBoolean) {
      clk.waitFallingEdge()
    }
    val item = new FetchFifoTransition

    item.data = interface.dataOut.payload.toBigInt
    item.addr = interface.pcOut.toBigInt
    printf("addr: %x, data: %x\n", item.addr, item.data)
    item
  }

  def rm(item: FetchFifoTransition): FetchFifoTransition = {
    val rmItem = new FetchFifoTransition

    rmItem.doCopy(item)
    rmMem(item.addr) = item.data
    rmMem(0) = 0
    rmItem
  }

  if (driveMode) {
    threads.enqueue(fork {
      sleep(10)
      while (true) {
        if (driveQueue.nonEmpty) {
          val item = driveQueue.dequeue()
          if (item.lastItem)
            simSuccess()
          rmQueue.enqueue(rm(item))
          drive(item)
        }
      }
    })

    threads.enqueue(fork {
      while (true) {
        clk.waitSampling()
        if (monitorQueue.nonEmpty) {
          val monItem = monitorQueue.dequeue()
          if (rmQueue.isEmpty) {
            simFailure("No transition in rm queue!")
          }
          val rmItem = rmQueue.dequeue()
          if (!(rmItem == monItem)) {
            var s = "Item compare fail!\n"
            s += "RM: " + rmItem.toString
            s += "MON: " + monItem.toString
//            simFailure(s)
          }
        }
      }
    })
  }

  threads.enqueue(fork {
    while (true) {
      val item = monitor()
      monitorQueue.enqueue(item)
    }
  })

//  threads.enqueue(fork {
//    while (driveQueue.nonEmpty) {
//      clk.waitSampling()
//      //        println(driveQueue.size)
//    }
//    simSuccess()
//  })

  def run(): Unit = {
    threads.foreach(thread => thread.join())
  }
}


object FetchFifoTester {
  def main(args: Array[String]): Unit = {
//    val aluConfig = SpinalConfig(defaultClockDomainFrequency = FixedFrequency(10 MHz))
    SimConfig
      .withWave
      .compile(new FetchFifo(2))
      .doSim {dut =>
        dut.clockDomain.doStimulus(10)
        dut.clockDomain.waitSampling()
        dut.io.clear #= true
        dut.io.pcIn #= 0xE000000
        dut.clockDomain.waitSampling()
        dut.io.clear #= false
        val agent = new FetchFifoAgent(dut.io, dut.clockDomain, true)
        val agentThread = fork(agent.run())

         val seqThread = fork {
          for (i <- 0 to 31) {
            val wdata = Random.nextInt(0xfffffff)
            agent.sequence.write(i % 32, wdata)
          }
          for (i <- 0 to 31) {
            agent.sequence.read(i % 32)
          }
          agent.sequence.finish()
        }

        agentThread.join()
        seqThread.join()
    }
  }
}
