/**
 * 
 */

function RemoteServlet(addr) {
	
	this.address = addr;
}

RemoteServlet.prototype.newRequest = function(type, data) {
	
	var payload = {
			requestType: type,
			data: payload
	};
	
	return $.ajax(this.address, {
			data: JSON.stringify(payload),
			dataType: 'script',
			type: 'POST'
		}
	);
};

function PDFDocumentAnalyzer(filename) {
	this.servlet = new RemoteServlet('/website-0.0.1-SNAPSHOT/PDFDocumentAnalyzer');
	this.filename = filename;
}

PDFDocumentAnalyzer.prototype.


var graphProvider = new RemoteServlet('/website-0.0.1-SNAPSHOT/GraphProvider');


function updateProgress() {
	
	
	
}

function launchReportAnalysis(filename) {
	
	graphProvider.callFunction('launchReportAnalysis', {
		file: filename,
		progressFunction: 'updateProgress'
	});
	
}