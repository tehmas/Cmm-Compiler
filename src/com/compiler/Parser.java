package com.compiler;

import javafx.beans.binding.BooleanExpression;

import java.beans.Expression;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class Parser {

    FileOutputStream treeFile;
    public ArrayList<String> tokenList;
    String curr;
    int i = 0;
    private int spaceCount = 0;
    public Parser() {


    }

    public void parse() {
        try{
            treeFile = new FileOutputStream("parse_tree.txt");
            curr = tokenList.get(i);
            Program();
            if (i != tokenList.size()) {
                System.out.print("i:" + i + " total:" + tokenList.size());
            }
            treeFile.close();
        }
        catch (Exception ex)
        {
            System.out.print(ex);
        }
    }

    void match(String token) {
        if (curr.equals(token)) {
            i += 1;
            if (i < tokenList.size())
                curr = tokenList.get(i);
        } else {
            System.out.print("Error!");
        }
    }

    void Type() {
        spaceCount++;
        PrintBegin("TYPE");
        if (curr.equals("VOID")) {
            match("VOID");
        } else if (curr.equals("INT")) {
            match("INT");
        } else if (curr.equals("CHAR")) {
            match("CHAR");
        }
        spaceCount--;
    }

    void VariableDeclaration() {
        spaceCount++;
        PrintBegin("VARIABLEDECLARATION");
        Type();
        if (curr.equals("ID")) {
            match("ID");
            if (curr.equals(";")) {
                match(";");
            }
        }
        spaceCount--;
    }

    void AssignmentStatement() {
        spaceCount++;
        PrintBegin("ASSIGNMENTSTATEMENT");
        if (curr.equals("AS")) {
            match("AS");
            Expression();

            if (curr.equals(";")) {
                match(";");
            }
        }
        spaceCount--;
    }

    void Expression() {
        spaceCount++;
        PrintBegin("EXPRESSION");
        Term();
        ExpressionPrime();
        spaceCount--;
    }

    void ExpressionPrime() {
        spaceCount++;
        PrintBegin("EXPRESSIONPRIME");
        if (curr.equals("+")) {
            match("+");
            Term();
            ExpressionPrime();
        } else if (curr.equals("-")) {
            match("-");
            Term();
            ExpressionPrime();
        } else ;
        spaceCount--;
    }

    void Term() {
        spaceCount++;
        PrintBegin("TERM");
        Factor();
        TermPrime();
        spaceCount--;
    }

    void Factor() {
        spaceCount++;
        if (curr.equals("ID")) {
            match("ID");
        } else if (curr.equals("NC")) {
            match("NC");
        } else if (curr.equals("(")) {
            match("(");
            Expression();
            if (curr.equals(")")) {
                match(")");
            }
        }
        spaceCount--;
    }

    void TermPrime() {
        spaceCount++;
        PrintBegin("TERMPRIME");
        if (curr.equals("*")) {
            match("*");
            Factor();
            TermPrime();
        } else if (curr.equals("/")) {
            match("/");
            Factor();
            TermPrime();
        } else ;
        spaceCount--;
    }

    void BooleanExpression() {
        // TRUE and FALSE are just for future purposes
        // because currently there is only int, char and void types
        spaceCount++;
        PrintBegin("BOOLEANEXPRESSION");
        if (curr.equals("TRUE")) {
            match("TRUE");
        } else if (curr.equals("FALSE")) {
            match("FALSE");
        } else {
            Expression();
            if (curr.equals("RO")) {
                match("RO");
                Expression();
            }
        }
        spaceCount--;
    }

    void ConditionalStatement() {
        spaceCount++;
        PrintBegin("CONDITIONALSTATEMENT");
        if (curr.equals("IF")) {
            match("IF");
            if (curr.equals("(")) {
                match("(");
                BooleanExpression();
                if (curr.equals(")")) {
                    match(")");
                    CodeChoice();
                    OptionalElse();
                }
            }
        }
        spaceCount--;
    }

    void OptionalElse() {
        spaceCount++;
        PrintBegin("OPTIONALELSE");
        if (curr.equals("ELSE")) {
            match("ELSE");
            ExtendedCondition();
        } else ;
        spaceCount--;
    }

    void ExtendedCondition() {
        spaceCount++;
        PrintBegin("EXTENDEDCONDITION");
        if (curr.equals("IF")) {
            ConditionalStatement();
        } else if (curr.equals("{")) {
            CodeChoice();
        }
        spaceCount--;
    }

    void Loop() {
        spaceCount++;
        PrintBegin("LOOP");
        if (curr.equals("WHILE")) {
            match("WHILE");
            if (curr.equals("(")) {
                match("(");
                BooleanExpression();
                if (curr.equals(")")) {
                    match(")");
                    CodeChoice();
                }
            }
        }
        spaceCount--;
    }

    void Input() {
        spaceCount++;
        PrintBegin("INPUT");
        if (curr.equals("CIN")) {
            match("CIN");
            if (curr.equals(">>")) {
                match(">>");
                if (curr.equals("ID")) {
                    match("ID");

                    if (curr.equals(";")) {
                        match(";");
                    }
                }
            }
        }
        spaceCount--;
    }

    void Output() {
        spaceCount++;
        PrintBegin("OUTPUT");
        if (curr.equals("COUT")) {
            match("COUT");

            if (curr.equals("<<")) {
                match("<<");

                OutputChoices();
            }
        }
        spaceCount--;
    }

    void OutputChoices() {
        spaceCount++;
        PrintBegin("OutputChoices");
        Expression();
        spaceCount--;
    }

    void ReturnStatement() {
        spaceCount++;
        PrintBegin("RETURNSTATEMENT");
        if (curr.equals("RETURN")) {
            match("RETURN");
            ReturnChoices();
            if (curr.equals(";")) {
                match(";");
            }
        }
        spaceCount--;

    }

    void ReturnChoices() {
        spaceCount++;
        PrintBegin("RETURNCHOICES");
        if (curr.equals("ID") || curr.equals("LC") || curr.equals("(")) {
            Expression();
        } else ;
        spaceCount--;
    }

    void FunctionCall() {
        spaceCount++;
        PrintBegin("FUNCTIONCALL");
        if (curr.equals("(")) {
            match("(");
            ArgumentList();
            if (curr.equals(")")) {
                match(")");

                if (curr.equals(";")) {
                    match(";");
                }
            }
        }
        spaceCount--;
    }

    void ArgumentList() {
        spaceCount++;
        PrintBegin("ARGUMENTLIST");
        if (curr.equals("ID")) {
            match("ID");
            AdditionalArgument();
        }
        spaceCount--;
    }

    void AdditionalArgument() {
        spaceCount++;
        PrintBegin("ADDITIONALARGUMENT");
        if (curr.equals(",")) {
            match(",");
            ArgumentList();
        } else ;

        spaceCount--;
    }

    void FunctionDefinition() {
        spaceCount++;
        PrintBegin("FUNCTIONDEFINITION");
        Type();
        if (curr.equals("ID")) {
            match("ID");

            if (curr.equals("(")) {
                match("(");
                ParameterList();

                if (curr.equals(")")) {
                    match(")");
                    if (curr.equals("{")) {
                        match("{");
                        Multiline();

                        if (curr.equals("}")) {
                            match("}");
                        }
                    }
                }
            }
        }

        spaceCount--;
    }

    void ParameterList() {
        spaceCount++;
        PrintBegin("ParameterList");
        Type();
        OptinalArrayType();
        if (curr.equals("ID")) {
            match("ID");
            AdditionalParameters();
        } else ;
        spaceCount--;
    }

    void OptinalArrayType() {
        spaceCount++;
        PrintBegin("OPTIONALARRAYTYPE");
        if (curr.equals("[")) {
            match("[");

            if (curr.equals("]")) {
                match("]");
            }
        } else ;
        spaceCount--;
    }

    void AdditionalParameters() {
        spaceCount++;
        PrintBegin("ADDITIONALPARAMETERS");
        if (curr.equals(",")) {
            match(",");
            Type();
            OptinalArrayType();
            if (curr.equals("ID")) {
                match("ID");
                AdditionalParameters();
            }
        } else ;

        spaceCount--;
    }


    void PrintBegin(String functionName){
        try{
            String spaces = "      ";
            String bars = "____";
            String endLine = "\n";
            for(int j = 0; j < spaceCount; j++)
            {
                treeFile.write(spaces.getBytes());
            }
            treeFile.write(bars.getBytes());
            treeFile.write(functionName.getBytes());
            treeFile.write(endLine.getBytes());
        }

        catch(Exception ex)
        {
            System.out.print(ex);
        }
    }

    void Program() {
        spaceCount++;
        PrintBegin("PROGRAM");
        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR")) {
            FunctionDefinition();
            Program();
       }
        else ;
        spaceCount--;
    }

    void CodeChoice() {
        spaceCount++;
        PrintBegin("CODECHOICE");
        if (curr.equals("{")) {
            match("{");
            Multiline();

            if (curr.equals("}")) {
                match("}");
            }
        } else if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR") ||
                curr.equals("WHILE") || curr.equals("CIN") || curr.equals("COUT") ||
                curr.equals("RETURN") || curr.equals("IF") || curr.equals("ID")) {
            SingleLine();
        }
        spaceCount--;
    }

    void Multiline() {
        spaceCount++;
        PrintBegin("MULTILINE");
        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR") ||
                curr.equals("WHILE") || curr.equals("CIN") || curr.equals("COUT") ||
                curr.equals("RETURN") || curr.equals("IF") || curr.equals("ID")) {
            SingleLine();
            Multiline();
        } else ;
        spaceCount--;
    }

    void SingleLine() {
        spaceCount++;
        PrintBegin("SINGLELINE");
        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR")) {
            VariableDeclaration();
        } else if (curr.equals("WHILE")) {
            Loop();
        } else if (curr.equals("CIN")) {
            Input();
        } else if (curr.equals("COUT")) {
            Output();
        } else if (curr.equals("RETURN")) {
            ReturnStatement();
        } else if (curr.equals("IF")) {
            ConditionalStatement();
        } else if (curr.equals("ID")) {
            IDStatement();
        }
        spaceCount--;
    }

    void IDStatement() {
        spaceCount++;
        PrintBegin("IDSTATEMENT");
        if (curr.equals("ID")) {
            match("ID");
            IDStatementChoice();
        }
        spaceCount--;
    }

    void IDStatementChoice() {
        spaceCount++;
        if (curr.equals("AS")) {
            AssignmentStatement();

        } else if (curr.equals("(")) {
            FunctionCall();
        }
        spaceCount--;
    }
};
