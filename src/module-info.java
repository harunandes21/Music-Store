module CSCLA1{
    
    requires java.desktop;
	requires junit;
	requires jbcrypt;
	
	requires com.google.gson;
	
    exports view;
    opens model to com.google.gson;
   // exports test;
}
