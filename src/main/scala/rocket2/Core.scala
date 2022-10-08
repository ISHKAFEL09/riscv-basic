package rocket2

import chisel3._
import chisel3.util._
import config._

case object CoreKey extends Field[CoreParams]
case object UseVM extends Field[Boolean]
case object BuildFPU extends Field[Option[() => FPU]]

abstract class CoreParams {
  def xLen: Int
  def pAddrBits: Int
  def vAddrBits: Int
  def pgIdxBits: Int
  def ppnBits: Int
  def vpnBits: Int
  def pgLevels: Int
  def asIdBits: Int
  def pgLevelBits: Int
  def retireWidth: Int
  def coreFetchWidth: Int
  def coreInstBits: Int
  def coreDCacheRegTagBits: Int
  def fastLoadByte: Boolean
  def fastLoadWord: Boolean
  def maxHartIdBits: Int
}

trait HasCoreParams {
  implicit val p: Parameters

  def coreParams: CoreParams = p(CoreKey)

  def xLen: Int = coreParams.xLen
  def pAddrBits: Int = coreParams.pAddrBits
  def vAddrBits: Int = coreParams.vAddrBits
  def pgIdxBits: Int = coreParams.pgIdxBits
  def ppnBits: Int = coreParams.ppnBits
  def vpnBits: Int = coreParams.vpnBits
  def pgLevels: Int = coreParams.pgLevels
  def pgLevelBits: Int = coreParams.pgLevelBits
  def asIdBits: Int = coreParams.asIdBits
  def hartIdLen: Int = coreParams.maxHartIdBits

  def retireWidth: Int = coreParams.retireWidth
  def coreFetchWidth: Int = coreParams.coreFetchWidth
  def coreInstBits: Int = coreParams.coreInstBits
  def coreInstBytes: Int = coreInstBits / 8
  def coreDataBits: Int = xLen
  def coreDataBytes: Int = coreDataBits / 8
  def coreDCacheRegTagBits: Int = coreParams.coreDCacheRegTagBits
  def coreMaxAddrBits: Int = math.max(ppnBits, vpnBits + 1) + pgIdxBits
  def vAddrBitsExtended: Int = if (vAddrBits < xLen) vAddrBits + 1 else vAddrBits

  def fastLoadByte: Boolean = coreParams.fastLoadByte
  def fastLoadWord: Boolean = coreParams.fastLoadWord

  if (fastLoadByte) require(fastLoadWord)
}

abstract class CoreModule(implicit val p: Parameters) extends Module with HasCoreParams

abstract class CoreBundle(implicit val p: Parameters) extends Bundle with HasCoreParams

class DebugIO extends Bundle {
}

class Core {

}

object Core extends App {
}
