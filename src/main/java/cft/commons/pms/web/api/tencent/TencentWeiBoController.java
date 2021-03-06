package cft.commons.pms.web.api.tencent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cft.commons.core.util.HttpClientUtils;
import cft.commons.pms.dto.tencent.ReviewDTO;
import cft.commons.pms.dto.tencent.TopicDTO;
import cft.commons.pms.dto.tencent.WeiBoDTO;
import cft.commons.pms.web.api.util.ApiUtils;

@Controller
public class TencentWeiBoController {

	private static final String APP_KEY = "801495189";
	private static final String CLIENT_SECET = "ff66b84d1e37af0b630639de332ef996";
	private static final String REDIRECT_URL = "http://localhost:8080/pms/tencent/tweibo.do";
	//调用API需要的公共部分
	private static final String COMMON_URL = "https://open.t.qq.com/api";
	//公有参数
	private static final String COMMON_PARAM = "oauth_consumer_key=" + APP_KEY
			                                   + "&oauth_version=2.a&scope=all";
	
	
	
    
	/////////////////////////////////////////////////////////////
	/////////////////////以下为*.do请求处理方法////////////////////////
	/////////////////////////////////////////////////////////////
	/* 回调地址  */
	@RequestMapping(value = "tweibo.do")
	public String tweibo(String code, String openid, String openkey,HttpServletRequest request) throws Exception {
		//获取code，通过code发出请求获取access_token
		String url = "https://open.t.qq.com/cgi-bin/oauth2/access_token?client_id=" + APP_KEY
				     + "&client_secret=" + CLIENT_SECET + "&redirect_uri=" + REDIRECT_URL
				     + "&grant_type=authorization_code&code=" + code;
		
		//发出请求，成功则返回带access_token的url字符串
		String result = HttpClientUtils.httpGet(url, 9000, 9000);
		String[] params = result.split("&");
		String tencent_token = "";
		
		//遍历切割后的字符串,获取access_token
		for (String param : params) {
			if (param.contains("access_token")) {
				tencent_token = param.split("=")[1];
				break;
			}
		}
		
		//判断是否授权成功
		if(!tencent_token.equals("")){
			//调用API需要的部分公有参数
			request.getSession().setAttribute("tencent_token", tencent_token);
		    request.getSession().setAttribute("openid", openid);
			request.getSession().setAttribute("openkey", openkey);
		}
		
		return "easyui/layout";
	}
    
    /* 发送一条微博消息   */	
	@RequestMapping(value="sendWeiBo.do")
	public @ResponseBody
	String sendWeiBo(String content,HttpServletRequest request) throws Exception{
		
		String url = COMMON_URL + "/t/add";
		
		Map<String,String> nvpMap = new HashMap<String,String>();
		//私有参数
		nvpMap.put("format", "json");
		nvpMap.put("content", content);
		nvpMap.put("clientip", ApiUtils.getClientIP());
		//公有参数
		nvpMap.put("access_token",(String)request.getSession().getAttribute("tencent_token"));
		nvpMap.put("oauth_consumer_key",APP_KEY);
		nvpMap.put("openid",(String)request.getSession().getAttribute("openid"));
		nvpMap.put("openkey",(String)request.getSession().getAttribute("openkey"));
		nvpMap.put("oauth_version", "2.a");
		nvpMap.put("scope", "all");
		//发出请求,返回字符串结果
		String result = HttpClientUtils.httpPost(nvpMap, url, 5000, 5000);

        System.out.println(result);

		/** 将字符串转成JSON,获取需要的信息    **/
		JSONObject jsonObject = new JSONObject(result);
		int errcode = (Integer)jsonObject.get("errcode");
		int ret = (Integer)jsonObject.get("ret");
		
		//根据官方文档,当errcode=0&&ret=0即请求成功
		if(errcode == 0 && ret == 0){
			return "success";
		}
		
		return "failure";
	}
	
	
	/* 发布一条带图片的微博    */
	@RequestMapping(value="sendPicWeiBo.do")
	public @ResponseBody
	String sendPicWeiBo(String content,HttpServletRequest request) throws Exception{
		
		String url = COMMON_URL + "/t/add_pic";
		
		Map<String,String> nvpMap = new HashMap<String,String>();
		Map<String, byte[]> itemsMap = new HashMap<String, byte[]>();
		
		//API需要的参数
		nvpMap.put("format", "json");
		nvpMap.put("content", content);
		nvpMap.put("clientip", ApiUtils.getClientIP());
		nvpMap.put("access_token", (String)request.getSession().getAttribute("tencent_token"));
		nvpMap.put("openid", (String)request.getSession().getAttribute("openid"));
		nvpMap.put("openkey", (String)request.getSession().getAttribute("openkey"));
		nvpMap.put("oauth_consumer_key", APP_KEY);
		nvpMap.put("oauth_version", "2.a");
		nvpMap.put("scope", "all");
		
		//读入图片,转成字节
		byte[] b = ApiUtils.readFileImage(request.getSession().getServletContext().getRealPath("/") + "/static/images/test.jpg");
		itemsMap.put("pic", b);
		
		//发出请求
		String info = ApiUtils.postMethodRequestWithFile(url, nvpMap, ApiUtils.header, itemsMap);
		
		if(info.equals("")){
			return "failure";
		}
		
		return "success";
	}
	
	
	/* 获取评论  */
	@RequestMapping(value="getReview.do")
	public String getReview(String rootid,HttpServletRequest request) throws Exception{
		//请求url
		String url = COMMON_URL + "/t/re_list?"
				     + "format=json"
				     + "&flag=1"
				     + "&rootid=" + rootid
				     + "&pageflag=0"
				     + "&pagetime=0"
				     + "&reqnum=10"
				     + "&twitterid=0"
				     + "&access_token=" + request.getSession().getAttribute("tencent_token")
		             + "&openid=" + request.getSession().getAttribute("openid")
		             + "&openkey=" + request.getSession().getAttribute("openkey")
		             + "&clientip=" + ApiUtils.getClientIP()
				     + "&" + COMMON_PARAM;

		String result = HttpClientUtils.httpGet(url, 5000, 5000);
//System.out.println(result);
		/* 将返回结果转换成JSON,获取需要的数据    */
		JSONObject json = new JSONObject(result);
		String msg = (String)json.get("msg");
		if(msg.equals("have no tweet")){
			request.setAttribute("message", "此微博目前没有被评论");
			return "tencent/getReview";
		}
		
		JSONObject data= json.getJSONObject("data");
		JSONArray info = data.getJSONArray("info");
		List<ReviewDTO> reList = new ArrayList<ReviewDTO>();
		
		for(int i = 0 ; i < info.length() ; i++){
			JSONObject obj = info.getJSONObject(i);
			
			//将需要的信息放入dto
			ReviewDTO re = new ReviewDTO();
			re.setId((String)obj.get("id"));
			re.setNick((String)obj.get("nick"));
			re.setText((String)obj.get("origtext"));
			re.setTimestamp((Integer)obj.get("timestamp"));
			
			reList.add(re);
		}
		
		if(reList != null && !reList.isEmpty()){
			request.setAttribute("reList", reList);
		}else{
			System.out.println("空");
		}
		
		return "tencent/getReview";
	}
	
	
	/* 转发微博    */
	@RequestMapping(value="forwardWeiBo.do")
	public String forwardWeiBo(String reid,String content,HttpServletRequest request) throws Exception{
		//请求url
		String url = COMMON_URL + "/t/re_add";
		
		Map<String,String> nvpMap = new HashMap<String,String>();
		//私有参数
		nvpMap.put("format", "json");
		nvpMap.put("content", content);
		nvpMap.put("clientip", ApiUtils.getClientIP());
		nvpMap.put("reid", reid);
		//公有参数
		nvpMap.put("access_token", (String)request.getSession().getAttribute("tencent_token"));
		nvpMap.put("openid", (String)request.getSession().getAttribute("openid"));
		nvpMap.put("openkey", (String)request.getSession().getAttribute("openkey"));
		nvpMap.put("oauth_consumer_key", APP_KEY);
		nvpMap.put("oauth_version", "2.a");
		nvpMap.put("scope", "all");
		
		String result = HttpClientUtils.httpPost(nvpMap, url, 5000, 5000);

		/*  将返回的字符串转换成JSON,获取需要的数据       */
		JSONObject json = new JSONObject(result);
		int errcode = (Integer)json.get("errcode");
		int ret = (Integer)json.get("ret");
		
		//当errcode==0&&ret==0时,请求成功
		if(errcode == 0 && ret == 0){
			request.setAttribute("success_info", "转发成功！请登录腾讯微博查看结果");
		}
		
		return "tencent/result";
	}
	
	
	/*  获取自己及关注的人的最新微博    */
	@RequestMapping(value="getFocusPeopleWeiBo.do")
	public @ResponseBody
	String getFocusPeopleWeiBo(HttpServletRequest request) throws Exception{
		//请求url
		String url = COMMON_URL + "/statuses/home_timeline?"
				     + "format=json"
				     + "&pageflag=0"
				     + "&pagetime=0"
				     + "&reqnum=20"
				     + "&type=3"
				     + "&contenttype=0"
				     + "&access_token=" + request.getSession().getAttribute("tencent_token")
				     + "&openid=" + request.getSession().getAttribute("openid")
				     + "&openkey=" + request.getSession().getAttribute("openkey")
				     + "&clientip=" + ApiUtils.getClientIP()
				     + "&" + COMMON_PARAM;
		
		String result = HttpClientUtils.httpGet(url, 5000, 5000);
//System.out.println(result);
		/*将返回的字符串转换成JSON,获取需要的数据*/
        JSONObject json = new JSONObject(result);
        JSONObject data = json.getJSONObject("data");
        JSONArray info = data.getJSONArray("info");
        List<WeiBoDTO> focusList = new ArrayList<WeiBoDTO>();
        
        for(int i = 0 ; i < info.length() ; i++){
        	JSONObject obj = info.getJSONObject(i);
        	//用户唯一id，用来排除自己的微博
        	//String uid = (String)obj.get("openid");
        
        	//用uid排除自己的微博
        	//if(!request.getSession().getAttribute("openid").equals(uid)){
        		//获取当前日期
        		String nowDate = ApiUtils.getNowDate();
        		//获取微博发表时间
        		String wbDate = ApiUtils.timestampToDate((Integer)obj.get("timestamp"));
        		
        		//获取当前日期发表的微博,排除其他微博
        		if(wbDate.equals(nowDate)){
        			WeiBoDTO wb = new WeiBoDTO();
        			
        			//判断微博内容中是否带http链接
        			if(obj.getString("text").indexOf("<a href") != -1){
		        		wb.setText(obj.getString("origtext"));
		        	}else{
		        		wb.setText(obj.getString("text"));
		        	}
        			
        			//判断此微博是不是转发的
		        	if(obj.getInt("type") == 2){
		        		JSONObject source = (JSONObject)obj.get("source");
		        		wb.setOrigtext(source.getString("origtext"));
		        	}
		        	
		        	//判断微博内容中是否带图片
		        	if(!obj.get("image").toString().equals("null")){
		        		//获取图片url数组
		        		JSONArray imageArray = (JSONArray)obj.get("image");
		        		String[] imgs = new String[imageArray.length()];
		                
		        		for(int j = 0 ; j < imageArray.length() ; j++){
		        			 imgs[j] = imageArray.get(j).toString();
		        		     wb.setImageUrls(imgs);
		        		}
		        	}
		        	
		            
		        	wb.setId(obj.getString("id"));
		        	wb.setNick(obj.getString("nick"));
		        	wb.setType(obj.getInt("type"));
		        	wb.setTimestamp(obj.getInt("timestamp"));	
		        	wb.setDate(ApiUtils.getWeiBoTime(obj.getInt("timestamp")));//将获取的timestamp转换时间格式
		       	
		        	
		        	focusList.add(wb);	
        		}
        	//}
        }
        
        /*
         * 将list拼接成JSON格式的字符串，返回给前台JQuery解析
         */
        String resultData = "";
    	
    	/*
    	 * 若微博带图，则使用下面的变量将获取到的图片url数组写成json格式，再和resultData字符串拼接起来
    	 */
//    	String images = "";
//    	String imgBegin = "[";
//    	String imgEnd = "]";
    	
        
        if(focusList == null || focusList.isEmpty()){
             return "empty";
        }else{
        	JSONArray jsonArray = new JSONArray();
        	
        	for(WeiBoDTO wb : focusList){
        		JSONObject jsonObject = new JSONObject();
        		jsonObject.put("id", wb.getId());
        		jsonObject.put("content", wb.getText());
        		jsonObject.put("name", wb.getNick());
        		jsonObject.put("time", wb.getDate());
        		//判断微博内容是否带图
        		if(wb.getImageUrls() != null){
        			jsonObject.put("images", wb.getImageUrls()[0]);
        		}else{
        			jsonObject.put("images", "null");
        		}
        		
        		jsonArray.put(jsonObject);
        	}
        	
        	 resultData = jsonArray.toString();
        }
        
       
System.out.println(resultData);

        return resultData;
	}
	
	
	/*  获取转播数和评论数     */
	@RequestMapping(value="getReviewAndBroadcast.do")
	public @ResponseBody String getReviewAndBroadcast(HttpServletRequest request) throws Exception{
		String ids = "377850008710440";
		
		//请求url
		String url = COMMON_URL + "/t/re_count?"
				     + "format=json"
				     + "&ids=" + ids
				     + "&flag=2"
				     + "&access_token=" + request.getSession().getAttribute("tencent_token")
				     + "&openid=" + request.getSession().getAttribute("openid")
				     + "&openkey" + request.getSession().getAttribute("openkey")
				     + "&clientip" + ApiUtils.getClientIP()
				     + "&" + COMMON_PARAM;
		
		String result = HttpClientUtils.httpGet(url, 5000, 5000);

        /* 将返回的字符串转换成JSON,获取需要的数据  */
        JSONObject json = new JSONObject(result);
        JSONObject data = json.getJSONObject("data");
        JSONObject wbID = data.getJSONObject(ids);
        
        int count = (Integer)wbID.get("count");
        int mcount = (Integer)wbID.get("mcount");
		
        
		return "count=" +count + ",mcount="+mcount;
	}
	
	
	/*
	 * 获取话题热榜
	 * */
	@RequestMapping(value="getHotTopicList.do")
	public String getHotTopicList(HttpServletRequest request) throws Exception{
		
	    //if(pos == null || pos.equals("")){
	    	//pos = "0";
	   // }
	    
		String url = COMMON_URL + "/trends/ht?"
				     + "format=json"
				     + "&reqnum=20"
				     + "&pos=0" 
				     + "&access_token=" + request.getSession().getAttribute("tencent_token")
				     + "&openid=" + request.getSession().getAttribute("openid")
		             + "&openkey=" + request.getSession().getAttribute("openkey")
		             + "&clientip=" + ApiUtils.getClientIP()
				     + "&" + COMMON_PARAM;
	    
		
		String result = HttpClientUtils.httpGet(url, 9000, 9000);
		
		//解析字符串,获取数据
		JSONObject json = new JSONObject(result);
		JSONObject data = json.getJSONObject("data");
		JSONArray info = data.getJSONArray("info");
		
		int hasnext = data.getInt("hasnext");
		int pos = data.getInt("pos");
		
		List<TopicDTO> tdList = new ArrayList<TopicDTO>();
		
		for(int i = 0 ; i < info.length() ; i++){
			JSONObject obj = info.getJSONObject(i);
			TopicDTO td = new TopicDTO();
			
			td.setId(obj.getString("id"));
			td.setKeywords(obj.getString("keywords"));
			td.setName(obj.getString("name"));
			td.setFavnum(obj.getInt("favnum"));
			td.setTweetnum(obj.getInt("tweetnum"));
			
			tdList.add(td);
		}
		
		if(tdList != null && !tdList.isEmpty()){
			request.setAttribute("tdList", tdList);
			request.setAttribute("hasnext", hasnext);
			request.setAttribute("pos", pos);
		}else{
			request.setAttribute("msg", "获取话题热榜失败!");
		}
		
		return "tencent/hotTopicList";
	}
}
