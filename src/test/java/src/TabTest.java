package src;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(ApplicationExtension.class)
public class TabTest extends ApplicationTest {

    private App app;

    @Override
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    // @Test
    // public void debugTabPane() {
    //     // Pobieramy `TabPane`
    //     TabPane tabPane = lookup(".tab-pane").query();
    //     if (tabPane == null) {
    //         System.out.println("TabPane not found!");
    //         return;
    //     }

    //     // Pobieramy wszystkie nazwy zakładek
    //     List<String> tabNames = tabPane.getTabs().stream()
    //             .map(Tab::getText) // Pobiera nazwy zakładek
    //             .collect(Collectors.toList());

    //     System.out.println("Found tab names: " + tabNames);
    // }
}
