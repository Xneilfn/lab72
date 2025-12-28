package com.functions.meta;

import com.Function;

public class Mult implements Function{
    private Function fun1;
    private Function fun2;
    public Mult(Function fun1, Function fun2){
        if (fun1 == null || fun2 == null) 
            throw new IllegalArgumentException("Functions cannot be null");
        
        this.fun1 = fun1;
        this.fun2 = fun2;
    }
    public double getLeftDomainBorder() {
        double left1 = fun1.getLeftDomainBorder();
        double left2 = fun2.getLeftDomainBorder();

        if (left1 == Double.NEGATIVE_INFINITY) {
            return left2;
        }
        if (left2 == Double.NEGATIVE_INFINITY) {
            return left1;
        }
        return Math.max(left1, left2);
    }
    public double getRightDomainBorder() {
        double right1 = fun1.getRightDomainBorder();
        double right2 = fun2.getRightDomainBorder();

        if (right1 == Double.POSITIVE_INFINITY) {
            return right2;
        }
        if (right2 == Double.NEGATIVE_INFINITY) {
            return right1;
        }
        return Math.min(right1, right2);
    }
    public double getFunctionValue(double x) throws IllegalArgumentException{
        if (!isInDomain(x)) {
            throw new IllegalArgumentException("Point x = " + x + " is not in the domain of the sum function");
        }
        return fun1.getFunctionValue(x) * fun2.getFunctionValue(x);
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