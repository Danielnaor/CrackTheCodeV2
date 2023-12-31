package com.example.crack.the.code;

import java.util.List;

public class Clue {
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

    // builder pattern
    public Clue(Builder builder){
        this.combination = builder.combination;
        this.hintMessage = builder.hintMessage;
        this.correctDigits = builder.correctDigits;
        this.wellPlacedDigits = builder.wellPlacedDigits;
        this.incorrectlyPlacedDigits = builder.incorrectlyPlacedDigits;
    }


     // builder pattern
     public static class Builder {
        private Combination combination;
        private String hintMessage;
        private int correctDigits;
        private int wellPlacedDigits;
        private int incorrectlyPlacedDigits;

        public Builder() {
        }

        public Builder combination(List<Integer> combination) {
            this.combination = new Combination(combination);
            return this;
        }

        public Builder combination(Combination combination) {
            this.combination = combination;
            return this;
        }

        public Builder hintMessage(String hintMessage) {
            this.hintMessage = hintMessage;
            return this;
        }

        public Builder correctDigits(int correctDigits) {
            this.correctDigits = correctDigits;
            return this;
        }

        public Builder wellPlacedDigits(int wellPlacedDigits) {
            this.wellPlacedDigits = wellPlacedDigits;
            return this;
        }

        public Builder incorrectlyPlacedDigits(int incorrectlyPlacedDigits) {
            this.incorrectlyPlacedDigits = incorrectlyPlacedDigits;
            return this;
        }

        public Clue build() {
            return new Clue(this);
        }
    }

    // getters and setters
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Clue)) {
            return false;
        }
        Clue other = (Clue) obj;
        return combination.equals(other.combination) &&
                hintMessage.equals(other.hintMessage) &&
                correctDigits == other.correctDigits &&
                wellPlacedDigits == other.wellPlacedDigits &&
                incorrectlyPlacedDigits == other.incorrectlyPlacedDigits;
    }

   
}
