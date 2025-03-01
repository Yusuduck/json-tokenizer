package org.example;

import org.example.example.Person;

import java.util.List;

public class Main {

    private static String simpleJsonStr = """
            {
            "name" : "Bob",
            "age" : 23,
            "country" : {
                    "id" : 1,
                    "name" : "France"
                },
            "roles" : [
                "editor",
                "admin"
            ]
            }
            """;
    public static void main(String[] args) {
        System.out.println("Start tokenize");
        List<Token> tokenList = JsonTokenizer.tokenize(simpleJsonStr);
        JsonParser parser = new JsonParser(tokenList);
        Object parsedJson = parser.parse();
//        Person person = (Person) parsedJson;
        Person person = parser.parsePerson();
        System.out.println("end of program");
    }
}