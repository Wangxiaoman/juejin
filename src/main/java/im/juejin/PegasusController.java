package im.juejin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import im.juejin.pegasus.PegasusClientService;

//@RestController
//@RequestMapping("/pg")
public class PegasusController {

    @Resource
    private PegasusClientService pegasusClientService;

    @GetMapping("/get")
    public String get(@RequestParam("tableName") String tableName,
            @RequestParam("hashKey") String hashKey, @RequestParam("sortKey") String sortKey) {

        return pegasusClientService.get(tableName, hashKey, sortKey);
    }

    @PostMapping("/muti/get")
    public Map<String, String> get(@RequestParam("tableName") String tableName,
            @RequestParam("hashKey") String hashKey) {

        return pegasusClientService.mutiGetMap(tableName, hashKey);
    }

    @PostMapping("/set")
    public String set(@RequestParam("tableName") String tableName,
            @RequestParam("hashKey") String hashKey, @RequestParam("sortKey") String sortKey,
            @RequestParam("value") String value,
            @RequestParam(value = "ttlSecond", defaultValue = "0") int ttlSecond) {

        if (ttlSecond == 0) {
            pegasusClientService.set(tableName, hashKey, sortKey, value);
        } else {
            pegasusClientService.set(tableName, hashKey, sortKey, value, ttlSecond);
        }
        return "OK";
    }

    @GetMapping("/save")
    public void saveTest(@RequestParam(name="threadGroupCount",defaultValue="1") int threadGroupCount){
        int keysCount = 10000;
        Map<Integer,List<String>> map = new HashMap<>();
        for(int i=0;i<threadGroupCount;i++){
            List<String> keys = getKeys(keysCount);
            map.put(i, keys);
        }
        
        long beginTime = System.currentTimeMillis();
        List<Thread> tt = new ArrayList<>();
        for (int i = 0; i < threadGroupCount; i++) {
            List<String> keys = map.get(i);
            Thread t1 = new Thread(new PSaveThread("test1 " + i, false, keys));
            Thread t2 = new Thread(new PSaveThread("test2 " + i, false, keys));
            Thread t3 = new Thread(new PSaveThread("test3 " + i, false, keys));
            tt.add(t1);
            tt.add(t2);
            tt.add(t3);
            t1.start();
            t2.start();
            t3.start();
        }
        
        for(Thread t : tt){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("cost time:"+(endTime - beginTime)+" ms");
    }

    private List<String> getKeys(int count) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(UUID.randomUUID().toString());
        }
        return result;
    }
    
    class PSaveThread implements Runnable {
        public PSaveThread(String name) {
            this.name = name;
        }

        public PSaveThread(String name, boolean readOrWrite, List<String> keys) {
            this.name = name;
            this.readOrWrite = readOrWrite;
            this.keys = keys;
        }

        private String name;
        private boolean readOrWrite;
        private List<String> keys;

        @Override
        public void run() {
            if (readOrWrite) {
                Collections.shuffle(keys);
            }

            long beginTime = System.currentTimeMillis();
            int hitCount = 0;
            for (int i = 0; i < keys.size(); i++) {
                if (readOrWrite) {
                    Object o = pegasusClientService.get("context", keys.get(i), "1");
                    if (o != null) {
                        hitCount++;
                    }
                } else {
                    pegasusClientService.set("context", keys.get(i), "1", keys.get(i));
                }

                if (i % 100 == 0) {
                    TimeUnit tu = TimeUnit.NANOSECONDS;
                    try {
                        tu.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            long endTime = System.currentTimeMillis();

            System.out.println("name:" + name + ",time:" + (endTime - beginTime) + "ms"
                    + ",hit count:" + hitCount);
        }
    }
}
