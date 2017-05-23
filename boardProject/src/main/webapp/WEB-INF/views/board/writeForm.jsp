<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기 화면</title>
<link rel="stylesheet" type="text/css"
	href="/resources/include/css/common.css" />
<link rel="stylesheet" type="text/css"
	href="/resources/include/css/board.css" />
<script type="text/javascript"
	src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" src="/resources/include/js/common.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						/* 저장 버튼 클릭 시 처리 이벤트 */
						$("#boardInsert")
								.click(
										function() {
											// 입력값 체크 
											if (!chkSubmit($('#b_name'), "이름을"))
												return;
											else if (!chkSubmit($('#b_title'),
													"제목을"))
												return;
											else if (!chkSubmit(
													$('#b_content'), "작성할 내용을"))
												return;
											else if (!chkSubmit($('#b_pwd'),
													"비밀번호를"))
												return;
											else {
												/* 배열내의 값을 찾아서 인덱스를 반환(요소가 없을 경우 -1반환) 
												jQuery.inArray(찾을값, 검색 대상의 배열) */
												var ext = $('#file').val()
														.split('.').pop()
														.toLowerCase();
												if (jQuery.inArray(ext, [
														'gif', 'png', 'jsp',
														'jpeg' ]) == -1) {
													alert('gif,png,jpg,jpeg 파일만 업로드 할 수 있습니다.');
													return;
												}
												$("#f_writeForm").attr({
														"method":"POST",
														"action":"/board/boardInsert.do"
												});
												$("#f_writeForm").submit();
											}
										});
						
						/* 목록 버튼 클릭 시 처리 이벤트 */
						$("#boardList").click(function() {
							location.href = "/board/boardList.do"
						});
					});
</script>
</head>
<body>
	<div class="contentContainer">
		<div class="contentTit">
			<h3>게시판 글작성</h3>
		</div>
		<div class="contentTB">
			<form id="f_writeForm" name="f_writeForm"
				enctype="multipart/form-data">
				<input type="hidden" name="csrf" value="${CSRF_TOKEN}" />
				<table id="boardWrite">
					<colgroup>
						<col width="17%" />
						<col width="83%" />
					</colgroup>
					<tr>
						<td class="ac">작성자</td>
						<td><input type="text" name="b_name" id="b_name"></td>
					</tr>
					<tr>
						<td class="ac">글제목</td>
						<td><input type="text" name="b_title" id="b_title"></td>
					</tr>
					<tr>
						<td class="ac">내용</td>
						<td><textarea name="b_content" id="b_content"></textarea></td>
					</tr>
					<tr>
						<td class="ac">첨부파일</td>
						<td><input type="file" name="file" id="file"></td>
					</tr>
					<tr>
						<td>비밀번호</td>
						<td><input type="password" name="b_pwd" id="b_pwd"></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="boardBut">
			<input type="button" value="저장" class="but" id="boardInsert">
			<input type="button" value="목록" class="but" id="boardList">
		</div>
	</div>
</body>
</html>