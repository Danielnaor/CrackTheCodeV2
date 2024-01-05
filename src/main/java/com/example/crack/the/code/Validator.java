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
    private boolean debug = true;
    public ArrayList<String> debugMessages = new ArrayList<String>();


    // empty constructor
    public Validator() {
        invalidCombinations = new ArrayList<>();
    }

    // constructor
    public Validator(Integer[] code, List<Clue> clues) {
        this.code = code;
        this.clues = clues;
        this.invalidCombinations = new ArrayList<>();
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
        private ArrayList<Integer[]> invalidCombinations = new ArrayList<>();
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
        return clues.stream().allMatch(clue -> checkIfComboValid(combination, clue));
    }


    // return true if the code found is valid with all clues
    public boolean isCodeValid(Integer[] codeFound) {
        return clues.stream().allMatch(clue -> checkIfCombinationValid(codeFound));
    }


     public boolean isCodeValid() {
        return clues.stream().allMatch(clue -> checkIfCombinationValid(code));
    }


    // check if the combination is valid with all the clues - the placement of the numbers does not matter
    public boolean checkIfCombinationSatisfiesAllClues(Integer[] combination, boolean satisfiesPartially) {
        for (Clue clue : clues) {
            int numCorrectDigits = (int) Arrays.stream(combination)
                    .filter(digit -> clue.getCombination().contains(digit))
                    .count();

            if (numCorrectDigits != clue.getCorrectDigits() && !satisfiesPartially) {
                handleInvalidCombination(combination, clue, numCorrectDigits);
                return false;
            }

            if (code.length - combination.length < clue.getCorrectDigits() - numCorrectDigits) {
                handleInvalidCombinationLength(combination, clue, numCorrectDigits);
                return false;
            }

            if (numCorrectDigits > clue.getCorrectDigits()) {
                return false;
            }
        }
        return true;
    }

    private void handleInvalidCombination(Integer[] combination, Clue clue, int numCorrectDigits) {
        if (debug) {
            String message = String.format("combo: %s clue: %s numCorrectDigits: %d correctDigits: %d",
                    Arrays.toString(combination), clue.getCombination(), numCorrectDigits, clue.getCorrectDigits());
            System.out.println(message);
            debugMessages.add(message);
        }
        invalidCombinations.add(combination);
    }

    private void handleInvalidCombinationLength(Integer[] combination, Clue clue, int numCorrectDigits) {
        if (debug) {
            String message = String.format("code length is %d and the length of the combination is %d and the number of correct digits is %d",
                    code.length, combination.length, clue.getCorrectDigits());
            System.out.println(message);
            debugMessages.add(message);
        }
        invalidCombinations.add(combination);
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

        // in case invalid combination should add a debugging message:
         /* 
    String - whats wrong with the combination: - print if statement and the class name and line number
    the expected value of correctDigits, wellPlacedDigits, incorrectlyPlacedDigits 
    the actual value of correctDigits, wellPlacedDigits, incorrectlyPlacedDigits
    the combination


    */


        for (int i = 0; i < clue.getCombination().size(); i++) {
            //  check if the number is correct (for now not checking if it is well placed or not) if it is then increment the number of correct digits




            if (clue.getCombination().get(i) != null && Arrays.asList(combination).contains(clue.getCombination().get(i))) {
                numCorrectDigits++; // number of digits that are correct for this combination

                // check if the number is well placed
                if (clue.getCombination().get(i) == combination[i]) {  // if so print both
                    System.out.println("clue.getCombination().get(i): " + clue.getCombination().get(i) + " combination[i]: " + combination[i] + "line: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
                    numCorrectDigitsWellPlaced++;
                } else {
                    numCorrectDigitsIncorrectlyPlaced++;
                }
            }


        
            

            // if the numbers correct digits is greater than the number of correct digits of the clue then return false
            if (numCorrectDigits > clue.getCorrectDigits()) {
                        
                        

                // in this case the combination has more correct digits than the clue so it is not a valid combination so we will add it to the invalid combinations list
                invalidCombinations.add(combination);

                 

                String debugMessage = "numCorrectDigits > clue.getCorrectDigits + class: " + this.getClass().getName() + "\t line: " + Thread.currentThread().getStackTrace()[1].getLineNumber() 
                + "\n" + "Expected - correctDigits: " + clue.getCorrectDigits() + " wellPlacedDigits: " + clue.getWellPlacedDigits() + " incorrectlyPlacedDigits: " + clue.getIncorrectlyPlacedDigits()
                + "\n" + "Actual - correctDigits: " + numCorrectDigits + " wellPlacedDigits: " + numCorrectDigitsWellPlaced + " incorrectlyPlacedDigits: " + numCorrectDigitsIncorrectlyPlaced;

                debugMessages.add(debugMessage);

            if(debug)
                    System.out.println(debugMessage);



                return false;
            }

            if (numCorrectDigitsWellPlaced > clue.getWellPlacedDigits()) {
                // in this case the combination has more correct digits that are well placed than the clue so it is not a valid combination so we will add it to the invalid combinations list
                // print combo and clue and numCorrectDigitsWellPlaced and clue.getWellPlacedDigits()
               //System.out.println("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigitsWellPlaced(combo): " + numCorrectDigitsWellPlaced + " wellPlacedDigits: " + clue.getWellPlacedDigits());
                
               
         invalidCombinations.add(combination);

                
                String debugMessage = "numCorrectDigitsWellPlaced > clue.getWellPlacedDigits + class: " + this.getClass().getName() + "\t line: " + Thread.currentThread().getStackTrace()[1].getLineNumber() 
                + "\n" + "Expected - correctDigits: " + clue.getCorrectDigits() + " wellPlacedDigits: " + clue.getWellPlacedDigits() + " incorrectlyPlacedDigits: " + clue.getIncorrectlyPlacedDigits()
                + "\n" + "Actual - correctDigits: " + numCorrectDigits + " wellPlacedDigits: " + numCorrectDigitsWellPlaced + " incorrectlyPlacedDigits: " + numCorrectDigitsIncorrectlyPlaced;

                debugMessages.add(debugMessage);

                if(debug)
                    System.out.println(debugMessage);


                return false;
            }

            if (numCorrectDigitsIncorrectlyPlaced > clue.getIncorrectlyPlacedDigits()) {
                // in this case the combination has more correct digits that are incorrectly placed than the clue so it is not a valid combination so we will add it to the invalid combinations list
                // print combo and clue and numCorrectDigitsIncorrectlyPlaced and clue.getIncorrectlyPlacedDigits()
             //  System.out.println("combo: " + Arrays.toString(combination) + " clue: " + clue.getCombination().toString() + " numCorrectDigitsIncorrectlyPlaced(combo): " + numCorrectDigitsIncorrectlyPlaced + " incorrectlyPlacedDigits: " + clue.getIncorrectlyPlacedDigits());
                
                invalidCombinations.add(combination);


                String debugMessage = "numCorrectDigitsIncorrectlyPlaced > clue.getIncorrectlyPlacedDigits + class: " + this.getClass().getName() + "\t line: " + Thread.currentThread().getStackTrace()[1].getLineNumber()
                + "\n" + "Expected - correctDigits: " + clue.getCorrectDigits() + " wellPlacedDigits: " + clue.getWellPlacedDigits() + " incorrectlyPlacedDigits: " + clue.getIncorrectlyPlacedDigits()
                + "\n" + "Actual - correctDigits: " + numCorrectDigits + " wellPlacedDigits: " + numCorrectDigitsWellPlaced + " incorrectlyPlacedDigits: " + numCorrectDigitsIncorrectlyPlaced;

                debugMessages.add(debugMessage);



                if(debug)
                    System.out.println(debugMessage);

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