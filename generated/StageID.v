module Registers(
  input         clock,
  input  [4:0]  io_rs1,
  input  [4:0]  io_rs2,
  input  [4:0]  io_rd,
  output [31:0] io_dataRs1,
  output [31:0] io_dataRs2,
  input  [31:0] io_dataRd,
  input         io_wren
);
  reg [31:0] regFile [0:31]; // @[Registers.scala 20:20]
  reg [31:0] _RAND_0;
  wire [31:0] regFile__T_1_data; // @[Registers.scala 20:20]
  wire [4:0] regFile__T_1_addr; // @[Registers.scala 20:20]
  wire [31:0] regFile__T_3_data; // @[Registers.scala 20:20]
  wire [4:0] regFile__T_3_addr; // @[Registers.scala 20:20]
  wire [31:0] regFile__T_6_data; // @[Registers.scala 20:20]
  wire [4:0] regFile__T_6_addr; // @[Registers.scala 20:20]
  wire  regFile__T_6_mask; // @[Registers.scala 20:20]
  wire  regFile__T_6_en; // @[Registers.scala 20:20]
  wire  _T; // @[Registers.scala 24:16]
  wire  _T_2; // @[Registers.scala 27:16]
  wire  _T_4; // @[Registers.scala 31:26]
  assign regFile__T_1_addr = io_rs1;
  assign regFile__T_1_data = regFile[regFile__T_1_addr]; // @[Registers.scala 20:20]
  assign regFile__T_3_addr = io_rs2;
  assign regFile__T_3_data = regFile[regFile__T_3_addr]; // @[Registers.scala 20:20]
  assign regFile__T_6_data = io_dataRd;
  assign regFile__T_6_addr = io_rd;
  assign regFile__T_6_mask = 1'h1;
  assign regFile__T_6_en = io_wren & _T_4;
  assign _T = io_rs1 != 5'h0; // @[Registers.scala 24:16]
  assign _T_2 = io_rs2 != 5'h0; // @[Registers.scala 27:16]
  assign _T_4 = io_rd != 5'h0; // @[Registers.scala 31:26]
  assign io_dataRs1 = _T ? regFile__T_1_data : 32'h0; // @[Registers.scala 22:14 Registers.scala 25:16]
  assign io_dataRs2 = _T_2 ? regFile__T_3_data : 32'h0; // @[Registers.scala 23:14 Registers.scala 28:16]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  _RAND_0 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 32; initvar = initvar+1)
    regFile[initvar] = _RAND_0[31:0];
  `endif // RANDOMIZE_MEM_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if(regFile__T_6_en & regFile__T_6_mask) begin
      regFile[regFile__T_6_addr] <= regFile__T_6_data; // @[Registers.scala 20:20]
    end
  end
endmodule
module StageID(
  input         clock,
  input         reset,
  input  [31:0] io_lastPipe_pc,
  input  [31:0] io_lastPipe_instr,
  output [31:0] io_ctrl_instr,
  input  [3:0]  io_ctrl_decode_pcSrc,
  input  [3:0]  io_ctrl_decode_aluSrc,
  input  [3:0]  io_ctrl_decode_aluOp,
  input         io_ctrl_decode_regWr,
  input         io_ctrl_decode_memWr,
  input         io_ctrl_decode_memRd,
  input  [3:0]  io_ctrl_decode_regSrc,
  input  [3:0]  io_ctrl_decode_brSrc,
  output [31:0] io_ctrl_forward_instr,
  output [31:0] io_ctrl_forward_rs1Data,
  output [31:0] io_ctrl_forward_rs2Data,
  input  [31:0] io_ctrl_forward_aluOp1,
  input  [31:0] io_ctrl_forward_aluOp2,
  input  [31:0] io_misc_wbAddr,
  input  [31:0] io_misc_wbData,
  input         io_misc_wbEn,
  output [31:0] io_misc_pcJump,
  input         io_misc_stall,
  input         io_misc_flush,
  output [31:0] io_pipe_pc,
  output [31:0] io_pipe_instr,
  output [31:0] io_pipe_aluOp1,
  output [31:0] io_pipe_aluOp2,
  output [3:0]  io_pipeCtrl_pcSrc,
  output [3:0]  io_pipeCtrl_aluSrc,
  output [3:0]  io_pipeCtrl_aluOp,
  output        io_pipeCtrl_regWr,
  output        io_pipeCtrl_memWr,
  output        io_pipeCtrl_memRd,
  output [3:0]  io_pipeCtrl_regSrc,
  output [3:0]  io_pipeCtrl_brSrc
);
  wire  regFile_clock; // @[StageID.scala 40:23]
  wire [4:0] regFile_io_rs1; // @[StageID.scala 40:23]
  wire [4:0] regFile_io_rs2; // @[StageID.scala 40:23]
  wire [4:0] regFile_io_rd; // @[StageID.scala 40:23]
  wire [31:0] regFile_io_dataRs1; // @[StageID.scala 40:23]
  wire [31:0] regFile_io_dataRs2; // @[StageID.scala 40:23]
  wire [31:0] regFile_io_dataRd; // @[StageID.scala 40:23]
  wire  regFile_io_wren; // @[StageID.scala 40:23]
  reg [3:0] regPipeCtrl_pcSrc; // @[StageID.scala 66:28]
  reg [31:0] _RAND_0;
  reg [3:0] regPipeCtrl_aluSrc; // @[StageID.scala 66:28]
  reg [31:0] _RAND_1;
  reg [3:0] regPipeCtrl_aluOp; // @[StageID.scala 66:28]
  reg [31:0] _RAND_2;
  reg  regPipeCtrl_regWr; // @[StageID.scala 66:28]
  reg [31:0] _RAND_3;
  reg  regPipeCtrl_memWr; // @[StageID.scala 66:28]
  reg [31:0] _RAND_4;
  reg  regPipeCtrl_memRd; // @[StageID.scala 66:28]
  reg [31:0] _RAND_5;
  reg [3:0] regPipeCtrl_regSrc; // @[StageID.scala 66:28]
  reg [31:0] _RAND_6;
  reg [3:0] regPipeCtrl_brSrc; // @[StageID.scala 66:28]
  reg [31:0] _RAND_7;
  wire  _T_1; // @[StageID.scala 67:15]
  reg [31:0] regPipe_pc; // @[StageID.scala 81:24]
  reg [31:0] _RAND_8;
  reg [31:0] regPipe_instr; // @[StageID.scala 81:24]
  reg [31:0] _RAND_9;
  reg [31:0] regPipe_aluOp1; // @[StageID.scala 81:24]
  reg [31:0] _RAND_10;
  reg [31:0] regPipe_aluOp2; // @[StageID.scala 81:24]
  reg [31:0] _RAND_11;
  wire  _T_5; // @[StageID.scala 85:16]
  Registers regFile ( // @[StageID.scala 40:23]
    .clock(regFile_clock),
    .io_rs1(regFile_io_rs1),
    .io_rs2(regFile_io_rs2),
    .io_rd(regFile_io_rd),
    .io_dataRs1(regFile_io_dataRs1),
    .io_dataRs2(regFile_io_dataRs2),
    .io_dataRd(regFile_io_dataRd),
    .io_wren(regFile_io_wren)
  );
  assign _T_1 = io_misc_stall | io_misc_flush; // @[StageID.scala 67:15]
  assign _T_5 = ~io_misc_stall; // @[StageID.scala 85:16]
  assign io_ctrl_instr = io_lastPipe_instr; // @[StageID.scala 59:14]
  assign io_ctrl_forward_instr = io_lastPipe_instr; // @[StageID.scala 74:17]
  assign io_ctrl_forward_rs1Data = regFile_io_dataRs1; // @[StageID.scala 75:19]
  assign io_ctrl_forward_rs2Data = regFile_io_dataRs2; // @[StageID.scala 76:19]
  assign io_misc_pcJump = 32'h0;
  assign io_pipe_pc = regPipe_pc; // @[StageID.scala 94:11]
  assign io_pipe_instr = regPipe_instr; // @[StageID.scala 94:11]
  assign io_pipe_aluOp1 = regPipe_aluOp1; // @[StageID.scala 94:11]
  assign io_pipe_aluOp2 = regPipe_aluOp2; // @[StageID.scala 94:11]
  assign io_pipeCtrl_pcSrc = regPipeCtrl_pcSrc; // @[StageID.scala 93:15]
  assign io_pipeCtrl_aluSrc = regPipeCtrl_aluSrc; // @[StageID.scala 93:15]
  assign io_pipeCtrl_aluOp = regPipeCtrl_aluOp; // @[StageID.scala 93:15]
  assign io_pipeCtrl_regWr = regPipeCtrl_regWr; // @[StageID.scala 93:15]
  assign io_pipeCtrl_memWr = regPipeCtrl_memWr; // @[StageID.scala 93:15]
  assign io_pipeCtrl_memRd = regPipeCtrl_memRd; // @[StageID.scala 93:15]
  assign io_pipeCtrl_regSrc = regPipeCtrl_regSrc; // @[StageID.scala 93:15]
  assign io_pipeCtrl_brSrc = regPipeCtrl_brSrc; // @[StageID.scala 93:15]
  assign regFile_clock = clock;
  assign regFile_io_rs1 = io_lastPipe_instr[19:15]; // @[StageID.scala 51:18]
  assign regFile_io_rs2 = io_lastPipe_instr[24:20]; // @[StageID.scala 52:18]
  assign regFile_io_rd = io_misc_wbAddr[4:0]; // @[StageID.scala 53:17]
  assign regFile_io_dataRd = io_misc_wbData; // @[StageID.scala 54:21]
  assign regFile_io_wren = io_misc_wbEn; // @[StageID.scala 55:19]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  regPipeCtrl_pcSrc = _RAND_0[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  regPipeCtrl_aluSrc = _RAND_1[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  regPipeCtrl_aluOp = _RAND_2[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  regPipeCtrl_regWr = _RAND_3[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  regPipeCtrl_memWr = _RAND_4[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  regPipeCtrl_memRd = _RAND_5[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  regPipeCtrl_regSrc = _RAND_6[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  regPipeCtrl_brSrc = _RAND_7[3:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{`RANDOM}};
  regPipe_pc = _RAND_8[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{`RANDOM}};
  regPipe_instr = _RAND_9[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_10 = {1{`RANDOM}};
  regPipe_aluOp1 = _RAND_10[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_11 = {1{`RANDOM}};
  regPipe_aluOp2 = _RAND_11[31:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      regPipeCtrl_pcSrc <= 4'h0;
    end else if (_T_1) begin
      regPipeCtrl_pcSrc <= 4'h0;
    end else begin
      regPipeCtrl_pcSrc <= io_ctrl_decode_pcSrc;
    end
    if (reset) begin
      regPipeCtrl_aluSrc <= 4'h0;
    end else if (_T_1) begin
      regPipeCtrl_aluSrc <= 4'h0;
    end else begin
      regPipeCtrl_aluSrc <= io_ctrl_decode_aluSrc;
    end
    if (reset) begin
      regPipeCtrl_aluOp <= 4'h0;
    end else if (_T_1) begin
      regPipeCtrl_aluOp <= 4'h0;
    end else begin
      regPipeCtrl_aluOp <= io_ctrl_decode_aluOp;
    end
    if (reset) begin
      regPipeCtrl_regWr <= 1'h0;
    end else if (_T_1) begin
      regPipeCtrl_regWr <= 1'h0;
    end else begin
      regPipeCtrl_regWr <= io_ctrl_decode_regWr;
    end
    if (reset) begin
      regPipeCtrl_memWr <= 1'h0;
    end else if (_T_1) begin
      regPipeCtrl_memWr <= 1'h0;
    end else begin
      regPipeCtrl_memWr <= io_ctrl_decode_memWr;
    end
    if (reset) begin
      regPipeCtrl_memRd <= 1'h0;
    end else if (_T_1) begin
      regPipeCtrl_memRd <= 1'h0;
    end else begin
      regPipeCtrl_memRd <= io_ctrl_decode_memRd;
    end
    if (reset) begin
      regPipeCtrl_regSrc <= 4'h0;
    end else if (_T_1) begin
      regPipeCtrl_regSrc <= 4'h0;
    end else begin
      regPipeCtrl_regSrc <= io_ctrl_decode_regSrc;
    end
    if (reset) begin
      regPipeCtrl_brSrc <= 4'h0;
    end else if (_T_1) begin
      regPipeCtrl_brSrc <= 4'h0;
    end else begin
      regPipeCtrl_brSrc <= io_ctrl_decode_brSrc;
    end
    if (reset) begin
      regPipe_pc <= 32'h0;
    end else if (io_misc_flush) begin
      regPipe_pc <= 32'h0;
    end else if (_T_5) begin
      regPipe_pc <= io_lastPipe_pc;
    end
    if (reset) begin
      regPipe_instr <= 32'h0;
    end else if (io_misc_flush) begin
      regPipe_instr <= 32'h4033;
    end else if (_T_5) begin
      regPipe_instr <= io_lastPipe_instr;
    end
    if (reset) begin
      regPipe_aluOp1 <= 32'h0;
    end else if (io_misc_flush) begin
      regPipe_aluOp1 <= 32'h0;
    end else if (_T_5) begin
      regPipe_aluOp1 <= io_ctrl_forward_aluOp1;
    end
    if (reset) begin
      regPipe_aluOp2 <= 32'h0;
    end else if (io_misc_flush) begin
      regPipe_aluOp2 <= 32'h0;
    end else if (_T_5) begin
      regPipe_aluOp2 <= io_ctrl_forward_aluOp2;
    end
  end
endmodule
