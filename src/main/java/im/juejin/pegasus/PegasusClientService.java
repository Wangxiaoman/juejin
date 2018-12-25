package im.juejin.pegasus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.xiaomi.infra.pegasus.client.PException;
import com.xiaomi.infra.pegasus.client.PegasusClientInterface;

//@Service
public class PegasusClientService {

    @Resource
    private PegasusClientInterface pegasusClientInterface;
    
    public void set(String tableName, String hashKey, String sortKey, String value, int ttlSecond) {
        try {
            pegasusClientInterface.set(tableName, hashKey.getBytes(), sortKey.getBytes(), value.getBytes(),
                    ttlSecond);
        } catch (PException e) {
            e.printStackTrace();
        }
    }

    public void set(String tableName, String hashKey, String sortKey, String value) {
        try {
            pegasusClientInterface.set(tableName, hashKey.getBytes(), sortKey.getBytes(), value.getBytes());
        } catch (PException e) {
            e.printStackTrace();
        }
    }

    public String get(String tableName, String hashKey, String sortKey) {
        String value = "";
        try {
            byte[] valuebytes =
                    pegasusClientInterface.get(tableName, hashKey.getBytes(), sortKey.getBytes());
            if (valuebytes != null) {
                value = new String(valuebytes);
            }
        } catch (PException e) {
            e.printStackTrace();
        }
        return StringUtils.isBlank(value) ? "" : value;
    }

    public Map<String, String> mutiGetMap(String tableName, String hashKey) {
        List<Pair<byte[], byte[]>> values = new ArrayList<>();
        try {
            pegasusClientInterface.multiGet(tableName, hashKey.getBytes(), null, values);
        } catch (PException e) {
            e.printStackTrace();
        }
        Map<String, String> result = new HashMap<>();
        for (Pair<byte[], byte[]> value : values) {
            result.put(new String(value.getKey()), new String(value.getValue()));
        }
        return result;
    }

    public List<Pair<byte[], byte[]>> mutiGetList(String tableName, String hashKey) {
        List<Pair<byte[], byte[]>> values = new ArrayList<>();
        try {
            pegasusClientInterface.multiGet(tableName, hashKey.getBytes(), null, values);
        } catch (PException e) {
            e.printStackTrace();
        }
        return values;
    }

    public void del(String tableName, String hashKey, String sortKey) {
        try {
            pegasusClientInterface.del(tableName, hashKey.getBytes(), sortKey.getBytes());
        } catch (PException e) {
            e.printStackTrace();
        }
    }
}
