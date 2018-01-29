package com.hello.dao;

import com.hello.domain.RequestMetaInfo;
import com.hello.domain.RequestMetaInfoLocation;
import com.hello.domain.XMLDataBase;
import com.hello.exception.ResourceFobiddenAccessException;
import com.hello.exception.URLNotFoundException;
import com.hello.util.UrlUniformer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
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

    private volatile int nextIndex=1;

    private StampedLock stampedLock=new StampedLock();

    private Map<String,RequestMetaInfo> requestMetaInfoMap=new HashMap<>();


    @PostConstruct
    @SneakyThrows
    public void init(){
        requestMetaInfoMap.put("/admin",null);  //不允许第三方设置以这个开头的url
        requestMetaInfoMap.put("/static",null);  //不允许第三方设置以这个开头的url

        JAXBContext context = JAXBContext.newInstance(XMLDataBase.class);
        Unmarshaller unmarshaller=context.createUnmarshaller();

        String parentPath=System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        if (!parentPath.endsWith(separator)){
            parentPath=parentPath+separator;
        }

        File file=new File(parentPath+"db.xml");
        if (!file.exists()){
            file.createNewFile();
        }
        if (file.length()==0){
            xmlDataBase=new XMLDataBase();
            return;
        }
        xmlDataBase= (XMLDataBase) unmarshaller.unmarshal(file);
        List<RequestMetaInfo> requestMetaInfos= getDB();
        requestMetaInfos.stream().forEach(this::recordUrl);
        int size=requestMetaInfos.size();
        if (size>0){
            nextIndex=requestMetaInfos.get(size-1).getId()+1;
        }
    }

    private void recordUrl(RequestMetaInfo requestMetaInfo){
        if (requestMetaInfo.getUrl().equals("/admin")||requestMetaInfo.getUrl().equals("/static")){
            throw  new ResourceFobiddenAccessException();
        }
        if (requestMetaInfoMap.containsKey(requestMetaInfo.getUrl())){
            log.info("already exist");
            throw  new ResourceFobiddenAccessException();
        }
        requestMetaInfoMap.put(UrlUniformer.adjustUrl(requestMetaInfo.getUrl()),requestMetaInfo);
    }

    private void deleteUrl(RequestMetaInfo requestMetaInfo){
        requestMetaInfoMap.remove(UrlUniformer.adjustUrl(requestMetaInfo.getUrl()));
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

        String parentPath=System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        if (!parentPath.endsWith(separator)){
            parentPath=parentPath+separator;
        }
        File file=new File(parentPath+"db.xml");
        if (!file.exists()){
            file.createNewFile();
        }
        try(FileWriter fileWriter=new FileWriter(file)){
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
            requestMetaInfos.remove(requestMetaInfoLocation.getLocation());
            deleteUrl(requestMetaInfoLocation.getRequestMetaInfo());
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
            if (!requestMetaInfo.getUrl().equals(requestMetaInfoLocation.getRequestMetaInfo().getUrl())){
                throw  new ResourceFobiddenAccessException();
            }
            requestMetaInfos.set(requestMetaInfoLocation.getLocation(),requestMetaInfo);
            deleteUrl(requestMetaInfo);
            recordUrl(requestMetaInfo);
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

    public RequestMetaInfo findByUrl(String url){
        return requestMetaInfoMap.get(UrlUniformer.adjustUrl(url));
    }

    private RequestMetaInfoLocation binaryFindById(int id, int beginIndex, int endIndex){
        log.info("{},{},{}",id,beginIndex,endIndex);
        List<RequestMetaInfo> requestMetaInfos= getDB();
        int midleIndex=(endIndex-beginIndex)>>1;
        if (midleIndex==0){
            RequestMetaInfo end=requestMetaInfos.get(endIndex);
            RequestMetaInfo begin=requestMetaInfos.get(beginIndex);
            if (end.getId()==id){
                return new RequestMetaInfoLocation(endIndex,end);
            }else if(begin.getId()==id){
                return new RequestMetaInfoLocation(beginIndex,begin);
            }else{
                throw new URLNotFoundException();
            }
        }
        midleIndex=beginIndex+midleIndex;
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
