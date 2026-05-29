package LoadBalancer;

import java.util.Collections;
import java.util.List;

public class IpHashLoadBalancer {

    private final List<Server> servers;

    public IpHashLoadBalancer(List<Server> servers){
         this.servers = Collections.unmodifiableList(servers);
    }

    public Server getServer(String ip){
        
        int hash = ip.hashCode();

        int index = hash % servers.size();

        return servers.get(index);
    }
    
}
