package myapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import serverClient.Client;

public class ChatUi extends Application {

    private Client client;
    private TextField textField;
    private ScrollPane scrollPane;
    private String username;
    private VBox chatBox;
    private HBox createMessageBubble(String message,boolean isMe){
        Label label=new Label(message);
        label.setWrapText(true);
        label.setMaxSize(250,250);
        //bubble style
        if(isMe){
            label.setStyle("-fx-background-color: #0084ff; "+
                    "-fx-text-fill:white; "+
                    "-fx-padding: 8; "+
                    "-fx-background-radius: 10");
        }
        else {
            label.setStyle("-fx-background-color: #e4e6eb; "+
                    "-fx-text-fill:blue; "+
                    "-fx-padding: 8; "+
                    "-fx-background-radius: 10");
        }
        HBox box=new HBox(label);
        //control alignment
        if(isMe){
            box.setStyle("-fx-alignment: center-right; ");
        }
        else {
            box.setStyle("-fx-alignment: center-left");
        }
       return box;
    }

    @Override
    public void start(Stage stage) {

        // 🔹 Ask username (popup)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your username:");

        username = dialog.showAndWait().orElse("User");

        // 🔹 Chat chatbox
        chatBox=new VBox(10);
        chatBox.setStyle("-fx-paddind:10; ");
         scrollPane=new ScrollPane(chatBox);
        scrollPane.setFitToHeight(true);

        // 🔹 Input field
        textField = new TextField();
        textField.setPromptText("Type your message...");

        // 🔹 Send button
        Button send = new Button("Send");

        // 🔹 Layout
        HBox inputBox = new HBox(10, textField, send);
        VBox root = new VBox(10, chatBox, inputBox);

        // 🔹 Actions when the send is clicked
        send.setOnAction(e -> sendMessage());
        textField.setOnAction(e -> sendMessage());

        // 🔹 Scene
        stage.setScene(new Scene(root, 500, 600));
        stage.setTitle("Chat App - " + username);
        stage.show();

        //  Connect AFTER UI loads
        connectToServer();
    }

    //  Send message
    private void sendMessage() {
        String message = textField.getText();

        if (!message.isEmpty()) {

            chatBox.getChildren().add(
                    createMessageBubble("[Me: ]"+message,true)
            );
            //auto scroll
            scrollPane.setVvalue(1.0);
            // send to server
            if (client != null) {
                client.sendMessage(message);
            }

            // clear input
            textField.clear();
        }
    }

    //  Connect to server
    private void connectToServer() {

        client = new Client();

        //  receive messages from server
        client.setMessageListener(message -> {
            Platform.runLater(() -> {
                boolean isMe=message.startsWith(username+": ");
                chatBox.getChildren().add(createMessageBubble(message,isMe));
                scrollPane.setVvalue(1.0);
            });
        });

        //  pass username
        new Thread(() -> client.startClient(username)).start();
    }

    public static void main(String[] args) {
        launch();
    }
}