import chisel3._
import chisel3.experimental.{BaseModule, ChiselEnum}
import chisel3.internal.firrtl.Width
import chisel3.util._
import chiseltest._

package object cod {
  val ctrlSize: Width = 4.W
  val xprWidth = GenConfig().xprlen.W
  val btbWidth = GenConfig().btbSize.W

  implicit class ClockRelated(clk: Clock) {
    def waitSampling(condition: Bool): Unit = {
      while (!condition.peek().litToBoolean) { clk.step(1) }
    }
  }

  def generate(dut: => RawModule) = {
    new stage.ChiselStage().emitVerilog(dut, Array("--target-dir", "generated"))
  }

  def rtlDebug(msg: String, data: Bits*) = {
    if (GenConfig().debugOn) {
      printf(msg, data: _*)
    }
  }

  def simDebug(msg: String) = {
    if (GenConfig().debugOn) println(msg)
  }

  object Const {
    val StartAddress = "h00000000".U(xprWidth)

    object PCSel {
      val pcSelValues = Enum(10)
      val plus4 :: jump :: excp :: start :: extra = pcSelValues
    }

    object BranchSel {
      val branchSelValues = Enum(20)
      val nop :: beq :: bne :: blt :: bltu :: bge :: bgeu :: jump :: extra = branchSelValues
    }

    object AluSrc {
      val aluSrcValues = Enum(10)
      val rf :: imm :: pc :: csr :: extra = aluSrcValues
    }

    object WbSrc {
      val wbSrcValues = Enum(10)
      val alu :: mem :: extra = wbSrcValues
    }

    object MemoryOp {
      val memoryOpValues = Enum(20)
      val mt_x :: mt_b :: mt_h :: mt_w :: mt_d :: mt_bu :: mt_hu :: mt_wu :: extra0 = memoryOpValues
      val m_x :: m_xrd :: m_xwr :: extra1 = extra0
    }

    object InstrType {
      val instrTypeValues = Enum(10)
      val typeN :: typeR :: typeI :: typeS :: typeB :: typeU :: typeJ :: extra = instrTypeValues
    }

    object AluOpType {
      val aluOpTypeValues = Enum(20)
      val add :: addu :: sub :: and :: or :: xor :: extra0 = aluOpTypeValues
      val bypass1 :: bypass2 :: lshift :: rshift :: extra1 = extra0
      val rshifta :: comp :: compu :: nop :: extra2 = extra1
    }

    object InstrFlag {
      val nop = 0.U
      val isBranch = (1 << 0).U
      val isLoad = (1 << 1).U
      val isStore = (1 << 2).U
      val isCsr = (1 << 3).U
      val isJump = (1 << 4).U
    }

    object ImmType {
      val immTypeValues = Enum(10)
      val typeN :: typeI :: typeB :: typeS :: typeU :: typeJ :: addPc :: extra = immTypeValues
    }

    val BUBBLE = "b0000_0000_0000_0000_0100_0000_0011_0011"

    object Instruction {
      val BUBBLE             = BitPat(Const.BUBBLE)
      val BEQ                = BitPat("b?????????????????000?????1100011")
      val BNE                = BitPat("b?????????????????001?????1100011")
      val BLT                = BitPat("b?????????????????100?????1100011")
      val BGE                = BitPat("b?????????????????101?????1100011")
      val BLTU               = BitPat("b?????????????????110?????1100011")
      val BGEU               = BitPat("b?????????????????111?????1100011")
      val JALR               = BitPat("b?????????????????000?????1100111")
      val JAL                = BitPat("b?????????????????????????1101111")
      val LUI                = BitPat("b?????????????????????????0110111")
      val AUIPC              = BitPat("b?????????????????????????0010111")
      val ADDI               = BitPat("b?????????????????000?????0010011")
      val SLLI               = BitPat("b000000???????????001?????0010011")
      val SLTI               = BitPat("b?????????????????010?????0010011")
      val SLTIU              = BitPat("b?????????????????011?????0010011")
      val XORI               = BitPat("b?????????????????100?????0010011")
      val SRLI               = BitPat("b000000???????????101?????0010011")
      val SRAI               = BitPat("b010000???????????101?????0010011")
      val ORI                = BitPat("b?????????????????110?????0010011")
      val ANDI               = BitPat("b?????????????????111?????0010011")
      val ADD                = BitPat("b0000000??????????000?????0110011")
      val SUB                = BitPat("b0100000??????????000?????0110011")
      val SLL                = BitPat("b0000000??????????001?????0110011")
      val SLT                = BitPat("b0000000??????????010?????0110011")
      val SLTU               = BitPat("b0000000??????????011?????0110011")
      val XOR                = BitPat("b0000000??????????100?????0110011")
      val SRL                = BitPat("b0000000??????????101?????0110011")
      val SRA                = BitPat("b0100000??????????101?????0110011")
      val OR                 = BitPat("b0000000??????????110?????0110011")
      val AND                = BitPat("b0000000??????????111?????0110011")
      val ADDIW              = BitPat("b?????????????????000?????0011011")
      val SLLIW              = BitPat("b0000000??????????001?????0011011")
      val SRLIW              = BitPat("b0000000??????????101?????0011011")
      val SRAIW              = BitPat("b0100000??????????101?????0011011")
      val ADDW               = BitPat("b0000000??????????000?????0111011")
      val SUBW               = BitPat("b0100000??????????000?????0111011")
      val SLLW               = BitPat("b0000000??????????001?????0111011")
      val SRLW               = BitPat("b0000000??????????101?????0111011")
      val SRAW               = BitPat("b0100000??????????101?????0111011")
      val LB                 = BitPat("b?????????????????000?????0000011")
      val LH                 = BitPat("b?????????????????001?????0000011")
      val LW                 = BitPat("b?????????????????010?????0000011")
      val LD                 = BitPat("b?????????????????011?????0000011")
      val LBU                = BitPat("b?????????????????100?????0000011")
      val LHU                = BitPat("b?????????????????101?????0000011")
      val LWU                = BitPat("b?????????????????110?????0000011")
      val SB                 = BitPat("b?????????????????000?????0100011")
      val SH                 = BitPat("b?????????????????001?????0100011")
      val SW                 = BitPat("b?????????????????010?????0100011")
      val SD                 = BitPat("b?????????????????011?????0100011")
      val FENCE              = BitPat("b?????????????????000?????0001111")
      val FENCE_I            = BitPat("b?????????????????001?????0001111")
      val MUL                = BitPat("b0000001??????????000?????0110011")
      val MULH               = BitPat("b0000001??????????001?????0110011")
      val MULHSU             = BitPat("b0000001??????????010?????0110011")
      val MULHU              = BitPat("b0000001??????????011?????0110011")
      val DIV                = BitPat("b0000001??????????100?????0110011")
      val DIVU               = BitPat("b0000001??????????101?????0110011")
      val REM                = BitPat("b0000001??????????110?????0110011")
      val REMU               = BitPat("b0000001??????????111?????0110011")
      val MULW               = BitPat("b0000001??????????000?????0111011")
      val DIVW               = BitPat("b0000001??????????100?????0111011")
      val DIVUW              = BitPat("b0000001??????????101?????0111011")
      val REMW               = BitPat("b0000001??????????110?????0111011")
      val REMUW              = BitPat("b0000001??????????111?????0111011")
      val LR_W               = BitPat("b00010??00000?????010?????0101111")
      val SC_W               = BitPat("b00011????????????010?????0101111")
      val LR_D               = BitPat("b00010??00000?????011?????0101111")
      val SC_D               = BitPat("b00011????????????011?????0101111")
      val ECALL              = BitPat("b00000000000000000000000001110011")
      val EBREAK             = BitPat("b00000000000100000000000001110011")
      val URET               = BitPat("b00000000001000000000000001110011")
      val MRET               = BitPat("b00110000001000000000000001110011")
      val DRET               = BitPat("b01111011001000000000000001110011")
      val SFENCE_VMA         = BitPat("b0001001??????????000000001110011")
      val WFI                = BitPat("b00010000010100000000000001110011")
      val CSRRW              = BitPat("b?????????????????001?????1110011")
      val CSRRS              = BitPat("b?????????????????010?????1110011")
      val CSRRC              = BitPat("b?????????????????011?????1110011")
      val CSRRWI             = BitPat("b?????????????????101?????1110011")
      val CSRRSI             = BitPat("b?????????????????110?????1110011")
      val CSRRCI             = BitPat("b?????????????????111?????1110011")
      val CUSTOM0            = BitPat("b?????????????????000?????0001011")
      val CUSTOM0_RS1        = BitPat("b?????????????????010?????0001011")
      val CUSTOM0_RS1_RS2    = BitPat("b?????????????????011?????0001011")
      val CUSTOM0_RD         = BitPat("b?????????????????100?????0001011")
      val CUSTOM0_RD_RS1     = BitPat("b?????????????????110?????0001011")
      val CUSTOM0_RD_RS1_RS2 = BitPat("b?????????????????111?????0001011")
      val CUSTOM1            = BitPat("b?????????????????000?????0101011")
      val CUSTOM1_RS1        = BitPat("b?????????????????010?????0101011")
      val CUSTOM1_RS1_RS2    = BitPat("b?????????????????011?????0101011")
      val CUSTOM1_RD         = BitPat("b?????????????????100?????0101011")
      val CUSTOM1_RD_RS1     = BitPat("b?????????????????110?????0101011")
      val CUSTOM1_RD_RS1_RS2 = BitPat("b?????????????????111?????0101011")
      val CUSTOM2            = BitPat("b?????????????????000?????1011011")
      val CUSTOM2_RS1        = BitPat("b?????????????????010?????1011011")
      val CUSTOM2_RS1_RS2    = BitPat("b?????????????????011?????1011011")
      val CUSTOM2_RD         = BitPat("b?????????????????100?????1011011")
      val CUSTOM2_RD_RS1     = BitPat("b?????????????????110?????1011011")
      val CUSTOM2_RD_RS1_RS2 = BitPat("b?????????????????111?????1011011")
      val CUSTOM3            = BitPat("b?????????????????000?????1111011")
      val CUSTOM3_RS1        = BitPat("b?????????????????010?????1111011")
      val CUSTOM3_RS1_RS2    = BitPat("b?????????????????011?????1111011")
      val CUSTOM3_RD         = BitPat("b?????????????????100?????1111011")
      val CUSTOM3_RD_RS1     = BitPat("b?????????????????110?????1111011")
      val CUSTOM3_RD_RS1_RS2 = BitPat("b?????????????????111?????1111011")
      val SLLI_RV32          = BitPat("b0000000??????????001?????0010011")
      val SRLI_RV32          = BitPat("b0000000??????????101?????0010011")
      val SRAI_RV32          = BitPat("b0100000??????????101?????0010011")
      val RDCYCLE            = BitPat("b11000000000000000010?????1110011")
      val RDTIME             = BitPat("b11000000000100000010?????1110011")
      val RDINSTRET          = BitPat("b11000000001000000010?????1110011")
      val RDCYCLEH           = BitPat("b11001000000000000010?????1110011")
      val RDTIMEH            = BitPat("b11001000000100000010?????1110011")
      val RDINSTRETH         = BitPat("b11001000001000000010?????1110011")
    }
  }

  case class GenConfig() {
    val xprlen = 32
    val nxpr = 32
    val btbSize = 1024
    val nxprbits = log2Ceil(nxpr)
    val rvc = false
    val vm = false
    val usingUser = false
    val innerIMem = true
    val debugOn = true
    val testCase = "tests/isa/rv32ui-p-add"
  }
}
