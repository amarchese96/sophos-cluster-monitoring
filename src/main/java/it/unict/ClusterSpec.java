package it.unict;

import java.util.Map;

public class ClusterSpec {

    private Map<String, String> nodeSelector;

    private Integer runInterval;

    public Map<String, String> getNodeSelector(){
        return nodeSelector;
    }

    public Integer getRunInterval() {
        return runInterval;
    }
}
