package im.juejin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import im.juejin.tair.TairService;

@RestController
@RequestMapping("/tair")
public class TairController {
    
    @Resource
    private TairService tairService;

    @GetMapping("/save")
    public void saveTest(@RequestParam(name="threadGroupCount",defaultValue="1") int threadGroupCount){
        int keysCount = 50000;
        Map<Integer,List<String>> map = new HashMap<>();
        for(int i=0;i<threadGroupCount;i++){
            List<String> keys = getKeys(keysCount);
            map.put(i, keys);
        }
        
        long beginTime = System.currentTimeMillis();
        List<Thread> tt = new ArrayList<>();
        for (int i = 0; i < threadGroupCount; i++) {
            List<String> keys = map.get(i);
            Thread t1 = new Thread(new TairThread("test1 " + i, false, keys));
            tt.add(t1);
            t1.start();
            
            TimeUnit tu = TimeUnit.NANOSECONDS;
            try {
                tu.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    
    
    @GetMapping("/read")
    public void readTest(@RequestParam(name="threadGroupCount",defaultValue="1") int threadGroupCount){
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
            Thread t1 = new Thread(new TairThread("test1 " + i, true, keys));
            tt.add(t1);
            t1.start();
            
            TimeUnit tu = TimeUnit.NANOSECONDS;
            try {
                tu.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            result.add(String.valueOf(RandomUtils.nextInt(100000000)));
//            result.add(UUID.randomUUID().toString());
        }
        return result;
    }
    
    class TairThread implements Runnable{
        
        public TairThread(String name,boolean readOrWrite,List<String> keys){
            this.name = name;
            this.readOrWrite = readOrWrite;
            this.keys = keys;
        }
        private String name;
        private boolean readOrWrite;
        private List<String> keys;
        
        @Override
        public void run() {
//            if(readOrWrite){
//                Collections.shuffle(keys);
//            }
            
            long beginTime =  System.currentTimeMillis();
            int hitCount = 0;
            for (int i = 0; i < keys.size(); i++) {
                if(readOrWrite){
                    Object o = tairService.get(3, keys.get(i));
                    if(o != null){
                        hitCount++;
                    }
                }else{
                    tairService.save(3, keys.get(i), keys.get(i));
                }
                
                if(i % 1000 == 0){
                    TimeUnit tu = TimeUnit.NANOSECONDS;
                    try {
                        tu.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            long endTime =  System.currentTimeMillis();
            
            System.out.println("name:"+name+",time:"+(endTime-beginTime) +"ms"+",hit count:"+hitCount);
        }
    }
}
