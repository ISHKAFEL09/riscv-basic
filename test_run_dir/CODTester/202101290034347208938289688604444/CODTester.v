module StageIF(
  input   clock,
  input   reset
);
  reg [31:0] pc; // @[StageIF.scala 35:19]
  reg [31:0] _RAND_0;
  wire [31:0] pcPlus4; // @[StageIF.scala 37:20]
  wire [6:0] _GEN_2; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_3; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_4; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_5; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_6; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_7; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_8; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_9; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_10; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_11; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_12; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_13; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_14; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_15; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_16; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_17; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_18; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_19; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_20; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_21; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_22; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_23; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_24; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_25; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_26; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_27; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_28; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_29; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_30; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_31; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_32; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_33; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_34; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_35; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_36; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_37; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_38; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_39; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_40; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_41; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_42; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_43; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_44; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_45; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_46; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_47; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_48; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_49; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_50; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_51; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_52; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_53; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_54; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_55; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_56; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_57; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_58; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_59; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_60; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_61; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_62; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_63; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_64; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_65; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_66; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_67; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_68; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_69; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_70; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_71; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_72; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_73; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_74; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_75; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_76; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_77; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_78; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_79; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_80; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_81; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_82; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_83; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_84; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_85; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_86; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_87; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_88; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_89; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_90; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_91; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_92; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_93; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_94; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_95; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_96; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_97; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_98; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_99; // @[StageIF.scala 52:11]
  wire [6:0] _GEN_100; // @[StageIF.scala 52:11]
  wire [31:0] instr; // @[StageIF.scala 48:19 StageIF.scala 52:11]
  wire  _T_12; // @[StageIF.scala 75:9]
  assign pcPlus4 = pc + 32'h4; // @[StageIF.scala 37:20]
  assign _GEN_2 = 7'h1 == pc[6:0] ? 7'h1 : 7'h0; // @[StageIF.scala 52:11]
  assign _GEN_3 = 7'h2 == pc[6:0] ? 7'h2 : _GEN_2; // @[StageIF.scala 52:11]
  assign _GEN_4 = 7'h3 == pc[6:0] ? 7'h3 : _GEN_3; // @[StageIF.scala 52:11]
  assign _GEN_5 = 7'h4 == pc[6:0] ? 7'h4 : _GEN_4; // @[StageIF.scala 52:11]
  assign _GEN_6 = 7'h5 == pc[6:0] ? 7'h5 : _GEN_5; // @[StageIF.scala 52:11]
  assign _GEN_7 = 7'h6 == pc[6:0] ? 7'h6 : _GEN_6; // @[StageIF.scala 52:11]
  assign _GEN_8 = 7'h7 == pc[6:0] ? 7'h7 : _GEN_7; // @[StageIF.scala 52:11]
  assign _GEN_9 = 7'h8 == pc[6:0] ? 7'h8 : _GEN_8; // @[StageIF.scala 52:11]
  assign _GEN_10 = 7'h9 == pc[6:0] ? 7'h9 : _GEN_9; // @[StageIF.scala 52:11]
  assign _GEN_11 = 7'ha == pc[6:0] ? 7'ha : _GEN_10; // @[StageIF.scala 52:11]
  assign _GEN_12 = 7'hb == pc[6:0] ? 7'hb : _GEN_11; // @[StageIF.scala 52:11]
  assign _GEN_13 = 7'hc == pc[6:0] ? 7'hc : _GEN_12; // @[StageIF.scala 52:11]
  assign _GEN_14 = 7'hd == pc[6:0] ? 7'hd : _GEN_13; // @[StageIF.scala 52:11]
  assign _GEN_15 = 7'he == pc[6:0] ? 7'he : _GEN_14; // @[StageIF.scala 52:11]
  assign _GEN_16 = 7'hf == pc[6:0] ? 7'hf : _GEN_15; // @[StageIF.scala 52:11]
  assign _GEN_17 = 7'h10 == pc[6:0] ? 7'h10 : _GEN_16; // @[StageIF.scala 52:11]
  assign _GEN_18 = 7'h11 == pc[6:0] ? 7'h11 : _GEN_17; // @[StageIF.scala 52:11]
  assign _GEN_19 = 7'h12 == pc[6:0] ? 7'h12 : _GEN_18; // @[StageIF.scala 52:11]
  assign _GEN_20 = 7'h13 == pc[6:0] ? 7'h13 : _GEN_19; // @[StageIF.scala 52:11]
  assign _GEN_21 = 7'h14 == pc[6:0] ? 7'h14 : _GEN_20; // @[StageIF.scala 52:11]
  assign _GEN_22 = 7'h15 == pc[6:0] ? 7'h15 : _GEN_21; // @[StageIF.scala 52:11]
  assign _GEN_23 = 7'h16 == pc[6:0] ? 7'h16 : _GEN_22; // @[StageIF.scala 52:11]
  assign _GEN_24 = 7'h17 == pc[6:0] ? 7'h17 : _GEN_23; // @[StageIF.scala 52:11]
  assign _GEN_25 = 7'h18 == pc[6:0] ? 7'h18 : _GEN_24; // @[StageIF.scala 52:11]
  assign _GEN_26 = 7'h19 == pc[6:0] ? 7'h19 : _GEN_25; // @[StageIF.scala 52:11]
  assign _GEN_27 = 7'h1a == pc[6:0] ? 7'h1a : _GEN_26; // @[StageIF.scala 52:11]
  assign _GEN_28 = 7'h1b == pc[6:0] ? 7'h1b : _GEN_27; // @[StageIF.scala 52:11]
  assign _GEN_29 = 7'h1c == pc[6:0] ? 7'h1c : _GEN_28; // @[StageIF.scala 52:11]
  assign _GEN_30 = 7'h1d == pc[6:0] ? 7'h1d : _GEN_29; // @[StageIF.scala 52:11]
  assign _GEN_31 = 7'h1e == pc[6:0] ? 7'h1e : _GEN_30; // @[StageIF.scala 52:11]
  assign _GEN_32 = 7'h1f == pc[6:0] ? 7'h1f : _GEN_31; // @[StageIF.scala 52:11]
  assign _GEN_33 = 7'h20 == pc[6:0] ? 7'h20 : _GEN_32; // @[StageIF.scala 52:11]
  assign _GEN_34 = 7'h21 == pc[6:0] ? 7'h21 : _GEN_33; // @[StageIF.scala 52:11]
  assign _GEN_35 = 7'h22 == pc[6:0] ? 7'h22 : _GEN_34; // @[StageIF.scala 52:11]
  assign _GEN_36 = 7'h23 == pc[6:0] ? 7'h23 : _GEN_35; // @[StageIF.scala 52:11]
  assign _GEN_37 = 7'h24 == pc[6:0] ? 7'h24 : _GEN_36; // @[StageIF.scala 52:11]
  assign _GEN_38 = 7'h25 == pc[6:0] ? 7'h25 : _GEN_37; // @[StageIF.scala 52:11]
  assign _GEN_39 = 7'h26 == pc[6:0] ? 7'h26 : _GEN_38; // @[StageIF.scala 52:11]
  assign _GEN_40 = 7'h27 == pc[6:0] ? 7'h27 : _GEN_39; // @[StageIF.scala 52:11]
  assign _GEN_41 = 7'h28 == pc[6:0] ? 7'h28 : _GEN_40; // @[StageIF.scala 52:11]
  assign _GEN_42 = 7'h29 == pc[6:0] ? 7'h29 : _GEN_41; // @[StageIF.scala 52:11]
  assign _GEN_43 = 7'h2a == pc[6:0] ? 7'h2a : _GEN_42; // @[StageIF.scala 52:11]
  assign _GEN_44 = 7'h2b == pc[6:0] ? 7'h2b : _GEN_43; // @[StageIF.scala 52:11]
  assign _GEN_45 = 7'h2c == pc[6:0] ? 7'h2c : _GEN_44; // @[StageIF.scala 52:11]
  assign _GEN_46 = 7'h2d == pc[6:0] ? 7'h2d : _GEN_45; // @[StageIF.scala 52:11]
  assign _GEN_47 = 7'h2e == pc[6:0] ? 7'h2e : _GEN_46; // @[StageIF.scala 52:11]
  assign _GEN_48 = 7'h2f == pc[6:0] ? 7'h2f : _GEN_47; // @[StageIF.scala 52:11]
  assign _GEN_49 = 7'h30 == pc[6:0] ? 7'h30 : _GEN_48; // @[StageIF.scala 52:11]
  assign _GEN_50 = 7'h31 == pc[6:0] ? 7'h31 : _GEN_49; // @[StageIF.scala 52:11]
  assign _GEN_51 = 7'h32 == pc[6:0] ? 7'h32 : _GEN_50; // @[StageIF.scala 52:11]
  assign _GEN_52 = 7'h33 == pc[6:0] ? 7'h33 : _GEN_51; // @[StageIF.scala 52:11]
  assign _GEN_53 = 7'h34 == pc[6:0] ? 7'h34 : _GEN_52; // @[StageIF.scala 52:11]
  assign _GEN_54 = 7'h35 == pc[6:0] ? 7'h35 : _GEN_53; // @[StageIF.scala 52:11]
  assign _GEN_55 = 7'h36 == pc[6:0] ? 7'h36 : _GEN_54; // @[StageIF.scala 52:11]
  assign _GEN_56 = 7'h37 == pc[6:0] ? 7'h37 : _GEN_55; // @[StageIF.scala 52:11]
  assign _GEN_57 = 7'h38 == pc[6:0] ? 7'h38 : _GEN_56; // @[StageIF.scala 52:11]
  assign _GEN_58 = 7'h39 == pc[6:0] ? 7'h39 : _GEN_57; // @[StageIF.scala 52:11]
  assign _GEN_59 = 7'h3a == pc[6:0] ? 7'h3a : _GEN_58; // @[StageIF.scala 52:11]
  assign _GEN_60 = 7'h3b == pc[6:0] ? 7'h3b : _GEN_59; // @[StageIF.scala 52:11]
  assign _GEN_61 = 7'h3c == pc[6:0] ? 7'h3c : _GEN_60; // @[StageIF.scala 52:11]
  assign _GEN_62 = 7'h3d == pc[6:0] ? 7'h3d : _GEN_61; // @[StageIF.scala 52:11]
  assign _GEN_63 = 7'h3e == pc[6:0] ? 7'h3e : _GEN_62; // @[StageIF.scala 52:11]
  assign _GEN_64 = 7'h3f == pc[6:0] ? 7'h3f : _GEN_63; // @[StageIF.scala 52:11]
  assign _GEN_65 = 7'h40 == pc[6:0] ? 7'h40 : _GEN_64; // @[StageIF.scala 52:11]
  assign _GEN_66 = 7'h41 == pc[6:0] ? 7'h41 : _GEN_65; // @[StageIF.scala 52:11]
  assign _GEN_67 = 7'h42 == pc[6:0] ? 7'h42 : _GEN_66; // @[StageIF.scala 52:11]
  assign _GEN_68 = 7'h43 == pc[6:0] ? 7'h43 : _GEN_67; // @[StageIF.scala 52:11]
  assign _GEN_69 = 7'h44 == pc[6:0] ? 7'h44 : _GEN_68; // @[StageIF.scala 52:11]
  assign _GEN_70 = 7'h45 == pc[6:0] ? 7'h45 : _GEN_69; // @[StageIF.scala 52:11]
  assign _GEN_71 = 7'h46 == pc[6:0] ? 7'h46 : _GEN_70; // @[StageIF.scala 52:11]
  assign _GEN_72 = 7'h47 == pc[6:0] ? 7'h47 : _GEN_71; // @[StageIF.scala 52:11]
  assign _GEN_73 = 7'h48 == pc[6:0] ? 7'h48 : _GEN_72; // @[StageIF.scala 52:11]
  assign _GEN_74 = 7'h49 == pc[6:0] ? 7'h49 : _GEN_73; // @[StageIF.scala 52:11]
  assign _GEN_75 = 7'h4a == pc[6:0] ? 7'h4a : _GEN_74; // @[StageIF.scala 52:11]
  assign _GEN_76 = 7'h4b == pc[6:0] ? 7'h4b : _GEN_75; // @[StageIF.scala 52:11]
  assign _GEN_77 = 7'h4c == pc[6:0] ? 7'h4c : _GEN_76; // @[StageIF.scala 52:11]
  assign _GEN_78 = 7'h4d == pc[6:0] ? 7'h4d : _GEN_77; // @[StageIF.scala 52:11]
  assign _GEN_79 = 7'h4e == pc[6:0] ? 7'h4e : _GEN_78; // @[StageIF.scala 52:11]
  assign _GEN_80 = 7'h4f == pc[6:0] ? 7'h4f : _GEN_79; // @[StageIF.scala 52:11]
  assign _GEN_81 = 7'h50 == pc[6:0] ? 7'h50 : _GEN_80; // @[StageIF.scala 52:11]
  assign _GEN_82 = 7'h51 == pc[6:0] ? 7'h51 : _GEN_81; // @[StageIF.scala 52:11]
  assign _GEN_83 = 7'h52 == pc[6:0] ? 7'h52 : _GEN_82; // @[StageIF.scala 52:11]
  assign _GEN_84 = 7'h53 == pc[6:0] ? 7'h53 : _GEN_83; // @[StageIF.scala 52:11]
  assign _GEN_85 = 7'h54 == pc[6:0] ? 7'h54 : _GEN_84; // @[StageIF.scala 52:11]
  assign _GEN_86 = 7'h55 == pc[6:0] ? 7'h55 : _GEN_85; // @[StageIF.scala 52:11]
  assign _GEN_87 = 7'h56 == pc[6:0] ? 7'h56 : _GEN_86; // @[StageIF.scala 52:11]
  assign _GEN_88 = 7'h57 == pc[6:0] ? 7'h57 : _GEN_87; // @[StageIF.scala 52:11]
  assign _GEN_89 = 7'h58 == pc[6:0] ? 7'h58 : _GEN_88; // @[StageIF.scala 52:11]
  assign _GEN_90 = 7'h59 == pc[6:0] ? 7'h59 : _GEN_89; // @[StageIF.scala 52:11]
  assign _GEN_91 = 7'h5a == pc[6:0] ? 7'h5a : _GEN_90; // @[StageIF.scala 52:11]
  assign _GEN_92 = 7'h5b == pc[6:0] ? 7'h5b : _GEN_91; // @[StageIF.scala 52:11]
  assign _GEN_93 = 7'h5c == pc[6:0] ? 7'h5c : _GEN_92; // @[StageIF.scala 52:11]
  assign _GEN_94 = 7'h5d == pc[6:0] ? 7'h5d : _GEN_93; // @[StageIF.scala 52:11]
  assign _GEN_95 = 7'h5e == pc[6:0] ? 7'h5e : _GEN_94; // @[StageIF.scala 52:11]
  assign _GEN_96 = 7'h5f == pc[6:0] ? 7'h5f : _GEN_95; // @[StageIF.scala 52:11]
  assign _GEN_97 = 7'h60 == pc[6:0] ? 7'h60 : _GEN_96; // @[StageIF.scala 52:11]
  assign _GEN_98 = 7'h61 == pc[6:0] ? 7'h61 : _GEN_97; // @[StageIF.scala 52:11]
  assign _GEN_99 = 7'h62 == pc[6:0] ? 7'h62 : _GEN_98; // @[StageIF.scala 52:11]
  assign _GEN_100 = 7'h63 == pc[6:0] ? 7'h63 : _GEN_99; // @[StageIF.scala 52:11]
  assign instr = {{25'd0}, _GEN_100}; // @[StageIF.scala 48:19 StageIF.scala 52:11]
  assign _T_12 = ~reset; // @[StageIF.scala 75:9]
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
  pc = _RAND_0[31:0];
  `endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      pc <= 32'h0;
    end else begin
      pc <= pcPlus4;
    end
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_12) begin
          $fwrite(32'h80000002,"pc: %x, instr: %x\n",pc,instr); // @[StageIF.scala 75:9]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule
module CODTester(
  input   clock,
  input   reset
);
  wire  dut_clock; // @[CODTest.scala 12:19]
  wire  dut_reset; // @[CODTest.scala 12:19]
  StageIF dut ( // @[CODTest.scala 12:19]
    .clock(dut_clock),
    .reset(dut_reset)
  );
  assign dut_clock = clock;
  assign dut_reset = reset;
endmodule
