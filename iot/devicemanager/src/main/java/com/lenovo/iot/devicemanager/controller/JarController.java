package com.lenovo.iot.devicemanager.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.bo.App;
import com.lenovo.iot.devicemanager.model.bo.AppSource;
import com.lenovo.iot.devicemanager.service.IAppService;
import com.lenovo.iot.devicemanager.service.IAppSourceService;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.MDFIVE;
import com.lenovo.iot.devicemanager.util.redis.RedisManager;

@RequestMapping("/JarController")
@Controller
public class JarController {

    protected static final Log log = LogFactory.getLog(JarController.class);
    
    private static final String ATTACHMENT_DIR_TEMP = File.separator + "devicemanager_temp" + File.separator;
    private static final String ATTACHMENT_DIR = File.separator + "devicemanager_files" + File.separator;
    private static final String MetaFileName = "self_desc.json";

    @Autowired
    private IAppService _AppServiceImpl;

    @Autowired
    private IAppSourceService _AppSourceServiceImpl;
    
	@Autowired
	private PlatformTransactionManager transactionManager;

//    @RequestMapping(value = "/springUpload.do", method = RequestMethod.POST)
//    @CrossOrigin(origins="*")
//    @ResponseBody
//    @RequiresPermissions({"procmodule:manage"})
//    public Object springUpload(HttpServletRequest request) {
//        JSONObject _JSONObjectControllerResult  = new JSONObject();
//
//        String targetFile = "";
//        boolean file_valid = false;
//        
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//        if(multipartResolver.isMultipart(request)) {
//            //将request变成多部分request
//            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
//            //获取multiRequest 中所有的文件名
//            Iterator iter = multiRequest.getFileNames();
//            while(iter.hasNext())
//            {
//                //一次遍历所有文件
//                MultipartFile file = multiRequest.getFile(iter.next().toString());
//                if(file != null)
//                {
//                    String fileName = file.getName();
//                    System.out.println("fileName--->>>>"+fileName+"---->>>>"+file.getOriginalFilename());
//
//                    //上传
//                    String uploadGenerateForder = System.getProperty("user.home") + ATTACHMENT_DIR;
//                    File dir = new File(uploadGenerateForder);
//                    if(!dir.exists() && !dir.isDirectory())      
//                    {
//                        dir.mkdir();    
//                    }
//                    String targetDir = uploadGenerateForder + UUID.randomUUID().toString().replace("-", "").toUpperCase() + "/";
//                    targetFile = targetDir + file.getOriginalFilename();
//                    
//                    dir = new File(targetDir);
//                    if(!dir.exists() && !dir.isDirectory()) {
//	                    dir.mkdir();
//	                }
//
//                    try {
//                        file.transferTo(new File(targetFile));
//                        file_valid = true;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    
//                    break;
//                }
//            }
//        } else {
//            _JSONObjectControllerResult.put("actionResultFlag", false);
//            _JSONObjectControllerResult.put("actionResultDesc", "no file upload");
//            return _JSONObjectControllerResult;
//        }
//
//        if(!file_valid) {
//            _JSONObjectControllerResult.put("actionResultFlag", false);
//            _JSONObjectControllerResult.put("actionResultDesc", "no file upload");
//            return _JSONObjectControllerResult;
//        }
//        
//        //check md5 whether duplicate or not
//        String md5Str = MDFIVE.getFileMD5(new File(targetFile));
//        Boolean isAlreadyAppName = checkAppMd5AndAppName_Same(md5Str);
//        if(isAlreadyAppName){
//            _JSONObjectControllerResult.put("actionResultFlag", false);
//            _JSONObjectControllerResult.put("actionResultDesc", "md5 repeat");
//            return _JSONObjectControllerResult;
//        }
//        
//        //read self description
//        String descJSONStr = RedisManager.readFixFileFromJarDescJSON(targetFile, MetaFileName);
//        if(null == descJSONStr || descJSONStr.trim().length()<=0){
//            _JSONObjectControllerResult.put("actionResultFlag", false);
//            _JSONObjectControllerResult.put("actionResultDesc", "jar descript file is null");
//            return _JSONObjectControllerResult.toJSONString();
//        }
//
//        JSONObject descJSONObject = (JSONObject) JSONObject.parse(descJSONStr);
//        String apptype = descJSONObject.getString("apptype");
//        String appname = descJSONObject.getString("appname");
//        String classname = descJSONObject.getString("classname");
//        String version = descJSONObject.getString("version");
//        String descStr = descJSONObject.getString("desc");
////        if(null == JSONArray_attributes || JSONArray_attributes.size() <= 0){
////            return null;
////        }
//
//        long companyid = LoginedAccountHolder.getLoginedAccount().getCompanyId();
//        App _App = new App();
//        _App.setApptype(apptype);
//        _App.setAppname(appname);
//        _App.setCompanyid(companyid);
//        _App.setClassname(classname);
//        _App.setAppversion(version);
//        _App.setAppfilename(targetFile);
//        _App.setCreatetimeat(System.currentTimeMillis());
//        _App.setMdfive(md5Str);
//        _App.setAppdesc(descStr);
//
//        String appIdPrimaryKey_ForeignhKey = _AppServiceImpl.doInsertApp(_App);
//        if(null == appIdPrimaryKey_ForeignhKey || appIdPrimaryKey_ForeignhKey.trim().length() == 0){
//            log.error("error:null is appIdPrimaryKey_ForeignhKey");
//            return null;
//        }
//
//        JSONArray JSONArray_attributes = descJSONObject.getJSONArray("attributes");
//        if(JSONArray_attributes != null) {
//	        List<String> list4AppSourceThisTransction = new ArrayList<String>();
//	        for(int i = 0 ; i < JSONArray_attributes.size() ; i++){
//	            JSONObject currentJSONObject4Attribute = JSONArray_attributes.getJSONObject(i);
//	            String attribute_name = currentJSONObject4Attribute.getString("name");
//	            String attribute_desc = currentJSONObject4Attribute.getString("desc");
//	            String attribute_unit = currentJSONObject4Attribute.getString("unit");
//	            String attribute_type = currentJSONObject4Attribute.getString("type");
//	            AppSource _AppSource = new AppSource();
//	            _AppSource.setAppname(appname);
//	            _AppSource.setSourcename(attribute_name);
//	            _AppSource.setSourcedisplayname(attribute_desc);
//	            _AppSource.setSourcedefault("");
//	            _AppSource.setSourcedatatype(attribute_type);
//	            _AppSource.setSourceunit(attribute_unit);
//	            _AppSource.setAppid(appIdPrimaryKey_ForeignhKey);
//	            _AppSource.setCreatetimeat(System.currentTimeMillis());
//	            String currentAppSourceKey = _AppSourceServiceImpl.doInsertAppSource(_AppSource);
//	            list4AppSourceThisTransction.add(currentAppSourceKey);
//	        }
//        }
//        
//        // 查询插入结果，并返回给客户端
//        App _AppSearchParameter = new App();
//        _AppSearchParameter.setId(Long.valueOf(appIdPrimaryKey_ForeignhKey));
//        List<App> list4AppCallbackshow = _AppServiceImpl.findAppByParameter(_AppSearchParameter);
//
//        String actionResultDesc = "";
//        if(null == list4AppCallbackshow || list4AppCallbackshow.size() <= 0){
//            actionResultDesc = "null == list4AppCallbackshow|| list4AppCallbackshow.size()<=0";
//            log.error(actionResultDesc);
//            return null;
//        }
//        if(list4AppCallbackshow.size() > 1){
//            actionResultDesc ="list4AppCallbackshow.size()>1";
//            log.error(actionResultDesc);
//            return null;
//        }
//
//        App _AppCallback = list4AppCallbackshow.get(0);
//        if(null == _AppCallback){
//            actionResultDesc = "null == _AppCallback";
//            log.error(actionResultDesc);
//            return null;
//        }
//
//        AppSource _AppSourceSearchParameter = new AppSource();
//        _AppSourceSearchParameter.setAppid(String.valueOf(_AppCallback.getId()));
//        List<AppSource> list4AppSourceCallbackShow =  _AppSourceServiceImpl.findAppSourceByParameter(_AppSourceSearchParameter);
//
//        _JSONObjectControllerResult.put("app",_AppCallback);
//        _JSONObjectControllerResult.put("appSource",list4AppSourceCallbackShow);
//        _JSONObjectControllerResult.put("actionResultFlag",true);
//        System.out.println("_JSONObjectControllerResult.toJSONString()--->>>>>"+_JSONObjectControllerResult.toJSONString());
//
//        return _JSONObjectControllerResult;
//    }

    
    
    @RequestMapping(value = "/upload_check.do", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
    @CrossOrigin(origins="*")
    @ResponseBody
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject upload_check(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        
        String uploadTempForder = System.getProperty("user.home") + ATTACHMENT_DIR_TEMP;
        File baseDir = new File(uploadTempForder);
        if(!baseDir.exists() && !baseDir.isDirectory())      
        {
        	baseDir.mkdir();    
        }

	    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;	
	    MultiValueMap<String, MultipartFile> map = multipartRequest.getMultiFileMap();
		if(map != null) {
            List<Pair<String, String>> files = new ArrayList<Pair<String, String>>();
            Iterator iter = map.keySet().iterator();
            while(iter.hasNext()) {
            	String str = (String) iter.next();
            	List<MultipartFile> fileList =  map.get(str);
					
				for(MultipartFile file : fileList) {
                    //上传
					String dir = UUID.randomUUID().toString().replace("-", "").toUpperCase();
					String filename = file.getOriginalFilename();
                    String targetDir = uploadTempForder + dir + File.separator;
                    String targetFile = targetDir + filename;
                    
                    File tempDir = new File(targetDir);
                    if(!tempDir.exists() && !tempDir.isDirectory()) {
                    	tempDir.mkdir();
	                }

                    try {
                        file.transferTo(new File(targetFile));
                        files.add(new Pair<String, String>(dir, filename));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
				}
			}
            
            JSONArray files_obj = new JSONArray();
            if(files.size() > 0) {
            	boolean hasError = false;
            	
            	for(Pair<String, String> pair : files) {
            		String filepath = pair.getValue0();
            		String filename = pair.getValue1();
            		String file = uploadTempForder + filepath + File.separator + filename;
            		
            		JSONObject fileObj = new JSONObject();
                    fileObj.put("path", filepath);
                    fileObj.put("file", filename);
            		
                    //check md5 whether duplicate or not
                    String md5Str = MDFIVE.getFileMD5(new File(file));
                    Boolean isAlreadyAppName = checkAppMd5AndAppName_Same(md5Str);
                    if(isAlreadyAppName){
                    	fileObj.put("result", false);
                    	fileObj.put("error", "文件已存在");
                    	
                    	hasError = true;
                    } else {
	                    //read self description
	                    String descJSONStr = RedisManager.readFixFileFromJarDescJSON(file, MetaFileName);
	                    if(null == descJSONStr || descJSONStr.trim().length() <= 0){
	                    	fileObj.put("result", false);
	                    	fileObj.put("error", "文件格式不正确（缺少元数据）");
	                    	
	                    	hasError = true;
	                    } else {
	                    	fileObj.put("result", true);
	                    	fileObj.put("error", "");
	                    	
	                        JSONObject descObj = (JSONObject)JSONObject.parse(descJSONStr);
	                        String apptype = descObj.getString("apptype");
	                        String appname = descObj.getString("appname");
	                        String classname = descObj.getString("classname");
	                        String version = descObj.getString("version");
	                        String desc = descObj.getString("desc");
	                        JSONArray attributes = descObj.getJSONArray("attributes");
	                        
	                        JSONArray attributes_target = new JSONArray();
	                        if(attributes != null) {
		              	        for(int i = 0 ; i < attributes.size() ; i++){
		              	            JSONObject current_attribute = attributes.getJSONObject(i);
		              	            String attribute_name = current_attribute.getString("name");
		              	            String attribute_desc = current_attribute.getString("desc");
		              	            String attribute_unit = current_attribute.getString("unit");
		              	            String attribute_type = current_attribute.getString("type");
		              	            
		              	            JSONObject attrObj = new JSONObject();
			              	        attrObj.put("name", attribute_name);
			              	        attrObj.put("desc", attribute_desc);
			              	        attrObj.put("unit", attribute_unit);
			              	        attrObj.put("type", attribute_type);
			              	        attrObj.put("default", "");
		              	            
			              	        attributes_target.add(attrObj);
		              	        }
		                    }
	                        
	                        fileObj.put("apptype", apptype);
	                        fileObj.put("appname", appname);
	                        fileObj.put("classname", classname);
	                        fileObj.put("version", version);
	                        fileObj.put("desc", desc);
	                        fileObj.put("attributes", attributes_target);
	                    }
                    }
 

                    files_obj.add(fileObj);
            	}
            	
            	result.put("result", !hasError);
            	result.put("error", "");
            	result.put("apps", files_obj);
            	return result;
            } else {
            	result.put("result", false);
            	result.put("error", "未上传文件(读取文件失败)");
                return result;
            }
		} else {
        	result.put("result", false);
        	result.put("error", "未上传文件");
            return result;
        }
    }
    
    @RequestMapping(value = "/upload.do", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
    @CrossOrigin(origins="*")
    @ResponseBody
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject upload(@RequestBody JSONObject app_object, HttpServletRequest request, HttpServletResponse response) {
        JSONObject result  = new JSONObject();
        
        String uploadTempForder = System.getProperty("user.home") + ATTACHMENT_DIR_TEMP;
        String uploadForder = System.getProperty("user.home") + ATTACHMENT_DIR;
        JSONArray apps = app_object.getJSONArray("apps");
        if(apps != null && apps.size() > 0) {
        	boolean hasError = false;
	        for(int i = 0 ; i < apps.size() ; i++) {
	            JSONObject app = apps.getJSONObject(i);
	            boolean app_check = app.getBoolean("result");
	            if(!app_check) {
	            	hasError = true;
	            	break;
	            }
	            
	            String filename = app.getString("file");
	            String filepath = app.getString("path");
	            String file_path = uploadTempForder + filepath + File.separator + filename;
    	        File file = new File(file_path);
    	        if(!file.exists()) {
	            	hasError = true;
	            	break;
    	        }
	        }
	        
	        if(hasError) {
            	result.put("result", false);
            	result.put("error", "文件错误");
	        } else {
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				TransactionStatus status = transactionManager.getTransaction(def);
				try {
    		        long companyid = LoginedAccountHolder.getLoginedAccount().getCompanyId();

    		        for(int i = 0 ; i < apps.size() ; i++) {
			            JSONObject app = apps.getJSONObject(i);
			            String filename = app.getString("file");
			            String filepath = app.getString("path");
			            String apptype = app.getString("apptype");
			            String appname = app.getString("appname");
			            String classname = app.getString("classname");
			            String version = app.getString("version");
			            String descStr = app.getString("desc");
			            JSONArray JSONArray_attributes = app.getJSONArray("attributes");
	//		            if(null == JSONArray_attributes || JSONArray_attributes.size() <= 0){
	//		                _JSONObjectControllerResult.put("result", false);
	//		                _JSONObjectControllerResult.put("error", "no app upload");
	//		            } else {

			            //将文件从临时文件夹移动至目标文件夹
			            String file_path_abs_temp = uploadTempForder + filepath + File.separator;
	    	        	String file_path_abs = uploadForder + filepath + File.separator;
	    	        	FileUtils.moveDirectoryToDirectory(new File(file_path_abs_temp), new File(uploadForder), true);
	    	        	
		    	        File file_new = new File(file_path_abs + filename);
	    	            String md5Str = MDFIVE.getFileMD5(file_new);
	    	
	    		        App _App = new App();
	    		        _App.setApptype(apptype);
	    		        _App.setAppname(appname);
	    		        _App.setCompanyid(companyid);
	    		        _App.setClassname(classname);
	    		        _App.setAppversion(version);
	    		        _App.setAppfilename(file_new.getAbsolutePath());
	    		        _App.setCreatetimeat(System.currentTimeMillis());
	    		        _App.setMdfive(md5Str);
	    		        _App.setAppdesc(descStr);
	    		
    			        String appIdPrimaryKey_ForeignhKey = _AppServiceImpl.doInsertApp(_App);
    			        if(JSONArray_attributes != null) {
    				        for(int j = 0 ; j < JSONArray_attributes.size() ; j++){
    				            JSONObject currentJSONObject4Attribute = JSONArray_attributes.getJSONObject(j);
    				            String attribute_name = currentJSONObject4Attribute.getString("name");
    				            String attribute_desc = currentJSONObject4Attribute.getString("desc");
    				            String attribute_unit = currentJSONObject4Attribute.getString("unit");
    				            String attribute_type = currentJSONObject4Attribute.getString("type");
    				            
    				            AppSource _AppSource = new AppSource();
    				            _AppSource.setAppname(appname);
    				            _AppSource.setSourcename(attribute_name);
    				            _AppSource.setSourcedisplayname(attribute_desc);
    				            _AppSource.setSourcedefault("");
    				            _AppSource.setSourcedatatype(attribute_type);
    				            _AppSource.setSourceunit(attribute_unit);
    				            _AppSource.setAppid(appIdPrimaryKey_ForeignhKey);
    				            _AppSource.setCreatetimeat(System.currentTimeMillis());
    				            
    				            _AppSourceServiceImpl.doInsertAppSource(_AppSource);
    				        }
    			        }
					}
					
					transactionManager.commit(status);
			        result.put("result", true);
			        result.put("error", "");
				} catch(Exception e) {
					e.printStackTrace();
					transactionManager.rollback(status);
			        result.put("result", false);
			        result.put("error", e.getMessage());
				}
	        }
        } else {
        	result.put("result", false);
        	result.put("error", "未上传文件");
        }
        
        return result;
    }

    //下载
    @RequestMapping(value = "/download.url", method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value="id", required=true) Long id, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){
    	App app = new App();
    	app.setId(id);
    	List<App> appList = _AppServiceImpl.findAppByParameter(app);
    	if(appList.size() != 1) {
    		_HttpServletResponse.setStatus(404);
            return;
        }

        // 读取文件
        String fullPath = appList.get(0).getAppfilename();
        File file = new File(fullPath);
        String fileName = file.getName();
        
        long fileLength = file.length();
        if(file.exists()) {
            OutputStream _OutputStream = null;
            try {
                javax.servlet.ServletOutputStream _ServletOutputStream = _HttpServletResponse.getOutputStream();
                _OutputStream = new BufferedOutputStream(_ServletOutputStream);
                _HttpServletResponse.setContentType("application/x-msdownload");
                _HttpServletResponse.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
                _HttpServletResponse.setHeader("Content-Length", String.valueOf(fileLength));

                InputStream is = new FileInputStream(file);
                OutputStream os = _HttpServletResponse.getOutputStream();

                byte[] b = new byte[2048];
                while (is.read(b) != -1) {
                    os.write(b);
                }

                is.close();
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                
                _HttpServletResponse.setStatus(401);
            } finally {
                if(_OutputStream != null){
                    try {
                        _OutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
        	_HttpServletResponse.setStatus(404);
        }
    }

    private Boolean checkAppMd5AndAppName_Same(String md5){
        Boolean result = false;
        App _App = new App();
        _App.setMdfive(md5);
        List<App> list4App = _AppServiceImpl.findAppByParameter(_App);
        if(list4App.size()<=0){
            result = false;
        }else{
            result = true;
        }

        return result;
    }
    
    @RequestMapping(value = "/erase.do", produces = { "application/json;charset=UTF-8" })
    @CrossOrigin(origins="*")
    @ResponseBody
    @RequiresPermissions({"procmodule:manage"})
    public JSONObject erase(@RequestBody JSONObject app, HttpServletRequest request, HttpServletResponse response) {
    	//"delete from app where id = ?"
    	//"delete from app_source where appid = ?"
        return new JSONObject();
    }

}
