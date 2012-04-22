package nfrancois.prezpush.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class DispatchMessageServlet extends HttpServlet {
	
	
	private static final ChannelService channelService = ChannelServiceFactory.getChannelService();
	private static final String SLIDES_ID_TOKEN = "slidesId";
	private static final String CMD_TOKEN = "cmd";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String slideId = req.getParameter(SLIDES_ID_TOKEN);
		// TODO json
		String cmd = req.getParameter(CMD_TOKEN);
		channelService.sendMessage(new ChannelMessage(slideId, cmd));
	}

}
