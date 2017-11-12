package com.pachong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Headerpachong {
	public static Log logger = LogFactory.getLog(Headerpachong.class);  //日志	
private static String loginurl="https://rapaport.auth0.com/usernamepassword/login";
	
	/**数据屏和数据列表的    主地址2 https://member.rapnet.com/RapNet/**/
//	public static String dataroot="https://member.rapnet.com/RapNet/";
	
	/**数据屏和数据列表的    主地址1 https://member.rapnet.com**/
	public final static String root="https://member.rapnet.com";
	
	/****第一页数据  表格的链接 交易屏 **/
	public final static String pagedata1="https://member.rapnet.com/RapNet/PriceGrid/GridResults.aspx";
	
	
	/****退出登陆 **/
	public final static String logout="https://member.rapnet.com/Login/LogOut.aspx";
	
	
	/***产品详情访问的url  DiamondID=%  是要替换的产品的id**/
	public final static  String efdurl="https://member.rapnet.com/RapNet/Search/ExpandFullDetails.aspx"
			+ "?DiamondID=%&Page=1&RowID=0&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0";//详情的基本链接 DiamondID的%是要替换成产品信息id
//	public void init()  
//    {  
//         try {  
//            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null,  
//                            new TrustSelfSignedStrategy())  
//                    .build();  
//            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;  
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
//                    sslcontext,hostnameVerifier);  
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
//                    .register("http", PlainConnectionSocketFactory.getSocketFactory())  
//                    .register("https", sslsf)  
//                    .build();  
//            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
//            // Increase max total connection to 200  
//       //     poolConnManager.setMaxTotal(maxTotalPool);  
//            // Increase default max connection per route to 20  
//          //  poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);  
//        //    SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();  
//        //    poolConnManager.setDefaultSocketConfig(socketConfig);  
//        } catch (Exception e) {  
//            log.error("InterfacePhpUtilManager init Exception"+e.toString());  
//        }  
//    }  
//     public CloseableHttpClient getConnection()  
//    {  
//        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)  
//                .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();  
//        CloseableHttpClient httpClient = HttpClients.custom()  
//                    .setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();  
//        if(poolConnManager!=null&&poolConnManager.getTotalStats()!=null)  
//        {  
//            log.info("now client pool "+poolConnManager.getTotalStats().toString());  
//        }  
//        return httpClient;  
//    } 
//    
//	/**创建一个待cookie的httpclient工具类**/
//	public static CloseableHttpClient newhttpclient() throws UnsupportedEncodingException{
//     	RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)//标准Cookie策略
//     		.setRedirectsEnabled(false)
//     		   //与服务器连接超时时间：httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接超时时间  
//                .setConnectTimeout(3000)  
//              .setSocketTimeout(3000)
//                .build();
//	   CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();//设置进去
//	  
//	    	return httpClient;
//	}
	/**登陆操作  提交使用的post配置**/
	public static HttpPost newloginPost1() throws UnsupportedEncodingException{
		logger.error("提交登陆数据");
	 
		HttpPost post=new HttpPost(loginurl);
    	List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
    	param.add(new BasicNameValuePair("client_id", "FsYXds0gouXuOtlRFnYsdAjF8nysbRcp"));
    	param.add(new BasicNameValuePair("connection", "Username-Password-Authentication"));
      	param.add(new BasicNameValuePair("username", "90341"));//账号
     	param.add(new BasicNameValuePair("password", "335808!@#sh"));//密码
    	param.add(new BasicNameValuePair("popup", "false"));
    	param.add(new BasicNameValuePair("redirect_uri", "https://member.rapnet.com/Login/LoginPage.aspx"));
    	param.add(new BasicNameValuePair("response_type", "code"));
    	param.add(new BasicNameValuePair("scope", "openid email"));
    	param.add(new BasicNameValuePair("sso", "false"));
    	param.add(new BasicNameValuePair("tenant", "rapaport"));
    	UrlEncodedFormEntity he = new UrlEncodedFormEntity(param,"UTF-8");
    //	Cookie auth0=s%3A6Y_CnYhUQ-XmPzX6rECo94nbvguJLJLL.OCGOSPHiHLDq6CO2X0ZWsFyqcMoWU7I%2BHRmyvxwxkjA
    	post.setEntity(he);
    	post.addHeader("Accept", "text/html");
    	post.addHeader("Accept-Encoding", "gzip, deflate, br");
    	post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
    //	post.addHeader("Auth0-Client", "eyJuYW1lIjoibG9jay5qcyIsInZlcnNpb24iOiIxMC44LjEiLCJsaWJfdmVyc2lvbiI6IjcuNi4xIn0");
    	post.addHeader("Connection", "keep-alive");
    //	post.addHeader("Content-Length", "254");
    	post.addHeader("Content-Type", "application/x-www-form-urlencoded");
    	post.addHeader("Host", "rapaport.auth0.com");
    	post.addHeader("Origin", "https://member.rapnet.com");
    	post.addHeader("Referer", "https://member.rapnet.com/Login/LoginPage.aspx");
    	post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
     	return post; 
	}
	
	/**创建登陆回调登陆的HTTP链接 
	 * newloginPosthtml 是登陆post返回的response的实体
	 * <form method="post" name="hiddenform" action="https://rapaport.auth0.com/login/callback">
    <input type="hidden" name="wa" value="wsignin1.0">
    <input type="hidden" 
           name="wresult" 
           value="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoiNTk4YjUxMTM2ZDViY2Y1Y2ExZTM0ZmZmIiwiZW1haWwiOiIxMzU0ODE1OTc1QHFxLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiOTAzNDEiLCJuaWNrbmFtZSI6IjkwMzQxIiwidXNlcm5hbWUiOiI5MDM0MSIsImlhdCI6MTUxMDAzNTI1OSwiZXhwIjoxNTEwMDM1MzE5LCJhdWQiOiJ1cm46YXV0aDA6cmFwYXBvcnQ6VXNlcm5hbWUtUGFzc3dvcmQtQXV0aGVudGljYXRpb24iLCJpc3MiOiJ1cm46YXV0aDAifQ.OEE16KK4CFHXGLGFj3aboyQQ0pLOCDbE66RhW9fhnFkMoaB9PFnirheC4lBDDokajop3wCvS1GyYVfVbh1ZkaZ-_Eo6ldeKoPdjvSqz2ZGXfmEuJTY23M28KFWacFdZsiIUadgj7dvj5zO7qrodaGpoVb2lLfElbm47AaaUU6nY">
    <input type="hidden" name="wctx" value="{&#34;strategy&#34;:&#34;auth0&#34;,&#34;auth0Client&#34;:&#34;&#34;,&#34;tenant&#34;:&#34;rapaport&#34;,&#34;connection&#34;:&#34;Username-Password-Authentication&#34;,&#34;client_id&#34;:&#34;FsYXds0gouXuOtlRFnYsdAjF8nysbRcp&#34;,&#34;response_type&#34;:&#34;code&#34;,&#34;scope&#34;:&#34;openid email&#34;,&#34;redirect_uri&#34;:&#34;https://member.rapnet.com/Login/LoginPage.aspx&#34;,&#34;realm&#34;:&#34;Username-Password-Authentication&#34;,&#34;_timer_state&#34;:{&#34;total&#34;:{&#34;start&#34;:1510035259544},&#34;metrics&#34;:{&#34;connection&#34;:{&#34;start&#34;:1510035259544}}},&#34;session_user&#34;:&#34;5a014f3bed21e067de07b63f&#34;}">
    <noscript>
        <p>
            Script is disabled. Click Submit to continue.
        </p><input type="submit" value="Submit">
    </noscript>
</form>
	 * **/
	public static HttpPost newcallback2(String newloginPosthtml) throws UnsupportedEncodingException{
		logger.error("登陆回调html");
		Document doc = Jsoup.parse(newloginPosthtml); 
 	        String wa=doc.select("input[name=wa]").val();
	        String wresult=doc.select("input[name=wresult]").val();
	        String wctx=doc.select("input[name=wctx]").val();
	        String formaction=doc.select("form[name=hiddenform]").get(0).attr("action");
	        if(wa==null||"".equals(wa)){
	        	return null;
	        }
	        if(wresult==null||"".equals(wresult)){
	        	return null;
	        }
	        if(wctx==null||"".equals(wctx)){
	        	return null;
	        }
	        if(formaction==null||"".equals(formaction)){
	        	return null;
	        }
		//创建一个新的链接
        HttpPost formpost=new HttpPost(formaction);
        
    	List<BasicNameValuePair> param2=new ArrayList<BasicNameValuePair>();
    	param2.add(new BasicNameValuePair("wa",wa));
    	param2.add(new BasicNameValuePair("wresult", wresult));
    	param2.add(new BasicNameValuePair("wctx", wctx));
    	UrlEncodedFormEntity he2 = new UrlEncodedFormEntity(param2,"UTF-8");
	    	formpost.setEntity(he2);
	    	
	    	formpost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	    	formpost.addHeader("Accept-Encoding", "gzip, deflate, br");
	    	formpost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
    //	post.addHeader("Auth0-Client", "eyJuYW1lIjoibG9jay5qcyIsInZlcnNpb24iOiIxMC44LjEiLCJsaWJfdmVyc2lvbiI6IjcuNi4xIn0");
	    	formpost.addHeader("Connection", "keep-alive");
    //	post.addHeader("Content-Length", "254");
	    	formpost.addHeader("Content-Type", "application/x-www-form-urlencoded");
	    	formpost.addHeader("Host", "rapaport.auth0.com");
	    	formpost.addHeader("Upgrade-Insecure-Requests", "1");
	    	formpost.addHeader("Referer", "https://member.rapnet.com/Login/LoginPage.aspx?ReturnUrl=%2fRapNet%2fPriceGrid%2fGridResults.aspx");
	    	formpost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
          return formpost;
	}
	/** 登陆权限认证完成 去门户注册登陆信息
	 * loginpagahtml 是一个html 带a的标签. 里面有一个的地址待code用于注册
	 * 	GET /Login/LoginPage.aspx?code=-KmKCqvQg870UzOq HTTP/1.1
	 * <p>Found. Redirecting to <a href="https://member.rapnet.com/Login/LoginPage.aspx?code=su8fbKJIKhcqydhX">https://member.rapnet.com/Login/LoginPage.aspx?code=su8fbKJIKhcqydhX</a></p>
 	 * **/
	public static HttpGet newloginpaga3(String href) throws UnsupportedEncodingException{
		logger.error("访问首页的html:"+href);

		  // Document doc2 = Jsoup.parse(loginpagahtml); 
		   //  String href=doc2.select("a").get(0).html();
			//	System.out.println(href);
   		        //创建一个新的链接
		       HttpGet loginpagapost=new HttpGet(href);
		       loginpagapost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		       loginpagapost.addHeader("Accept-Encoding", "gzip, deflate, br");
		       loginpagapost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
	 	       loginpagapost.addHeader("Connection", "keep-alive");
	 	 //     loginpagapost.addHeader("Content-Type", "application/x-www-form-urlencoded");
	 	       //	loginpagapost.addHeader("Host", "member.rapnet.com");
	 	     	loginpagapost.addHeader("Upgrade-Insecure-Requests", "1");
	 	    	//loginpagapost.addHeader("Referer", "https://member.rapnet.com/Login/LoginPage.aspx");
	 	   		loginpagapost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
	 	   
//	 	   		Host: member.rapnet.com
//	 	   		User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0
//	 	   		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//	 	   		Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3
//	 	   		Accept-Encoding: gzip, deflate, br
//	 	   		Cookie: ASP.NET_Rapnet=rwn45jtt0xl4uasm51edkvfn; CookieDeviceNumber=5134659; __qca=P0-1556215185-1510227059039; __hstc=205636236.476c9f1c4d30b1d4c806da13c70eb33d.1510227125048.1510227125048.1510227125048.1; __hssrc=1; __hssc=205636236.4.1510227125049; hubspotutk=476c9f1c4d30b1d4c806da13c70eb33d; __hs_opt_out=no; SnapABugRef=https%3A%2F%2Fmember.rapnet.com%2FRapNet%2F%20; SnapABugHistory=1#; SnapABugVisit=1#1510227602
//	 	   		Connection: keep-alive
//	 	   		Upgrade-Insecure-Requests: 1


	 	   		
	 	   		
	 	   		return   loginpagapost;
	}
	
	
	
	/**  
	 * 第一个数据交易屏的网页访问
 	 * **/
	public static HttpGet newpagedata4(String tuchuurl)  {
		logger.error("访问数据页 交易屏 "+pagedata1);
		   HttpGet getdata=null;
 			getdata = new HttpGet(pagedata1);
 		   getdata.addHeader("Host", "member.rapnet.com");
		   getdata.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		   getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		   getdata.addHeader("Accept-Language", " zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
		   getdata.addHeader("Referer",tuchuurl);
		   getdata.addHeader("Upgrade-Insecure-Requests", "1");
		   getdata.addHeader("Connection", "keep-alive");
		   getdata.addHeader("Host", "member.rapnet.com");
		   return getdata;
	}
	
	
	/**  
	 * 列表中的一个图片的地址解析
	 * //			GET /RapNet/Search/GetImageFile.aspx?LotID=85870606&FileType=IMAGE HTTP/1.1
 	 * **/
	public static HttpGet newimger(String url)  {
		logger.error("列表中的一个图片或文件的地址解析:"+url);
		   HttpGet getdata=null;
 			getdata = new HttpGet(url);
 		   getdata.addHeader("Host", "member.rapnet.com");
		   getdata.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		   getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		   getdata.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
		//   getdata.addHeader("Referer", Referer);
		   getdata.addHeader("Upgrade-Insecure-Requests", "1");
		   getdata.addHeader("Connection", "keep-alive");
		   getdata.addHeader("Host", "member.rapnet.com");
		   return getdata;
	}
	/**  
	 * 列表中详情数据的解析
	 *GET /RapNet/Search/ExpandFullDetails.aspx?DiamondID=83487226&Page=1&RowID=0&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0 HTTP/1.1
  	 * **/
	public static HttpGet newExpandFullDetails(String url)  {
		logger.error(" 列表中详情数据的解析 "+url);
		   HttpGet getdata=null;
 			getdata = new HttpGet(url);
 		   getdata.addHeader("Host", "member.rapnet.com");
		   getdata.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		   getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		   getdata.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
		//   getdata.addHeader("Referer", Referer);
		   getdata.addHeader("Upgrade-Insecure-Requests", "1");
		   getdata.addHeader("Connection", "keep-alive");
		   getdata.addHeader("Host", "member.rapnet.com");
		   return getdata;
	}
	
	/**  
	 * 访问交易屏中  表格的某个单元格  读取单元格列表
	 * //		    GET /RapNet/Search/Results.aspx?Code=Hmsx7tN6o2JHmMIh1xC0ew%3d%3d&SearchSessionID=149123871 HTTP/1.1
 	 "上个页面  https://member.rapnet.com/RapNet/PriceGrid/GridResults.aspx"
 	 * **/
	public static HttpGet newpagetable(String url,String Referer)  {
// 		Host: member.rapnet.com
// 		User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0
// 		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
// 		Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3
// 		Accept-Encoding: gzip, deflate, br
// 		Referer: https://member.rapnet.com/RapNet/PriceGrid/GridResults.aspx
// 		Cookie: __qca=P0-122770300-1509960495037; __hstc=205636236.7f827a1691990dc18a92379ad24c4f0d.1509960557170.1510192726192.1510198025898.13; hubspotutk=7f827a1691990dc18a92379ad24c4f0d; CookieDeviceNumber=5130606; SnapABugHistory=2#; __hs_opt_out=no; _hp2_id.600257526=%7B%22userId%22%3A%226940480858651708%22%2C%22pageviewId%22%3A%224765168619356259%22%2C%22sessionId%22%3A%224257586561188155%22%2C%22identity%22%3Anull%2C%22trackerVersion%22%3A%223.0%22%7D; ASP.NET_Rapnet=ualdpfu2t4ier2eeahnpmihg; __hssrc=1; SnapABugVisit=6#1510192637; UserCulture=zh-cn; SnapABugRef=https%3A%2F%2Fmember.rapnet.com%2FRapNet%2F%20https%3A%2F%2Fmember.rapnet.com%2FLogin%2FLoginPage.aspx; .MEMBERRAPNETFORMSAUTH=CCA5C260D7493F3DD1BF03D5384C1F63C5DBBB060D9496ED1A4AAD90249B148FC58DE60D0338DCAE5BACC0A6DCEB6211CCCD0D0AAEB7CC70C26D4CC90B688BA9EB88612FE36E39049EE7A82E1ACA95BDAE012862DB9A6D9C66EED7D2560B0D98B24A79EA10C66CAD840CBE384A5919EADAE8442B3A298CBD83C8554C984A6C3B89987FA1548516403ACF113F69A3EC0AFB519EDF8A496EE09E621621AE1BF94447FC8B85173FF7F3F7FECE483F16A224211296D3482A3BB25B1BF335CA7F12B61FCAFFF40DF702715ECD733C2CB8295181BAF95DF902E841E6A949F8D0EC29A7A4F40C9963382B3DCE8854B01FAF69D8417CB36968DDB391998B8DE91ED0694A58538A0D2A77E78B7054BBF1054DDD54AE4D2C29; ActiveUserRowID=ActiveUserRowID=17824175
// 		Connection: keep-alive
		
// 		Upgrade-Insecure-Requests: 1
		logger.error("访问交易屏中  表格的某个单元格  读取单元格列表 "+url);
		   HttpGet getdata=null;
 			getdata = new HttpGet(url);
 		   getdata.addHeader("Host", "member.rapnet.com");
		   getdata.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		   getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		   getdata.addHeader("Accept-Language", " zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
		   getdata.addHeader("Referer", Referer);
		   getdata.addHeader("Upgrade-Insecure-Requests", "1");
		   getdata.addHeader("Connection", "keep-alive");
		   getdata.addHeader("Host", "member.rapnet.com");
		   return getdata;
	}
	
	
	/**  
	 * 读取单元格列表 的翻页
	*	POST /RapNet/Search/Results.aspx?Code=hma70%2bLUr4lvp%2bVZmDEeuA%3d%3d&SearchSessionID=149136979 HTTP/1.1
	 * @throws UnsupportedEncodingException 
	 * html上个页面的html  数据列表页面
	 * Referer是上个页面的地址
 	 * **/
	public static HttpPost newTurnthepage(String html,String Referer) throws UnsupportedEncodingException  {
		 Document newpagetabled = Jsoup.parse(html); 
		String action= newpagetabled.getElementById("aspnetForm").attr("action");//得到提交form 的地址
		action=action.split("\\."+"/")[1];//地址里有./ 要截取掉  然后拼接根目录 成为完整目录
        HttpPost formpost=new HttpPost("https://member.rapnet.com/RapNet/Search/"+action);
      System.out.println("翻页路径:"+"https://member.rapnet.com/RapNet/Search/"+action);
 	    	formpost.addHeader("Accept", "*/*");
	    	formpost.addHeader("Accept-Encoding", "gzip, deflate, br");
	    	formpost.addHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
	    	formpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
	    	formpost.addHeader("Host", "member.rapnet.com");
	    	formpost.addHeader("X-Requested-With", "XMLHttpRequest");
	    	formpost.addHeader("X-MicrosoftAjax", "Delta=true");
	    	formpost.addHeader("Cache-Control", "no-cache");
	    	//formpost.addHeader("Content-Length", "15117");
 	    	formpost.addHeader("Connection", "keep-alive");
	    	formpost.addHeader("Referer", Referer);
	    	formpost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
//	    	String __COMPRESSEDVIEWSTATE=;
//	    	String __EVENTVALIDATION=;
//	    	String ctl00_antiforgery=;
	    	List<NameValuePair> param2=new ArrayList<NameValuePair>();
	    	 
	    	param2.add(new BasicNameValuePair("TopSearchBox",""));
	    	param2.add(new BasicNameValuePair("__ASYNCPOST","true"));
	    	param2.add(new BasicNameValuePair("__EVENTARGUMENT",""));
	    	param2.add(new BasicNameValuePair("__EVENTTARGET","ctl00$cphMainContent$lbntNavigate5"));
	    	param2.add(new BasicNameValuePair("__EVENTVALIDATION",newpagetabled.getElementById("__EVENTVALIDATION").attr("value")));
 	    	param2.add(new BasicNameValuePair("__COMPRESSEDVIEWSTATE",newpagetabled.getElementById("__COMPRESSEDVIEWSTATE").attr("value")));
     
  
	    	param2.add(new BasicNameValuePair("__VIEWSTATE", ""));
	    	param2.add(new BasicNameValuePair("__VIEWSTATEENCRYPTED", ""));
	    	param2.add(new BasicNameValuePair("ctl00$ScriptManager1", "ctl00$cphMainContent$udpMain|ctl00$cphMainContent$lbntNavigate5"));
 	    	param2.add(new BasicNameValuePair("ctl00$antiforgery", newpagetabled.getElementById("ctl00_antiforgery").attr("value")));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$hfSelectedDiaomnds", ""));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$hidCurrentStartRow", ""));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$txtDialogMsg", ""));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$txtDialogMsgType", ""));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$txtMarkup", ""));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$txtSelectedLots", ""));
 	    	  param2.add(new BasicNameValuePair("ctl00$cphMainContent$txtSession", ""));
 	 
//	    	for(int shu=2;shu<52;shu++){
//	    		String shuzi="";
//	    		if(shu<10){
//	    			shuzi="0"+shu;
//	    		}else{
//	    			shuzi=shu+"";
//	    		}
// 	    		String ct1contentid="ctl00_cphMainContent_gvResults_ctl"+shuzi+"_hidDiamondID";
//  	    		String cphMainContentid="ctl00_cphMainContent_gvResults_ctl"+shuzi+"_hidNoteDiamondID";
//	    		
//	    		 if(newpagetabled.getElementById(ct1contentid)!=null){
//	  		    	param2.add(new BasicNameValuePair(newpagetabled.getElementById(ct1contentid).attr("name"),
//	  		    			newpagetabled.getElementById(ct1contentid).attr("value")));
//			    
//	  		    	param2.add(new BasicNameValuePair(newpagetabled.getElementById(cphMainContentid).attr("name"), 
//			    			newpagetabled.getElementById(cphMainContentid).attr("value")));
//		    		}else{
//		    			System.out.println("没有找到id"+"ctl00_cphMainContent_gvResults_ctl"+shuzi+"_hidDiamondID");
//		    		}
//	    	 }
	    	//循环输出参数
//	    	for(NameValuePair p: param2){
//	    		System.out.println(p.getName()+"   "+p.getValue());
//	    	}
	    	 
	    	UrlEncodedFormEntity he2 = new UrlEncodedFormEntity(param2);
	    	//Entity e=
		    formpost.setEntity(he2);
	    	return formpost;
	     
	}
	/** 退出登陆
 	 * **/
	public static HttpGet logout99(String Referer)    {
		logger.error("退出登陆 "+logout);
		  HttpGet LogOut=new HttpGet(logout);
	      LogOut.addHeader("Host", "member.rapnet.com");
	      LogOut.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
	      LogOut.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	      LogOut.addHeader("Accept-Language", " zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
	      LogOut.addHeader("Accept-Encoding", "gzip, deflate, br");
	      LogOut.addHeader("Referer", Referer);
	      LogOut.addHeader("Upgrade-Insecure-Requests", "1");
	      LogOut.addHeader("Connection", "keep-alive");
	      LogOut.addHeader("Host", "member.rapnet.com");
 		   return LogOut;
	}
	
	  /**输出html文件  path存储路径和文件名称  html是输出内容**/
	public static void newfile(String path,String html){
		  FileWriter fileWriter2 =null;
		 try {
			 File file = new File(path);  
			    System.out.println(file.getParentFile());  
			    if (!file.getParentFile().exists()) {  
			        boolean result = file.getParentFile().mkdirs();  
			        if (!result) {  
			            System.out.println("创建失败");  
			        }  
			    }  
			    
			  File f2=new File(path);
 			  fileWriter2 = new FileWriter(f2);  
			  fileWriter2.write(html);  
			  fileWriter2.close();
		} catch (IOException e) {
 			e.printStackTrace();
		}finally {
			logger.error("数据导出:"+path);
			if(fileWriter2!=null){
				try {
					fileWriter2.close();
				} catch (IOException e) {
 					e.printStackTrace();
					logger.error(e);
				}
			}
		}
	}
}
