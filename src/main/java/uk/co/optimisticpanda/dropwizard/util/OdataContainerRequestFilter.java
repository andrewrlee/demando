package uk.co.optimisticpanda.dropwizard.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

// Based on this: http://stackoverflow.com/questions/9382324/jersey-and-odata-key-path-param-format
// This allows jetty to receive odata style requests like this: www.localhost.com/customer(1) 
// Doesn't provide any other odata operations
public class OdataContainerRequestFilter implements ContainerRequestFilter {

	private static Logger logger = LoggerFactory.getLogger(OdataContainerRequestFilter.class);
	private Pattern uriPattern = Pattern.compile("(.*?)\\((.*?)\\)(.*)");

	public ContainerRequest filter(ContainerRequest request) {
		if (isOdataRequest(request.getRequestUri())) {
			request.setUris(request.getBaseUri(), convertToRestRequest(request.getRequestUri()));
		}
		return request;
	}

	URI convertToRestRequest(URI requestUri) {
		String result = requestUri.toString();
		String query = "";
		if(result.contains("?")){
			query =  result.substring(result.indexOf("?"));
			result = result.substring(0, result.indexOf("?"));
		}
		result = result.replace('(', '/');
		result = result.replace(")", "");
		try {
			return new URI(result + query);
		} catch (URISyntaxException e) {
			logger.warn("Problem converting odata request into rest request!:{} returning original uri!", result);
			return requestUri;
		}
	}

	boolean isOdataRequest(URI uri) {
		Matcher matcher = uriPattern.matcher(uri.toString());
		return matcher.matches();
	}

}
