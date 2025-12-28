package com.functions.meta;

import com.Function;

public class Scale implements Function{
    private Function originalFunction;
    private double xScale;
    private double yScale;
    
    public Scale(Function originalFunction, double xScale, double yScale) throws IllegalArgumentException{
        if (originalFunction == null) {
            throw new IllegalArgumentException("Original function cannot be null");
        }
        if (xScale == 0) {
            throw new IllegalArgumentException("X scale coefficient cannot be zero");
        }
        this.originalFunction = originalFunction;
        this.xScale = xScale;
        this.yScale = yScale;
    }
    
    public double getFunctionValue(double x) {
        double originalX = x / xScale;
        
        if (!isInOriginalDomain(originalX)) {
            throw new IllegalArgumentException("Transformed point x = " + originalX + " is not in the domain of original function");
        }
        
        return yScale * originalFunction.getFunctionValue(originalX);
    }

    public double getLeftDomainBorder() {
        return scaleDomainBorder(originalFunction.getLeftDomainBorder(), originalFunction.getRightDomainBorder(), true);
    }
    
    
    public double getRightDomainBorder() {
        return scaleDomainBorder(originalFunction.getLeftDomainBorder(), originalFunction.getRightDomainBorder(), false);
    }
    
    private double scaleDomainBorder(double originalLeft, double originalRight, boolean isLeftBorder) {
        if (xScale > 0) {
            double border = isLeftBorder ? originalLeft : originalRight;
            if (border == Double.NEGATIVE_INFINITY || border == Double.POSITIVE_INFINITY) {
                return border;
            }
            return border * xScale;
        } else {
            double border = isLeftBorder ? originalRight : originalLeft;
            if (border == Double.NEGATIVE_INFINITY) return Double.POSITIVE_INFINITY;
            if (border == Double.POSITIVE_INFINITY) return Double.NEGATIVE_INFINITY;
            return border * xScale;
        }
    }
    
    private boolean isInOriginalDomain(double x) {
        double left = originalFunction.getLeftDomainBorder();
        double right = originalFunction.getRightDomainBorder();
        
        return (x >= left || left == Double.NEGATIVE_INFINITY) && 
               (x <= right || right == Double.POSITIVE_INFINITY);
    }

}
