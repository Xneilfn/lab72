package com;

public class FunctionPoint {
    private double x;
    private double y;
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    double getX(){return x;}
    double getY(){return y;}
    void setX(double X){this.x = X;}
    void setY(double Y){this.y = Y;}

    public String toString(){
        return String.format("(%.2f; %.2f)", x, y);
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionPoint point = (FunctionPoint) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    public int hashCode(){
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }

    public Object clone(){
        return (Object) new FunctionPoint(this);
    }
}