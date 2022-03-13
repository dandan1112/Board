<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>

<html>
<head>
  <meta charset="UTF-8">
  <title>sidebar</title>
 <style>
   .no-underline{
      text-decoration:none;
   }
 </style>
</head>

<body>
	<div style="background-color:#1B5E20"><h1><font color=white>Menu</font></h1></div>
	<br>
	<h1>
		<a href="${contextPath}/member/listMembers.do"  class="no-underline">회원 관리</a><br><br>
		<a href="${contextPath}/board/listArticles.do"  class="no-underline">게시판 관리</a><br><br>
		<a href="#" class="no-underline">상품 관리</a><br>
	</h1>
	
</body>
</html>