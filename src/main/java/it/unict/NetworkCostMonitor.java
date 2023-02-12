package it.unict;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.stream.Collectors;

@ApplicationScoped
public class NetworkCostMonitor {

    private final int minCost = 1;

    private final int maxCost = 100;

    public void updateNetworkCosts(ClusterGraph clusterGraph) {
        double maxLatency = Collections.max(
                clusterGraph.getClusterNodes()
                        .stream()
                        .map(ClusterNode::getMaxLatency)
                        .collect(Collectors.toList())
        );

        double minLatency = Collections.min(
                clusterGraph.getClusterNodes()
                        .stream()
                        .map(ClusterNode::getMinLatency)
                        .collect(Collectors.toList())
        );

        int newRange = maxCost - minCost;
        int oldRange = (int)(maxLatency - minLatency);

        clusterGraph.getClusterNodes().forEach(clusterNode -> {
            clusterNode.getLatencies().forEach((key, value) -> {
                int networkCost = (oldRange == 0) ? minCost : ((int)(value - minLatency) * newRange / oldRange) + minCost;
                clusterNode.getNode()
                        .getMetadata()
                        .getLabels()
                        .put("network.cost." + key, String.valueOf(networkCost));
            });

            clusterNode.getNode()
                    .getMetadata().
                    getLabels()
                    .put("network.cost." + clusterNode.getName(), String.valueOf(minCost)
            );
        });
    }
}
