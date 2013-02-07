<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Analyse du rapport</title>
<script src="/website-0.0.1-SNAPSHOT/js/jquery-1.9.1.min.js"></script>
<script src="/website-0.0.1-SNAPSHOT/js/jit.js"></script>
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
		keywords = keywords.array;
		
		for(var i in keywords) {
			
			keyword = keywords[i];
			
			selector.append(
					'<input type=\"checkbox\" name=\"keyword\" id=\"keyword\" value=\"'+ keyword +'\">'+ 
						keyword + 
					'<br>'
				);
		}
		
		$("input#keyword").change(
				function(evt) {
					
					console.log($(this).val() + " state changed !");
					
				}
			);
		
		
	}
	
	function loadGraph() {
		  var fd = new $jit.ForceDirected({
			    //id of the visualization container
			    injectInto: 'graph',
			    //Enable zooming and panning
			    //by scrolling and DnD
			    Navigation: {
			      enable: true,
			      //Enable panning events only if we're dragging the empty
			      //canvas (and not a node).
			      panning: 'avoid nodes',
			      zooming: 10 //zoom speed. higher is more sensible
			    },
			    // Change node and edge styles such as
			    // color and width.
			    // These properties are also set per node
			    // with dollar prefixed data-properties in the
			    // JSON structure.
			    Node: {
			      overridable: true
			    },
			    Edge: {
			      overridable: true,
			      color: '#23A4FF',
			      lineWidth: 0.4
			    },
			    //Native canvas text styling
			    /*Label: {
			      type: labelType, //Native or HTML
			      size: 10,
			      style: 'bold'
			    },
			    //Add Tips
			    Tips: {
			      enable: true,
			      onShow: function(tip, node) {
			        //count connections
			        var count = 0;
			        node.eachAdjacency(function() { count++; });
			        //display node info in tooltip
			        tip.innerHTML = "<div class=\"tip-title\">" + node.name + "</div>"
			          + "<div class=\"tip-text\"><b>connections:</b> " + count + "</div>";
			      }
			    },*/
			    // Add node events
			    Events: {
			      enable: true,
			      type: 'Native',
			      //Change cursor style when hovering a node
			      onMouseEnter: function() {
			        fd.canvas.getElement().style.cursor = 'move';
			      },
			      onMouseLeave: function() {
			        fd.canvas.getElement().style.cursor = '';
			      },
			      //Update node positions when dragged
			      onDragMove: function(node, eventInfo, e) {
			          var pos = eventInfo.getPos();
			          node.pos.setc(pos.x, pos.y);
			          fd.plot();
			      },
			      //Implement the same handler for touchscreens
			      onTouchMove: function(node, eventInfo, e) {
			        $jit.util.event.stop(e); //stop default touchmove event
			        this.onDragMove(node, eventInfo, e);
			      },
			      //Add also a click handler to nodes
			      onClick: function(node) {
			        if(!node) return;
			        // Build the right column relations list.
			        // This is done by traversing the clicked node connections.
			        var html = "<h4>" + node.name + "</h4><b> connections:</b><ul><li>",
			            list = [];
			        node.eachAdjacency(function(adj){
			          list.push(adj.nodeTo.name);
			        });
			        //append connections information
			        $jit.id('inner-details').innerHTML = html + list.join("</li><li>") + "</li></ul>";
			      }
			    },
			    //Number of iterations for the FD algorithm
			    iterations: 200,
			    //Edge length
			    levelDistance: 130,
			    // Add text to the labels. This method is only triggered
			    // on label creation and only for DOM labels (not native canvas ones).
			    onCreateLabel: function(domElement, node){
			      domElement.innerHTML = node.name;
			      var style = domElement.style;
			      style.fontSize = "0.8em";
			      style.color = "#ddd";
			    },
			    // Change node styles when DOM labels are placed
			    // or moved.
			    onPlaceLabel: function(domElement, node){
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
			 fd.loadJSON({ adjacencies: []});
			  // compute positions incrementally and animate.
			  fd.computeIncremental({
			    iter: 40,
			    property: 'end',
			    onStep: function(perc){
			      Log.write(perc + '% loaded...');
			    },
			    onComplete: function(){
			      Log.write('done');
			      fd.animate({
			        modes: ['linear'],
			        transition: $jit.Trans.Elastic.easeOut,
			        duration: 2500
			      });
			    }
			  });	
	}
	
	function progress(noob) {
		
		$("#progress").html(noob);
		
	}
	
	$(document).ready(function() {
		loadReport();
		});

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
			<div id="graph"></div>
		</td>
		</tr>
	</table>
</body>
</html>
