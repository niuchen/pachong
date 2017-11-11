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
import java.util.HashSet;
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
import org.apache.http.client.methods.CloseableHttpResponse;
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
			 client=HttpClientUtil.getHttpClient();
			 /**-----------登陆开始-------------***/
			HttpPost loginpost= Headerpachong.newloginPost1();
			CloseableHttpResponse  response=client.execute(loginpost);//登陆
			 tuchuurl=loginpost.getURI().toString();
			 String newloginPosthtml= EntityUtils.toString(response.getEntity());//获取登陆返回的回调方法数据
			 logger.error("登陆状态:"+response.getStatusLine().getStatusCode() );
			 
			 
			 response.close();
			// printResponse(response);
//			logger.error("第一条cookies是" + response.getFirstHeader("set-cookie"));
//		        Header[] hs = response.getHeaders("Set-Cookie");
 			 HttpPost callbackpost= Headerpachong.newcallback2(newloginPosthtml);//生成提交执行回调数据url
 			 /*****$$$$$$$$$登陆结束$$$$$$$$$$$***/

 			 /**-----------执行回调读取令牌      开始-------------***/
 			CloseableHttpResponse  response2=client.execute(callbackpost);//执行提交执行回调数据url
			 tuchuurl=callbackpost.getURI().toString();
			 String loginpagahtml= EntityUtils.toString(response2.getEntity());//得到paga注册code 和注册url
			 logger.error("loginpagahtml值"+loginpagahtml);
			 int statuscode = response2.getStatusLine().getStatusCode();
			 Header header=response2.getFirstHeader("location");
		 	// response2.close();
			 logger.error("回调状态:"+statuscode);
			 /**-----------执行回调读取令牌      结束-------------***/
			// printResponse(response2);
			 CloseableHttpResponse  response3=null;
			 HttpGet redirect=null;
 			 if(statuscode==302){
				// 读取新的 URL 地址 
				
				   if (header!=null){
				      String newuri=header.getValue();
				   //   callbackpost.abort();
				   //   client.
 				     // client=HttpClientUtil.getHttpClient();
				           redirect=Headerpachong.newloginpaga3(newuri);
 				     //    callbackpost.abort();  //终止端口
  				        logger.error("第一次重定向:"+newuri);
				         response3=    client.execute(redirect);
				     //  printResponse(response3);		        
				   }else{
					  logger.error("偷空");
				   }
			 }else{
				logger.error("返回的不是重定向");
			 }
		 
			 /**-----------访问首页      开始-------------***/
			 /**二次重定向****/
 			
 			int rep3status=response3.getStatusLine().getStatusCode();
 			CloseableHttpResponse  redirect2response=null;
			 HttpGet redirect2 =null;
			 if(rep3status==302){
				 String rep3header=response3.getFirstHeader("location").getValue();
 		        logger.error("第二次重定向:"+rep3header);
		           redirect2 =Headerpachong.newloginpaga3(Headerpachong.root+rep3header);
		           //   redirect.abort();
		           //   client.close();
				    //  client=HttpClientUtil.getHttpClient();
		        //   response3.close();
		           redirect2response= client.execute(redirect2);
			 }else{
				 logger.error(" 登陆失败.  第二次转发首页不是302");
				 return 0;
			 }
//  			  HttpGet pagapost=Headerpachong.newloginpaga3(loginpagahtml);//生成访问首页
//  			 HttpResponse  response3=client.execute(pagapost);//执行访问首页
// 			 tuchuurl=pagapost.getURI().toString();
 			 String pagaponse= EntityUtils.toString(redirect2response.getEntity());//得到paga注册code 和注册url
 		//	redirect2response.close();
 			 //redirect2.abort();  //终止端口
 			 //System.out.println("pagaponse:"+pagaponse);
//			 logger.error("访问首页状态:"+response3.getStatusLine().getStatusCode()+tuchuurl );
			 Headerpachong.newfile("d:\\s1.html", pagaponse);
			 if(pagaponse.indexOf("交易屏")==-1){
				 logger.error("注册登陆失败. 访问首页 没有找到交易屏关键字    终止.");
				 if(pagaponse.indexOf("RapNetMainContent")!=-1){
					 logger.error("账号重复登录! 只能稍后等待了");
 				 }
				 return 0;
			 } 

			 
			 /**-----------访问首页      结束-------------***/
//			 Header[] header=response.getAllHeaders();
//			 for(Header hobj:header){
//				logger.error(hobj.getName()+":"+hobj.getValue());
//			 }
			 
			 /**-----------访问交易屏页面     starte-------------***/
			 HttpGet datapost1= Headerpachong.newpagedata4(tuchuurl);//数据屏的页面首页
		 
			 CloseableHttpResponse pingdateresponse4=client.execute(datapost1);//执行数据屏
			 tuchuurl=datapost1.getURI().toString();
			 logger.error("交易屏状态:"+pingdateresponse4.getStatusLine().getStatusCode() );
			 String datahrml= EntityUtils.toString(pingdateresponse4.getEntity());//得到paga注册code 和注册url
			 Headerpachong.newfile("d:\\s2.html", datahrml);
			// pingdateresponse4.close();
			//#####
			 /**-----------访问交易屏页面     ent-------------***/
			 
			 
			 
			 /**-----------访问 交易屏  某一个交易单元格   打卡数据列表     starte-------------***/
			  Document doc2 = Jsoup.parse(datahrml); 
			  String tableid="ctl00_cphMainContent_repGrids_ctl00_gvGrid";
			  String tdid="ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value";
			  String href0=doc2.getElementById(tableid).html();
//			  String href2=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl02_gvGrid").html();
//			  String href4=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl04_gvGrid").html();
//			  String href6=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl06_gvGrid").html();
//			  String href8=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl08_gvGrid").html();
//			  String href10=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl10_gvGrid").html();
//			  String href12=doc2.getElementById("ctl00_cphMainContent_repGrids_ctl12_gvGrid").html();
 			  Document href0doc = Jsoup.parse(href0); 
 			 
 			 Element anode= href0doc.getElementById(tdid);//得到第一个单元格的HTML
 			String ahref=anode.attr("href");//得到第1个单元格的点击url地址
 	    		 String ahrefurl[]=ahref.split("\\."+"\\."+"/");
 	    		 if(ahrefurl.length!=2){
	    			 logger.error(ahrefurl.length+"--------表格里的 a标签的href链接截取错误"+ahref);
 	    		 }
 	    		String ahrefu="https://member.rapnet.com/RapNet/"+ahrefurl[1];
	    		System.out.println("第一个单元格url地址:"+ahrefu);
	    		  HttpGet newpagetable= Headerpachong.newpagetable(ahrefu,tuchuurl);//访问交易屏中  表格的某个单元格  读取单元格列表
	    		  
	    		  CloseableHttpResponse pingdatapageresponse=client.execute(newpagetable);//执行数据屏一个单元格
	  			// tuchuurl=newpagetable.getURI().toString();
	  			 logger.error("访问交易屏中  表格的某个单元格  读取单元格列表状态:"+pingdatapageresponse.getStatusLine().getStatusCode() );
	  			 String newpagetablehrml= EntityUtils.toString(pingdatapageresponse.getEntity());//得到paga注册code 和注册url
	  		//	pingdatapageresponse.close();
	  			 Headerpachong.newfile("d:\\s3.html", newpagetablehrml);
		    	 /**-----------访问 交易屏  某一个交易单元格   打开数据列表   ent-------------***/ 
	  	 
	  			  Document newpagetablehrmldoc = Jsoup.parse(newpagetablehrml); 
	  			Elements anodes= newpagetablehrmldoc.select("a[title='View video']");
	  			System.out.println("总数:"+anodes.size());
	  			List<String> shulsit=new ArrayList<String>();
 	  			
	  				 int k=0;
					for(Element eobj:anodes){
						String hrefeobj=eobj.attr("href");
						hrefeobj=Headerpachong.root+hrefeobj;
  					    shulsit.add(hrefeobj);//得到ID
 					}
					HashSet h = new HashSet(shulsit);     //去重 
					shulsit.clear();      
					shulsit.addAll(h); 
			
	  			
				
			  			System.out.println("产品解析开始,数量:"+shulsit.size());
		String efdurl="https://member.rapnet.com/RapNet/Search/ExpandFullDetails.aspx"
				+ "?DiamondID=%&Page=1&RowID=0&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0";//详情的基本链接 DiamondID的%是要替换成产品信息id
			  			for(String hrefeobj:shulsit){
			  				try {
			  				  logger.error((k++)+"解析前图片地址:"+hrefeobj);
 								//hrefeobj=Headerpachong.root+hrefeobj;
					           HttpGet imgerget= Headerpachong.newimger(hrefeobj);
					           CloseableHttpResponse   impes  =client.execute(imgerget);//执行图片真是路径
								    int imstatuscode = impes.getStatusLine().getStatusCode();
								    String newuri="";
								    if(imstatuscode==302){
								    	// 读取新的 URL 地址 
										   Header imgheader=impes.getFirstHeader("Location");
										 if(imgheader!=null){
											   newuri=imgheader.getValue();
										        logger.error("图片地址解析后:"+newuri);
										 }else{
											 logger.error("图片地址空"+imgerget);
										 }
								    }else{
								    	logger.error("图片解析错误.没有得到地址或302状态"+imgerget);
								    }
								   // imgerget.abort();  //终止端口
								    impes.close();
								    
			  				/***产品详情解析开始****/	
			  					  String sid= analysis(hrefeobj).get("LotID");
		 			  			  HttpGet efdget= 	Headerpachong.newExpandFullDetails( efdurl.replaceAll("[%]", sid));
		 			  			CloseableHttpResponse edfpes=client.execute(efdget);//
					  			  String edfpeshrml= EntityUtils.toString(edfpes.getEntity());//
					  			  edfpeshrml+="<a id='imgurl' heft='"+newuri+"'>";
				 	  			 logger.error("详细信息状态:"+edfpes.getStatusLine().getStatusCode()+":"+efdurl.replaceAll("[%]", sid));
				 	  			/***产品详情解析    ent****/	
				 	  			//当前查询的ctl 是多少  表示属于那个大模块
				 	  			 String headid=newpagetablehrmldoc.select("head").get(0).attr("id").split("_")[0];
				 	  			//第几页数据
				 	  			String pagesun=newpagetablehrmldoc.getElementById(headid+"_cphMainContent_lblPageNumBottom").text();
				 	  			String filename="d:\\spaga\\"+tableid+"\\"+tdid+"\\"+sid+"\\"+pagesun+".html";
				 	  			logger.error("产品详情的页面保存路径:"+filename);
				 	  			 //  printResponse(edfpes);
 				  	  			 Headerpachong.newfile(filename, edfpeshrml);
 				  	  		  // edfpes.close();
				 	  			// break;
			  				} catch (Exception e) {
								logger.error("产品详情和图片解析异常"+hrefeobj);
								e.printStackTrace();
							} 
			  			}
			  			
			
//	  			https://member.rapnet.com/RapNet/Search/GetImageFile.aspx?LotID=85912218&FileType=IMAGE
	  			
	//  			https://member.rapnet.com/RapNet/Search/ExpandFullDetails.aspx?DiamondID=85912218&Page=1&RowID=1&SearchType=REGULAR&DRows=50&Xtn=-1&newcerts=0
//	  			 
 	    	 /**----------- 执行 数据列表   翻页     starte-------------***/ 
	  			logger.error("翻页开始");
	  		    HttpPost newTurnthepage=Headerpachong.newTurnthepage(newpagetablehrml, tuchuurl);
	  		   // tuchuurl=  newTurnthepage.getURI().toString();
	  		   logger.error(newTurnthepage.getURI().toString());
		  		 response=null;
	  			 response=client.execute(newTurnthepage);//执行翻页
	  			 String newpagetablehrml2= EntityUtils.toString(response.getEntity());//得到paga注册code 和注册url
	  			logger.error("翻页状态"+ response.getStatusLine().getStatusCode()+":");
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
// 		    		logger.error(ahref);
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
					 CloseableHttpResponse response=client.execute(logout);//执行退出登陆
					 logger.error("退出登陆:"+response.getStatusLine().getStatusCode() );
					 logger.error(""+response.toString() );
					 logger.error("");
					 logger.error("");
					 response.close();
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
//			logger.error("递归调用 因为有没有通过的操作.");
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
	       logger.error("status:" + httpResponse.getStatusLine());
	       logger.error("headers:");
	        HeaderIterator iterator = httpResponse.headerIterator();
	        while (iterator.hasNext()) {
	        	Header h=(Header) iterator.next();
	           logger.error("\t" + h.getName());
	           logger.error("\t" + h.getValue());
	           logger.error("\t" + h.getName());
	            
	        }
	        // 判断响应实体是否为空
	        if (entity != null) {
//	            String responseString = EntityUtils.toString(entity);
//	           logger.error("response length:" + responseString.length());
//	           logger.error("response content:"
//	                    + responseString.replace("\r\n", ""));
	        }
	    }
    public static Map<String,String> cookieMap = new HashMap<String, String>(64);
	 //从响应信息中获取cookie
    public static String setCookie(HttpResponse httpResponse)
    {
       logger.error("----setCookieStore");
        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length==0)
        {
           logger.error("----there are no cookies");
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
       logger.error("----setCookieStore success");
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
