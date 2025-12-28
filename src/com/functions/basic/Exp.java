package com.functions.basic;

import com.Function;

public class Exp implements Function{
    public Exp(){};

    public double getFunctionValue(double x) {
        return Math.exp(x);
    }
    
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}