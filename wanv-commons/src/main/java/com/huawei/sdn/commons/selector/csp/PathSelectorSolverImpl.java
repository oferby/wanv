package com.huawei.sdn.commons.selector.csp;

import com.huawei.sdn.commons.data.ConnectorStatistics;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.data.PSFlowTimer;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.planning.LinkSelectionSolution;
import com.huawei.sdn.commons.selector.csp.planning.SolverThread;
import com.huawei.sdn.commons.selector.flow.FlowProgrammer;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solver.ProblemFactChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by oWX212574 on 6/23/2014.
 */
@Controller("pathSelectorSolver")
public class PathSelectorSolverImpl implements SolverEventListener, PathSelectorSolver {

    private static Logger logger = LoggerFactory.getLogger(PathSelectorSolverImpl.class);
    private Solver solver;
    private LinkSelectionSolution linkSelectionSolution;
    private Map<Integer, PSFlow> psFlowBestSolutionMap = new ConcurrentHashMap<>();
    @Autowired
    private FlowProgrammer flowProgrammer;
    private SolverThread solverThread;
    @Autowired
    private RouteSelector routeSelector;

    private ExecutorService executorService = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000));

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Timer elapseTimeTimer;

    @PostConstruct
    public void restart() {

        if (solverThread != null) {
            solverThread.setStopSolverThread();
        }

        linkSelectionSolution = new LinkSelectionSolution();

        SolverFactory solverFactory = SolverFactory.createFromXmlResource("SolverConfig.xml");

        SolverConfig solverConfig = solverFactory.getSolverConfig();

        solver = solverConfig.buildSolver();

        solver.addEventListener(this);


        elapseTimeTimer = new Timer("Flow-Elapse-Time");

        logger.debug("Starting flow timer");
        elapseTimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                PathSelectorSolverImpl.this.updateFlowTime();
            }
        }, 10000, 10000);

    }

    @Override
    public int getScore() {

        Solution bestSolution = solver.getBestSolution();

        if (bestSolution == null) {
            return 0;
        }

        Score score = bestSolution.getScore();

        if (score == null) {
            return 0;
        }

        Number[] numbers = score.toLevelNumbers();

        return numbers[1].intValue();

    }

    @Override
    public PSFlow getCurrentFlow(long id) {
        return psFlowBestSolutionMap.get(id);
    }

    @Override
    public void addFlow(final PSFlow flow) {

        if (psFlowBestSolutionMap.containsKey(flow.hashCode())) {
            logger.debug("Solution already has flow id: " + flow.getId());
            return;

        }

        logger.debug("Adding flow to solution map: " + flow.toString());
        psFlowBestSolutionMap.put(flow.hashCode(), flow);

        if (flow.getCtData() == null) {
            logger.error("no CtData for " + flow.toString());
        }


        solver.addProblemFactChange(new ProblemFactChange() {
            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();

                scoreDirector.beforeEntityAdded(flow);
                workingSolution.getPsFlowList().add(flow);
                scoreDirector.afterEntityAdded(flow);

                PSFlowTimer flowTimer = new PSFlowTimer();
                flowTimer.setPsFlow(flow);

                scoreDirector.beforeProblemFactAdded(flowTimer);
                workingSolution.addPSFlowTimer(flowTimer);
                scoreDirector.afterProblemFactAdded(flowTimer);


            }
        });

        checkSolverThread();

    }

    @Override
    public boolean flowExists(PSFlow psFlow) {
        return psFlowBestSolutionMap.containsKey(psFlow.hashCode());
    }

    @Override
    public void removeFlow(final PSFlow flow) {

        readWriteLock.writeLock().lock();

        try {

            if (psFlowBestSolutionMap.containsKey(flow.hashCode())) {

                logger.debug("Removing flow from solution: " + flow.toString());
                psFlowBestSolutionMap.remove(flow.hashCode());

            } else {

                logger.warn("there is no flow in the solution map for hash: " + flow.hashCode() + ",  " + flow.toString());

            }

            solver.addProblemFactChange(new ProblemFactChange() {
                @Override
                public void doChange(ScoreDirector scoreDirector) {

                    LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();
                    for (Iterator<PSFlow> iterator = workingSolution.getPsFlowList().iterator(); iterator.hasNext(); ) {

                        PSFlow instance = iterator.next();
                        if (instance.getId() == flow.getId()) {
                            scoreDirector.beforeEntityRemoved(instance);
                            iterator.remove();
                            scoreDirector.afterEntityRemoved(instance);
                            break;
                        }
                    }


                    for (Iterator<PSFlowTimer> iterator = workingSolution.getPsFlowTimerList().iterator(); iterator.hasNext(); ) {

                        PSFlowTimer flowTimer = iterator.next();
                        if (flowTimer.getPsFlow().getId() == flow.getId()) {
                            scoreDirector.beforeProblemFactRemoved(flowTimer);
                            iterator.remove();
                            scoreDirector.afterProblemFactRemoved(flowTimer);
                            break;
                        }

                    }


                }
            });

        } finally {
            readWriteLock.writeLock().unlock();
        }

    }


    private void updateFlowTime() {

        readWriteLock.writeLock().lock();

        try {

            logger.trace("updating flow elapse time");

            solver.addProblemFactChange(new ProblemFactChange() {
                @Override
                public void doChange(ScoreDirector scoreDirector) {

                    LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();

                    List<PSFlowTimer> psFlowTimerList = workingSolution.getPsFlowTimerList();
                    for (PSFlowTimer flowTimer : psFlowTimerList) {

                        scoreDirector.beforeProblemFactChanged(flowTimer);
                        flowTimer.setElapseTime();
                        scoreDirector.afterProblemFactChanged(flowTimer);

                    }

                }
            });

        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void clearAllFlows() {

        logger.debug("Removing all flows");
        psFlowBestSolutionMap.clear();

        solver.addProblemFactChange(new ProblemFactChange() {
            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();
                Set<PSFlow> psFlowList = workingSolution.getPsFlowList();

                for (Iterator<PSFlow> iterator = psFlowList.iterator(); iterator.hasNext(); ) {
                    PSFlow instance = iterator.next();
                    scoreDirector.beforeEntityRemoved(instance);
                    iterator.remove();
                    scoreDirector.afterEntityRemoved(instance);
                }

            }
        });
    }

    @Override
    public synchronized void bestSolutionChanged(BestSolutionChangedEvent event) {

        try {


            if (solver.isEveryProblemFactChangeProcessed()) {

                Solution solverBestSolution = solver.getBestSolution();

                logger.debug("got new best solution. score: " + solverBestSolution.getScore().toString());

                LinkSelectionSolution bestSolution = (LinkSelectionSolution) solverBestSolution;

                Set<PSFlow> psFlowList = bestSolution.getPsFlowList();

                for (Iterator<PSFlow> iterator = psFlowList.iterator(); iterator.hasNext(); ) {
                    final PSFlow psFlowFromSolution = iterator.next();

                    if (psFlowFromSolution.getCtData() == null) {
                        logger.error("no CtData for " + psFlowFromSolution.toString());
                    }

                    PSFlow currentFlow = psFlowBestSolutionMap.get(psFlowFromSolution.hashCode());

                    if (currentFlow == null) {
                        logger.debug("flow from solution was removed from flow map. " + psFlowFromSolution);

                    } else if (psFlowFromSolution.getConnectorOut() == null) {
                        logger.debug("solution changed out port to null. There are no available ports for this flow.");
                        currentFlow.setConnectorOut(null);

                    } else if (currentFlow.getConnectorOut() != null && psFlowFromSolution.getConnectorOut().getId().equals(currentFlow.getConnectorOut().getId()) &&
                            currentFlow.getQueue() != null && currentFlow.getQueue().getId() == psFlowFromSolution.getQueue().getId()) {
//                        logger.debug("flow from solution not changed.");

                    } else if (psFlowFromSolution.getConnectorOut().getType().equals(ConnectorType.REMOTE) && currentFlow.getConnectorOut() == null) {

                        logger.debug("adding DGW flow from solution to the switch. " + psFlowFromSolution);
                        currentFlow.setConnectorOut(psFlowFromSolution.getConnectorOut());

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                PathSelectorSolverImpl.this.addFlowToSwitch(psFlowFromSolution);
                            }
                        });

                    } else if (currentFlow.getConnectorOut() == null || (!psFlowFromSolution.getConnectorOut().getId().equals(currentFlow.getConnectorOut().getId()))) {
                        logger.debug("Flow out connector changed from port: " + (currentFlow.getConnectorOut() == null ? "NULL" : currentFlow.getConnectorOut().getId()) + " to port: " + psFlowFromSolution.getConnectorOut().getId());
                        logger.debug("Current flow: " + currentFlow.toString());

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                flowConnectorChanged(psFlowFromSolution);
                            }
                        });


                    } else if (psFlowFromSolution.getQueue() != null && currentFlow.getQueue() != null && psFlowFromSolution.getQueue().getId() != currentFlow.getQueue().getId()) {
                        logger.debug("Queue changed from id: " + currentFlow.getQueue().getId() + " to ID: " + psFlowFromSolution.getQueue().getId());

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                flowConnectorChanged(psFlowFromSolution);
                            }
                        });

                    } else {
                        logger.debug("new solution not handled. Current flow out port: " + currentFlow.getConnectorOut().getId() + ", new flow out port: " + psFlowFromSolution.getConnectorOut().getId());
                    }
                }

            }

        } catch (Exception e) {
            logger.debug("got exception in solution selector", e);
        }

    }

    private void flowConnectorChanged(PSFlow flow) {

        logger.debug("flow changed: " + flow.toString());
        psFlowBestSolutionMap.put(flow.hashCode(), flow);
        try {
            flowProgrammer.modifyFlow(flow);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


    }

    private void addFlowToSwitch(PSFlow flow) {
        logger.debug("adding flow to switch" + flow.toString());
        psFlowBestSolutionMap.put(flow.hashCode(), flow);
        try {
            flowProgrammer.addFlow(flow);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void portStatusChanged(final String connectorId, final boolean connected) {

        logger.debug("port " + connectorId + " status changed to " + connected);

        solver.addProblemFactChange(new ProblemFactChange() {
            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();
                Collection problemFacts = workingSolution.getProblemFacts();

                for (Object fact : problemFacts) {

                    if (fact instanceof PSConnector) {

                        if (((PSConnector) fact).getId().equals(connectorId)) {
                            logger.debug("Changing connector id: " + connectorId + " to status connected: " + connected);
                            scoreDirector.beforeProblemFactChanged(fact);
                            ((PSConnector) fact).setActive(connected);
                            scoreDirector.afterProblemFactChanged(fact);
                            break;
                        }
                    }

                }

            }
        });

    }

    @Override
    public Set<PSFlow> getAllKnownFlows() {

        Set<PSFlow> psFlowSet = new HashSet<>();

        for (PSFlow psFlow : psFlowBestSolutionMap.values()) {
            psFlowSet.add(psFlow.getCopy());
        }

        return psFlowSet;

    }

    @Override
    public void connectorMetrics(final Map<String, ConnectorStatistics> connectorStatisticsMap) {

        solver.addProblemFactChange(new ProblemFactChange() {

            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();
                Collection problemFacts = workingSolution.getProblemFacts();

                for (Object fact : problemFacts) {

                    if (fact instanceof PSConnector) {

                        PSConnector connector = (PSConnector) fact;
                        logger.trace("Adding statistics to " + connector.toString());
                        ConnectorStatistics statistics = connectorStatisticsMap.get(connector.getId());
                        if (statistics != null) {
                            scoreDirector.beforeProblemFactChanged(connector);
                            connector.getConnectorStatistics().update(statistics);

                            PSConnector connectorInstance = routeSelector.getConnector(connector.getId());
                            connectorInstance.getConnectorStatistics().update(statistics);

                            scoreDirector.afterProblemFactChanged(connector);

                        }

                    }


                }

            }
        });


    }

    @Override
    public Map<String, ConnectorStatistics> getConnectorStatisticsFromSolution() {

        Map<String, ConnectorStatistics> connectorStatisticsMap = new HashMap<>();

        LinkSelectionSolution bestSolution = (LinkSelectionSolution) solver.getBestSolution();

        Collection facts;
        if (bestSolution == null) {
            facts = routeSelector.getAllConnectors();
        } else {
            facts = bestSolution.getProblemFacts();
        }

        for (Object fact : facts) {

            if (fact instanceof PSConnector) {
                connectorStatisticsMap.put(((PSConnector) fact).getId(), ((PSConnector) fact).getConnectorStatistics());
            }

        }

        return connectorStatisticsMap;

    }

    @Override
    public void enableSolver(boolean enable) {

        if (!enable) {
            solverThread.setStopSolverThread();
            this.clearAllFlows();
        } else {
            checkSolverThread();
        }

    }

    public Set<PSFlow> getAllKnownFlowsFromSolution() {

        LinkSelectionSolution bestSolution = (LinkSelectionSolution) solver.getBestSolution();

        if (bestSolution == null) {
            return new HashSet<>();
        }

        Set<PSFlow> psFlowSet = new HashSet<>();

        for (PSFlow psFlow : bestSolution.getPsFlowList()) {
            psFlowSet.add(psFlow.getCopy());
        }

        return psFlowSet;

    }

    @Override
    public void addPSConnector(final PSConnector psConnector) {

        solver.addProblemFactChange(new ProblemFactChange() {

            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();

                scoreDirector.beforeProblemFactAdded(psConnector);
                workingSolution.addPSConnector(psConnector);
                scoreDirector.afterProblemFactAdded(psConnector);

            }

        });

    }

    @Override
    public void modifyPSConnector(final PSConnector psConnector) {

        solver.addProblemFactChange(new ProblemFactChange() {

            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();

                scoreDirector.beforeProblemFactChanged(psConnector);
                workingSolution.addPSConnector(psConnector);
                scoreDirector.afterProblemFactChanged(psConnector);

            }

        });

    }

    @Override
    public void remotePSConnector(final PSConnector psConnector) {

        solver.addProblemFactChange(new ProblemFactChange() {

            @Override
            public void doChange(ScoreDirector scoreDirector) {

                LinkSelectionSolution workingSolution = (LinkSelectionSolution) scoreDirector.getWorkingSolution();

                scoreDirector.beforeProblemFactRemoved(psConnector);
                workingSolution.removePSConnector(psConnector);
                scoreDirector.afterProblemFactRemoved(psConnector);

            }

        });

    }

    public Map<Integer, PSFlow> getPsFlowBestSolutionMap() {
        return psFlowBestSolutionMap;
    }

    private synchronized void checkSolverThread() {

        if (solverThread==null || !solverThread.isRunning()) {

            try {
                logger.debug("Starting new solver thread.");

                solverThread = new SolverThread(solver, linkSelectionSolution);

                new Thread(solverThread).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
