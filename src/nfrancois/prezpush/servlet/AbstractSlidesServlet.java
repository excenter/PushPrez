package nfrancois.prezpush.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public abstract class AbstractSlidesServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(AbstractSlidesServlet.class.getCanonicalName());
	
	private static final String PATH_SEPARATOR = "/";
	private static final String REMOTE_TOKEN = "remote";
	private static final String REMOTE_PAGE = "/WEB-INF/jsp/slides-remote.jsp";
	private static final String CHANNEL_ID_PARAM = "channelId";
	private static final String SLIDES_ID_PARAM = "slidesId";	
	private static final String REMOTE_URL_TEMPLATE = "http://%s.appspot.com/%s/%s/remote";
	
	private static final String HEADER_VIEWER_JS = "<script type=\"text/javascript\" src=\"/_ah/channel/jsapi\"></script>\n"+
	"<script type=\"text/javascript\" src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js\"></script>\n"+
	"<script type=\"text/javascript\" src=\"/js/jquery.toastmessage-min.js\"></script>	\n"+
	"<script type=\"text/javascript\" src=\"/js/slides-commons.js\"></script>\n"+
	"<script type=\"text/javascript\" src=\"/js/slides-viewer.js\"></script>\n"+
    "<link type=\"text/css\" href=\"/css/jquery.toastmessage-min.css\" rel=\"stylesheet\"/> \n";

	private static final String BODY_VIEWER_JS = "<script language=\"JavaScript\">\n"+
	 	"	initViewer('CHANNEL_ID','SLIDES_ID');\n"+
	 	"</script>\n";	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getPathInfo();
		// Extract
		String[] urlTokens = url.split(PATH_SEPARATOR);
		String slidesId = urlTokens[1];
		boolean isRemote = url.endsWith(REMOTE_TOKEN);
		// Channel
		String channelId = ChannelServiceFactory.getChannelService().createChannel(slidesId);
		// Response
		req.setAttribute(CHANNEL_ID_PARAM, channelId);
		req.setAttribute(SLIDES_ID_PARAM, slidesId);
		if (isRemote) {
			req.getRequestDispatcher(REMOTE_PAGE).forward(req, resp);
		} else {
			log.info("Viewer de "+slidesId);
			URLFetchService urlFetcher= URLFetchServiceFactory.getURLFetchService();
			HTTPResponse response = urlFetcher.fetch(getURL(slidesId));
			if(response.getResponseCode() == 200){
				// TODO async
				sendUrlRemoteToUser(slidesId);
				String originalHtml = getContent(response);
				// TODO : cache
				String transformedHtml = insertJS(replaceInternalLink(originalHtml), channelId, slidesId);
				resp.getWriter().append(transformedHtml);
			} else {
				// TODO TimeOut ? reload ?
				// TODO gestion erreur 
			}

		}
	}
	
	private String getContent(HTTPResponse response) throws UnsupportedEncodingException{
		return new String(response.getContent(), "UTF-8");
	}
	
	private String insertJS(String html, String channelId, String slidesId){
		html = html.replaceFirst("</script>", "</script>"+HEADER_VIEWER_JS);
		String jsToIncludeInBody = BODY_VIEWER_JS.replaceFirst("CHANNEL_ID", channelId).replaceFirst("SLIDES_ID", slidesId);
		html = html.replaceFirst("</body>", jsToIncludeInBody+"</body>");
		return html;
	}
	
	protected URL getURL(String slidesId) throws MalformedURLException{
		return new URL(String.format(urlTemplate(), slidesId));
	}
	
	private void sendUrlRemoteToUser(String slidesId){
		User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		log.info("User = "+currentUser);
		if(currentUser != null){
			XMPPService xmppService = XMPPServiceFactory.getXMPPService();
			JID jid = new JID(currentUser.getEmail());
			log.info("JID = "+jid);
//			if(xmppService.getPresence(jid).isAvailable()){
				String applicationId = System.getProperty("com.google.appengine.application.id");
				String urlMessage  = String.format(REMOTE_URL_TEMPLATE, applicationId, servletUrl(), slidesId);
				log.info("Message = "+urlMessage);
				 Message msg = new MessageBuilder()
		            .withRecipientJids(jid)
		            .withBody(urlMessage)
		            .build();
				xmppService.sendMessage(msg);
//			} else {
//				log.info("User not dispo");
//			} 		    
		}
	}
	
	
	protected abstract String urlTemplate();

	protected abstract String replaceInternalLink(String html);
	
	protected abstract String servletUrl();

}
