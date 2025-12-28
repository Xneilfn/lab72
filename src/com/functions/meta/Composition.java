package com.functions.meta;

import com.Function;

public class Composition implements Function{
    private Function outerFunction;
    private Function innerFunction;
    
    public Composition(Function outerFunction, Function innerFunction) {
        if (outerFunction == null || innerFunction == null) {
            throw new IllegalArgumentException("Functions cannot be null");
        }
        this.outerFunction = outerFunction;
        this.innerFunction = innerFunction;
    }
    
    public double getFunctionValue(double x) throws IllegalArgumentException{
        if (!isInInnerDomain(x)) {
            throw new IllegalArgumentException("Point x = " + x + " is not in the domain of inner function");
        }
        
        double innerValue = innerFunction.getFunctionValue(x);
        
        if (!isInOuterDomain(innerValue)) {
            throw new IllegalArgumentException("Value " + innerValue + " is not in the domain of outer function");
        }
        
        return outerFunction.getFunctionValue(innerValue);
    }

    public double getLeftDomainBorder() {
        return innerFunction.getLeftDomainBorder();
    }
    
    public double getRightDomainBorder() {
        return innerFunction.getRightDomainBorder();
    }
    
    private boolean isInInnerDomain(double x) {
        double left = innerFunction.getLeftDomainBorder();
        double right = innerFunction.getRightDomainBorder();
        
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
    
    private boolean isInOuterDomain(double x) {
        double left = outerFunction.getLeftDomainBorder();
        double right = outerFunction.getRightDomainBorder();
        
        if (left == Double.NEGATIVE_INFINITY && right == Double.POSITIVE_INFINITY) {
            return true;
        } else if (left == Double.NEGATIVE_INFINITY) {
            return x < right;
        } else if (right == Double.POSITIVE_INFINITY) {
            return x > left;
        } else {
            return x > left && x < right;
        }
    }
}
