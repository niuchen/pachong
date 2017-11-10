package com.pachong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.swing.text.AbstractDocument.BranchElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Apiurl implements Job{
	public static Log logger = LogFactory.getLog(Apiurl.class);  //日志	
	
	

	
	
	
	

	/***采集具体执行方法  int是返回执行是否成功  0失败,  1成功**/
	public static int zhixing(){
		String tuchuurl="";
		CloseableHttpClient client=null;
		int isreunt=0;
		 try {
			 client=Headerpachong.newhttpclient();
			 /**-----------登陆开始-------------***/
			HttpPost loginpost= Headerpachong.newloginPost1();
			 HttpResponse response=client.execute(loginpost);//登陆
			 tuchuurl=loginpost.getURI().toString();
			 String newloginPosthtml= EntityUtils.toString(response.getEntity());//获取登陆返回的回调方法数据
			 logger.error("登陆状态:"+response.getStatusLine().getStatusCode() );
			// printResponse(response);
//			 System.out.println("第一条cookies是" + response.getFirstHeader("set-cookie"));
//		        Header[] hs = response.getHeaders("Set-Cookie");
 			 HttpPost callbackpost= Headerpachong.newcallback2(newloginPosthtml);//生成提交执行回调数据url
 			 /*****$$$$$$$$$登陆结束$$$$$$$$$$$***/

 			 /**-----------执行回调读取令牌      开始-------------***/
 			 HttpResponse  response2=client.execute(callbackpost);//执行提交执行回调数据url
			 tuchuurl=callbackpost.getURI().toString();
			 String loginpagahtml= EntityUtils.toString(response2.getEntity());//得到paga注册code 和注册url
			 System.out.println("loginpagahtml值"+loginpagahtml);
			 int statuscode = response2.getStatusLine().getStatusCode();
			 logger.error("回调状态:"+statuscode );
			 /**-----------执行回调读取令牌      结束-------------***/
			// printResponse(response2);
			 HttpResponse  response3=null;
			 if(statuscode==302){
				// 读取新的 URL 地址 
				   Header header=response2.getFirstHeader("location");
				   if (header!=null){
				      String newuri=header.getValue();
				    
				         HttpGet redirect=Headerpachong.newloginpaga3(newuri);
 				         System.out.println("第一次重定向:"+newuri);
				         response3=    client.execute(redirect);
				     //  printResponse(response3);
				       
				      
				        
				   }else{
					   System.out.println("偷空");
				   }
			 }else{
				 System.out.println("返回的不是重定向");
			 }
		
			 /**-----------访问首页      开始-------------***/
			 /**二次重定向****/
			 HttpResponse  redirect2response=null;
			 if(response3.getStatusLine().getStatusCode()==302){
			  
		         System.out.println("第二次重定向:"+response3.getFirstHeader("location").getValue());
		         HttpGet redirect2 =Headerpachong.newloginpaga3(Headerpachong.root+response3.getFirstHeader("location").getValue());
		            redirect2response= client.execute(redirect2);
			 }else{
				 logger.error(" 登陆失败.  第二次转发首页不是302");
				 return 0;
			 }
//  			  HttpGet pagapost=Headerpachong.newloginpaga3(loginpagahtml);//生成访问首页
//  			 HttpResponse  response3=client.execute(pagapost);//执行访问首页
// 			 tuchuurl=pagapost.getURI().toString();
 			 String pagaponse= EntityUtils.toString(redirect2response.getEntity());//得到paga注册code 和注册url
 			 //System.out.println("pagaponse:"+pagaponse);
//			 logger.error("访问首页状态:"+response3.getStatusLine().getStatusCode()+tuchuurl );
			 Headerpachong.newfile("d:\\s1.html", pagaponse);
			 if(pagaponse.indexOf("交易屏")==-1){
				 logger.error("注册登陆失败. 访问首页 没有找到交易屏关键字    终止.");
				 if(pagaponse.indexOf("RapNetMainContent")==1){
					 logger.error("账号重复登录! 只能稍后等待了");
 				 }
				 return 0;
			 } 
 			 
//			   String url="https://member.rapnet.com/RapNet/Search/GetImageFile.aspx?LotID=83097553&FileType=IMAGE";
//			   HttpGet getdata = new HttpGet(url);
//			 //  getdata.addHeader("Host", "member.rapnet.com");
//			    getdata.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
//			 //  getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			 //  getdata.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//			 //  getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
//			//   getdata.addHeader("Referer", Referer);
//			 //  getdata.addHeader("Upgrade-Insecure-Requests", "1");
//			//   getdata.addHeader("Connection", "keep-alive");
//			//   getdata.addHeader("Host", "member.rapnet.com");
// 					 try {
//						     //  client=Apiurl.newhttpclient();
//						       HttpResponse impes=client.execute(getdata);//执行图片真是路径
//							// 读取新的 URL 地址 
//							   Header header=impes.getFirstHeader("Location");
//							 if(header!=null){
//								 String newuri=header.getValue();
//							     System.out.println("1图片地址:"+impes.getFirstHeader("Location").getValue());
//							 }else{
//								 System.out.println("1图片地址空");
//							 }
// 							 System.out.println(EntityUtils.toString(impes.getEntity()));
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			   
			 
			 
			 
			 /**-----------访问首页      结束-------------***/
//			 Header[] header=response.getAllHeaders();
//			 for(Header hobj:header){
//				 System.out.println(hobj.getName()+":"+hobj.getValue());
//			 }
			 
			 /**-----------访问交易屏页面     starte-------------***/
			 HttpGet datapost1= Headerpachong.newpagedata4(tuchuurl);//数据屏的页面首页
		 
			 HttpResponse response4=client.execute(datapost1);//执行数据屏
			 tuchuurl=datapost1.getURI().toString();
			 logger.error("交易屏状态:"+response4.getStatusLine().getStatusCode() );
			 String datahrml= EntityUtils.toString(response4.getEntity());//得到paga注册code 和注册url
			 Headerpachong.newfile("d:\\s2.html", datahrml);
			 /**-----------访问交易屏页面     ent-------------***/
			 
			 
			 
			 /**-----------访问 交易屏  某一个交易单元格   打卡数据列表     starte-------------***/
			  Document doc2 = Jsoup.parse(datahrml); 
			  String href0=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl00_gvGrid").html();
//			  String href2=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl02_gvGrid").html();
//			  String href4=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl04_gvGrid").html();
//			  String href6=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl06_gvGrid").html();
//			  String href8=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl08_gvGrid").html();
//			  String href10=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl10_gvGrid").html();
//			  String href12=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl12_gvGrid").html();
 			  Document href0doc = Jsoup.parse(href0); 
 			 Element anode= href0doc.getElementById("ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value");//得到第一个单元格的HTML
 			String ahref=anode.attr("href");//得到第二个单元格的点击url地址
 	    		 String ahrefurl[]=ahref.split("\\."+"\\."+"/");
 	    		 if(ahrefurl.length!=2){
	    			 logger.error(ahrefurl.length+"--------表格里的 a标签的href链接截取错误"+ahref);
 	    		 }
 	    		String ahrefu="https://member.rapnet.com/RapNet/"+ahrefurl[1];
	    		System.out.println("第一个单元格url地址:"+ahrefu);
	    		  HttpGet newpagetable= Headerpachong.newpagetable(ahrefu,tuchuurl);//访问交易屏中  表格的某个单元格  读取单元格列表
	    		  response=null;
	  			 response=client.execute(newpagetable);//执行数据屏
	  			// tuchuurl=newpagetable.getURI().toString();
	  			 logger.error("访问交易屏中  表格的某个单元格  读取单元格列表状态:"+response.getStatusLine().getStatusCode() );
	  			 String newpagetablehrml= EntityUtils.toString(response.getEntity());//得到paga注册code 和注册url
	  			Headerpachong.newfile("d:\\s3.html", newpagetablehrml);
		    	 /**-----------访问 交易屏  某一个交易单元格   打开数据列表   ent-------------***/ 
	  	 
	  			  Document newpagetablehrmldoc = Jsoup.parse(newpagetablehrml); 
	  			Elements anodes= newpagetablehrmldoc.select("a[title='View video']");
	  			System.out.println("总数:"+anodes.size());
	  			List<String> shulsit=new ArrayList<String>();
	  			try {
	  				 HttpResponse impes;
	  				 int k=0;
					for(Element eobj:anodes){
						String hrefeobj=eobj.attr("href");
						hrefeobj=Headerpachong.root+hrefeobj;
					 //   HttpGet imgerget= Headerpachong.newimger(hrefeobj);
					    System.out.println((k++)+"解析前图片地址:"+hrefeobj);
 					    shulsit.add(analysis(hrefeobj).get("LotID"));//得到ID
// 					   impes  =client.execute(imgerget);//执行图片真是路径
//					    int imstatuscode = impes.getStatusLine().getStatusCode();
//					    if(imstatuscode==302){
//					    	// 读取新的 URL 地址 
//							   Header header=impes.getFirstHeader("Location");
//							 if(header!=null){
//								 String newuri=header.getValue();
//							         System.out.println("图片地址解析后:"+impes.getFirstHeader("Location").getValue());
//							 }else{
//								 logger.error("图片地址空"+imgerget);
//							 }
//					    }else{
//					    	logger.error("图片解析错误.没有得到地址或302状态"+imgerget);
//					    }
 					//System.out.println(EntityUtils.toString(impes.getEntity()));//
					//  System.out.println(lotid+":图片状态:"+imstatuscode );
					    impes=null;
					}
				} catch (Exception e) {
					logger.error("图片解析异常");
					e.printStackTrace();
				} 
	  			
	  			
	  			System.out.println("产品解析开始");
String efdurl="https://member.rapnet.com/RapNet/Search/ExpandFullDetails.aspx"
		+ "?DiamondID=%&Page=1&RowID=0&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0";//详情的基本链接 DiamondID的%是要替换成产品信息id
	  			for(String sid:shulsit){
	  			  HttpGet efdget= 	Headerpachong.newExpandFullDetails( efdurl.replaceAll("[%]", sid));
	  			  HttpResponse edfpes=client.execute(efdget);//
	  			  String edfpeshrml= EntityUtils.toString(edfpes.getEntity());//
 	  			  System.out.println("详细信息状态:"+edfpes.getStatusLine().getStatusCode()+":"+efdurl.replaceAll("[%]", sid));
 	  			  printResponse(edfpes);
 	  			  
 	  			 Headerpachong.newfile("d:\\paga\\"+sid+".html", edfpeshrml);
 	  			 break;
	  			}
	  		 
//	  			https://member.rapnet.com/RapNet/Search/GetImageFile.aspx?LotID=85912218&FileType=IMAGE
	  			
	//  			https://member.rapnet.com/RapNet/Search/ExpandFullDetails.aspx?DiamondID=85912218&Page=1&RowID=1&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0
//	  			 
 	    	 /**----------- 执行 数据列表   翻页     starte-------------***/ 
	  			 System.out.println("翻页开始");
	  		    HttpPost newTurnthepage=Headerpachong.newTurnthepage(newpagetablehrml, tuchuurl);
	  		   // tuchuurl=  newTurnthepage.getURI().toString();
	  		    System.out.println(newTurnthepage.getURI().toString());
		  		 response=null;
	  			 response=client.execute(newTurnthepage);//执行翻页
	  			 String newpagetablehrml2= EntityUtils.toString(response.getEntity());//得到paga注册code 和注册url
	  			 System.out.println("翻页状态"+ response.getStatusLine().getStatusCode()+":");
	  		//	 sysh(response.getAllHeaders());
	  			printResponse(response);
	  			System.out.println("翻页状态"+ response.getStatusLine().getStatusCode()+":");
	  			Headerpachong.newfile("d:\\s4.html", newpagetablehrml2);
	  			 //https://member.rapnet.com/RapNet/Search/Results.aspx?Code=dqkPeRM4L7zAMOV%2fIQAfkw%3d%3d&SearchSessionID=149113546
	    	 
 	    	 /**----------- 执行 数据列表   翻页     end-------------***/ 
	  			 
	  			 
 			//ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lblColor
// 			 ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value
// 			 ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_VVS1_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_VVS2_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_VS1_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_VS2_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_SI1_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_SI2_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_I1_Value
// 			ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_I2_Value
 			 
// 		     Elements anode= href0doc.select("a");
// 		     for(Element e:anode){
// 		    	 String ahref=e.attr("href");
// 		    	 if(ahref!=null||!"".equals(ahref)) {
// 		    		//dataroot
// 		    		 String ahrefurl[]=ahref.split("\\."+"\\."+"/");
// 		    
// 		    		 if(ahrefurl.length!=2){
// 		    			 logger.error(ahref.split("../").length+"--------表格里的 a标签的href链接截取错误"+ahref);
// 		    			break;
// 		    		 }
// 		    		   ahref=dataroot+ahref.split("../")[1];
// 		    		System.out.println(ahref);
// 		    		//https://member.rapnet.com/RapNet/Search/Results.aspx?Code=dqkPeRM4L7zAMOV%2fIQAfkw%3d%3d&SearchSessionID=149113546
// 		    	 }else{
// 		    		 System.out.println(ahref);
// 		    	 }
// 		    	 break;
// 		     }



			 isreunt=1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}finally {
			if(client!=null){
				try {
					 HttpGet logout= Headerpachong.logout99(tuchuurl);//退出登陆 方式占用不释放下次报错.
					 HttpResponse response=client.execute(logout);//执行退出登陆
					 logger.error("退出登陆:"+response.getStatusLine().getStatusCode() );
					 logger.error(""+response.toString() );
					 logger.error("");
					 logger.error("");
					client.close();
				} catch (IOException e) {
					isreunt=0;
 					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e);
				}
			}
		}
//		 if(isreunt!=1){
//			 System.out.println("递归调用 因为有没有通过的操作.");
// 			  Apiurl.zhixing();
//		 }else{
		     return isreunt;
		// }
	}
	private static void sysh(Header[] he3){
		for(Header h:he3)
		System.out.println(h.getName()+" : "+h.getValue());
	}
	/**测试main方法**/
	public static void main(String[] args) {
		Apiurl ap=new Apiurl();
		try {
	 ap.execute(null);
//			String d="../Search/Results.aspx?C";
//			String ddd[]=d.split("\\."+"\\."+"/");
//			System.out.println(ddd.length);
//			System.out.println(ddd[0]);
//			System.out.println(ddd[1]);
			//System.out.println(ddd[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/***定时任务的执行方法. quartz定时器.这里的main不在是线上应用的入口 定时器的main才是 ***/
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
//		if(1==1){
//			System.out.println("ddd");
//			return;
//		}
		long start,end;
		start = System.currentTimeMillis();
		 logger.error("开始爬虫~~~~~~~~~~~~~~~~~~爬虫开始时间:" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start)+"");

	   int isreturn=	0;
	   int i=1;
	   while(isreturn==0){
		   if(i>1){
			   logger.error("~~~~~~~~~~~~~~~~~~执行有异常重复执行.第"+i+"次");
		   } 
		   isreturn= Apiurl.zhixing();//如果状态是0 就一直执行 0表示没有完成.
		   if(isreturn==1){
			   break;
		   }
		   i++;
		   if(i%3==0){//每执行3次 就等待10分钟在执行三次.知道状态=1正常了.
			   try {
				   logger.error(i%3+"~~~~~~~~~~~~~~~~~~等待10分钟.第"+i+"次");
				TimeUnit.MINUTES.sleep(10);//10分钟
				 
				} catch (InterruptedException e) {
 					e.printStackTrace();
				}
		   }
		   if(i==100){ 
			   logger.error("~~~~~~~~~~~~~~~~~~重复次数过多,停止此次采集.第"+i+"次");
			   break;
		   }
		   break;
	   }
 		 end = System.currentTimeMillis();  
		 logger.error("结束爬虫~~~~~~~~~~~~~~~~~~爬虫开始时间:" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start) + "; ~爬虫结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end)+ "; Run Time:" + (end - start)/1000 + "(s)");
		
	}

	 public static void printResponse(HttpResponse httpResponse)
	            throws ParseException, IOException {
	        // 获取响应消息实体
	        HttpEntity entity = httpResponse.getEntity();
	        // 响应状态
	        System.out.println("status:" + httpResponse.getStatusLine());
	        System.out.println("headers:");
	        HeaderIterator iterator = httpResponse.headerIterator();
	        while (iterator.hasNext()) {
	        	Header h=(Header) iterator.next();
	            System.out.println("\t" + h.getName());
	            System.out.println("\t" + h.getValue());
	            System.out.println("\t" + h.getName());
	            
	        }
	        // 判断响应实体是否为空
	        if (entity != null) {
//	            String responseString = EntityUtils.toString(entity);
//	            System.out.println("response length:" + responseString.length());
//	            System.out.println("response content:"
//	                    + responseString.replace("\r\n", ""));
	        }
	    }
    public static Map<String,String> cookieMap = new HashMap<String, String>(64);
	 //从响应信息中获取cookie
    public static String setCookie(HttpResponse httpResponse)
    {
        System.out.println("----setCookieStore");
        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length==0)
        {
            System.out.println("----there are no cookies");
            return null;
        }
        String cookie = "";
        for (int i = 0; i < headers.length; i++) {
            cookie += headers[i].getValue();
            if(i != headers.length-1)
            {
                cookie += ";";
            }
        }
 
        String cookies[] = cookie.split(";");
        for (String c : cookies)
        {
            c = c.trim();
            if(cookieMap.containsKey(c.split("=")[0]))
            {
                cookieMap.remove(c.split("=")[0]);
            }
            cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "":(c.split("=").length ==2?c.split("=")[1]:c.split("=",2)[1]));
        }
        System.out.println("----setCookieStore success");
        String cookiesTmp = "";
        for (String key :cookieMap.keySet())
        {
            cookiesTmp +=key+"="+cookieMap.get(key)+";";
        }
 
        return cookiesTmp.substring(0,cookiesTmp.length()-2);
    }
    public static Map<String,String> analysis(String url) {
   	 Map<String, String> paramMap = new HashMap<String, String>();
       paramMap.clear();
       if (!"".equals(url)) {// 如果URL不是空字符串
           url = url.substring(url.indexOf('?') + 1);
           String paramaters[] = url.split("&");
           for (String param : paramaters) {
               String values[] = param.split("=");
               paramMap.put(values[0], values[1]);
           }
       }
       return paramMap;
   }
}
