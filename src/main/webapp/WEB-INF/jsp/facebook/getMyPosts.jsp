<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Your Friends</title>
<style type="text/css">
.w1{
    width:300px;
   }
</style>

<script>

	(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) return;
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/zh_CN/sdk.js#xfbml=1&version=v2.0";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
</script>

</head>
<body>

<div style="width:800px;padding-top: 30px;">			
            
            <span id="auth_msg" class="alert alert-danger" style="display:none;float:left;margin-left:250px"></span>
            <br>
            <span id="data_msg" class="alert alert-info" style="display:none;float:left;margin-left:280px"></span>		
			
			<div class="form-group">
				<div id="dd" class="col-sm-11" >
					<table id="wbTable" class="table" style="argin-left:40px" class="myList">
						<thead>
						<!-- 判断Facebook是否有授权 -->
							<c:choose>
								<c:when test="${sessionScope.facebook_token != null && sessionScope.facebook_token != ''}">
									<tr align='center' class='success'>
										<td style='display:none'>微博ID</td>
										<td class='w1'>My Posts:</td>
				   					</tr>
								</c:when>
								<c:otherwise>
									Facebook is not for authorization, can't get my message!<br><br>
								</c:otherwise>
							</c:choose>
						</thead>
						<tbody>
							<!-- 判断当前用户是否有消息 -->
		   					<c:choose>
		   						<c:when test="${myList != null && myList != ''}">
		   							<c:forEach items="${myList}" var="myposts">
		   								<tr align='center'>
		            		            	<td style='display:none'>${myposts.postID}</td>
		            		            	<td class='w1'>
			            		            	<div align="left">
			            		            		<font size="2px" color="#8e7437">${myposts.update_time}</font>
			            		            		<br>${myposts.message}
			            		            		<!-- 如果有图片则显示 -->
			            		            		<c:if test="${'null' != myposts.imageUrl && '' != myposts.imageUrl}">
			            		            			<br><img src="${myposts.imageUrl}" />
			            		            		</c:if>
		            		            			<br>
		            		            			<div align="right">
		            		            				<font color="#f58220">Comment(&nbsp;${myposts.commentNum}&nbsp;)</font>
		            		            				&nbsp;&nbsp;&nbsp;
			            		            			<div class="fb-like" data-href="https://www.facebook.com/${fn:replace(myposts.postID,'_','/posts/')}" data-width="10" data-layout="button_count" data-action="like" data-show-faces="false" data-share="false"></div>
				            		            		<div class="fb-share-button" data-href="https://www.facebook.com/${fn:replace(myposts.postID,'_','/posts/')}" data-width="10" data-type="button_count"></div>
		            		            			</div>
		            		            			<br><div class="fb-comments" data-href="https://www.facebook.com/${fn:replace(myposts.postID,'_','/posts/')}" data-numposts="3" data-colorscheme="light"></div>
		            		            			<br>
		            		            			<c:forEach items="${myposts.comDTO}" var="mycommments">
			            		            			<font ></font> ${mycommments.fromName}&nbsp;&nbsp;&nbsp;<font size="2px" color="#8e7437">${mycommments.createTime}</font><br>
			            		            			${mycommments.message}<br>
			            		            		</c:forEach>
		            		            		</div>
		            		            	</td>
	            		            	</tr>
		   							</c:forEach>
		   						</c:when>
		   						<c:otherwise>
		   							<c:if test="${sessionScope.facebook_token != null && sessionScope.facebook_token != ''}">
		   								You don't have post anything, let's post!
		   							</c:if>
		   						</c:otherwise>
		   					</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			
</div>

<div id="fb-root"></div>

</body>
</html>
