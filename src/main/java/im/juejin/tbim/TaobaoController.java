package im.juejin.tbim;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.response.OpenimUsersAddResponse;

@RestController
@RequestMapping("/tb/im")
public class TaobaoController {
    private static final String appkey = "25257318";
    private static final String secret = "171a5c7422d4265ec9321ad88df874a2";
    
    private static final String testurl = "http://gw.api.tbsandbox.com/router/rest";
    private static final String producturl = "http://gw.api.taobao.com/router/rest";
    
    
    public static void addUser(){
        
        
        TaobaoClient client = new DefaultTaobaoClient(testurl, appkey, secret);
        OpenimUsersAddRequest req = new OpenimUsersAddRequest();
        List<Userinfos> list2 = new ArrayList<Userinfos>();
        Userinfos obj3 = new Userinfos();
        list2.add(obj3);
        obj3.setNick("king");
        obj3.setIconUrl("http://pic35.photophoto.cn/20150509/0010023742301768_b.jpg");
        obj3.setEmail("uid@taobao.com");
        obj3.setMobile("18600000000");
        obj3.setTaobaoid("tbnick123");
        obj3.setUserid("imuser123");
        obj3.setPassword("xxxxxx");
        obj3.setRemark("demo");
        obj3.setExtra("{}");
        obj3.setCareer("demo");
        obj3.setVip("{}");
        obj3.setAddress("demo");
        obj3.setName("demo");
        obj3.setAge(123L);
        obj3.setGender("M");
        obj3.setWechat("demo");
        obj3.setQq("demo");
        obj3.setWeibo("demo");
        req.setUserinfos(list2);
        OpenimUsersAddResponse rsp;
        try {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
    
//    public void test(){
//        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//        OpenimUsersGetRequest req = new OpenimUsersGetRequest();
//        req.setUserids("abc");
//        OpenimUsersGetResponse rsp = client.execute(req);
//        System.out.println(rsp.getBody());
//        
//    }
    
    
    public static void main(String[] args) {
        addUser();
    }
}
