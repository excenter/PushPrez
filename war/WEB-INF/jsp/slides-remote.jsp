<html>
<head>
	<title>Slides remote</title>
    <link type="text/css" href="/css/jquery.toastmessage-min.css" rel="stylesheet"/> 
	<script type="text/javascript" src="/_ah/channel/jsapi"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript" src="/js/jquery.toastmessage-min.js"></script>	
	<script type="text/javascript" src="/js/slides-commons.js"></script>
	<script type="text/javascript" src="/js/slides-remote.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#next").click(next);
			$("#prev").click(previous);
		});	
	</script>
</head>
<body>
	<img alt="next" id="next" src="/images/next.png">
	<br/>
	<img alt="prev" id="prev" src="/images/prev.png">
	<script language="JavaScript">
		initRemote('<%=request.getAttribute("channelId")%>', '<%=request.getAttribute("slidesId")%>');
	</script>
<body>
</html>