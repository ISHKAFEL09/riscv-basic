// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Primary design header
//
// This header should be included by all source files instantiating the design.
// The class here is then constructed to instantiate the design.
// See the Verilator manual for examples.

#ifndef _VFETCHFIFO_H_
#define _VFETCHFIFO_H_  // guard

#include "verilated.h"

//==========

class VFetchFifo__Syms;
class VFetchFifo_VerilatedVcd;


//----------

VL_MODULE(VFetchFifo) {
  public:
    
    // PORTS
    // The application code writes and reads these signals to
    // propagate new values into/out from the Verilated model.
    VL_IN8(clk,0,0);
    VL_IN8(reset,0,0);
    VL_IN8(io_clear,0,0);
    VL_IN8(io_dataIn_valid,0,0);
    VL_OUT8(io_dataIn_ready,0,0);
    VL_OUT8(io_dataOut_valid,0,0);
    VL_IN8(io_dataOut_ready,0,0);
    VL_IN(io_dataIn_payload,31,0);
    VL_OUT(io_dataOut_payload,31,0);
    VL_IN(io_pcIn,31,0);
    VL_OUT(io_pcOut,31,0);
    
    // LOCAL SIGNALS
    // Internals; generally not touched by application code
    CData/*2:0*/ FetchFifo__DOT__valid_q;
    CData/*0:0*/ FetchFifo__DOT__valid;
    CData/*0:0*/ FetchFifo__DOT__pop_fifo;
    CData/*2:0*/ FetchFifo__DOT__lowest_free_entry;
    CData/*2:0*/ FetchFifo__DOT__valid_pushed;
    CData/*2:0*/ FetchFifo__DOT__entry_en;
    IData/*30:0*/ FetchFifo__DOT___zz_2_;
    IData/*31:0*/ FetchFifo__DOT__rdata_q_0;
    IData/*31:0*/ FetchFifo__DOT__rdata_q_1;
    IData/*31:0*/ FetchFifo__DOT__rdata_q_2;
    IData/*31:0*/ FetchFifo__DOT__rdata;
    IData/*30:0*/ FetchFifo__DOT__pc;
    
    // LOCAL VARIABLES
    // Internals; generally not touched by application code
    CData/*0:0*/ __Vclklast__TOP__clk;
    CData/*0:0*/ __Vclklast__TOP__reset;
    IData/*31:0*/ __Vm_traceActivity;
    
    // INTERNAL VARIABLES
    // Internals; generally not touched by application code
    VFetchFifo__Syms* __VlSymsp;  // Symbol table
    
    // CONSTRUCTORS
  private:
    VL_UNCOPYABLE(VFetchFifo);  ///< Copying not allowed
  public:
    /// Construct the model; called by application code
    /// The special name  may be used to make a wrapper with a
    /// single model invisible with respect to DPI scope names.
    VFetchFifo(const char* name = "TOP");
    /// Destroy the model; called (often implicitly) by application code
    ~VFetchFifo();
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
    static void _eval_initial_loop(VFetchFifo__Syms* __restrict vlSymsp);
  public:
    void __Vconfigure(VFetchFifo__Syms* symsp, bool first);
  private:
    static QData _change_request(VFetchFifo__Syms* __restrict vlSymsp);
    static QData _change_request_1(VFetchFifo__Syms* __restrict vlSymsp);
  public:
    static void _combo__TOP__4(VFetchFifo__Syms* __restrict vlSymsp);
  private:
    void _ctor_var_reset() VL_ATTR_COLD;
  public:
    static void _eval(VFetchFifo__Syms* __restrict vlSymsp);
  private:
#ifdef VL_DEBUG
    void _eval_debug_assertions();
#endif  // VL_DEBUG
  public:
    static void _eval_initial(VFetchFifo__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _eval_settle(VFetchFifo__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _sequent__TOP__1(VFetchFifo__Syms* __restrict vlSymsp);
    static void _sequent__TOP__2(VFetchFifo__Syms* __restrict vlSymsp);
    static void _settle__TOP__3(VFetchFifo__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void traceChgThis(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__2(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__3(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__4(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__5(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__6(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceFullThis(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceFullThis__1(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis__1(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code);
} VL_ATTR_ALIGNED(VL_CACHE_LINE_BYTES);

//----------


#endif  // guard
