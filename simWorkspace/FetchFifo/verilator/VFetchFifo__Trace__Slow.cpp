// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VFetchFifo__Syms.h"


//======================

void VFetchFifo::trace(VerilatedVcdC* tfp, int, int) {
    tfp->spTrace()->addCallback(&VFetchFifo::traceInit, &VFetchFifo::traceFull, &VFetchFifo::traceChg, this);
}
void VFetchFifo::traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->open()
    VFetchFifo* t = (VFetchFifo*)userthis;
    VFetchFifo__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (!Verilated::calcUnusedSigs()) {
        VL_FATAL_MT(__FILE__, __LINE__, __FILE__,
                        "Turning on wave traces requires Verilated::traceEverOn(true) call before time 0.");
    }
    vcdp->scopeEscape(' ');
    t->traceInitThis(vlSymsp, vcdp, code);
    vcdp->scopeEscape('.');
}
void VFetchFifo::traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VFetchFifo* t = (VFetchFifo*)userthis;
    VFetchFifo__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    t->traceFullThis(vlSymsp, vcdp, code);
}

//======================


void VFetchFifo::traceInitThis(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    vcdp->module(vlSymsp->name());  // Setup signal names
    // Body
    {
        vlTOPp->traceInitThis__1(vlSymsp, vcdp, code);
    }
}

void VFetchFifo::traceFullThis(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vlTOPp->traceFullThis__1(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VFetchFifo::traceInitThis__1(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->declBit(c+113,"io_clear", false,-1);
        vcdp->declBit(c+121,"io_dataIn_valid", false,-1);
        vcdp->declBit(c+129,"io_dataIn_ready", false,-1);
        vcdp->declBus(c+137,"io_dataIn_payload", false,-1, 31,0);
        vcdp->declBit(c+145,"io_dataOut_valid", false,-1);
        vcdp->declBit(c+153,"io_dataOut_ready", false,-1);
        vcdp->declBus(c+161,"io_dataOut_payload", false,-1, 31,0);
        vcdp->declBus(c+169,"io_pcIn", false,-1, 31,0);
        vcdp->declBus(c+177,"io_pcOut", false,-1, 31,0);
        vcdp->declBit(c+185,"clk", false,-1);
        vcdp->declBit(c+193,"reset", false,-1);
        vcdp->declBit(c+113,"FetchFifo io_clear", false,-1);
        vcdp->declBit(c+121,"FetchFifo io_dataIn_valid", false,-1);
        vcdp->declBit(c+129,"FetchFifo io_dataIn_ready", false,-1);
        vcdp->declBus(c+137,"FetchFifo io_dataIn_payload", false,-1, 31,0);
        vcdp->declBit(c+145,"FetchFifo io_dataOut_valid", false,-1);
        vcdp->declBit(c+153,"FetchFifo io_dataOut_ready", false,-1);
        vcdp->declBus(c+161,"FetchFifo io_dataOut_payload", false,-1, 31,0);
        vcdp->declBus(c+169,"FetchFifo io_pcIn", false,-1, 31,0);
        vcdp->declBus(c+177,"FetchFifo io_pcOut", false,-1, 31,0);
        vcdp->declBit(c+185,"FetchFifo clk", false,-1);
        vcdp->declBit(c+193,"FetchFifo reset", false,-1);
        vcdp->declBus(c+105,"FetchFifo valid_q", false,-1, 2,0);
        vcdp->declBus(c+73,"FetchFifo rdata_q_0", false,-1, 31,0);
        vcdp->declBus(c+81,"FetchFifo rdata_q_1", false,-1, 31,0);
        vcdp->declBus(c+89,"FetchFifo rdata_q_2", false,-1, 31,0);
        vcdp->declBus(c+9,"FetchFifo rdata", false,-1, 31,0);
        vcdp->declBit(c+17,"FetchFifo valid", false,-1);
        vcdp->declBus(c+201,"FetchFifo rdata_unaligned", false,-1, 31,0);
        vcdp->declBit(c+209,"FetchFifo valid_unaligned", false,-1);
        vcdp->declBit(c+25,"FetchFifo unaligned_is_compressed", false,-1);
        vcdp->declBit(c+33,"FetchFifo aligned_is_compressed", false,-1);
        vcdp->declBus(c+97,"FetchFifo pc", false,-1, 30,0);
        vcdp->declBit(c+217,"FetchFifo pc_en", false,-1);
        vcdp->declBus(c+225,"FetchFifo pc_incr", false,-1, 2,0);
        vcdp->declBit(c+41,"FetchFifo pop_fifo", false,-1);
        vcdp->declBus(c+1,"FetchFifo lowest_free_entry", false,-1, 2,0);
        vcdp->declBus(c+49,"FetchFifo valid_pushed", false,-1, 2,0);
        vcdp->declBus(c+57,"FetchFifo valid_popped", false,-1, 2,0);
        vcdp->declBus(c+233,"FetchFifo valid_d", false,-1, 2,0);
        vcdp->declBus(c+65,"FetchFifo entry_en", false,-1, 2,0);
    }
}

void VFetchFifo::traceFullThis__1(VFetchFifo__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFetchFifo* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->fullBus(c+1,(vlTOPp->FetchFifo__DOT__lowest_free_entry),3);
        vcdp->fullBus(c+9,(vlTOPp->FetchFifo__DOT__rdata),32);
        vcdp->fullBit(c+17,(vlTOPp->FetchFifo__DOT__valid));
        vcdp->fullBit(c+25,((3U != (3U & (vlTOPp->FetchFifo__DOT__rdata 
                                          >> 0x10U)))));
        vcdp->fullBit(c+33,((3U != (3U & vlTOPp->FetchFifo__DOT__rdata))));
        vcdp->fullBit(c+41,(vlTOPp->FetchFifo__DOT__pop_fifo));
        vcdp->fullBus(c+49,(vlTOPp->FetchFifo__DOT__valid_pushed),3);
        vcdp->fullBus(c+57,(((IData)(vlTOPp->FetchFifo__DOT__pop_fifo)
                              ? (4U | (3U & ((IData)(vlTOPp->FetchFifo__DOT__valid_pushed) 
                                             >> 1U)))
                              : (IData)(vlTOPp->FetchFifo__DOT__valid_pushed))),3);
        vcdp->fullBus(c+65,(vlTOPp->FetchFifo__DOT__entry_en),3);
        vcdp->fullBus(c+73,(vlTOPp->FetchFifo__DOT__rdata_q_0),32);
        vcdp->fullBus(c+81,(vlTOPp->FetchFifo__DOT__rdata_q_1),32);
        vcdp->fullBus(c+89,(vlTOPp->FetchFifo__DOT__rdata_q_2),32);
        vcdp->fullBus(c+97,(vlTOPp->FetchFifo__DOT__pc),31);
        vcdp->fullBus(c+105,(vlTOPp->FetchFifo__DOT__valid_q),3);
        vcdp->fullBit(c+113,(vlTOPp->io_clear));
        vcdp->fullBit(c+121,(vlTOPp->io_dataIn_valid));
        vcdp->fullBit(c+129,(vlTOPp->io_dataIn_ready));
        vcdp->fullBus(c+137,(vlTOPp->io_dataIn_payload),32);
        vcdp->fullBit(c+145,(vlTOPp->io_dataOut_valid));
        vcdp->fullBit(c+153,(vlTOPp->io_dataOut_ready));
        vcdp->fullBus(c+161,(vlTOPp->io_dataOut_payload),32);
        vcdp->fullBus(c+169,(vlTOPp->io_pcIn),32);
        vcdp->fullBus(c+177,(vlTOPp->io_pcOut),32);
        vcdp->fullBit(c+185,(vlTOPp->clk));
        vcdp->fullBit(c+193,(vlTOPp->reset));
        vcdp->fullBus(c+201,(((2U & (IData)(vlTOPp->FetchFifo__DOT__valid_q))
                               ? ((0xffff0000U & (vlTOPp->FetchFifo__DOT__rdata_q_1 
                                                  << 0x10U)) 
                                  | (0xffffU & (vlTOPp->FetchFifo__DOT__rdata 
                                                >> 0x10U)))
                               : ((0xffff0000U & (vlTOPp->io_dataIn_payload 
                                                  << 0x10U)) 
                                  | (0xffffU & (vlTOPp->FetchFifo__DOT__rdata 
                                                >> 0x10U))))),32);
        vcdp->fullBit(c+209,((1U & (((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                     >> 1U) | ((IData)(vlTOPp->FetchFifo__DOT__valid_q) 
                                               & (IData)(vlTOPp->io_dataIn_valid))))));
        vcdp->fullBit(c+217,(((IData)(vlTOPp->io_clear) 
                              | ((IData)(vlTOPp->io_dataOut_ready) 
                                 & (IData)(vlTOPp->io_dataOut_valid)))));
        vcdp->fullBus(c+225,(((2U & vlTOPp->io_pcOut)
                               ? ((3U != (3U & (vlTOPp->FetchFifo__DOT__rdata 
                                                >> 0x10U)))
                                   ? 2U : 4U) : ((3U 
                                                  != 
                                                  (3U 
                                                   & vlTOPp->FetchFifo__DOT__rdata))
                                                  ? 2U
                                                  : 4U))),3);
        vcdp->fullBus(c+233,(((IData)(vlTOPp->io_clear)
                               ? 0U : ((IData)(vlTOPp->FetchFifo__DOT__pop_fifo)
                                        ? (4U | (3U 
                                                 & ((IData)(vlTOPp->FetchFifo__DOT__valid_pushed) 
                                                    >> 1U)))
                                        : (IData)(vlTOPp->FetchFifo__DOT__valid_pushed)))),3);
    }
}
