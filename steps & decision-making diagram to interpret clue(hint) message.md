# Steps, logic, and/or decision-making process and diagram to interpret clue(hint) message

<!-- steps required to interpret the clue/hint's message - create a section steps using the following template: -->

## Steps to interpret clue(hint) message
<!-- create a list with syntax: step number. step description. -->
- step 1 
    - (do only for the first clue.)
    - try to identify the delimiter of the combination given in the clue.
    - make sure that if there is only one space after a number and the delimiter is not just a space, then the number is not a part of the combination.

- step 2
    - use the information obtained in step 0 to seperate the combinatoin and the clue/hint's message.
    
<!-- now come a sub-decision making prosess - to interpret the clue/hint's message -->

## A decision making diagram to interpret clue(hint) message
<!-- 
if first index of the message is a number, then skeip to step 6.

Step 1. Check if the first word of the message is a number written in words (e.g. one, two, three, etc.)
    - if yes, then repalce the word with the number.
    - if no, then skip to step 2.

Step 2. check if the first word is 'Nothing'
    - if yes, need to make sure if nothing is correct and go to step 3.5. 
    - if no, just keep going.

Step 3. if the first word is 'All'
    - if yes, skip to step 3.5. 
    - if no, then skip to step 4.

Step 3.5. check if it's all correct or all incorrect
    - if yes, then go to step 4.
    - if no, then go to step 4. 

Step 4. check if the second word is 'but' or 'except'
    - if either is in, go to step 5.
    - if neither is in, then go to step 6.

Step 5. check if the third word is a number written in words (e.g. one, two, three, etc.)
    - if yes, we will use the number of digits in the combo - the number in the third word as the number of digits in the combination, and then they will either be all correct or all incorrect.
    - if no, then skip to step 5.5

Step 5.5. output - problems and attempt to use step 6.
 
Step 6. check how many numbers are in the message - example of a message with 2 numbers '2.... 1....'
    - if 3 numbers, 
        - find max number 
            - for debug: if not at index 0
                - check if it's the first number that appears in the message
                    - if yes, print that the number is not at index 0 but it's the first number that appears in the message. -- no throw exception yet
                    - if no, print that the number is not at index 0 and it's not the first number that appears in the message. -- no throw exception yet
            - make sure the 2 other numbers add up to the max number
                - if yes, then max means the number of correct digits in the combination, and the other 2 numbers mean the number of digits that are correct and are correctly placed and the number of digits that are correct but are not correctly placed. now we need to identify which is which.
                    - for now, we will assume that the first number is the number of digits that are correct and are correctly placed and the second number is the number of digits that are correct but are not correctly placed.
                - if no, then throw exception.

use "` to warp the text in the decision making diagram.
example: |maybe| E["`A text that needs to be wrapped to another line`"];
  
  <div style="height: 300px; overflow: auto; font-size: 1.2em;">

  <div style="max-height: 300px; overflow: auto; white-space: nowrap;">
-->
<details>
  <summary>Click to expand</summary>

  <div style="max-height: 300px; overflow: auto; white-space: nowrap;">
    <div class="mermaid-container">

```mermaid
        graph TD;

        A[Is the first word a number written in words?] -->|yes| B["`Replace the word with the number`"];
        C -->|yes| D["`Need to make sure if nothing is correct.`"];
        C -->|no| E["`Keep going.`"];
        B --> F["`Is the first word 'Nothing'?`"];

        F -->|yes| G["`Skip to step 3.5.`"];
        F -->|no| H[Skip to step 4.];
        G --> I["`Is it all correct or all incorrect?`"];
        I -->|yes| J["`Go to step 4.`"];
        I -->|no| K["`Go to step 4.`"];
        H --> L["`Is the second word 'but' or 'except'?`"];
        L -->|yes| M["`Go to step 5.`"];
        L -->|no| N["`Go to step 6.`"];
        M --> O["`Is the third word a number written in words?`"];
        O -->|yes| P["`We will use the number of digits in the combo - the number in the third word as the number of digits in the combination, and then they will either be all correct or all incorrect.`"];
        O -->|no| Q["`Skip to step 5.5`"];
        Q --> R["`Output - problems and attempt to use step 6.`"];
        N --> S["`How many numbers are in the message?`"];
        S -->|3| T["`Find max number`"];
        T --> U["`Is the max number at index 0?`"];
        U -->|yes| V["`Is it the first number that appears in the message?`"];
        V -->|yes| W["`Print that the number is not at index 0 but it's the first number that appears in the message. -- no throw exception yet`"];
        V -->|no| X["`Print that the number is not at index 0 and it's not the first number that appears in the message. -- no throw exception yet`"];
        U -->|no| Y["`Skip to step 6.5`"];
        Y --> Z["`Output - problems and attempt to use step 6.`"];
        S -->|2| AA["`Find max number`"];
        AA --> AB["`Is the max number at index 0?`"];
        AB -->|yes| AC["`Is it the first number that appears in the message?`"];
        AC -->|yes| AD["`Print that the number is not at index 0 but it's the first number that appears in the message. -- no throw exception yet`"];
        AC -->|no| AE["`Print that the number is not at index 0 and it's not the first number that appears in the message. -- no throw exception yet`"];
        AB -->|no| AF["`Skip to step 6.5`"];
        AF --> AG["`Output - problems and attempt to use step 6.`"];
        S -->|1| AH["`Find max number`"];
        AH --> AI["`Is the max number at index 0?`"];
        AI -->|yes| AJ["`Is it the first number that appears in the message?`"];
        AJ -->|yes| AK["`Print that the number is not at index 0 but it's the first number that appears in the message. -- no throw exception yet`"];
        AJ -->|no| AL["`Print that the number is not at index 0 and it's not the first number that appears in the message. -- no throw exception yet`"];
        AI -->|no| AM["`Skip to step 6.5`"];
        AM --> AN["`Output - problems and attempt to use step 6.`"];
```
      
</div>
  </div>

  <style>
    .mermaid-container {
      font-size: 20px; /* Adjust the font size as needed */
    }
  </style>
</details>
