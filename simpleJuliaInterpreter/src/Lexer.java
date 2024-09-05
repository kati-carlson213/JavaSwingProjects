import java.util.*;

class Lexer {


    //method for when tokens are analyzed -- takes in a string to see if its a number/digit
    public static boolean isNumber (String str) {
        boolean number = false;
        if (str.matches("^-?\\d+$")) {
            number=true;

        }
        else {
            number=false;
        }

        return number;

    }


    //if a string is like '(x)', this puts in whitespace and separates the parentheses from the identifier so it goes '( x )'
    public static String separateParentheses(String str) {
        boolean parenthesesTrue = false;

        String result ="";

        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i)=='(') {
                parenthesesTrue= true;
            }
        }

        if (parenthesesTrue) {
            for (int j=0; j<str.length(); j++) {
                char character = str.charAt(j);
                if (character=='(') {
                    result += " ( ";
                }
                else if (character==')') {
                    result += " ) ";
                }
                else {
                    result+=character;
                }
            }
        }

        else {
            result=str;
        }

        return result;
    }



    //puts whitespace between operator and identifier so they are processed as separate lexemes
    public static String separateOperator(String str) {
        boolean operatorTrue = false;

        String result ="";

        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i)=='=' || str.charAt(i)=='!' || str.charAt(i)=='<' || str.charAt(i)=='>' || str.charAt(i)=='~' || str.charAt(i)=='+' || str.charAt(i)=='-' || str.charAt(i)=='*' || str.charAt(i)=='/') {
                operatorTrue= true;
            }
        }


        String operator ="";
        String id = "";

        if (operatorTrue) {
            int variableIndex=0;

            for (int j=0; j<str.length(); j++) {

                char character = str.charAt(j);

                if ((character >= '0' & character <= '9') || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                    id+= character;
                }

                else if (character=='=' || character=='!' || character=='<' || character=='>' || character=='~' || character=='+' || character=='-' || character=='*' || character=='/') {
                    operator+=character;
                }
            }

            if (str.charAt(0)=='=' || str.charAt(0)=='!' || str.charAt(0)=='<' || str.charAt(0)=='>' || str.charAt(0)=='~' || str.charAt(0)=='+' || str.charAt(0)=='-' || str.charAt(0)=='*' || str.charAt(0)=='/') {
                result= operator + " " + id;
            }
            else {
                result = id + " " + operator;
            }
        }

        else {
            result=str;
        }

        return result;
    }



    //retrieves the lexemes in the form of an array
    public static String[] lexemeArr(String str){

        ArrayList <String> withComments = new ArrayList <String> ();



        for (String line:str.split("\n")) {
            withComments.add(line);
        }

        ArrayList <String> withoutComments = new ArrayList<String> ();


        //makes an arraylist without the comments in it--> the comments are ignored
        for (int x=0; x<withComments.size(); x++) {
            String currentLine = withComments.get(x);

            currentLine = currentLine.trim();

            if (currentLine.length()>0 && currentLine.charAt(0)=='/' && currentLine.charAt(1)=='/') {}
            else if (currentLine.contains("//")) {
                String addLine = "";
                int index = currentLine.indexOf("//");
                addLine = currentLine.substring(0, index);
                withoutComments.add(addLine);
            }
            else {
                withoutComments.add(currentLine);

            }
        }



        str="";

        for (int z = 0; z<withoutComments.size(); z++) {
            str += withoutComments.get(z) + "\n";


        }


        ArrayList<String> list = new ArrayList<String>();

        //split separates lexemes by whitespace
        String[] arr = str.split("\\s+");

        ArrayList<String> arr_list = new ArrayList<String>();

        for (int a=0; a< arr.length; a++) {
            if (arr[a]!= ""){
                arr_list.add(arr[a]);
            }
        }


        String[] arr2;
        String[] arr3;
        String[] result;


        String separated = "";


        for (int i =0; i<arr_list.size(); i++) {
            separated= separateParentheses(arr_list.get(i));
            arr2= separated.split("\\s+");

            for (int j = 0; j<arr2.length; j++) {
                separated= separateOperator(arr2[j]);
                arr3= separated.split("\\s+");

                for (int k = 0; k<arr3.length; k++) {
                    list.add(arr3[k]);
                }
            }

        }

        result = list.toArray(new String[0]);


        return result;



    }


    //returns what token a lexeme is
    public static String returnToken(String input) {

        String tokenType="";

        boolean variableLetterTrue=false;
        boolean digitTrue =false;
        boolean numberTrue = isNumber(input);

        if (input.length()==1) {

            char character = input.charAt(0);

            if((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                variableLetterTrue=true; }
            else if (Character.isDigit(character)) {
                digitTrue=true;
            }

        }

        switch (input) {
            case "+":
                tokenType= "add_operator";
                break;
            case "-":
                tokenType= "minus_operator";
                break;
            case "*":
                tokenType= "mul_operator";
                break;
            case "/":
                tokenType= "div_operator";
                break;
            case "%":
                tokenType= "mod_operator";
                break;
            case "=":
                tokenType= "assign_operator";
                break;
            case "==":
                tokenType= "eq_operator";
                break;
            case "~=":
                tokenType= "ne_operator";
                break;
            case ">":
                tokenType= "gt_operator";
                break;
            case ">=":
                tokenType= "ge_operator";
                break;
            case "<":
                tokenType= "lt_operator";
                break;
            case "<=":
                tokenType= "le_operator";
                break;
            case "+=":
                tokenType= "add_assign_operator";
                break;
            case "-=":
                tokenType= "sub_assign_operator";
                break;
            case "*=":
                tokenType= "mult_assign_operator";
                break;
            case "/=":
                tokenType= "div_assign_operator";
                break;
            case "%=":
                tokenType= "mod_assign_operator";
                break;
            case "function":
                tokenType= "func_keyword";
                break;
            case "print":
                tokenType= "print_keyword";
                break;
            case "if":
                tokenType= "if_keyword";
                break;
            case "then":
                tokenType= "then_keyword";
                break;
            case "end":
                tokenType= "end_keyword";
                break;
            case "while":
                tokenType= "while_keyword";
                break;
            case "do":
                tokenType= "do_keyword";
                break;
            case "repeat":
                tokenType= "repeat_keyword";
                break;
            case "until":
                tokenType= "until_keyword";
                break;
            case "else":
                tokenType = "else_keyword";
                break;
            case "(":
                tokenType= "left_parentheses";
                break;
            case ")":
                tokenType= "right_parentheses";
                break;


            default:
                if (variableLetterTrue) {
                    tokenType= "identifer";
                }
                else if(digitTrue || numberTrue) {
                    tokenType= "integer_literal";
                }
                else {
                    tokenType= "unspecified_token";
                }
                break;
        }

        return tokenType;


    }


    //takes in the lexeme array and returns an array of each lexeme's token
    public static ArrayList<String> returnTokenList(String[] arr) {
        ArrayList<String> result = new ArrayList<String>();

        for (int i=0; i<arr.length; i++) {
            result.add(returnToken(arr[i]));
        }

        return result;
    }

}