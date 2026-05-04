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
    private ComboBox<String> userSelector;

    private HBox createMessageBubble(String message, boolean isMe) {

        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(250);

        if (isMe) {
            label.setStyle("-fx-background-color:#0084ff; -fx-text-fill:white; -fx-padding:8; -fx-background-radius:10;");
        } else {
            label.setStyle("-fx-background-color:#e4e6eb; -fx-text-fill:black; -fx-padding:8; -fx-background-radius:10;");
        }

        HBox box = new HBox(label);

        if (isMe) {
            box.setStyle("-fx-alignment: center-right;");
        } else {
            box.setStyle("-fx-alignment: center-left;");
        }

        return box;
    }

    @Override
    public void start(Stage stage) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter username:");
        username = dialog.showAndWait().orElse("User");

        userSelector = new ComboBox<>();
        userSelector.setPromptText("Select User");
//        userSelector.getItems().addAll("A", "B", "C");

        chatBox = new VBox(10);
        chatBox.setStyle("-fx-padding:10;");

        scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);

        textField = new TextField();
        textField.setPromptText("Type message...");

        Button send = new Button("Send");

        HBox inputBox = new HBox(10,userSelector, textField, send);
        VBox root = new VBox(10, scrollPane, inputBox);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        send.setOnAction(e -> sendMessage());
        textField.setOnAction(e -> sendMessage());

        stage.setScene(new Scene(root, 500, 600));
        stage.setTitle("Chat - " + username);
        stage.show();

        connectToServer();
    }

    private void sendMessage() {
        String message = textField.getText();

        if (!message.isEmpty()) {

            // show locally
            chatBox.getChildren().add(
                    createMessageBubble(username + ": " + message, true)
            );

            scrollPane.setVvalue(1.0);

            String target = userSelector.getValue();

            if (target != null) {
                client.sendPrivateMessage(target, message);
            } else {
                client.sendMessage(message); // fallback to public
            }

            textField.clear();
        }
    }

    private void connectToServer() {

        client = new Client();

        client.setMessageListener(message -> {
            Platform.runLater(() -> {

                //  HANDLE USER LIST
                if (message.startsWith("USERLIST:")) {

                    String users = message.substring(9);
                    String[] userArray = users.split(",");

                    userSelector.getItems().clear();

                    for (String user : userArray) {
                        if (!user.equals(username) && !user.isEmpty()) {
                            userSelector.getItems().add(user);
                        }
                    }

                    return;
                }

                boolean isMe = message.startsWith(username + ":");

                chatBox.getChildren().add(
                        createMessageBubble(message, isMe)
                );

                scrollPane.setVvalue(1.0);
            });
        });

        new Thread(() -> client.startClient(username)).start();
    }

    public static void main(String[] args) {
        launch();
    }
}