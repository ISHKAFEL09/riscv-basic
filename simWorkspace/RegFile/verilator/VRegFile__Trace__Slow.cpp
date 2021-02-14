// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VRegFile__Syms.h"


//======================

void VRegFile::trace(VerilatedVcdC* tfp, int, int) {
    tfp->spTrace()->addCallback(&VRegFile::traceInit, &VRegFile::traceFull, &VRegFile::traceChg, this);
}
void VRegFile::traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->open()
    VRegFile* t = (VRegFile*)userthis;
    VRegFile__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (!Verilated::calcUnusedSigs()) {
        VL_FATAL_MT(__FILE__, __LINE__, __FILE__,
                        "Turning on wave traces requires Verilated::traceEverOn(true) call before time 0.");
    }
    vcdp->scopeEscape(' ');
    t->traceInitThis(vlSymsp, vcdp, code);
    vcdp->scopeEscape('.');
}
void VRegFile::traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VRegFile* t = (VRegFile*)userthis;
    VRegFile__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    t->traceFullThis(vlSymsp, vcdp, code);
}

//======================


void VRegFile::traceInitThis(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    vcdp->module(vlSymsp->name());  // Setup signal names
    // Body
    {
        vlTOPp->traceInitThis__1(vlSymsp, vcdp, code);
    }
}

void VRegFile::traceFullThis(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vlTOPp->traceFullThis__1(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VRegFile::traceInitThis__1(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->declBus(c+513,"io_regio_raddr1", false,-1, 4,0);
        vcdp->declBus(c+521,"io_regio_raddr2", false,-1, 4,0);
        vcdp->declBus(c+529,"io_regio_waddr", false,-1, 4,0);
        vcdp->declQuad(c+537,"io_regio_wdata", false,-1, 63,0);
        vcdp->declBit(c+553,"io_regio_wen", false,-1);
        vcdp->declQuad(c+561,"io_regio_rdata1", false,-1, 63,0);
        vcdp->declQuad(c+577,"io_regio_rdata2", false,-1, 63,0);
        vcdp->declBit(c+593,"clk", false,-1);
        vcdp->declBit(c+601,"reset", false,-1);
        vcdp->declBus(c+513,"RegFile io_regio_raddr1", false,-1, 4,0);
        vcdp->declBus(c+521,"RegFile io_regio_raddr2", false,-1, 4,0);
        vcdp->declBus(c+529,"RegFile io_regio_waddr", false,-1, 4,0);
        vcdp->declQuad(c+537,"RegFile io_regio_wdata", false,-1, 63,0);
        vcdp->declBit(c+553,"RegFile io_regio_wen", false,-1);
        vcdp->declQuad(c+561,"RegFile io_regio_rdata1", false,-1, 63,0);
        vcdp->declQuad(c+577,"RegFile io_regio_rdata2", false,-1, 63,0);
        vcdp->declBit(c+593,"RegFile clk", false,-1);
        vcdp->declBit(c+601,"RegFile reset", false,-1);
        {int i; for (i=0; i<32; i++) {
                vcdp->declQuad(c+1+i*2,"RegFile mem", true,(i+0), 63,0);}}
    }
}

void VRegFile::traceFullThis__1(VRegFile__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VRegFile* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->fullQuad(c+1,(vlTOPp->RegFile__DOT__mem[0]),64);
        vcdp->fullQuad(c+3,(vlTOPp->RegFile__DOT__mem[1]),64);
        vcdp->fullQuad(c+5,(vlTOPp->RegFile__DOT__mem[2]),64);
        vcdp->fullQuad(c+7,(vlTOPp->RegFile__DOT__mem[3]),64);
        vcdp->fullQuad(c+9,(vlTOPp->RegFile__DOT__mem[4]),64);
        vcdp->fullQuad(c+11,(vlTOPp->RegFile__DOT__mem[5]),64);
        vcdp->fullQuad(c+13,(vlTOPp->RegFile__DOT__mem[6]),64);
        vcdp->fullQuad(c+15,(vlTOPp->RegFile__DOT__mem[7]),64);
        vcdp->fullQuad(c+17,(vlTOPp->RegFile__DOT__mem[8]),64);
        vcdp->fullQuad(c+19,(vlTOPp->RegFile__DOT__mem[9]),64);
        vcdp->fullQuad(c+21,(vlTOPp->RegFile__DOT__mem[10]),64);
        vcdp->fullQuad(c+23,(vlTOPp->RegFile__DOT__mem[11]),64);
        vcdp->fullQuad(c+25,(vlTOPp->RegFile__DOT__mem[12]),64);
        vcdp->fullQuad(c+27,(vlTOPp->RegFile__DOT__mem[13]),64);
        vcdp->fullQuad(c+29,(vlTOPp->RegFile__DOT__mem[14]),64);
        vcdp->fullQuad(c+31,(vlTOPp->RegFile__DOT__mem[15]),64);
        vcdp->fullQuad(c+33,(vlTOPp->RegFile__DOT__mem[16]),64);
        vcdp->fullQuad(c+35,(vlTOPp->RegFile__DOT__mem[17]),64);
        vcdp->fullQuad(c+37,(vlTOPp->RegFile__DOT__mem[18]),64);
        vcdp->fullQuad(c+39,(vlTOPp->RegFile__DOT__mem[19]),64);
        vcdp->fullQuad(c+41,(vlTOPp->RegFile__DOT__mem[20]),64);
        vcdp->fullQuad(c+43,(vlTOPp->RegFile__DOT__mem[21]),64);
        vcdp->fullQuad(c+45,(vlTOPp->RegFile__DOT__mem[22]),64);
        vcdp->fullQuad(c+47,(vlTOPp->RegFile__DOT__mem[23]),64);
        vcdp->fullQuad(c+49,(vlTOPp->RegFile__DOT__mem[24]),64);
        vcdp->fullQuad(c+51,(vlTOPp->RegFile__DOT__mem[25]),64);
        vcdp->fullQuad(c+53,(vlTOPp->RegFile__DOT__mem[26]),64);
        vcdp->fullQuad(c+55,(vlTOPp->RegFile__DOT__mem[27]),64);
        vcdp->fullQuad(c+57,(vlTOPp->RegFile__DOT__mem[28]),64);
        vcdp->fullQuad(c+59,(vlTOPp->RegFile__DOT__mem[29]),64);
        vcdp->fullQuad(c+61,(vlTOPp->RegFile__DOT__mem[30]),64);
        vcdp->fullQuad(c+63,(vlTOPp->RegFile__DOT__mem[31]),64);
        vcdp->fullBus(c+513,(vlTOPp->io_regio_raddr1),5);
        vcdp->fullBus(c+521,(vlTOPp->io_regio_raddr2),5);
        vcdp->fullBus(c+529,(vlTOPp->io_regio_waddr),5);
        vcdp->fullQuad(c+537,(vlTOPp->io_regio_wdata),64);
        vcdp->fullBit(c+553,(vlTOPp->io_regio_wen));
        vcdp->fullQuad(c+561,(vlTOPp->io_regio_rdata1),64);
        vcdp->fullQuad(c+577,(vlTOPp->io_regio_rdata2),64);
        vcdp->fullBit(c+593,(vlTOPp->clk));
        vcdp->fullBit(c+601,(vlTOPp->reset));
    }
}
