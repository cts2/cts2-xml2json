package mayo.edu.client;

import mayo.edu.shared.XmlJsonResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface JsonServiceAsync {

	void getJsonFromXml(String json, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getJsonFromRestService(String restUrl, AsyncCallback<XmlJsonResponse> callback)
			throws IllegalArgumentException;
	
	
	
}
