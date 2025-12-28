package com.threads;

import com.functions.Functions;

public class SimpleIntegrator implements Runnable{
    private Task tasks;

    public SimpleIntegrator(Task task) {this.tasks = task;}

    public void run() {
        for(int i = 0; i < tasks.getTasksCount(); i++){
            while(!tasks.getReady()) {

            }
            double result = Functions.integrate(tasks.getF(), tasks.getLeftX(), tasks.getRightX(), tasks.getStep());
            System.out.println("Result " + tasks.getLeftX() + " " + tasks.getRightX() + " " + tasks.getStep() + " " + result);

            tasks.setReady(false);
            }

    }
}
