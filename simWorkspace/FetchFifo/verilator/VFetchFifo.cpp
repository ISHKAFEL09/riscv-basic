// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VFetchFifo.h for the primary calling header

#include "VFetchFifo.h"
#include "VFetchFifo__Syms.h"

//==========

void VFetchFifo::eval_step() {
    VL_DEBUG_IF(VL_DBG_MSGF("+++++TOP Evaluate VFetchFifo::eval\n"); );
    VFetchFifo__Syms* __restrict vlSymsp = this->__VlSymsp;  // Setup global symbol table
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
#ifdef VL_DEBUG
    // Debug assertions
    _eval_debug_assertions();
#endif  // VL_DEBUG
    // Initialize
    if (VL_UNLIKELY(!vlSymsp->__Vm_didInit)) _eval_initial_loop(vlSymsp);
#ifdef VM_TRACE
    // Tracing
#endif  // VM_TRACE
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
        VL_DEBUG_IF(VL_DBG_MSGF("+ Clock loop\n"););
        vlSymsp->__Vm_activity = true;
        _eval(vlSymsp);
        if (VL_UNLIKELY(++__VclockLoop > 100)) {
            // About to fail, so enable debug to see what's not settling.
            // Note you must run make with OPT=-DVL_DEBUG for debug prints.
            int __Vsaved_debug = Verilated::debug();
            Verilated::debug(1);
            __Vchange = _change_request(vlSymsp);
            Verilated::debug(__Vsaved_debug);
            VL_FATAL_MT("D:/Work/Code/ChiselPrj/tmp/job_1/FetchFifo.v", 7, "",
                "Verilated model didn't converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

void VFetchFifo::_eval_initial_loop(VFetchFifo__Syms* __restrict vlSymsp) {
    vlSymsp->__Vm_didInit = true;
    _eval_initial(vlSymsp);
    vlSymsp->__Vm_activity = true;
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
        _eval_settle(vlSymsp);
        _eval(vlSymsp);
        if (VL_UNLIKELY(++__VclockLoop > 100)) {
            // About to fail, so enable debug to see what's not settling.
            // Note you must run make with OPT=-DVL_DEBUG for debug prints.
            int __Vsaved_debug = Verilated::debug();
            Verilated::debug(1);
            __Vchange = _change_request(vlSymsp);
            Verilated::debug(__Vsaved_debug);
            VL_FATAL_MT("D:/Work/Code/ChiselPrj/tmp/job_1/FetchFifo.v", 7, "",
                "Verilated model didn't DC converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

VL_INLINE_OPT void VFetchFifo::_sequent__TOP__1(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_sequent__TOP__1\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    if (vlTOPp->reset) {
        vlTOPp->FetchFifo__DOT__pc = 0U;
    } else {
        if (((IData)(vlTOPp->io_clear) | ((IData)(vlTOPp->io_dataOut_ready) 
                                          & (IData)(vlTOPp->io_dataOut_valid)))) {
            vlTOPp->FetchFifo__DOT__pc = (0x7fffffffU 
                                          & ((IData)(vlTOPp->io_clear)
                                              ? (vlTOPp->io_pcIn 
                                                 >> 1U)
                                              : vlTOPp->FetchFifo__DOT___zz_2_));
        }
    }
    if (vlTOPp->reset) {
        vlTOPp->FetchFifo__DOT__rdata_q_0 = 0U;
    } else {
        if ((1U & (IData)(vlTOPp->FetchFifo__DOT__entry_en))) {
            vlTOPp->FetchFifo__DOT__rdata_q_0 = ((2U 
                                                  & (IData)(vlTOPp->FetchFifo__DOT__valid_q))
                                                  ? vlTOPp->FetchFifo__DOT__rdata_q_1
                                                  : vlTOPp->io_dataIn_payload);
        }
    }
    vlTOPp->io_pcOut = (vlTOPp->FetchFifo__DOT__pc 
                        << 1U);
    if (vlTOPp->reset) {
        vlTOPp->FetchFifo__DOT__rdata_q_1 = 0U;
    } else {
        if ((2U & (IData)(vlTOPp->FetchFifo__DOT__entry_en))) {
            vlTOPp->FetchFifo__DOT__rdata_q_1 = ((4U 
                                                  & (IData)(vlTOPp->FetchFifo__DOT__valid_q))
                                                  ? vlTOPp->FetchFifo__DOT__rdata_q_2
                                                  : vlTOPp->io_dataIn_payload);
        }
    }
    if (vlTOPp->reset) {
        vlTOPp->FetchFifo__DOT__rdata_q_2 = 0U;
    } else {
        if ((4U & (IData)(vlTOPp->FetchFifo__DOT__entry_en))) {
            vlTOPp->FetchFifo__DOT__rdata_q_2 = vlTOPp->io_dataIn_payload;
        }
    }
}

VL_INLINE_OPT void VFetchFifo::_sequent__TOP__2(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_sequent__TOP__2\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->FetchFifo__DOT__valid_q = ((IData)(vlTOPp->io_clear)
                                        ? 0U : ((IData)(vlTOPp->FetchFifo__DOT__pop_fifo)
                                                 ? 
                                                (4U 
                                                 | (3U 
                                                    & ((IData)(vlTOPp->FetchFifo__DOT__valid_pushed) 
                                                       >> 1U)))
                                                 : (IData)(vlTOPp->FetchFifo__DOT__valid_pushed)));
    vlTOPp->io_dataIn_ready = (1U & (~ ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                        >> 1U)));
    vlTOPp->FetchFifo__DOT__lowest_free_entry = ((~ (IData)(vlTOPp->FetchFifo__DOT__valid_q)) 
                                                 & (1U 
                                                    | (6U 
                                                       & ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                                          << 1U))));
}

VL_INLINE_OPT void VFetchFifo::_combo__TOP__4(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_combo__TOP__4\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
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

void VFetchFifo::_eval(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_eval\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    if ((((IData)(vlTOPp->clk) & (~ (IData)(vlTOPp->__Vclklast__TOP__clk))) 
         | ((IData)(vlTOPp->reset) & (~ (IData)(vlTOPp->__Vclklast__TOP__reset))))) {
        vlTOPp->_sequent__TOP__1(vlSymsp);
        vlTOPp->__Vm_traceActivity = (2U | vlTOPp->__Vm_traceActivity);
    }
    if (((IData)(vlTOPp->clk) & (~ (IData)(vlTOPp->__Vclklast__TOP__clk)))) {
        vlTOPp->_sequent__TOP__2(vlSymsp);
        vlTOPp->__Vm_traceActivity = (4U | vlTOPp->__Vm_traceActivity);
    }
    vlTOPp->_combo__TOP__4(vlSymsp);
    vlTOPp->__Vm_traceActivity = (8U | vlTOPp->__Vm_traceActivity);
    // Final
    vlTOPp->__Vclklast__TOP__clk = vlTOPp->clk;
    vlTOPp->__Vclklast__TOP__reset = vlTOPp->reset;
}

VL_INLINE_OPT QData VFetchFifo::_change_request(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_change_request\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    return (vlTOPp->_change_request_1(vlSymsp));
}

VL_INLINE_OPT QData VFetchFifo::_change_request_1(VFetchFifo__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_change_request_1\n"); );
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    // Change detection
    QData __req = false;  // Logically a bool
    return __req;
}

#ifdef VL_DEBUG
void VFetchFifo::_eval_debug_assertions() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFetchFifo::_eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((io_clear & 0xfeU))) {
        Verilated::overWidthError("io_clear");}
    if (VL_UNLIKELY((io_dataIn_valid & 0xfeU))) {
        Verilated::overWidthError("io_dataIn_valid");}
    if (VL_UNLIKELY((io_dataOut_ready & 0xfeU))) {
        Verilated::overWidthError("io_dataOut_ready");}
    if (VL_UNLIKELY((clk & 0xfeU))) {
        Verilated::overWidthError("clk");}
    if (VL_UNLIKELY((reset & 0xfeU))) {
        Verilated::overWidthError("reset");}
}
#endif  // VL_DEBUG
