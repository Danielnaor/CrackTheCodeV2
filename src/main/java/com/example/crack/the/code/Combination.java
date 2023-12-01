/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.crack.the.code;

import java.util.List;

/**
 *
 * @author danie
 */
public class Combination {

        public List<Integer> combination;
        // how many numbers are not nulls
        public Integer numNotNull;

        public Combination(List<Integer> combination, int numNotNull) {
                this.combination = combination;
                this.numNotNull = numNotNull;
        }

        public Combination(List<Integer> combination) {
                this.combination = combination;
                numNotNull = 0;
                for (int i = 0; i < combination.size(); i++) {
                        if (combination.get(i) != null) {
                                numNotNull++;
                        }
                }
        }

        @Override
        public String toString() {
                String str = "";
                for (int i = 0; i < combination.size(); i++) {
                        str += combination.get(i) + " ";
                }
                return str;
        }
        
        // clone method
        public Combination clone() {
                return new Combination(combination, numNotNull);
        }

        public void updateNumNotNull() {
                numNotNull = 0;
                for (int i = 0; i < combination.size(); i++) {
                        if (combination.get(i) != null) {
                                numNotNull++;
                        }
                }

                this.numNotNull = numNotNull;
        }

        



}
