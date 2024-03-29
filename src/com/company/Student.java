package com.company;

import java.util.Random;

public class Student extends Thread{
    private Printer printer;

    public Student(ThreadGroup studentThreadGroup, Printer printer, String stuName) {
        super(studentThreadGroup,stuName);
        this.printer = printer;
    }

    @Override
    public void run(){
        int numOfDocument = 5;

        for (int i = 1; i <= numOfDocument; i++) {
            String docName = "Doc"+ (i);

            Random ranNum = new Random();

            int docLength = 1 + ranNum.nextInt(10 - 1);

            Document document = new Document(this.getName(),docName,docLength);
            printer.printDocument(document);

            boolean endLoop = i == numOfDocument ;
            if (!endLoop){

                try {
                    int sleepTime = 1000 + ranNum.nextInt(5000 - 1000);
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        System.out.println("\n\t <<<<<<<  "+this.getName()+ " finished the document printing >>>>>>");
    }
}
