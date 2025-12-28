package com.functions.meta;

import com.Function;

public class Power implements Function{
    private Function baseFunction;
    private double exponent;
    public Power(Function baseFunction, double exponent) throws IllegalArgumentException{ 
        if (baseFunction == null) {
            throw new IllegalArgumentException("Base function cannot be null");
        }
        this.baseFunction = baseFunction;
        this.exponent = exponent;
    }
    public double getFunctionValue(double x) throws IllegalArgumentException, ArithmeticException{
        if (!isInDomain(x)) {
            throw new IllegalArgumentException("Point x = " + x + " is not in the domain of the power function");
        }
        
        double baseValue = baseFunction.getFunctionValue(x);
        
        if (baseValue < 0 && exponent != (int) exponent) {
            throw new ArithmeticException("Cannot raise negative number to fractional power at x = " + x);
        }
        
        if (baseValue == 0 && exponent <= 0) {
            throw new ArithmeticException("Zero cannot be raised to non-positive power at x = " + x);
        }
        
        return Math.pow(baseValue, exponent);
    }
    public double getLeftDomainBorder() {
        return baseFunction.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return baseFunction.getRightDomainBorder();
    }
    private boolean isInDomain(double x){
        double left = getLeftDomainBorder();
        double right = getRightDomainBorder();

        if (left == Double.NEGATIVE_INFINITY && right == Double.POSITIVE_INFINITY) {
            return true;
        } else if (left == Double.NEGATIVE_INFINITY) {
            return x < right;
        } else if (right == Double.POSITIVE_INFINITY) {
            return x > left;
        } else {
            return x >= left && x <= right;
        }
    }
}