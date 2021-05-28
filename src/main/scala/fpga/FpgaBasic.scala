package fpga

import chisel3._
import chisel3.experimental.{ChiselAnnotation, annotate, chiselName}
import firrtl.AttributeAnnotation
import firrtl.annotations.Annotation
import firrtl.transforms.DontTouchAnnotation

@chiselName
class FpgaBasic extends RawModule {
  val clock = IO(Input(Clock()))
  val reset = IO(Input(Bool()))
  val reset_n = !reset

  def debug(data: Data) = {
    annotate(new ChiselAnnotation {
      override def toFirrtl: Annotation = DontTouchAnnotation(data.toTarget)
    })

    annotate(new ChiselAnnotation {
      override def toFirrtl: Annotation = AttributeAnnotation(data.toTarget, """mark_debug = "true"""")
    })
  }
}
