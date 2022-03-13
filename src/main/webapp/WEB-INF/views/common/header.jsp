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
<title>header</title>
</head>
<body>
<table border=0  width="100%" style="padding:0 50px;">
  <tr>
     <td>
		<a href="${contextPath}/main.do">
			<img src="${contextPath}/resources/image/osulloc.png"/>
		</a>
     </td>
     <td>
       <img src="${contextPath}/resources/image/main.png"/>
     </td>
     
     <td width=200 style="word-break:break-all;">
       <c:choose>
          <c:when test="${isLogOn == true  && member!= null}">
            <h3>환영합니다. ${member.name}님!</h3>
            <a href="${contextPath}/member/logout.do"><h3 style="margin-bottom:0;">로그아웃</h3></a>
          </c:when>
          <c:otherwise>
	        <a href="${contextPath}/member/loginForm.do"><h3 style="margin-bottom:0">로그인</h3></a>
	      </c:otherwise>
	   </c:choose>     
     </td>
  </tr>
</table>
</body>
</html>