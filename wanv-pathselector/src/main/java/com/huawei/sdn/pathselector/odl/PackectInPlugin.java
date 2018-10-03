package com.huawei.sdn.pathselector.odl;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.ConfigurationLoaderImpl;
import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.db.EventDbControllerImpl;
import com.huawei.sdn.commons.jms.OVSStatsConsumer;
import com.huawei.sdn.commons.db.model.EventCategory;
import com.huawei.sdn.commons.db.model.EventStatus;
import com.huawei.sdn.commons.db.model.Severity;
import com.huawei.sdn.pathselector.db.RoutingDataControllerImpl;
import com.huawei.sdn.pathselector.odl.L2.PacketHandler;
import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import com.huawei.sdn.pathselector.odl.y1731.OamManager;
import com.huawei.sdn.pathselector.tools.BeanFactory;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerListener;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.*;
import org.opendaylight.controller.sal.reader.IReadService;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The OpenDaylight First Frame Plug-in<br/>
 * It implements the OpenDayLight SAL interface {@link IListenDataPacket} to
 * intercept the Packet In and {@link IFlowProgrammerListener} to synchronize
 * the Path Selector Cache Flows.
 */
public class PackectInPlugin implements IListenDataPacket {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackectInPlugin.class);
    protected ISwitchManager switchManager;
    protected IFlowProgrammerService programmer;
    protected IDataPacketService dataPacketService;
    protected IReadService readService;
    protected IStatisticsManager statisticsManager;
    protected ITopologyManager topologyManager;
    protected OVSStatsConsumer ovsStatsConsumer;
    protected Node switchNode;
    private PacketHandler packetHandler;
    private boolean springInitialized = false;

    public void setDataPacketService(IDataPacketService s) {
        this.dataPacketService = s;
    }

    public void unsetDataPacketService(IDataPacketService s) {
        if (this.dataPacketService == s) {
            this.dataPacketService = null;
        }
    }

    public void setFlowProgrammerService(IFlowProgrammerService s) {
        this.programmer = s;
    }

    public void unsetFlowProgrammerService(IFlowProgrammerService s) {
        if (this.programmer == s) {
            this.programmer = null;
        }
    }

    public void setSwitchManager(ISwitchManager s) {
        LOGGER.debug("SwitchManager set");
        this.switchManager = s;
    }

    public void unsetSwitchManager(ISwitchManager s) {
        if (this.switchManager == s) {
            LOGGER.debug("SwitchManager removed!");
            this.switchManager = null;
        }
    }

    public void unsetReadService() {
        this.readService =null;
    }

    public void setReadService(IReadService iReadService) {
        this.readService = iReadService;
    }

    public void unsetStatisticsManager() {
        this.statisticsManager =null;
    }

    public void setStatisticsManager(IStatisticsManager iStatisticsManager) {
        this.statisticsManager = iStatisticsManager;
    }

    public void setTopologyManager(ITopologyManager topologyManager) {
        this.topologyManager = topologyManager;
    }

    public void unsetTopologyManager() {
        this.topologyManager = null;
    }


    /**
     * Function called by the dependency manager when all the required
     * dependencies are satisfied
     */
    void init() {


        // Disabling the SimpleForwarding and ARPHandler bundle to not conflict
        // with this one
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        for (final Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getSymbolicName().contains("arphandler")
                    || bundle.getSymbolicName().contains("simpleforwarding")) {
                try {
                    bundle.uninstall();
                } catch (final BundleException e) {
                    LOGGER.error("Exception in Bundle uninstall " + bundle.getSymbolicName(), e);
                }
            }
        }
        final BeanFactory beanFactory = BeanFactory.getInstance();
        beanFactory.addBean(IDataPacketService.class, dataPacketService);
        beanFactory.addBean(IFlowProgrammerService.class, programmer);
        beanFactory.addBean(ISwitchManager.class, switchManager);
        beanFactory.addBean(IReadService.class, readService);

        LOGGER.info("Initialized");


    }

    /**
     * Function called by the dependency manager when at least one dependency
     * become unsatisfied or when the component is shutting down because for
     * example bundle is being stopped.
     */
    void destroy() {
        // clear the configuration cache
        // switchConfigManager.clear();

        // clear the flow cache and all the flows
//        final Collection<PSFlow<ODLFlowData>> f = pathSelectorEngine.removeAllFlows();
//        if (switchNode != null) {
//            for (final PSFlow<ODLFlowData> psF : f) {
//                removeFlow(psF.getCtData().getFlow(), switchNode);
//            }
//        }
        // stop the consumers
//        portStatusConsumer.stop();
//        ovsStatsConsumer.stop();
        LOGGER.info("Destroyed");
    }

    /**
     * Function called by dependency manager after "init ()" is called and after
     * the services provided by the class are registered in the service registry
     */
    void start() {
        LOGGER.info("Started");
    }

    /**
     * Function called by the dependency manager before the services exported by
     * the component are unregistered, this will be followed by a "destroy ()"
     * calls
     */
    void stop() {
        LOGGER.info("Stopped");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.opendaylight.controller.sal.packet.IListenDataPacket#receiveDataPacket
     * (org.opendaylight.controller.sal.packet.RawPacket)
     */
    @Override
    public PacketResult receiveDataPacket(RawPacket inPkt) {

        try {

            if (!springInitialized) {
                getSpringBeans();
                return PacketResult.IGNORED;
            }


            if (inPkt == null) {
                return PacketResult.IGNORED;
            }

            final Packet packet = this.dataPacketService.decodeDataPacket(inPkt);

            if (!(packet instanceof Ethernet))
                return PacketResult.IGNORED;

            if (packetHandler.handlePacket(inPkt, (Ethernet) packet))
                return PacketResult.CONSUME;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.debug("packet was not handled at all");

        return PacketResult.IGNORED;
    }

    private synchronized boolean getSpringBeans() {

        if (!springInitialized && WanApplicationContextProvider.isReady()) {

            LOGGER.debug("Starting spring setup");

            ServiceHelperImpl serviceHelper = WanApplicationContextProvider.getBean(ServiceHelper.class);
            serviceHelper.setDataPacketService(dataPacketService);
            serviceHelper.setSwitchManager(switchManager);
            serviceHelper.setProgrammer(programmer);
            serviceHelper.setReadService(readService);
            serviceHelper.setStatisticsManager(statisticsManager);
            serviceHelper.setTopologyManager(topologyManager);

            packetHandler = WanApplicationContextProvider.getBean(PacketHandler.class);

            NodeConnectorHandler nodeConnectorHandler = WanApplicationContextProvider.getBean(NodeConnectorHandler.class);
            nodeConnectorHandler.init();

            OamManager oamManager = WanApplicationContextProvider.getBean(OamManager.class);
            oamManager.startTimer();

            RoutingDataControllerImpl dataController = WanApplicationContextProvider.getBean(RoutingDataControllerImpl.class);
            dataController.setup();


            EventDbControllerImpl eventDbController = WanApplicationContextProvider.getBean(EventDbControllerImpl.class);
            eventDbController.setup();

            eventDbController.save(Severity.NORMAL, EventCategory.SYSTEM,"WANV Started", EventStatus.NEW);

            ConfigurationLoaderImpl configurationLoader = WanApplicationContextProvider.getBean(ConfigurationLoaderImpl.class);
            configurationLoader.init();

            springInitialized = true;

            LOGGER.debug("Spring initialized.");

            return true;
        }

        return false;

    }

}
