package mayo.edu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.xml.transform.TransformerException;

import mayo.edu.client.JsonService;
import mayo.edu.shared.XmlJsonResponse;

import org.json.XMLToJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class JsonServiceImpl extends RemoteServiceServlet implements
		JsonService {
	
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JsonParser jp = new JsonParser();

	@Override
	public String getJsonFromXml(String xml) throws IllegalArgumentException, TransformerException {
		return prettify(new XMLToJson(this.getServletContext()).toJson(xml));
	}
	
	private String prettify(String json) {	
		 return gson.toJson(jp.parse(json));
	}

	
	@Override
	public XmlJsonResponse getJsonFromRestService(String restUrl)
			throws IllegalArgumentException, TransformerException {
		
		String xml = makeRestCall(restUrl);
		String json = new XMLToJson(this.getServletContext()).toJson(xml);
		
		XmlJsonResponse response = new XmlJsonResponse();
		response.setJson(json);
		response.setXml(xml);
		
		return response;
	}
	
	/**
	 * Make a REST call to retrieve XML response
	 * @param endpoint
	 * @return
	 */
	private String makeRestCall(String endpoint) {

		HttpURLConnection request = null;
		BufferedReader rd = null;
		StringBuilder response = null;
				
		try {
			
			java.net.URL endpointUrl = new java.net.URL(endpoint);
			request = (java.net.HttpURLConnection) endpointUrl
					.openConnection();
			request.setRequestMethod("GET");

			rd = new java.io.BufferedReader(
					new java.io.InputStreamReader(request.getInputStream()));
			
			request.connect();

			// read the response
			response = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null) {
				response.append(line + '\n');
			}

			// Finally, the connection is released and the response is processed
			request.disconnect();
			rd.close();
			System.out.println((response != null) ? response.toString()
					: "No Response");

		} catch (MalformedURLException e) {
			System.out.println("Exception: " + e.getMessage());
			// e.printStackTrace();
		} catch (ProtocolException e) {
			System.out.println("Exception: " + e.getMessage());
			// e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			// e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			// e.printStackTrace();
		} finally {
			try {
				request.disconnect();
			} catch (Exception e) {
			}

			if (rd != null) {
				try {
					rd.close();
				} catch (IOException ex) {
				}
				rd = null;
			}
		}

		return response.toString();
	}


}
