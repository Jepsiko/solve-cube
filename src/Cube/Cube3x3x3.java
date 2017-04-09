package Cube;

public class Cube3x3x3 extends Cuboid {
    
    // Constructor
    Cube3x3x3() {
        super(3);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Special Movements">
    void l() {
        L();
        M();
    }
    void lP() {
        LP();
        MP();
    }
    void l2() {
        L2();
        M2();
    }
    
    void r() {
        R();
        M();
    }
    void rP() {
        RP();
        MP();
    }
    void r2() {
        R2();
        M2();
    }
    
    void b() {
        B();
        S();
    }
    void bP() {
        BP();
        SP();
    }
    void b2() {
        B2();
        S2();
    }
    
    void f() {
        F();
        S();
    }
    void fP() {
        FP();
        SP();
    }
    void f2() {
        F2();
        S2();
    }
    
    void d() {
        D();
        E();
    }
    void dP() {
        DP();
        EP();
    }
    void d2() {
        D2();
        E2();
    }
    
    void u() {
        U();
        E();
    }
    void uP() {
        UP();
        EP();
    }
    void u2() {
        U2();
        E2();
    }
    
    private void M() { turnFace('x', 1, true); }
    private void MP() { turnFace('x', 1, false); }
    private void M2() { halfTurn('x', 1); }
    
    private void S() { turnFace('y', 1, true); }
    private void SP() { turnFace('y', 1, false); }
    private void S2() { halfTurn('y', 1); }
    
    private void E() { turnFace('z', 1, true); }
    private void EP() { turnFace('z', 1, false); }
    private void E2() { halfTurn('z', 1); }
    //</editor-fold>
}
