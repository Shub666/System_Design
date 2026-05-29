package LoadBalancer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance {

    private final List<Server> servers;

    public RandomLoadBalance(List<Server> servers){
        this.servers = Collections.unmodifiableList(servers);
    }

    public Server getServer(){
        int i = ThreadLocalRandom.current().nextInt(servers.size());
        return servers.get(i);
    }
    
}
