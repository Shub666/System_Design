package LoadBalancer;

import java.util.Collections;
import java.util.List;

public class LeastConnectionsLoadBalancer {

        private final List<Server> servers;

    public LeastConnectionsLoadBalancer(List<Server> servers){
         this.servers = Collections.unmodifiableList(servers);
    }
    
     public Server getServer(){
        
        Server leastConnectedServer= servers.get(0);

        for(int i=0;i<servers.size();i++){
            Server currentServer = servers.get(i);
            if(currentServer.getActiveConnections() < leastConnectedServer.getActiveConnections()){
                leastConnectedServer = currentServer;
            }
        }
        return leastConnectedServer;
    }

}
