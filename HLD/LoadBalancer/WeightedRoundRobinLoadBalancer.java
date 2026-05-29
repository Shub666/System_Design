package LoadBalancer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightedRoundRobinLoadBalancer {

    private final List<Server> servers;
    private final AtomicInteger index;
    private final int totalWeight;


    public WeightedRoundRobinLoadBalancer(List<Server> servers){

        this.servers = Collections.unmodifiableList(servers);
        this.index = new AtomicInteger(0);
        this.totalWeight= servers.stream().mapToInt(Server::getWeight).sum();
    }

    public Server getServer(){
        int i = index.getAndIncrement() % totalWeight;
        int currentWeight =0;
        for(Server server: servers){

            currentWeight += server.getWeight();
            if(i < currentWeight){
                return server;
            }

        }
        return servers.get(0);
    }
    
}
