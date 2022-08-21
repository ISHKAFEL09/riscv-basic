package fpga

import chisel3._
import chisel3.experimental.{ChiselAnnotation, annotate, chiselName}
import firrtl.AttributeAnnotation
import firrtl.annotations.Annotation
import firrtl.transforms.DontTouchAnnotation

@chiselName
class FpgaBasic extends MultiIOModule {
  val clock2 = IO(Input(Bool()))
  val reset_n = !reset.asBool
  val din = IO(Input(Bool()))
  val dout1 = IO(Output(Bool()))

  withClock(clock2.asClock)) {
    dout1 := RegNext(din)
  }

  def debug(data: Data) = {
    annotate(new ChiselAnnotation {
      override def toFirrtl: Annotation = DontTouchAnnotation(data.toTarget)
    })

    annotate(new ChiselAnnotation {
      override def toFirrtl: Annotation = AttributeAnnotation(data.toTarget, """mark_debug = "true"""")
    })
  }
}
