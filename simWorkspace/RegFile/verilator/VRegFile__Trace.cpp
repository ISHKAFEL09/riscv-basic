// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VRegFile__Syms.h"


//======================

void VRegFile::traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VRegFile* t = (VRegFile*)userthis;
    VRegFile__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (vlSymsp->getClearActivity()) {
        t->traceChgThis(vlSymsp, vcdp, code);
    }
}

//======================


void VRegFile::traceChgThis(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        if (VL_UNLIKELY((2U & vlTOPp->__Vm_traceActivity))) {
            vlTOPp->traceChgThis__2(vlSymsp, vcdp, code);
        }
        vlTOPp->traceChgThis__3(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VRegFile::traceChgThis__2(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgQuad(c+1,(vlTOPp->RegFile__DOT__mem[0]),64);
        vcdp->chgQuad(c+3,(vlTOPp->RegFile__DOT__mem[1]),64);
        vcdp->chgQuad(c+5,(vlTOPp->RegFile__DOT__mem[2]),64);
        vcdp->chgQuad(c+7,(vlTOPp->RegFile__DOT__mem[3]),64);
        vcdp->chgQuad(c+9,(vlTOPp->RegFile__DOT__mem[4]),64);
        vcdp->chgQuad(c+11,(vlTOPp->RegFile__DOT__mem[5]),64);
        vcdp->chgQuad(c+13,(vlTOPp->RegFile__DOT__mem[6]),64);
        vcdp->chgQuad(c+15,(vlTOPp->RegFile__DOT__mem[7]),64);
        vcdp->chgQuad(c+17,(vlTOPp->RegFile__DOT__mem[8]),64);
        vcdp->chgQuad(c+19,(vlTOPp->RegFile__DOT__mem[9]),64);
        vcdp->chgQuad(c+21,(vlTOPp->RegFile__DOT__mem[10]),64);
        vcdp->chgQuad(c+23,(vlTOPp->RegFile__DOT__mem[11]),64);
        vcdp->chgQuad(c+25,(vlTOPp->RegFile__DOT__mem[12]),64);
        vcdp->chgQuad(c+27,(vlTOPp->RegFile__DOT__mem[13]),64);
        vcdp->chgQuad(c+29,(vlTOPp->RegFile__DOT__mem[14]),64);
        vcdp->chgQuad(c+31,(vlTOPp->RegFile__DOT__mem[15]),64);
        vcdp->chgQuad(c+33,(vlTOPp->RegFile__DOT__mem[16]),64);
        vcdp->chgQuad(c+35,(vlTOPp->RegFile__DOT__mem[17]),64);
        vcdp->chgQuad(c+37,(vlTOPp->RegFile__DOT__mem[18]),64);
        vcdp->chgQuad(c+39,(vlTOPp->RegFile__DOT__mem[19]),64);
        vcdp->chgQuad(c+41,(vlTOPp->RegFile__DOT__mem[20]),64);
        vcdp->chgQuad(c+43,(vlTOPp->RegFile__DOT__mem[21]),64);
        vcdp->chgQuad(c+45,(vlTOPp->RegFile__DOT__mem[22]),64);
        vcdp->chgQuad(c+47,(vlTOPp->RegFile__DOT__mem[23]),64);
        vcdp->chgQuad(c+49,(vlTOPp->RegFile__DOT__mem[24]),64);
        vcdp->chgQuad(c+51,(vlTOPp->RegFile__DOT__mem[25]),64);
        vcdp->chgQuad(c+53,(vlTOPp->RegFile__DOT__mem[26]),64);
        vcdp->chgQuad(c+55,(vlTOPp->RegFile__DOT__mem[27]),64);
        vcdp->chgQuad(c+57,(vlTOPp->RegFile__DOT__mem[28]),64);
        vcdp->chgQuad(c+59,(vlTOPp->RegFile__DOT__mem[29]),64);
        vcdp->chgQuad(c+61,(vlTOPp->RegFile__DOT__mem[30]),64);
        vcdp->chgQuad(c+63,(vlTOPp->RegFile__DOT__mem[31]),64);
    }
}

void VRegFile::traceChgThis__3(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+513,(vlTOPp->io_regio_raddr1),5);
        vcdp->chgBus(c+521,(vlTOPp->io_regio_raddr2),5);
        vcdp->chgBus(c+529,(vlTOPp->io_regio_waddr),5);
        vcdp->chgQuad(c+537,(vlTOPp->io_regio_wdata),64);
        vcdp->chgBit(c+553,(vlTOPp->io_regio_wen));
        vcdp->chgQuad(c+561,(vlTOPp->io_regio_rdata1),64);
        vcdp->chgQuad(c+577,(vlTOPp->io_regio_rdata2),64);
        vcdp->chgBit(c+593,(vlTOPp->clk));
        vcdp->chgBit(c+601,(vlTOPp->reset));
    }
}
