// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VALU__Syms.h"


//======================

void VALU::trace(VerilatedVcdC* tfp, int, int) {
    tfp->spTrace()->addCallback(&VALU::traceInit, &VALU::traceFull, &VALU::traceChg, this);
}
void VALU::traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->open()
    VALU* t = (VALU*)userthis;
    VALU__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (!Verilated::calcUnusedSigs()) {
        VL_FATAL_MT(__FILE__, __LINE__, __FILE__,
                        "Turning on wave traces requires Verilated::traceEverOn(true) call before time 0.");
    }
    vcdp->scopeEscape(' ');
    t->traceInitThis(vlSymsp, vcdp, code);
    vcdp->scopeEscape('.');
}
void VALU::traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VALU* t = (VALU*)userthis;
    VALU__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    t->traceFullThis(vlSymsp, vcdp, code);
}

//======================


void VALU::traceInitThis(VALU__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    vcdp->module(vlSymsp->name());  // Setup signal names
    // Body
    {
        vlTOPp->traceInitThis__1(vlSymsp, vcdp, code);
    }
}

void VALU::traceFullThis(VALU__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vlTOPp->traceFullThis__1(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VALU::traceInitThis__1(VALU__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->declBus(c+1,"io_opcode", false,-1, 3,0);
        vcdp->declBus(c+9,"io_op1", false,-1, 31,0);
        vcdp->declBus(c+17,"io_op2", false,-1, 31,0);
        vcdp->declBus(c+25,"io_aluOut", false,-1, 31,0);
        vcdp->declBus(c+1,"ALU io_opcode", false,-1, 3,0);
        vcdp->declBus(c+9,"ALU io_op1", false,-1, 31,0);
        vcdp->declBus(c+17,"ALU io_op2", false,-1, 31,0);
        vcdp->declBus(c+25,"ALU io_aluOut", false,-1, 31,0);
    }
}

void VALU::traceFullThis__1(VALU__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VALU* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->fullBus(c+1,(vlTOPp->io_opcode),4);
        vcdp->fullBus(c+9,(vlTOPp->io_op1),32);
        vcdp->fullBus(c+17,(vlTOPp->io_op2),32);
        vcdp->fullBus(c+25,(vlTOPp->io_aluOut),32);
    }
}
