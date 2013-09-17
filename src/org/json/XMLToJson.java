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
package org.json;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Scanner;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class XMLToJson {

	static Transformer transformer = null;
	static String transformerURL = null;
	static final String xsltFile = java.io.File.separator + "xsl" + java.io.File.separator + "XMLToJson.xsl";

	private Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	JsonParser jp = new JsonParser();
	
	public XMLToJson(String basePath) {
		String base = basePath + xsltFile;
		if(transformer == null || (base != transformerURL)) {
			transformerURL = base;
			TransformerFactory tFactory = TransformerFactory.newInstance();
			try {
				transformer = tFactory.newTransformer(new StreamSource(transformerURL));
			} catch (TransformerConfigurationException e) {
				System.err.println("Unable to load transformation: " + transformerURL);
			}
		}
	}


	public String toJson(String xml) {
		StringWriter json = new StringWriter();
		if(transformer==null)
			return "Missing transformer: " + transformerURL;
		try {
			transformer.transform(new StreamSource(new StringReader(xml)),
					      new StreamResult(json));
		} catch (TransformerException e) {
			return "Transformer exception: " + e.getLocalizedMessage();
		}
		return prettify(json.toString());
	}
	

	private String prettify(String json) {
		try {
			return gson.toJson(jp.parse(json));
		} catch (JsonSyntaxException e) {
			return json;
		}
		 
	}


	public static void main(String[] args) throws TransformerException,
			IOException {
		Scanner scanner = new Scanner(new File(args[0]));
		scanner.useDelimiter("\\Z");
		System.out.println(new XMLToJson(null).toJson(scanner.next()).toString());
		scanner.close();
	}
}
