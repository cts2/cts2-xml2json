This directory contains the XML to JSON conversion application, as contained in the OMG CTS2 JSON Rendering RFC.  

Use notes:
1) The Google Web Toolkit plugin must be installed and recompilation needs to include Google/GWT Compile.

Installation notes:
1) This is currently installed at http://informatics.mayo.edu/cts2/services/xmlToJson.  For curious reasons, the actual 
   install on the server is in webapps/xml2Json. Wherever it is installed, you may need to also create an inner directory
   (webapps/xml2Json/xml2Json) to actually run.
2) The war is currently compiled with Java 1.6
3) org/json/XMLToJson.java currently resides in two places:
	1) This project
	2) The svn repository: http://informatics.mayo.edu/svn/trunk/cts2/supp/py4cts2/java/src/org/json/
4) xsl/XMLToJson.xsl currently resides in *three* places:
	1) This project
	2) The svn repository http://informatics.mayo.edu/svn/trunk/cts2/supp/py4cts2/java/src/org/json/
	3) The git repository https://raw.github.com/cts2/cts2-specification/master/Conversions/v1.1xslt/XMLtoJSON
	
This obviously needs to be refactored in the not too distant future.