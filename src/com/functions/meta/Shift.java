package com.functions.meta;

import com.Function;

public class Shift implements Function{
    private Function originalFunction;
    private double xShift;
    private double yShift;

    public Shift(Function originalFunction, double xShift, double yShift) throws IllegalArgumentException{
        if (originalFunction == null) {
            throw new IllegalArgumentException("Original function cannot be null");
        }
        this.originalFunction = originalFunction;
        this.xShift = xShift;
        this.yShift = yShift;
    }
    
    public double getFunctionValue(double x) throws IllegalArgumentException{
        if (!isDomain(x)) {
            throw new IllegalArgumentException("Point x = " + x + " is not in the domain of the shifted function");
        }
        
        double originalX = x - xShift;
        double originalValue = originalFunction.getFunctionValue(originalX);
        
        return originalValue + yShift;
    }
    
    public double getLeftDomainBorder() {
        double originalLeft = originalFunction.getLeftDomainBorder();
        
        if (originalLeft == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        }
        
        return originalLeft + xShift;
    }
    
    public double getRightDomainBorder() {
        double originalRight = originalFunction.getRightDomainBorder();
        
        if (originalRight == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        
        return originalRight + xShift;
    }
    
    private boolean isDomain(double x) {
        double originalX = x - xShift;
        double originalLeft = originalFunction.getLeftDomainBorder();
        double originalRight = originalFunction.getRightDomainBorder();
        
        return (originalX >= originalLeft || originalLeft == Double.NEGATIVE_INFINITY) && 
               (originalX <= originalRight || originalRight == Double.POSITIVE_INFINITY);
    }
}
