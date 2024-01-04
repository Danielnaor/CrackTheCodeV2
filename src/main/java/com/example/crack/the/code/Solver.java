/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author danie
 */

/*
this class will be used to solve the puzzle
How the solver eliminates possible combinations

The way the solver eliminates possible combinations is by using the clues to eliminate the possible combinations. it will do so by the following steps:

1. find all the clues that that thier combinations are nothing is correct and eliminate all the instances of the digits in the combinations of the clues from the possible combinations.
for example if the clue is 1 2 3 nothing is correct and a clue is 1 2 4 one number is correct and well placed the code will replace the 1 and 2 in the clue with -1 to indicate that the numbers are not valid.
2. then the solver will check if there is any clue that were solvedCode
3. then it will check if there is any conflicting cases meaning that there are 2 clues with the a number that is the same for both indexes and in one clue the number is correct and well placed and in the other clue the number is correct but wrongly placed. if there is a conflicting case then the number is not gonna be in the code.
4. then it will check if there is any clue that were solvedCode
5. then it will generate all the possible combinations that can be generated from the clues. (all the numbers in the clues are the possible numbers that can be in the code)
6. check each possible combination if it satisfies all the clues. if it does then it is the correct code.

 */







public class Solver {

    // the code to be solvedCode
    private Integer[] code;

    // store the indexes that are solvedCode
    private boolean[] solvedIndexes;

    private int numSolvedIndexes = 0;

    // the possible combinations
    private ArrayList<Integer[]> possibleCombinations;

    // the clues
    private List<Clue> clues;

    // a boolean to indicate if the code was 
    private boolean solved; 

    // banned list - will store the numbers that are eliminated from all indexes
    private List<Integer> bannedList;

    // banned list indexs - will store the numbers that are eliminated from each index
    private List<List<Integer>> bannedListPerIndex;

     // store the number that we know will be in the code but we dont know the index
     private ArrayList<Integer> inCodeIndexUnknown = new ArrayList<>();
        
     //boolean for debug output
        private static boolean debug = false;

    // this will store the for sure invalid combinations
    private ArrayList<Integer[]> invalidCombinations = new ArrayList<>();

    // the max number
    private int maxNumber = 0;

    private Validator validator;

    // logger
   // private static final Logger logger = Logger.getLogger(Solver.class.getName());
    
    public Solver(List<Clue> clues) {
        this.clues = new ArrayList<>();
        
        for(Clue clue:clues){
            this.clues.add(clue.clone());
        }

        this.solved = false;

        Integer codeLength = 0;
        if  (!clues.isEmpty()) {
            codeLength = clues.get(0).getCombination().size();
        }

        this.code = new Integer[codeLength];
        this.solvedIndexes = new boolean[codeLength];

        // initialize the banned list
        this.bannedList = new ArrayList<>();

        // initialize the banned list per index
        this.bannedListPerIndex = new ArrayList<>();
        for (int i = 0; i < codeLength; i++) {
            this.bannedListPerIndex.add(new ArrayList<>());
        }        
        
    }

    // solve the riddle
    public Integer[] solve() throws IOException{

        // if len of clues is then return;
        if(clues.size() == 0){
            return code;
        }

        

        if(debug){
            System.out.println("the clues before removing nothing is correct: ");
            for (Clue clue : clues) {
                //System.out.println(clue.toString());
                System.out.println(clue.toString());
            }
        }

        


        // check all the nothing is correct clues and eliminate the numbers by adding them to the banned list
        removeNothingIsCorrect();

        
        
        
        if (isSolved()) {
            solved = true;
            return code;
        }
        
        

        // remove all numbers that do not appear in any clue (after nothing is correct since if all clues are nothing is correct then the numbers that do not appear in any clue are the numbers that are not in the code)
        removeInvalidNumbers();

        removeBannedNumbers();

        if(debug){
    
            System.out.println("the clues after removing invalid numbers and banned numbers: ");
            for (Clue clue : clues) {
                //System.out.println(clue.toString());
                System.out.println(clue.toString());
            }
        }
        


        // loop trougth all clues and check if any clue has numbers placed wrongly if so add all those numbers to that baned list too. 
        //banWronglyPlacedAtIndex();

        
        if(debug){
            generatePossibleCombinations();
            System.out.println("the size of the possible combinations: " + possibleCombinations.size());
        }


        // check if there is the code is solvedCode and/or if any clue is solvedCode
        // check if we solvedCode the code
        if (isSolved()) {
            solved = true;
            return code;
        }


        updateBannedListFromUnknown();

        if(debug){
            System.out.println("the clues after updating banned list from unknown: ");
             for (Clue clue : clues) {
                System.out.println(clue.toString());
            }
            // 
            System.out.println("done.\n");
        }
        

       // update the number of solved indexes
         updateNumSolvedIndexes();


        if(numSolvedIndexes == 0 && possibleCombinations != null && possibleCombinations.size() >= 1){
            int clueIndex = maxCorrectDigitsClue(); // the index of the clue that has the most correct digits
            int numCorrectDigits = clues.get(clueIndex).getCorrectDigits(); // the number of correct digits in the clue
            useMostCorrectNumberClue(clueIndex);
            updateBannedListFromUnknown();

            if(possibleCombinations.size() > 1){
                // run it numCorrectDigits - 2 (since we already ran it once) times
                for (int i = 0; i < numCorrectDigits - 2; i++) {
                    useMostCorrectNumberClue(clueIndex);
                    updateBannedListFromUnknown();

                    if(possibleCombinations.size() == 1){
                        break;
                    }
                }
            }


        } else{
            if(debug){
                System.out.println("the number of solved indexes: " + numSolvedIndexes);
            }
        }


        //removeConflictingCases();
        removeContradictingCases();
        
        
        // print the unknown numbers
        if(debug){
            System.out.println("the numbers that are in the code but we dont know the index: " + inCodeIndexUnknown.toString());
        }
    
         if (isSolved()) {
            solved = true;
            return code;
        }

        //
        updateBannedListFromUnknown();
        removeBannedNumbers();


        if (isSolved()) {
            return code;
        }
        if(debug){
            // print the banned list
        System.out.println("the banned list: " + bannedList.toString());

        // print the banned list per index
        System.out.println("the banned list per index: ");
        for (int i = 0; i < bannedListPerIndex.size(); i++) {
            System.out.println("index " + i + ": " + bannedListPerIndex.get(i).toString());
        }

                    System.out.println("the numbers that are in the code but we dont know the index: " + inCodeIndexUnknown.toString());

                    
                    // print the clues
                     System.out.println("the clues after removing banned numbers: ");
        for (Clue clue : clues) {
            System.out.println(clue.toString());
        }


        }
        
        // remove the banned numbers
        removeBannedNumbers();
       
        

        // generate all the possible combinations that can be generated from the clues. (all the numbers in the clues are the possible numbers that can be in the code)
        generatePossibleCombinations();

        
        if(debug){
            // print the size of the possible combinations
            System.out.println("the size of the possible combinations: " + possibleCombinations.size());
        }
        
        
        // validator 
        validator = new Validator.Builder()
                .code(code)
                .clues(clues)
                .build();


        for(Clue clue:clues){
            // loop trougth the possible combinations and check if the combination satisfies the clue
            for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
                

               // if (!checkIfComboValid(possibleCombinations.get(i), clue)) {
                if(!validator.checkIfComboValid(possibleCombinations.get(i), clue)){
                    possibleCombinations.remove(i);

                }

                if (possibleCombinations.size() == 1) {
                    break;
                }
            }

            if (possibleCombinations.size() == 1) {
                break;
            }
        }


        if(isSolved()){
            solved = true;
            return code;
        }
        
        if(debug){
            // print the size of the possible combinations
            System.out.println("the size of the possible combinations: " + possibleCombinations.size());

            // print the banned list per index
            System.out.println("the banned list per index: ");
            for (int i = 0; i < bannedListPerIndex.size(); i++) {
                System.out.println("index " + i + ": " + bannedListPerIndex.get(i).toString());
            }
        }


        
        return code;

    }


    

    private void updateBannedListFromUnknown() {
        // compare with the inCodeIndexUnknown list and the clues and see if the codes can be solvedCode
        // an example 
        // if the inCodeIndexUnknown list is 1 3 7
        // and a clue is 4, null, 7, 3 with 2 correct digits and 1 well placed digit and 1 incorrectly placed digit
        // since the 3 and 7 are in the inCodeIndexUnknown list and the clue has 2 correct digits we then know that the 3 and 7 are in the code and the 4 is not in the code
        // so we can eliminate the 4 from the clue and add it to the banned list of the index

        
        for (Clue clue : clues) {
            // count how many numbers in the clue are in the inCodeIndexUnknown list
            int numInCodeIndexUnknown = 0;
            for (Integer number : clue.getCombination()) {
                if (inCodeIndexUnknown.contains(number)) {
                    numInCodeIndexUnknown++;
                }
            }

            // if the number of numbers in the clue that are in the inCodeIndexUnknown list is the same as the number of correct digits then we know that all the numbers in the clue are in the code and the numbers that are not in the clue are not in the code
            if (numInCodeIndexUnknown == clue.getCorrectDigits()) {
                // loop trougth the numbers in the clue
                for (int i = 0; i < clue.getCombination().size(); i++) {

                    // if the number is in the inCodeIndexUnknown list then remove it from the banned list (for all indexes)
                    if (!inCodeIndexUnknown.contains(clue.getCombination().get(i)) && clue.getCombination().get(i) != null) {
                     if (!bannedList.contains(clue.getCombination().get(i))) {
                            bannedList.add(clue.getCombination().get(i));
                        }
                    }

                }
            }
        }

        // check if the number at inCodeIndexUnknown can be added to the index's banned list
        for (Integer number : inCodeIndexUnknown) {
            // loop trougth the clues and check if the number is in the clue
            for (Clue clue : clues) {
                if (clue.getCombination().contains(number)) {
                    // check if the all the numbers that are correct not well placed and if so then add the number to the banned list of the index
                    if (clue.getCorrectDigits() == clue.getIncorrectlyPlacedDigits()) {
                        if (!bannedListPerIndex.get(clue.getCombination().indexOf(number)).contains(number)) {
                            bannedListPerIndex.get(clue.getCombination().indexOf(number)).add(number);
                        }
                    }
                }
            }
        }

        // update the numer of correct digits and incorrectly placed digits
        for (Clue clue : clues) {
            for(int i = 0; i < clue.getCombination().size(); i++){
                if(clue.getCombination().get(i) != null){
                    if(bannedListPerIndex.get(i).contains(clue.getCombination().get(i))){
                        clue.setCorrectDigits(clue.getCorrectDigits() - 1);
                        clue.setIncorrectlyPlacedDigits(clue.getIncorrectlyPlacedDigits() - 1);
                    }
                }
            }
        } 

        // remove the banned numbers
        removeBannedNumbers();

    }

    
        

      

    private void removeContradictingCases() {
        /*
         * contradictions:
         * 
         * example of senerio - given the 2 following clues:
            - 1 2 3 - one number is correct and well placed
            - 4 5 3 - one number is correct but wrongly placed

            - we can say that the number 3 is wrong and wont be on the code ( at all indexs) because 
            it cant be correct and well placed and correct but wrongly placed at the same time. (it contradicts itself)

         */

        
            
        // loop through and look for clues and/or digits that we are 100% sure about the correctness of thier placement
        // and then check for contradictions - if it's present at the same index in another clue and it's not well placed then it's not in the code

        // loop trougth the clues
        for (int i = 0; i < clues.size(); i++) {
            Clue clue = clues.get(i);
            
            // check if we know for sure if the number is well placed or not
            if(((clue.getWellPlacedDigits() == clue.getCorrectDigits()) || (clue.getIncorrectlyPlacedDigits() == clue.getCorrectDigits())) && clue.getCorrectDigits() > 0){
                // loop trougth the numbers in the clue
                for (int j = 0; j < clue.getCombination().size(); j++) {
                    // check if the number is not null
                    if(clue.getCombination().get(j) != null){

                    Integer number = clue.getCombination().get(j);

                    boolean isCorrectlyPlaced = clue.getWellPlacedDigits() > 0;



                    // look if the number is in the same index in another clue
                    for (int k = 0; k < clues.size(); k++) {
                        if(k != i){
                            Clue otherClue = clues.get(k);
                            // check if the number is in the same index in the other clue
                            if(otherClue.getCombination().get(j) != null && otherClue.getCombination().get(j) == number){
                               // make sure the correctness aspect is the opposite for the other clue
                               boolean isCorrectlyPlacedOtherClue = otherClue.getWellPlacedDigits() > 0;

                                 if(isCorrectlyPlaced != isCorrectlyPlacedOtherClue){
                                      // if the number is not in the banned list then add it to the banned list
                                      if(!bannedList.contains(number)){
                                        bannedList.add(number);
                                      }
                                 }

                            }
                        }
                    }
                }
                }
            }
        }

        // remove the banned numbers
        removeBannedNumbers();

        // TODO: check if the code is solvedCode
        // TODO: include explenations as for why a certain combination is not valid
                    


    }

    /**
     * Remove all numbers that do not appear in any clue.
     *
     */
    public void removeInvalidNumbers() {

        

        HashSet<Integer> allNumbers = getAllNumbersInClues();
        
        // add the numbers in the inCodeIndexUnknown list to the allNumbers list
        for (Integer number : inCodeIndexUnknown) {
                if(debug)
                    System.out.println("adding number " + number + " to the allNumbers list");
                allNumbers.add(number);
        }

        if(debug)
            System.out.println("all numbers: " + allNumbers.toString());
        
         
        // the max number of all numbers
        if (!allNumbers.isEmpty()) {
             maxNumber = allNumbers.stream().max(Integer::compare).get();
        }        

        for (int i = 0; i < maxNumber; i++) {
            if (!allNumbers.contains(i)) {
                bannedList.add(i);
                if(debug){
                    System.out.println("the number " + i + " is not in any clue");
                }
                
            }
        }

       
    }

    

    public HashSet<Integer> getAllNumbersInClues() {
        HashSet<Integer> allNumbers = new HashSet<>();
        for(Clue clue:clues){
            for(Integer number:clue.getCombination()){
                if(number != null){
                    allNumbers.add(number);
                }
            }
        }

        return allNumbers;
    }

    private void removeNothingIsCorrect() {
        if(debug){
            System.out.println("has nothing is correct: " + (lookForNothingIsCorrect() != null));
        }
       while(lookForNothingIsCorrect() != null){
            int clueIndex = lookForNothingIsCorrect();
            List<Integer> combination = clues.get(clueIndex).getCombination();
            for (int i = 0; i < combination.size(); i++) {
                if (combination.get(i) != null) {
                    if (!bannedList.contains(combination.get(i))) {
                        bannedList.add(combination.get(i));
                    }
                }
                               
            }

            clues.remove(clueIndex);
        }

        if(debug){
            // print banned list
            System.out.println("the banned list: " + bannedList.toString());

        }
       

        removeBannedNumbers();
    }


    

    private Integer lookForNothingIsCorrect() {
        for(int i = 0; i < clues.size(); i++){
            Clue clue = clues.get(i);
            if (clue.getCorrectDigits() == 0) { // if 0 correct digits then nothing is correct
                return i;
            }
        }

        return null;
    }

    private void checkIfClueSolved() {
        // if the number of numbers that are not nulls in the combination is the same as the number of correct digits then the clue is solvedCode
        for (Clue clue : clues) {
            clue.getCombinationObject().updateNumNotNull();
            if (clue.getCombinationObject().numNotNull == clue.getCorrectDigits() && clue.getCorrectDigits() > 0) {
                for (int i = 0; i < clue.getCombination().size(); i++) {
                    if (clue.getCombination().get(i) != null && code[i] == null) {
                        // detarmine if the number is well placed or not
                        if (clue.getWellPlacedDigits() > 0 && clue.getIncorrectlyPlacedDigits() == 0) {
                            code[i] = clue.getCombination().get(i);
                            solvedIndexes[i] = true;

                            // update the number correct digits and well placed digits
                            clue.setCorrectDigits(clue.getCorrectDigits() - 1);
                            clue.setWellPlacedDigits(clue.getWellPlacedDigits() - 1);

                            // also update the number of numbers that are not nulls in the combination
                            clue.getCombinationObject().numNotNull--;

                            // replace the number in the combination with null
                            if(debug)
                                System.out.println("combo before removing numebr: " + clue.getCombinationObject().toString());
                          
                            clue.getCombination().set(i, null);
                            clue.getCombinationObject().combination.set(i, null);
                            if(debug)
                                System.out.println("combo after: " + clue.getCombinationObject().toString());
                            
                        }
                        // if the number of well placed digits is 0 then the number is not well placed so add it to the banned list of the index
                        else if (clue.getWellPlacedDigits() == 0) {
                            // if the number is not in the banned list then add it to the banned list
                            if (!bannedListPerIndex.get(i).contains(clue.getCombination().get(i))) {
                                bannedListPerIndex.get(i).add(clue.getCombination().get(i));
                            }
                            // since the number is not well placed then but is correct then it is not in the code so add it to the list of numbers that are in the code but we dont know the index
                            /*if (!inCodeIndexUnknown.contains(clue.getCombination().get(i))) {
                            }*/
                            
                            // if debug is true and the inCodeIndexUnknown already contains the number then print a debug message
                        /*     if (debug && inCodeIndexUnknown.contains(clue.getCombination().get(i))) {
                                System.out.println("the number " + clue.getCombination().get(i) + " is already in the inCodeIndexUnknown list");
                            }
                            */

                            inCodeIndexUnknown.add(clue.getCombination().get(i));

                            // update the number correct digits and incorrectly placed digits
                            clue.setCorrectDigits(clue.getCorrectDigits() - 1);
                            clue.setIncorrectlyPlacedDigits(clue.getIncorrectlyPlacedDigits() - 1);

                            

                            // replace the number in the combination with null
                            clue.getCombination().set(i, null);
                            clue.getCombinationObject().combination.set(i, null);

                            clue.getCombinationObject().updateNumNotNull();
                            



                        }
                          else{ // in this case we have some correct numbers that are well placed and some correct numbers that are not well placed
                        
                            // check if the numbers is in the index banned list of all indexes that are not solvedCode except one
                            int inHowManyBannedLists = 0;
                            int indexNotBanned = 0;
                            for (int j = 0; j < bannedListPerIndex.size(); j++) {
                                if (!solvedIndexes[j]) {
                                if (bannedListPerIndex.get(j).contains(clue.getCombination().get(i))) {
                                        inHowManyBannedLists++;
                                    } else {
                                        indexNotBanned = j;
                                    }
                              }
                            }

                            int numUnsolveIndexes = 0;
                            for(boolean indexSolved:solvedIndexes){
                                if(!indexSolved){
                                    numUnsolveIndexes++;
                                }
                            }
                            // if the number is in all the banned lists except one then the number is in the index that is not banned
                            if (inHowManyBannedLists == numUnsolveIndexes - 1) {

                                
                                code[indexNotBanned] = clue.getCombination().get(i);
                                solvedIndexes[indexNotBanned] = true;
                            

                            // update the number correct digits and incorrectly placed digits
                            clue.setCorrectDigits(clue.getCorrectDigits() - 1);
                            if(indexNotBanned == i){
                                clue.setWellPlacedDigits(clue.getWellPlacedDigits() - 1);
                            } else {
                                clue.setIncorrectlyPlacedDigits(clue.getIncorrectlyPlacedDigits() - 1);
                            }


                            // replace the number in the combination with null
                            clue.getCombination().set(i, null);
                            clue.getCombinationObject().combination.set(i, null);

                            clue.getCombinationObject().updateNumNotNull();
                            }

                        }
                       // */




                       
                        
                    }
                }
            } if (clue.getCorrectDigits() == 0 && clue.getCombinationObject().numNotNull != 0) {
                System.out.println("the clue " + clue.getHintMessage() + " does not have any correct digits but the number of numbers that are not nulls in the combination is not 0");
                
            }
        }

        

        // remove the clues that are solvedCode
        for (int i = 0; i < clues.size(); i++) {
            if (clues.get(i).getCorrectDigits() == 0) {
                clues.remove(i);
            }
        }
    }

    /**
     * replace all numbers that are in the banned list from the clues with null
     * 
     *
     */
    private void removeBannedNumbers() {
        // loop between banned list
        for(Integer num:bannedList){
            
            // add the number to the banned list of all indexes
            for (int i = 0; i < bannedListPerIndex.size(); i++) {
                if (!bannedListPerIndex.get(i).contains(num)) {
                    bannedListPerIndex.get(i).add(num);
                }
            }
        }

        // loop between the clues and replace all the numbers that are in the banned list at that index with null
        for (Clue clue : clues) {
            for (int i = 0; i < clue.getCombination().size(); i++) {
                if (clue.getCombination().get(i) != null && bannedListPerIndex.get(i).contains(clue.getCombination().get(i))) {
                    clue.getCombination().set(i, null);
                    clue.getCombinationObject().combination.set(i, null);
                    clue.getCombinationObject().numNotNull--;
                }
            }
        }



        
    }

    // a different, better name for this method, that checks if the code, of the riddle, which is the thing that we want to solve for is "isSolvedCode"
    boolean isSolved() {

        

        // if the possible combinations is 1 then the code is solvedCode
        if ( possibleCombinations != null && possibleCombinations.size() == 1) {
            code = possibleCombinations.get(0);
            
            return true;
        }

        // call a method that will update numSolvedIndexes to the number of indexes that are solved. 
        updateNumSolvedIndexes();


        //either the must include number have somthing or the known index have somthing
        if (clues.size() == 0 && (inCodeIndexUnknown.size() > 0 || numSolvedIndexes > 0)) {
            generatePossibleCombinations();

            if (possibleCombinations.size() == 1) {
                code = possibleCombinations.get(0);
                return true;
            } else if (possibleCombinations.size() == 0) {
                throw new RuntimeException("the code is not solved and there are no possible combinations left and the number of clues is 0");
            }

        }

        removeBannedNumbers();
        // check for any solvedCode clues
        checkIfClueSolved();

        // check if there is only one index that the numbers without a known index can be in
        int numUnsolveIndexes = 0;
        for(boolean indexSolved:solvedIndexes){
            if(!indexSolved){
                numUnsolveIndexes++;
            }
        }

        if(numUnsolveIndexes == 0){
            return true;
        } else if(numUnsolveIndexes == 1){
            for (int i = 0; i < code.length; i++) {
                if (code[i] == null) {
                    // find the number that is not in the banned list
                    for (Integer number : inCodeIndexUnknown) {
                        if (!bannedListPerIndex.get(i).contains(number)) {
                            code[i] = number;
                            return true;
                        }
                    }
                    
                }
            }
        }

        return false;
    }

    

    private void updateNumSolvedIndexes() {
        numSolvedIndexes = 0;
        
        for(boolean indexSolved:solvedIndexes){
            if(indexSolved){
                numSolvedIndexes++;
            }
        }
    }

    /**
     * generate all the possible combinations that can be generated considering the banned list and the banned list per index
     * 
     *
     */
    private void generatePossibleCombinations() {
        // initialize the possible combinations
        if(possibleCombinations != null ){
            if(debug)   
                System.out.println("before generating possible combinations - size of possibleCombinations list: " + possibleCombinations.size());

                possibleCombinations = new ArrayList<>();
       // print the size of the possible combinations
       if(debug)
            System.out.println("the size of the possible combinations: " + possibleCombinations.size());



        } else{
            possibleCombinations = new ArrayList<>();
        }
       

        generateCombinations(possibleCombinations, new Integer[code.length], 0);

        
        // print called generateCombinations


    if(debug)
        System.out.println("called generateCombinations" + " - size of possibleCombinations list: " + possibleCombinations.size());


        // debug - prinnt the size of the possible combinations
        if(debug){
            System.out.println("the size of the possible combinations (before removeing duplicates): " + possibleCombinations.size());
        }
        // remove the same combinations
        for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (Arrays.equals(possibleCombinations.get(i), possibleCombinations.get(j))) {
                    possibleCombinations.remove(i);
                    break;
                }
            }
        }


        if(debug){
                System.out.println("the size of the possible combinations (after removeing duplicates): " + possibleCombinations.size());
        }


        

        // remove combinations that doesnt contains ALL the numbers that are in the inCodeIndexUnknown list
        for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
            Integer[] combination = possibleCombinations.get(i);
            for (Integer digit : inCodeIndexUnknown) {
                if (!Arrays.asList(combination).contains(digit)) {
                    if(debug){
                        System.out.println("removing combination: " + Arrays.toString(combination) + " because it does not contain the number " + digit);
                    }
                    possibleCombinations.remove(i);
                    break;
                }
            }
        }
        

        // print the size of the possible combinations
        if(debug)
            System.out.println("the size of the possible combinations (after removeing combinations that doesnt contains ALL the numbers that are in the inCodeIndexUnknown list): " + possibleCombinations.size());

        

    }

   

    private void generateCombinations(ArrayList<Integer[]> combinations, Integer[] currentCombination, int index) {
        if(index == code.length){

          //  System.out.println("adding combination: " + Arrays.toString(currentCombination));

            // Create a copy of the array before adding it to the list
            Integer[] copyCombination = Arrays.copyOf(currentCombination, currentCombination.length);
    
            combinations.add(copyCombination);
            possibleCombinations.add(copyCombination);
            return;
        }

        if(code[index] != null){
            currentCombination[index] = code[index];
            generateCombinations(combinations, currentCombination, index + 1);
            return;
        } 

        for (int digit = 0; digit < 10; digit++) {
            if (!bannedListPerIndex.get(index).contains(digit)) {
               // System.out.println("index " + index + " digit " + digit + " is not in the banned list of index " + index + " so we will add it to the current combination: ");
                currentCombination[index] = digit;
                generateCombinations(combinations, currentCombination, index + 1);
            } else{
                if(debug){
                    System.out.println("the number " + digit + " is in the banned list of index " + index);
                    // print the banned list of index
                    System.out.println("the banned list of index " + index + ": " + bannedListPerIndex.get(index).toString());
                }
            }
        }

    }

    // return the index of the clue that has the most correct digits
    private int maxCorrectDigitsClue(){
        // loop trougth the clues and find the clue that has the most correct digits
        int maxCorrectDigits = 0;
        int clueIndex = 0;
        for (int i = 0; i < clues.size(); i++) {
            if (clues.get(i).getCorrectDigits() > maxCorrectDigits) {
                maxCorrectDigits = clues.get(i).getCorrectDigits();
                clueIndex = i;
            }
        }

        return clueIndex;
    }


    private void useMostCorrectNumberClue(int clueIndex){
        // with this method we will use the clue that has the most correct digits
        // we will test what happeds if we pass in each digit with all clues to determine if we can solve for at least one digits

        // loop trougth the clues and find the clue that has the most correct digits
       

        List<Clue> mostUseClueAsList = new ArrayList<>();
        mostUseClueAsList.add(clues.get(clueIndex));

        // generate all possibale combinations that have a length = to the number of correct digits in the clue
        possibleCombinations = new ArrayList<>();


        // the combinations will just be some of the numbers in the clue (it will be X numbers where X is the number of correct digits in the clue)
        // the order of the numbers in the combination does not matter
        // it will not include all the numbers in the clue
        //  example if the clue is 1 2 3 and the number of correct digits is 2 then the possible combinations will be 1 2, 1 3, 2 3. 
        for (int i = 0; i < mostUseClueAsList.size(); i++) {

        Clue currentClue = mostUseClueAsList.get(i);
        List<Integer> combination = currentClue.getCombination();
        int numCorrectDigits = currentClue.getCorrectDigits(); // the number of correct digits in the clue
            

        generateCombinationsOneClue(possibleCombinations, combination, numCorrectDigits);


        }




         // print the size of the possible combinations (one clue)
         if(debug)
            System.out.println("the size of the possible combinations (one clue): " + possibleCombinations.size());
        

        // now chcek which combination satisfies the number of correct digits of the clues (for now we will not check if the numbers are well placed or not)
        for (int j = possibleCombinations.size() - 1; j >= 0; j--) {

            // initialize the validator using the builder
            validator = new Validator.Builder()
            .code(code)
            .clues(clues)
            .build();

            if (!validator.checkIfCombinationSatisfiesAllClues(possibleCombinations.get(j),false)) {
                possibleCombinations.remove(j);
            }

        }

        // if all combinations do not satisfy the clues then regenerate the combinations but this time we will check if the combinations satisfy the clues partially
        if (possibleCombinations.size() == 0) {
            if(debug)
                System.out.println("all combinations do not satisfy the clues so we will check if the combinations satisfy the clues partially");

            // generate all possibale combinations that have a length = to the number of correct digits in the clue
            possibleCombinations = new ArrayList<>();
            for (int i = 0; i < mostUseClueAsList.size(); i++) {

                Clue currentClue = mostUseClueAsList.get(i);
                List<Integer> combination = currentClue.getCombination();
                int numCorrectDigits = currentClue.getCorrectDigits(); // the number of correct digits in the clue
            

                 generateCombinationsOneClue(possibleCombinations, combination, numCorrectDigits);
             }

        // print the size of the possible combinations (one clue)
        if(debug)
            System.out.println("the size of the possible combinations (one clue): " + possibleCombinations.size());

        // print with partial
        if(debug)
            System.out.println("with partial: ");
        // now chcek which combination satisfies the number of correct digits at least partily of the clues (for now we will not check if the numbers are well placed or not)
        for (int j = possibleCombinations.size() - 1; j >= 0; j--) {

            // initialize the validator using the builder
            validator = new Validator.Builder()
            .code(code)
            .clues(clues)
            .build();

            if (!validator.checkIfCombinationSatisfiesAllClues(possibleCombinations.get(j),true)) {
                possibleCombinations.remove(j);
            }

            
        }
        

        }

        // remove the combinations that are in the invalid combinations list
         for (Integer[] invalidCombination : invalidCombinations) {
            for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
                if (Arrays.equals(possibleCombinations.get(i), invalidCombination)) {
                    possibleCombinations.remove(i);
                }
            }
            
        }

        // remove the same combinations
        for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (Arrays.equals(possibleCombinations.get(i), possibleCombinations.get(j))) {
                    possibleCombinations.remove(i);
                    break;
                }
            }
        }


        // remove all combinations that do not satisfy the clue
       if( possibleCombinations.size() > 1){
        if(possibleCombinations.size() == 1){
            // add all those digits to the inCodeIndexUnknown list
            for (Integer digit : possibleCombinations.get(0)) {
                if (!inCodeIndexUnknown.contains(digit)) {
                    inCodeIndexUnknown.add(digit);
                }

                // print - updated the inCodeIndexUnknown list
                if(debug)
                    System.out.println("updated the inCodeIndexUnknown list: " + inCodeIndexUnknown.toString());

            }
        } else {

            // print the possible combinations
            if(debug){
                System.out.println("the possible combinations: ");
                for (Integer[] combination : possibleCombinations) {
                    System.out.println(Arrays.toString(combination));
                }
            }      
        // check which combination satisfies the most clues
        int maxSatisfies = 0;
        int maxSatisfiesIndex = 0;
        for (int i = 0; i < possibleCombinations.size(); i++) {
            int numSatisfies = 0;
            for (Clue clue : clues) {

                // reinitialize the validator using the builder
                validator = new Validator.Builder()
                .code(code)
                .clues(clues)
                .build();

                if (validator.checkIfCombinationSatisfiesClue(possibleCombinations.get(i), clue)) {
                    numSatisfies++;
                }

                
            }

            // print how many clues the combination satisfies
            if(debug)
                System.out.println("the combination: " + Arrays.toString(possibleCombinations.get(i)) + " satisfies " + numSatisfies + " clues");

            if (numSatisfies > maxSatisfies) {
                maxSatisfies = numSatisfies;
                maxSatisfiesIndex = i;
            } 
            
            if(numSatisfies == maxSatisfies){
                // we will add the most common number in both combinations to the inCodeIndexUnknown list
                // first we will find the most common number in both combinations

                // check which one fully satisfies most clues
                int numSatisfies1 = 0;
                int numSatisfies2 = 0;


                for (Clue clue2 : clues) {
                    // print each clue
                    
                    // reinitialize the validator using the builder
                    validator = new Validator.Builder()
                    .code(code)
                    .clues(clues)
                    .build();
                    

                    if (validator.checkIfCombinationSatisfiesClue(possibleCombinations.get(i), clue2)) {
                        numSatisfies1++;
                    }

                    if (validator.checkIfCombinationSatisfiesClue(possibleCombinations.get(maxSatisfiesIndex), clue2)) {
                        numSatisfies2++;
                    }
                }

                if(numSatisfies1 > numSatisfies2){
                    maxSatisfiesIndex = i;
                } else if(numSatisfies1 == numSatisfies2){


                    // print both satisfies the same number of clues
                    if(debug)
                        System.out.println("both fully satisfies the same number of clues");
                } 

                // if the combinations are not the same 
                if (!Arrays.equals(possibleCombinations.get(i), possibleCombinations.get(maxSatisfiesIndex))) {
                    // find the most common number in both combinations

                int mostCommonNumber = 0;
                int mostCommonNumberCount = 0;
                for (Integer number : possibleCombinations.get(i)) {
                    if (number == null) {
                        continue;
                    }
                    int count = 0;
                    for (Integer number2 : possibleCombinations.get(maxSatisfiesIndex)) {
                        if (number == number2) {
                            count++;
                        }
                    }

                    
                    if (count > mostCommonNumberCount && !inCodeIndexUnknown.contains(number)){
                        mostCommonNumberCount = count;
                        mostCommonNumber = number;
                    }
                }

                // print the combination
                if(debug)
                    System.out.println("the combination: " + Arrays.toString(possibleCombinations.get(i)) + " and the combination: " + Arrays.toString(possibleCombinations.get(maxSatisfiesIndex)) + " both fully satisfies the same number of clues and the most common number is " + mostCommonNumber + " and the count is " + mostCommonNumberCount);
                // print the count and the number
                if(debug)
                    System.out.println("the most common number is " + mostCommonNumber + " and the count is " + mostCommonNumberCount);


                // add the most common number to the inCodeIndexUnknown list
                if (!inCodeIndexUnknown.contains(mostCommonNumber)) {
                    inCodeIndexUnknown.add(mostCommonNumber);
                }

                

                // print - updated the inCodeIndexUnknown list
                if(debug)
                    System.out.println("updated the inCodeIndexUnknown list: " + inCodeIndexUnknown.toString());
            }
        }
                
            
        }

        // print the maxSatisfies and the maxSatisfiesIndex
        if(debug)
            System.out.println("the maxSatisfies: " + maxSatisfies + " and the maxSatisfiesIndex: " + maxSatisfiesIndex + " and the combination: " + Arrays.toString(possibleCombinations.get(maxSatisfiesIndex)));


        }



        } else {




            // problem
            System.out.println("problem with the clue " + clues.get(clueIndex).getHintMessage());
            System.out.println("the size of the possible combinations: " + possibleCombinations.size());

            // print the possible combinations
            if(debug){
            System.out.println("the possible combinations: ");
            for (Integer[] combination : possibleCombinations) {
                System.out.println(Arrays.toString(combination));
            }
        }   
        }
        
    }

    private void generateCombinationsOneClue(ArrayList<Integer[]> possibleCombinations, List<Integer> numbers, int numCorrectDigits) {
        int[] indices = new int[numCorrectDigits];
        int lastIndex = numCorrectDigits - 1;
    
        // Initialize indices
        for (int i = 0; i < numCorrectDigits; i++) {
            indices[i] = i;
        }
    
        while (indices[0] < numbers.size() - numCorrectDigits + 1) {
            Integer[] combination = new Integer[numCorrectDigits];
    
            // Build the combination
            for (int i = 0; i < numCorrectDigits; i++) {
                combination[i] = numbers.get(indices[i]);
            }
    
            possibleCombinations.add(combination);
    
            // Move to the next combination
            if (indices[lastIndex] < numbers.size() - 1) {
                indices[lastIndex]++;
            } else {
                int j = lastIndex;
                while (j > 0 && indices[j] == numbers.size() - numCorrectDigits + j) {
                    j--;
                }
    
                indices[j]++;
                for (int k = j + 1; k < numCorrectDigits; k++) {
                    indices[k] = indices[k - 1] + 1;
                }
            }
        }
    }

        
    

    // get possible combinations
    public ArrayList<Integer[]> getPossibleCombinations() {
        return possibleCombinations;
    }

    // set debug
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    // ban num at index
    public void banNumAtIndex(int index, int num) {
        if (!bannedListPerIndex.get(index).contains(num)) {
            bannedListPerIndex.get(index).add(num);
        }
    }

    //get solved
    public boolean getSolved() {
        return solved;
    }

    // get code
    public Integer[] getCode() {
        return code;
    }


}
