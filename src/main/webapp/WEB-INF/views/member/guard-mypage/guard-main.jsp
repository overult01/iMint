<!-- 마이페이지 기능 중 회원 정보 삭제 & 회원 탈퇴 기능의 백엔드는 양정민님 파트입니다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>@@아기당근@@ :: 마이페이지</title>
	<jsp:include page="../../libs/libsStyles.jsp" flush="false" />
</head>

<body>
 	<jsp:include page="../../include/header.jsp" flush="false" />
 	<aside></aside>
	
	
	보호자 마이페이지 메인입니다.
	
	
	<jsp:include page="../../include/footer.jsp" flush="false"/>
	<jsp:include page="../../libs/libsScript.jsp" flush="false" />
	<script src="/static/js/guard-main.js"></script>
</body>
</html>