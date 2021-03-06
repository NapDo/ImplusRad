package org.acra.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.log.ACRALog;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public final class HttpRequest
{
  private int connectionTimeOut = 3000;
  private String login;
  private int maxNrRetries = 3;
  private String password;
  private int socketTimeOut = 3000;
  
  private UsernamePasswordCredentials getCredentials()
  {
    if ((this.login != null) || (this.password != null)) {
      return new UsernamePasswordCredentials(this.login, this.password);
    }
    return null;
  }
  
  private HttpClient getHttpClient()
  {
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    localBasicHttpParams.setParameter("http.protocol.cookie-policy", "rfc2109");
    HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, this.connectionTimeOut);
    HttpConnectionParams.setSoTimeout(localBasicHttpParams, this.socketTimeOut);
    HttpConnectionParams.setSocketBufferSize(localBasicHttpParams, 8192);
    SchemeRegistry localSchemeRegistry = new SchemeRegistry();
    localSchemeRegistry.register(new Scheme("http", new PlainSocketFactory(), 80));
    if (ACRA.getConfig().disableSSLCertValidation()) {
      localSchemeRegistry.register(new Scheme("https", new FakeSocketFactory(), 443));
    }
    for (;;)
    {
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
      localDefaultHttpClient.setHttpRequestRetryHandler(new SocketTimeOutRetryHandler(localBasicHttpParams, this.maxNrRetries, null));
      return localDefaultHttpClient;
      localSchemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
    }
  }
  
  private HttpPost getHttpPost(URL paramURL, Map<?, ?> paramMap)
    throws UnsupportedEncodingException
  {
    HttpPost localHttpPost = new HttpPost(paramURL.toString());
    UsernamePasswordCredentials localUsernamePasswordCredentials = getCredentials();
    if (localUsernamePasswordCredentials != null) {
      localHttpPost.addHeader(BasicScheme.authenticate(localUsernamePasswordCredentials, "UTF-8", false));
    }
    localHttpPost.setHeader("User-Agent", "Android");
    localHttpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
    localHttpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
    localHttpPost.setEntity(new StringEntity(getParamsAsString(paramMap), "UTF-8"));
    return localHttpPost;
  }
  
  private String getParamsAsString(Map<?, ?> paramMap)
    throws UnsupportedEncodingException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramMap.keySet().iterator();
    if (localIterator.hasNext())
    {
      Object localObject1 = localIterator.next();
      if (localStringBuilder.length() != 0) {
        localStringBuilder.append('&');
      }
      Object localObject2 = paramMap.get(localObject1);
      if (localObject2 == null) {}
      for (Object localObject3 = "";; localObject3 = localObject2)
      {
        localStringBuilder.append(URLEncoder.encode(localObject1.toString(), "UTF-8"));
        localStringBuilder.append('=');
        localStringBuilder.append(URLEncoder.encode(localObject3.toString(), "UTF-8"));
        break;
      }
    }
    return localStringBuilder.toString();
  }
  
  public void sendPost(URL paramURL, Map<?, ?> paramMap)
    throws IOException
  {
    HttpClient localHttpClient = getHttpClient();
    HttpPost localHttpPost = getHttpPost(paramURL, paramMap);
    ACRA.log.d(ACRA.LOG_TAG, "Sending request to " + paramURL);
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext()) {
      localIterator.next();
    }
    HttpResponse localHttpResponse = localHttpClient.execute(localHttpPost, new BasicHttpContext());
    if (localHttpResponse != null)
    {
      if (localHttpResponse.getStatusLine() != null)
      {
        String str = Integer.toString(localHttpResponse.getStatusLine().getStatusCode());
        if ((str.startsWith("4")) || (str.startsWith("5"))) {
          throw new IOException("Host returned error code " + str);
        }
      }
      EntityUtils.toString(localHttpResponse.getEntity());
    }
  }
  
  public void setConnectionTimeOut(int paramInt)
  {
    this.connectionTimeOut = paramInt;
  }
  
  public void setLogin(String paramString)
  {
    this.login = paramString;
  }
  
  public void setMaxNrRetries(int paramInt)
  {
    this.maxNrRetries = paramInt;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public void setSocketTimeOut(int paramInt)
  {
    this.socketTimeOut = paramInt;
  }
  
  private static class SocketTimeOutRetryHandler
    implements HttpRequestRetryHandler
  {
    private final HttpParams httpParams;
    private final int maxNrRetries;
    
    private SocketTimeOutRetryHandler(HttpParams paramHttpParams, int paramInt)
    {
      this.httpParams = paramHttpParams;
      this.maxNrRetries = paramInt;
    }
    
    public boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext)
    {
      if ((paramIOException instanceof SocketTimeoutException))
      {
        if (paramInt <= this.maxNrRetries)
        {
          if (this.httpParams != null)
          {
            int i = 2 * HttpConnectionParams.getSoTimeout(this.httpParams);
            HttpConnectionParams.setSoTimeout(this.httpParams, i);
            ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut - increasing time out to " + i + " millis and trying again");
          }
          for (;;)
          {
            return true;
            ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut - no HttpParams, cannot increase time out. Trying again with current settings");
          }
        }
        ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut but exceeded max number of retries : " + this.maxNrRetries);
      }
      return false;
    }
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.HttpRequest
 * JD-Core Version:    0.7.0.1
 */