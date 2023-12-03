import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.swing.text.html.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final TextField searchTextField = new TextField();
    private final Button searchButton = new Button("Search");
    private final ListView<Movie> movieListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Movie Search");

        HBox searchBar = new HBox();
        searchBar.setPadding(new Insets(10));
        searchBar.getChildren().addAll(new Label("Search:"), searchTextField, searchButton);

        movieListView.setPrefHeight(300);

        VBox layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(searchBar, movieListView);

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.show();

        searchButton.setOnAction(this::onSearchButtonClick);
    }

    private void onSearchButtonClick(ActionEvent event) {
        String searchTerm = searchTextField.getText();

        try {
            String jsonResponse = makeAPIRequest(searchTerm);
            JSONArray jsonArray = new JSONArray(jsonResponse);

            movieListView.getItems().clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Movie movie = new Movie(jsonObject);
                movieListView.getItems().add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makeAPIRequest(String searchTerm) throws Exception {
        // Replace with your actual API URL and parameters
        String urlString = "https://www.omdbapi.com/?apikey=YOUR_API_KEY&s=" + searchTerm;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        connection.disconnect();
        return response.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
