package uk.co.optimisticpanda.dropwizard.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.dropwizard.util.OdataContainerRequestFilter;

public class OdataContainerRequestFilterTest {

	private OdataContainerRequestFilter filter;

	@Before public void createOdataContainerRequestFilter(){
		this.filter = new OdataContainerRequestFilter();
	}
	
	@Test
	public void isOdataRequest() throws URISyntaxException {
		assertFalse(filter.isOdataRequest(new URI("www.localhost.com/customer/1")));
		assertTrue(filter.isOdataRequest(new URI("www.localhost.com/customer(1)")));
	}
	
	@Test
	public void convertRequestUri() throws URISyntaxException {
		assertEquals("www.localhost.com/customers/1", convert("www.localhost.com/customers/1"));
		assertEquals("www.localhost.com/customers/1", convert("www.localhost.com/customers(1)"));
		assertEquals("www.localhost.com/customers/1/order/1", convert("www.localhost.com/customers(1)/order(1)"));
		assertEquals("www.localhost.com/customers/?$filter=endswith(CompanyName,'Futterkiste')", convert("www.localhost.com/customers/?$filter=endswith(CompanyName,'Futterkiste')"));
	}

	private String convert(String expectedUri){
		try {
			URI uri = filter.convertToRestRequest(new URI(expectedUri));
			return uri.toString();
		} catch (URISyntaxException e) {
			fail(expectedUri + " wasn't a valid uri:" +  e.getMessage());
			throw new RuntimeException();
		}
		
	}
	
}
