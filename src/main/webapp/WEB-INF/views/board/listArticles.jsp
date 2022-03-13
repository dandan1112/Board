<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="articlesList" value="${articlesMap.articlesList}" />
<c:set var="section" value="${articlesMap.section}" />
<c:set var="pageNum" value="${articlesMap.pageNum}" />
<c:set var="totalArticles" value="${articlesMap.totalArticles}" />

<fmt:parseDate var="parsedDate" value="${nowday}" pattern="yy-dd-mm" />


<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<style>
.cls1 {
	text-decoration: none;
}

.cls2 {
	text-align: center;
	font-size: 20px;
}

.cls3 {
	text-decoration: none;
}
</style>
<meta charset="UTF-8">
<title>글목록창</title>
</head>
<script>

// 로그안 하지 않고 글 쓰기 시도할 경우 로그인 폼으로 이동
	function fn_articleForm(isLogOn, articleForm, loginForm) {
		if (isLogOn != '' && isLogOn != 'false') {
			location.href = articleForm;
		} else {
			alert("로그인 후 글쓰기가 가능합니다.")
			location.href = loginForm + '?action=/board/articleForm.do';
		}
	}
</script>
<body>
	<br>
	<table align="center" border="1" width="80%">
		<tr height="10" align="center" bgcolor="#696969">
			<td><font color="white">글 번호</font></td>
			<td><font color="white">작성자</font></td>
			<td><font color="white">제목</font></td>
			<td><font color="white">작성일</font></td>
			<td><font color="white">조회 수</font></td>
		</tr>
		
		<c:choose>
			<c:when test="${articlesList ==null }">
				<tr height="10">
					<td colspan="4">
						<p align="center">
							<b><span style="font-size: 9pt;">등록된 글이 없습니다.</span></b>
						</p>
					</td>
				</tr>
			</c:when>

			<c:when test="${articlesList !=null }">
				<c:forEach var="article" items="${articlesList}" varStatus="articleNum">
					<tr align="center">
						<td width="5%">${articleNum.count}</td>
						<td width="10%">${article.id}</td>
						<td align='left' width="35%">
						<span style="padding-right: 30px"></span>
						
						<c:choose>
							<c:when test='${article.lvl > 1}'>
								<c:forEach begin="1" end="${article.lvl}" step="1">
									<span style="padding-left: 20px"> </span>
								</c:forEach>
									<span style="font-size: 12px;">[답변]</span>
									<a class='cls1'
										href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}
									</a>
									<span> <c:if test="${article.writeDate >= nowday}">
											<img class="uploadNew" src="../resources/image/new.png"
												style="width: 28px; height: 15px;" />
										</c:if>
									</span>
								</c:when>

								<c:otherwise>
									<a class='cls1'
										href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}</a>
									<span>
										<c:if test="${article.writeDate >= nowday}">
											<img class="uploadNew" src="../resources/image/new.png" style="width: 28px; height: 15px;" />
										</c:if>
									</span>
								</c:otherwise>
							</c:choose></td>

						<td width="10%">${article.writeDate}</td>
						<td width="10%">${article.viewNum}</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>

	<div>
		<c:choose>
			<c:when test="${totalArticles >= 100}">
				<!-- 글 개수 > 100 -->
				<c:forEach var="page" begin="1" end="10" step="1">
					<c:if test="${section > 1 && page == 1 }">
						<a class="cls3"
							href="${contextPath}/board/listArticles.do?section=${section-1}&pageNum=${(section-1)*10+1}">[처음]</a>
					</c:if>
					<a class="cls3"
						href="${contextPath}/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10+page}
					</a>
					<c:if test="${page == 10}">
						<a class="cls3"
							href="${contextPath}/board/listArticles.do?section=${section+1}&pageNum=${section*10+1}">[이전]</a>
					</c:if>
				</c:forEach>
			</c:when>

			<c:when test="${totalArticles < 100}">
				<!-- 글 개수 < 100  -->
				<c:forEach var="page" begin="1" end="${totalArticles/10 +1}"
					step="1">
					<c:choose>
						<c:when test="${page == pageNum}">
							<a class="cls3"
								href="${contextPath}/board/listArticles.do?section=${section}&pageNum=${page}">${page}
							</a>
						</c:when>
						<c:otherwise>
							<a class="cls3"
								href="${contextPath}/board/listArticles.do?section=${section}&pageNum=${page}">${page}
							</a>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
		</c:choose>
	</div>

	<a class="cls1"
		href="javascript:fn_articleForm('${isLogOn}','${contextPath}/board/articleForm.do','${contextPath}/member/loginForm.do')">
		<p class="cls2">
			<b>글쓰기</b>
		</p>
	</a>
</body>
</html>