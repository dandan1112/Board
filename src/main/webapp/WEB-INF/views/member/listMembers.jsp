<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
	request.setCharacterEncoding("UTF-8");
%>


<html>
<head>
<meta charset=UTF-8">
<title>member list</title>
</head>
<style>
.cls1 {
	text-decoration: none;
}
.cls2 {
	text-align: center;
	font-size: 20px;
}
</style>
<body>
	<br>
	<table border="1" align="center" width="80%">
		<tr align="center" bgcolor="#696969">
			<td><font color="white">아이디</font></td>
			<td><font color="white">비밀번호</font></td>
			<td><font color="white">이름</font></td>
			<td><font color="white">이메일</font></td>
			<td><font color="white">가입일</font></td>
			<td></td>
		</tr>

		<c:forEach var="member" items="${membersList}">
			<tr align="center">
				<td>${member.id}</td>
				<td>${member.pwd}</td>
				<td>${member.name}</td>
				<td>${member.email}</td>
				<td>${member.joinDate}</td>
				<td><a
					href="${contextPath}/member/removeMember.do?id=${member.id}">삭제</a></td>
			</tr>
		</c:forEach>
	</table>
	<a class="cls1" href="${contextPath}/member/memberForm.do">
	<p class="cls2"><b>회원가입</b></p>
	</a>
</body>
</html>
