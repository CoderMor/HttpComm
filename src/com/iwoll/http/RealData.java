package com.iwoll.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 执行请求获取数据
 * @author Esen
 *
 */
public class RealData implements Callable<String>{

	private Map<String,String> requestParams;
	private String requestUrl = null;
	
	public RealData(Map<String,String> requestParams ,String requestUrl){
		
		this.requestParams = requestParams;
		this.requestUrl = requestUrl;
	}
	
	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		HttpConnection connection = new HttpConnection();
		StringBuilder builder = new StringBuilder();
        String len;
        InputStream inputStream = new BufferedInputStream(connection.dispatchConnection("GET", requestUrl, requestParams, null));
        if (inputStream != null) {
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((len = br.readLine()) != null) {
                    builder.append(len);
                }
            } catch (UnsupportedEncodingException e) {
              
            } catch (IOException e) {
               
            }finally{
            	inputStream.close();
            	connection.closeConnection();
            }
        }

        return builder.toString();
	
	}
}
