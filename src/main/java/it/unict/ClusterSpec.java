package it.unict;

import java.util.Map;

public class ClusterSpec {

    private Map<String, String> nodeSelector;

    private Boolean resourceMonitorEnabled;

    private Boolean networkCostMonitorEnabled;

    private Integer runPeriod;

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
