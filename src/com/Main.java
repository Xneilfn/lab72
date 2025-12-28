package com;
import java.io.*;

import com.functions.*;
import com.functions.basic.*;
import com.threads.*;

public class Main {
    public static void nonThread(){
        Task tasks = new Task(100);

        for(int i = 0; i < tasks.getTasksCount(); i++){
            Function f = new Log(1 + Math.random() * 9);
            tasks.setF(f);
            tasks.setLeftX(Math.random() * 100);
            tasks.setRightX(100 + Math.random() * 100);
            tasks.setStep(Math.random());
            System.out.println("Source " + tasks.getLeftX() + " " + tasks.getRightX() + " " + tasks.getStep());
            double result = Functions.integrate(tasks.getF(), tasks.getLeftX(), tasks.getRightX(), tasks.getStep());
            System.out.println("Result " + tasks.getLeftX() + " " + tasks.getRightX() + " " + tasks.getStep() + " " + result);
        }
    }

    public static void simpleThreads(){
        Task tasks = new Task(100);
        Thread generator = new Thread(new SimpleGenerator(tasks));
        Thread integrator = new Thread(new SimpleIntegrator(tasks));
        generator.setPriority(Thread.MAX_PRIORITY);
        integrator.setPriority(Thread.NORM_PRIORITY);
        generator.start();
        integrator.start();
    }

    public static void complicatedThreads() {
        Task tasks = new Task(100);
        Semaphore semaphore = new Semaphore();
        Generator generator = new Generator(tasks,semaphore);
        Integrator integrator = new Integrator(tasks, semaphore);
        generator.setPriority(Thread.MAX_PRIORITY);
        integrator.setPriority(Thread.NORM_PRIORITY);
        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        generator.interrupt();
        integrator.interrupt();

        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            System.out.println(" ");
        }

    }   

    public static void main(String[] args) {
        try{
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}