package src;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
public void testButtonClick() {
    // Upewniamy się, że GUI jest gotowe
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Znalezienie TabPane (jeśli mamy tylko jedno)
    TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);

    // Znalezienie zakładki "Buttons"
    Tab buttonsTab = tabPane.getTabs().stream()
        .filter(tab -> "Buttons".equals(tab.getText()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Nie znaleziono zakładki 'Buttons'!"));

    // Przełączenie na zakładkę "Buttons"
    Platform.runLater(() -> tabPane.getSelectionModel().select(buttonsTab));
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);


    // Kliknięcie w przycisk
    clickOn("Click me!");

    // Sprawdzenie zmiany tekstu
    verifyThat(".button", hasText("Clicked!"));

    // Sprawdzenie konsoli
    assertTrue(outContent.toString().contains("Button clicked!"), "Console should contain 'Button clicked!'");
}


@Test
public void testSubmitForm() {
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Przejdź do zakładki "Form"
    TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
    Tab formTab = tabPane.getTabs().stream()
        .filter(tab -> "Form".equals(tab.getText()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Nie znaleziono zakładki 'Form'!"));

    Platform.runLater(() -> tabPane.getSelectionModel().select(formTab));
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Wprowadź tekst i kliknij przycisk
    clickOn(".text-field").write("John Doe");
    clickOn("Submit");
    WaitForAsyncUtils.waitForFxEvents();

    // Sprawdź, czy tekst przycisku się nie zmienił
    verifyThat("Submit", hasText("Submit"));

    // Sprawdź zawartość konsoli
    assertTrue(outContent.toString().contains("Submitted: John Doe"),
            "Console should contain 'Submitted: John Doe'");

    
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);
}


    @Test
    public void testChoiceBoxSelection() {
        // Upewniamy się, że GUI jest gotowe
        WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);
    
        // Znalezienie TabPane (jeśli mamy tylko jedno)
        TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
    
        // Znalezienie zakładki "Lists"
        Tab listsTab = tabPane.getTabs().stream()
            .filter(tab -> "Lists".equals(tab.getText()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Nie znaleziono zakładki 'Lists'!"));
    
        // Przełączenie na zakładkę "Lists"
        Platform.runLater(() -> tabPane.getSelectionModel().select(listsTab));
        WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);
    
        // Sprawdzenie widoczności
        verifyThat(".choice-box", isVisible());
    
        // Pobranie samego ChoiceBoxa
        ChoiceBox<String> choiceBox = lookup(".choice-box").queryAs(ChoiceBox.class);
    
        // Funkcja pomocnicza do wyboru opcji przez kliknięcie + strzałki
        Runnable selectNext = () -> {
            clickOn(".choice-box");
            type(KeyCode.DOWN);
            type(KeyCode.ENTER);
            WaitForAsyncUtils.waitForFxEvents();
        };
    
        Runnable selectPrevious = () -> {
            clickOn(".choice-box");
            type(KeyCode.UP);
            type(KeyCode.ENTER);
            WaitForAsyncUtils.waitForFxEvents();
        };
        
        // Zmieniamy opcje krok po kroku
        selectNext.run(); // Option 2
        assertTrue("Option 2".equals(choiceBox.getValue()), "Powinno być 'Option 2'");
    
        selectNext.run(); // Option 3
        assertTrue("Option 3".equals(choiceBox.getValue()), "Powinno być 'Option 3'");
    
        selectPrevious.run(); // Back to Option 2
        assertTrue("Option 2".equals(choiceBox.getValue()), "Powinno być 'Option 2'");
    
        selectPrevious.run(); // Back to Option 1
        assertTrue("Option 1".equals(choiceBox.getValue()), "Powinno być 'Option 1'");
    }
    
    @Test
public void testTableSorting() {
    // Upewniamy się, że GUI jest gotowe
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Przechodzimy do zakładki "Table"
    TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
    Tab tableTab = tabPane.getTabs().stream()
        .filter(tab -> "Table".equals(tab.getText()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Nie znaleziono zakładki 'Table'!"));

    Platform.runLater(() -> tabPane.getSelectionModel().select(tableTab));
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Kliknięcie w nagłówki kolumn, żeby przetestować sortowanie
    clickOn("ID");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    clickOn("Name");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    clickOn("Age");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // Sprawdzenie, czy tabela jest widoczna
    verifyThat(".table-view", isVisible());

    // Opcjonalnie: możesz sprawdzić kolejność wierszy (ale wymaga dostępu do TableView)
    TableView<String[]> table = lookup(".table-view").queryAs(TableView.class);
    assertTrue(table.getItems().size() > 0, "Tabela powinna zawierać dane.");
}

@Test
public void testTableSortingWithVerification() {
    // Poczekaj na GUI
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Przejdź do zakładki "Table"
    TabPane tabPane = lookup(".tab-pane").queryAs(TabPane.class);
    Tab tableTab = tabPane.getTabs().stream()
        .filter(tab -> "Table".equals(tab.getText()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Nie znaleziono zakładki 'Table'!"));

    Platform.runLater(() -> tabPane.getSelectionModel().select(tableTab));
    WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);

    // Kliknij nagłówek "Name", by posortować
    clickOn("Name");
    WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

    // Pobierz tabelę
    TableView<String[]> table = lookup(".table-view").queryAs(TableView.class);
    assertTrue(table.getItems().size() >= 3, "Tabela powinna zawierać co najmniej 3 rekordy.");

    // Pobierz dane po sortowaniu
    String name1 = table.getItems().get(0)[1]; // pierwszy wiersz, kolumna "Name"
    String name2 = table.getItems().get(1)[1];
    String name3 = table.getItems().get(2)[1];

    // Sprawdź poprawną kolejność alfabetyczną
    assertTrue("Alice".equals(name1), "Pierwszy wiersz powinien zawierać 'Alice'");
    assertTrue("Bob".equals(name2), "Drugi wiersz powinien zawierać 'Bob'");
    assertTrue("Charlie".equals(name3), "Trzeci wiersz powinien zawierać 'Charlie'");
}


}
