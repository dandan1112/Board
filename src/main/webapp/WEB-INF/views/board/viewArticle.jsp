<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>

<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<c:set var="article"  value="${articleMap.article}"  />
<c:set var="removeCompleted"  value="${articleMap.removeCompleted}"  />
<c:set var="imageFileList"  value="${articleMap.imageFileList}"  />

<%
  request.setCharacterEncoding("UTF-8");
%> 

<head>
   <meta charset="UTF-8">
   <title>글보기</title>
   <script  src="http://code.jquery.com/jquery-latest.min.js"></script>
      
   <style>
     #tr_btn_modify{
       display:none;
     }
     
    .tr_modEnable {
      visibility:hidden;
     }
     img {
	max-width: 50%;
	}
   </style>
   
   <script type="text/javascript" >
   
   	// 게시글 조회 화면으로 돌아가기
     function backToList(obj){
	    obj.action="${contextPath}/board/listArticles.do";
	    obj.submit();
     }
 	// 버튼 설정 변경
	function fn_enable(){
		 document.getElementById("i_title").disabled=false; // 제목, 내용 수정만 가능하도록 설정 변경
		 document.getElementById("i_content").disabled=false;
		 document.getElementById("tr_btn_modify").style.display="block"; // tr_btn_modify 버튼 나타나게 함
		 document.getElementById("tr_btn").style.display="none"; // tr_btn의 버튼들 없애기
		 $(".tr_modEnable").css('visibility', 'visible'); // 이미지 추가 버튼 보이기
	 }
	 // modArticle.do로 정보 제출
	 function fn_modify_article(obj){
		 obj.action="${contextPath}/board/modArticle.do";
		 obj.submit();
	 }
	 // form에 정보 넣어 removeArticle.do로 보내기
	 function fn_remove_article(url,articleNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
	     var articleNOInput = document.createElement("input");
	     articleNOInput.setAttribute("type","hidden");
	     articleNOInput.setAttribute("name","articleNO");
	     articleNOInput.setAttribute("value", articleNO);
		 
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);
	     form.submit();
	 }
	 // form에 정보 넣어 replyForm.do로 보내기
	 function fn_reply_form(isLogOn, url, parentNO){
		  if(isLogOn != '' && isLogOn != 'false'){
				 var form = document.createElement("form");
				 form.setAttribute("method", "post");
				 form.setAttribute("action", url);
			     var parentNOInput = document.createElement("input");
			     parentNOInput.setAttribute("type","hidden");
			     parentNOInput.setAttribute("name","parentNO");
			     parentNOInput.setAttribute("value", parentNO);
				 
			     form.appendChild(parentNOInput);
			     document.body.appendChild(form);
				 form.submit();
			  }else{
			    alert("로그인 후 글쓰기가 가능합니다.");
			    location.href="${contextPath}/member/loginForm.do?action=/board/replyForm.do&parentNO="+parentNO;
			  }
		 }
	// 파일이 있으면 미리보기에 사진 보이기
	 function readURL(input, index) {
	     if (input.files && input.files[0]) {
	         var reader = new FileReader();
	         reader.onload = function (e) { // 읽기 성공 시 실행
	             $('#preview'+index).attr('src', e.target.result); // 속성을 가져오거나 추가
	         }
	         reader.readAsDataURL(input.files[0]);
	     }
	 }  
	 
 </script>
</head>
<body>
<br>
  <form name="frmArticle" method="post"   enctype="multipart/form-data">
  <table  border=0  align="center" >
  <tr>
   <td width=150 align="center" bgcolor="#696969">
   <font color="white">글번호</font>
   </td>
   <td >
    <input type="text"  value="${article.articleNO }"  disabled />
    <input type="hidden" name="articleNO" value="${article.articleNO}"  />
   </td>
  </tr>
  <tr>
    <td width="150" align="center" bgcolor="#696969">
      <font color="white">작성자 아이디</font>
   </td>
   <td >
    <input type=text value="${article.id}" name="writer"  disabled />
   </td>
  </tr>
  <tr>
    <td width="150" align="center" bgcolor="#696969">
     <font color="white">제목 </font>
   </td>
   <td>
    <input type=text value="${article.title}"  name="title"  id="i_title" disabled />
   </td>   
  </tr>
  <tr>
    <td width="150" align="center" bgcolor="#696969">
      <font color="white">내용</font>
   </td>
   <td>
    <textarea rows="20" cols="60"  name="content"  id="i_content"  disabled>${article.content}</textarea>
   </td>  
  </tr>

<c:set  var="img_index" />
<c:choose>  
	 <c:when test="${not empty imageFileList && imageFileList!='null' }">
		  <c:forEach var="item" items="${imageFileList}" varStatus="status" >
			    <tr id="tr_${status.count}">
				    <td width="150" align="center" bgcolor="#696969" >
				      <font color="white">file ${status.count}</font>
				   </td>
				   <td>
				    <img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${item.imageFileName}" id="preview${status.index}"/>
				   </td>   
				</tr>  
				  <tr  class ="tr_modEnable"   id="tr_sub${status.count}">
				    <td width="150" align="center" bgcolor="#696969"><font color="white">파일 수정</font></td>
				    <td>
				       <input  type="file" name="imageFileName${status.index}" id="i_imageFileName${status.index}" onchange="readURL(this, ${status.index});"   />
				       <input  type="button" value="이미지 삭제하기"/>
				    </td>
				 </tr>
			</c:forEach>
   </c:when>	
 </c:choose>
 
  <tr class ="tr_modEnable">
	   <td colspan="2">
	      <input type="button"   value="이미지 추가"/>
	   </td>
  </tr>

  <tr>
	   <td width="150" align="center" bgcolor="#696969">
	     <font color="white">등록일자</font>
	   </td>
	   <td bgcolor="f5f5dc">
	      ${article.writeDate}
	   </td>   
  </tr>
   
  <tr   id="tr_btn_modify">
	   <td colspan="2" align="right">
	       <input type=button value="수정반영하기"   onClick="fn_modify_article(frmArticle)"  >
           <input type=button value="취소"  onClick="backToList(frmArticle)">
	   </td>   
  </tr>
    
  <tr  id="tr_btn"    >
   <td colspan="2" align="center">
       <c:if test="${member.id == article.id}">
	      <input type=button value="수정하기" onClick="fn_enable()">
	      <input type=button value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
	    </c:if>
	    <input type=button value="리스트로 돌아가기"  onClick="backToList(this.form)">
	     <input type=button value="답글쓰기">
   </td>
  </tr>
 </table>
 </form>
</body>
</html>