package com.hello.dao;

import com.hello.domain.RequestMetaInfo;
import com.hello.domain.RequestMetaInfoLocation;
import com.hello.domain.XMLDataBase;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.StampedLock;

/**
 * @author lesson
 * @date 2018/1/10 18:33
 */
@Component
@Slf4j
public class XMLDao  {

    private XMLDataBase xmlDataBase;

    private Set<String> urlSet=new HashSet<>();

    private volatile int nextIndex=1;

    private StampedLock stampedLock=new StampedLock();


    @PostConstruct
    @SneakyThrows
    public void init(){
        urlSet.add("/admin");  //不允许第三方设置以这个开头的url
        urlSet.add("/static");  //不允许第三方设置以这个开头的url
        Resource resource=new ClassPathResource("db.xml");
        JAXBContext context = JAXBContext.newInstance(XMLDataBase.class);
        Unmarshaller unmarshaller=context.createUnmarshaller();
        xmlDataBase= (XMLDataBase) unmarshaller.unmarshal(resource.getInputStream());
        List<RequestMetaInfo> requestMetaInfos= getDB();
        requestMetaInfos.stream().forEach(this::recordUrl);
        int size=requestMetaInfos.size();
        if (size>0){
            nextIndex=requestMetaInfos.get(size-1).getId()+1;
        }
    }

    private void recordUrl(RequestMetaInfo requestMetaInfo){
        if (!urlSet.add(requestMetaInfo.getUrl())){
            throw new RuntimeException("url:{"+requestMetaInfo.getUrl()+"} already exist..");
        }

    }


    public void reload(){
        init();
    }

    @SneakyThrows
    public void addMeta(RequestMetaInfo requestMetaInfo){
            long stamp=-1;
            try{
                stamp=stampedLock.writeLock();
                recordUrl(requestMetaInfo);
                requestMetaInfo.setId(nextIndex++);
                getDB().add(requestMetaInfo);
                persistDb();
            }finally {
                if (stamp!=-1){
                    stampedLock.unlockWrite(stamp);
                }
            }



    }

    private void persistDb() throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(XMLDataBase.class);
        String path=this.getClass().getClassLoader().getResource("db.xml").getPath();
        try(FileWriter fileWriter=new FileWriter(path)){
            context.createMarshaller().marshal(xmlDataBase,fileWriter);
        }
    }

    public List<RequestMetaInfo> showAllMetaInfo(){
        long stamp=-1;
        try{
            stamp=stampedLock.readLock();
            return Collections.unmodifiableList(getDB());
        }finally {
            if (stamp!=-1){
                stampedLock.unlockRead(stamp);
            }
        }

    }

    @SneakyThrows
    public void deleteById(int id){
        long stamp=-1;
        try{
            stamp=stampedLock.writeLock();
            List<RequestMetaInfo> requestMetaInfos= getDB();
            RequestMetaInfoLocation requestMetaInfoLocation=binaryFindById(id,0, getDB().size()-1);
            requestMetaInfos.set(requestMetaInfoLocation.getLocation(),null);  //TODO 用定时任务 定时去清理为null的对象
            persistDb();
        }finally {
            if (stamp!=-1){
                stampedLock.unlockWrite(stamp);
            }
        }

    }

    private List<RequestMetaInfo> getDB() {
        return xmlDataBase.getRequestMetaInfos();
    }

    @SneakyThrows
    public void updateById(int id,RequestMetaInfo requestMetaInfo){

        long stamp=-1;
        try{
            stamp=stampedLock.writeLock();
            List<RequestMetaInfo> requestMetaInfos= getDB();
            RequestMetaInfoLocation requestMetaInfoLocation=binaryFindById(id,0, getDB().size()-1);
            requestMetaInfos.set(requestMetaInfoLocation.getLocation(),requestMetaInfo);
            persistDb();
        }finally {
            if (stamp!=-1){
                stampedLock.unlockWrite(stamp);
            }
        }


    }

    public List<RequestMetaInfo> findById(int id){
        long stamp=-1;
        try{
            stamp=stampedLock.readLock();
            List<RequestMetaInfo> requestMetaInfos=new ArrayList<>();
            requestMetaInfos.add( binaryFindById(id,0,getDB().size()-1).getRequestMetaInfo());
            return requestMetaInfos;
        }finally {
            if (stamp!=-1){
                stampedLock.unlockRead(stamp);
            }
        }
    }

    private RequestMetaInfoLocation binaryFindById(int id, int beginIndex, int endIndex){
        List<RequestMetaInfo> requestMetaInfos= getDB();
        if (endIndex<=beginIndex){ //迭代结束
            RequestMetaInfo requestMetaInfo=requestMetaInfos.get(endIndex);
            if (requestMetaInfo.getId()==id){
                return new RequestMetaInfoLocation(endIndex,requestMetaInfo);
            }else{
                throw new RuntimeException("not find");
            }
        }
        int midleIndex=(endIndex-beginIndex)>>1;
        RequestMetaInfo requestMetaInfo=requestMetaInfos.get(midleIndex);
        if (requestMetaInfo.getId()<id){
            return binaryFindById(id,midleIndex+1,endIndex);
        }else if (requestMetaInfo.getId()>id){
            return binaryFindById(id,beginIndex,midleIndex-1);
        }else{
            return new RequestMetaInfoLocation(midleIndex,requestMetaInfo);
        }

    }
}
