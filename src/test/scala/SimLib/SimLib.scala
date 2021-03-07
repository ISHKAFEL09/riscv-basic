package SimLib

import chisel3._
import chiseltest._

import scala.collection.mutable

trait Package

object implClass {
  implicit class clockRelated(clk: Clock) {
    def waitSamplingWhere(condition: Bool) = {
      clk.step()
      while (!condition.peek().litToBoolean) { clk.step() }
    }
  }
}

abstract class Driver[T <: Package](bus: Data, clk: Clock) {
  val q = mutable.Queue[T]()

  def setup(): Unit = {}

  def send(pkg: T): Unit

  def run(): Unit = {
    setup()
    while (true) {
      if (q.nonEmpty) {
        send(q.dequeue())
      }
      clk.step(1)
    }
  }

  def waitDone(): Unit = {
    var finish = false
    while (!finish) {
      if (q.isEmpty) finish = true
      clk.step(1)
    }
  }
}

abstract class Monitor[T <: Package](bus: Data, clk: Clock) {
  val q = mutable.Queue[T]()

  def sample(): T
  def run(): Unit = {
    while (true) {
      clk.step(1)
      q.enqueue(sample())
    }
  }
}

abstract class Sequence[T <: Package](drv: Driver[T]) {
  def run(): Unit
}

abstract class ReferenceModel[T <: Package, S <: Package](qin: mutable.Queue[T],
                                                          clk: Clock) {
  val q = mutable.Queue[S]()
  var cnt: Int = 0
  def calculate(pkg: T): Option[S]
  def setup(): Unit
  def run() = {
    setup()
    while (true) {
      if (qin.nonEmpty) {
        val a = calculate(qin.dequeue())
        a match {
          case Some(i) => q.enqueue(i);
          case None =>
        }
        cnt += 1
      }
      clk.step(1)
    }
  }
  def report() = {
    println(s"[RM] total ${cnt} packages got")
  }
}

abstract class ScoreBoard[T <: Package](queueDut: mutable.Queue[T], queueRm: mutable.Queue[T], clk: Clock) {
  def compare(i: T, j: T): Boolean
  def isValid(i: T): Boolean
  def run() = {
    while (true) {
      while (queueDut.nonEmpty && !isValid(queueDut.head)) queueDut.dequeue()
      while (queueRm.nonEmpty && !isValid(queueRm.head)) queueRm.dequeue()
      if (queueDut.nonEmpty && queueRm.nonEmpty) {
        val d0 = queueDut.dequeue()
        val d1 = queueRm.dequeue()
        assert (compare(d0, d1), s"Compare package fail!!! DUT: ${d0}, RM: ${d1}")
      }
      clk.step(1)
    }
  }
  def report() = {
    while (queueDut.nonEmpty && queueRm.nonEmpty) {}
    clk.step(10)
    while (queueDut.nonEmpty && !isValid(queueDut.head)) queueDut.dequeue()
    while (queueRm.nonEmpty && !isValid(queueRm.head)) queueRm.dequeue()
    assert (queueDut.isEmpty, s"${queueDut.length} package left in Monitor queue, ${queueDut.dequeue()}")
    assert (queueRm.isEmpty, s"${queueRm.length} package left in RM queue")
  }
}

abstract class Environment(dut: RawModule) {
  def setup(): Unit
  def run(): Unit
  def report(): Unit
}

abstract class TestBench[T](env: Environment) {
  def runSeq(): Unit
  def run() = {
    env.setup()
    env.run()
    runSeq()
    env.report()
  }
}

