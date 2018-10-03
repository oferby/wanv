package com.huawei.sdn.commons.selector.csp.choco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.parsing.Problem;
import solver.Solver;
import solver.constraints.ICF;
import solver.constraints.IntConstraintFactory;
import solver.constraints.LCF;
import solver.search.solution.Solution;
import solver.search.strategy.IntStrategyFactory;
import solver.variables.IntVar;
import solver.variables.VF;
import solver.variables.VariableFactory;

import java.util.Arrays;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 1/7/2016.
 */
@RunWith(JUnit4.class)
public class TestChoco {


    @Test
    public void test1(){

        Solver solver = new Solver();

        IntVar[] all = new IntVar[14];

        all[0] = VariableFactory.bounded("1", 1,14,solver);
        all[1] = VariableFactory.bounded("2", 1,14,solver);
        all[2] = VariableFactory.bounded("3", 1,14,solver);
        all[3] = VariableFactory.bounded("4", 1,14,solver);
        all[4] = VariableFactory.bounded("5", 1,14,solver);
        all[5] = VariableFactory.bounded("6", 1,14,solver);
        all[6] = VariableFactory.bounded("7", 1,14,solver);
        all[7] = VariableFactory.bounded("8", 1,14,solver);
        all[8] = VariableFactory.bounded("9", 1,14,solver);
        all[9] = VariableFactory.bounded("10", 1,14,solver);
        all[10] = VariableFactory.bounded("11", 1,14,solver);
        all[11] = VariableFactory.bounded("12", 1,14,solver);
        all[12] = VariableFactory.bounded("13", 1,14,solver);
        all[13] = VariableFactory.bounded("14", 1,14,solver);

        solver.post(IntConstraintFactory.alldifferent(all));

        solver.post(IntConstraintFactory.member(all[1],3,3));
        solver.post(IntConstraintFactory.member(all[11],6,6));
        solver.post(IntConstraintFactory.member(all[5],5,5));
        solver.post(IntConstraintFactory.member(all[6],4,4));
        solver.post(IntConstraintFactory.member(all[13],2,2));
        solver.post(IntConstraintFactory.member(all[8],1,1));

        IntVar[] linesSum = VariableFactory.boundedArray("line",7,1,100,solver);
        IntVar[] line1 = new IntVar[] {all[0],all[1],all[2],all[3]} ;
        IntVar[] line2 = new IntVar[] {all[0],all[1],all[2],all[3]} ;
        IntVar[] line3 = new IntVar[] {all[0],all[1],all[2],all[3]} ;
        IntVar[] line4 = new IntVar[] {all[0],all[1],all[2],all[3]} ;
        IntVar[] line5 = new IntVar[] {all[0],all[1],all[2],all[3]} ;
        IntVar[] line6 = new IntVar[] {all[0],all[1],all[2],all[3]} ;
        IntVar[] line7 = new IntVar[] {all[0],all[1],all[2],all[3]} ;

        solver.post(ICF.sum(line1, linesSum[0]));
        solver.post(ICF.sum(line2, linesSum[1]));
        solver.post(ICF.sum(line3, linesSum[2]));
        solver.post(ICF.sum(line4, linesSum[3]));
        solver.post(ICF.sum(line5, linesSum[4]));
        solver.post(ICF.sum(line6, linesSum[5]));
        solver.post(ICF.sum(line7, linesSum[6]));

        solver.post(ICF.arithm(linesSum[0], "=", linesSum[1]));
        solver.post(ICF.arithm(linesSum[2], "=", linesSum[1]));
        solver.post(ICF.arithm(linesSum[2], "=", linesSum[3]));
        solver.post(ICF.arithm(linesSum[3], "=", linesSum[4]));
        solver.post(ICF.arithm(linesSum[4], "=", linesSum[5]));
        solver.post(ICF.arithm(linesSum[5], "=", linesSum[6]));


        solver.post();

        solver.set(IntStrategyFactory.random_value(all));

        solver.findSolution();

        Solution lastSolution = solver.getSolutionRecorder().getLastSolution();



        System.out.print(lastSolution.toString());

//        for (int i = 0; i < 7; i++) {
//
//            int s = 0;
//            for (int j = 0; j < 4; j++) {
//
//                System.out.println("line " + i + ": " + lastSolution.getIntVal(line1[i]));
//            }
//
//        }

    }

    @Test
    public void test2(){


        Solver solver = new Solver();
        IntVar[] IBIN = VF.enumeratedArray("IBIN", 7, 1, 14, solver);
        int[] sizes = new int[]{2, 3, 1, 4, 2};
        IntVar[] BLOADS = VF.enumeratedArray("BLOADS", 3, 0, 5, solver);
        solver.post(ICF.bin_packing(IBIN, sizes, BLOADS, 1));
        solver.findAllSolutions();

        Solution lastSolution = solver.getSolutionRecorder().getLastSolution();

        System.out.print(lastSolution.toString());

    }




}
