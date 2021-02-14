// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VFetchFifo__Syms.h"


//======================

void VFetchFifo::traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VFetchFifo* t = (VFetchFifo*)userthis;
    VFetchFifo__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (vlSymsp->getClearActivity()) {
        t->traceChgThis(vlSymsp, vcdp, code);
    }
}

//======================


void VFetchFifo::traceChgThis(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        if (VL_UNLIKELY((1U & (vlTOPp->__Vm_traceActivity 
                               | (vlTOPp->__Vm_traceActivity 
                                  >> 2U))))) {
            vlTOPp->traceChgThis__2(vlSymsp, vcdp, code);
        }
        if (VL_UNLIKELY((1U & (vlTOPp->__Vm_traceActivity 
                               | (vlTOPp->__Vm_traceActivity 
                                  >> 3U))))) {
            vlTOPp->traceChgThis__3(vlSymsp, vcdp, code);
        }
        if (VL_UNLIKELY((2U & vlTOPp->__Vm_traceActivity))) {
            vlTOPp->traceChgThis__4(vlSymsp, vcdp, code);
        }
        if (VL_UNLIKELY((4U & vlTOPp->__Vm_traceActivity))) {
            vlTOPp->traceChgThis__5(vlSymsp, vcdp, code);
        }
        vlTOPp->traceChgThis__6(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VFetchFifo::traceChgThis__2(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+1,(vlTOPp->FetchFifo__DOT__lowest_free_entry),3);
    }
}

void VFetchFifo::traceChgThis__3(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+9,(vlTOPp->FetchFifo__DOT__rdata),32);
        vcdp->chgBit(c+17,(vlTOPp->FetchFifo__DOT__valid));
        vcdp->chgBit(c+25,((3U != (3U & (vlTOPp->FetchFifo__DOT__rdata 
                                         >> 0x10U)))));
        vcdp->chgBit(c+33,((3U != (3U & vlTOPp->FetchFifo__DOT__rdata))));
        vcdp->chgBit(c+41,(vlTOPp->FetchFifo__DOT__pop_fifo));
        vcdp->chgBus(c+49,(vlTOPp->FetchFifo__DOT__valid_pushed),3);
        vcdp->chgBus(c+57,(((IData)(vlTOPp->FetchFifo__DOT__pop_fifo)
                             ? (4U | (3U & ((IData)(vlTOPp->FetchFifo__DOT__valid_pushed) 
                                            >> 1U)))
                             : (IData)(vlTOPp->FetchFifo__DOT__valid_pushed))),3);
        vcdp->chgBus(c+65,(vlTOPp->FetchFifo__DOT__entry_en),3);
    }
}

void VFetchFifo::traceChgThis__4(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+73,(vlTOPp->FetchFifo__DOT__rdata_q_0),32);
        vcdp->chgBus(c+81,(vlTOPp->FetchFifo__DOT__rdata_q_1),32);
        vcdp->chgBus(c+89,(vlTOPp->FetchFifo__DOT__rdata_q_2),32);
        vcdp->chgBus(c+97,(vlTOPp->FetchFifo__DOT__pc),31);
    }
}

void VFetchFifo::traceChgThis__5(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+105,(vlTOPp->FetchFifo__DOT__valid_q),3);
    }
}

void VFetchFifo::traceChgThis__6(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBit(c+113,(vlTOPp->io_clear));
        vcdp->chgBit(c+121,(vlTOPp->io_dataIn_valid));
        vcdp->chgBit(c+129,(vlTOPp->io_dataIn_ready));
        vcdp->chgBus(c+137,(vlTOPp->io_dataIn_payload),32);
        vcdp->chgBit(c+145,(vlTOPp->io_dataOut_valid));
        vcdp->chgBit(c+153,(vlTOPp->io_dataOut_ready));
        vcdp->chgBus(c+161,(vlTOPp->io_dataOut_payload),32);
        vcdp->chgBus(c+169,(vlTOPp->io_pcIn),32);
        vcdp->chgBus(c+177,(vlTOPp->io_pcOut),32);
        vcdp->chgBit(c+185,(vlTOPp->clk));
        vcdp->chgBit(c+193,(vlTOPp->reset));
        vcdp->chgBus(c+201,(((2U & (IData)(vlTOPp->FetchFifo__DOT__valid_q))
                              ? ((0xffff0000U & (vlTOPp->FetchFifo__DOT__rdata_q_1 
                                                 << 0x10U)) 
                                 | (0xffffU & (vlTOPp->FetchFifo__DOT__rdata 
                                               >> 0x10U)))
                              : ((0xffff0000U & (vlTOPp->io_dataIn_payload 
                                                 << 0x10U)) 
                                 | (0xffffU & (vlTOPp->FetchFifo__DOT__rdata 
                                               >> 0x10U))))),32);
        vcdp->chgBit(c+209,((1U & (((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                    >> 1U) | ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                              & (IData)(vlTOPp->io_dataIn_valid))))));
        vcdp->chgBit(c+217,(((IData)(vlTOPp->io_clear) 
                             | ((IData)(vlTOPp->io_dataOut_ready) 
                                & (IData)(vlTOPp->io_dataOut_valid)))));
        vcdp->chgBus(c+225,(((2U & vlTOPp->io_pcOut)
                              ? ((3U != (3U & (vlTOPp->FetchFifo__DOT__rdata 
                                               >> 0x10U)))
                                  ? 2U : 4U) : ((3U 
                                                 != 
                                                 (3U 
                                                  & vlTOPp->FetchFifo__DOT__rdata))
                                                 ? 2U
                                                 : 4U))),3);
        vcdp->chgBus(c+233,(((IData)(vlTOPp->io_clear)
                              ? 0U : ((IData)(vlTOPp->FetchFifo__DOT__pop_fifo)
                                       ? (4U | (3U 
                                                & ((IData)(vlTOPp->FetchFifo__DOT__valid_pushed) 
                                                   >> 1U)))
                                       : (IData)(vlTOPp->FetchFifo__DOT__valid_pushed)))),3);
    }
}
