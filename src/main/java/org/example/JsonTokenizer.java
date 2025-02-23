package org.example;

import java.util.ArrayList;
import java.util.List;

public class JsonTokenizer {
    public static List<Token> tokenize(String jsonStr){
        int index = 0;
        List<Token> tokenList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        while(index < jsonStr.length()){
            char currentChar = jsonStr.charAt(index);
            switch(currentChar){
                case '['-> {
                    tokenList.add(new Token(String.valueOf(currentChar), TokenType.OPEN_SQUARE_BRACKET));
                    index++;
                }
                case ']'-> {
                    tokenList.add(new Token(String.valueOf(currentChar), TokenType.CLOSE_SQUARE_BRACKET));
                    index++;
                }
                case ',' -> {
                    tokenList.add(new Token(String.valueOf(currentChar), TokenType.COMMA));
                    index++;
                }
                case ':' -> {
                    tokenList.add(new Token(String.valueOf(currentChar), TokenType.COLON));
                    index++;
                }
                case '{' -> {
                    tokenList.add(new Token(String.valueOf(currentChar), TokenType.OPEN_BRACKET));
                    index++;
                }
                case '}' -> {
                    tokenList.add(new Token(String.valueOf(currentChar), TokenType.CLOSE_BRACKET));
                    index++;
                }
                case ' ', '\n', '\r', '\t' -> index++;
                case '"' -> {
                    index++;
                    stringBuilder.setLength(0);
                    while(index < jsonStr.length() && jsonStr.charAt(index) != '"'){
                        stringBuilder.append(jsonStr.charAt(index));
                        index++;
                    }
                    if(index < jsonStr.length()){
                        tokenList.add(new Token(stringBuilder.toString(), TokenType.STRING));
                        index++;
                    } else {
                        throw new RuntimeException("Unterminated string"); // Handle error
                    }
                }
                default -> {
                    //number or boolean or null
                    if(Character.isDigit(currentChar) || currentChar == '-'){
                        stringBuilder.setLength(0);
                        while (index < jsonStr.length() && (Character.isDigit(jsonStr.charAt(index)) || jsonStr.charAt(index) == '.'  || jsonStr.charAt(index) == '-')){
                            stringBuilder.append(jsonStr.charAt(index));
                            index++;
                        }
                        try{
                            Double.parseDouble(stringBuilder.toString());
                        } catch (NumberFormatException e){
                            throw new RuntimeException("Invalid number format.");
                        }
                        tokenList.add(new Token(stringBuilder.toString(), TokenType.NUMBER));
                    } else if(Character.isLetter(currentChar)){
                        stringBuilder.setLength(0);
                        while (index < jsonStr.length() && Character.isLetter(jsonStr.charAt(index))){
                            stringBuilder.append(jsonStr.charAt(index));
                            index++;
                        }
                        String keywordStr = stringBuilder.toString();
                        if(keywordStr.equals("true") || keywordStr.equals("false")){
                            tokenList.add(new Token(keywordStr, TokenType.BOOLEAN));
                        } else if(keywordStr.equals("null")){
                            tokenList.add(new Token(keywordStr, TokenType.NULL));
                        } else {
                            throw new RuntimeException("Invalid keyword: " + keywordStr);
                        }
                    } else {
                        throw new RuntimeException("Unexpected character: " + currentChar);
                    }
                }
            }
        }
        return tokenList;
    }
}
