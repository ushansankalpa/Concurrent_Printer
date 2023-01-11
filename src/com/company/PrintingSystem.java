package com.company;

public class PrintingSystem {
    public static void main(String[] args) {
        ThreadGroup studentThreadGroup = new ThreadGroup("Student");
        ThreadGroup techThreadGroup = new ThreadGroup("Technician");

        ServicePrinter printer = new LaserPrinter("DELL",50,20,0,studentThreadGroup);

        Thread student1 = new Student(studentThreadGroup,printer,"Student 1");
        Thread student2 = new Student(studentThreadGroup,printer,"Student 2");
        Thread student3 = new Student(studentThreadGroup,printer,"Student 3");
        Thread student4 = new Student(studentThreadGroup,printer,"Student 4");

        Thread paperTech = new PaperTechnician(techThreadGroup,printer,"Paper Technician");
        Thread tonerTech = new TonerTechnician(techThreadGroup,printer,"Toner Technician");

        student1.start();
        student2.start();
        student3.start();
        student4.start();

        paperTech.start();
        tonerTech.start();


        try {
            student1.join();
            student2.join();
            student3.join();
            student4.join();

            paperTech.join();
            tonerTech.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        System.out.println("\n ******* All document printing are successfully finished *******");

        System.out.println("\n\t\t ============== Summery ==============");
        System.out.println(printer.toString());

    }

}
