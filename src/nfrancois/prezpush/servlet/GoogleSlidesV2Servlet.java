package nfrancois.prezpush.servlet;


@SuppressWarnings("serial")
public class GoogleSlidesV2Servlet extends AbstractSlidesServlet {
	
	private static final String GOOGLE_PREZ_URL_TEMPLATE = "https://docs.google.com/presentation/d/%s/present#slide=id.p";

	@Override
	protected String urlTemplate() {
		return GOOGLE_PREZ_URL_TEMPLATE;
	}

	@Override
	protected String replaceInternalLink(String html) {
		return html.replaceAll("src=\"/", "src=\"http://docs.google.com/").replaceAll("href=\"/", "href=\"http://docs.google.com/");
	}
	

}
