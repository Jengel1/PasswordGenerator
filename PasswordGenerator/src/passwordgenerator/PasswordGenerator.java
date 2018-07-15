package passwordgenerator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Jengel1
 */
public class PasswordGenerator extends Application {
    
    private Stage primaryStage;
    private AnchorPane rootLayout;
    
    /**
    @param args - the command line arguments
    */
    public static void main(String[] args) {
        launch(args);
    }
    
    /*
    Create primary stage and begin program
    @param primaryStage - the primary layout of the program
    */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initPwdGenScrn();
    }

    /*
    Initiate the password generator screen
    */
    private void initPwdGenScrn() {
        try {
            //Load root layout from fxml doc
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PasswordGenerator.class.getResource("PwdGenScrn.fxml"));
            rootLayout = (AnchorPane) loader.load();
            
            //show scene containing root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setTitle("Password Generator");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}