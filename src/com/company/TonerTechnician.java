package com.company;

import java.util.Random;

public class TonerTechnician extends Thread {
    private ServicePrinter printer;


    public TonerTechnician(ThreadGroup techThreadGroup, ServicePrinter printer, String techName) {
        super(techThreadGroup, techName);
        this.printer = printer;
    }

    @Override
    public void run(){
        int replaceAttempt = 3;

        for (int i = 0; i < replaceAttempt; i++) {
            Random ranNum = new Random();

            printer.replaceTonerCartridge();

            try {
                int sleepTime = 1000 + ranNum.nextInt(5000 -1000);
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.println("\n\t------ "+this.getName()+" finished attempts to replace toner cartridge -----");
    }

}
