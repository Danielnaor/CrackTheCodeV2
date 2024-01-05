/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(Riddle.class.getName());
    

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
        return "Riddle{" + "code=" + Arrays.toString(code) + ", clues=" + clues + '}';
    }

    /**
     * Will return if the riddle could be solved using the clues (will attempt to solve using the Solver class) 
     * Will only return true if the riddle could be solved using the clues and the Solver class solved it correctly
     * @return 
     */
    public boolean isSolvable() {
        Solver solver = new Solver(clues);
        boolean solverHasSolved = solver.isSolved();

        if (solverHasSolved) {
            Integer[] solverCode = solver.getCode();

            if (Arrays.equals(solverCode, code)) {
                Validator validator = new Validator.Builder()
                        .code(code)
                        .clues(clues)
                        .build();

                boolean isCodeValid = validator.isCodeValid(code);

                if (isCodeValid) {
                    return true;
                } else {
                    logger.severe("The code is not valid with the clues");
                    throw new IllegalStateException("The code is not valid with the clues");
                }
            } else {
                logger.warning("Warning: the Solver class was able to solve for a code but it was not the correct code");
                logger.warning("Reminder: change this to logger (printed by: Riddle.java " + getClass().getName() + " line: " + Thread.currentThread().getStackTrace()[1].getLineNumber() + ")");
                return false;
            }
        } else {
            return false;
        }
    }
}