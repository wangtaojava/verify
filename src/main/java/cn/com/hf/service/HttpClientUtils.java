/**
 * llin 2019年4月11日上午11:58:13
 */
package cn.com.hf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author llin
 */
@Service
public class HttpClientUtils {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final ObjectMapper mapper = new ObjectMapper();
	
//	/**
//	 * 向第三方URL发送通知
//	 * @param url
//	 * @param notifyInde
//	 * @param multiValueMap
//	 * @return
//	 */
//	public String notification(String url, int notifyInde, MultiValueMap<String, String> multiValueMap){
//		
//		String respStr = "";
//		
//		StringBuilder sb  = new StringBuilder("向[");
//		sb.append(url).append("]第").append(notifyInde).append("次发送通知：").append(multiValueMap.toString());
//		logger.info(sb.toString());
//        try {
//			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//			requestFactory.setConnectTimeout(10*1000);
//			requestFactory.setReadTimeout(10*1000);
//			RestTemplate client = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			//  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(multiValueMap, headers);
//			//  执行HTTP请求
//			ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
//			respStr = response.getBody();
//			logger.info( url + ",通知应答<<<<<\n" + respStr);
//		} catch (Exception e) {
//			logger.error("通知[" + url + "]异常", e);
//		}
//        return respStr;
//	}
	
	public String httpSend(String url, Map<String, String> map, int socketTimeout, int connTimeout) throws Exception {
		
		List<String> keys = new ArrayList<String>(map.keySet());
		StringBuilder sb = new StringBuilder((map.size() +1) * 10);
		Collections.sort(keys); //根据元素的自然顺序,按升序进行排序
        for(String key : keys){
            sb.append(key).append("=");
            sb.append(map.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
        String sendMsg = sb.toString();
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (StringUtils.isNotBlank(sendMsg)) {
			sendMsg = sendMsg.trim();
			String[] paramsKeyValues = sendMsg.split("&");
			for (int i = 0; paramsKeyValues != null && i < paramsKeyValues.length; i++) {
				String paramsKeyValue = paramsKeyValues[i];
				if (StringUtils.isNotBlank(paramsKeyValue)) {
					paramsKeyValue = paramsKeyValue.trim();
					int index = paramsKeyValue.indexOf("=");
					if (index != -1 && index != (paramsKeyValue.length() - 1))
						params.add(new BasicNameValuePair(paramsKeyValue.substring(0, index),
								paramsKeyValue.substring(index + 1)));
				}
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout * 1000)
				.setConnectTimeout(connTimeout * 1000).build();// 设置请求和传输超时时间
		httpPost.setConfig(requestConfig);
		httpClient = HttpClients.createDefault();
		response = httpClient.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
		HttpParams params2 = response.getParams();
		
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			// handle response-message, return to the controller & application
			// modules
			result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
		} else {
			logger.info(
					"send message fail \n\n<<<<<<<<<<<<<<<<<<<<<<<<<the http-server return an invalid return code : "
							+ statusLine.getStatusCode() + "\n<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		}
		httpClient.close();
		return result;
	}
	
//    public JSONObject doPost(String url,JSONObject json){
//      CloseableHttpClient httpClient = null;
//      HttpPost post = new HttpPost(url);
//      JSONObject response = null;
//      try {
//      	httpClient = HttpClients.createDefault();
//          StringEntity s = new StringEntity(json.toString(), "gbk");
//          RequestConfig requestConfig = RequestConfig.custom()  
//                  .setConnectTimeout(2000).setConnectionRequestTimeout(2000)  
//                  .setSocketTimeout(2000).build(); 
//          s.setContentEncoding("gbk");
//          s.setContentType("application/json");//发送json数据需要设置contentType
//          post.setConfig(requestConfig);
//          post.setEntity(s);
//          HttpResponse res = httpClient.execute(post);
//          if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//        	  org.apache.http.HttpEntity entity = res.getEntity();
//              String result = EntityUtils.toString(entity);// 返回json格式：
//              response = JSONObject.fromObject(result);
//          }
//      } catch (Exception e) {
//          throw new RuntimeException(e);
//      }finally{
//			try {
//				if(httpClient != null)
//					  httpClient.close();
//			} catch (IOException e) {
//			}
//      }
//      return response;
//  }
	

}
