/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mayo.edu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import mayo.edu.client.JsonService;
import mayo.edu.shared.XmlJsonResponse;

import org.json.XMLToJson;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class JsonServiceImpl extends RemoteServiceServlet implements
		JsonService {
	
	public String getBasePath() {
        String dataPath;

        HttpSession httpSession = getThreadLocalRequest().getSession(true);
        ServletContext context = httpSession.getServletContext();

        String realContextPath = context.getRealPath(getThreadLocalRequest().getContextPath());

        if (isDevelopmentMode()) {
            dataPath = realContextPath;
        } else {
            dataPath = realContextPath + "/../";
        }

        return dataPath;
    }
	
	/**
     * Determine if the app is in development mode. To do this get the request
     * URL and if it contains 127.0.0.1, then it is in development mode.
     * 
     * @return
     */
    private boolean isDevelopmentMode() {
        return getThreadLocalRequest().getHeader("Referer").contains("127.0.0.1");
    }


	@Override
	public String getJsonFromXml(String xml) throws IllegalArgumentException {
		return new XMLToJson(getBasePath()).toJson(xml);
	}

	
	@Override
	public XmlJsonResponse getJsonFromRestService(String restUrl)
			throws IllegalArgumentException {
		
		String xml = makeRestCall(restUrl);
		String json = new XMLToJson(getBasePath()).toJson(xml);
		
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
