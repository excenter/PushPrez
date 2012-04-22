// Viewer


var next_code = 39;
var prev_code = 37;

initViewer = function(_channelId, _slidesId){
	initSocket(_channelId, _slidesId);
	socket.onopen = notifyRemote;
	socket.onmessage = remoteReceive;
	socket.onclose = disconnect;
	// TODO socket.onerror : => Toast error
}

notifyRemote = function(){
	sendMessage(dispatchMessageServlet, cmd(viewerConnectionToken))
}

remoteReceive = function(msg){
	var cmd = jQuery.trim(msg.data);
	if(cmd == nextToken){
	    var key = document.createEvent("KeyEvents");
	    key.initKeyEvent("keydown", true, true, null, false, false, false, false, next_code, next_code);
//		var key = document.createEvent( 'KeyboardEvent' );
//		key.initKeyboardEvent( 'keydown', true, true, null, false, false, false, false, 39, 39 );
	    document.documentElement.dispatchEvent(key);	
	} else if(cmd == previousToken){
	    var key = document.createEvent("KeyEvents");
	    key.initKeyEvent("keydown", true, true, null, false, false, false, false, prev_code, prev_code);
	    document.documentElement.dispatchEvent(key);
	}
}

disconnect = function(){
	sendMessage(dispatchMessageServlet , cmd(viewerDeconnectionToken));
}



jQuery(window).unload(function() {
	jQuery(window).delay(1000, function() {
		socket.close();
	});
});
