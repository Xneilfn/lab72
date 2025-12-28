package com;

import java.io.*;
import java.util.NoSuchElementException;

import com.functions.TabulatedFunctionFactory;

import java.util.Iterator;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable{

    public void writeExternal(ObjectOutput out) throws IOException{
        if (out == null) {
            throw new IllegalArgumentException("Out stream cannot be null");
        }
        FunctionNode cur = head;
        out.writeInt(this.size);
        for(int i = 0; i < this.size;i++){
            cur = cur.next;
            out.writeDouble(cur.data.getX());
            out.writeDouble(cur.data.getY());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
        int size1 = in.readInt();
        for(int i = 0; i < size1;i++){
            this.addNodeToTail();
            this.getNodeByIndex(this.size-1).data = new FunctionPoint(in.readDouble(), in.readDouble());
        }
    }

    
    private static class FunctionNode {
        private FunctionPoint data;
        private FunctionNode next;
        private FunctionNode prev;
    }
    
    private FunctionNode head;
    private int size;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;

    public LinkedListTabulatedFunction(){
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        size = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if (leftX >= rightX) throw new IllegalArgumentException("Error! leftX >= rightX");
        if (pointsCount <= 2) throw new IllegalArgumentException("Error! pointCount <= 2");

        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        size = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;

        double interval = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * interval;
            if (i == pointsCount - 1) {
                x = rightX;
            }
        
            FunctionNode newNode = addNodeToTail();
            newNode.data = new FunctionPoint(x, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if (leftX >= rightX) throw new IllegalArgumentException("Error! leftX >= rightX");
        if (values.length <= 2) throw new IllegalArgumentException("Error! values.length <= 2");

        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        size = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;

        double interval = (rightX - leftX) / (values.length - 1);
        
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * interval;
            if (i == values.length - 1) {
                x = rightX; 
            }
        
            FunctionNode newNode = addNodeToTail();
            newNode.data = new FunctionPoint(x, values[i]);
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException{
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Error! Must have at least 2 points");
        }
    
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Error! Points must be strictly ordered by X coordinate");
            }
        }
        
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        size = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;

        for (int i = 0; i < points.length; i++) {
            FunctionNode newNode = addNodeToTail();
            newNode.data = new FunctionPoint(points[i]);
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        
        FunctionNode current;
        int startIndex;
        
        if (lastAccessedIndex != -1 && Math.abs(index - lastAccessedIndex) < Math.min(index, size - index)) {
            current = lastAccessedNode;
            startIndex = lastAccessedIndex;
        } else if (index <= size / 2) {
            current = head.next;
            startIndex = 0;
        } else {
            current = head.prev;
            startIndex = size - 1;
        }
        
        while (startIndex < index) {
            current = current.next;
            startIndex++;
        }
        while (startIndex > index) {
            current = current.prev;
            startIndex--;
        }
        
        lastAccessedNode = current;
        lastAccessedIndex = index;
        return current;
    }
    
    private FunctionNode addNodeToTail() {
        return addNodeByIndex(size);
    }
    
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        
        FunctionNode newNode = new FunctionNode();
        FunctionNode nextNode, prevNode;
        
        if (index == size) {
            nextNode = head;
            prevNode = head.prev;
        } else {
            nextNode = getNodeByIndex(index);
            prevNode = nextNode.prev;
        }
        
        newNode.next = nextNode;
        newNode.prev = prevNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;
        
        size++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;
        
        return newNode;
    }
    
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        
        FunctionNode toDelete = getNodeByIndex(index);
        
        toDelete.prev.next = toDelete.next;
        toDelete.next.prev = toDelete.prev;
        
        if (lastAccessedIndex == index) {
            lastAccessedNode = (index == size - 1) ? head.prev : toDelete.next;
            lastAccessedIndex = (index == size - 1) ? size - 2 : index;
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }
        
        size--;
        
        toDelete.next = null;
        toDelete.prev = null;
        
        return toDelete;
    }

    public double getLeftDomainBorder(){ return head.next.data.getX();};
    public double getRightDomainBorder(){ return head.prev.data.getX();}

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int index = 0;
        while (index < this.size && getNodeByIndex(index).data.getX() < point.getX()) {index++;}
        if (index != size && getNodeByIndex(index).data.getX() == point.getX()) throw new InappropriateFunctionPointException("Error! this point is not correct value");
        addNodeByIndex(index);
        getNodeByIndex(index).data = point;
    }

    public void deletePoint(int index) throws IllegalArgumentException{
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        deleteNodeByIndex(index);
    }

    public double getFunctionValue(double x){
        if (x < head.next.data.getX() || x > head.prev.data.getX())
            return Double.NaN;
        else{
            int i = 0;
            while(i < size && x > getNodeByIndex(i).data.getX()){i++;}
            if (x == getNodeByIndex(i).data.getX()){ return getNodeByIndex(i).data.getY();}
            double y = (x - getNodeByIndex(i-1).data.getX())*(getNodeByIndex(i).data.getY() - getNodeByIndex(i-1).data.getY()) / 
            (getNodeByIndex(i).data.getX() - getNodeByIndex(i-1).data.getX()) + getNodeByIndex(i-1).data.getY();
            return y;
        }
    }

    public int getPointsCount(){ return size;}

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{ 
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        return getNodeByIndex(index).data;
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException{
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        if ((index != 0 && getNodeByIndex(index-1).data.getX() > point.getX()) || (index != size - 1 && getNodeByIndex(index+1).data.getX() < point.getX()))
            throw new InappropriateFunctionPointException("Error! this point is not correct value");
        getNodeByIndex(index).data = point;
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        return getNodeByIndex(index).data.getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException{ 
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        if ((index != 0 && getNodeByIndex(index-1).data.getX() > x) || (index != size - 1 && getNodeByIndex(index+1).data.getX() < x))
            throw new InappropriateFunctionPointException("Error! this point is not correct value");
        getNodeByIndex(index).data.setX(x);
    }
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        return getNodeByIndex(index).data.getY();
    }
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{ 
        if (index >= size || index < 0) throw new FunctionPointIndexOutOfBoundsException("Error! this index is not included");
        getNodeByIndex(index).data.setY(y);
    }

    public String toString(){
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        FunctionNode node = head.next;
        for (int i = 0; i < size - 1; i++){
            sb.append(node.data.toString());
            sb.append(", ");
            node = node.next;
        }
        sb.append(node.data.toString().toString());
        sb.append(" }");
        return sb.toString();
    }

    
    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || !(o instanceof TabulatedFunction)) return false;

        TabulatedFunction other = (TabulatedFunction) o;
        if (this.size != other.getPointsCount()) return false;

        if (o instanceof LinkedListTabulatedFunction){
            LinkedListTabulatedFunction cur = (LinkedListTabulatedFunction) o;
            FunctionNode n1 = head.next;
            FunctionNode n2 = cur.head.next;
            for(int i = 0; i < size; i++){
                if (!(n1.data.equals(n2.data))) return false;
                n1 = n1.next;
                n2 = n2.next;
            }
        }
        else{
            FunctionNode node = head.next;
            for(int i = 0; i < size; i++){
                if (!(node.data.equals(other.getPoint(i)))) return false;
                node = node.next;
            }
        }

        return true;
    }

    public int hashCode(){
        if (size == 0) return 0;

        int hash = size;
        FunctionNode node = head.next;

        for (int i = 0; i < size; i++){
            hash = hash ^ node.data.hashCode();
            node = node.next;
        }
        return hash;
    }

    public Object clone(){
        FunctionPoint[] copy = new FunctionPoint[size];
        FunctionNode node = head.next;
        for (int i = 0; i < size; i++){
            copy[i] = (FunctionPoint) node.data.clone();
            node = node.next;
        }
        return new LinkedListTabulatedFunction(copy);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode currentNode = head.next;
            public boolean hasNext() {
                return currentNode != head;
            }


            public FunctionPoint next() {
                if(!hasNext()) throw new NoSuchElementException();
                FunctionPoint point = new FunctionPoint(currentNode.data);
                currentNode = currentNode.next;
                return point;
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }

    public void printFunction() {
        System.out.println("Tabulated function points:");
        for (int i = 0; i < size; i++) {
            System.out.printf("Point %d: x=%.2f, y=%.2f\n", i, getNodeByIndex(i).data.getX(), getNodeByIndex(i).data.getY());
        }
    }
}