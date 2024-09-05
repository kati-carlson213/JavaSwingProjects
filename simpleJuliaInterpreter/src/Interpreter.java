import java.util.*;

class Interpreter {
    private Lexer lexicalAnalyzer;
    private Parser parser;

    private Outputs outputClass;


    private HashMap<String, Integer> variables;


    public Interpreter(Outputs outputs) {
        this.lexicalAnalyzer = new Lexer();
        this.parser = new Parser();
        outputClass = outputs;

        this.variables = new HashMap<String,Integer>();
    }

    public void interpret(String sourceCode) throws Exception {
        String[] lexemes= lexicalAnalyzer.lexemeArr(sourceCode);
        ArrayList<String> tokens_list = lexicalAnalyzer.returnTokenList(lexemes);
        String[] tokens = tokens_list.toArray(new String[0]);

        boolean parsed = false;

        try { parser.parse(lexemes, tokens);
            parsed = true;
        }
        catch (Exception e) {
            String errorMessage = String.valueOf(e);
            outputClass.setOutput(errorMessage, true);
        }


        if (parsed) {
            Node root = parser.getRootNode();
            visitNode(root);
        }

    }


    private int visitNode(Node node) throws Exception {
        switch (node.data) {
            case "<program>":
                visitProgram(node);
                break;
            case "<function>": case "func_keyword":
                visitFunction(node);
                break;
            case "<block>":
                visitBlock(node);
                break;
            case "<statement>":
                visitStatement(node);
                break;
            case "<if_statement>": case "if_keyword":
                visitIfStatement(node);
                break;
            case "<then_block>": case "then_keyword":
                visitThenBlock(node);
                break;
            case "<else_block>": case "else_keyword":
                visitElseBlock(node);
                break;
            case "<while_statement>": case "while_keyword":
                visitWhileStatement(node);
            case "<assignment_statement>": case "assign_operator":
                visitAssignmentStatement(node);
                break;
            case "<repeat_statement>":
                visitRepeatStatement(node);
                break;
            case "<print_statement>": case "print_keyword":
                visitPrintStatement(node);
                break;
            case "<boolean_expression>":
                return visitBooleanExpression(node);
            case "<relative_op>":
                return visitRelativeOperator(node);
            case "<arithmetic_expression>":
                return visitArithmeticExpression(node);
            case "identifer": case "integer_literal":
                return Integer.parseInt(node.getChildren().get(0).data);
            case "end_keyword": case "<end>": case "":
                break;
            default:
                throw new Exception("Unknown node: " + node.data);
        }
        return 0;
    }


    private void visitProgram(Node node) throws Exception {
        for (Node child : node.getChildren()) {

            visitNode(child);
        }
    }

    private void visitFunction(Node node) throws Exception {

        int size = node.getChildren().size();

        if (size>0) {

            for (Node child : node.getChildren()) {

                if (!(child.data).equals("identifer") && !(child.data).equals("left_parentheses") && !(child.data).equals("right_parentheses") && !(child.data).equals("end_keyword")) {
                    visitNode(child); }

            }
        }

    }

    private void visitBlock(Node node) throws Exception {

        List <Node> childrenList = node.getChildren();


        for (int i=0; i<childrenList.size(); i++) {

            Node child = childrenList.get(i);

            visitNode(child);

        }

    }

    private void visitStatement(Node node) throws Exception {

        Node child = node.getChildren().get(0);

        visitNode(child);

    }

    private void visitIfStatement(Node node) throws Exception {

        Node booleanNode = node.getChildren().get(1);
        Node statementNode = node.getChildren().get(2);
        Node thenNode = statementNode.getChildren().get(0);
        Node elseNode = statementNode.getChildren().get(1);


        int condition = visitBooleanExpression(booleanNode);

        if (condition == 1) {
            visitThenBlock(thenNode);
        }
        else {
            visitElseBlock(elseNode);
        }
    }

    private void visitThenBlock(Node node) throws Exception {
        visitBlock(node);
    }


    private void visitElseBlock(Node node) throws Exception {
        visitBlock(node);
    }

    private void visitWhileStatement(Node node) throws Exception {
        Node child = node.getChildren().get(1); //boolean expression

        Node child2 = node.getChildren().get(3); //block


        int condition = visitBooleanExpression(child);

        while (condition == 1) {

            visitBlock(child2);

            condition = visitBooleanExpression(child);
        }


    }

    private void visitAssignmentStatement(Node node) throws Exception {
        if (node.getChildren().size()!=3) {
            return;
        }

        else {


            List <Node> childrenList = node.getChildren();
            Node child = node.getChildren().get(0);

            String identifier = (child.getChildren().get(0)).data;
            String operator = (node.getChildren().get(1)).data;



            boolean notEquals = false;

            if (operator.equals("add_assign_operator") || operator.equals("sub_assign_operator") || operator.equals("mult_assign_operator") || operator.equals("div_assign_operator") || operator.equals("mod_assign_operator")) {
                notEquals = true;
            }


            int value =0;



            Node tempNode = childrenList.get(2); //arithmetic expression



            if (tempNode.getChildren().size()==1) {

                   Node tempNode2 = tempNode.getChildren().get(0);

                   String strValue = (tempNode2.getChildren().get(0)).data;

                   if ((tempNode2.data).equals("integer_literal")) {
                       value = Integer.parseInt(strValue);
                   } else if ((tempNode2.data).equals("identifer")) {
                       value = variables.get(strValue);
                   }


                   if (notEquals) {


                       int num = 0;
                       int previousValue = variables.get(identifier);


                       Node temp = new Node("integer_literal");
                       temp.addChild(new Node(String.valueOf(previousValue)));


                       num= visitArithmeticAssign(operator, temp, tempNode2);
                       variables.put(identifier, num);

                   }

                   else {
                       variables.put(identifier, value);
                   }

            }


            else {

                if (tempNode.getChildren().size()==2) {
                    Node temp = tempNode.getChildren().get(0);
                    Node temp2 = tempNode.getChildren().get(1);


                    if (notEquals) {

                        int num;
                        Node add;

                        if (temp2.data.equals(null) ||temp2.data.equals("") ) {
                           Node temp3 = new Node("");
                           Node temp4 = temp;
                           temp4.addChild(temp.getChildren().get(0));


                            if (temp.data.equals("integer_literal")) {

                              temp3.data ="integer_literal";

                              int toAdd = variables.get(identifier);
                              String addStr = String.valueOf(toAdd);


                              temp3.addChild(new Node(addStr));


                            }
                            else if (temp.data.equals("identifer")) {

                               int get= variables.get(temp.getChildren().get(0).data);
                               String get_str = String.valueOf(get);

                               temp3.data ="identifer";
                               temp3.addChild(new Node(get_str));
                            }

                            num = visitArithmeticAssign(operator, temp3, temp4);


                            variables.put(identifier, num);

                        }

                        else {
                            num = visitArithmeticAssign(operator, temp, temp2);
                            variables.put(temp.getChildren().get(0).data, num);
                        }


                    }

                    else {
                        throw new Exception("Assignment statement error.");
                    }
                }
                else if (tempNode.getChildren().size()==3) {
                    Node temp = tempNode.getChildren().get(0);
                    Node temp2 = tempNode.getChildren().get(1);
                    Node temp3 = tempNode.getChildren().get(2);


                    int num = visitArithmeticExpression(tempNode);


                    variables.put(identifier, num);

                }

            }
        }

    }

    private int visitArithmeticAssign(String operator, Node node1, Node node2) throws Exception{
        int result=0;

        Node node3 =  node2.getChildren().get(0);
        Node node4;
        String node1_str="";
        String node2_str="";

        if (!(node2.data).equals("<arithmetic_expression>")) {
            node1_str = node1.data;
            node2_str = node2.data;
        }
        else {
            node1_str = node1.data;
            node2_str = node3.data;
        }


        int node1_int =0;
        int node2_int=0;

        if (node1_str.equals("identifer")) {
            node1_int = variables.get(node1.getChildren().get(0).data);
        }
        else if (node1_str.equals("integer_literal")) {
            node1_int = Integer.parseInt(node1.getChildren().get(0).data);
        }


        if (node2_str.equals("identifer")) {
            if ((node2.data).equals("<arithmetic_expression>")) {
                node4 =  node3.getChildren().get(0);
                node2_int = variables.get(node4.data);
            }
            else {
                node2_int = variables.get(node3.data);
            }

        }
        else if (node2_str.equals("integer_literal")) {
            if ((node2.data).equals("<arithmetic_expression>")) {
                node4 =  node3.getChildren().get(0);
                node2_int =  Integer.parseInt(node4.data);
            }
            else {

                node2_int =  Integer.parseInt(node3.data);
            }

        }




        switch (operator) {
            case "add_assign_operator":
                result = node1_int + node2_int;
                break;

            case "sub_assign_operator":
                result = node1_int - node2_int;
                break;
            case "mult_assign_operator":
                result = node1_int * node2_int;
                break;
            case "div_assign_operator":
                if (node2_int==0) {
                    throw new Exception("Divide by zero error.");
                }
                else {
                    result = node1_int / node2_int;
                    break;
                }
            case "mod_assign_operator":
                result = node1_int % node2_int;
                break;
            default: break;
        }


        return result;
    }




    private void visitRepeatStatement(Node node) throws Exception {

        int condition = visitBooleanExpression(node.getChildren().get(3));

        while (condition == 0) {
            visitBlock(node.getChildren().get(1));

            condition = visitBooleanExpression(node.getChildren().get(3));


        }
    }


    private void visitPrintStatement(Node node) throws Exception {

        String toPrint ="";
        List <Node> childrenList = node.getChildren();


        for (int i=0; i<childrenList.size(); i++) {
            Node child = childrenList.get(i);
            if ((child.data).equals("<arithmetic_expression>")) {
                Node child2 = (child.getChildren()).get(0);
                Node child3 = (child2.getChildren()).get(0);

                if ((child2.data).equals("integer_literal")) {
                    toPrint= child3.data;
                }
                else if ((child2.data).equals("identifer")) {
                    int value = variables.get(child3.data);

                    toPrint= String.valueOf(value);
                }

            }
        }

        outputClass.setOutput(toPrint, false);
        //System.out.println(toPrint);

    }


    private int visitBooleanExpression(Node node) throws Exception {

        if (node.getChildren().size()!=3) {
            return -1;
        }

        else {

            Node child1 = node.getChildren().get(0);
            Node child2 = node.getChildren().get(1);
            Node child3 = node.getChildren().get(2);



            Node relative = child1.getChildren().get(0);
            Node node1 = child2.getChildren().get(0);
            Node node2 = child3.getChildren().get(0);




            Node grandchild1 = node1.getChildren().get(0);
            Node grandchild2 = node2.getChildren().get(0);


            String left_str = grandchild1.data;
            String right_str = grandchild2.data;

            char left_chara = left_str.charAt(0);
            char right_chara = right_str.charAt(0);



            int left;
            int right;

            if((left_chara >= 'a' && left_chara <= 'z') || (left_chara >= 'A' && left_chara <= 'Z')) {

                left = variables.get(String.valueOf(left_chara));
            }
            else {
                left = Integer.parseInt(left_str);
            }


            if((right_chara >= 'a' && right_chara <= 'z') || (right_chara >= 'A' && right_chara <= 'Z')) {
                right = variables.get(String.valueOf(right_chara));
            }
            else {
                right = Integer.parseInt(right_str);
            }




            return visitRelativeOperator(relative, left, right);
        }

    }


    private int visitRelativeOperator(Node node) throws Exception {
        return visitRelativeOperator(node, visitNode(node.getChildren().get(1)), visitNode(node.getChildren().get(2)));
    }



    private int visitRelativeOperator(Node node, int left, int right) throws Exception {
        String operator = node.data;

        switch (operator) {
            case "eq_operator":
                if (left == right) {
                    return 1;
                }
                else {
                    return 0;
                }

            case "ne_operator":
                if (left != right) {
                    return 1;
                }
                else {
                    return 0;
                }

            case "gt_operator":
                if (left > right) {
                    return 1;
                }
                else {
                    return 0;
                }

            case "ge_operator":
                if (left >= right) {
                    return 1;
                }
                else {
                    return 0;
                }

            case "lt_operator":
                if (left < right) {
                    return 1;
                }
                else {
                    return 0;
                }

            case "le_operator":
                if (left <= right) {
                    return 1;
                }
                else {
                    return 0;
                }

            default:
                throw new Exception("Unknown relative operator: " + operator);
        }

    }


    private int visitArithmeticExpression(Node node) throws Exception {
        int result = 0;
        String operator = node.getChildren().get(0).data;


        Node arith1 = node.getChildren().get(1);
        Node arith2 = node.getChildren().get(2);

        Node leftNode = arith1.getChildren().get(0);
        Node rightNode = arith2.getChildren().get(0);

        String left_str = leftNode.getChildren().get(0).data;
        String right_str = rightNode.getChildren().get(0).data;


        char left_chara = left_str.charAt(0);
        char right_chara = right_str.charAt(0);


        int left;
        int right;

        if((left_chara >= 'a' && left_chara <= 'z') || (left_chara >= 'A' && left_chara <= 'Z')) {

            left = variables.get(String.valueOf(left_chara));
        }
        else {
            left = Integer.parseInt(left_str);
        }


        if((right_chara >= 'a' && right_chara <= 'z') || (right_chara >= 'A' && right_chara <= 'Z')) {
            right = variables.get(String.valueOf(right_chara));
        }
        else {
            right = Integer.parseInt(right_str);
        }





        switch (operator) {
            case "add_operator":
                result = left + right;
                break;
            case "sub_operator":
                result = left - right;
                break;
            case "mul_operator":
                result = left * right;
                break;
            case "div_operator":
                result = left / right;
                break;
            case "mod_operator":
                result = left % right;
                break;
            default:
                throw new Exception("Unknown arithmetic operator: " + operator);
        }

        return result;
    }

}
