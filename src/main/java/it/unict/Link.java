package it.unict;

public class Link {

    private final String neighborClusterNodeName;

    private final double latency;

    public Link(String neighborClusterNodeName, double latency) {
        this.neighborClusterNodeName = neighborClusterNodeName;
        this.latency = latency;
    }

    public String getNeighborClusterNodeName() {
        return neighborClusterNodeName;
    }

    public Double getLatency() {
        return latency;
    }
}
