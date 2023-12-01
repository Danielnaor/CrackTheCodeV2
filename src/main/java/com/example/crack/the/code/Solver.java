/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

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
2. then the solver will check if there is any clue that were solved
3. then it will check if there is any conflicting cases meaning that there are 2 clues with the a number that is the same for both indexes and in one clue the number is correct and well placed and in the other clue the number is correct but wrongly placed. if there is a conflicting case then the number is not gonna be in the code.
4. then it will check if there is any clue that were solved
5. then it will generate all the possible combinations that can be generated from the clues. (all the numbers in the clues are the possible numbers that can be in the code)
6. check each possible combination if it satisfies all the clues. if it does then it is the correct code.

 */







public class Solver {

    // the code to be solved
    private Integer[] code;

    // store the indexes that are solved
    private boolean[] solvedIndexes;

    // the possible combinations
    private ArrayList<Integer[]> possibleCombinations;

    // the clues
    private List<Clue> clues;

    // a boolean to indicate if the code was solved
    private final boolean solved;

    // banned list - will store the numbers that are eliminated from all indexes
    private List<Integer> bannedList;

    // banned list indexs - will store the numbers that are eliminated from each index
    private List<List<Integer>> bannedListPerIndex;

     // store the number that we know will be in the code but we dont know the index
     private ArrayList<Integer> inCodeIndexUnknown = new ArrayList<>();
        
     //boolean for debug output
        private static final boolean debug = true;

    


    // constructor
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

    public Integer[] solve(){

        if(debug){
            System.out.println("the clues before removing nothing is correct: ");
             for (Clue clue : clues) {
                System.out.println(clue.toString());
            }
            // 
            System.out.println("done.\n");
        }


        // check all the nothing is correct clues and eliminate the numbers by adding them to the banned list
        removeNothingIsCorrect();

        
        
        
         if (isSolved()) {
            return code;
        }
        
        

        // remove all numbers that do not appear in any clue (after nothing is correct since if all clues are nothing is correct then the numbers that do not appear in any clue are the numbers that are not in the code)
        removeInvalidNumbers();

        removeBannedNumbers();

        if(debug){
            System.out.println("the clues after removing invalid numbers and banned numbers: ");

             for (Clue clue : clues) {
                System.out.println(clue.toString());
            }
            // 
            System.out.println("done.\n");
        }
        


        // loop trougth all clues and check if any clue has numbers placed wrongly if so add all those numbers to that baned list too. 
        //banWronglyPlacedAtIndex();

        



        // check if there is the code is solved and/or if any clue is solved
        // check if we solved the code
        if (isSolved()) {
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
        

        if(debug){

        generatePossibleCombinations();

        // print the size of the possible combinations
        System.out.println("the size of the possible combinations: " + possibleCombinations.size());
        }
        // remove all conflicting cases
        // if there is a conflicting case then the number is not gonna be in the code
        // conflicting case is when there are 2 clues with the a number that is the same for both indexes and in one clue the number is correct and well placed and in the other clue the number is correct but wrongly placed
        removeConflictingCases();
                if(debug){

                generatePossibleCombinations();
        // print the size of the possible combinations
        System.out.println("the size of the possible combinations: " + possibleCombinations.size());

                        }
        
                        // print the unknown numbers
        System.out.println("the numbers that are in the code but we dont know the index: " + inCodeIndexUnknown.toString());


        // print the clues
        /*
        System.out.println("the clues: ");
        for (Clue clue : clues) {
            System.out.println(clue.toString());
        }
//      */
        
        // print the banned list
        System.out.println("the banned list: " + bannedList.toString());

        // print the banned list per index
        System.out.println("the banned list per index: ");
        for (int i = 0; i < bannedListPerIndex.size(); i++) {
            System.out.println("index " + i + ": " + bannedListPerIndex.get(i).toString());
        }

        // generate all the possible combinations that can be generated from the clues. (all the numbers in the clues are the possible numbers that can be in the code)
        generatePossibleCombinations();

        // print the size of the possible combinations
        System.out.println("the size of the possible combinations: " + possibleCombinations.size());
        
        return code;

    }

    private void updateBannedListFromUnknown() {
        // compare with the inCodeIndexUnknown list and the clues and see if the codes can be solved
        // an example 
        // if the inCodeIndexUnknown list is 1 3 7
        // and a clue is 4, null, 7, 3 with 2 correct digits and 1 well placed digit and 1 incorrectly placed digit
        // since the 3 and 7 are in the inCodeIndexUnknown list and the clue has 2 correct digits we then know that the 3 and 7 are in the code and the 4 is not in the code
        // so we can eliminate the 4 from the clue and add it to the banned list of the index

        
        // loop trougth the clues
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
                  /*  // if the number is not in the inCodeIndexUnknown list then add it to the banned list of the index
                    if (!inCodeIndexUnknown.contains(clue.getCombination().get(i)) && clue.getCombination().get(i) != null) {
                        if (!bannedListPerIndex.get(i).contains(clue.getCombination().get(i))) {
                            bannedListPerIndex.get(i).add(clue.getCombination().get(i));
                        }
                    } */

                    // if the number is in the inCodeIndexUnknown list then remove it from the banned list (for all indexes)
                    if (!inCodeIndexUnknown.contains(clue.getCombination().get(i)) && clue.getCombination().get(i) != null) {
                     if (!bannedList.contains(clue.getCombination().get(i))) {
                            bannedList.add(clue.getCombination().get(i));
                        }
                    }

                }
            }
        }

        // remove the banned numbers
        removeBannedNumbers();

    }

    private void banWronglyPlacedAtIndex() {

        


        for(Clue clue:clues){
            if(clue.getIncorrectlyPlacedDigits() == clue.getCorrectDigits()){
                if(clue.getCorrectDigits() <= 0){
                    // throw an error
                    throw new Error("the clue " + clue.getHintMessage() + " has 0 correct digits");
                }
                if(clue.getWellPlacedDigits() > 0){
                    // throw an error
                    throw new Error("the clue " + clue.getHintMessage() + " has well placed digits");
                }

                // add all the numbers in the clue to the banned list of the index
                for (int i = 0; i < clue.getCombination().size(); i++) {
                    if (clue.getCombination().get(i) != null) {
                        // if the number is not in the banned list then add it to the banned list
                        if (!bannedListPerIndex.get(i).contains(clue.getCombination().get(i))) {
                            bannedListPerIndex.get(i).add(clue.getCombination().get(i));
                        }
                        
                    }
                }

            }
        }

        // remove the banned numbers
        removeBannedNumbers();

       
      
        
        
    
    }

    private void removeConflictingCases() {
        // check if there is any conflicting cases
        // conflicting case is when there are 2 clues with the a number that is the same for both indexes and in one clue the number is correct and well placed and in the other clue the number is correct but wrongly placed
        for (int i = 0; i < clues.size(); i++) {
            Clue clue1 = clues.get(i);
            for (int j = i + 1; j < clues.size(); j++) {
                
                Clue clue2 = clues.get(j);

                // check if the clues have the same number in the same index
                for (int k = 0; k < clue1.getCombination().size(); k++) {
                    if (clue1.getCombination().get(k) != null && clue2.getCombination().get(k) != null && clue1.getCombination().get(k) == clue2.getCombination().get(k)) {
                        // check if the number is correct and well placed in one clue and correct but wrongly placed in the other clue
                        if (clue1.getWellPlacedDigits() > 0 && clue2.getIncorrectlyPlacedDigits() > 0 && clue1.getIncorrectlyPlacedDigits() == 0 && clue2.getWellPlacedDigits() == 0) {
                            // if the number is not in the banned list then add it to the banned list
                            if (!bannedList.contains(clue1.getCombination().get(k))) {
                                bannedList.add(clue1.getCombination().get(k));
                            }
                    
                            // not needed to update the correct digits and well placed digits since we know that those numbers are not in the code (incorrectly numbers

                                                    
                          //  clue1.getCombinationObject().numNotNull--;
                          //  clue2.getCombinationObject().numNotNull--;

                        }
                    }
                }
            }
        }

        //call removeBannedNumbers to remove the numbers that are in the banned list
        removeBannedNumbers();

    }

    /**
     * Remove all numbers that do not appear in any clue.
     *
     */
    public void removeInvalidNumbers() {

        

        HashSet<Integer> allNumbers = getAllNumbersInClues(clues);
        

        System.out.println("all numbers: " + allNumbers.toString());
        
        // the max number of all numbers
        int maxNumber = allNumbers.stream().max(Integer::compare).get();

        for (int i = 0; i < maxNumber; i++) {
            if (!allNumbers.contains(i)) {
                bannedList.add(i);
                if(debug){
                    System.out.println("the number " + i + " is not in any clue");
                }
                
            }
        }

       
    }

    

    public HashSet<Integer> getAllNumbersInClues(List<Clue> clues) {
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

        // print banned list
        System.out.println("the banned list: " + bannedList.toString());

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
        // if the number of numbers that are not nulls in the combination is the same as the number of correct digits then the clue is solved
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
                                System.out.println("combo before: " + clue.getCombinationObject().toString());
                          
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
                            if (debug && inCodeIndexUnknown.contains(clue.getCombination().get(i))) {
                                System.out.println("the number " + clue.getCombination().get(i) + " is already in the inCodeIndexUnknown list");
                            }
                            

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
                        
                            // check if the numbers is in the index banned list of all indexes except one
                            int inHowManyBannedLists = 0;
                            int indexNotBanned = 0;
                            for (int j = 0; j < bannedListPerIndex.size(); j++) {
                               // if (j != i) {
                                    if (bannedListPerIndex.get(j).contains(clue.getCombination().get(i))) {
                                        inHowManyBannedLists++;
                                    } else {
                                        indexNotBanned = j;
                                    }
                           //     }
                            }

                            // if the number is in all the banned lists except one then the number is in the index that is not banned
                            if (inHowManyBannedLists == bannedListPerIndex.size() - 1) {

                                
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

        

        // remove the clues that are solved
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

    private boolean isSolved() {
        removeBannedNumbers();
        // check for any solved clues
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

    /**
     * generate all the possible combinations that can be generated considering the banned list and the banned list per index
     * 
     *
     */
    private void generatePossibleCombinations() {
        // initialize the possible combinations
       possibleCombinations = new ArrayList<>();

        generateCombinations(possibleCombinations, new Integer[code.length], 0);

        System.out.println();
        System.out.println("size of possible combinations before removing banned numbers: " + possibleCombinations.size());


        // remove combinations that doesnt contains ALL the numbers that are in the inCodeIndexUnknown list

        
        for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
            Integer[] combination = possibleCombinations.get(i);
            for (Integer digit : inCodeIndexUnknown) {
                if (!Arrays.asList(combination).contains(digit)) {
                    possibleCombinations.remove(i);
                    break;
                }
            }
        }
        

        

    }

   

    private void generateCombinations(ArrayList<Integer[]> combinations, Integer[] currentCombination, int index) {
        if(index == code.length){
            combinations.add(currentCombination);
            return;
        }

        if(code[index] != null){
            currentCombination[index] = code[index];
            generateCombinations(combinations, currentCombination, index + 1);
            return;
        } 

        for (int digit = 0; digit < 10; digit++) {
            if (!bannedList.contains(digit) && !bannedListPerIndex.get(index).contains(digit)) {

                currentCombination[index] = digit;
                generateCombinations(combinations, currentCombination, index + 1);
            }
        }

    }


    private void useMostCorrectNumberClue(){
        // with this method we will use the clue that has the most correct digits
        // we will test what happeds if we pass in each digit with all clues to determine if we can solve for at least one digits

        // loop trougth the clues and find the clue that has the most correct digits
        int maxCorrectDigits = 0;
        int clueIndex = 0;
        for (int i = 0; i < clues.size(); i++) {
            if (clues.get(i).getCorrectDigits() > maxCorrectDigits) {
                maxCorrectDigits = clues.get(i).getCorrectDigits();
                clueIndex = i;
            }
        }

        List<Clue> mostUseClueAsList = new ArrayList<>();
        mostUseClueAsList.add(clues.get(clueIndex));


        // generate all the possible combinations that can be generated from that clue (alone) with the help of the banned list and the banned list per index
         possibleCombinations = new ArrayList<>();
        
        
         HashSet<Integer> allNumbers = getAllNumbersInClues(mostUseClueAsList);
        

        System.out.println("all numbers: " + allNumbers.toString());
        
        // the max number of all numbers
        int maxNumber = allNumbers.stream().max(Integer::compare).get();

        for (int i = 0; i < maxNumber; i++) {
            if (!allNumbers.contains(i)) {
                bannedList.add(i);
                if(debug){
                    System.out.println("the number " + i + " is not in any clue");
                }
                
            }
        }
        

        
    }

     private void generatePossibleCombinationsFromClue() {
        // initialize the possible combinations
       possibleCombinations = new ArrayList<>();

       // get the numbers that are in the clue

        generateCombinationsFromClues(possibleCombinations, new Integer[code.length], 0);

        System.out.println();
        System.out.println("size of possible combinations before removing banned numbers: " + possibleCombinations.size());


        // remove combinations that doesnt contains ALL the numbers that are in the inCodeIndexUnknown list

        
        for (int i = possibleCombinations.size() - 1; i >= 0; i--) {
            Integer[] combination = possibleCombinations.get(i);
            for (Integer digit : inCodeIndexUnknown) {
                if (!Arrays.asList(combination).contains(digit)) {
                    possibleCombinations.remove(i);
                    break;
                }
            }
        }
        
    }

    private void generateCombinationsFromClues(ArrayList<Integer[]> combinations, Integer[] currentCombination, int index, List<Integer> temp_bannedList, List<List<Integer>> temp_bannedListPerIndex) {
        if(index == code.length){
            combinations.add(currentCombination);
            return;
        }

        if(code[index] != null){
            currentCombination[index] = code[index];
            generateCombinations(combinations, currentCombination, index + 1);
            return;
        } 

        for (int digit = 0; digit < 10; digit++) {
            if (!bannedList.contains(digit) && !bannedListPerIndex.get(index).contains(digit)) {

                currentCombination[index] = digit;
                generateCombinations(combinations, currentCombination, index + 1);
            }
        }

    }

        // a method to check if a combinatuion satisfies a clue
    private boolean combinationSatisfiesClue(Integer[] combination, Clue clue) {
        // check if the combination satisfies the clue
        // check if the number of correct digits is the same as the number of correct digits in the clue
        int numCorrectDigits = 0;
        for (int i = 0; i < combination.length; i++) {
            if (combination[i] != null && clue.getCombination().get(i) != null && combination[i] == clue.getCombination().get(i)) {
                numCorrectDigits++;
            }
        }

        if (numCorrectDigits != clue.getCorrectDigits()) {
            return false;
        }

        // check if the number of well placed digits is the same as the number of well placed digits in the clue
        int numWellPlacedDigits = 0;
        for (int i = 0; i < combination.length; i++) {
            if (combination[i] != null && clue.getCombination().get(i) != null && combination[i] == clue.getCombination().get(i) && combination[i] == code[i]) {
                numWellPlacedDigits++;
            }
        }

        if (numWellPlacedDigits != clue.getWellPlacedDigits()) {
            return false;
        }

        // check if the number of incorrectly placed digits is the same as the number of incorrectly placed digits in the clue
        int numIncorrectlyPlacedDigits = 0;
        for (int i = 0; i < combination.length; i++) {
            if (combination[i] != null && clue.getCombination().get(i) != null && combination[i] == clue.getCombination().get(i) && combination[i] != code[i]) {
                numIncorrectlyPlacedDigits++;
            }
        }

        if (numIncorrectlyPlacedDigits != clue.getIncorrectlyPlacedDigits()) {
            return false;
        }

        return true;
    }   


}
