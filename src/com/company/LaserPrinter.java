package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LaserPrinter implements ServicePrinter {
    private String printerID;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numOfDocPrinted;
    private ThreadGroup studentThreadGroup;

    ReentrantLock lock = new ReentrantLock(true);
    Condition condition = lock.newCondition();

    public LaserPrinter(String printerID, int currentPaperLevel, int currentTonerLevel, int numOfDocPrinted, ThreadGroup studentThreadGroup) {
        this.printerID = printerID;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numOfDocPrinted = numOfDocPrinted;
        this.studentThreadGroup = studentThreadGroup;
    }

    @Override
    public  void printDocument(Document document) {
        lock.lock();
        String userId = document.getUserID();
        String docName = document.getDocumentName();
        int pageLength = document.getNumberOfPages();

        while (this.currentPaperLevel < pageLength || this.currentTonerLevel < pageLength) {

            if (this.currentPaperLevel < pageLength && this.currentTonerLevel < pageLength) {
                System.out.println("\n[Student : " + userId + "| Document : " + docName + "| Pages :" + pageLength + "] *** Unavailable Paper & Toner level *** [ Paper Level :" + currentPaperLevel + " | Toner Level -" + currentTonerLevel + "]");
            } else if (this.currentPaperLevel < pageLength) {
                System.out.println("\n[Student : " + userId + "| Document : " + docName + "| Pages :" + pageLength + "] *** Unavailable Paper level *** [ Paper Level :" + currentPaperLevel + " ]");

            } else {
                System.out.println("\n[Student : " + userId + "| Document : " + docName + "| Pages :" + pageLength + "] *** Unavailable Toner level *** [ Toner Level :" + currentTonerLevel + " ]");
            }

            try {
                condition.await();
                //wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        lock.unlock();

        System.out.println("\n[Student : " + userId + "| Document : " + docName + "| Pages :" + pageLength + "] >> Starting print document with " + pageLength + " pages <<");

        this.currentPaperLevel -= pageLength;
        this.currentTonerLevel -= pageLength;
        numOfDocPrinted++;
        System.out.println("[Student : " + userId + "| Document : " + docName + "| Pages :" + pageLength + "] >> Document print successfully. New paper level : " + currentPaperLevel + " & New toner level :" + currentTonerLevel + " <<");


    }


    @Override
    public void replaceTonerCartridge() {
        lock.lock();
        while (currentTonerLevel >= Minimum_Toner_Level) {
            System.out.println("\nChecking current toner level .... [ It is not necessary to replace the toner at this time.]  Current Toner Level :" + currentTonerLevel);

            try {
                condition.await(5000, TimeUnit.MILLISECONDS);
                //wait(5000);
                if (studentThreadGroup.activeCount() < 1) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nChecking current toner level .... *** Toner level low *** [ Start replacing toner cartridge ...]  Current Toner Level :" + currentTonerLevel);
        currentTonerLevel += PagesPerTonerCartridge;
        System.out.println("<<< [ Successfully replaced toner cartridge ...] >>> Current Toner Level :" + currentTonerLevel);
        //notifyAll();
        condition.signalAll();
        lock.unlock();
    }

    @Override
    public void refillPaper() {
        lock.lock();
        while ((this.currentPaperLevel + SheetsPerPack) > Full_Paper_Tray) {
            System.out.println("\nChecking current paper level .... [ It is not necessary to replace the paper at this time.]  Current Paper Level :" + currentPaperLevel);

            try {
                condition.await(5000, TimeUnit.MILLISECONDS);
                //wait(5000);
                if (studentThreadGroup.activeCount() < 1) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nChecking current paper level .... *** Paper level low *** [ Start replacing Paper pack ...]  Current Paper Level :" + currentPaperLevel);
        currentPaperLevel += SheetsPerPack;
        System.out.println("<<< [ Successfully replaced Paper pack ...] >>> Current Paper Level :" + currentPaperLevel);
        //notifyAll();
        condition.signalAll();
        lock.unlock();
    }

    @Override
    public String toString() {
        return "LaserPrinter { " +
                "printerID='" + printerID + '\'' +
                ", Current Paper Level=" + currentPaperLevel +
                ", Current Toner Level=" + currentTonerLevel +
                ", Printed Documents=" + numOfDocPrinted +
                '}';
    }
}
