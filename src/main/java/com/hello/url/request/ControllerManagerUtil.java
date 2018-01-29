package com.hello.url.request;

import com.hello.util.UrlUniformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/12 10:51
 */
@Component
public class ControllerManagerUtil {

    @Autowired
    private List<BizController> bizControllers;


    Map<String,BizController> bizControllerMap=new HashMap<>();

    @PostConstruct
    public void init(){
        bizControllers.stream().forEach(this::addBizController);
    }

    private void addBizController(BizController bizController){
        bizControllerMap.put(bizController.showMyAcceptUrl(),bizController);
    }

    public BizController findBizControllerByUrl(String url){
        url=UrlUniformer.adjustUrl(url);
        BizController bizController=bizControllerMap.get(url);
        if (Objects.nonNull(bizController)){
            return bizController;
        }
        String urlFamily=url.substring(0,url.indexOf("/",1));
        return bizControllerMap.get(urlFamily+"/*");
    }


}
