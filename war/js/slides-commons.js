// ConsviewerConnectionTokentantes
var nextToken = 'next';
var previousToken = 'previous';
var cmdParam = 'cmd';
var viewerConnectionToken = 'viewerConnection';
var viewerDeconnectionToken = 'viewerDeconnection'
var noViewerConnectedToken = 'noViewerConnected';
var remotedConnectionToken = 'remoteConnection';
var remoteDeconnectionToken = 'remoteDeconnection';
var noRemotedConnectedToken = 'noRemoteConnected';
var dispatchMessageServlet = '/dispatchmessage';
// Variables
var channelId;
var slidesId;
var socket;
var channel;

// Generique

cmd = function(command){
	return cmdParam+'='+command;
}

initSocket = function(_channelId, _slidesId){
	channelId = _channelId;
	slidesId = _slidesId;
	channel = new goog.appengine.Channel(channelId);
	socket = channel.open();	
}

sendMessage = function(path, opt_param) {
	// TODO JQuery
	path += '?slidesId=' + slidesId;
	if (opt_param) {
		path += '&'+ opt_param;
	}
	var xhr = new XMLHttpRequest();
	xhr.open('POST', path, true);
	xhr.send();
};
