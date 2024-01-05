/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.crack.the.code;

//UNUSED IMPORTS 
import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author danie
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class ClueParser {

    private static Logger logger = Logger.getLogger(ClueParser.class.getName());

    Boolean all = false;
    Boolean nothing = false;
    Util util = new Util(logger);

    String hintMessage;

   
    public Clue createClueObjectFromText(String clueString) {

        if (clueString == null || clueString.isEmpty()) {
            return null;
        }

        clueString = clueString.toLowerCase();

        //TODO: if we are writing numbers as words, we can try to assume that the digits would all be part of the combination.
        // i think if 3 digits are present written as words, then we can assume that the digits (numbers as digits) would be part of the combination


        // figure out what is the separator for the combination
        // the separator is all the characters from the first character that is not a number to the character before the next number
        boolean foundFirstNum = false;
        String combinationSeparator = "";
        for (int i = 0; i < clueString.length(); i++) {
            if (!Character.isDigit(clueString.charAt(i))) {
                combinationSeparator += clueString.charAt(i);
                System.out.println("combinationSeparatorscan: " + combinationSeparator);
            } else if (foundFirstNum) {
                break;
            } else {
                foundFirstNum = true;
            }
        }

        // check if all or nothing is present in the string
        checkIfAllOrNothing(clueString);

        if (all || nothing) {
            // if any more number 

            // check if any digts are present in the message
            String comboStrring = "";
            if (all) {
                comboStrring = clueString.substring(0, clueString.indexOf("all"));
                // set hint message - begin when the word "all" starts (from the a in all to the end of the string)
                hintMessage = clueString.substring(clueString.indexOf("all"));

            } else if (nothing) {
                comboStrring = clueString.substring(0, clueString.indexOf("nothing"));
                // set hint message - begin when the word "nothing" starts (from the n in nothing to the end of the string)
                hintMessage = clueString.substring(clueString.indexOf("nothing"));

            }

            // get all digits from the string
            ArrayList<Integer> digits = new ArrayList<Integer>();

            for (int i = 0; i < comboStrring.length(); i++) {
                if (Character.isDigit(comboStrring.charAt(i))) {
                    digits.add(Integer.parseInt(comboStrring.charAt(i) + ""));
                }
            }

            System.out.println("digits: " + digits);

            // need to implement a logic to deal with digits being present in the message that arent equal to the number of digits in the combination
            // ex - all number are correct, but 2 are well placed
            // becuse didnt implement this logic, check if there is a digit in the message that is not equal to the number of digits in the combination
            // if more than 1 digit is present in the message, we know it's wrong anyway
            if (util.getDigits(hintMessage).size() > 1) {
                throw new IllegalArgumentException("ERROR: more than 1 digit is present in the message");
            } else if (util.getDigits(hintMessage).size() == 1 && util.getDigits(hintMessage).containsKey(digits.size())) {
                // in this case, we know that the number of digits in the message is equal to the number of digits in the combination so just 
                // print some warning message
                logger.warning("Warning: the number of digits in the message is equal to the number of digits in the combination");
            }
            // print message
            System.out.println("hintMessage: " + hintMessage);

            // now if it's noghting make sure it's says nothing is correct, if so then we can just return a clue object and if not throw an error
            if (nothing) {

                if (hintMessage.equals("nothing is correct")) {
                    return new Clue.Builder()
                            .combination(digits)
                            .hintMessage(hintMessage)
                            .correctDigits(0)
                            .wellPlacedDigits(0)
                            .incorrectlyPlacedDigits(0)
                            .build();
                } else {
                    throw new IllegalArgumentException("ERROR: hint message is not nothing is correct. hint message: " + hintMessage);
                }
            }

            if (all) {

                // implemnt other cases 
                if (hintMessage.contains("all") && hintMessage.contains("correct")) {

                    // find out if well placed / correctly placed 
                    if (hintMessage.contains("well placed") || hintMessage.contains("correctly placed") && !hintMessage.contains("wrong placed") && !hintMessage.contains("incorrectly placed")) {
                        return new Clue.Builder()
                                .combination(digits)
                                .hintMessage(hintMessage)
                                .correctDigits(digits.size())
                                .wellPlacedDigits(digits.size())
                                .incorrectlyPlacedDigits(0)
                                .build();
                    } else if (hintMessage.contains("wrong placed") || hintMessage.contains("incorrectly placed") && !hintMessage.contains("well placed") && !hintMessage.contains("correctly placed")) {
                        return new Clue.Builder()
                                .combination(digits)
                                .hintMessage(hintMessage)
                                .correctDigits(digits.size())
                                .wellPlacedDigits(0)
                                .incorrectlyPlacedDigits(digits.size())
                                .build();
                    } else {
                        throw new IllegalArgumentException("ERROR: hint message is not all numbers are correct. hint message: " + hintMessage);
                    }
            }
        }

        } else {

            System.out.print("combinationSeparator: " + combinationSeparator);
            // if space is the separator, then print (space)
            if (combinationSeparator.equals(" ")) {
                System.out.println("(space)");
            } else {
                System.out.println();
            }

            Scanner scanner = new Scanner(clueString);
            scanner.useDelimiter(combinationSeparator);
            List<Integer> combination = new ArrayList<>();
            while (scanner.hasNextInt()) {
                combination.add(scanner.nextInt());

            }

            // first index of the last number of the combination
            Integer lastNum = combination.get(combination.size() - 1);
            int indexLastNumCombo = clueString.indexOf(lastNum.toString());

            String hintMessage = clueString.substring(indexLastNumCombo + lastNum.toString().length() + 1);

            // if first character of hint message is a space, remove it
            if (hintMessage.charAt(0) == ' ') {
                hintMessage = hintMessage.substring(1);
            }

            // if the first word is a number as a word then check if the indexes before have any digits and if so, add them to the combination
            String firstWord = util.getFirstWord(hintMessage);

            // print 
            System.out.println("firstWord: " + firstWord);

            if (firstWord == null) {
                throw new IllegalArgumentException("ERROR: first word of hint message is null");
            }

            if(util.isNumberWrittenAsWord(firstWord)){

                // get the index of the first word
                int indexFirstWord = hintMessage.indexOf(firstWord);

                // if not 0 
                if(indexFirstWord != 0){
                    // get the substring before the first word
                    String subStringBeforeFirstWord = hintMessage.substring(0, indexFirstWord);

                    // get all digits from the string
                    ArrayList<Integer> digits = new ArrayList<Integer>();

                    for (int i = 0; i < subStringBeforeFirstWord.length(); i++) {
                        if (Character.isDigit(subStringBeforeFirstWord.charAt(i))) {
                            digits.add(Integer.parseInt(subStringBeforeFirstWord.charAt(i) + ""));
                        }
                    }

                    // add the digits to the combination
                    combination.addAll(digits);
                }

                // still include the first word in the hint message, just remove anythign before it
                hintMessage = hintMessage.substring(indexFirstWord);

            }

                
            

            System.out.println("hintMessage: " + hintMessage);

            // now if the first character of the hint message is a not a letter or a number try reseting and using the seperator of the combination as the seperator for the hint message
            if (!Character.isLetter(hintMessage.charAt(0)) && !Character.isDigit(hintMessage.charAt(0))) {

                scanner = new Scanner(clueString);
                scanner.useDelimiter(combinationSeparator);
                combination = new ArrayList<>();
                while (scanner.hasNextInt()) {
                    combination.add(scanner.nextInt());
                }

                // first index of the last number of the combination
                lastNum = combination.get(combination.size() - 1);
                indexLastNumCombo = clueString.indexOf(lastNum.toString());

                hintMessage = clueString.substring(indexLastNumCombo + lastNum.toString().length() + 1);

                // if first character of hint message is a space, remove it
                if (hintMessage.charAt(0) == ' ') {
                    hintMessage = hintMessage.substring(1);
                }

                // now if the first character of the hint message is a not a letter or a number print an error message
                if (!Character.isLetter(hintMessage.charAt(0)) && !Character.isDigit(hintMessage.charAt(0))) {
                    throw new IllegalArgumentException("ERROR: first character of hint message is not a letter or a number");
                }
            }

            // now we will need to check if the hint message has either:
            // "all"
            // "nothing"
            // any number (words - one, two, three, four, five, six, seven, eight, nine)
            // any number (digits - 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
            // if it does, then we will use the last number of the combination as the number of correct digits (regardless of whether it is well placed or not)

            // to check if all or nothing we would first use the util class to check if the first word is all or nothing
            

             
            
            // print all or nothing
            System.out.println("all: " + all);
            System.out.println("nothing: " + nothing);

            // if both all and nothing are false, then we will check if any number (words - one, two, three, four, five, six, seven, eight, nine) appears in the hint message
            if (!(all || nothing)) {

                System.out.println("hintMessage: " + hintMessage);

                // convert all numbers as words to numbers as digits using the Util class.

                hintMessage = util.convertNumbersToDigits(hintMessage);

                /*  if the first word of the hint message is either:
                "numbers"
                 "number"
                 "digits"
                 "digit"

                 AND there is no number before it, then we will assume that the last number of the combination is actually the number of correct digits and not part of the combination
                    so we will remove it from the combination
                */

                // get the first word of the hint message
                String firstWordHintMessage = util.getFirstWord(hintMessage);
                Integer indexFirstWord = hintMessage.indexOf(firstWordHintMessage);

                if (firstWordHintMessage.equals("numbers") || firstWordHintMessage.equals("number") || firstWordHintMessage.equals("digits") || firstWordHintMessage.equals("digit")) {

                    // if the index of the first word is 0, then we will assume that the last number of the combination is actually the number of correct digits and not part of the combination
                    // so we will remove it from the combination
                    if (indexFirstWord == 0) {
                        combination.remove(combination.size() - 1);
                    } 

                    // check if there is a number anywhere BEFORE the first word and if so it's all good an no modifications for now 
                    else if (util.getDigits(hintMessage.substring(0, indexFirstWord)).size() > 0) {
                        // Question: should i throw an error or issue a warning or issue a severe warning if there is more than 1 digit before the first word?
                        // Answer(copilot): throw an error
                        throw new IllegalArgumentException("ERROR: there is more than 1 digit before the first word");

                    } 

                }
                

                    
                

                System.out.println("fixed hintMessage: " + hintMessage);





                

                //  how many times a number any number (words - one, two, three, four, five, six, seven, eight, nine) appears in the hint message
                int numDigits = 0;

                ArrayList<Integer> digitsInHintMessage = new ArrayList<Integer>();

                // split the string into words - to avoid getting numbers that are 2 digits long (like 25) and counting them as 2 digits
                String[] words = clueString.split(" ");
                for (String word : words) {
                    try {
                        Integer num = Integer.parseInt(word);
                        if (num != null) {
                            numDigits++;
                            digitsInHintMessage.add(num);
                        }
                    } catch (NumberFormatException e) {
                        // do nothing
                    }
                }

                System.out.println("numDigits: " + numDigits);
                System.out.println("digitsInHintMessage: " + digitsInHintMessage);

                // if 2 or 3 digits are present 
                if(digitsInHintMessage.size() == 2 || digitsInHintMessage.size() == 3){
                
                    
                Integer maxDigit = 0;
                for (Integer digit : digitsInHintMessage) {
                    if (digit > maxDigit) {
                        maxDigit = digit;
                    }
                }

                // if 3 numbers 
                if(digitsInHintMessage.size() == 3){
                    // make sure that the 2 other digits combined are equal to the max digit
                    Integer sumOfOtherDigits = 0;
                    for (Integer digit : digitsInHintMessage) {
                        if (digit != maxDigit) {
                            sumOfOtherDigits += digit;
                        }
                    }

                    if(sumOfOtherDigits != maxDigit){
                        throw new IllegalArgumentException("ERROR: the sum of the 2 other digits is not equal to the max digit");
                    }

                    // now just need to figuer out which digit is the number of well placed digits and which is the number of incorrectly placed digits

                    Integer correctDigits = maxDigit; // the number of correct digits - well placed and incorrectly placed (regardless of the correctness of the placement)

                    Integer wellPlacedDigits = 0;
                    Integer incorrectlyPlacedDigits = 0;

                    throw new IllegalArgumentException("ERROR: not implemented yet - need to figure out which digit is the number of well placed digits and which is the number of incorrectly placed digits");
                }

            }  else if (digitsInHintMessage.size() == 1) {
                    Integer correctDigits = digitsInHintMessage.get(0); // the number of correct digits - well placed and incorrectly placed (regardless of the correctness of the placement)
                    // check if wrong placed or well placed

                    // should create a enum the different words that can be used to describe the wrong placed and well placed digits
                    // well placed - well placed, correctly placed
                    // wrong placed - wrong placed, incorrectly placed
                    // name of enum to store both of these (both well placed and incorrectly placed) will be:



            } else if (digitsInHintMessage.size() > 3) {
                throw new IllegalArgumentException("ERROR: more than 3 digits are present in the message");
            } else {
                // error
                throw new IllegalArgumentException("ERROR: no digits are present in the message");
            }

        }

        }

        

        

        return null;

    }

    

    private void checkIfAllOrNothing(String clueString) {


        String firstWord = util.getFirstWord(clueString);

            if(firstWord == null){
                throw new IllegalArgumentException("ERROR: first word of hint message is null");
            }



            if (firstWord.equals("all")) {
                all = true;
            } else if (firstWord.equals("nothing")) {
                nothing = true;
            } else{

                // if the first word is not all or nothing, then we will check if the hint message contains all or nothing and if it does, we will throw an error
                if (clueString.contains("all") || clueString.contains("nothing")) {
                    throw new IllegalArgumentException("ERROR: hint message contains all or nothing");
                }
            }
    }



    // testing 
    public static void main(String[] args) {
        ClueParser clueParser = new ClueParser();
        
        // test case 1:
        String clueString = "9, 2, 8, 5 one number is correct but wrong placed";
        Clue clue = clueParser.createClueObjectFromText(clueString);

    }
}

 /*
     * This class will take in a string and create a clue object from it.
     * 
     * the challlenge is that the string might be in a few formats. 
     * examples of formats and what should be created from them:
     
     test case 1:
     given string: 9, 2, 8, 5 one number is correct but wrong placed
     expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(9, 2, 8, 5))
                    .hintMessage("one number is correct but wrong placed")
                    .correctDigits(1)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(1)
                    .build();

        test case 2:
        given string: 0 1 2 3 One number is correct and well placed
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(0, 1, 2, 3))
                    .hintMessage("One number is correct and well placed")
                    .correctDigits(1)
                    .wellPlacedDigits(1)
                    .incorrectlyPlacedDigits(0)
                    .build();

        test case 3:
        given string: 1 3 2 0 one number is correct but wrongly placed
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(1, 3, 2, 0))
                    .hintMessage("one number is correct but wrongly placed")
                    .correctDigits(1)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(1)
                    .build();

        test case 4:
        given string: 0 0 0 0 nothing is correct
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(0, 0, 0, 0))
                    .hintMessage("nothing is correct")
                    .correctDigits(0)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(0)
                    .build();

        test case 5:
        given string: 0 6 7 2 numbers are correct but wrong placed
        expected clue object can be created using this code (builder pattern):
        Clue clue = new Clue.Builder()
                    .combination(Arrays.asList(0, 6, 7))
                    .hintMessage("2 numbers are correct but wrong placed")
                    .correctDigits(2)
                    .wellPlacedDigits(0)
                    .incorrectlyPlacedDigits(2)
                    .build();

        
        in order to attempt and deal with probmems like test case 5, we will check and if the first word is number or numbers the digit before it will be part of the hint message



     */