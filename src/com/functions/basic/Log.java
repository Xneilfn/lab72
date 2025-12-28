package com.functions.basic;

import com.Function;

public class Log implements Function {
    double base = 0;

    public Log(){};

    public Log(double base){
        if (base <= 0 || base == 1) throw new IllegalArgumentException();
        this.base = base;
    }

    public double getFunctionValue(double x) throws IllegalArgumentException{
        if (base == 0){
            return Math.log(x);
        }
        else{
            return Math.log(x) / Math.log(base);
        }
    }
    
    public double getLeftDomainBorder() {
        return Double.MIN_VALUE;
    }
    
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}
