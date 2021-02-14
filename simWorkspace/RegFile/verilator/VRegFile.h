// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Primary design header
//
// This header should be included by all source files instantiating the design.
// The class here is then constructed to instantiate the design.
// See the Verilator manual for examples.

#ifndef _VREGFILE_H_
#define _VREGFILE_H_  // guard

#include "verilated.h"

//==========

class VRegFile__Syms;
class VRegFile_VerilatedVcd;


//----------

VL_MODULE(VRegFile) {
  public:
    
    // PORTS
    // The application code writes and reads these signals to
    // propagate new values into/out from the Verilated model.
    VL_IN8(clk,0,0);
    VL_IN8(io_regio_raddr1,4,0);
    VL_IN8(io_regio_raddr2,4,0);
    VL_IN8(io_regio_waddr,4,0);
    VL_IN8(io_regio_wen,0,0);
    VL_IN8(reset,0,0);
    VL_IN64(io_regio_wdata,63,0);
    VL_OUT64(io_regio_rdata1,63,0);
    VL_OUT64(io_regio_rdata2,63,0);
    
    // LOCAL SIGNALS
    // Internals; generally not touched by application code
    QData/*63:0*/ RegFile__DOT__mem[32];
    
    // LOCAL VARIABLES
    // Internals; generally not touched by application code
    CData/*0:0*/ __Vclklast__TOP__clk;
    IData/*31:0*/ __Vm_traceActivity;
    
    // INTERNAL VARIABLES
    // Internals; generally not touched by application code
    VRegFile__Syms* __VlSymsp;  // Symbol table
    
    // CONSTRUCTORS
  private:
    VL_UNCOPYABLE(VRegFile);  ///< Copying not allowed
  public:
    /// Construct the model; called by application code
    /// The special name  may be used to make a wrapper with a
    /// single model invisible with respect to DPI scope names.
    VRegFile(const char* name = "TOP");
    /// Destroy the model; called (often implicitly) by application code
    ~VRegFile();
    /// Trace signals in the model; called by application code
    void trace(VerilatedVcdC* tfp, int levels, int options = 0);
    
    // API METHODS
    /// Evaluate the model.  Application must call when inputs change.
    void eval() { eval_step(); }
    /// Evaluate when calling multiple units/models per time step.
    void eval_step();
    /// Evaluate at end of a timestep for tracing, when using eval_step().
    /// Application must call after all eval() and before time changes.
    void eval_end_step() {}
    /// Simulation complete, run final blocks.  Application must call on completion.
    void final();
    
    // INTERNAL METHODS
  private:
    static void _eval_initial_loop(VRegFile__Syms* __restrict vlSymsp);
  public:
    void __Vconfigure(VRegFile__Syms* symsp, bool first);
  private:
    static QData _change_request(VRegFile__Syms* __restrict vlSymsp);
    static QData _change_request_1(VRegFile__Syms* __restrict vlSymsp);
    void _ctor_var_reset() VL_ATTR_COLD;
  public:
    static void _eval(VRegFile__Syms* __restrict vlSymsp);
  private:
#ifdef VL_DEBUG
    void _eval_debug_assertions();
#endif  // VL_DEBUG
  public:
    static void _eval_initial(VRegFile__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _eval_settle(VRegFile__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _sequent__TOP__1(VRegFile__Syms* __restrict vlSymsp);
    static void _settle__TOP__2(VRegFile__Syms* __restrict vlSymsp);
    static void traceChgThis(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__2(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__3(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceFullThis(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceFullThis__1(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis__1(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code);
} VL_ATTR_ALIGNED(VL_CACHE_LINE_BYTES);

//----------


#endif  // guard
