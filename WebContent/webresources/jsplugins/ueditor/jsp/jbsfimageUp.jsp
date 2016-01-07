    <%@ page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8"%>
    <%@ page import="com.hawkfly.uploader.Uploader4DB" %>

    <%
    request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	Uploader4DB up = new Uploader4DB(request);
    up.setSavePath("upload");
    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
    up.setAllowFiles(fileType);
    up.setMaxSize(10000); //单位KB
    up.upload();
    int status = 0;
    if("SUCCESS".equals(up.getState())){
    	status = 1;
    }
    response.getWriter().print("{'original':'"+up.getOriginalName()+"','url':'"+up.getUrl()+"','title':'"+up.getTitle()+"','state':'"+up.getState()+"','id':'"+up.getId()+"'}");
    //response.getWriter().print("{\"original\":\""+up.getOriginalName()+"\",\"url\":\""+up.getUrl()+"\",\"title\":'"+up.getTitle()+"','state':'"+up.getState()+"\',\"status\":\""+status+"\",\"type\":\"ajax\",\"msg\":\""+up.getState()+"\",\"id\":\""+up.getId()+"\"}");
    %>