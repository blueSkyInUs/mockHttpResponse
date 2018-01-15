package com.hello.url.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author lesson
 * @date 2018/1/12 10:51
 */
@Component
public class ControllerManagerUtil {

    @Autowired
    private List<BizController> bizControllers;

    Map<String,BizController> bizControllerMap;

    @PostConstruct
    public void init(){
        bizControllers.stream().forEach(this::addBizController);
    }

    private void addBizController(BizController bizController){
        bizControllerMap.put(bizController.showMyAcceptUrl(),bizController);
    }

    public BizController findBizControllerByUrl(String url){
        return bizControllerMap.get(url);
    }
}
