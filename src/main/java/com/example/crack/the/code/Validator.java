/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

/**
 *
 * @author carmitnaor
 */


import java.util.*;


public class Validator {

    /*
     * this class will do all sorts of validation for the game including 
     * checking if the combination is valid based on the clues
     * checking if part of the combination is valid based on the clues
     * checking if a clue is valid considering the code (for generating the clues for the riddle generator)
     * 
     *  
    */

    // instance variables
    private Integer[] code;
    private List<Clue> clues;
    private ArrayList<Integer[]> invalidCombinations;
    private boolean debug = false;

    // store the debug messages 
    public ArrayList<String> debugMessages = new ArrayList<String>();


    // empty constructor
    public Validator() {



    }

    // constructor
    public Validator(Integer[] code, List<Clue> clues) {
        this.code = code;
        this.clues = clues;

        invalidCombinations = new ArrayList<Integer[]>();
    }

    public Validator(Integer[] code, List<Clue> clues, ArrayList<Integer[]> invalidCombinations){
        this.code = code;
        this.clues = clues;
        this.invalidCombinations = invalidCombinations;
        }

    // builder pattern
    public Validator(Builder builder) {
        this.code = builder.code;
        this.clues = builder.clues;
        this.invalidCombinations = builder.invalidCombinations;
        this.debug = builder.debug;
    }



    // builder pattern
    public static class Builder {
        private Integer[] code;
        private List<Clue> clues;
        private ArrayList<Integer[]> invalidCombinations = new ArrayList<Integer[]>();
        private boolean debug = false;

        public Builder code(Integer[] code) {
            this.code = code;
            return this;
        }

        public Builder clues(List<Clue> clues) {
            this.clues = clues;
            return this;
        }

        public Builder invalidCombinations(ArrayList<Integer[]> invalidCombinations) {
            this.invalidCombinations = invalidCombinations;
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Validator build() {
            return new Validator(this);
        }
    }

    // check if the combination is valid with all the clues - this would include the placement of the numbers
    public boolean checkIfCombinationValid(Integer[] combination) {
        for (Clue clue : clues) {
            if (!checkIfComboValid(combination, clue)) {
                return false;
            }
        }

        return true;
    }


    // check if the combination is valid with all the clues - the placement of the numbers does not matter
    public boolean checkIfCombinationSatisfiesAllClues(Integer[] combination, boolean sutifiesPartially)  { 

        for (Clue clue : clues) {            

            int numCorrectDigits = 0;
            for (int i = 0; i < clue.getCombination().size(); i++) {
                    // only check if the number is correct we dont care if it is well placed or not
                    if (clue.getCombination().get(i) != null && Arrays.asList(combination).contains(clue.getCombination().get(i))) {
                        numCorrectDigits++;
                    }
            }

            if (numCorrectDigits != clue.getCorrectDigits() && !sutifiesPartially) {
                
                // print just the combo, the combination of clue and the number of correct digits and how the clue needs to have
                if(debug)
                    System.out.println("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigits: " + numCorrectDigits + " correctDigits: " + clue.getCorrectDigits());
                                    
                debugMessages.add("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigits: " + numCorrectDigits + " correctDigits: " + clue.getCorrectDigits());

                    return false;
            } 

            // even if partically satisfies the clue, if the code length - the length of the combination(which equals the numbers that have to be right and we just dont know which numbers from the clue are right but we know how many have to be right) is less than the number of correct digits then it is not a valid combination
            if (code.length - combination.length < clue.getCorrectDigits() - numCorrectDigits) {
                // print the code length - the length of the combination(which equals the numbers that have to be right and we just dont know which numbers from the clue are right but we know how many have to be right) and the number of correct digits
                if(debug)
                    System.out.println("code length is " + code.length + " and the length of the combination is " + combination.length + " and the number of correct digits is " + clue.getCorrectDigits());
                debugMessages.add("code length is " + code.length + " and the length of the combination is " + combination.length + " and the number of correct digits is " + clue.getCorrectDigits());
                
                    invalidCombinations.add(combination); 
                
                return false;
            } 

            if (numCorrectDigits > clue.getCorrectDigits()) {
                return false;
            }
            
            
        }

        return true;
    }

    


        // check if the combination satisfies one clue - the placement of the digit does not matter
    public boolean checkIfCombinationSatisfiesClue(Integer[] combination, Clue clue) {
            int numCorrectDigits = 0;
            for (int i = 0; i < clue.getCombination().size(); i++) {
                // only check if the number is correct we dont care if it is well placed or not
                if (clue.getCombination().get(i) != null && Arrays.asList(combination).contains(clue.getCombination().get(i))) {
                    numCorrectDigits++;
                }
    
                // if the numbers correct digits is greater than the number of correct digits of the clue then return false
                if (numCorrectDigits > clue.getCorrectDigits()) {
                    
                    // in this case the combination has more correct digits than the clue so it is not a valid combination so we will add it to the invalid combinations list
                    invalidCombinations.add(combination);
                    return false;
                }
    
               
    
            }
    
             if (code.length - combination.length < clue.getCorrectDigits() - numCorrectDigits && code.length != combination.length) {
                if(debug)   
                    System.out.println("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigits(the number of correct digits in the combination): " + numCorrectDigits + " correctDigits: " + clue.getCorrectDigits());

                debugMessages.add("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigits(the number of correct digits in the combination): " + numCorrectDigits + " correctDigits: " + clue.getCorrectDigits());
                
                    // print the code length - the length of the combination(which equals the numbers that have to be right and we just dont know which numbers from the clue are right but we know how many have to be right) and the number of correct digits
                    if(debug)
                        System.out.println("code length is " + code.length + " and the length of the combination is " + combination.length + " and the number of correct digits is " + clue.getCorrectDigits());
                   
                        debugMessages.add("code length is " + code.length + " and the length of the combination is " + combination.length + " and the number of correct digits is " + clue.getCorrectDigits());
                    
                        invalidCombinations.add(combination); 
                    
                    return false;
                } 
    
            
    
            if (numCorrectDigits != clue.getCorrectDigits()) {
                return false;
            }
    
            return true;
        }


        // check if the combination satisfies the clues with the placement of the numbers - make a shorter method name
    public boolean checkIfComboValid(Integer[] combination , Clue clue) {
        int numCorrectDigits = 0;
        int numCorrectDigitsWellPlaced = 0;
        int numCorrectDigitsIncorrectlyPlaced = 0;


        for (int i = 0; i < clue.getCombination().size(); i++) {
            // only check if the number is correct we dont care if it is well placed or not
            if (clue.getCombination().get(i) != null && Arrays.asList(combination).contains(clue.getCombination().get(i))) {
                numCorrectDigits++;

                // check if the number is well placed
                if (clue.getCombination().get(i) == combination[i]) {
                    numCorrectDigitsWellPlaced++;
                } else {
                    numCorrectDigitsIncorrectlyPlaced++;
                }
            }

            // if the numbers correct digits is greater than the number of correct digits of the clue then return false
            if (numCorrectDigits > clue.getCorrectDigits()) {
                        
                        

                // in this case the combination has more correct digits than the clue so it is not a valid combination so we will add it to the invalid combinations list
                invalidCombinations.add(combination);
                return false;
            }

            if (numCorrectDigitsWellPlaced > clue.getWellPlacedDigits()) {
                // in this case the combination has more correct digits that are well placed than the clue so it is not a valid combination so we will add it to the invalid combinations list
                // print combo and clue and numCorrectDigitsWellPlaced and clue.getWellPlacedDigits()
         //       System.out.println("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigitsWellPlaced(combo): " + numCorrectDigitsWellPlaced + " wellPlacedDigits: " + clue.getWellPlacedDigits());
                
               
         invalidCombinations.add(combination);
                return false;
            }

            if (numCorrectDigitsIncorrectlyPlaced > clue.getIncorrectlyPlacedDigits()) {
                // in this case the combination has more correct digits that are incorrectly placed than the clue so it is not a valid combination so we will add it to the invalid combinations list
                // print combo and clue and numCorrectDigitsIncorrectlyPlaced and clue.getIncorrectlyPlacedDigits()
 //               System.out.println("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigitsIncorrectlyPlaced(combo): " + numCorrectDigitsIncorrectlyPlaced + " incorrectlyPlacedDigits: " + clue.getIncorrectlyPlacedDigits());
                
                invalidCombinations.add(combination);
                return false;
            }



           

        }

         if (code.length - combination.length < clue.getCorrectDigits() - numCorrectDigits && code.length != combination.length) {

                // print the code length - the length of the combination(which equals the numbers that have to be right and we just dont know which numbers from the clue are right but we know how many have to be right) and the number of correct digits
                if(debug)
                    System.out.println("code length is " + code.length + " and the length of the combination is " + combination.length + " and the number of correct digits is " + clue.getCorrectDigits());
                
                debugMessages.add("code length is " + code.length + " and the length of the combination is " + combination.length + " and the number of correct digits is " + clue.getCorrectDigits());

                    invalidCombinations.add(combination); 
                
                return false;
         } 

        

        if (numCorrectDigits != clue.getCorrectDigits() || numCorrectDigitsWellPlaced != clue.getWellPlacedDigits() || numCorrectDigitsIncorrectlyPlaced != clue.getIncorrectlyPlacedDigits()) {
            return false;
        }


        return true;
    }

    // get the debug messages
    public ArrayList<String> getDebugMessages() {
        return debugMessages;
    }
    

}