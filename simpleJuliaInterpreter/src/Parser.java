

//Parser class
class Parser {

    private String[] tokenArr;
    private String[] lexemeArr;
    private int currentTokenIndex;
    private Node rootNode;





    public void parse(String[] lexemes, String[] tokens) throws Exception {
        this.lexemeArr = lexemes;
        this.tokenArr = tokens;


        this.currentTokenIndex = 0;
        this.rootNode = new Node("<program>");
        program(rootNode);

    }


    public Node getRootNode() {
        return rootNode;
    }


    private void program(Node parent) throws Exception {
        Node fNode = new Node("<function>");
        Node eNode = new Node("<end>");

        parent.addChild(fNode);

        match("func_keyword", fNode);
        match("identifer", fNode);
        match("left_parentheses", fNode);
        match("right_parentheses", fNode);

        if (currentTokenIndex != tokenArr.length - 1) {
            Node bNode = new Node("<block>");
            fNode.addChild(bNode);

            block(bNode);

            match("end_keyword", parent);
            parent.addChild(eNode);

            if (currentTokenIndex < tokenArr.length) {
                throw new Exception("Code outside the function is not allowed.");
            }

        } else {
            match("end_keyword", parent);
            parent.addChild(eNode);
        }
    }



    private void block(Node parent) throws Exception {


        while (!checkIndex().equals("end_keyword")) {

            Node sNode = new Node("<statement>");

            if (checkIndex().equals("else_keyword")) {
                sNode.data="";
            }

            parent.addChild(sNode);
             statement(sNode);

        }

    }


    private void statement(Node parent) throws Exception {


        String nextTokenType = checkIndex();


        if (nextTokenType.equals("if_keyword")) {
            ifStatement(parent);
        }   else if (nextTokenType.equals("then_keyword")) {
            thenStatement(parent);
        }  else if (nextTokenType.equals("else_keyword")) {
            elseStatement(parent);
        }  else if (nextTokenType.equals("identifer")) {
            assignmentStatement(parent);
        } else if (nextTokenType.equals("while_keyword")) {
            whileStatement(parent);
        } else if (nextTokenType.equals("print_keyword")) {
            printStatement(parent);
        } else if (nextTokenType.equals("repeat_keyword")) {
            repeatStatement(parent);
        }
        else if (nextTokenType.equals("until_keyword")) {
            Node block = parent.getParent();
            block.getChildren().remove(2);

            repeatStatement(parent);

        }
        else if (nextTokenType.equals("end_keyword")) {

        }
         else {
            throw new Exception("Not a valid statement.");

        }
    }




    private void ifStatement(Node parent) throws Exception{
        Node ifNode = new Node("<if_statement>");

        parent.addChild(ifNode);
        match("if_keyword", ifNode);
        booleanExpression(ifNode);
        block(ifNode);

    }


    private void thenStatement(Node parent) throws Exception{
        Node thenNode = new Node("<then_block>");


        parent.addChild(thenNode);
        match("then_keyword", thenNode);


        block(thenNode);
    }

    private void elseStatement(Node parent) throws Exception{
        Node elseNode = new Node("<else_block>");


        parent = parent.getParent();
        parent = parent.getParent();


        parent.addChild(elseNode);
        match("else_keyword", elseNode);
        block(elseNode);
        match("end_keyword", elseNode);
    }



    private void whileStatement(Node parent) throws Exception {
        Node wNode = new Node("<while_statement>");
        parent.addChild(wNode);
        match("while_keyword", wNode);
        booleanExpression(wNode);
        match("do_keyword", wNode);
        Node bNode = new Node("<block>");
        wNode.addChild(bNode);
        block(bNode);
        match("end_keyword", wNode);
    }



    private void assignmentStatement(Node parent) throws Exception {

        Node aNode = new Node("<assignment_statement>");
        parent.addChild(aNode);
        match("identifer", aNode);

        String currentToken = tokenArr[currentTokenIndex];

        switch (currentToken) {
            case "add_assign_operator":
                match ("add_assign_operator", aNode);
                arithmeticAssign(aNode);
                break;
            case "sub_assign_operator":
                match ("sub_assign_operator", aNode);
                arithmeticAssign(aNode);
                break;
            case "mult_assign_operator":
                match ("mult_assign_operator", aNode);
                arithmeticAssign(aNode);
                break;
            case "div_assign_operator":
                match ("div_assign_operator", aNode);
                arithmeticAssign(aNode);
                break;
            case "mod_assign_operator":
                match ("mod_assign_operator", aNode);
                arithmeticAssign(aNode);
                break;
            default:
                match("assign_operator", aNode);
                arithmeticExpression(aNode);
                break;
        }

    }


    private void arithmeticAssign (Node parent) throws Exception {

        Node aNode;

        String nextTokenType = checkIndex();

        if (nextTokenType.equals("identifer") || nextTokenType.equals("integer_literal")) {
            aNode = new Node("<arithmetic_expression>");
            parent.addChild(aNode);

            match(nextTokenType, aNode);
            arithmeticAssign(aNode);

        }

        else {
                Node parent2 = parent.getParent();
                Node parent3 = parent2.getParent();
                Node parent4 = parent3.getParent(); //statement
                Node parent5 = parent4.getParent(); //block


                block(parent5);

        }

    }

    private void repeatStatement(Node parent) throws Exception {
        Node rNode = new Node("<repeat_statement>");
        Node bNode = new Node("<block>");
        String nextTokenType = checkIndex();


        if (nextTokenType.equals("repeat_keyword")) {
            parent.addChild(rNode);
            match("repeat_keyword", rNode);
            rNode.addChild(bNode);

            block(bNode);
        }

        else if (nextTokenType.equals("until_keyword")) {

            Node parent2 = parent.getParent();

            match("until_keyword", parent2);
            booleanExpression(parent2);

        }

    }

    private void printStatement(Node parent) throws Exception {
        Node pNode = new Node("<print_statement>");
        parent.addChild(pNode);
        match("print_keyword", pNode);
        match("left_parentheses", pNode);
        arithmeticExpression(pNode);
        match("right_parentheses", pNode);

    }

    private void booleanExpression(Node parent) throws Exception {
        Node booNode = new Node("<boolean_expression>");
        parent.addChild(booNode);

        String nextTokenType = checkIndex();

         if(nextTokenType.equals("le_operator") ||  nextTokenType.equals("lt_operator") ||  nextTokenType.equals("ge_operator") || nextTokenType.equals("gt_operator") || nextTokenType.equals("eq_operator") ||  nextTokenType.equals("ne_operator")) {

            relativeOp(booNode);
            arithmeticExpression(booNode);
            arithmeticExpression(booNode);
        }
        else {
            throw new Exception("Unexpected boolean condition.");
        }


    }



    private void relativeOp(Node parent) throws Exception {
        Node rNode = new Node("<relative_op>");
        parent.addChild(rNode);

        String nextTokenType = checkIndex();


        if (nextTokenType.equals("le_operator") || nextTokenType.equals("lt_operator")  || nextTokenType.equals("ge_operator") || nextTokenType.equals("gt_operator")  || nextTokenType.equals("eq_operator") || nextTokenType.equals("ne_operator")){
            match(nextTokenType, parent);
            Node tempNode = new Node(nextTokenType);
            rNode.addChild(tempNode);

        }
        else {
            throw new Exception("Unexpected relative operator.");
        }
    }




    private void arithmeticExpression(Node parent) throws Exception {
        Node aNode = new Node("<arithmetic_expression>");
        parent.addChild(aNode);

        String nextTokenType = checkIndex();

        if (nextTokenType.equals("identifer") || nextTokenType.equals("integer_literal")) {
            match(nextTokenType, aNode);

        }
        else if (nextTokenType.equals("add_operator") || nextTokenType.equals("sub_operator") || nextTokenType.equals("mul_operator") || nextTokenType.equals("div_operator") || nextTokenType.equals("mod_operator"))  {
            match(nextTokenType, aNode);
            arithmeticExpression(aNode);
            arithmeticExpression(aNode);

        }
        else {
            throw new Exception("Unexpected arithmetic expression");
        }
    }



    private String match(String expectedType, Node parent) throws Exception {
        String currentToken = tokenArr[currentTokenIndex];


        if (!currentToken.equals(expectedType)) {
            throw new Exception("Token mismatch.");
        }



        Node tokenNode = new Node(currentToken);

        if (!(tokenNode.data).equals("le_operator") && !(tokenNode.data).equals("lt_operator") && !(tokenNode.data).equals("ge_operator") && !(tokenNode.data).equals("gt_operator") && !(tokenNode.data).equals("eq_operator") && !(tokenNode.data).equals("ne_operator")) {
            parent.addChild(tokenNode);
        }



        switch (currentToken) {
            case "identifer": case "integer_literal":

                String currentLexeme =lexemeArr[currentTokenIndex];
                Node lexemeNode = new Node (currentLexeme);
                tokenNode.addChild(lexemeNode);
                break;



            default:
                break;
        }



        tokenNode.setToken(currentToken);

        currentTokenIndex++;
        return currentToken;
    }


    private String checkIndex() throws Exception {
        String return_str = "";


        if (currentTokenIndex < tokenArr.length) {

            return_str = tokenArr[currentTokenIndex];


        } else {
            return_str = null;

            throw new Exception("Unexpected end of input.");
        }

        return return_str;
    }

    public void returnTree() {
        Tree tree = new Tree(rootNode);
        tree.printTree();
    }

}
