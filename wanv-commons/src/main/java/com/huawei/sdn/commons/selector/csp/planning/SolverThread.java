package com.huawei.sdn.commons.selector.csp.planning;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.solver.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ofer Ben-Yacov on 6/23/2014.
 */
public class SolverThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolverThread.class);
    private Solver solver;
    private Solution solution;
    private boolean running = true;

    public SolverThread(Solver solver, LinkSelectionSolution solution) {
        this.solver = solver;
        this.solution = solution;

        LOGGER.debug("Solver thread created");
    }

    @Override
    public void run() {

        LOGGER.debug("Solver thread is running");

        try {

            solver.solve(solution);

        } catch (Exception e) {
            LOGGER.error("Got exception in solver thread: ", e);
        }

        running = false;
        LOGGER.debug("Solver thread is stopping");
    }

    public void setStopSolverThread() {
        solver.terminateEarly();
    }

    public boolean isRunning() {
        return running;
    }
}
