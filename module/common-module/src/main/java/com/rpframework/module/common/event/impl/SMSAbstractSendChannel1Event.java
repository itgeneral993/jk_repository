package com.rpframework.module.common.event.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rpframework.module.common.event.ISMSEvent;

public abstract class SMSAbstractSendChannel1Event implements ISMSEvent {
	final Logger logger = LoggerFactory.getLogger(getClass());
	protected String charset = "UTF-8";
	protected String serverIP = "222.73.117.158";
	protected String serverPort = "80";
	protected String accout = "jiekou-cs-01";
	protected String pswd = "Tch147200";
	
	public abstract void initForSet();
	
	@Override
	public int getChannelSend() {
		return 1;
	}

	@Override
	public boolean sendSMS(String phone, String content) {
		/**
		 * http://222.73.117.158/msg/HttpSendSM?account=111111&pswd=123456
		 * &mobile=18900000000,13800138000&msg=test&needstatus=true&product=99999
		 */
		HttpClient client = HttpClients.createDefault();
		String mobile = phone;
		String msg = content;
	
		List<NameValuePair> params = new ArrayList<NameValuePair>();  
	    params.add(new BasicNameValuePair("account", accout));  
	    params.add(new BasicNameValuePair("pswd",pswd)); 
	    params.add(new BasicNameValuePair("mobile",mobile));
		try {
			params.add(new BasicNameValuePair("msg",URLEncoder.encode(msg, charset)));
			params.add(new BasicNameValuePair("needstatus", "true"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		String param = URLEncodedUtils.format(params,charset);
		String url = "http://"+serverIP+":"+serverPort+"/msg/HttpSendSM?" + param;
		
		logger.info(url);
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		try {
			response = client.execute(post);
			
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, charset);
			logger.info("result:"+result);
			
			return true;
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		SMSAbstractSendChannel1Event s = new SMSAbstractSendChannel1Event() {
			
			@Override
			public void initForSet() {
				
			}
		};
		
		s.sendSMS("15390891113", "TEST....");
	}

}