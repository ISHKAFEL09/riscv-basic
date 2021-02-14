// Generator : SpinalHDL v1.4.0    git head : 96467330de902dd3909050e284757af593f5b2d1
// Date      : 26/05/2020, 00:20:40
// Component : RegFile



module RegFile (
  input      [4:0]    io_regio_raddr1,
  input      [4:0]    io_regio_raddr2,
  input      [4:0]    io_regio_waddr,
  input      [63:0]   io_regio_wdata,
  input               io_regio_wen,
  output reg [63:0]   io_regio_rdata1,
  output reg [63:0]   io_regio_rdata2,
  input               clk,
  input               reset 
);
  wire       [63:0]   _zz_1_;
  wire       [63:0]   _zz_2_;
  reg [63:0] mem [0:31];

  always @ (posedge clk) begin
    if(io_regio_wen) begin
      mem[io_regio_waddr] <= io_regio_wdata;
    end
  end

  assign _zz_1_ = mem[io_regio_raddr1];
  assign _zz_2_ = mem[io_regio_raddr2];
  always @ (*) begin
    if((io_regio_raddr1 != 5'h0))begin
      io_regio_rdata1 = _zz_1_;
    end else begin
      io_regio_rdata1 = 64'h0;
    end
  end

  always @ (*) begin
    if((io_regio_raddr2 != 5'h0))begin
      io_regio_rdata2 = _zz_2_;
    end else begin
      io_regio_rdata2 = 64'h0;
    end
  end


endmodule
