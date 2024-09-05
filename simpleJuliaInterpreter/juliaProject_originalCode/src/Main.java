//Class: CS 4308 Section #2
//Term: Summer 2023
//Name: Katie Carlson
//Instructor: Sharon Perry


import java.io.*;
import java.util.*;


public class Main {
    public static void main(String[] args) {


        Lexer lexicalAnalyzer = new Lexer();

        Scanner scan = new Scanner(System.in);


        System.out.println("The name of the Julia file: ");
        String path = scan.nextLine();

        File file = new File(path);

        String str = "";



        try {
            BufferedReader buff = new BufferedReader(new FileReader(file));

            String line;
            while ((line = buff.readLine()) != null){
                str+= line + "\n";

            }
            buff.close();


                try {


                    System.out.println();
                    System.out.println("Your code's output:");

                    String sourceCode = str;

                    Interpreter interpreter = new Interpreter();
                    interpreter.interpret(sourceCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }



        }
        //if file doesnt exist in directory
        catch (Exception e) {
            System.out.println("File can't be found.");
        }

    }
}
