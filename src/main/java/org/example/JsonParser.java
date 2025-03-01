package org.example;

import org.example.example.Country;
import org.example.example.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private List<Token> tokens;
    private int currentTokenIndex;

    public JsonParser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public Object parse(){
        if(tokens.isEmpty()){
            return null;
        }
        return parseValue();
    }

    private Object parseValue(){
        if(currentTokenIndex >= tokens.size()){
            return null;
        }

        Token currentToken = tokens.get(currentTokenIndex);

        switch (currentToken.type){
            case STRING -> {
                currentTokenIndex++;
                return currentToken.value;
            }
            case NUMBER -> {
                currentTokenIndex++;
                return Double.parseDouble(currentToken.value);
            }
            case BOOLEAN -> {
                currentTokenIndex++;
                return Boolean.parseBoolean(currentToken.value);
            }
            case NULL -> {
                currentTokenIndex++;
                return null;
            }
            case OPEN_BRACKET -> {
                return parseObject();
            }
            case OPEN_SQUARE_BRACKET -> {
                return parseArray();
            }
            default -> {
                throw new RuntimeException("Unexpected token: " + currentToken.value);
            }
        }
    }

    private Object parseObject(){
        currentTokenIndex++; //Consume open bracket
        Map<String, Object> object = new HashMap<>();
        if (currentTokenIndex >= tokens.size() || tokens.get(currentTokenIndex).type == TokenType.CLOSE_BRACKET){
            currentTokenIndex++; // Consume close bracket
            return object;
        }

        while(true){
            String key = tokens.get(currentTokenIndex).value;
            currentTokenIndex++;
            if(tokens.get(currentTokenIndex).type == TokenType.COLON){
                currentTokenIndex++; // Consume colon
            }
             Object value = parseValue();
            object.put(key, value);
            if(currentTokenIndex >= tokens.size() || tokens.get(currentTokenIndex).type == TokenType.CLOSE_BRACKET){
                currentTokenIndex++; // Consume close bracket
                break;
            }
            if (currentTokenIndex >= tokens.size() || tokens.get(currentTokenIndex).type != TokenType.COMMA) {
                throw new RuntimeException("Expected comma or close bracket in object");
            }
            currentTokenIndex++;
        }
        return object;
    }

    private Object parseArray(){
        currentTokenIndex++; //Consume open square bracket
        List<Object> array =  new ArrayList<Object>();

        if (currentTokenIndex >= tokens.size() || tokens.get(currentTokenIndex).type == TokenType.CLOSE_SQUARE_BRACKET) {
            currentTokenIndex++; // Consume CLOSE_SQUARE_BRACKET (empty array)
            return array;
        }

        while(true){
            Object value = parseValue();
            array.add(value);

            if (currentTokenIndex >= tokens.size() || tokens.get(currentTokenIndex).type == TokenType.CLOSE_SQUARE_BRACKET) {
                currentTokenIndex++; // Consume CLOSE_SQUARE_BRACKET
                break;
            }

            if (currentTokenIndex >= tokens.size() || tokens.get(currentTokenIndex).type != TokenType.COMMA) {
                throw new RuntimeException("Expected comma or close square bracket in array");
            }
            currentTokenIndex++;
        }
        return array;
    }

    public Person parsePerson(){
Person person = new Person();
setPersonValue(person);
return person;
    }

    private void setPersonValue(Person person){
        if(currentTokenIndex >= tokens.size()){
            return;
        }

        while(currentTokenIndex < tokens.size()-1){
            if(tokens.get(currentTokenIndex).type == TokenType.OPEN_BRACKET){
                currentTokenIndex++;
            }
            if(tokens.get(currentTokenIndex).type == TokenType.COMMA){
                currentTokenIndex++;
            }
            switch (tokens.get(currentTokenIndex).value){
                case "name" -> {
                    currentTokenIndex++;
                    if(tokens.get(currentTokenIndex).type != TokenType.COLON){
                        throw new RuntimeException("no colon");
                    }
                    currentTokenIndex++;
                    person.name = tokens.get(currentTokenIndex).value;
                    currentTokenIndex++;
                }
                case "age" -> {
                    currentTokenIndex++;
                    if(tokens.get(currentTokenIndex).type != TokenType.COLON){
                        throw new RuntimeException("no colon");
                    }
                    currentTokenIndex++;
                    person.age = Integer.parseInt(tokens.get(currentTokenIndex).value);
                    currentTokenIndex++;
                }
                case "roles" -> {
                    parseRoles(person);
                }
                case "country" -> {
                    parseCountry(person);
                }
                default -> {
                    System.out.println("lalala");
                    throw new RuntimeException("Unexpected token: " + tokens.get(currentTokenIndex).value);
                }
            }
        }
    }
    private void parseCountry(Person person){
        currentTokenIndex++;
        if(tokens.get(currentTokenIndex).type == TokenType.COLON){
            currentTokenIndex++;
        }
        if(tokens.get(currentTokenIndex).type == TokenType.NULL){
            person.country = null;
        } else if(tokens.get(currentTokenIndex).type == TokenType.OPEN_BRACKET) {
            currentTokenIndex++;
            Country country = new Country();
            while (true){
                switch (tokens.get(currentTokenIndex).value){
                    case "name" -> {
                        currentTokenIndex++;
                        if(tokens.get(currentTokenIndex).type == TokenType.COLON){
                            currentTokenIndex++;
                        }
                        country.name = tokens.get(currentTokenIndex).value;
                        currentTokenIndex++;
                    }
                    case "id" -> {
                        currentTokenIndex++;
                        if(tokens.get(currentTokenIndex).type == TokenType.COLON){
                            currentTokenIndex++;
                        }
                        country.id = Integer.parseInt(tokens.get(currentTokenIndex).value);
                        currentTokenIndex++;
                    }
                }
                if(tokens.get(currentTokenIndex).type == TokenType.COMMA){
                    currentTokenIndex++;
                } else if(tokens.get(currentTokenIndex).type == TokenType.CLOSE_BRACKET){
                    currentTokenIndex++; //Consume close bracket
                    break;
                }
            }
            person.country = country;
        }
    }

    private void parseRoles(Person person){
        currentTokenIndex++;
        if(tokens.get(currentTokenIndex).type == TokenType.COLON){
            currentTokenIndex++;
        }
        if(tokens.get(currentTokenIndex).type == TokenType.NULL){
            person.roles = null;
        }else if(tokens.get(currentTokenIndex).type == TokenType.OPEN_SQUARE_BRACKET) {
            currentTokenIndex++; // Consume Open square bracket
            List<String> roles = new ArrayList<>();
            while(true){
                if(tokens.get(currentTokenIndex).type == TokenType.STRING){
                    roles.add(tokens.get(currentTokenIndex).value);
                    currentTokenIndex++;
                }
                if(tokens.get(currentTokenIndex).type == TokenType.COMMA){
                    currentTokenIndex++;
                } else if(tokens.get(currentTokenIndex).type == TokenType.CLOSE_SQUARE_BRACKET){
                    currentTokenIndex++;
                    break;
                }
            }
            person.roles = roles;
        }




    }
}
