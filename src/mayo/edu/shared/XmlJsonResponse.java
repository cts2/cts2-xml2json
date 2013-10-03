package mayo.edu.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XmlJsonResponse implements IsSerializable {

	private static final long serialVersionUID = 2L;
	
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
