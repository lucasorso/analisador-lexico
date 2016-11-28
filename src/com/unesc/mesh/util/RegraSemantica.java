/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unesc.mesh.util;

/**
 *
 * @author LucasOrso
 */
public enum RegraSemantica {
    
    REGRA_100(100, "Regra_100"),
    REGRA_110(110, "Regra_110"),
    REGRA_120(120, "Regra_120"),
    REGRA_130(130, "Regra_130"),
    REGRA_140(140, "Regra_140"),
    REGRA_150(150, "Regra_150");
    
    int mRegra;
    String mNomeRegra;
    
    RegraSemantica(int aRegra, String aNomeRegra){
        mRegra = aRegra;
        mNomeRegra = aNomeRegra;
    }

    public int getmRegra() {
        return mRegra;
    }

    public void setmRegra(int mRegra) {
        this.mRegra = mRegra;
    }

    public String getmNomeRegra() {
        return mNomeRegra;
    }

    public void setmNomeRegra(String mNomeRegra) {
        this.mNomeRegra = mNomeRegra;
    }
    
    public static RegraSemantica getRegraSemantica(int aIdRegra){
        for (RegraSemantica r : values()){
            if (r.getmRegra() == aIdRegra){
                return r;
            } 
        }
        return null;
    }
}
