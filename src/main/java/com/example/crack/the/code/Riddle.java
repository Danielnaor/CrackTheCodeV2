/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

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




   



}


