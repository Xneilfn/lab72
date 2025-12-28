package com.functions;



import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.ArrayTabulatedFunction;
import com.TabulatedFunction;
import com.Function;
import com.FunctionPoint;
import com.InappropriateFunctionPointException;
import com.LinkedListTabulatedFunction;

public class TabulatedFunctions{
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();


    private TabulatedFunctions() throws AssertionError{
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (function == null) throw new IllegalArgumentException();
        if (pointsCount < 2) throw new IllegalArgumentException();
        if (leftX >= rightX ) throw new IllegalArgumentException();

        if (leftX < function.getLeftDomainBorder() || leftX > function.getRightDomainBorder()) throw new IllegalArgumentException();
        if (rightX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) throw new IllegalArgumentException();

        FunctionPoint values[] = new FunctionPoint[pointsCount];
        double interval = (rightX - leftX)/ (pointsCount - 1);
        for(int i = 0; i < pointsCount; i++){
            double x = leftX + i * interval;
            if (i == pointsCount - 1) x = rightX;
            values[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }

        return factory.createTabulatedFunction(values);
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass, Function function, double leftX, double rightX, int pointsCount){
        if (function == null) throw new IllegalArgumentException();
        if (pointsCount < 2) throw new IllegalArgumentException();
        if (leftX >= rightX ) throw new IllegalArgumentException();

        if (leftX < function.getLeftDomainBorder() || leftX > function.getRightDomainBorder()) throw new IllegalArgumentException();
        if (rightX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) throw new IllegalArgumentException();

        FunctionPoint values[] = new FunctionPoint[pointsCount];
        double interval = (rightX - leftX)/ (pointsCount - 1);
        for(int i = 0; i < pointsCount; i++){
            double x = leftX + i * interval;
            if (i == pointsCount - 1) x = rightX;
            values[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getDeclaredConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) values);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException{
        if (function == null) {
            throw new IllegalArgumentException("Function cannot be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Out stream cannot be null");
        }

        DataOutputStream dataOut = new DataOutputStream(out);

        int pointsCount = function.getPointsCount();

        dataOut.writeInt(pointsCount);

        for(int i = 0; i < pointsCount;i++){
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        out.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException{
        if(in == null){
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();

        FunctionPoint points[] = new FunctionPoint[pointsCount];
    
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }

        return factory.createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        if (function == null) {
            throw new IllegalArgumentException("Function cannot be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Out stream cannot be null");
        }
        
        PrintWriter writer = new PrintWriter(out);

        int pointsCount = function.getPointsCount();
        writer.print(pointsCount);
        writer.print(' ');

        for(int i=0; i < pointsCount; i++){
            writer.print(function.getPointX(i));
            writer.print(' ');
            writer.print(function.getPointY(i));
            if (i < pointsCount - 1){writer.print(' ');}
        }

        writer.println();
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        if(in == null){
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int)tokenizer.nval;

        FunctionPoint points[] = new FunctionPoint[pointsCount];
        int i =0;

        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
            i++;
        }
        
        return factory.createTabulatedFunction(points);
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory Factory){
        factory = Factory;
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws InappropriateFunctionPointException {
        return factory.createTabulatedFunction(points);
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getDeclaredConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getDeclaredConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass, FunctionPoint[] points) throws InappropriateFunctionPointException {
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getDeclaredConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
