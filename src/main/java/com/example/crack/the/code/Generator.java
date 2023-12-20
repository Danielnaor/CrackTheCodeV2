/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

import java.io.File;
import java.io.FileWriter;
import java.net.IDN;
import java.util.*;

/**
 *
 * @author carmitnaor
 */


 /**
  * 
  *   this class will generate riddles for the user that can be following defult settings or custom based on the user's settings of:
  *   difficulty level
  *  length of the code 
    *  number of clues
    * whether or not to have the possibility of repeating digits
    * etc.
  

 */
public class Generator {

    // the default settings for the game
    private int lengthOfCode = 4; // the length of the code will determine the number of digits in the code
    private boolean repeatDigits = false; // whether or not to have the possibility of repeating digits
    // set the max and min values for the code and the clues (defult is 0-9 inclusive)
    private int max = 9;
    private int min = 0;

    // store the clues
    private List<Clue> clues = new ArrayList<Clue>();

    // chances to get all digits correct and no digits correct
    private Integer allDigitsCorrect = 5;
    private Integer noDigitsCorrect = 15;

    // numbers that are correct, number of digits that are correctly placed and incorrectly placed 
    private Integer correctDigits = 0; // number of digits that are correct regardless of placement
    private Integer correctPlacement = 0; // number of digits that are correct and correctly placed
    private Integer incorrectPlacement = 0; // number of digits that are correct but incorrectly placed

    // the number of digits that would be incorrect in the clue (regardless of placement)
    private Integer incorrectDigits = 0;

    // store all the numbers that will not be in the code (be used for incorrect numbers in the clues)
    private HashSet<Integer> incorrectNumbers;


    // store the code
    private Integer[] code;

    // in order to prevent the generator from getting stuck in an infinite loop, we will have a max number of attempts to generate a clue
   // private Integer maxNumberOfAttempts = 1000; // if the generator fails to generate a clue after 1000 attempts, it will throw an error
    // tbd if we will need this var for the generator test

    // define the max number of clues, to prevent a situation where the generator will basically give the user 20 clues for a 3 digit code
    private Integer maxNumberOfClues = 20; // to calc the max number of clues, we will use the following formula: (lengthOfCode + 1) * lengthOfCode





    // empty constructor (defult settings)
    public Generator() {
    }

    // constructor that will allow for custom settings
    public Generator(int lengthOfCode, boolean repeatDigits, int max, int min) {
        this.lengthOfCode = lengthOfCode;
        this.repeatDigits = repeatDigits;
        this.max = max;
        this.min = min;

        maxNumberOfClues = (lengthOfCode + 1) * lengthOfCode;

        
    }

    // builder pattern
    public Generator(Builder builder){
        this.lengthOfCode = builder.lengthOfCode;
        this.repeatDigits = builder.repeatDigits;
        this.max = builder.max;
        this.min = builder.min;
        this.maxNumberOfClues = builder.maxNumberOfClues;

    }

    // new class for builder pattern
    public static class Builder{
        private int lengthOfCode = 4; // the length of the code will determine the number of digits in the code
        private boolean repeatDigits = false; // whether or not to have the possibility of repeating digits
        // set the max and min values for the code and the clues (defult is 0-9 inclusive)
        private int max = 9;
        private int min = 0;

        private Integer  maxNumberOfClues = (lengthOfCode + 1) * lengthOfCode;
        // chances to get all digits correct and no digits correct

        public Builder lengthOfCode(int lengthOfCode){
            this.lengthOfCode = lengthOfCode;
            this.maxNumberOfClues = (lengthOfCode + 1) * lengthOfCode;

            return this;
        }

        

        public Builder repeatDigits(boolean repeatDigits){
            this.repeatDigits = repeatDigits;
            return this;
        }

        public Builder max(int max){
            this.max = max;
            return this;
        }

        public Builder min(int min){
            this.min = min;
            return this;
        }

        public Builder maxNumberOfClues(int maxNumberOfClues){
            this.maxNumberOfClues = maxNumberOfClues;
            return this;
        }


        public Generator build(){
            return new Generator(this);
        }
    }

    /*
     * steps to generate a puzzle/riddle:
     * 1. generate a code
     * 2. generate clues based on the code
     * 3. validate the clues
     * 4. return the clues
     */


     // method to generate a puzzle/riddle
        public Riddle generateRiddle(){
            // generate a code
           code = generateCode();

           // initialize the incorrectNumbers set
              incorrectNumbers = new HashSet<Integer>();

            // add all the numbers that are not in the code to the incorrectNumbers set
            for(int i = min; i <= max; i++){
                boolean inCode = false;
                for(int j = 0; j < lengthOfCode; j++){
                    if(code[j] == i){
                        inCode = true;
                    }
                }
                if(!inCode){
                    incorrectNumbers.add(i);
                }
            }
    
            // generate clues until it is valid
            
            Solver solver = new Solver(clues);

            while(solver.isSolvedCode() == false){
                // generate clues based on the code
               
                // randomize the amount of correct digits in the clue, have 5% chance of having all digits correct and have 15% chance of having no digits correct
                // note: if getting all digits correct we will try to make the placemnt have the biggest incorrect placement.
                // call a custom randomizer
                randomizer();

                // generate a clue based on the number of correct digits, correct placement and incorrect placement (generated by the randomizer)
                Clue clue = generateClue();

    
                // validate the clues
                if(validateClues(clues)){
                    // return the clues
                    return new Riddle.Builder()
                    .code(code)
                    .clues(clues).build();
                }
            }
    
            // validate the clues
            validateClues(clues);
    
            // return the clues
            return new Riddle(code, clues);
        }

    private Clue generateClue() {
        // using the CorrectDigits, CorrectPlacement and IncorrectPlacement variables, generate a clue
        
        // the combimation of the clue:
        Integer[] combination = new Integer[lengthOfCode];


        incorrectDigits = lengthOfCode - correctDigits;
        // first lets deside which digits are correct and which are incorrect
        // randomly choose CorrectDigits digits from the code and make them correct


       Integer[] correctDigitsList = new Integer[this.correctDigits];
       // store the indexes of the correct digits in the code
       Integer[] correctDigitsIndexes = new Integer[this.correctDigits];
    
       for(int i = 0; i < this.correctDigits; i++){
            // randomly choose a index from the code
            int random = (int)(Math.random() * lengthOfCode);
            // if not allowing for repeating digits, make sure the digit is not already in the correctDigits list
            if(repeatDigits == false){
                while(Arrays.asList(correctDigitsList).contains(random)){
                    random = (int)(Math.random() * lengthOfCode);
                }
            }

            // add the digit to the correctDigits list
            correctDigitsList[i] = code[random];

            // add the index of the digit in the code to the correctDigitsIndexes list
            correctDigitsIndexes[i] = random;

        }

        // for debugging purposes make sure the number of correct digits is not greater than the length of the code
        if(this.correctDigits > lengthOfCode){
            throw new Error("Error: the number of correct digits is greater than the length of the code - check the Generator class - generateClue method - the par of the code that places the correct digits");
        }

                    Integer[] incorrectDigitsList = null;


        if(lengthOfCode != this.correctDigits){
            // randomly choose IncorrectDigits digits from the incorrectNumbers set and make them incorrect
            incorrectDigitsList = new Integer[lengthOfCode - this.correctDigits];

            // no need to store the indexes of incorrect digits since they are not in the code and therefore do not have indexes in the code
            for(int i = 0; i < lengthOfCode - this.correctDigits; i++){
                // randomly choose a index from the incorrectNumbers set
                int random = (int)(Math.random() * incorrectNumbers.size());
                // if not allowing for repeating digits, make sure the digit is not already in the incorrectDigits list
                if(repeatDigits == false){
                    while(Arrays.asList(incorrectDigitsList).contains(random)){
                        random = (int)(Math.random() * incorrectNumbers.size());
                    }
                }

                // add the digit to the incorrectDigits list
                incorrectDigitsList[i] = incorrectNumbers.toArray(new Integer[incorrectNumbers.size()])[random];
            }
        }
        

        // now lets deside which digits are correctly placed and which are incorrectly placed
        // use checkIfComboValid - from the Validator class to check if the combo is valid

        // let first place the correct digits that are correctly placed
        if(this.correctPlacement > 0){
            for(int i = 0; i < this.correctPlacement; i++){
                // randomly choose a index from the correctDigits list
                int randomIndex = (int)(Math.random() * correctDigitsList.length);


                // for debugging purposes make sure the index is not already taken (should not happen) and if it is throw a new error
                if(combination[correctDigitsIndexes[randomIndex]] != null){
                    throw new Error("Error: randomIndex is already taken - (should not happen) - check the Generator class - generateClue method - the par of the code that places the correct digits that are correctly placed");
                }

                // place the digit in the correct index in the combination list
                combination[correctDigitsIndexes[randomIndex]] = correctDigitsList[randomIndex];


            }
        }

        // for debuuiging purposes make sure the number of available indexes is not less than the number of correct digits that are incorrectly placed
        if(lengthOfCode - this.correctPlacement < this.incorrectPlacement){
            throw new Error("Error: the number of available indexes is less than the number of correct digits that are incorrectly placed - check the Generator class - generateClue method - the par of the code that places the correct digits that are incorrectly placed");
        }
        // note to self: if ever adding Logger - add to log in the info catgory if the number of available indexes is less than the number of correct digits that are incorrectly placed

        // now lets place the correct digits that are incorrectly placed
        if(this.incorrectPlacement > 0){
            for(int i = 0; i < this.incorrectPlacement; i++){
                // randomly choose a index from the correctDigits list
                int randomIndex = (int)(Math.random() * correctDigitsList.length);

                // randomly choose a index from the combination list that is not already taken
                int randomIndex2 = (int)(Math.random() * combination.length);
                // since we are placing the correct digits that are incorrectly placed, we need to make sure that the index is not the same as the index of the correct digit in the code and we need to make sure that the index is not already taken
                while(combination[randomIndex2] != null || randomIndex2 == correctDigitsIndexes[randomIndex]){
                    randomIndex2 = (int)(Math.random() * combination.length);
                }

                // place the digit in the correct index in the combination list
                combination[randomIndex2] = correctDigitsList[randomIndex];                
            }
        }


        // for debugging purposes make sure the number of available indexes is not less than the number of incorrect digits
        if(lengthOfCode - this.correctPlacement - this.incorrectPlacement < this.incorrectDigits){
            throw new Error("Error: the number of available indexes is less than the number of incorrect digits - check the Generator class - generateClue method - the par of the code that places the incorrect digits");
        }

        // now lets place the incorrect digits
        if(this.incorrectDigits > 0){
            // assert that the number of indexes that are not taken is equal to the number of incorrect digits
            int numberOfIndexesNotTaken = 0;
            // also store the indexes that are not taken
            
            Integer[] indexesNotTaken = new Integer[lengthOfCode - this.correctDigits]; // correctDigits is the number of correct digits regardless of placement and equals to correctPlacement + incorrectPlacement
            for(int i = 0; i < combination.length; i++){
                if(combination[i] == null){
                    numberOfIndexesNotTaken++;
                    indexesNotTaken[i] = i;
                }
            }

            if(numberOfIndexesNotTaken != incorrectDigitsList.length || numberOfIndexesNotTaken < incorrectDigitsList.length){
                throw new Error("Error: the number of indexes that are not taken is not equal to the number of incorrect digits - check the Generator class - generateClue method - the par of the code that places the incorrect digits");
            }

            // put the incorrect digits in the indexes that are not taken
            for(int i = 0; i < numberOfIndexesNotTaken; i++){
                // use the indexesNotTaken array to get the index that is not taken
                int randomIndex = (int)(Math.random() * indexesNotTaken.length);
                
                // place the digit in the correct index in the combination list
                combination[indexesNotTaken[randomIndex]] = incorrectDigitsList[i];

                // remove the index from the indexesNotTaken array
                indexesNotTaken[randomIndex] = null;
            }


        }

        // for debugging purposes make sure the number of available indexes is 0
        int numberOfIndexesNotTaken = 0;
        for(int i = 0; i < combination.length; i++){
            if(combination[i] == null){
                numberOfIndexesNotTaken++;
            }
        }
        if(numberOfIndexesNotTaken != 0){
            throw new Error("Error: the number of available indexes is not 0 - check the Generator class - generateClue method - the par of the code that places the incorrect digits");
        }

        // for debugging purposes make sure the combination is valid
        Validator validator = new Validator();

        

        Clue clue = new Clue.Builder()
        .combination(Arrays.asList(combination))
        .hintMessage("correctDigits: " + correctDigits + " correctPlacement: " + correctPlacement + " incorrectPlacement: " + incorrectPlacement)
        .correctDigits(correctDigits)
        .wellPlacedDigits(correctPlacement)
        .incorrectlyPlacedDigits(incorrectPlacement)
        .build();

       

        if(!validator.checkIfComboValid(combination, clue)){
            throw new Error("Error: the combination is not valid - check the Generator class - generateClue method - the par of the code that places the incorrect digits");
        }

        return  clue;

        
    }

    // generate a code
    public Integer[] generateCode(){
        // generate a code that is the length specified by the variable lengthOfCode
        Integer[] code = new Integer[lengthOfCode];

        // generate a random code, make sure there are no repeating digits unless repeatDigits is true and store it in the code array. the numbers in the code will be between the min and max values
        for(int i = 0; i < lengthOfCode; i++){
            int random = (int)(Math.random() * (max - min + 1) + min);
            if(repeatDigits){
                code[i] = random;
            }else{
                // check if the random number is already in the code
                for(int j = 0; j < i; j++){
                    if(code[j] == random){
                        // if the random number is already in the code, generate a new random number and check again
                        random = (int)(Math.random() * (max - min + 1) + min);
                        j = 0;
                    }
                }
                code[i] = random;
            }
        }

        return code;
        

    }


    // custom randomizer to determine the amount of correct digits and the correctness of the placement of the digits
    // note: if getting all digits correct we will try to make the placemnt have the biggest incorrect placement.
    public void randomizer(){

        // randomize the amount of correct digits in the clue, have 5% chance of having all digits correct and have 15% chance of having no digits correct
           int random = (int)(Math.random() * 100 + 1);
           if(random <= allDigitsCorrect){
               // have all digits correct
               correctDigits = lengthOfCode;
              // have as many digits as possible be incorrectly placed
                correctPlacement = 0;
                
                // if lengthOfCode is even, all digits will be incorrectly placed, if lengthOfCode is odd, all digits except for one will be incorrectly placed
                if(lengthOfCode % 2 == 0){
                    incorrectPlacement = lengthOfCode;
                }else{
                    incorrectPlacement = lengthOfCode - 1;
                }
                correctPlacement = correctDigits - incorrectPlacement;

              }else if(random <= noDigitsCorrect){
                    // have no digits correct
                    correctDigits = 0;
                    correctPlacement = 0;
                    incorrectPlacement = lengthOfCode;
                }else{
                    // determine the amount of correct digits which will be between 1 and lengthOfCode - 1
                    correctDigits = (int)(Math.random() * (lengthOfCode - 1) + 1);
                    
                    // detemine the amount of digits that are correctly placed which will be between 0 and correctDigits
                    correctPlacement = (int)(Math.random() * (correctDigits + 1));

                    // number of digits that are incorrectly placed will be the difference between correctDigits and correctPlacement
                    incorrectPlacement = correctDigits - correctPlacement;

                }
        }

    // validate the clues
    public boolean validateClues(List<Clue> clues){
        // validate the clues

        //initialize the validator with the Builder pattern
        Validator validator = new Validator.Builder()
        .code(code)
        .clues(clues)
        .build();
        

        // for debugging purposes make sure the combination is valid for individual clues
        for(int i = 0; i < clues.size(); i++){
            Integer[] combination = new Integer[lengthOfCode];
            for(int j = 0; j < lengthOfCode; j++){
                combination[j] = clues.get(i).getCombination().get(j);
            }




            if(!validator.checkIfComboValid(combination, clues.get(i))){
               // throw new Error("Error: the combination is not valid - check the Generator class - validateClues method - the par of the code that places the incorrect digits");
                // before we will throw an error, lets create a folder for the errors, then create a folder named after the date and time we run this, then create mutiple files:
                // 1. file for all clues
                // 2. the attempted combination
                // 3. the clue that is not valid
                // 4. the error message
                // 5. all of the above in one file

                // create a folder for the errors if it does not exist
                File errorsFolder = new File("errors");
                if(!errorsFolder.exists()){
                    errorsFolder.mkdir();
                }

                // create a folder for the errors of this run if it does not exist
                Date date = new Date();
                File errorsOfThisRunFolder = new File("errors/" + date.toString());
                if(!errorsOfThisRunFolder.exists()){
                    errorsOfThisRunFolder.mkdir();
                }

                // create a file for all clues
                File allCluesFile = new File("errors/" + date.toString() + "/allClues.txt");
                if(!allCluesFile.exists()){
                    try{
                        allCluesFile.createNewFile();
                    }catch(Exception e){
                        System.out.println("Error: could not create the allCluesFile");
                    }
                }

                // create a file for the attempted combination
                File attemptedCombinationFile = new File("errors/" + date.toString() + "/attemptedCombination.txt");
                if(!attemptedCombinationFile.exists()){
                    try{
                        attemptedCombinationFile.createNewFile();
                    }catch(Exception e){
                        System.out.println("Error: could not create the attemptedCombinationFile");
                    }
                }

                // create a file for the clue that is not valid and then also the attempted combination
                File clueThatIsNotValidFile = new File("errors/" + date.toString() + "/clueThatIsNotValid.txt");
                if(!clueThatIsNotValidFile.exists()){
                    try{
                        clueThatIsNotValidFile.createNewFile();
                    }catch(Exception e){
                        System.out.println("Error: could not create the clueThatIsNotValidFile");
                    }
                }

                // create a file for the error message
                File errorMessageFile = new File("errors/" + date.toString() + "/errorMessage.txt");
                if(!errorMessageFile.exists()){
                    try{
                        errorMessageFile.createNewFile();
                    }catch(Exception e){
                        System.out.println("Error: could not create the errorMessageFile");
                    }
                }

                // create a file for all of the above
                File allOfTheAboveFile = new File("errors/" + date.toString() + "/allOfTheAbove.txt");
                if(!allOfTheAboveFile.exists()){
                    try{
                        allOfTheAboveFile.createNewFile();
                    }catch(Exception e){
                        System.out.println("Error: could not create the allOfTheAboveFile");
                    }
                }

                // write to the allCluesFile
                String allClueString = "all clues: ";
                for(int j = 0; j < clues.size(); j++){
                    allClueString += "clue #" + (j + 1) + ": \n" + clues.get(j).toString() + "\n";
                }
                writeToFile(allCluesFile, allClueString);

                // write to the attemptedCombinationFile
                String attemptedCombinationString = "attempted combination: ";
                for(int j = 0; j < lengthOfCode; j++){
                    attemptedCombinationString += combination[j] + " ";
                }
                writeToFile(attemptedCombinationFile, attemptedCombinationString);

                // write to the clueThatIsNotValidFile
                String clueThatIsNotValidString = "clue that is not valid: ";
                for(int j = 0; j < lengthOfCode; j++){
                    clueThatIsNotValidString += clues.get(i).getCombination().get(j) + " ";
                }
                writeToFile(clueThatIsNotValidFile, clueThatIsNotValidString);


                // write to the errorMessageFile --> write the error message and then a few lines below write the debugMessages from the validator
                String errorMessageString = "";
                errorMessageString += "Error: the combination is not valid - check the Generator class - validateClues method - the par of the code that places the incorrect digits" + "\n\n";
                errorMessageString += validator.getDebugMessages();
                writeToFile(errorMessageFile, errorMessageString);


                // write to the allOfTheAboveFile
                String allOfTheAboveString = "";
                allOfTheAboveString += allClueString + "\n\n";
                allOfTheAboveString += attemptedCombinationString + "\n\n";
                allOfTheAboveString += clueThatIsNotValidString + "\n\n";
                allOfTheAboveString += errorMessageString;
                writeToFile(allOfTheAboveFile, allOfTheAboveString);





            }


        }

        return true;
        
    }

    /**
     * write to a file
     * @param file the file to write to
     * @param text the text to write to the file
     */
    public void writeToFile(File file, String text){
        try{
            // create a new scanner
            Scanner scanner = new Scanner(file);

            // create a new string builder
            StringBuilder stringBuilder = new StringBuilder();

            // add the text to the string builder
            stringBuilder.append(text);

            // add a new line to the string builder
            stringBuilder.append("\n");

            // add the text from the file to the string builder
            while(scanner.hasNextLine()){
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }

            // close the scanner
            scanner.close();

            // create a new file writer
            FileWriter fileWriter = new FileWriter(file);

            // write to the file
            fileWriter.write(stringBuilder.toString());

            // close the file writer
            fileWriter.close();

        }catch(Exception e){
            System.out.println("Error: could not write to the file");
        }
    }
}


// gui that will allow for:
// an X button that will allow for: 
    //the delition of singal digits from clues
    //the deletion of entire clues
// an erase button that will revese the action of the X button
// a submit button that will submit the guess
// a button that will allow for the user to change the settings of the game


// a new class that will store the code and the clues and the assets of a Crack the Code will be called: 
// a. Ridle
// b. Puzzle
// c. Crack the Code
// d. Game
// correct answer is: a. Riddle

