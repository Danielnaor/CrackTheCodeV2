/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.example.crack.the.code;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danie
 */
public class CrackTheCode {

    public static void main(String[] args) {
        // test the solver with this 

        // test case 1
        ArrayList<Clue> clues = new ArrayList<>();
        clues.add(new Clue(new ArrayList<>(List.of(8, 9, 5, 1)), "Two digits are correct but wrongly placed", 2, 0, 2));
        clues.add(new Clue(new ArrayList<>(List.of(2, 1, 6, 9)), "One digit is correct and well placed and Another digit right but in wrongly placed", 2, 1, 1));
        clues.add(new Clue(new ArrayList<>(List.of(3, 6, 9, 4)), "Two digits are correct, one is well placed and another is wrongly placed", 2, 1, 1));
        clues.add(new Clue(new ArrayList<>(List.of(4, 7, 2, 1)), "Two digits are correct, one is well placed and another is wrongly placed", 2, 1, 1));
        clues.add(new Clue(new ArrayList<>(List.of(1, 2, 3, 7)), "Three digits are right but all are at wrong place.", 3, 0, 3));
        
        // for testing insert nothing is correct with 2, 2, 2, 2
      //  clues.add(new Clue(new ArrayList<>(List.of(2, 2, 2, 2)), "Nothing is correct", 0, 0, 0));
      //          clues.add(new Clue(new ArrayList<>(List.of(3, 7, 1, 9)), "all correct", 4, 4, 0));
        
      ArrayList<Clue> deepCloneClues = new ArrayList<>();
        for (int i = 0; i < clues.size(); i++) {
            deepCloneClues.add(clues.get(i).clone());
        }


        Solver solver = new Solver(clues);
                Integer[] solution = solver.solve();


       


        
        
        System.out.println("Solution: ");
        for (int i = 0; i < solution.length; i++) {
            System.out.print(solution[i] + " ");
        }
    }
}

// test cases

// test case 1
// 8 9 5 1 Two digits are correct but wrongly placed
// 2 1 6 9 One digit is correct and well placed and Another digit right but in wrongly placed
// 3 6 9 4 Two digits are correct, one is well placed and another is wrongly placed
// 4 7 2 1  Two digits are correct, one is well placed and another is wrongly placed
// 1 2 3 7 Three digits are right but all are at wrong place.