// Generator : SpinalHDL v1.4.0    git head : 96467330de902dd3909050e284757af593f5b2d1
// Date      : 05/05/2020, 22:31:43
// Component : ALU



module ALU (
  input      [3:0]    io_opcode,
  input      [31:0]   io_op1,
  input      [31:0]   io_op2,
  output     [31:0]   io_aluOut 
);
  reg        [31:0]   _zz_1_;

  always @ (*) begin
    case(io_opcode)
      4'b0000 : begin
        _zz_1_ = (io_op1 & io_op2);
      end
      4'b0001, 4'b0101 : begin
        _zz_1_ = (io_op1 | io_op2);
      end
      4'b0010 : begin
        _zz_1_ = (io_op1 + io_op2);
      end
      4'b0110 : begin
        _zz_1_ = (io_op1 - io_op2);
      end
      default : begin
        _zz_1_ = 32'h0;
      end
    endcase
  end

  assign io_aluOut = _zz_1_;

endmodule
