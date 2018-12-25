package im.juejin.pegasus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiaomi.infra.pegasus.client.PException;
import com.xiaomi.infra.pegasus.client.PegasusClientFactory;
import com.xiaomi.infra.pegasus.client.PegasusClientInterface;

//@Configuration
public class PegasusClientManager {
    @Bean(name = "pegasusClientInterface")
    public PegasusClientInterface getInstance(@Value("${pegasus.meta.servers}")String mateServers,@Value("${pegasus.operation.timeout}")int opTimeOut,@Value("${pegasus.aync.works}") int ayncWorks) {
        PegasusClientInterface client = null;
        try {
            client = PegasusClientFactory.getSingletonClient(mateServers, opTimeOut, ayncWorks);
        } catch (PException e) {
            e.printStackTrace();
        }
        return client;
    }
}
