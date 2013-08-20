package mayo.edu.client;

import mayo.edu.shared.XmlJsonResponse;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("cts2")
public interface JsonService extends RemoteService {

	String getJsonFromXml (String json) throws IllegalArgumentException;
	
	XmlJsonResponse getJsonFromRestService (String json) throws IllegalArgumentException;
}