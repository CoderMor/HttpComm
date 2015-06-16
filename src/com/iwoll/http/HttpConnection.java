package com.iwoll.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class HttpConnection {

        private static final int CONNECT_TIME_OUT = 5 * 1000;
        private static final int READ_TIME_OUT = 10 * 1000;
        public static final String HTTP_GET = "GET";
        public static final String HTTP_POST = "POST";

        private HttpURLConnection connection;

        public HttpConnection() {}

        /**
         * GET请求获取HttpURLConnection对象
         * @param url 请求地址
         * @param params 请求参数
         * @param header 请求http头
         * @return HttpURLConnection
         */
        private HttpURLConnection doGetConnection(String url, Map<String, String> params, Map<String, String> header) {

            StringBuilder builder = new StringBuilder(url);
            Set<Map.Entry<String, String>> entries = null;
            if (params != null && !params.isEmpty()) {
                builder.append("?");
                entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {

                    try {
                        builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                    } catch (UnsupportedEncodingException e) {
                       
                    }
                }
                
                builder.deleteCharAt(builder.length() - 1);
            }

            try {

                URL u = new URL(builder.toString());
                connection = (HttpURLConnection) u.openConnection();
                connection.setRequestProperty("Accept-Encoding", "gzip");
                if (header != null && !header.isEmpty()) {
                    entries = header.entrySet();
                    for (Map.Entry<String, String> entry : entries) {

                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(CONNECT_TIME_OUT);
                connection.setReadTimeout(READ_TIME_OUT);
               
            } catch (MalformedURLException e) {
             
            } catch (IOException e) {
             
            }
            return connection;
        }

        /**
         * POST请求获取HttpURLConnection对象
         * @param url 请求地址
         * @param params 请求参数
         * @param header 请求http头
         * @return HttpURLConnection
         */
        private HttpURLConnection doPostConnection(String url, Map<String, String> params, Map<String, String> header) {

            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, String>> entries = null;
            if (params != null && !params.isEmpty()) {
                entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    try {
                        builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
                    } catch (UnsupportedEncodingException e) {
                       
                    }
                }
                
                builder.deleteCharAt(builder.length() - 1);
            }

            try {
                URL u = new URL(url);
                connection = (HttpURLConnection) u.openConnection();
                connection.setRequestProperty("Accept-Encoding", "gzip");
                if (header != null && !header.isEmpty()) {
                    entries = header.entrySet();
                    for (Map.Entry<String, String> entry : entries) {

                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(CONNECT_TIME_OUT);
                connection.setReadTimeout(READ_TIME_OUT);
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(builder.toString().getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
               
            } catch (MalformedURLException e) {
              
            } catch (IOException e) {
                
            }
            return connection;
        }
        
        /**
         * 分发请求
         * @param method 请求方法-GET、POST
         * @param url 请求地址
         * @param params 请求参数
         * @param header 请求http头
         * @return InputStream
         * @throws IOException
         */
        protected InputStream dispatchConnection(String method, String url, Map<String, String> params, Map<String, String> header) throws IOException {

            HttpURLConnection connection = null;
            if (method.equals("GET")) {
                connection = doGetConnection(url, params, header);
            } else if (method.equals("POST")) {
                connection = doPostConnection(url, params, header);
            }
            connection.connect();
            String encoding = connection.getContentEncoding();
            //首先判断服务器返回的数据是否支持gzip压缩
            if (encoding != null && encoding.contains("gzip")) {
            	//如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
                return new GZIPInputStream(connection.getInputStream());    
            }
            if (connection.getResponseCode() == 200)
                return connection.getInputStream();
                
            return null;
        }
        
        /**
         * 关闭连接
         */
        public void closeConnection(){

            if(connection != null)
                connection.disconnect();
            connection = null;
        }
}
