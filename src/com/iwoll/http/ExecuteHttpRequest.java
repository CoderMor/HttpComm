package com.iwoll.http;

import java.util.Map;

/**
 * 
 * @author Esen
 *
 */
public interface ExecuteHttpRequest {

	@SuppressWarnings("rawtypes")
	<T extends Map>void executeHttp(T t,int ThreadCount,String requestUrl,CommonCallback httpResultCallback);

}
