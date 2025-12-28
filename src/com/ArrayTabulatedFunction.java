package com;

import java.io.*;
import java.util.NoSuchElementException;

import com.functions.TabulatedFunctionFactory;

import java.util.Iterator;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {

    public void writeExternal(ObjectOutput out) throws IOException{
        if (out == null) {
            throw new IllegalArgumentException("Out stream cannot be null");
        }
        out.writeInt(this.size);
        for(int i = 0; i < this.size;i++){
            out.writeDouble(arr[i].getX());
            out.writeDouble(arr[i].getY());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
        this.size = in.readInt();
        FunctionPoint[] newArr = new FunctionPoint[this.size];
        for(int i = 0; i < this.size;i++){
            newArr[i] = new FunctionPoint(in.readDouble(), in.readDouble());
        }
        this.arr = newArr;
    }

    private FunctionPoint[] arr;
    private int size;

    public ArrayTabulatedFunction(){
        this.arr = new FunctionPoint[0];
        this.size = 0;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if (leftX >= rightX) throw new IllegalArgumentException("Error! leftX >= rightX");
        if (pointsCount <= 2) throw new IllegalArgumentException("Error! pointCount <= 2");
        this.arr = new FunctionPoint[pointsCount];
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * interval;
            if (i == pointsCount - 1) {
                x = rightX;
            }
            this.arr[i] = new FunctionPoint(x, 0);
        }
        this.size = pointsCount;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if (leftX >= rightX) throw new IllegalArgumentException("Error! leftX >= rightX");
        if (values.length <= 2) throw new IllegalArgumentException("Error! values.length <= 2");
        this.size = values.length;
        this.arr = new FunctionPoint[size];
        double interval = (rightX - leftX) / (size - 1);
        for (int i = 0; i < size; i++) {
            double x = leftX + i * interval;
            if (i == size - 1) {
                x = rightX;
            }
            this.arr[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException{
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Error! Must have at least 2 points");
        }
        
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Error! Points must be strictly ordered by X coordinate");
            }
        }
        
        this.size = points.length;
        this.arr = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            this.arr[i] = new FunctionPoint(points[i]);
        }
    }

    public double getLeftDomainBorder(){ return this.arr[0].getX();};
    public double getRightDomainBorder(){ return this.arr[this.size - 1].getX();}

    public double getFunctionValue(double x){
        if (x < this.arr[0].getX() || x > this.arr[this.size - 1].getX())
            return Double.NaN;
        else{
            int i = 0;
            while(i < this.size && x > this.arr[i].getX()){i++;}
            if (x == this.arr[i].getX()){ return this.arr[i].getY();}
            double y = (x - arr[i-1].getX())*(arr[i].getY() - arr[i-1].getY()) / (arr[i].getX() - arr[i-1].getX()) + arr[i-1].getY();
            return y;
        }
    }
    public int getPointsCount(){ return this.size;}
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{ 
        if (index >= this.size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        return arr[index];
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException{
        if (index >= this.size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        if ((index != 0 && this.arr[index-1].getX() > point.getX()) || (index != this.size - 1 && this.arr[index+1].getX() < point.getX()))
            throw new InappropriateFunctionPointException("Error! this point is not correct value");
        this.arr[index] = point;
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= this.size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        return this.arr[index].getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException{ 
        if (index >= this.size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        if ((index != 0 && this.arr[index-1].getX() > x) || (index != this.size - 1 && this.arr[index+1].getX() < x))
            throw new InappropriateFunctionPointException("Error! this point is not correct value");
        this.arr[index].setX(x);
    }
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= this.size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        return this.arr[index].getY();
    }
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{ 
        if (index >= this.size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        this.arr[index].setY(y);
    }

    public void deletePoint(int index) throws IllegalArgumentException{
        if (this.size < 3) throw new IllegalArgumentException("Error! values.length <= 2");
        FunctionPoint[] newArr = new FunctionPoint[this.size - 1];
        System.arraycopy(this.arr, 0, newArr, 0, index);
        System.arraycopy(this.arr, index+1, newArr, index, this.size - index - 1);
        this.arr = newArr;
        this.size--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int index = 0;
        while (index < this.size && this.arr[index].getX() < point.getX()) {index++;}
        if (this.arr[index].getX() == point.getX()) throw new InappropriateFunctionPointException("Error! this point is not correct value");
        FunctionPoint[] newArr = new FunctionPoint[this.size + 1];
        System.arraycopy(this.arr, 0, newArr, 0, index);
        newArr[index] = point;
        System.arraycopy(this.arr, index, newArr, index+1, this.size - index);
        this.arr = newArr;
        this.size++;
    }

    public String toString(){
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int i = 0; i < size - 1; i++){
            sb.append(arr[i].toString());
            sb.append(", ");
        }
        sb.append(arr[size - 1].toString());
        sb.append(" }");
        return sb.toString();
    }

    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || !(o instanceof TabulatedFunction)) return false;

        TabulatedFunction other = (TabulatedFunction) o;
        if (this.size != other.getPointsCount()) return false;

        if (o instanceof ArrayTabulatedFunction){
            ArrayTabulatedFunction cur = (ArrayTabulatedFunction) o;
            for(int i = 0; i < size; i++){
                if (!(this.arr[i].equals(cur.arr[i]))) return false;
            }
        }
        else{
            for(int i = 0; i < size; i++){
                if (!(this.arr[i].equals(other.getPoint(i)))) return false;
            }
        }

        return true;
    }

    public int hashCode(){
        if (size == 0) return 0;

        int hash = size;

        for (int i = 0; i < size; i++){
            hash = hash ^ arr[i].hashCode();
        }
        return hash;
    }

    public Object clone(){
        FunctionPoint[] copy = new FunctionPoint[size];
        for (int i = 0; i < size; i++){
            copy[i] = (FunctionPoint) arr[i].clone();
        }
        return new ArrayTabulatedFunction(copy);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>(){
            private int position = 0;

            public boolean hasNext() {
                return position < size;
            }


            public FunctionPoint next() {
                if(!hasNext()) throw new NoSuchElementException();
                return new FunctionPoint(arr[position++]);
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory{
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points)  {
            return new ArrayTabulatedFunction(points);
        }
    }

    public void printFunction() {
        System.out.println("Tabulated function points:");
        for (int i = 0; i < this.size; i++) {
            System.out.printf("Point %d: x=%.2f, y=%.2f\n", i, arr[i].getX(), arr[i].getY());
        }
    }
}