module CSCLA1{
    
    requires java.desktop;
	requires junit;
    opens view to javafx.fxml;
    exports view;
}
