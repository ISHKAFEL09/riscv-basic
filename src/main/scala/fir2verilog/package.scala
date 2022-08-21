package object fir2verilog {

}

object GenFromFir extends App {
  import firrtl.stage.FirrtlMain

  FirrtlMain.main(Array("-i", "generated/py/temp.fir", "-o", "generated/py/fir.v", "-X", "verilog"))
}