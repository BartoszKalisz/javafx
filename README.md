komendy do uruchomienia aplikacji(przy obecnym układzie katalogów):
    budowanie: javac --module-path <sciezka do biblioteki javaFx> --add-modules javafx.controls,javafx.fxml -d bin src/main/java/src/App.java 
                np. javac --module-path C:/Users/bartek/Downloads/openjfx-21.0.6_windows-x64_bin-sdk/javafx-sdk-21.0.6/lib --add-modules javafx.controls,javafx.fxml -d bin src/main/java/src/App.java 

    odpalanie: java --module-path <sciezka do biblioteki javaFx> --add-modules javafx.controls,javafx.fxml -cp bin src.App
                np. java --module-path C:/Users/bartek/Downloads/openjfx-21.0.6_windows-x64_bin-sdk/javafx-sdk-21.0.6/lib --add-modules javafx.controls,javafx.fxml -cp bin src.App

komendy do uruchamiania testów:
    mvn test(wszystkie testy)
    mvn test -Dtest=AppTest#<nazwa_testu> np mvn test -Dtest=AppTest#testSubmitForm    
