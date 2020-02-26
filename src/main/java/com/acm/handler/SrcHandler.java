package com.acm.handler;

import com.acm.common.annotation.IgnoreSecurity;
import com.acm.common.constant.StatusCode;
import com.acm.pojo.vo.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Api(description = "静态资源处理", tags = "SrcHandler", basePath = "/src")
@Controller
@RequestMapping("/src")
public class SrcHandler extends BaseHandler{

    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

    @ApiOperation(value = "上传", notes = "")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean upload(@RequestParam MultipartFile file){
        ResultBean resultBean = new ResultBean();
        System.out.println("enter");
        try {
            System.out.println(file.getContentType());
            System.out.println(file.getName());
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getSize());
        }
        catch (Exception e){

        }
        return resultBean;
    }

    @ApiOperation(value = "下载", notes = "")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    @IgnoreSecurity
    public ResultBean download(HttpServletResponse response){
        ResultBean resultBean = new ResultBean();
        try {
            File file = new java.io.File("/home/bnc/Videos/2020-02-16 18-39-44.flv");
            InputStream inputStream = new DataInputStream(new FileInputStream(file.getAbsoluteFile()));
//            System.out.println(inputStream.available());
            byte[] ret = new byte[inputStream.available()];
            inputStream.read(ret);
            response.reset();
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("video/x-flv");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(ret);
            toClient.flush();
            toClient.close();
        }
        catch (Exception e){
        }
        return resultBean;
    }

    @ApiOperation(value = "修改", notes = "")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean update(){
        ResultBean resultBean = new ResultBean();
        try {

        }
        catch (Exception e){

        }
        return resultBean;
    }

    @ApiOperation(value = "删除", notes = "")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delete(){
        ResultBean resultBean = new ResultBean();
        try {

        }
        catch (Exception e){

        }
        return resultBean;
    }
}
