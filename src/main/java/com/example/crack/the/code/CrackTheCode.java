/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.example.crack.the.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author danie
 */
public class CrackTheCode {

    public static void main(String[] args) throws IOException {
        ArrayList<Clue> clues = new ArrayList<>();

        // test case 1:
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(9, 2, 8, 5)), "one number is correct but wrong placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 9, 3, 7)), "two numbers are correct but wrong placed", 2, 0, 2));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(5, 2, 0, 1)), "one number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(6, 5, 0, 7)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(8, 5, 2, 4)), "two numbers are correct but wrong placed", 2, 0, 2));
        // solution: 3, 8 , 4 , 1
        Solver solver = new Solver(clues);
        Integer[] solution = {3, 8, 4, 1};
        Integer[] solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 1);

        // test case 2:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(3, 6, 8)), "one number is correct and correctly placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(3, 8, 7)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 7, 6)), "one number is correct but wrongly placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(4, 7, 1)), "two numbers are correct but wrongly placed", 2, 0, 2));
        // solution: 1, 6, 4

        solver = new Solver(clues);
        solution = new Integer[]{1, 6, 4};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 2);


        // test case 3:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(3, 4, 2)), "one number is correct but wrongly placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 7, 3)), "one number is correct but wrongly placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 6, 5)), "two numbers are correct and correctly placed", 2, 2, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(8, 5, 3)), "two numbers are correct but wrongly placed", 2, 0, 2));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 6, 4)), "nothing is correct", 0, 0, 0));
        // solution: 1, 3, 5
        solver = new Solver(clues);
        
        solution = new Integer[]{1, 3, 5};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 3);

        // test case 4:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(0, 1, 2)), "one number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 0)), "one number is correct but wrongly placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 8, 0)), "one number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(3, 1, 4)), "one number is correct but wrongly placed", 1, 0, 1));
        
        // solution: 4, 8, 2
        solver = new Solver(clues);
        solution = new Integer[]{4, 8, 2};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 4);

        // test case 5:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(8, 9, 5, 1)), "Two digits are correct but wrongly placed", 2, 0, 2));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 1, 6, 9)), "One digit is correct and well placed and Another digit right but in wrongly placed", 2, 1, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(3, 6, 9, 4)), "One digit right and in the right place. Another digit right but in wrong place.", 2, 1, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(4, 7, 2, 1)), "One digit right and in the right place. Another digit right but in wrong place.", 2, 1, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 7)), "Three digits are right but all are at wrong place.", 3, 0, 3));

        // solution: 3, 7, 1, 9
        solver = new Solver(clues);
        solution = new Integer[]{3, 7, 1, 9};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 5);

        // test case 6:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 3, 4)), "Two Numbers are correct. One well placed and other wrongly placed.", 2, 1, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(5, 6, 7)), "Nothing is Correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(5, 6, 4)), "One Number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(4, 0, 1)), "Two Numbers are correct but wrongly placed.", 2, 0, 2));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 2, 4)), "Two Numbers are correct. One well placed and other wrongly placed.", 2, 1, 1));
        
        // solution: 3, 1, 4
        solver = new Solver(clues);
        solution = new Integer[]{3, 1, 4};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 6);

        // test case 7:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(5, 6, 7)), "One Number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(4, 3, 5)), "Nothing is Correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(0, 6, 7)), "Two Numbers are correct and well placed", 2, 2, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 0, 2)), "One Number is correct and correctly placed.", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(7, 2, 1)), "One number is correct but wrongly placed", 1, 0, 1));
        
        // solution: 0, 0, 7
        solver = new Solver(clues);
        solution = new Integer[]{0, 0, 7};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 7);

        // test case 8:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)), "One number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 0, 2, 3)), "one number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 1, 0, 3)), "one number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 7)), "one number is correct and well placed", 1, 1, 0));
        
        // solution: 0, 0, 0, 7
        solver = new Solver(clues);
        solution = new Integer[]{0, 0, 0, 7};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 8);

        // test case 9:
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)), "One number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 0, 2, 3)), "one number is correct and well placed", 1, 1, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 3, 2, 0)), "one number is correct but wrongly placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 7, 2, 3)), "one number is correct but wrongly placed", 1, 0, 1));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(7, 4, 5, 0)), "two numbers are correct but wrongly placed", 2, 0, 2));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(5, 4, 7, 7)), "two numbers are correct and well placed", 2, 2, 0));
        // solution: 0, 0, 7, 7
       
        solver = new Solver(clues);
        solution = new Integer[]{0, 0, 7, 7};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 9);

// test case 10: 
        clues.clear();
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(2, 2, 2, 2)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(3, 3, 3, 3)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(4, 4, 4, 4)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(5, 5, 5, 5)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(6, 6, 6, 6)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(7, 7, 7, 7)), "nothing is correct", 0, 0, 0));
        clues.add(new Clue(new ArrayList<Integer>(Arrays.asList(8, 8, 8, 8)), "nothing is correct", 0, 0, 0));
// solution: 9, 9, 9, 9
        solver = new Solver(clues);
        solution = new Integer[]{9, 9, 9, 9};
        solutionSolver = solver.solve();
        passOrFail(solution, solutionSolver, 10);

    }

    // pass or fail tests - void
    public static void passOrFail(Integer[] solution, Integer[] solutionSolver, int numTestCases) {
        if (Arrays.equals(solutionSolver, solution)) {
            System.out.println("Pass test case " + numTestCases);
        } else {
            System.out.println("Fail test case " + numTestCases);
            // expected solution
            System.out.println("Expected solution: " + Arrays.toString(solution));
            // actual solution
            System.out.println("Actual solution: " + Arrays.toString(solutionSolver));
        }
    }
}

// test case 1:
// 9, 2, 8, 5 one number is correct but wrong placed
// 1, 9, 3, 7 two numbers are correct but wrong placed
// 5, 2, 0, 1 one number is correct and well placed
// 6, 5, 0, 7 nothing is correct
// 8, 5, 2, 4 two numbers are correct but wrong placed
// solution: 3, 8 , 4 , 1

// test case 2:
// 3, 6, 8 one number is correct and correctly placed
// 3, 8, 7 nothing is correct
// 2, 7, 6 one number is correct but wrongly placed
// 4, 7, 1 two numbers are correct but wrongly placed
// solution: 1, 6, 4

// test case 3:
// 3, 4, 2 one number is correct but wrongly placed
// 2, 7, 3 one number is correct but wrongly placed
// 1, 6, 5 two numbers are correct and correctly placed
// 8, 5, 3 two numbers are correct but wrongly placed
// 2, 6, 4 nothing is correct
// solution: 1, 3, 5

// test case 4:
// 0, 1, 2 one number is correct and well placed
// 2, 3, 0 one number is correct but wrongly placed
// 1, 8, 0 one number is correct and well placed
// 3, 1, 4 one number is correct but wrongly placed
// solution: 4, 8, 2

// test case 5:
// 8 9 5 1 Two digits are correct  but wrongly placed
// 2 1 6 9 One digit is correct and well placed and Another digit right but in wrongly placed
// 3 6 9 4 One digit right and in the right place. Another digit right but in wrong place.
// 4 7 2 1 One digit right and in the right place. Another digit right but in wrong place.
// 1 2 3 7 Three digits are right but all are at wrong place.
// solution: 3, 7, 1, 9

// test case 6:
// 2,3,4 Two Numbers are correct. One well placed and other wrongly placed.
// 5,6,7 Nothing is Correct
// 5,6,4 One Number is correct and well placed
// 4,0,1 Two Numbers are correct but wrongly placed.
// 1,2,4 Two Numbers are correct. One well placed and other wrongly placed.
// solution: 3, 1, 4 

// test case 7:
// 5,6,7 One Number is correct and well placed
// 4,3,5 Nothing is Correct
// 0,6,7 Two Numbers are correct and well placed
// 1,0,2 One Number is correct and correctly placed.
// 7,2,1 One number is correct but wrongly placed 
// solution: 0, 0, 7

// test case 8:
// 0 1 2 3 One number is correct and well placed
// 1 0 2 3 one number is correct and well placed
// 2 1 0 3 one number is correct and well placed
// 1 2 3 4 nothing is correct
// 1 2 3 7 one number is correct and well placed
// solution: 0, 0, 0, 7

// test case 9:
// 0 1 2 3 One number is correct and well placed
// 1 0 2 3 one number is correct and well placed
// 1 3 2 0 one number is correct but wrongly placed
// 1 7 2 3 one number is correct but wrongly placed
// 7 4 5 0 two numbers are correct but wrongly placed
// 5 4 7 7 two numbers are correct and well placed
// solution: 0, 0, 7, 7

// test case 10: 
// 0 0 0 0 nothing is correct
// 1 1 1 1 nothing is correct
// 2 2 2 2 nothing is correct
// 3 3 3 3 nothing is correct
// 4 4 4 4 nothing is correct
// 5 5 5 5 nothing is correct
// 6 6 6 6 nothing is correct
// 7 7 7 7 nothing is correct
// 8 8 8 8 nothing is correct
// solution: 9, 9, 9, 9

