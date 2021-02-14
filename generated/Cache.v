module Cache(
  input          clock,
  input          reset,
  output         io_cacheReq_ready,
  input          io_cacheReq_valid,
  input          io_cacheReq_bits_wr,
  input  [31:0]  io_cacheReq_bits_addr,
  input  [31:0]  io_cacheReq_bits_data,
  input          io_cacheRespBundle_ready,
  output         io_cacheRespBundle_valid,
  output [31:0]  io_cacheRespBundle_bits_data,
  output         io_memReq_valid,
  output         io_memReq_bits_wr,
  output [31:0]  io_memReq_bits_addr,
  output [127:0] io_memReq_bits_data,
  input          io_memResp_valid,
  input  [127:0] io_memResp_bits_data
);
  reg  tags_valid [0:1023]; // @[Cache.scala 55:25]
  reg [31:0] _RAND_0;
  wire  tags_valid_tag_data; // @[Cache.scala 55:25]
  wire [9:0] tags_valid_tag_addr; // @[Cache.scala 55:25]
  wire  tags_valid__T_43_data; // @[Cache.scala 55:25]
  wire [9:0] tags_valid__T_43_addr; // @[Cache.scala 55:25]
  wire  tags_valid__T_43_mask; // @[Cache.scala 55:25]
  wire  tags_valid__T_43_en; // @[Cache.scala 55:25]
  reg  tags_valid_tag_en_pipe_0;
  reg [31:0] _RAND_1;
  reg [9:0] tags_valid_tag_addr_pipe_0;
  reg [31:0] _RAND_2;
  reg  tags_dirty [0:1023]; // @[Cache.scala 55:25]
  reg [31:0] _RAND_3;
  wire  tags_dirty_tag_data; // @[Cache.scala 55:25]
  wire [9:0] tags_dirty_tag_addr; // @[Cache.scala 55:25]
  wire  tags_dirty__T_43_data; // @[Cache.scala 55:25]
  wire [9:0] tags_dirty__T_43_addr; // @[Cache.scala 55:25]
  wire  tags_dirty__T_43_mask; // @[Cache.scala 55:25]
  wire  tags_dirty__T_43_en; // @[Cache.scala 55:25]
  reg  tags_dirty_tag_en_pipe_0;
  reg [31:0] _RAND_4;
  reg [9:0] tags_dirty_tag_addr_pipe_0;
  reg [31:0] _RAND_5;
  reg [17:0] tags_tag [0:1023]; // @[Cache.scala 55:25]
  reg [31:0] _RAND_6;
  wire [17:0] tags_tag_tag_data; // @[Cache.scala 55:25]
  wire [9:0] tags_tag_tag_addr; // @[Cache.scala 55:25]
  wire [17:0] tags_tag__T_43_data; // @[Cache.scala 55:25]
  wire [9:0] tags_tag__T_43_addr; // @[Cache.scala 55:25]
  wire  tags_tag__T_43_mask; // @[Cache.scala 55:25]
  wire  tags_tag__T_43_en; // @[Cache.scala 55:25]
  reg  tags_tag_tag_en_pipe_0;
  reg [31:0] _RAND_7;
  reg [9:0] tags_tag_tag_addr_pipe_0;
  reg [31:0] _RAND_8;
  reg [31:0] datas_0 [0:1023]; // @[Cache.scala 56:26]
  reg [31:0] _RAND_9;
  wire [31:0] datas_0_data_data; // @[Cache.scala 56:26]
  wire [9:0] datas_0_data_addr; // @[Cache.scala 56:26]
  wire [31:0] datas_0__T_56_data; // @[Cache.scala 56:26]
  wire [9:0] datas_0__T_56_addr; // @[Cache.scala 56:26]
  wire  datas_0__T_56_mask; // @[Cache.scala 56:26]
  wire  datas_0__T_56_en; // @[Cache.scala 56:26]
  reg  datas_0_data_en_pipe_0;
  reg [31:0] _RAND_10;
  reg [9:0] datas_0_data_addr_pipe_0;
  reg [31:0] _RAND_11;
  reg [31:0] datas_1 [0:1023]; // @[Cache.scala 56:26]
  reg [31:0] _RAND_12;
  wire [31:0] datas_1_data_data; // @[Cache.scala 56:26]
  wire [9:0] datas_1_data_addr; // @[Cache.scala 56:26]
  wire [31:0] datas_1__T_56_data; // @[Cache.scala 56:26]
  wire [9:0] datas_1__T_56_addr; // @[Cache.scala 56:26]
  wire  datas_1__T_56_mask; // @[Cache.scala 56:26]
  wire  datas_1__T_56_en; // @[Cache.scala 56:26]
  reg  datas_1_data_en_pipe_0;
  reg [31:0] _RAND_13;
  reg [9:0] datas_1_data_addr_pipe_0;
  reg [31:0] _RAND_14;
  reg [31:0] datas_2 [0:1023]; // @[Cache.scala 56:26]
  reg [31:0] _RAND_15;
  wire [31:0] datas_2_data_data; // @[Cache.scala 56:26]
  wire [9:0] datas_2_data_addr; // @[Cache.scala 56:26]
  wire [31:0] datas_2__T_56_data; // @[Cache.scala 56:26]
  wire [9:0] datas_2__T_56_addr; // @[Cache.scala 56:26]
  wire  datas_2__T_56_mask; // @[Cache.scala 56:26]
  wire  datas_2__T_56_en; // @[Cache.scala 56:26]
  reg  datas_2_data_en_pipe_0;
  reg [31:0] _RAND_16;
  reg [9:0] datas_2_data_addr_pipe_0;
  reg [31:0] _RAND_17;
  reg [31:0] datas_3 [0:1023]; // @[Cache.scala 56:26]
  reg [31:0] _RAND_18;
  wire [31:0] datas_3_data_data; // @[Cache.scala 56:26]
  wire [9:0] datas_3_data_addr; // @[Cache.scala 56:26]
  wire [31:0] datas_3__T_56_data; // @[Cache.scala 56:26]
  wire [9:0] datas_3__T_56_addr; // @[Cache.scala 56:26]
  wire  datas_3__T_56_mask; // @[Cache.scala 56:26]
  wire  datas_3__T_56_en; // @[Cache.scala 56:26]
  reg  datas_3_data_en_pipe_0;
  reg [31:0] _RAND_19;
  reg [9:0] datas_3_data_addr_pipe_0;
  reg [31:0] _RAND_20;
  wire  _T; // @[Decoupled.scala 40:37]
  reg  _T_1_wr; // @[Reg.scala 15:16]
  reg [31:0] _RAND_21;
  reg [31:0] _T_1_addr; // @[Reg.scala 15:16]
  reg [31:0] _RAND_22;
  reg [31:0] _T_1_data; // @[Reg.scala 15:16]
  reg [31:0] _RAND_23;
  wire [64:0] _T_3; // @[Cache.scala 58:72]
  wire [31:0] reqReg_addr; // @[Cache.scala 58:72]
  reg  valid; // @[Cache.scala 59:22]
  reg [31:0] _RAND_24;
  wire  _GEN_3; // @[Cache.scala 61:29]
  wire [17:0] addrReq_tag; // @[Cache.scala 63:47]
  wire [1:0] addrReg_word; // @[Cache.scala 64:37]
  wire [9:0] addrReg_line; // @[Cache.scala 64:37]
  wire [17:0] addrReg_tag; // @[Cache.scala 64:37]
  wire  _T_28; // @[Cache.scala 68:34]
  wire  hit; // @[Cache.scala 68:23]
  wire [30:0] readMemAddr; // @[Cat.scala 29:58]
  wire [63:0] _T_34; // @[Cache.scala 76:37]
  wire [63:0] _T_35; // @[Cache.scala 76:37]
  reg [2:0] state; // @[Cache.scala 98:22]
  reg [31:0] _RAND_25;
  wire  _GEN_21; // @[Cache.scala 91:34]
  wire  _GEN_22; // @[Cache.scala 91:34]
  wire  _GEN_23; // @[Cache.scala 91:34]
  wire  _GEN_24; // @[Cache.scala 91:34]
  wire  cacheWrite_sel; // @[Cache.scala 122:27]
  wire  _T_63; // @[Conditional.scala 37:30]
  wire  _T_64; // @[Cache.scala 105:30]
  wire  _T_65; // @[Conditional.scala 37:30]
  wire  _T_66; // @[Conditional.scala 37:30]
  wire  _T_67; // @[Conditional.scala 37:30]
  wire  _T_68; // @[Conditional.scala 37:30]
  wire  _T_69; // @[Conditional.scala 37:30]
  wire  _T_70; // @[Decoupled.scala 40:37]
  wire  _T_71; // @[Cache.scala 120:28]
  wire  _T_72; // @[Cache.scala 120:53]
  wire [127:0] _T_78; // @[Cache.scala 124:82]
  wire [31:0] _GEN_62; // @[Cache.scala 124:38]
  wire [31:0] _GEN_63; // @[Cache.scala 124:38]
  wire [31:0] _GEN_64; // @[Cache.scala 124:38]
  wire [31:0] _GEN_65; // @[Cache.scala 124:38]
  assign tags_valid_tag_addr = tags_valid_tag_addr_pipe_0;
  assign tags_valid_tag_data = tags_valid[tags_valid_tag_addr]; // @[Cache.scala 55:25]
  assign tags_valid__T_43_data = 1'h1;
  assign tags_valid__T_43_addr = reqReg_addr[13:4];
  assign tags_valid__T_43_mask = 1'h1;
  assign tags_valid__T_43_en = state == 3'h5;
  assign tags_dirty_tag_addr = tags_dirty_tag_addr_pipe_0;
  assign tags_dirty_tag_data = tags_dirty[tags_dirty_tag_addr]; // @[Cache.scala 55:25]
  assign tags_dirty__T_43_data = 1'h1;
  assign tags_dirty__T_43_addr = reqReg_addr[13:4];
  assign tags_dirty__T_43_mask = 1'h1;
  assign tags_dirty__T_43_en = state == 3'h5;
  assign tags_tag_tag_addr = tags_tag_tag_addr_pipe_0;
  assign tags_tag_tag_data = tags_tag[tags_tag_tag_addr]; // @[Cache.scala 55:25]
  assign tags_tag__T_43_data = reqReg_addr[31:14];
  assign tags_tag__T_43_addr = reqReg_addr[13:4];
  assign tags_tag__T_43_mask = 1'h1;
  assign tags_tag__T_43_en = state == 3'h5;
  assign datas_0_data_addr = datas_0_data_addr_pipe_0;
  assign datas_0_data_data = datas_0[datas_0_data_addr]; // @[Cache.scala 56:26]
  assign datas_0__T_56_data = cacheWrite_sel ? io_memResp_bits_data[31:0] : io_cacheReq_bits_data;
  assign datas_0__T_56_addr = reqReg_addr[13:4];
  assign datas_0__T_56_mask = cacheWrite_sel | _GEN_21;
  assign datas_0__T_56_en = state == 3'h5;
  assign datas_1_data_addr = datas_1_data_addr_pipe_0;
  assign datas_1_data_data = datas_1[datas_1_data_addr]; // @[Cache.scala 56:26]
  assign datas_1__T_56_data = cacheWrite_sel ? io_memResp_bits_data[63:32] : io_cacheReq_bits_data;
  assign datas_1__T_56_addr = reqReg_addr[13:4];
  assign datas_1__T_56_mask = cacheWrite_sel | _GEN_22;
  assign datas_1__T_56_en = state == 3'h5;
  assign datas_2_data_addr = datas_2_data_addr_pipe_0;
  assign datas_2_data_data = datas_2[datas_2_data_addr]; // @[Cache.scala 56:26]
  assign datas_2__T_56_data = cacheWrite_sel ? io_memResp_bits_data[95:64] : io_cacheReq_bits_data;
  assign datas_2__T_56_addr = reqReg_addr[13:4];
  assign datas_2__T_56_mask = cacheWrite_sel | _GEN_23;
  assign datas_2__T_56_en = state == 3'h5;
  assign datas_3_data_addr = datas_3_data_addr_pipe_0;
  assign datas_3_data_data = datas_3[datas_3_data_addr]; // @[Cache.scala 56:26]
  assign datas_3__T_56_data = cacheWrite_sel ? io_memResp_bits_data[127:96] : io_cacheReq_bits_data;
  assign datas_3__T_56_addr = reqReg_addr[13:4];
  assign datas_3__T_56_mask = cacheWrite_sel | _GEN_24;
  assign datas_3__T_56_en = state == 3'h5;
  assign _T = io_cacheReq_ready & io_cacheReq_valid; // @[Decoupled.scala 40:37]
  assign _T_3 = {_T_1_wr,_T_1_addr,_T_1_data}; // @[Cache.scala 58:72]
  assign reqReg_addr = _T_3[63:32]; // @[Cache.scala 58:72]
  assign _GEN_3 = _T | valid; // @[Cache.scala 61:29]
  assign addrReq_tag = io_cacheReq_bits_addr[31:14]; // @[Cache.scala 63:47]
  assign addrReg_word = reqReg_addr[3:2]; // @[Cache.scala 64:37]
  assign addrReg_line = reqReg_addr[13:4]; // @[Cache.scala 64:37]
  assign addrReg_tag = reqReg_addr[31:14]; // @[Cache.scala 64:37]
  assign _T_28 = tags_tag_tag_data == addrReq_tag; // @[Cache.scala 68:34]
  assign hit = tags_valid_tag_data & _T_28; // @[Cache.scala 68:23]
  assign readMemAddr = {addrReg_tag,addrReg_line,addrReg_word,1'h0}; // @[Cat.scala 29:58]
  assign _T_34 = {datas_1_data_data,datas_0_data_data}; // @[Cache.scala 76:37]
  assign _T_35 = {datas_3_data_data,datas_2_data_data}; // @[Cache.scala 76:37]
  assign _GEN_21 = 2'h0 == addrReg_word; // @[Cache.scala 91:34]
  assign _GEN_22 = 2'h1 == addrReg_word; // @[Cache.scala 91:34]
  assign _GEN_23 = 2'h2 == addrReg_word; // @[Cache.scala 91:34]
  assign _GEN_24 = 2'h3 == addrReg_word; // @[Cache.scala 91:34]
  assign cacheWrite_sel = state == 3'h4; // @[Cache.scala 122:27]
  assign _T_63 = 3'h0 == state; // @[Conditional.scala 37:30]
  assign _T_64 = tags_valid_tag_data & tags_dirty_tag_data; // @[Cache.scala 105:30]
  assign _T_65 = 3'h1 == state; // @[Conditional.scala 37:30]
  assign _T_66 = 3'h2 == state; // @[Conditional.scala 37:30]
  assign _T_67 = 3'h3 == state; // @[Conditional.scala 37:30]
  assign _T_68 = 3'h4 == state; // @[Conditional.scala 37:30]
  assign _T_69 = 3'h5 == state; // @[Conditional.scala 37:30]
  assign _T_70 = io_cacheRespBundle_ready & io_cacheRespBundle_valid; // @[Decoupled.scala 40:37]
  assign _T_71 = state == 3'h1; // @[Cache.scala 120:28]
  assign _T_72 = state == 3'h3; // @[Cache.scala 120:53]
  assign _T_78 = io_memResp_bits_data >> addrReg_word; // @[Cache.scala 124:82]
  assign _GEN_62 = datas_0_data_data; // @[Cache.scala 124:38]
  assign _GEN_63 = 2'h1 == addrReg_word ? datas_1_data_data : _GEN_62; // @[Cache.scala 124:38]
  assign _GEN_64 = 2'h2 == addrReg_word ? datas_2_data_data : _GEN_63; // @[Cache.scala 124:38]
  assign _GEN_65 = 2'h3 == addrReg_word ? datas_3_data_data : _GEN_64; // @[Cache.scala 124:38]
  assign io_cacheReq_ready = ~valid; // @[Cache.scala 60:21]
  assign io_cacheRespBundle_valid = state == 3'h5; // @[Cache.scala 123:28]
  assign io_cacheRespBundle_bits_data = cacheWrite_sel ? {{31'd0}, _T_78[0]} : _GEN_65; // @[Cache.scala 124:32]
  assign io_memReq_valid = _T_71 | _T_72; // @[Cache.scala 120:19]
  assign io_memReq_bits_wr = 1'h0; // @[Cache.scala 74:21]
  assign io_memReq_bits_addr = {{1'd0}, readMemAddr}; // @[Cache.scala 75:23]
  assign io_memReq_bits_data = {_T_35,_T_34}; // @[Cache.scala 76:23]
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
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    tags_valid[initvar] = _RAND_0[0:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  tags_valid_tag_en_pipe_0 = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  tags_valid_tag_addr_pipe_0 = _RAND_2[9:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    tags_dirty[initvar] = _RAND_3[0:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  tags_dirty_tag_en_pipe_0 = _RAND_4[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  tags_dirty_tag_addr_pipe_0 = _RAND_5[9:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    tags_tag[initvar] = _RAND_6[17:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  tags_tag_tag_en_pipe_0 = _RAND_7[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{`RANDOM}};
  tags_tag_tag_addr_pipe_0 = _RAND_8[9:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_9 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    datas_0[initvar] = _RAND_9[31:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_10 = {1{`RANDOM}};
  datas_0_data_en_pipe_0 = _RAND_10[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_11 = {1{`RANDOM}};
  datas_0_data_addr_pipe_0 = _RAND_11[9:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_12 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    datas_1[initvar] = _RAND_12[31:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_13 = {1{`RANDOM}};
  datas_1_data_en_pipe_0 = _RAND_13[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_14 = {1{`RANDOM}};
  datas_1_data_addr_pipe_0 = _RAND_14[9:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_15 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    datas_2[initvar] = _RAND_15[31:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_16 = {1{`RANDOM}};
  datas_2_data_en_pipe_0 = _RAND_16[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_17 = {1{`RANDOM}};
  datas_2_data_addr_pipe_0 = _RAND_17[9:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_18 = {1{`RANDOM}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 1024; initvar = initvar+1)
    datas_3[initvar] = _RAND_18[31:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_19 = {1{`RANDOM}};
  datas_3_data_en_pipe_0 = _RAND_19[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_20 = {1{`RANDOM}};
  datas_3_data_addr_pipe_0 = _RAND_20[9:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_21 = {1{`RANDOM}};
  _T_1_wr = _RAND_21[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_22 = {1{`RANDOM}};
  _T_1_addr = _RAND_22[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_23 = {1{`RANDOM}};
  _T_1_data = _RAND_23[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_24 = {1{`RANDOM}};
  valid = _RAND_24[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_25 = {1{`RANDOM}};
  state = _RAND_25[2:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if(tags_valid__T_43_en & tags_valid__T_43_mask) begin
      tags_valid[tags_valid__T_43_addr] <= tags_valid__T_43_data; // @[Cache.scala 55:25]
    end
    tags_valid_tag_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      tags_valid_tag_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if(tags_dirty__T_43_en & tags_dirty__T_43_mask) begin
      tags_dirty[tags_dirty__T_43_addr] <= tags_dirty__T_43_data; // @[Cache.scala 55:25]
    end
    tags_dirty_tag_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      tags_dirty_tag_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if(tags_tag__T_43_en & tags_tag__T_43_mask) begin
      tags_tag[tags_tag__T_43_addr] <= tags_tag__T_43_data; // @[Cache.scala 55:25]
    end
    tags_tag_tag_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      tags_tag_tag_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if(datas_0__T_56_en & datas_0__T_56_mask) begin
      datas_0[datas_0__T_56_addr] <= datas_0__T_56_data; // @[Cache.scala 56:26]
    end
    datas_0_data_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      datas_0_data_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if(datas_1__T_56_en & datas_1__T_56_mask) begin
      datas_1[datas_1__T_56_addr] <= datas_1__T_56_data; // @[Cache.scala 56:26]
    end
    datas_1_data_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      datas_1_data_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if(datas_2__T_56_en & datas_2__T_56_mask) begin
      datas_2[datas_2__T_56_addr] <= datas_2__T_56_data; // @[Cache.scala 56:26]
    end
    datas_2_data_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      datas_2_data_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if(datas_3__T_56_en & datas_3__T_56_mask) begin
      datas_3[datas_3__T_56_addr] <= datas_3__T_56_data; // @[Cache.scala 56:26]
    end
    datas_3_data_en_pipe_0 <= io_cacheReq_ready & io_cacheReq_valid;
    if (io_cacheReq_ready & io_cacheReq_valid) begin
      datas_3_data_addr_pipe_0 <= io_cacheReq_bits_addr[13:4];
    end
    if (_T) begin
      _T_1_wr <= io_cacheReq_bits_wr;
    end
    if (_T) begin
      _T_1_addr <= io_cacheReq_bits_addr;
    end
    if (_T) begin
      _T_1_data <= io_cacheReq_bits_data;
    end
    if (reset) begin
      valid <= 1'h0;
    end else if (_T_63) begin
      if (valid) begin
        if (hit) begin
          valid <= 1'h0;
        end else begin
          valid <= _GEN_3;
        end
      end else begin
        valid <= _GEN_3;
      end
    end else if (_T_65) begin
      valid <= _GEN_3;
    end else if (_T_66) begin
      valid <= _GEN_3;
    end else if (_T_67) begin
      valid <= _GEN_3;
    end else if (_T_68) begin
      if (io_memResp_valid) begin
        valid <= 1'h0;
      end else begin
        valid <= _GEN_3;
      end
    end else begin
      valid <= _GEN_3;
    end
    if (reset) begin
      state <= 3'h0;
    end else if (_T_63) begin
      if (valid) begin
        if (hit) begin
          state <= 3'h5;
        end else if (_T_64) begin
          state <= 3'h1;
        end
      end
    end else if (_T_65) begin
      state <= 3'h2;
    end else if (_T_66) begin
      if (io_memResp_valid) begin
        state <= 3'h3;
      end
    end else if (_T_67) begin
      state <= 3'h4;
    end else if (_T_68) begin
      if (io_memResp_valid) begin
        state <= 3'h5;
      end
    end else if (_T_69) begin
      if (_T_70) begin
        state <= 3'h0;
      end
    end
  end
endmodule
