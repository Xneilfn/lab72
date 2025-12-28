package com.threads;

import com.functions.basic.Log;
import com.Function;

public class Generator extends Thread{
    Task tasks;
    Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore){
        this.tasks = task;
        this.semaphore = semaphore;
    }

    public void run() {

        try {
            for (int i = 0; i < tasks.getTasksCount(); i++) {
                if (Thread.interrupted()) throw new InterruptedException();

                try {
                    semaphore.beginWrite();
                    Function f = new Log(1 + Math.random() * 9);
                    tasks.setF(f);
                    tasks.setLeftX(Math.random() * 100);
                    tasks.setRightX(100 + Math.random() * 100);
                    tasks.setStep(Math.random());
                    System.out.println("Source " + tasks.getLeftX() + " " + tasks.getRightX() + " " + tasks.getStep());
                } finally {
                    semaphore.endWrite();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Генератор был прерван");
        }
    }
}
