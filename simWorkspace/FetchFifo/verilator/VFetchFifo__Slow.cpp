// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VFetchFifo.h for the primary calling header

#include "VFetchFifo.h"
#include "VFetchFifo__Syms.h"

//==========

VL_CTOR_IMP(VFetchFifo) {
    VFetchFifo__Syms* __restrict vlSymsp = __VlSymsp = new VFetchFifo__Syms(this, name());
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Reset internal values
    
    // Reset structure values
    _ctor_var_reset();
}

void VFetchFifo::__Vconfigure(VFetchFifo__Syms* vlSymsp, bool first) {
    if (0 && first) {}  // Prevent unused
    this->__VlSymsp = vlSymsp;
}

VFetchFifo::~VFetchFifo() {
    delete __VlSymsp; __VlSymsp=NULL;
}

void VFetchFifo::_settle__TOP__3(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_settle__TOP__3\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->io_pcOut = (vlTOPp->FetchFifo__DOT__pc 
                        << 1U);
    vlTOPp->io_dataIn_ready = (1U & (~ ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                        >> 1U)));
    vlTOPp->FetchFifo__DOT__lowest_free_entry = ((~ (IData)(vlTOPp->FetchFifo__DOT__valid_q)) 
                                                 & (1U 
                                                    | (6U 
                                                       & ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                                          << 1U))));
    vlTOPp->FetchFifo__DOT__valid = (1U & ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                           | (IData)(vlTOPp->io_dataIn_valid)));
    vlTOPp->FetchFifo__DOT__rdata = ((1U & (IData)(vlTOPp->FetchFifo__DOT__valid_q))
                                      ? vlTOPp->FetchFifo__DOT__rdata_q_0
                                      : vlTOPp->io_dataIn_payload);
    vlTOPp->FetchFifo__DOT__valid_pushed = (((IData)(vlTOPp->io_dataIn_valid)
                                              ? (IData)(vlTOPp->FetchFifo__DOT__lowest_free_entry)
                                              : 0U) 
                                            | (IData)(vlTOPp->FetchFifo__DOT__valid_q));
    vlTOPp->FetchFifo__DOT___zz_2_ = (0x7fffffffU & 
                                      (vlTOPp->FetchFifo__DOT__pc 
                                       + ((2U & vlTOPp->io_pcOut)
                                           ? ((3U != 
                                               (3U 
                                                & (vlTOPp->FetchFifo__DOT__rdata 
                                                   >> 0x10U)))
                                               ? 2U
                                               : 4U)
                                           : ((3U != 
                                               (3U 
                                                & vlTOPp->FetchFifo__DOT__rdata))
                                               ? 2U
                                               : 4U))));
    vlTOPp->io_dataOut_payload = ((2U & vlTOPp->io_pcOut)
                                   ? ((2U & (IData)(vlTOPp->FetchFifo__DOT__valid_q))
                                       ? ((0xffff0000U 
                                           & (vlTOPp->FetchFifo__DOT__rdata_q_1 
                                              << 0x10U)) 
                                          | (0xffffU 
                                             & (vlTOPp->FetchFifo__DOT__rdata 
                                                >> 0x10U)))
                                       : ((0xffff0000U 
                                           & (vlTOPp->io_dataIn_payload 
                                              << 0x10U)) 
                                          | (0xffffU 
                                             & (vlTOPp->FetchFifo__DOT__rdata 
                                                >> 0x10U))))
                                   : vlTOPp->FetchFifo__DOT__rdata);
    vlTOPp->io_dataOut_valid = (1U & ((2U & vlTOPp->io_pcOut)
                                       ? ((3U != (3U 
                                                  & (vlTOPp->FetchFifo__DOT__rdata 
                                                     >> 0x10U)))
                                           ? (IData)(vlTOPp->FetchFifo__DOT__valid)
                                           : (((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                               >> 1U) 
                                              | ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                                 & (IData)(vlTOPp->io_dataIn_valid))))
                                       : (IData)(vlTOPp->FetchFifo__DOT__valid)));
    vlTOPp->FetchFifo__DOT__pop_fifo = (((IData)(vlTOPp->io_dataOut_ready) 
                                         & (IData)(vlTOPp->io_dataOut_valid)) 
                                        & ((3U == (3U 
                                                   & vlTOPp->FetchFifo__DOT__rdata)) 
                                           | (vlTOPp->io_pcOut 
                                              >> 1U)));
    vlTOPp->FetchFifo__DOT__entry_en = ((IData)(vlTOPp->FetchFifo__DOT__pop_fifo)
                                         ? (3U & ((IData)(vlTOPp->FetchFifo__DOT__valid_pushed) 
                                                  >> 1U))
                                         : ((IData)(vlTOPp->io_dataIn_valid)
                                             ? (IData)(vlTOPp->FetchFifo__DOT__lowest_free_entry)
                                             : 0U));
}

void VFetchFifo::_eval_initial(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_eval_initial\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->__Vclklast__TOP__clk = vlTOPp->clk;
    vlTOPp->__Vclklast__TOP__reset = vlTOPp->reset;
}

void VFetchFifo::final() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::final\n"); );
    // Variables
    VFetchFifo__Syms* __restrict vlSymsp = this->__VlSymsp;
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VFetchFifo::_eval_settle(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_eval_settle\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->_settle__TOP__3(vlSymsp);
    vlTOPp->__Vm_traceActivity = (1U | vlTOPp->__Vm_traceActivity);
}

void VFetchFifo::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_ctor_var_reset\n"); );
    // Body
    io_clear = VL_RAND_RESET_I(1);
    io_dataIn_valid = VL_RAND_RESET_I(1);
    io_dataIn_ready = VL_RAND_RESET_I(1);
    io_dataIn_payload = VL_RAND_RESET_I(32);
    io_dataOut_valid = VL_RAND_RESET_I(1);
    io_dataOut_ready = VL_RAND_RESET_I(1);
    io_dataOut_payload = VL_RAND_RESET_I(32);
    io_pcIn = VL_RAND_RESET_I(32);
    io_pcOut = VL_RAND_RESET_I(32);
    clk = VL_RAND_RESET_I(1);
    reset = VL_RAND_RESET_I(1);
    FetchFifo__DOT___zz_2_ = VL_RAND_RESET_I(31);
    FetchFifo__DOT__valid_q = VL_RAND_RESET_I(3);
    FetchFifo__DOT__rdata_q_0 = VL_RAND_RESET_I(32);
    FetchFifo__DOT__rdata_q_1 = VL_RAND_RESET_I(32);
    FetchFifo__DOT__rdata_q_2 = VL_RAND_RESET_I(32);
    FetchFifo__DOT__rdata = VL_RAND_RESET_I(32);
    FetchFifo__DOT__valid = VL_RAND_RESET_I(1);
    FetchFifo__DOT__pc = VL_RAND_RESET_I(31);
    FetchFifo__DOT__pop_fifo = VL_RAND_RESET_I(1);
    FetchFifo__DOT__lowest_free_entry = VL_RAND_RESET_I(3);
    FetchFifo__DOT__valid_pushed = VL_RAND_RESET_I(3);
    FetchFifo__DOT__entry_en = VL_RAND_RESET_I(3);
    __Vm_traceActivity = 0;
}
