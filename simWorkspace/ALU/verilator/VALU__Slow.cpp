// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VALU.h for the primary calling header

#include "VALU.h"
#include "VALU__Syms.h"

//==========

VL_CTOR_IMP(VALU) {
    VALU__Syms* __restrict vlSymsp = __VlSymsp = new VALU__Syms(this, name());
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Reset internal values
    
    // Reset structure values
    _ctor_var_reset();
}

void VALU::__Vconfigure(VALU__Syms* vlSymsp, bool first) {
    if (0 && first) {}  // Prevent unused
    this->__VlSymsp = vlSymsp;
}

VALU::~VALU() {
    delete __VlSymsp; __VlSymsp=NULL;
}

void VALU::_eval_initial(VALU__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VALU::_eval_initial\n"); );
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VALU::final() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VALU::final\n"); );
    // Variables
    VALU__Syms* __restrict vlSymsp = this->__VlSymsp;
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VALU::_eval_settle(VALU__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VALU::_eval_settle\n"); );
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->_combo__TOP__1(vlSymsp);
}

void VALU::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VALU::_ctor_var_reset\n"); );
    // Body
    io_opcode = VL_RAND_RESET_I(4);
    io_op1 = VL_RAND_RESET_I(32);
    io_op2 = VL_RAND_RESET_I(32);
    io_aluOut = VL_RAND_RESET_I(32);
    __Vm_traceActivity = 0;
}
