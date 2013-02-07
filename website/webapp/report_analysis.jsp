<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Analyse du rapport</title>
<script src="/website-0.0.1-SNAPSHOT/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">

	var servlet = "/website-0.0.1-SNAPSHOT/GraphProvider";
	var filename="<%= request.getParameter("file") %>";
	
	var selectedKeywords = [];
	
	function getProgress(hash) {
		
		$("#progress").load(
				servlet,
				'hash='+hash
		);
	}
	
	function loadReport() {
		
		$.get(servlet, 
			{
				'file' :  filename 
			},
			function(data) {
				  
				console.log("Received hash : " + data);
				getProgress(data);	
			}
		);	
	}
	
	function loadKeywordSelector(keywords) {
		
		var selector = $("#keyword_selector");
		var keyword;
		
		for(var i in keywords) {
			
			keyword = keywords[i];
			
			selector.append(
					'<input type=\"checkbox\" name=\"keyword\" id=\"keyword\" value=\"'+ keyword +'\">'+ 
						keyword + 
					'<br>'
				);
		}
		
		$("");
		
		
	}
	
	function progress(noob) {
		
		$("#progress").html(noob);
		
	}

</script>
</head>
<body>
	<table>
		<tr>
		<td>
			Choisissez des mots clés :
		</td>
		<td>
			Graph :
		</td>
		</tr>
		<tr>
		<td>
		<div id="keyword_selector" style="position: absolute; overflow: auto; height: 90%; width: 200px;">
		</div>
		</td>
		<td>
			<div id="progress"></div>
		</td>
		</tr>
	</table>
</body>
</html>
