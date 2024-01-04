/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author carmitnaor
 */
public class Riddle {

    /*
     * this class will store all the diffrent components of a crack the code riddle
     * including the code, the clues, the number of guesses
     * 
     * 
     */

    // the code
    private Integer[] code;

    // the clues
    private List<Clue> clues;

    

    public Riddle() {
    }

    public Riddle(Integer[] code, List<Clue> clues) {
        this.code = code;
        this.clues = clues;
    }

    // builder pattern
    public Riddle(Builder builder) {
        this.code = builder.code;
        this.clues = builder.clues;
    }


    public static Builder builder() {
        return new Builder();
    }



    // builder pattern
    public static class Builder {

        // the code
        private Integer[] code;

        // the clues
        private List<Clue> clues;

        public Builder() {
        }

        public Builder code(Integer[] code) {
            this.code = code;
            return this;
        }

        public Builder clues(List<Clue> clues) {
            this.clues = clues;
            return this;
        }

        public Riddle build() {
            return new Riddle(code, clues);
        }

    }

    public Integer[] getCode() {
        return code;
    }

    public void setCode(Integer[] code) {
        this.code = code;
    }

    public List<Clue> getClues() {
        return clues;
    }

    public void setClues(List<Clue> clues) {
        this.clues = clues;
    }

    

    @Override
    public String toString() {
        return "Riddle{" + "code=" + code + ", clues=" + clues + '}';
    }

    /**
         * Will return if the riddle could be solved using the clues (will attempt to solve using the Solver class) 
         * Will only return true if the riddle could be solved using the clues and the Solver class solved it correctly
         * @return 
         */
    public boolean isSolvable() {
        Solver solver = new Solver(clues);
        
        // boolean - stores whether the Solver class was able to solve for any code (regadless if solved correctly) - name should be more specific then isSolved
        boolean SolverHasSolved = solver.isSolved();

        // if the Solver class was able to solve for any code check whether it solved correctly
        if (SolverHasSolved) {
            // the code the solver was able to solve for
            Integer[] solverCode = solver.getCode();

            // check if the code the solver solved for is the same as the code of the riddle
            if (Arrays.equals(solverCode, code)) {
                // verify that the code is valid with clues using the validatior 
                Validator validator = new Validator.Builder()
                        .code(code)
                        .clues(clues)
                        .build();

                    //   validator.isCodeValid(); 
                    boolean isCodeValid = validator.isCodeValid(code);

                    if(isCodeValid){
                        return true;
                    } else{
                        // throw new IllegalStateException
                        throw new IllegalStateException("The code is not valid with the clues");
                    }
                
            } else {
                // false and issue a warning
                System.out.println("Warning: the Solver class was able to solve for a code but it was not the correct code");
                // print reminder to change this to logger  with file and line
                System.out.println("Reminder: change this to logger (printed by: riddle.java" + System.out.getClass().getName() + " line: " + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
                return false;
            }
        } else{
            return false;
        }
    }



   



}


