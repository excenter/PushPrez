package nfrancois.prezpush.servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;


@SuppressWarnings("serial")
public class GoogleSlidesV1Servlet extends AbstractSlidesServlet {
	
	private static final String GOOGLE_PREZ_URL_TEMPLATE = "https://docs.google.com/present/view?id=%s";

	@Override
	protected String urlTemplate() {
		return GOOGLE_PREZ_URL_TEMPLATE;
	}

	@Override
	protected String replaceInternalLink(String html) {
		return html.replaceAll("src=\"/", "src=\"http://docs.google.com/").replaceAll("href=\"/", "href=\"http://docs.google.com/");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getPathInfo();
		GDocsRequestType gDocRequestType = GDocsRequestType.findByUrl(url);
		if(gDocRequestType != null){
			HTTPResponse gDocResponse = postRequest(gDocRequestType, req);
			resp.setStatus(gDocResponse.getResponseCode());
			// TODO headers
			if(gDocResponse.getContent() != null){
				resp.getOutputStream().write(gDocResponse.getContent());
			}
		}
	}	
	
	@SuppressWarnings("unchecked")
	private HTTPResponse postRequest(GDocsRequestType type, HttpServletRequest request) throws IOException{
		HTTPRequest componentRequest = new HTTPRequest(type.getUrl(), HTTPMethod.POST);
		StringBuilder payloadBuidler = new StringBuilder();
		Map<String, Object> parameterMap = request.getParameterMap();
		for (Entry<String, Object> entry : parameterMap.entrySet()) {
			payloadBuidler.append(entry.getKey()).append("=").append(request.getParameter(entry.getKey())).append("&");
		}
		// TODO headers
		componentRequest.setPayload(payloadBuidler.toString().getBytes());
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		return fetcher.fetch(componentRequest);	
	}
	
	private static enum GDocsRequestType {
		
		COMPONENTS("components"),
		PERF("perf"),
		TOUCH("touch");
		

		private final String token;

		private GDocsRequestType(String token){
			this.token = token;
		}
		
		public URL getUrl() throws MalformedURLException{
			return new URL("https://docs.google.com/present/"+token);
		}
		
		public static GDocsRequestType findByUrl(String url){
			for(GDocsRequestType type : values()){
				if(url.endsWith(type.token)){
					return type;
				}
			}
			return null;
		}
		
	}

	@Override
	protected String servletUrl() {
		return "googlev1";
	}	

}
