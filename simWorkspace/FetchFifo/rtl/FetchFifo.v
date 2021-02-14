// Generator : SpinalHDL v1.4.0    git head : 96467330de902dd3909050e284757af593f5b2d1
// Date      : 07/06/2020, 21:45:18
// Component : FetchFifo



module FetchFifo (
  input               io_clear,
  input               io_dataIn_valid,
  output              io_dataIn_ready,
  input      [31:0]   io_dataIn_payload,
  output reg          io_dataOut_valid,
  input               io_dataOut_ready,
  output reg [31:0]   io_dataOut_payload,
  input      [31:0]   io_pcIn,
  output     [31:0]   io_pcOut,
  input               clk,
  input               reset 
);
  wire                _zz_1_;
  wire       [30:0]   _zz_2_;
  wire       [30:0]   _zz_3_;
  reg        [2:0]    valid_q;
  reg        [31:0]   rdata_q_0;
  reg        [31:0]   rdata_q_1;
  reg        [31:0]   rdata_q_2;
  wire       [31:0]   rdata;
  wire                valid;
  wire       [31:0]   rdata_unaligned;
  wire                valid_unaligned;
  wire                unaligned_is_compressed;
  wire                aligned_is_compressed;
  reg        [30:0]   pc;
  wire                pc_en;
  wire       [2:0]    pc_incr;
  wire                pop_fifo;
  wire       [2:0]    lowest_free_entry;
  wire       [2:0]    valid_pushed;
  wire       [2:0]    valid_popped;
  wire       [2:0]    valid_d;
  wire       [2:0]    entry_en;

  assign _zz_1_ = io_pcOut[1];
  assign _zz_2_ = (pc + _zz_3_);
  assign _zz_3_ = {28'd0, pc_incr};
  assign rdata = (valid_q[0] ? rdata_q_0 : io_dataIn_payload);
  assign valid = (valid_q[0] || io_dataIn_valid);
  assign rdata_unaligned = (valid_q[1] ? {rdata_q_1[15 : 0],rdata[31 : 16]} : {io_dataIn_payload[15 : 0],rdata[31 : 16]});
  assign valid_unaligned = (valid_q[1] ? 1'b1 : (valid_q[0] && io_dataIn_valid));
  assign unaligned_is_compressed = (rdata[17 : 16] != (2'b11));
  assign aligned_is_compressed = (rdata[1 : 0] != (2'b11));
  always @ (*) begin
    if(_zz_1_)begin
      io_dataOut_payload = rdata_unaligned;
    end else begin
      io_dataOut_payload = rdata;
    end
  end

  always @ (*) begin
    if(_zz_1_)begin
      io_dataOut_valid = (unaligned_is_compressed ? valid : valid_unaligned);
    end else begin
      io_dataOut_valid = valid;
    end
  end

  assign pc_en = (io_clear || (io_dataOut_ready && io_dataOut_valid));
  assign pc_incr = (io_pcOut[1] ? (unaligned_is_compressed ? (3'b010) : (3'b100)) : (aligned_is_compressed ? (3'b010) : (3'b100)));
  assign io_pcOut = {pc,(1'b0)};
  assign io_dataIn_ready = (! valid_q[1]);
  assign pop_fifo = ((io_dataOut_ready && io_dataOut_valid) && ((! aligned_is_compressed) || io_pcOut[1]));
  assign lowest_free_entry = ((~ valid_q) & {valid_q[1 : 0],1'b1});
  assign valid_pushed = ((io_dataIn_valid ? lowest_free_entry : (3'b000)) | valid_q);
  assign valid_popped = (pop_fifo ? {1'b1,valid_pushed[2 : 1]} : valid_pushed);
  assign valid_d = (io_clear ? (3'b000) : valid_popped);
  assign entry_en = (pop_fifo ? {1'b0,valid_pushed[2 : 1]} : (io_dataIn_valid ? lowest_free_entry : (3'b000)));
  always @ (posedge clk or posedge reset) begin
    if (reset) begin
      rdata_q_0 <= 32'h0;
      rdata_q_1 <= 32'h0;
      rdata_q_2 <= 32'h0;
      pc <= 31'h0;
    end else begin
      if(pc_en)begin
        pc <= (io_clear ? io_pcIn[31 : 1] : _zz_2_);
      end
      if(entry_en[2])begin
        rdata_q_2 <= io_dataIn_payload;
      end
      if(entry_en[0])begin
        rdata_q_0 <= (valid_q[1] ? rdata_q_1 : io_dataIn_payload);
      end
      if(entry_en[1])begin
        rdata_q_1 <= (valid_q[2] ? rdata_q_2 : io_dataIn_payload);
      end
    end
  end

  always @ (posedge clk) begin
    valid_q <= valid_d;
  end


endmodule
