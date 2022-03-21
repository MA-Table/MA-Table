package com.apps.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class AdminCon {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminCon.class);

	@RequestMapping("/")
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "index";
	}

	@RequestMapping("login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		logger.info("QCon login :::");
    	String id = request.getParameter("qid");
    	String pw = request.getParameter("qpassword");
    	String targetUrl = "https://statapi.nl.go.kr/api/login_ex.ajax?user_id=" + id + "&user_pw=" + pw + "&api=JTR6UJXZITJ0CIN";
    	String result = "";
    	
		// TODO Auto-generated method stub
    	try {
			URL url = new URL(targetUrl);
			URLConnection conn = url.openConnection();

			BufferedReader bf;
			bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			
			result = bf.readLine();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	JsonParser jsonParser = new JsonParser();
    	JsonObject jsonObject = (JsonObject)jsonParser.parse(result);
    	
    	logger.info("request ::: " + request.getSession().getServletContext().getContextPath());
    	
    	if((jsonObject.get("status").toString()).equals("200")) {
    		String appRoot = "C:" + System.getProperty("file.separator") + "javaticket";
    		String adminId = id;
    		String qipaddr = "graph.nl.go.kr"; //클릭 서버
    		
    		logger.info("[goToStatVisualPage] appRoot ::: " + appRoot + ", adminId ::: " + adminId);
    		logger.info("[goToStatVisualPage] qipaddr ::: " + qipaddr);
    		
    		String qpasswd = "ndl%it2010";
    		
    		String userDirectory = "NLSUMVIEWER";

    		String serviceType = "https";
    		String qport = "8088";

    		String qlikUrl = serviceType + "://" + qipaddr + ":" + qport + "/hub";
    		
    		logger.info("[goToStatVisualPage] qlikUrl First ::: " + qlikUrl);

    		QSTicket ticket = new QSTicket();
    		logger.info("[goToStatVisualPage] ticket");
    		
    		String ticketStr = ticket.getTicket(appRoot, adminId, userDirectory, qipaddr, qpasswd);
    		logger.info("[goToStatVisualPage] ticketStr ::: " + ticketStr);
    		
    		qlikUrl += "?qlikTicket=" + ticketStr;
    		
    		logger.info("[goToStatVisualPage] qlikUrl ticketed ::: " + qlikUrl);
    		try {
    			response.sendRedirect(qlikUrl);
    		} catch (Exception e) {
    			
    		}
    		
    	} else {
    		try {
    			response.sendRedirect("http://graph.nl.go.kr:8009");
    		} catch (Exception e) {
    			
    		}
    	}
	}
		
}






















































