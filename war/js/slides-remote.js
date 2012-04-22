// Remote

initRemote =  function(_channelId, _slidesId){
	initSocket(_channelId, _slidesId);
	// TODO socket.onopen => enregistrement coté serveur
	socket.onmessage = viewerConnected;
	// TODO socket.onerror => Toast error
	// TODO socket close => dé-enregistrement coté serveur
}

viewerConnected = function(msg){
	var cmd = jQuery.trim(msg.data);
	if(cmd == viewerConnectionToken){
		$().toastmessage('showToast', {
			text     : 'New viewer connected',
			position : 'middle-right',
			type     : 'notice',
		});
	} else if(cmd== viewerDeconnectionToken){
		$().toastmessage('showToast', {
			text     : 'viewer disconnected',
			position : 'middle-right',
			type     : 'notice',
		});		
	}
}

next = function(){
	sendMessage(dispatchMessageServlet, cmd(nextToken))
}

previous = function(){
	sendMessage(dispatchMessageServlet, cmd(previousToken))
}