package com.threads;

import com.Function;

public class Task {
    private Function f;
    private double leftX;
    private double rightX;
    private double step;
    private int tasksCount;

    private volatile boolean isReady = false;

    public Function getF() {
        return f;
    }

    public void setReady(boolean isReady){
        this.isReady = isReady;
    }


    public boolean getReady(){
        return isReady;
    }

    public void setF(Function f) {
        this.f = f;
    }

    public double getLeftX() {
        return leftX;
    }

    public void setLeftX(double leftX) {
        this.leftX = leftX;
    }

    public double getRightX() {
        return rightX;
    }

    public void setRightX(double rightX) {
        this.rightX = rightX;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    public Task(int tasksCount) {
        this.tasksCount = tasksCount;
    }
}
