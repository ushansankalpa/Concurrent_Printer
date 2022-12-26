package com.company;

import java.util.Random;

public class PaperTechnician extends Thread{
    private ServicePrinter printer;

    public PaperTechnician(ThreadGroup techThreadGroup, ServicePrinter printer, String techName) {
        super(techThreadGroup,techName);
        this.printer = printer;
    }


    @Override
    public void run(){

        int refillAttempt = 3;

        for (int i = 0; i < refillAttempt; i++) {
            Random ranNum = new Random();

            printer.refillPaper();

            try {
                int sleepTime = 1000 + ranNum.nextInt(5000 -1000);
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("\n\t------ "+this.getName()+" finished attempts to refill paper packs ------");

    }


}
