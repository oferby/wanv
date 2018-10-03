package com.huawei.sdn.commons.selector.routing;

import java.util.List;

import org.junit.Test;

import solver.ResolutionPolicy;
import solver.Solver;
import solver.constraints.ICF;
import solver.constraints.IntConstraintFactory;
import solver.search.solution.Solution;
import solver.search.strategy.selectors.values.IntDomainMin;
import solver.search.strategy.selectors.variables.InputOrder;
import solver.search.strategy.strategy.IntStrategy;
import solver.variables.IntVar;
import solver.variables.VF;
import solver.variables.VariableFactory;

/**
 * Created by oWX212574 on 6/9/2014.
 */
public class SolverTest {


    @Test
    public void testSolver() {

        Solver solver = new Solver();

        int[] frameDelayArray = { 21, 20, 21 };

        IntVar[] portArray = VariableFactory.boundedArray("portArray", 3, 0, 1, solver);
        IntVar single = VariableFactory.bounded("single", 1, 1, solver);
        solver.post(IntConstraintFactory.sum(portArray, single));

        IntVar delay = VF.bounded("delay", 0, 10000, solver);
        solver.post(ICF.scalar(portArray, frameDelayArray, delay));

        IntVar flowPerPort = VF.bounded("flowPerPort", 0, 10000, solver);
        solver.post(ICF.scalar(portArray, getFlowsPerPort(), flowPerPort));

        IntVar[] scope = { delay, flowPerPort };

        solver.set(new IntStrategy(scope, new InputOrder<IntVar>(), new IntDomainMin()));

        solver.findParetoFront(ResolutionPolicy.MINIMIZE, flowPerPort, delay);

        List<Solution> solutions = solver.getSolutionRecorder().getSolutions();

        Solution lastSolution = solutions.get(0);

        if (lastSolution == null)
            throw new RuntimeException();

        for (int i = 0; i < 3; i++) {
            System.out.print(lastSolution.getIntVal(portArray[i]) + " ");
        }

    }

    private int[] getFlowsPerPort() {

        int[] flowsPerPort = new int[3];

        for (int i = 0; i < 3; i++) {

            flowsPerPort[i] = 100 - i * 10;

        }

        return flowsPerPort;
    }

}
