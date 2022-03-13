<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
%>

<head>
<meta charset="UTF-8">
<title>글쓰기창</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

	// 파일이 있으면 미리보기에 사진 보이기
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	// 게시글 조회 화면으로 돌아가기
	function backToList(obj) {
		obj.action = "${contextPath}/board/listArticles.do";
		obj.submit();
	}
	var cnt = 1;
	// 파일 추가 시 밑에 'file(번호)' 문구 추가
	function fn_addFile() {
		$("#d_file").append("<br>"+"<input type='file' name='file"+cnt+"'/>");
		cnt++;
	}
</script>
<title>글쓰기창</title>
</head>
<body>
	<br>
	<h1 style="text-align: center">게시글 작성</h1>
	<form name="articleForm" method="post" action="${contextPath}/board/addNewArticle.do" enctype="multipart/form-data">
		<table border="0" align="center">
			<tr>
				<td  width=150 align="center" bgcolor="#696969"><font color="white">작성자</font></td>
				<td colspan=2 align="left">
				<input type="text" size="20" maxlength="100" value="${member.name }" readonly />
				</td>
			</tr>
			<tr>
				<td width=150 align="center" bgcolor="#696969"><font color="white">게시글 구분</font></td>
				<td align="left">
				<input type="radio" name="notice" value=0>일반
				<input type="radio" name="notice" value=1>공지
				</td>
			</tr>
			<tr>
				<td  width=150 align="center" bgcolor="#696969"><font color="white">글 제목</font></td>
				<td colspan="2">
				<input type="text" name="title" size="68" maxlength="500"/></td>
			</tr>
			<tr>
				<td  width=150 align="center" bgcolor="#696969"><font color="white">글 내용</font></td>
				<td colspan=2>
				<textarea name="content" rows="10" cols="65" maxlength="4000" placeholder="내용을 입력해 주세요"></textarea></td>
			</tr>
			<tr>
				<td  width=150 align="center" bgcolor="#696969"><font color="white">이미지 파일 첨부</font></td>
				<td>
				<input type="file" name="imageFileName" onchange="readURL(this);" />
				</td>
				<td><img id="preview" src="#" width=200 height=200 /></td>

				<td align="left"><input type="button" value="파일 추가" onClick="fn_addFile()" /></td>
			</tr>
			<tr>
				<td colspan="4"><div id="d_file"></div></td>
			</tr>
			<tr>
				<td align="right"></td>
				<td colspan="2">
				<input type="submit" value="글쓰기" />
				<input type=button value="목록보기" onClick="backToList(this.form)" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
