package com.compiler;

public class TokenLexeme {
    public String token;
    public String lexeme;
    public boolean valid;

    public TokenLexeme(boolean valid, String token, String lexeme) {
        this.token = token;
        this.lexeme = lexeme;
        this.valid = valid;
    }
}