package im.juejin;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import im.juejin.redis.RedisMgetTestService;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Resource
    private RedisMgetTestService redisMgetTestService;
    
    @GetMapping("/mget")
    public Map<String,Object> mget(@RequestParam("number")int number){
        return redisMgetTestService.mgetResult(number);
    }
    
    @GetMapping("/get")
    public Map<String,Object> get(@RequestParam("number")int number){
        return redisMgetTestService.getResult(number);
    }
    
    @GetMapping("/mset")
    public long mset(){
        return redisMgetTestService.mset();
    }
}
