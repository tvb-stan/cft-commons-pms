package cft.commons.pms.web.api.util.constants;
/**
 * @author luffy
 *
 */
public class SinaConstants {
	
	public static final String APP_KEY = "4281626272";
	public static final String CLIENT_SECET = "69eade123bb72a88abadcdde95e8e6ae";
	public static final String REDIRECT_URL = "http://localhost:8080/pms/sina/sinaweibo.do";
	
	/**
	 * @sina api url
	 *
	 */
	public static final String Access_TokenUrl = "https://api.weibo.com/oauth2/access_token";
	public static final String StatusesUpdateUrl = "https://api.weibo.com/2/statuses/update.json";
	public static final String StatusesUploadUrl = "https://upload.api.weibo.com/2/statuses/upload.json";
	public static final String StatusesFriendsTimelineUrl = "https://api.weibo.com/2/statuses/friends_timeline.json";
}
