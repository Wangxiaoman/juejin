package im.juejin;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import im.juejin.pegasus.PegasusClientService;

@RestController
@RequestMapping("/pg")
public class PegasusController {

    @Resource
    private PegasusClientService pegasusClientService;

    @GetMapping("/get")
    public String get(@RequestParam("tableName") String tableName,
            @RequestParam("hashKey") String hashKey, @RequestParam("sortKey") String sortKey) {

        return pegasusClientService.get(tableName, hashKey, sortKey);
    }
    
    @PostMapping("/muti/get")
    public Map<String,String> get(@RequestParam("tableName") String tableName,
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
}
