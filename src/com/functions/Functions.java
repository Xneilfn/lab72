package com.functions;

import com.Function;

import com.functions.meta.*;

public class Functions {
    private Functions() throws AssertionError{
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Double integrate(Function function, double leftX, double rightX, double h){
        if (leftX > rightX) throw new IllegalArgumentException();

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) throw new IllegalArgumentException();

        if (h <= 0) throw new IllegalArgumentException();

        double sum = 0;
        
        double correctX = leftX;
        while (correctX + h < rightX){
            sum += (function.getFunctionValue(correctX) + function.getFunctionValue(correctX + h)) * h / 2;
            correctX += h;
        }

        if (correctX < rightX){
            sum += (function.getFunctionValue(correctX) + function.getFunctionValue(rightX)) * (rightX - correctX) / 2;
        }

        return sum;
    }
}