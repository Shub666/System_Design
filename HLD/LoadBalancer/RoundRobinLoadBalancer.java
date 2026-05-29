package LoadBalancer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer{

    private final List<Server> servers;
    private final AtomicInteger index = new AtomicInteger(0);


    public RoundRobinLoadBalancer(List<Server> servers){
        this.servers = Collections.unmodifiableList(servers);
    }

    public Server getServer(){
        int i = index.getAndIncrement() % servers.size();
        return servers.get(i);
    }
}