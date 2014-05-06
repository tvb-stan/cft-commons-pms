<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Write New Post</title>
<script type="text/javascript"
	src="http://connect.facebook.net/en_US/all.js"></script>
<script>
	/* 发布新消息 */
	function writeNewPost(){
		
			FB.init({
				appId : '137410796429161',
				status : true,
				xfbml : true
			});
			
			FB.getLoginStatus(function(response) {
				if (response.status === 'connected') {
					alert("已登录");
					var uid = response.authResponse.userID;
					var facebookToken = response.authResponse.accessToken;
					/* alert(facebookToken); */
					/* 判断是否有图片 */
					if(document.getElementById("pictureUrl").value != "" || null != document.getElementById("pictureUrl").value){
						postFBmsg(document.getElementById("msg").value,document.getElementById("pictureUrl").value,facebookToken);
					}else{
						postFBmsg(document.getElementById("msg").value,facebookToken);
					}
					
				} else {
					FB.login(function (response){
						
						if (response.status === 'connected') {
							alert("回调");
							var facebookToken = response.authResponse.accessToken;
							
							/* 判断是否有图片 */
							if(document.getElementById("pictureUrl").value == "" || null == document.getElementById("pictureUrl").value){
								postFBmsg(document.getElementById("msg").value,document.getElementById("pictureUrl").value,facebookToken);
							}else{
								postFBmsg(document.getElementById("msg").value,facebookToken);
							}
						}
					});
				}
			});
	
	}
	
	function postFBmsg(msg,facebookToken){
		FB.api('/me/feed', 'post', {
			message : msg,
			access_token : facebookToken
		}, function(response) {
			if (!response || response.error) {
				alert('Error occured:'+response.error);
			} else {
				alert('Post ID: ' + response.id);
			}
		});
	}
	
	function postFBmsg(msg,pictureUrl,facebookToken){
		FB.api('/me/feed', 'post', {
			message : msg,
			access_token : facebookToken,
			picture : pictureUrl
		}, function(response) {
			if (!response || response.error) {
				alert('Error occured:'+response.error);
			} else {
				alert('Post ID: ' + response.id);
			}
		});
	}
	
	/* 点击删除pictureUrl */
	function clean(){
		document.getElementById("pictureUrl").value = "";
	}
	
</script>
</head>

<body>
	<div
		style="width: 600px; height: 300px; border: 2px solid #7CFC00; background-color: #E0FFFF">
		<label class="col-sm-3 control-label">内容:</label>
		<textarea  class="form-control" id="msg" rows="5" cols="60">这是测试微博.......</textarea>
		<br>
		<label class="col-sm-8 control-label">图片地址（Facebook相册图片不可以）:</label>
		<br>
		<input id ="pictureUrl" type="text" class="col-md-8" value="http://img4.duitang.com/uploads/item/201302/06/20130206195233_dHdt5.thumb.600_0.jpeg"/>
		&nbsp;&nbsp;
		<button onclick="clean();">清空</button>
		<br><br>
		<button onclick="writeNewPost();" style="float: right;">发布</button>
	</div>
	<div id="fb-root"></div>
</body>
</html>