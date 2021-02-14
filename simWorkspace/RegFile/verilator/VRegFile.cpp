// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VRegFile.h for the primary calling header

#include "VRegFile.h"
#include "VRegFile__Syms.h"

//==========

void VRegFile::eval_step() {
    VL_DEBUG_IF(VL_DBG_MSGF("+++++TOP Evaluate VRegFile::eval\n"); );
    VRegFile__Syms* __restrict vlSymsp = this->__VlSymsp;  // Setup global symbol table
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
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
            VL_FATAL_MT("D:/Work/Code/ChiselPrj/tmp/job_1/RegFile.v", 7, "",
                "Verilated model didn't converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

void VRegFile::_eval_initial_loop(VRegFile__Syms* __restrict vlSymsp) {
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
            VL_FATAL_MT("D:/Work/Code/ChiselPrj/tmp/job_1/RegFile.v", 7, "",
                "Verilated model didn't DC converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

VL_INLINE_OPT void VRegFile::_sequent__TOP__1(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_sequent__TOP__1\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Variables
    CData/*4:0*/ __Vdlyvdim0__RegFile__DOT__mem__v0;
    CData/*0:0*/ __Vdlyvset__RegFile__DOT__mem__v0;
    QData/*63:0*/ __Vdlyvval__RegFile__DOT__mem__v0;
    // Body
    __Vdlyvset__RegFile__DOT__mem__v0 = 0U;
    if (vlTOPp->io_regio_wen) {
        __Vdlyvval__RegFile__DOT__mem__v0 = vlTOPp->io_regio_wdata;
        __Vdlyvset__RegFile__DOT__mem__v0 = 1U;
        __Vdlyvdim0__RegFile__DOT__mem__v0 = vlTOPp->io_regio_waddr;
    }
    if (__Vdlyvset__RegFile__DOT__mem__v0) {
        vlTOPp->RegFile__DOT__mem[__Vdlyvdim0__RegFile__DOT__mem__v0] 
            = __Vdlyvval__RegFile__DOT__mem__v0;
    }
}

VL_INLINE_OPT void VRegFile::_settle__TOP__2(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_settle__TOP__2\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->io_regio_rdata1 = ((0U != (IData)(vlTOPp->io_regio_raddr1))
                                ? vlTOPp->RegFile__DOT__mem
                               [vlTOPp->io_regio_raddr1]
                                : VL_ULL(0));
    vlTOPp->io_regio_rdata2 = ((0U != (IData)(vlTOPp->io_regio_raddr2))
                                ? vlTOPp->RegFile__DOT__mem
                               [vlTOPp->io_regio_raddr2]
                                : VL_ULL(0));
}

void VRegFile::_eval(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_eval\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    if (((IData)(vlTOPp->clk) & (~ (IData)(vlTOPp->__Vclklast__TOP__clk)))) {
        vlTOPp->_sequent__TOP__1(vlSymsp);
        vlTOPp->__Vm_traceActivity = (2U | vlTOPp->__Vm_traceActivity);
    }
    vlTOPp->_settle__TOP__2(vlSymsp);
    // Final
    vlTOPp->__Vclklast__TOP__clk = vlTOPp->clk;
}

VL_INLINE_OPT QData VRegFile::_change_request(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_change_request\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    return (vlTOPp->_change_request_1(vlSymsp));
}

VL_INLINE_OPT QData VRegFile::_change_request_1(VRegFile__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_change_request_1\n"); );
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    // Change detection
    QData __req = false;  // Logically a bool
    return __req;
}

#ifdef VL_DEBUG
void VRegFile::_eval_debug_assertions() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VRegFile::_eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((io_regio_raddr1 & 0xe0U))) {
        Verilated::overWidthError("io_regio_raddr1");}
    if (VL_UNLIKELY((io_regio_raddr2 & 0xe0U))) {
        Verilated::overWidthError("io_regio_raddr2");}
    if (VL_UNLIKELY((io_regio_waddr & 0xe0U))) {
        Verilated::overWidthError("io_regio_waddr");}
    if (VL_UNLIKELY((io_regio_wen & 0xfeU))) {
        Verilated::overWidthError("io_regio_wen");}
    if (VL_UNLIKELY((clk & 0xfeU))) {
        Verilated::overWidthError("clk");}
    if (VL_UNLIKELY((reset & 0xfeU))) {
        Verilated::overWidthError("reset");}
}
#endif  // VL_DEBUG
