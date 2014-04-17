<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>获取评论列表信息</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/dist/css/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/js/jquery-validation/validate.css">
<style type="text/css">
#ShuRu {
	background-color: #F0FFFF;
}
</style>
</head>

<body>

	<div id="ShuRu"
		style="border: 5px solid #3399FF; border-radius: 25px; moz-border-radius: 25px; width: 600px; height: 700px; padding-top: 30px;">
		<form class="form-horizontal" role="form" action="${pageContext.request.contextPath}/sina/sinaCommentsToMe.do" method="post" >
			
			<div class="form-group">
				<label class="col-sm-4 control-label" style="color:blue">获取评论列表信息>>></label>
			</div>
			
			<div class="form-group">
				<div class="col-sm-10" >
					<table class="table table-bordered table-hover"  style="margin-left:50px" >
						<tr align="center"  class="success">
							<td>评论的内容</td>
							<td>评论作者的用户昵称</td>
							<td>评论创建时间</td>
							<td>微博信息内容</td>
						</tr>
						<c:forEach items="${sinaComDTOs}" var="sinaComDTO" >
							<tr align="center"  class="warning">
								<td>${sinaComDTO.text}</td>
								<td>${sinaComDTO.screen_name}</td>
								<td>${sinaComDTO.created_at}</td>
								<td>${sinaComDTO.textStatus}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>

		</form>
	</div>
</body>
</html>