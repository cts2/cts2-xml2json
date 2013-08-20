package mayo.edu.shared;

import java.io.Serializable;

public class XmlJsonResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String i_json;
	String i_xml;
	
	
	public XmlJsonResponse() {

	}
	
	public void setXml(String xml) {
		i_xml = xml;
	}
	
	public void setJson(String json){
		i_json = json;
	}
	
	public String getJson() {
		return i_json;
	}
	
	public String getXml() {
		return i_xml;
		
	}
}
