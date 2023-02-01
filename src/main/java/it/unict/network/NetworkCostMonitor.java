package it.unict.network;

import io.fabric8.kubernetes.api.model.Node;
import it.unict.telemetry.TelemetryService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class NetworkCostMonitor {

    private static final Logger log = LoggerFactory.getLogger(NetworkCostMonitor.class);

    private final int minCost = 1;

    private final int maxCost = 100;

    @RestClient
    TelemetryService telemetryService;

    public void updateNetworkCosts(List<Node> nodeList) {
        List<String >nodeNameList = nodeList.stream().map(n-> n.getMetadata().getName()).collect(Collectors.toList());

        Map<String, Map<String,Float>> latencyValues = telemetryService.getNodesLatencies().await().indefinitely();

        int highest = 0;
        int lowest = 0;

        if (!latencyValues.isEmpty()) {
            highest = Collections.max(
                    latencyValues
                            .values()
                            .stream()
                            .map(x -> Collections.max(x.values()))
                            .collect(Collectors.toList())
            ).intValue();
            lowest = Collections.min(
                    latencyValues
                            .values()
                            .stream()
                            .map(x -> Collections.min(x.values()))
                            .collect(Collectors.toList())
            ).intValue();
        }

        int newRange = maxCost - minCost;
        int finalLowest = lowest;
        int finalHighest = highest;
        int oldRange = finalHighest - finalLowest;

        nodeList.forEach(node -> {
            String nodeName = node.getMetadata().getName();
            if (latencyValues.containsKey(nodeName)) {
                latencyValues.get(nodeName).forEach((key, value) -> {
                    if (nodeNameList.contains(key)) {
                        int networkCost = (oldRange == 0) ? minCost : ((value.intValue() - finalLowest) * newRange / oldRange) + minCost;
                        log.info("Network cost between nodes {} and {}: {}", nodeName, key, networkCost);

                        node.getMetadata().getLabels().put(
                                "network.cost." + key,
                                String.valueOf(networkCost)
                        );
                    }
                });
            }

            node.getMetadata().getLabels().put(
                    "network.cost." + nodeName,
                    String.valueOf(minCost)
            );
        });

        log.info("------------------------------------");
    }
}
