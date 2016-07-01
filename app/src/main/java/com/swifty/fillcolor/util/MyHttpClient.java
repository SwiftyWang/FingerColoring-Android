package com.swifty.fillcolor.util;

import com.swifty.fillcolor.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MyHttpClient {
    private DefaultHttpClient httpClient;
    private HttpPost httpPost;
    private HttpGet httpGet;
    private HttpEntity httpEntity;
    private HttpResponse httpResponse;
    private int timeoutConnection = 6000;
    private HttpParams httpParameters;
    private int timeoutSocket = 6000;

    public MyHttpClient() {
        httpParameters = new BasicHttpParams();// Set the timeout in
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);// Set the default socket timeout
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    }

    public MyHttpClient(int timeoutconn, int timeoutsock) {
        httpParameters = new BasicHttpParams();// Set the timeout in
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutconn);// Set
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutsock);
    }

    public String executePostRequest(String path, List<NameValuePair> params) {
        httpClient = new DefaultHttpClient(httpParameters);
        String ret = null;
        try {
            httpPost = new HttpPost(path);
            httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            addHeader(httpPost);
            httpPost.setEntity(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                ret = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private void addHeader(HttpRequestBase httprequest) {
        try {
            httprequest.setHeader("token", MyApplication.userToken);
        } catch (Exception e) {
            L.e(e.toString());
        }
    }

    public String executePostRequest(String path)
            throws Exception {
        httpClient = new DefaultHttpClient(httpParameters);
        String ret = null;
        this.httpPost = new HttpPost(path);
        addHeader(httpPost);
        httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = httpResponse.getEntity();
            ret = EntityUtils.toString(entity);
        }
        return ret;
    }

    public String executeGetRequest(String path)
            throws Exception {
        path = path.replace(" ", "%20");
        httpClient = new DefaultHttpClient(httpParameters);
        String ret = null;
        httpGet = new HttpGet(path);
        addHeader(httpGet);
        httpResponse = httpClient.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = httpResponse.getEntity();
            ret = EntityUtils.toString(entity);
        }
        return ret;
    }
}