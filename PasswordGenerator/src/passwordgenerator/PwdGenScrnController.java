/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordgenerator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jengel1
 */
public class PwdGenScrnController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    //reference to this stage
    private Stage primaryStage;
    
    //list of character options for password
    private ArrayList<String> listOfCharOptions = new ArrayList<>(Arrays.asList(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz",
            "!@#$%^&*?<>",
            "0123456789"
    ));
    //list of integers representing the indexes of listOfCharOptions to filter character options
    private static ArrayList<Integer> charIndexes = new ArrayList<>();
    //list of integers representing the indexes of listOfCharOptions of character sets used
    private static ArrayList<Integer> indexesUsed = new ArrayList<>();
    //list of integers representing the indexes of listOfCharOptions of character sets not yet used in current password
    private static ArrayList<Integer> indexesNotUsed = charIndexes;
    
    
    /*
    References to FXML objects
    */
    @FXML
    private TextField pwdLenTF;
    @FXML
    private TextField pwdResultTF;
    
    @FXML
    private CheckBox upperCharCB;
    @FXML
    private CheckBox lowerCharCB;
    @FXML
    private CheckBox specialCharCB;
    @FXML
    private CheckBox numberCharCB;
    
    @FXML
    private Button pwdGenBtn;
    @FXML
    private Button pwdCopyBtn;
    @FXML
    private Button exitBtn;
    
    @FXML
    private Label newPwdLb;
    
    /*
    Btn handler to populate the password result text field with newly generated password 
    @param event - generate password btn clicked
    */
    @FXML
    public void handleGenPwdBtn(ActionEvent event){
        if(isInputValid()){
            //clear all existing data
            pwdResultTF.clear();
            charIndexes.clear();
            indexesUsed.clear();
            indexesNotUsed.clear();

            //generate new password
            StringBuilder password = new StringBuilder();
            filterCharSets();
            int counter = 0;
            int pwdLength = Integer.parseInt(pwdLenTF.getText());
            while(counter < pwdLength){
                password.append(getRandomChar(getCharSet()));  //append first character to password string builder
                counter++;
                validatePassword();
                //force use of character sets not yet used
                if((indexesUsed.size() + indexesNotUsed.size()) == pwdLength){
                    charIndexes = indexesNotUsed;
                }
            }
            pwdResultTF.setText(password.toString());
        }
    }
    
    /*
    Method to test which checkboxes are selected
    Add indexes of selected checkbox character sets to the charIndexes list
    */
    private void filterCharSets(){
        if(upperCharCB.isSelected()){
            charIndexes.add(0);  //index of uppercase characters
        }
        if(lowerCharCB.isSelected()){
            charIndexes.add(1);  //index of lowercase characters
        }
        if(specialCharCB.isSelected()){
            charIndexes.add(2);  //index of special characters
        }
        if(numberCharCB.isSelected()){
            charIndexes.add(3);  //index of numbers
        }
    }
    
    /*
    Method to select which set of characters to use
    return charSet - the index of the character set in listOfCharOptions
    */
    private int getCharSet(){
        int charSet = 0;
        if(charIndexes.size() == 1){  //only a single character set selected
            charSet = charIndexes.get(0);  //use that character set
        } else {  //multiple character sets selected
            for(int i = 0; i < charIndexes.size(); i++){
                //select random character set index from list of charIndexes
                int selectedIndex = (int) Math.round(Math.random() * charIndexes.size());
                if(selectedIndex < charIndexes.size()){
                    charSet = charIndexes.get(selectedIndex);
                }
            }
            indexesUsed.add(charSet);  //add charSet to list of indexesUsed for use in password validation
        }
        return charSet;
    }
    
    /*
    Method to select a random character from the character set
    @param index - the index of the character set in listOfCharOptions
    return result - random character to be appended to password
    */
    private String getRandomChar(int index){
        String result = "";
        String charSet = listOfCharOptions.get(index);  //set of characters
        for(int i = 0; i < charSet.length(); i++){
            //select random character from the selected set of characters
            int position = (int) Math.round(Math.random() * charSet.length() - 1);
            if(position >= 0 && position < 26){
                result = charSet.substring(position, position + 1);
            }
        }
        return result;
    }
    
    /*
    Method to ensure that atleast one character from each selected character set is used in password
    Compares values in indexesUsed to values in indexesNotUsed 
    Removes any matching values from indexesNotUsed
    */
    private void validatePassword(){ 
        for(int i = 0; i < indexesUsed.size(); i++){
            if(indexesNotUsed.contains(indexesUsed.get(i))){
                indexesNotUsed.remove(indexesUsed.get(i));
            }
        }
    }
    
    /*
    Method to validate user input and manage error handling
    return boolean value - true if input is valid, else false
    */
    private boolean isInputValid(){
        int currentPassword = 0;
        String errorMessage = "";
        
        //ensure text entered into password length text field is an integer
        try {
            currentPassword = Integer.parseInt(pwdLenTF.getText());
            //ensure password length is not left blank
            if(pwdLenTF.getText() == null || pwdLenTF.getText().length() == 0){
                errorMessage += "Please enter a valid password length between 1 - 16!\n";
            }
            //ensure password length is between 1 - 16
            if(!(currentPassword > 0 && currentPassword < 17)){
                errorMessage += "Please enter a valid password length between 1 - 16!\n";
            }
        }
        catch(NumberFormatException e){
            errorMessage += "Please enter a valid password length between 1 - 16!\n";
        }
        
        //ensure atleast one checkbox is selected
        if(!upperCharCB.isSelected() 
                && !lowerCharCB.isSelected() 
                && !specialCharCB.isSelected() 
                && !numberCharCB.isSelected()){
            errorMessage += "You must select atleast one type of character!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Invalid Selections");
            alert.setHeaderText("Please correct invalid selections");
            alert.setContentText(errorMessage);

            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    System.out.println("Alerting!");
                }
            }));
            return false;
        }  //end else
    }
    
    /*
    Btn handler to copy the password result to system clipboard
    @param event - copy btn clicked
    */
    @FXML
    public void handleCopyBtn(ActionEvent event){
        //copy password to system clipboard
        StringSelection stringSelection = new StringSelection (pwdResultTF.getText());
        Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        clpbrd.setContents (stringSelection, null);
        
        //alert user that password has been copied to system clipboard
        newPwdLb.setText("New Password -- (Copied to Clipboard!)");

        //reset label to original text after 2 seconds
        Timer t = new Timer();
        t.schedule(new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(()->{
                    newPwdLb.setText("New Password");
                    t.cancel();
                });
            }
        }, 2000);
    }
    
    /*
    Btn handler to exit application
    @param event - exit btn clicked
    */
    @FXML
    public void handleExitBtn(ActionEvent event) {
        try{
            primaryStage = (Stage) pwdResultTF.getScene().getWindow();  //reference to LoginScrn.fxml using usernameField
            primaryStage.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }  
    
}