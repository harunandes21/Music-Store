module CSCLA1{
    
    requires java.desktop;
	requires junit;
	
	requires jbcrypt;
	
	requires com.google.gson;
	
    exports view;
    exports test to junit;
    opens model to com.google.gson;
   // exports test;
}
