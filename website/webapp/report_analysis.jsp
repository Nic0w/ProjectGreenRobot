<%@page import="fr.esiea.sd.greenrobot.pdf_analysis.graph.KeywordsGraphBuilder"%>
<%@page import="fr.esiea.sd.greenrobot.website.PDFAnalysisTask"%>
<%@page import="fr.esiea.sd.greenrobot.pdf_analysis.graph.Keyword"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Analyse du rapport</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/base.css"
	media="screen" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/report_analysis.css"
	media="screen" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jit.js"></script>
<script type="text/javascript">

	var servlet = "/website-0.0.1-SNAPSHOT/GraphProvider";
	var filename="<%=request.getParameter("file")%>";

	var _hash_, json =[{
		"id" : "aUniqueIdentifier",
		"name" : ".",
		"data" : {
			"$color" : "#83548B",
			"$type" : "circle",
			"$dim" : 10
		},
		"adjacencies" : [ ]
	}];
	
	var selectedKeywords = [];

	function getProgress(hash) {

		console.log("getProgress => " + hash);
		$("#progress").load(servlet, 'hash=' + hash);
	}

	function loadReport() {

		$.get(servlet, {
			'file' : filename
		}, function(data) {

			console.log("Received hash : " + data);
			_hash_ = data;
			getProgress(data);
		});
	}

	function loadKeywordSelector(keywords) {

		var selector = $("#keyword_selector");
		var keyword;
		keywords = keywords.array;

		for ( var i in keywords) {

			keyword = keywords[i];

			selector
					.append('<input type=\"checkbox\" name=\"keyword\" id=\"keyword\" value=\"'+ keyword +'\">'
							+ keyword + '<br>');
		}

		$("input#keyword").change(function(evt) {

			console.log($(this).val() + " state changed !");
			
			var selected = [];
			var inputs = $("input#keyword").get();
			
			for(var i in inputs)
				if(inputs[i].checked)
					selected.push(inputs[i].value);
			
			console.log(selected);
			
			$.post(servlet, JSON.stringify({ hash: _hash_, selected: selected }), function(data) {
				
				json = JSON.parse(data);
				loadGraph();
			});
			
		});

	}

	function loadGraph() {
		var fd = new $jit.ForceDirected({
			//id of the visualization container
			injectInto : 'infovis',
			//Enable zooming and panning
			//by scrolling and DnD
			Navigation : {
				enable : true,
				//Enable panning events only if we're dragging the empty
				//canvas (and not a node).
				panning : 'avoid nodes',
				zooming : 10
			//zoom speed. higher is more sensible
			},
			// Change node and edge styles such as
			// color and width.
			// These properties are also set per node
			// with dollar prefixed data-properties in the
			// JSON structure.
			Node : {
				overridable : true
			},
			Edge : {
				overridable : true,
				color : '#23A4FF',
				lineWidth : 0.4
			},
			//Native canvas text styling
			Label : {
				type : 'HTML', //Native or HTML
				size : 10,
				style : 'bold'
			},
			//Add Tips
			Tips : {
				enable : true,
				onShow : function(tip, node) {
					//count connections
					var count = 0;
					node.eachAdjacency(function() {
						count++;
					});
					//display node info in tooltip
					tip.innerHTML = "<div class=\"tip-title\">" + node.name
							+ "</div>"
							+ "<div class=\"tip-text\"><b>connections:</b> "
							+ count + "</div>";
				}
			},
			// Add node events
			Events : {
				enable : true,
				type : 'Native',
				//Change cursor style when hovering a node
				onMouseEnter : function() {
					fd.canvas.getElement().style.cursor = 'move';
				},
				onMouseLeave : function() {
					fd.canvas.getElement().style.cursor = '';
				},
				//Update node positions when dragged
				onDragMove : function(node, eventInfo, e) {
					var pos = eventInfo.getPos();
					node.pos.setc(pos.x, pos.y);
					fd.plot();
				},
				//Implement the same handler for touchscreens
				onTouchMove : function(node, eventInfo, e) {
					$jit.util.event.stop(e); //stop default touchmove event
					this.onDragMove(node, eventInfo, e);
				},
				//Add also a click handler to nodes
				onClick : function(node) {
					if (!node)
						return;
					// Build the right column relations list.
					// This is done by traversing the clicked node connections.
					var html = "<h4>" + node.name
							+ "</h4><b> connections:</b><ul><li>", list = [];
					node.eachAdjacency(function(adj) {
						list.push(adj.nodeTo.name);
					});
					//append connections information
					$jit.id('inner-details').innerHTML = html
							+ list.join("</li><li>") + "</li></ul>";
				}
			},
			//Number of iterations for the FD algorithm
			iterations : 200,
			//Edge length
			levelDistance : 130,
			// Add text to the labels. This method is only triggered
			// on label creation and only for DOM labels (not native canvas ones).
			onCreateLabel : function(domElement, node) {
				domElement.innerHTML = node.name;
				var style = domElement.style;
				style.fontSize = "0.8em";
				style.color = "#ddd";
			},
			// Change node styles when DOM labels are placed
			// or moved.
			onPlaceLabel : function(domElement, node) {
				var style = domElement.style;
				var left = parseInt(style.left);
				var top = parseInt(style.top);
				var w = domElement.offsetWidth;
				style.left = (left - w / 2) + 'px';
				style.top = (top + 10) + 'px';
				style.display = '';
			}
		});
		// load JSON data.
		fd.loadJSON(/*[{
			"id" : "aUniqueIdentifier",
			"name" : ".",
			"data" : {
				"$color" : "#83548B",
				"$type" : "circle",
				"$dim" : 10
			},
			"adjacencies" : [ "anOtherNode" ]
		}, {
			"id" : "anOtherNode",
			"name" : "usually a nodes name",
			"data" : {
				"$color" : "#83548B",
				"$type" : "circle",
				"$dim" : 10
			},
			"adjacencies" : ["aUniqueIdentifier"]
		}]*/ json);
		// compute positions incrementally and animate.
		fd.computeIncremental({
			iter : 40,
			property : 'end',
			onStep : function(perc) {
				console.log(perc + '% loaded...');
			},
			onComplete : function() {
				console.log('done');
				fd.animate({
					modes : [ 'linear' ],
					transition : $jit.Trans.Elastic.easeOut,
					duration : 2500
				});
			}
		});
	}

	function progress(noob) {

		$("#progress").html(noob);

	}

	$(document).ready(function() {
		
		$("input[type=\"checkbox\"]").change(function (event) {
			
			$(this).parent().toggleClass("selected");
			
		});
		
		$("#container").fadeIn();
		
		/* loadKeywords(); */
		
		/* loadReport(); */
	});
</script>
</head>
<body>


	<div id="container" style="">

		<div id="left-container">
			Choisissez des mot-clés :
			
			<div id="keyword_selector"
				style="position: absolute; overflow: auto; height: 90%; width: 200px;">
			
				<%
					Object attr = session.getAttribute("task");
				
					if(attr != null) {
						if(attr instanceof PDFAnalysisTask) {
							PDFAnalysisTask task = (PDFAnalysisTask) attr;
	
							KeywordsGraphBuilder graphBuilder = task.getGraphBuilder();
							
							for(Keyword k : graphBuilder.getAllKeywords()) {
								
								String w = k.getWord();
								
								out.println("<div class=\"keyword\"><label for=\""+ w +"\">"+ w +"</label>");
								out.println("<input type=\"checkbox\" class=\"keyword_select\" id=\""+ w +"\"></div>");
								//out.println("<br>");
								
							}
						}
					}
				
				%>	
				
			</div>
		</div>


		<div id="center-container">
			<div id="progress"></div>
			<div id="infovis"></div>
		</div>
		<div id="right-container">

			<div id="inner-details"></div>

		</div>

		<div id="log"></div>
	</div>
</body>
</html>
