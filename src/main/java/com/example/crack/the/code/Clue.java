package com.example.crack.the.code;

import java.util.List;

public class Clue {
   // private static Combination combination;
   private  Combination combination;
    private String hintMessage;
    private int correctDigits;
    private int wellPlacedDigits;
    private int incorrectlyPlacedDigits;

    // might make a seperate variable for the combination object

    public Clue(List<Integer> combination, String hintMessage, int correctDigits, int wellPlacedDigits, int incorrectlyPlacedDigits) {
        this.combination = new Combination(combination);
        this.hintMessage = hintMessage;
        this.correctDigits = correctDigits;
        this.wellPlacedDigits = wellPlacedDigits;
        this.incorrectlyPlacedDigits = incorrectlyPlacedDigits;
    }

    public Combination getCombinationObject() {
        return combination;
    }

    public List<Integer> getCombination() {
        return combination.combination;
    }

    public String getHintMessage() {
        return hintMessage;
    }

    public int getCorrectDigits() {
        return correctDigits;
    }

    public int getWellPlacedDigits() {
        return wellPlacedDigits;
    }

    public int getIncorrectlyPlacedDigits() {
        return incorrectlyPlacedDigits;
    }

    public void setCombination(List<Integer> combination) {
        if(this.combination == null || this.combination.numNotNull == null){
            this.combination = new Combination(combination);
        } else {
            this.combination.combination = combination;
        }
    }

    public void setCombinationObject(Combination combination) {
        this.combination = combination;
    }

    public void setHintMessage(String hintMessage) {
        this.hintMessage = hintMessage;
    }

    public void setCorrectDigits(int correctDigits) {
        this.correctDigits = correctDigits;
    }

    public void setWellPlacedDigits(int wellPlacedDigits) {
        this.wellPlacedDigits = wellPlacedDigits;
    }

    public void setIncorrectlyPlacedDigits(int incorrectlyPlacedDigits) {
        this.incorrectlyPlacedDigits = incorrectlyPlacedDigits;
    }

    // clone
    public Clue clone() {
        return new Clue(combination.combination, hintMessage, correctDigits, wellPlacedDigits, incorrectlyPlacedDigits);
    }

    // You can add more methods as needed for additional functionality

    @Override
    public String toString() {
        return combination.toString() + " " + hintMessage +
                " (Correct: " + correctDigits +
                ", Well Placed: " + wellPlacedDigits +
                ", Incorrectly Placed: " + incorrectlyPlacedDigits + ")";
    }
}
