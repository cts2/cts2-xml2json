package mayo.edu.client;

import mayo.edu.shared.XmlJsonResponse;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class XmlToJson implements EntryPoint {

	private static final String INFO = "This page will take XML and convert it to JSON using the proposed <a href='http://informatics.mayo.edu/svn/trunk/cts2/spec/submission/RFC_JSON/RFC_Submission/2013-09-01.pdf '>OMG XML to JSON Transformation Rules</a> </br>" +
	                                   "<p>" + 
	                                   "The XSLT transformation used by this application can be found <a href='https://github.com/cts2/cts2-xml2json/blob/master/war/xsl/XMLToJson.xsl'>HERE</a><br/>" +
	                                   "and the project for this application is on <a href='https://github.com/cts2/cts2-xml2json'>GitHub</a><br/><p>" +
	                                   "<ul> " + 
	                                   "<li>Enter a REST service that returns XML and this will display the XML and converted JSON.</li>" +
	                                   "<li>Enter some XML and it will be converted JSON</li>" +
	                                   "</ul>";
	
	private static final String NO_REST_URL_MSG = "Please enter a URL.";
	private static final String NO_XML_MSG = "Please enter some XML.";
	
	private VLayout i_overallLayout;
	private RichTextEditor i_inputText;
	private RichTextEditor i_outputText;
	private TextItem i_restTextItem; 
	private Button i_submitRest;
	private Button i_submit;
	
	private ModalWindow i_busyIndicator;
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final JsonServiceAsync rpcService = GWT
			.create(JsonService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		 		
		// initialize the main layout container
        i_overallLayout = new VLayout();
        i_overallLayout.setWidth100();
        i_overallLayout.setHeight100();
        i_overallLayout.setMargin(5);
        i_overallLayout.setMembersMargin(15);
        i_overallLayout.setBackgroundColor("#88a0a6");
       
        HLayout windowLayout = new HLayout();
        
        VLayout inputWindow = createWindow(true, "XML (Input)");
        VLayout outputWindow = createWindow(false, "JSON (Output)");
        
        windowLayout.addMember(inputWindow);
        windowLayout.addMember(outputWindow);
                
        i_overallLayout.addMember(getInfoLabel());
        i_overallLayout.addMember(getRestEndpointLayout());
        i_overallLayout.addMember(windowLayout);
        i_overallLayout.addMember(getButtonLayout());
        
		// Draw the Layout - main layout
        RootLayoutPanel.get().add(i_overallLayout);

	}

	private VLayout getInfoLabel() {
		
		VLayout infoLayout = new VLayout();
		infoLayout.setWidth100();
		infoLayout.setHeight(160);
		infoLayout.setBackgroundColor("white");
		
        infoLayout.addMember(createInfoPanel());
        return infoLayout;
	}

	private HLayout getWindowLabel(String title){
		Label label = new Label();  
        label.setHeight(20);
        label.setWidth100();
        label.setPadding(2);  
        label.setBackgroundColor("#E1E1E1");
        label.setAlign(Alignment.LEFT);  
        label.setValign(VerticalAlignment.CENTER);  
        label.setWrap(false);    
        label.setShowEdges(true);  
        label.setContents("<b>" + title + "</b>");  
        
        HLayout labelLayout = new HLayout();
        labelLayout.setWidth100();
        labelLayout.setHeight(30);
        labelLayout.addMember(label);
        
        return labelLayout;
	}
	
	/**
	 * Layout for entering a REST URL to retrieve the JSON format
	 * @return
	 */
	private HLayout getRestEndpointLayout() {
		
		final DynamicForm form = new DynamicForm();  
		form.setWidth(500);
		form.setHeight(20);
		form.setMargin(0);
		form.setAlign(Alignment.LEFT);
		
		i_restTextItem = new TextItem();  
		i_restTextItem.setAlign(Alignment.LEFT);
		i_restTextItem.setWidth(400);
		i_restTextItem.setTitle("REST URL");  
        
        form.setItems(i_restTextItem);
		
        i_submitRest = new Button();
        i_submitRest.setTitle("Get XML and Generate JSON");
        i_submitRest.setWidth(200);
		i_submitRest.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if (i_restTextItem.getDisplayValue().trim().length() == 0){
					SC.warn(NO_REST_URL_MSG);
				}

				else {
					// Need to send in the overall layout so the whole
	                // screen is greyed out.
	                i_busyIndicator = new ModalWindow(i_overallLayout, 40, "#dedede");
	                i_busyIndicator.setLoadingIcon("loading_circle.gif");
	                i_busyIndicator.show("Getting XML and JSON", true);
					
					rpcService.getJsonFromRestService(i_restTextItem.getDisplayValue(), new AsyncCallback<XmlJsonResponse>() {
						
						@Override
						public void onSuccess(XmlJsonResponse xmlJsonResponse) {
							i_inputText.setValue(xmlJsonResponse.getXml());
							i_outputText.setValue(xmlJsonResponse.getJson());
							
							// hide the busy indicator.
	                        i_busyIndicator.hide();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							i_outputText.setValue("Error retrieving JSON: " + caught.toString());
							
							// hide the busy indicator.
	                        i_busyIndicator.hide();
						}
					});
				}
			}
		});
		
		VLayout buttonLayout = new VLayout();
		buttonLayout.setWidth100();
		buttonLayout.setHeight100();
		buttonLayout.setAlign(VerticalAlignment.CENTER);
		buttonLayout.addMember(i_submitRest);
				
		VLayout formLayout = new VLayout();
		formLayout.setWidth(500);
		formLayout.setHeight100();
		formLayout.setAlign(VerticalAlignment.CENTER);
		formLayout.addMember(form);
        
        HLayout restLayout = new HLayout();  
		restLayout.setWidth100();  
		restLayout.setHeight(50);  
		restLayout.setMembersMargin(5);
		restLayout.addMember(formLayout);
		restLayout.addMember(buttonLayout);
		
		return restLayout;
		
	}

	private VLayout createWindow(boolean isInput, String title) {
		
		RichTextEditor richTextEditor = new RichTextEditor();  
        richTextEditor.setHeight100(); 
        richTextEditor.setWidth100();
        richTextEditor.setOverflow(Overflow.HIDDEN);  
        richTextEditor.setCanDragResize(true);  
        richTextEditor.setShowEdges(true);  
        
        
        // hide all of the rich text controls
        richTextEditor.setControlGroups(new String[]{"fontControls"});
		
		if (isInput){
			i_inputText = richTextEditor;
		}
		else {
			i_outputText = richTextEditor;
		}
		richTextEditor.setValue("");

		VLayout textLayout = new VLayout();  
		textLayout.setWidth100();  
		textLayout.setHeight100();  
		textLayout.setMargin(5);
		textLayout.addMember(getWindowLabel(title));
		textLayout.addMember(richTextEditor);
		
        return textLayout;
		
	}
	
	private HLayout getButtonLayout() {
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setWidth100();
		buttonLayout.setHeight(30);
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setMembersMargin(20);
		
		Button clearButton = new Button();
		clearButton.setTitle("Clear");
		
		clearButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				i_outputText.setValue("");
				i_inputText.setValue("");
				
			}
		});
		
		i_submit = new Button();
		i_submit.setTitle("Generate JSON");
		
		i_submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				System.out.println("input = *" + i_inputText.getValue() + "*");
				
				if (i_inputText.getValue().trim().length() < 5){
					SC.warn(NO_XML_MSG);
				}

				else {
					// Need to send in the overall layout so the whole
	                // screen is greyed out.
	                i_busyIndicator = new ModalWindow(i_overallLayout, 40, "#dedede");
	                i_busyIndicator.setLoadingIcon("loading_circle.gif");
	                i_busyIndicator.show("Generating JSON", true);
					
					rpcService.getJsonFromXml(i_inputText.getValue(), new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String jsonString) {
							i_outputText.setValue(jsonString);
							
							// hide the busy indicator.
	                        i_busyIndicator.hide();
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							i_outputText.setValue("Error retrieving JSON: " + caught.toString());
							
							// hide the busy indicator.
	                        i_busyIndicator.hide();
						}
					});
				}
			}
		});
		
		buttonLayout.addMember(clearButton);
		buttonLayout.addMember(i_submit);
		
		return buttonLayout;
	}
	
	/**
     * HTMLPane to display data/links.
     * 
     * @return
     */
    private HTMLPane createInfoPanel() {
        HTMLPane htmlPane = new HTMLPane();
        htmlPane.setWidth100();
        htmlPane.setHeight100();
        //htmlPane.setMargin(10);
        //htmlPane.setShowEdges(true); 
        //htmlPane.setPadding(10);
        htmlPane.setBackgroundColor("#eeeeee");

        String contextPath = GWT.getHostPageBaseURL();
        System.out.println("ContextPath: " + contextPath);
        
        try {
        
	        String fileLocation = contextPath + "data/frontPageData.html";
	        htmlPane.setContentsURL(fileLocation);
        }
        catch (Exception e) {
			SC.warn("Couldn't find info page.");
		}

        return htmlPane;
    }
		
}
