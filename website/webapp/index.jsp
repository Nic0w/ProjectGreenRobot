<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Développement Durable - Analyse automatisée de rapports</title>
<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">

	function launchTransition() {
		$("#content").fadeOut();
		window.location = "<%=request.getContextPath()%>/report_analysis.jsp";
	}
	
	function pollServer(data) {
		
		if(data.execute != undefined) {
			window[data.execute](data.args);
		}
		
		if(data.break_loop == undefined)
		$.ajax({
			url : "<%=request.getContextPath()%>/PDFReceiver", //server script to process data
			type : 'POST',
			success : function(data, textStatus, jqXHR) {

				console.log("Request was successful :");
				console.log("Response is : " + JSON.stringify(data));
				
				setTimeout(function() {
					pollServer(data);
				}, 
				600);
				
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("error : " + errorThrown + " -> " + textStatus);
			},
			dataType : 'json'
		});
		else if(data.transition) {
			setTimeout(launchTransition, 1000);
		}
		
	}


	function updateProgress(args) {
		
		if(args.progress == undefined) {
			$("progress").removeAttr("value"); /* reset the progress bar into undefined state */			
		}
		else {
			console.log("Updating progress : " + args.progress);
			
			$("progress").attr({value:args.progress,max:100});
			$("#percentage").html(args.progress + "%");
		}
		
		if(args.text != undefined) {
			console.log("Updating text : " + args.text);
			
			$("#status").html(args.text);
		}
		
		
	}

	function progressHandlingFunction(e){
    	if(e.lengthComputable){
        	$("progress").attr({value:e.loaded,max:e.total});
        	$("#percentage").html(Math.round((e.loaded/e.total)*100)+"%");
    	}
	}

	function submitForms() {

		console.log("Submitting form !");
		
		
		var formData;
		var progressHandler;

		if ($("#url_input")[0].disabled) { /* We upload a file ! */	
			console.log("User chose to upload a file.");
		
			formData = new FormData($("#file_upload")[0]);
			progressHandler = function(e) {
		    	if(e.lengthComputable){
		        	$('progress').attr({value:e.loaded,max:e.total});
		        	$("span").html(Math.round((e.loaded/e.total)*100)+"%");
		    	}
			};

		} else { /* The PDF File is downloaded from the given URL. */
			console.log("User chose to use an URL.");
		
			$("#file_input")[0].disabled = true;
			formData = new FormData($("#file_download")[0]);
			progressHandler = function(e) {}; /* Empty function, because here we have to receive the progress from the server. */
		}

		$.ajax({
			url : "<%=request.getContextPath()%>/PDFReceiver", //server script to process data
			type : 'POST',
			xhr : function() { // custom xhr
				myXhr = $.ajaxSettings.xhr();
				if (myXhr.upload) { // check if upload property exists
					myXhr.upload.addEventListener('progress',
							progressHandlingFunction, false); // for handling the progress of the upload
				}
				return myXhr;
			},
			//Ajax events
			/*beforeSend : beforeSendHandler,*/
			success : function(data, textStatus, jqXHR) {

				//$("#content").fadeOut();
				console.log("Request was successful :");
				console.log("Response is : " + JSON.stringify(data));
				
				pollServer(data);
				
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("error : " + errorThrown + " -> " + textStatus);
			},
			// Form data
			data : formData,
			dataType : 'json',
			//Options to tell JQuery not to process data or worry about content-type
			cache : false,
			contentType : false,
			processData : false
		});

	}

	function toogleURLInput() {
		$("#url_input")[0].disabled = !$("#url_input")[0].disabled;
	}

	function validateFileUpload() {

		var fileInputField = $("#file_input");
		var htmlElement = fileInputField[0];

		if (htmlElement.files.length > 0) {

			var file = htmlElement.files[0];

			if (file.type != "application/pdf") { /*Invalid MIME type*/
				htmlElement.value = "";
				alert("Nous ne traitons que les fichiers PDF !");
				return;
			}

			toogleURLInput();
		} else {
			toogleURLInput();
		}
	}

	$(document).ready(function() {

		$("progress").attr("value", 0);

		$("#file_input").change(validateFileUpload);

		$("#submit_button").click(submitForms);
	});
</script>
</head>
<body>
	<div id="content">
		<div id="text" align="center">
			<H1>Développement Durable - Analyse automatisée de rapports</H1>
			<div id="presentation" style="width: 50%;">
				Bienvenue sur notre site. <br> Nous avons créés ce logiciel qui
				analyse les termes les plus récurrents de votre rapport. <br>
				Vous pourrez ainsi voir la « distance » entre deux termes. Par
				exemple « femme » et « emploi». <br> Vous saurez la fréquence
				de certains mots de votre rapport : par exemple les principaux gaz
				polluants. Ce logiciel a été réalisé par des étudiants en quatrième
				année de l’<strong>ESIEA</strong>.<br> <br> L’équipe est
				composé de : Nicolas Broquet , Soraya Bouakkaz, Mohamed Idoubella,
				Samir Hamide Michel Messak, Hugo Faye et Emmanuelle Claeys<br>
			</div>
			<br> <br> <br>
			<div id="forms" style="width: 70%;">
				<div id="form_upload" style="float: left;">
					Uploadez un fichier PDF depuis votre PC ici :
					<form id="file_upload" enctype="multipart/form-data">
						<input id="file_input" type="file" name="file" />
					</form>
				</div>
				<div id="form_url" style="float: right;">
					Analyser un document présent sur un serveur distant ici :
					<form id="file_download" enctype="multipart/form-data">
						<input id="url_input" type="text" name="url" size="40"
							maxlength="500" /> <br>
					</form>
				</div>
				<input type="button" id="submit_button" value="Traiter !">
			</div>
			<span id="status"></span>
			<progress></progress>
			<span id="percentage"></span>


		</div>
	</div>
</body>
</html>