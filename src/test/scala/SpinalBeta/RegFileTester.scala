package SpinalBeta

import spinal.core._
import spinal.core.sim._
import spinal.sim.SimThread

import collection._
import scala.util.Random

class RegFileTransition {
  var addr: Int = 0
  var data: BigInt = 0
  var rw: Boolean = false
  var lastItem = false

  def ==(that: RegFileTransition): Boolean = {
    (addr == that.addr) && (data == that.data)
  }

  override def toString: String = {
    "addr: " + addr.toString + ", data: " + data.toString + "\n"
  }

  def doCopy(that: RegFileTransition):Unit = {
    addr = that.addr
    data = that.data
    rw = that.rw
  }
}

class RegFileAgent(interface: RegFileIo, clk: ClockDomain, val driveMode: Boolean = false) {
  class Sequence(sequencer: mutable.Queue[RegFileTransition]) {
    def write(addr: Int, data: Int): Unit = {
      val item = new RegFileTransition
      item.data = data
      item.addr = addr
      item.rw = false
      sequencer.enqueue(item)
    }

    def read(addr: Int): Unit = {
      val item = new RegFileTransition
      item.addr = addr
      item.rw = true
      sequencer.enqueue(item)
    }

    def finish(): Unit = {
      val item = new RegFileTransition
      item.lastItem = true
      sequencer.enqueue(item)
    }
  }

  val driveQueue = new mutable.Queue[RegFileTransition]()
  val monitorQueue = new mutable.Queue[RegFileTransition]()
  val rmQueue = new mutable.Queue[RegFileTransition]()
  val rmMem = new mutable.HashMap[Int, BigInt]()
  val sequence = new Sequence(driveQueue)
  val threads = new mutable.Queue[SimThread]()

  var enable = false
  def drive(item: RegFileTransition): Unit = {
    clk.waitSampling()
    enable = true
    if (item.rw) {
      interface.raddr1 #= item.addr
      interface.wen #= false
    } else {
      interface.waddr #= item.addr
      interface.wdata #= item.data
      interface.wen #= true
    }
    clk.waitSampling()
    interface.wen #= false
    enable = false
  }

  def monitor(): RegFileTransition = {
    clk.waitFallingEdge()
    while (!enable) clk.waitFallingEdge()

    val item = new RegFileTransition

    if (interface.wen.toBoolean) {
      item.rw = false
      item.addr = interface.waddr.toInt
      item.data = interface.wdata.toBigInt
    } else {
      item.rw = true
      item.addr = interface.raddr1.toInt
      item.data = interface.rdata1.toBigInt
    }
    printf("rw: %b, addr: %x, data: %x\n", item.rw, item.addr, item.data)
    item
  }

  def rm(item: RegFileTransition): RegFileTransition = {
    val rmItem = new RegFileTransition

    rmItem.doCopy(item)
    if (item.rw) {
      rmItem.data = rmMem(item.addr)
    } else {
      rmMem(item.addr) = item.data
    }
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
            simFailure(s)
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


object RegFileTester {
  def main(args: Array[String]): Unit = {
//    val aluConfig = SpinalConfig(defaultClockDomainFrequency = FixedFrequency(10 MHz))
    SimConfig
      .withWave
      .compile(new RegFile)
      .doSim {dut =>
        dut.clockDomain.doStimulus(10)
        val agent = new RegFileAgent(dut.io.regio, dut.clockDomain, true)
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
