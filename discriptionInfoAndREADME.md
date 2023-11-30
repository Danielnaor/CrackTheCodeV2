<!-- this file will be used to create a high level planing document for a project using ChatGPT. 
The project will be able to generate Crack the Code puzzles. 
The project will also be have a Solver that can solve Crack the Code puzzles.
-->


# Crack the Code 
## What is Crack the Code?
Crack the Code is a puzzle game that contains combinations/sets of X numbers , Corresponding to these combinations of numbers are hints are given regarding the correctness of these digits/combinations. The goal of the puzzle is to decode the correct code using the combinations of numbers and the hints given. The puzzle is solved when the correct code is decoded.

Each pair of a combination and its corresponding hint forms a clue. 

A puzzle will be composed of a multiple clues.

## Examples of a Crack the Code puzzle

### Example 1

```
3 6 8 one number is correct and well placed 
3 8 7 Nothing is correct
2 7 6 One number is correct but wrongly placed
4 7 1 Two numbers are correct but wrongly placed
```
The correct code is 1 6 4

### Example 2

```
3 4 2 One number is correct but wrongly placed
2 7 3 One number is correct but wrongly placed
1 6 5 Two numbers are correct and well placed
8 5 3 Two numbers are correct but wrongly placed
2 6 4 Nothing is correct
```

The correct code is 1 3 5


## Modules of the project

The project will be composed of two modules:

1. A solver that will solve Crack the Code puzzles.
2. A generator that will generate Crack the Code puzzles.

## Solver

The solver will solve Crack the Code puzzles.
the features of the solver will be as follows:
1. The solver will be able to solve puzzles given a set of clues.
2. the length of the code will be determined by the length of the combinations in the clues.
3. the solver will validate that the code is correct by checking if the code satisfies all the clues.

## Generator
The generator will generate Crack the Code puzzles. 
The features of the generator are:
1. The generator will be able to generate puzzles with a code with length (the combination will also have the same length) specified by the user.
2. The generator will be able to generate puzzles with a number of clues specified by the user.

## How are the puzzles solved?
The puzzles are solved by using the clues to eliminate the possible combinations of the code. The solver will use the clues to eliminate the possible combinations of the code to get the least number of possible combinations. The solver will then check if the code satisfies all the clues. If the code satisfies all the clues then the code is the correct code. 

### How the solver eliminates possible combinations

The way the solver eliminates possible combinations is by using the clues to eliminate the possible combinations. it will do so by the following steps:
1. find all the clues that that thier combinations are nothing is correct and eliminate all the instances of the digits in the combinations of the clues from the possible combinations.
for example if the clue is 1 2 3 nothing is correct and a clue is 1 2 4 one number is correct and well placed the code will replace the 1 and 2 in the clue with -1 to indicate that the numbers are not valid. 
2. then the solver will check if there is any clue that all by the am