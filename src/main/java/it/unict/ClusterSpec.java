package it.unict;

import java.util.Map;

public class ClusterSpec {

    private final Map<String, String> nodeSelector;

    private final Boolean resourceMonitorEnabled;

    private final Boolean networkCostMonitorEnabled;

    private final Integer runPeriod;

    public ClusterSpec(Map<String, String> nodeSelector, Boolean resourceMonitorEnabled, Boolean networkCostMonitorEnabled, Integer runPeriod) {
        this.nodeSelector = nodeSelector;
        this.resourceMonitorEnabled = resourceMonitorEnabled;
        this.networkCostMonitorEnabled = networkCostMonitorEnabled;
        this.runPeriod = runPeriod;
    }

    public Map<String, String> getNodeSelector(){
        return nodeSelector;
    }

    public Boolean isResourceMonitorEnabled() {
        return resourceMonitorEnabled;
    }

    public Boolean isNetworkCostMonitorEnabled() {
        return networkCostMonitorEnabled;
    }

    public Integer getRunPeriod() { return runPeriod; }
}
