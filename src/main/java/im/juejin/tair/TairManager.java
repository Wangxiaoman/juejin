package im.juejin.tair;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Splitter;
import com.taobao.tair.impl.DefaultTairManager;

@Configuration
public class TairManager {
    
    @Bean(name = "defaultTairManager")
    public DefaultTairManager getDefaultTairManager(@Value("${tair.config.servers}")String configServers){
        if(StringUtils.isEmpty(configServers)){
            // warning
            return null;
        }
        
        DefaultTairManager defaultTairManager = new DefaultTairManager();
        List<String> cs = Splitter.on(",").splitToList(configServers);
        defaultTairManager.setConfigServerList(cs);
        defaultTairManager.setGroupName("context");
        defaultTairManager.init();
        return defaultTairManager;
    }
    
    
}
