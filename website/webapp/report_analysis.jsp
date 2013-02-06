<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Analyse du rapport</title>
<script src="/website-0.0.1-SNAPSHOT/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">

	var filename="<%= request.getParameter("file") %>";
	
	function test() {
		
		$.load('/website-0.0.1-SNAPSHOT/GraphProvider');
		
	}
	
	
	function progress(noob) {
		
		$("#progress").html(noob);
		
	}

</script>
</head>
<body>
	<div id="progress"></div>
</body>
</html>
