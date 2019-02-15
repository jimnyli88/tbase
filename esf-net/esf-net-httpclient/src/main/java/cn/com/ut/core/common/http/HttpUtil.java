package cn.com.ut.core.common.http;

import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static String sentGet(String url) {

		CloseableHttpClient httpClient = HttpClients.custom().build();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000)
				.setConnectTimeout(30000).build();
		HttpGet httpPost = new HttpGet(url);
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setConfig(requestConfig);
		String result = null;
		try {
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");

		} catch (ConnectionPoolTimeoutException e) {
			e.printStackTrace();

		} catch (ConnectTimeoutException e) {
			e.printStackTrace();

		} catch (SocketTimeoutException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
		}

		return result;

	}

}
