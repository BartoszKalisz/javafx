package src;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testfx.matcher.base.NodeMatchers.isVisible;

import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;


@ExtendWith(ApplicationExtension.class)
public class AppTest extends ApplicationTest {

    private App app;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream(); // Przechwycenie konsoli

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();

        // Przechwytujemy System.out, aby sprawdzić, co zostało wypisane w konsoli
        System.setOut(new PrintStream(outContent));
    }

@Test
public void testEmployeeAnnaKowalskaDetails() {
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // 1. Przejście do zakładki "Employees"
    TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
    Tab employeeTab = tabPane.getTabs().stream()
        .filter(tab -> "Employees".equals(tab.getText()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Tab 'Employees' not found"));

    clickOn("Employees");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // Sprawdzenie czy aktualna zakładka to "Employees"
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
assertEquals("Employees", selectedTab.getText(), "Powinien być aktywny tab 'Employees'");
    

    // 2. Kliknięcie w nagłówek "First Name", aby posortować
    clickOn("First Name");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // Pobierz pierwszy wiersz z tabeli
TableView<Employee> table = lookup(".table-view").queryAs(TableView.class);
Employee firstRow = table.getItems().get(0);
Employee secondRow = table.getItems().get(1);
Employee thirdRow = table.getItems().get(2);

// Sprawdź, czy pierwszy wiersz to Anna
assertEquals("Anna", firstRow.getFirstName(), "Pierwszy wiersz powinien być 'Anna'");
assertEquals("John", secondRow.getFirstName(), "Pierwszy wiersz powinien być 'Anna'");
assertEquals("Maria", thirdRow.getFirstName(), "Pierwszy wiersz powinien być 'Anna'");

    // 3. Znalezienie TableView i wyszukanie wiersza z Anną Kowalską

    Employee anna = table.getItems().stream()
        .filter(e -> "Anna".equals(e.getFirstName()) && "Kowalska".equals(e.getLastName()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Anna Kowalska not found in table"));

    // 4. Sprawdzenie czy jej stanowisko to "Manager"
    assertEquals("Manager", anna.getPosition(), "Anna Kowalska should be a Manager");

    // 5. Zaznaczenie jej wiersza w tabeli
    Platform.runLater(() -> table.getSelectionModel().select(anna));
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // 6. Kliknięcie przycisku "Show Details"
    clickOn("Show Details");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // 7. Sprawdzenie, czy jesteśmy na zakładce "Employee Details: Anna"
    Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
    assertEquals("Employee Details: Anna", currentTab.getText(), "Should be on details tab for Anna");

    // 8. Sprawdzenie danych w szczegółach
    verifyThat("Anna", isVisible());
    verifyThat("Kowalska", isVisible());
    verifyThat("Manager", isVisible());
    verifyThat("123-456-789", isVisible());

    // 9. Kliknięcie "Back"
    clickOn("Back");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // 10. Sprawdzenie, czy wróciliśmy do zakładki "Employees"
    Tab currentAfterBack = tabPane.getSelectionModel().getSelectedItem();
    assertEquals("Employees", currentAfterBack.getText(), "Should be back on Employees tab");
}

// @Test
// public void testProductModificationScenarioWithSortAndDatabaseCheck() {
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

//     // 1. Przejdź do zakładki "Products"
//     TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
//     Tab productsTab = tabPane.getTabs().stream()
//         .filter(tab -> "Products".equals(tab.getText()))
//         .findFirst()
//         .orElseThrow(() -> new RuntimeException("Zakładka 'Products' nie została znaleziona"));

//     clickOn("Products");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
//     assertEquals("Products", tabPane.getSelectionModel().getSelectedItem().getText(), "Powinien być aktywny tab 'Products'");

//     // 2. Kliknięcie w "Type", aby posortować
//     clickOn("Type");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

//     // Sprawdzenie czy posortowane rosnąco według typu
//     TableView<Product> table = lookup("#productTable").queryAs(TableView.class);
//     List<String> types = table.getItems().stream()
//         .map(Product::getCategory)
//         .toList();
//     List<String> sortedTypes = new ArrayList<>(types);
//     Collections.sort(sortedTypes);
//     assertEquals(sortedTypes, types, "Typy powinny być posortowane rosnąco");

//     // 3. Sprawdzenie produktów z kategorii Electronics
//     var electronics = table.getItems().stream()
//         .filter(p -> "Electronics".equals(p.getCategory()))
//         .toList();

//     assertEquals(3, electronics.size(), "Powinny być 3 produkty z kategorii Electronics");
//     assertTrue(electronics.stream().anyMatch(p -> "Smartphone".equals(p.getName())));
//     assertTrue(electronics.stream().anyMatch(p -> "Tablet".equals(p.getName())));

//     // 4. Usunięcie jednego produktu np. Laptop
//     Product toDelete = electronics.stream().filter(p -> "Smartphone".equals(p.getName())).findFirst().orElseThrow();
//     Platform.runLater(() -> table.getSelectionModel().select(toDelete));
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
//     clickOn("Delete");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

//     // Sprawdzenie GUI – produkt nie powinien być już widoczny
//     var afterDelete = table.getItems().stream()
//         .map(Product::getName)
//         .toList();
//     assertFalse(afterDelete.contains("Smartphone"), "Laptop powinien zostać usunięty z tabeli");

//     // DODANE: sprawdzenie w bazie danych
//     ProductService productService = new ProductService();
//     List<Product> fromDb = productService.getAllProducts();
//     assertFalse(fromDb.stream().anyMatch(p -> "Laptop".equals(p.getName())), "Laptop nie powinien istnieć w bazie"); //TODO zmienic sprawzdanie z laptopa bo usuwamy cos innego, dodanie emchanizmu ktory faktycznie nie usuwa,aby po kazdym usunieciu nie trzebylo dodawac

//     // 5. Kliknięcie Add i sprawdzenie zakładki
//     clickOn("Add");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
//     Tab addTab = tabPane.getSelectionModel().getSelectedItem();
//     assertEquals("Add Product", addTab.getText(), "Powinna być aktywna zakładka dodawania produktu");

//     // 6. Wprowadzenie danych i zapis
//     clickOn(".text-field").write("Electronics"); // Type
//     type(KeyCode.TAB);
//     write("Tablet"); // Name
//     type(KeyCode.TAB);
//     write("999.99"); // Price

//     clickOn("Save");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
//     assertEquals("Products", tabPane.getSelectionModel().getSelectedItem().getText(), "Po zapisie wracamy do zakładki Products");

//     // 7. Sprawdzenie, czy "Tablet" się pojawił
//     boolean foundTablet = table.getItems().stream()
//         .anyMatch(p -> "Tablet".equals(p.getName()) && "Electronics".equals(p.getCategory()));  //TODO todanie więcej szczególów do sprawdzeń w calym tescie
//     assertTrue(foundTablet, "Nowy produkt 'Tablet' powinien się pojawić w tabeli");

//     // 8. Kliknięcie Add i Cancel
//     clickOn("Add");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
//     clickOn("Cancel");
//     WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

//     // Sprawdzenie, że nadal jesteśmy na zakładce Products
//     assertEquals("Products", tabPane.getSelectionModel().getSelectedItem().getText(), "Po anulowaniu powinien być 'Products'");
// }

@Test
public void testChangingProductPriceToLowerSegmentAndFilterValidation() {
    // 1. Przejście do zakładki Products
    clickOn("Products");
    TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    assertEquals("Products", selectedTab.getText(), "Powinniśmy znajdować się na zakładce 'Products'");

// 2. Sprawdzenie filtrowania < 20,000
ChoiceBox<String> filterBox = lookup("#priceFilter").queryAs(ChoiceBox.class); // poprawka: "priceFitler" → "priceFilter"
TableView<Product> table = lookup("#productTable").queryAs(TableView.class);

Platform.runLater(() -> filterBox.setValue("Below 20,000"));
WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

List<Product> below20k = table.getItems();
assertEquals(5, below20k.size(), "Powinno być 5 produktów poniżej 20,000");
for (Product p : below20k) {
    assertTrue(p.getPrice() < 20000, "Produkt powinien mieć cenę < 20,000: " + p.getPrice());
}

// 3. Sprawdzenie filtrowania 20,000 - 1,000,000
Platform.runLater(() -> filterBox.setValue("20,000 - 1,000,000"));
WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

List<Product> midRange = table.getItems();
assertEquals(1, midRange.size(), "Powinien być 1 produkt w przedziale 20k-1M");
for (Product p : midRange) {
    assertTrue(p.getPrice() >= 20000 && p.getPrice() <= 1000000,
        "Produkt powinien być w zakresie 20k-1M: " + p.getPrice());
}

// 4. Sprawdzenie filtrowania > 1,000,000
Platform.runLater(() -> filterBox.setValue("Above 1,000,000"));
WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

List<Product> above1M = table.getItems();
assertEquals(0, above1M.size(), "Nie powinno być żadnego produktu powyżej 1,000,000");
for (Product p : above1M) {
    assertTrue(p.getPrice() > 1000000, "Produkt powinien mieć cenę > 1,000,000: " + p.getPrice());
}

    // 5. Reset filtrów
    Platform.runLater(() -> filterBox.setValue("All prices"));
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // 6. Wyszukanie produktu z ceną 1000.00
    Product[] product1000 = new Product[1];
for (Product p : table.getItems()) {
    if (Math.abs(p.getPrice() - 1000.00) < 0.01) {       //TODO - dziwna składnia
        product1000[0] = p;
        break;
    }
}
assertTrue(product1000[0] != null, "Nie znaleziono produktu z ceną 1000.00");
    assertTrue(product1000 != null, "Nie znaleziono produktu z ceną 1000.00");

    // 7. Edycja produktu
    Platform.runLater(() -> table.getSelectionModel().select(product1000[0]));
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
    clickOn("#editButton");

    // Sprawdzenie, że jesteśmy w zakładce edycji
    Tab editTab = tabPane.getSelectionModel().getSelectedItem();
    assertTrue(editTab.getText().contains("Edit"), "Powinniśmy być w zakładce 'Edit Product'");

    // 8. Zmiana ceny na 999.99
    clickOn("#priceField").eraseText(10).write("999.99");
    clickOn("#saveButton");

    // Sprawdzenie, że wróciliśmy do zakładki Products
    Tab afterSaveTab = tabPane.getSelectionModel().getSelectedItem();
    assertEquals("Products", afterSaveTab.getText(), "Powinniśmy wrócić do zakładki 'Products' po zapisaniu");

    // 9. Sprawdzenie, że produkt ma nową cenę 999.99
    Product[] updatedProduct = new Product[1];
for (Product p : table.getItems()) {
    if (p.getId() == product1000[0].getId()) {                         //TODO do poprawineia ten sposób bo teraz zakładamy ze 1produkt mial 1000 i ze po zmianie tez tylko 1bedzie mial 999.99
        updatedProduct[0] = p;
        break;
    }
}
    assertTrue(updatedProduct != null, "Nie znaleziono produktu po edycji");
    assertEquals(999.99, updatedProduct[0].getPrice(), 0.01, "Cena powinna zostać zmieniona na 999.99");

    // 10. Sprawdzenie, że znajduje się teraz w filtrze < 20,000
    Platform.runLater(() -> filterBox.setValue("Below 20,000"));
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
    boolean foundInLowSegment = table.getItems().stream().anyMatch(p -> p.getId() == updatedProduct[0].getId());    //Potzrbna zmiana cen bo sprawdzamy czy jest teraz ponizej <20k, ale wczesniej tez byl
    assertTrue(foundInLowSegment, "Produkt powinien znajdować się teraz w filtrze < 20,000");

    // 11. Przypadkowe kliknięcie Edit i potem Cancel – nic się nie powinno zmienić
    Platform.runLater(() -> table.getSelectionModel().select(updatedProduct[0]));
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);
    clickOn("#editButton");
    clickOn("#cancelButton");

    // Sprawdzenie, że nadal jesteśmy na zakładce Products
    Tab afterCancelTab = tabPane.getSelectionModel().getSelectedItem();
    assertEquals("Products", afterCancelTab.getText(), "Po anulowaniu nadal powinniśmy być na zakładce 'Products'");
}//TODO można doddac sprawdzenia ze klikanie przyciskow bez zaznaczenia nic nie daje
}
