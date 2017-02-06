package com.compiler;

public class TokenLexeme {
    public String token;
    public String lexeme;
    public Boolean valid;

    public TokenLexeme(Boolean vaild, String token, String lexeme) {
        this.token = token;
        this.lexeme = lexeme;
        this.valid = valid;
    }
}