package com.iwoll.http;
/**
 * 通用回调接口
 * @author Esen
 *
 */
public interface CommonCallback extends BaseInterface {

	<T>void ResultCallback(T...t);
}
