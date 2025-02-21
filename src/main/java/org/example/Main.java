package org.example;

import java.util.List;

public class Main {

    private static String simpleJsonStr = """
            {
            "name" : "Bob", 
            "age" : 23
            }
            """;
    public static void main(String[] args) {
        System.out.println("Start tokenize");
        List<Token> tokenList = JsonParser.tokenize(simpleJsonStr);
        System.out.println("end of program");
    }
}