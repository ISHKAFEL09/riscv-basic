// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VRegFile.h for the primary calling header

#include "VRegFile.h"
#include "VRegFile__Syms.h"

//==========

VL_CTOR_IMP(VRegFile) {
    VRegFile__Syms* __restrict vlSymsp = __VlSymsp = new VRegFile__Syms(this, name());
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Reset internal values
    
    // Reset structure values
    _ctor_var_reset();
}

void VRegFile::__Vconfigure(VRegFile__Syms* vlSymsp, bool first) {
    if (0 && first) {}  // Prevent unused
    this->__VlSymsp = vlSymsp;
}

VRegFile::~VRegFile() {
    delete __VlSymsp; __VlSymsp=NULL;
}

void VRegFile::_eval_initial(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_eval_initial\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->__Vclklast__TOP__clk = vlTOPp->clk;
}

void VRegFile::final() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::final\n"); );
    // Variables
    VRegFile__Syms* __restrict vlSymsp = this->__VlSymsp;
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VRegFile::_eval_settle(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_eval_settle\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->_settle__TOP__2(vlSymsp);
}

void VRegFile::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_ctor_var_reset\n"); );
    // Body
    io_regio_raddr1 = VL_RAND_RESET_I(5);
    io_regio_raddr2 = VL_RAND_RESET_I(5);
    io_regio_waddr = VL_RAND_RESET_I(5);
    io_regio_wdata = VL_RAND_RESET_Q(64);
    io_regio_wen = VL_RAND_RESET_I(1);
    io_regio_rdata1 = VL_RAND_RESET_Q(64);
    io_regio_rdata2 = VL_RAND_RESET_Q(64);
    clk = VL_RAND_RESET_I(1);
    reset = VL_RAND_RESET_I(1);
    { int __Vi0=0; for (; __Vi0<32; ++__Vi0) {
            RegFile__DOT__mem[__Vi0] = VL_RAND_RESET_Q(64);
    }}
    __Vm_traceActivity = 0;
}
