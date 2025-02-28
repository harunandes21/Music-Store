module CSCLA1{
    
    requires java.desktop;
    opens view to javafx.fxml;
    exports view;
}
