<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div class="bs-sidebar hidden-print affix col-md-3" role="complementary" style="border-left:1px;border-left-color:#E7E7E7">
	<ul class="accordion">

		<li id="one" class="files"><a href="#one">Tencent API</a>
		    <ul class="sub-menu">
		        <li><a href="${ctx}/view/addWeiBoView.do">发送一条微博</a></li>
		        <li><a href="${ctx}/view/addPicWeiBoView.do">发送一条带图片的微博</a></li>
		        <li><a href="${ctx}/tencent/getView.do?state=comments">获取单条微博的评论列表</a>
		        <li><a href="${ctx}/tencent/getView.do?state=forward">转发一条微博</a>
		        <li><a href="${ctx}/tencent/getFocusPeopleWeiBo.do">获取关注的人的最新微博</a>
		        <li><a href="${ctx}/tencent/getHotTopicList.do">查看话题热榜</a>
		    </ul>

		</li>
		
		<li id="one" class="files"><a href="#one">Sina API</a>
			<ul class="sub-menu">
				<li><a href="${ctx}/view/sina_statuses_update"><em>01.1</em>发布一条微博信息</a></li>						
				<li><a href="${ctx}/view/sina_statuses_upload"><em>01.2</em>上传图片并发布一条微博</a></li>
				<li><a href="${ctx}/sina/sinaCommentsToMe.do"><em>02</em>获取评论列表</a></li>
				<li><a href="${ctx}/view/sina_statuses_repost"><em>04.1</em>转发一条微博信息</a></li>
				<li><a href="${ctx}/sina/sinaStatuseFriends.do"><em>04.2</em>转发某条微博信息</a></li>			
				<li><a href="${ctx}/sina/sinaStatuseFriends.do"><em>05</em>获取关注人的信息动态</a></li>
			</ul>
		</li>
		
		<li id="one" class="files"><a href="#one">Instagram API</a>
			
		<ul class="sub-menu">
				<li><a href="goCreate.do"><em>01</em>发布照片</a></li>
				<li><a href="comment.do"><em>01</em>获取各个不同的社交平台上最新的回复</a></li>
				<li><a href="goComment.do"><em>01</em>创建评论</a></li>
				<li><a href="followFirend.do"><em>01</em>获取关注人的信息动态</a></li>
				<li><a href="share.do"><em>01</em>分享</a></li>
				<li><a href="likeMedia.do"><em>01</em>分享一个like，并且获取这个like的在各个平台上的信息</a></li>
			    <li><a href="popular.do"><em>01</em>获取最受欢迎的照片</a></li>
			    <li><a href="followFirend2.do"><em>01</em>获取全部照片</a></li>
			</ul>	
			
		</li>
		
		<li id="one" class="files"><a href="#one">FaceBook API</a>
			<ul class="sub-menu">
				<li><a href="${ctx}/facebook/writeNewPost.do"><em>01</em>Write new post</a></li>
				<li><a href=""><em>02</em>Get friendlist</a></li>
				<li><a href=""><em>03</em>...</a></li>
				<li><a href=""><em>04</em>...</a></li>
				<li><a href=""><em>05</em>...</a></li>
				<li><a href=""><em>06</em>...</a></li>
			</ul>
		</li>
		
		
		<!-- 统一四大平台 -->
		<li id="one" class="files"><a href="#one">API</a>
			<ul class="sub-menu">
				<li><a href="${ctx}/view/sendMsgView.do">发布一条新消息</a></li>
				<li><a href="${ctx}/view/sendPicMsgView.do">发布一条带图片的新消息</a></li>
				<li><a href="${ctx}/view/getFriendsDynamicView.do">获取自己及关注的人的最新动态</a></li>
				<li><a href="${ctx}/view/getShare.do">分享</a></li>
			</ul>
		</li>
		
	</ul>
</div>
