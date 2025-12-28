package com.threads;

import com.functions.basic.Log;
import com.Function;

public class SimpleGenerator implements Runnable{

    private Task tasks;

    public SimpleGenerator(Task task) {this.tasks = task;}

    public void run() {
        for (int i = 0; i < tasks.getTasksCount(); i++) {

            while(tasks.getReady()) {

            }

            Function f = new Log(1 + Math.random() * 9);
            tasks.setF(f);
            tasks.setLeftX(Math.random() * 100);
            tasks.setRightX(100 + Math.random() * 100);
            tasks.setStep(Math.random());
            System.out.println("Source " + tasks.getLeftX() + " " + tasks.getRightX() + " " + tasks.getStep());

            tasks.setReady(true);
            }

    }
}
