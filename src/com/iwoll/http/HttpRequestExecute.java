package com.iwoll.http;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * 
 * @author Esen
 *
 */
public class HttpRequestExecute implements ExecuteHttpRequest{

	@SuppressWarnings("unused")
	private CommonCallback httpResultCallback = null;
	public HttpRequestExecute(){
		httpResultCallback = new CommonCallback(){
			@Override
			public <T>void ResultCallback(T...t) {
				// TODO Auto-generated method stub
			}
		};
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			
			switch(msg.what){
			
				case 0:
					httpResultCallback.ResultCallback(msg.obj);
					break;
				case 1:
					httpResultCallback.ResultCallback("");
					break;
			}
		}
	};
	
	@SuppressWarnings("rawtypes")
	@Override
	public <T extends Map> void executeHttp(T t, int ThreadCount, String requestUrl,
			CommonCallback httpResultCallback) {
		// TODO Auto-generated method stub
		this.httpResultCallback = httpResultCallback;
		FutureTask<String> futureTask = new FutureTask<String>(new RealData(t,requestUrl));
		
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(futureTask);
		
		try {
			String result = futureTask.get();
			if(null != result || TextUtils.isEmpty(result)){
				Message message = new Message();
				message.what = 0 ;
				message.obj = result;
				mHandler.sendMessage(message);
			}else mHandler.sendEmptyMessage(1);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			executor.shutdownNow();
		}
	}
}
