package com.functions;

import com.FunctionPoint;
import com.TabulatedFunction;

public interface TabulatedFunctionFactory {
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount);
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double values[]);
    public TabulatedFunction createTabulatedFunction(FunctionPoint[] points);
}
