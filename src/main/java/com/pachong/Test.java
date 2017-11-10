package com.pachong;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="https://member.rapnet.com/RapNet/Search/GetImageFile.aspx?LotID=83097553&FileType=IMAGE";
		   HttpGet getdata=null;
			getdata = new HttpGet(url);
		   getdata.addHeader("Host", "member.rapnet.com");
		   getdata.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/56.0");
		   getdata.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		   getdata.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		   getdata.addHeader("Accept-Encoding", "gzip, deflate, br");
		//   getdata.addHeader("Referer", Referer);
		   getdata.addHeader("Upgrade-Insecure-Requests", "1");
		   getdata.addHeader("Connection", "keep-alive");
		   getdata.addHeader("Host", "member.rapnet.com");
		   CloseableHttpClient client=null;
		 
				 try {
					       client=Apiurl.newhttpclient();
					       HttpResponse impes=client.execute(getdata);//执行图片真是路径
						// 读取新的 URL 地址 
						   Header header=impes.getFirstHeader("Location");
						 if(header!=null){
							 String newuri=header.getValue();
						     System.out.println("图片地址:"+impes.getFirstHeader("Location").getValue());
						 }else{
							 System.out.println("图片地址空");
						 }
						 String newloginPosthtml= EntityUtils.toString(impes.getEntity());
						 System.out.println(newloginPosthtml);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   
		 
	}

}
