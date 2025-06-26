package src;

import java.io.File;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private TableView<Product> productTable;
    private TableView<Employee> employeeTable;
    private ProductService productService = new ProductService();
    private EmployeeService employeeService = new EmployeeService();
    private TabPane tabPane;
    private Tab addEditProductTab;
    private boolean isEditMode = false;
    private Product productToEdit = null;

    @Override
    public void start(Stage primaryStage) {
        tabPane = new TabPane();

        Tab productsTab = createProductsTab();
        Tab employeesTab = createEmployeesTab();
        employeesTab.setId("employeesTab");

        tabPane.getTabs().addAll(productsTab, employeesTab);

        Scene scene = new Scene(tabPane, 1040, 650);
        scene.getStylesheets().add(new File("src/main/resources/style.css").toURI().toString());

        primaryStage.setTitle("Store Management Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createProductsTab() {
        Tab tab = new Tab("Products");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Label label = new Label("Product Management");

        productTable = new TableView<>();
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Product, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));

        productTable.getColumns().addAll(idCol, typeCol, nameCol, priceCol);

        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        ChoiceBox<String> priceFilter = new ChoiceBox<>();

        priceFilter.setId("priceFilter");
        productTable.setId("productTable");
        addButton.setId("addButton");
        editButton.setId("editButton");
        deleteButton.setId("deleteButton");

        addButton.setOnAction(e -> openAddEditProductTab(false, null));

        editButton.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openAddEditProductTab(true, selected);
            }
        });

        deleteButton.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                productService.deleteProduct(selected.getId());
                loadProducts();
            }
        });

        priceFilter.getStyleClass().add("button-like");
        priceFilter.getItems().addAll("All prices", "Below 20,000", "20,000 - 1,000,000", "Above 1,000,000");
        priceFilter.setValue("All prices");
        priceFilter.setOnAction(e -> applyProductPriceFilter(priceFilter.getValue()));

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton, priceFilter);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(label, productTable, buttonBox);
        tab.setContent(layout);

        loadProducts();

        return tab;
    }

    private void openAddEditProductTab(boolean editMode, Product product) {
        if (addEditProductTab != null && tabPane.getTabs().contains(addEditProductTab)) {
            tabPane.getTabs().remove(addEditProductTab);
        }

        isEditMode = editMode;
        productToEdit = product;

        addEditProductTab = new Tab(editMode ? "Edit Product" : "Add Product");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        TextField typeField = new TextField();
        typeField.setPromptText("Type");
        typeField.setId("typeField");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setId("nameField");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setId("priceField");

        if (editMode && product != null) {
            typeField.setText(product.getCategory());
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
        }

        Button saveButton = new Button("Save");
        saveButton.setId("saveButton");
        saveButton.setOnAction(e -> {
            try {
                String type = typeField.getText();
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());

                if (editMode && product != null) {
                    product.setCategory(type);
                    product.setName(name);
                    product.setPrice(price);
                    productService.updateProduct(product);
                } else {
                    productService.addProduct(new Product(0, type, name, price));
                }

                loadProducts();
                tabPane.getTabs().remove(addEditProductTab);
                tabPane.getTabs().stream()
            .filter(tab -> "Products".equals(tab.getText()))
            .findFirst()
            .ifPresent(tabPane.getSelectionModel()::select);
            } catch (NumberFormatException ex) {
                showAlert("Invalid price value.");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setId("cancelButton");
        cancelButton.setOnAction(e -> {
    tabPane.getTabs().remove(addEditProductTab);

    // 👇 Dodaj to, aby wrócić na zakładkę "Products"
    tabPane.getTabs().stream()
        .filter(tab -> "Products".equals(tab.getText()))
        .findFirst()
        .ifPresent(tabPane.getSelectionModel()::select);
});

        layout.getChildren().addAll(new Label("Type:"), typeField, new Label("Name:"), nameField,
                new Label("Price:"), priceField, saveButton, cancelButton);

        addEditProductTab.setContent(layout);
        tabPane.getTabs().add(addEditProductTab);
        tabPane.getSelectionModel().select(addEditProductTab);
    }

    private void applyProductPriceFilter(String selectedOption) {
        List<Product> allProducts = productService.getAllProducts();
        ObservableList<Product> filtered;
        switch (selectedOption) {
            case "Below 20,000" -> filtered = FXCollections.observableArrayList(allProducts.stream().filter(p -> p.getPrice() < 20000).toList());
            case "20,000 - 1,000,000" -> filtered = FXCollections.observableArrayList(allProducts.stream().filter(p -> p.getPrice() >= 20000 && p.getPrice() <= 1000000).toList());
            case "Above 1,000,000" -> filtered = FXCollections.observableArrayList(allProducts.stream().filter(p -> p.getPrice() > 1000000).toList());
            default -> filtered = FXCollections.observableArrayList(allProducts);
        }
        productTable.setItems(filtered);
    }

    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        ObservableList<Product> data = FXCollections.observableArrayList(products);
        productTable.setItems(data);
    }

    private Tab createEmployeesTab() {
        Tab tab = new Tab("Employees");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Label label = new Label("Employee Management");

        employeeTable = new TableView<>();
        employeeTable.setId("employeeTable");
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Employee, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Employee, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));

        TableColumn<Employee, String> positionCol = new TableColumn<>("Position");
        positionCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPosition()));

        employeeTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, positionCol);

        Button detailsButton = new Button("Show Details");
        detailsButton.setId("showDetailsButton");
        detailsButton.setOnAction(e -> showEmployeeDetails());

        HBox buttonBox = new HBox(10, detailsButton);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(label, employeeTable, buttonBox);
        tab.setContent(layout);

        loadEmployees();

        return tab;
    }

    private void showEmployeeDetails() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Tab detailTab = new Tab("Employee Details: " + selected.getFirstName());

            GridPane layout = new GridPane();
            layout.setPadding(new Insets(30));
            layout.setVgap(15);
            layout.setHgap(20);
            layout.setAlignment(Pos.CENTER_LEFT);

            layout.addRow(0, createLabel("ID:"), createValueLabel(String.valueOf(selected.getId())));
            layout.addRow(1, createLabel("First Name:"), createValueLabel(selected.getFirstName()));
            layout.addRow(2, createLabel("Last Name:"), createValueLabel(selected.getLastName()));
            layout.addRow(3, createLabel("Position:"), createValueLabel(selected.getPosition()));

            Label phoneLabel = createValueLabel(selected.getPhoneNumber());
            phoneLabel.setId("phoneText");
            layout.addRow(4, createLabel("Phone:"), phoneLabel);

            layout.addRow(5, createLabel("Address:"), createValueLabel(selected.getAddress()));

            Button backButton = new Button("Back");
            backButton.setId("backButton");
            backButton.setOnAction(e -> tabPane.getTabs().remove(detailTab));
            layout.add(backButton, 1, 6);

            detailTab.setContent(layout);
            tabPane.getTabs().add(detailTab);
            tabPane.getSelectionModel().select(detailTab);
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("detail-label");
        return label;
    }

    private Label createValueLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("value-label");
        return label;
    }

    private void loadEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        ObservableList<Employee> data = FXCollections.observableArrayList(employees);
        employeeTable.setItems(data);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
