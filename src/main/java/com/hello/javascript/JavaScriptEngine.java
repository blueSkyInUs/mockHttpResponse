package com.hello.javascript;

import com.alibaba.fastjson.JSONObject;
import com.hello.exception.JAVAScriptParserException;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author lesson
 * @date 2018/1/26 10:04
 */
@Component
public class JavaScriptEngine {

    @SneakyThrows({ScriptException.class,NoSuchMethodException.class})
    public  String invokerJSRequestScript(String script,String scriptFunction,JSONObject scriptFunctionArg){
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(new StringReader(script));
        if (engine instanceof Invocable){
            Invocable in= (Invocable) engine;
            return (String) in.invokeFunction(scriptFunction,scriptFunctionArg);
        }
        throw new JAVAScriptParserException();
    }

    @SneakyThrows({ScriptException.class,NoSuchMethodException.class})
    public  String invokerJSResponseScript(String script,String scriptFunction,JSONObject scriptFunctionArg,String originalResponseContent){
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(new StringReader(script));
        if (engine instanceof Invocable){
            Invocable in= (Invocable) engine;
            return (String) in.invokeFunction(scriptFunction,scriptFunctionArg,originalResponseContent);
        }
        throw new JAVAScriptParserException();
    }

    @SneakyThrows
    public static void main(String[] args) {

        Resource resource=new ClassPathResource("jsUtil/encrypt.js");
        String prefix=new String(Files.readAllBytes(Paths.get(resource.getURI())));//内嵌md5加密跟base64编码

        String script= new String(Files.readAllBytes(Paths.get("C:\\Users\\HP\\Desktop\\two.js")));
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.eval(new StringReader(prefix+script));
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("a","b");
        jsonObject.put("token","b2f84adc-48a6-4df6-ad9a-eacfa5cf3715");
        jsonObject.put("sid","eyJ2ZXJzaW9uIjoiMS4wLjAiLCJwcm9kdWN0Q29kZSI6InBheW1heCIsInVkaWQiOiJkZXZpY2VJZC04NjQ0NjAwMzE5MzU0MjMtZ2VuZXJhdGUtY2FyZG5pdSIsImNsaWVudCI6IkFuZHJvaWQiLCJwcm9kdWN0VmVyc2lvbiI6IjguMi4yIiwiYXBwQ2hhbm5lbCI6ImNhcmRuaXUiLCJhcHBWZXJzaW9uIjoiNy4xLjEiLCJ1c2VySWQiOiIyNDIyNjkyIn0=");
        jsonObject.put("sign","9279F1FE41ED3F0C1378AF596F6FDD4F");
        if (engine instanceof Invocable){
            Invocable in= (Invocable) engine;
            String result= (String) in.invokeFunction("handleRequestParams",jsonObject);
            System.out.println(result);
        }
        System.out.println(new String(Base64.getEncoder().encode("hahddahah".getBytes())));

//        System.out.println();
    }
}
