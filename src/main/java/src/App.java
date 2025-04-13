package src;

import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // --- Tab: Form ---
        Tab formTab = new Tab("Form");
        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(30));  // Większy odstęp
        formLayout.setVgap(15);
        formLayout.setHgap(15);
        formLayout.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField nameInput = new TextField();
       
        nameInput.setPromptText("Enter your name...");
        nameInput.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;"); // Większe pole tekstowe

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px;");
        submitButton.setOnAction(e -> System.out.println("Submitted: " + nameInput.getText()));

        formLayout.add(nameLabel, 0, 0);
        formLayout.add(nameInput, 1, 0);
        formLayout.add(submitButton, 1, 1);
        formTab.setContent(formLayout);


           // --- Tab: Buttons ---
        Tab buttonTab = new Tab("Buttons");
        VBox buttonLayout = new VBox(20);
        buttonLayout.setPadding(new Insets(30));
        buttonLayout.setAlignment(Pos.CENTER);

        // Przycisk zmieniający napis
        Button simpleButton = new Button("Click me!");
        simpleButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 16px 32px;");
        simpleButton.setOnAction(e -> {
            Platform.runLater(() -> simpleButton.setText("Clicked!")); // Zapewniamy poprawną zmianę GUI
            System.out.println("Button clicked!");
        });

        buttonLayout.getChildren().add(simpleButton);
        buttonTab.setContent(buttonLayout);

        // --- Tab: Lists ---
        Tab listTab = new Tab("Lists");
        VBox listLayout = new VBox(20);
        listLayout.setPadding(new Insets(30));
        listLayout.setAlignment(Pos.CENTER);

        Label listLabel = new Label("Choose an option:");
        listLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Option 1", "Option 2", "Option 3");
        choiceBox.setValue("Option 1");
        choiceBox.setStyle("-fx-font-size: 20px; -fx-pref-width: 250px; -fx-pref-height: 40px;");

        listLayout.getChildren().addAll(listLabel, choiceBox);
        listTab.setContent(listLayout);



        // --- Tab: Table ---
        Tab tableTab = new Tab("Table");
        VBox tableLayout = new VBox(15);
        tableLayout.setPadding(new Insets(20));
        tableLayout.setAlignment(Pos.CENTER);

        /* Tworzenie tabeli */
        TableView<String[]> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Automatyczne dopasowanie kolumn

        // Definiowanie kolumn
        TableColumn<String[], String> col1 = new TableColumn<>("ID");
        col1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> col2 = new TableColumn<>("Name");
        col2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> col3 = new TableColumn<>("Age");
        col3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        // Dodanie kolumn do tabeli
        table.getColumns().addAll(col1, col2, col3);

        /* Dodanie przykładowych danych */
        table.getItems().addAll(
            new String[]{"1", "Bob", "30"},
            new String[]{"2", "Charlie", "28"},
            new String[]{"3", "Alice", "25"}
        );

        // Nagłówek
        Label tableLabel = new Label("User Data Table");
        tableLabel.setTextFill(Color.WHITE);
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dodanie elementów do układu
        tableLayout.getChildren().addAll(tableLabel, table);
        tableTab.setContent(tableLayout);


        // Add all tabs
        tabPane.getTabs().addAll(formTab, buttonTab, listTab, tableTab);

        // 🎯 **Tworzenie sceny z większym oknem**
        Scene scene = new Scene(tabPane, 1040, 650); // Powiększone okno

        // ✅ **Dodanie arkusza stylów**
        scene.getStylesheets().add(new File("src\\main\\resources\\style.css").toURI().toString());

        // 🎯 **Ustawienie sceny w Stage**
        primaryStage.setTitle("Styled Test Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showPopup() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Dialog Window");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label label = new Label("This is a dialog window.");
        label.setTextFill(Color.DARKRED);
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        closeButton.setOnAction(e -> dialog.close());

        layout.getChildren().addAll(label, closeButton);

        Scene scene = new Scene(layout, 300, 200); // Większe okienko
        scene.getStylesheets().add(new File("src\\main\\resources\\style.css").toURI().toString());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
