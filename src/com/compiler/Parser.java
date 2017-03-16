package com.compiler;

import javafx.beans.binding.BooleanExpression;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.function.Function;

public class Parser {

    public ArrayList<String> tokenList;
    String curr;
    int i = 0;

    public Parser() {

    }

    public void parse() {
        curr = tokenList.get(i);
        Program();
        if (i != tokenList.size()) {
            System.out.print("i:" + i + " total:" + tokenList.size());
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
        if (curr.equals("VOID")) {
            match("VOID");
        } else if (curr.equals("INT")) {
            match("INT");
        } else if (curr.equals("CHAR")) {
            match("CHAR");
        }
    }

    void VariableDeclaration() {
        Type();
        if (curr.equals("ID")) {
            match("ID");
            if (curr.equals(";")) {
                match(";");
            }
        }
    }

    void AssignmentStatement() {
        if (curr.equals("AS")) {
            match("AS");
            Expression();

            if (curr.equals(";")) {
                match(";");
            }
        }
    }

    void Expression() {
        Term();
        ExpressionPrime();
    }

    void ExpressionPrime() {
        if (curr.equals("+")) {
            match("+");
            Term();
            ExpressionPrime();
        } else if (curr.equals("-")) {
            match("-");
            Term();
            ExpressionPrime();
        } else ;
    }

    void Term() {
        Factor();
        TermPrime();
    }

    void Factor() {
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
    }

    void TermPrime() {
        if (curr.equals("*")) {
            match("*");
            Factor();
            TermPrime();
        } else if (curr.equals("/")) {
            match("/");
            Factor();
            TermPrime();
        } else ;
    }

    void BooleanExpression() {
        // TRUE and FALSE are just for future purposes
        // because currently there is only int, char and void types
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
    }

    void ConditionalStatement() {
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
    }

    void OptionalElse() {
        if (curr.equals("ELSE")) {
            match("ELSE");
            ExtendedCondition();
        } else ;

    }

    void ExtendedCondition() {
        if (curr.equals("IF")) {
            ConditionalStatement();
        } else if (curr.equals("{")) {
            CodeChoice();
        }
    }

    void Loop() {
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
    }

    void Input() {
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
    }

    void Output() {
        if (curr.equals("COUT")) {
            match("COUT");

            if (curr.equals("<<")) {
                match("<<");

                OutputChoices();
            }
        }
    }

    void OutputChoices() {
        Expression();
    }

    void ReturnStatement() {
        if (curr.equals("RETURN")) {
            match("RETURN");
            ReturnChoices();
            if (curr.equals(";")) {
                match(";");
            }
        }

    }

    void ReturnChoices() {
        if (curr.equals("ID") || curr.equals("LC") || curr.equals("(")) {
            Expression();
        } else ;
    }

    void FunctionCall() {
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

    }

    void ArgumentList() {
        if (curr.equals("ID")) {
            match("ID");
            AdditionalArgument();
        }
    }

    void AdditionalArgument() {
        if (curr.equals(",")) {
            match(",");
            ArgumentList();
        } else ;
    }

    void FunctionDefinition() {
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
    }

    void ParameterList() {
        Type();
        OptinalArrayType();
        if (curr.equals("ID")) {
            match("ID");
            AdditionalParameters();
        } else ;
    }

    void OptinalArrayType() {
        if (curr.equals("[")) {
            match("[");

            if (curr.equals("]")) {
                match("]");
            }
        } else ;
    }

    void AdditionalParameters() {
        if (curr.equals(",")) {
            match(",");
            Type();
            OptinalArrayType();
            if (curr.equals("ID")) {
                match("ID");
                AdditionalParameters();
            }
        } else ;
    }

    void Program() {
        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR")) {
            FunctionDefinition();
            Program();
        } else ;
    }

    void CodeChoice() {
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
    }

    void Multiline() {

        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR") ||
                curr.equals("WHILE") || curr.equals("CIN") || curr.equals("COUT") ||
                curr.equals("RETURN") || curr.equals("IF") || curr.equals("ID")) {
            SingleLine();
            Multiline();
        } else ;
    }

    void SingleLine() {
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
    }

    void IDStatement() {
        if (curr.equals("ID")) {
            match("ID");
            IDStatementChoice();
        }
    }

    void IDStatementChoice() {
        if (curr.equals("AS")) {
            AssignmentStatement();

        } else if (curr.equals("(")) {
            FunctionCall();
        }
    }
};
