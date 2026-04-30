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
    private TextArea chatArea;
    private TextField textField;

    private String username;

    @Override
    public void start(Stage stage) {

        // 🔹 Ask username (popup)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your username:");

        username = dialog.showAndWait().orElse("User");

        // 🔹 Chat area
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        // 🔹 Input field
        textField = new TextField();
        textField.setPromptText("Type your message...");

        // 🔹 Send button
        Button send = new Button("Send");

        // 🔹 Layout
        HBox inputBox = new HBox(10, textField, send);
        VBox root = new VBox(10, chatArea, inputBox);

        // 🔹 Actions
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

            // show locally
            chatArea.appendText("[Me]: " + message + "\n");

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
                chatArea.appendText(message + "\n");
            });
        });

        //  pass username
        new Thread(() -> client.startClient(username)).start();
    }

    public static void main(String[] args) {
        launch();
    }
}