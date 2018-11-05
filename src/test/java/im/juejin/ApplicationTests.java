package im.juejin;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import im.juejin.pegasus.PegasusClientService;
import im.juejin.tair.TairService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ApplicationTests {
    @Resource
    private PegasusClientService pegasusClientService;
    
//    @Test
//    public void test(){
//        pegasusClientService.set("testTable", "userId", "itemId1", "context1");
//        pegasusClientService.set("testTable", "userId", "itemId2", "context2");
//    
//        System.out.println(pegasusClientService.get("testTable", "userId", "itemId1"));
//        System.out.println(pegasusClientService.get("testTable", "userId", "itemId2"));
//        
//        pegasusClientService.set("testTable", "userId", "itemId3", "context3",10);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(pegasusClientService.get("testTable", "userId", "itemId3"));
//        
//        pegasusClientService.del("testTable", "userId", "itemId2");
//        System.out.println(pegasusClientService.get("testTable", "userId", "itemId2"));
//    }
    
    @Resource
    private TairService tairService;
    public void tairTest(){
        tairService.save(1, "1234", "1234");
    }
}