package com.threads;

import com.functions.Functions;

public class Integrator extends Thread{
    Task tasks;
    Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore){
        this.tasks = task;
        this.semaphore = semaphore;
    }

    public void run() {
        try {
            for (int i = 0; i < tasks.getTasksCount(); i++) {
                if(Thread.interrupted()) throw new InterruptedException();
                try {
                    semaphore.beginRead();
                    double result = Functions.integrate(tasks.getF(), tasks.getLeftX(), tasks.getRightX(), tasks.getStep());
                    System.out.println("Result " + tasks.getLeftX() + " " + tasks.getRightX() + " " + tasks.getStep() + " " + result);
                } finally {
                    semaphore.endRead();
                }
            }
        } catch(InterruptedException e){
            System.out.println("Интегратор был прерван");
        }
    }
}
