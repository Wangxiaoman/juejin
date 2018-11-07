package im.juejin.tair;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.impl.DefaultTairManager;

@Service
public class TairService {
    @Resource
    private DefaultTairManager defaultTairManager;

    public boolean save(int namespace, String key, String value) {
        ResultCode rcode = defaultTairManager.put(namespace, key, value);
        if (Objects.equal(rcode.getCode(), 0)) {
            return true;
        }
        return false;
    }
    
    public Object get(int namespace, String key) {
        Result<DataEntry> result = defaultTairManager.get(namespace, key);
        if(result != null){
            if (Objects.equal(result.getRc().getCode(), 0)) {
                return result.getValue().getValue();
            }
        }
        return null;
    }

}
