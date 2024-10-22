package cs1302.gallery;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.LocalTime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.Priority;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.scene.text.Font;

/**
 * Represents an iTunes Gallery App.
 */
public class GalleryApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private Stage stage;
    private Scene scene;
    private VBox root;

    HBox topHBox;
    Text msg;
    String updateMsg;
    VBox imgViewVBox;
    HBox botHBox;
    ProgressBar pBar;
    Label iTunesLabel;
    Button playButton;
    Label search;
    TextField userInput;
    ComboBox<String> listBox;
    Button getImgButton;
    ArrayList<Image> images;
    ArrayList<String> imgUrlList;
    ArrayList<String> nonDupUrlList;
    ArrayList<Image> currentImg;
    TilePane imgTilePane;
    ImageView imgView;
    Image img;
    Alert alert;
    String url = "https://itunes.apple.com/search";
    String query;

    /**
     * Constructs a {@code GalleryApp} object}.
     */
    public GalleryApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox();
        this.topHBox = new HBox();
        this.imgViewVBox = new VBox();
        this.imgTilePane = new TilePane();
    } // GalleryApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        System.out.println("init() called");
        this.updateMsg = "Type in a term, select a media type, then click the button.";
        this.msg = new Text(updateMsg); // Display Message
        this.msg.setTextAlignment(TextAlignment.LEFT);
        this.msg.setFont(Font.font("time new roman", 10));
        setSearchBar();
        setContent();
        this.botHBox = new HBox();
        this.pBar = new ProgressBar(0);
        this.iTunesLabel = new Label("Images provided by iTunes Search API.");
        HBox.setHgrow(pBar, Priority.ALWAYS);
        this.pBar.setMaxWidth(Double.MAX_VALUE);
        this.botHBox.setPadding(new Insets(5));
        this.botHBox.setAlignment(Pos.CENTER_LEFT);
        this.botHBox.setSpacing(2);
        this.botHBox.getChildren().addAll(pBar, iTunesLabel);
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.root.getChildren().addAll(topHBox,msg,imgTilePane,botHBox);
        this.stage = stage;
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("GalleryApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
    } // start

    /** {@inheritDoc} */
    @Override
    public void stop() {
        System.out.println("stop() called");
    } // stop

    /**
     * Set up SearchBar.
     */
    private void setSearchBar() {
        this.topHBox.setAlignment(Pos.CENTER);
        this.topHBox.setSpacing(2);
        this.topHBox.setPadding(new Insets (1));
        this.playButton = new Button("Play");
        this.search = new Label("Search");
        this.userInput = new TextField("Prince");
        this.listBox = new ComboBox<String>();
        this.listBox.getItems().addAll("movie", "podcast", "music", "musicVideo", "audiobook",
                                       "shortFilm", "tvShow", "software", "ebook", "all");
        this.listBox.getSelectionModel().select(2);
        this.getImgButton = new Button("Get Images");
        this.playButton.setDisable(true);
        getImg();
        playButton.setOnAction((update) -> playImg());
        this.topHBox.getChildren().addAll(playButton,search,userInput,listBox,getImgButton);
    } // SearchBar Constructor

    /**
     * Set default images on Image Tile Pane Display.
     */
    private void setContent() {
        for (int i = 0; i < 20; i++) {
            imgView = new ImageView();
            img = new Image("file:resources/default.png");
            imgView.setImage(img);
            imgTilePane.getChildren().add(imgView);
        } // for tilePane
    } // Content

    /**
     * Get Images from iTunes API.
     */
    private void getImg () {
        EventHandler<ActionEvent> getImages = (event) -> {
            alert = new Alert(AlertType.ERROR);
            try {
                getImgButton.setDisable(true);
                playButton.setDisable(true);
                pBar.setProgress(0);
                String term = URLEncoder.encode(userInput.getText(), StandardCharsets.UTF_8);
                String limit = URLEncoder.encode("200", StandardCharsets.UTF_8);
                String media = URLEncoder.encode(listBox.getValue(), StandardCharsets.UTF_8);
                //String query = String.format("?term=%s&limit=%s&media=%s", term, limit, media);
                query = String.format("?term=%s&media=%s&limit=%s", term, media, limit);
                HttpRequest request = HttpRequest.newBuilder()
                                      .uri(URI.create(url + query))
                                      .build();
                HttpResponse<String> response = GalleryApp.HTTP_CLIENT.send(request,
                                                BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new IOException(response.toString());
                } // if
                String jsonString = response.body();
                // parse the JSON-formatted string using GSON
                ItunesResponse itunesResponse = GalleryApp.GSON
                                                .fromJson(jsonString,
                                                cs1302.gallery.ItunesResponse.class);
                downloadImg(itunesResponse);
                runNow(() -> {
                    setImages();
                    playButton.setDisable(false);
                    getImgButton.setDisable(false);
                    msg.setText(url + query);
                });
            } catch (IOException | InterruptedException e) {
                alert.setContentText("URI: " + url + query + "\n\n" + e.toString());
                alert.showAndWait();
                msg.setText("Last attempt to get images failed...");
                getImgButton.setDisable(false);
            } catch (IllegalArgumentException ie) {
                alert.setContentText("URI: " + url + query + "\n\n" + ie.getMessage());
                alert.showAndWait();
                msg.setText("Last attempt to get images failed...");
                getImgButton.setDisable(false);
            }
        };
        getImgButton.setOnAction(getImages);
    } // getImg

    /**
     * Download Images from iTunes and store their URL into a List.
     * @param itunesResponse - ItunesResponse
     */
    private void downloadImg(ItunesResponse itunesResponse) {
        imgUrlList = new ArrayList<String>();
        for (int i = 0; i < itunesResponse.resultCount; i++) {
            ItunesResult result = itunesResponse.results[i];
            String imgUrl = result.artworkUrl100;
            imgUrlList.add(imgUrl);
            System.out.println(i + " ::Image URL List:: " + imgUrl);
            msg.setText("Getting images...");
        } // for
        if (imgUrlList.size() < 21) {
            throw new IllegalArgumentException("Exception: java.lang.IllegalArgumentException: "
            + imgUrlList.size() + " distinct results found, but 21 or more are needed.");
        } // if
        removeDuplicate();
    } // downloadImg

    /**
     * Store all non duplicate images into a List.
     */
    private void removeDuplicate() {
        System.out.println("remove dup called");
        nonDupUrlList = new ArrayList<String>();
        for (int i = 0; i < imgUrlList.size(); i++) {
            if (!nonDupUrlList.contains(imgUrlList.get(i))) {
                nonDupUrlList.add(imgUrlList.get(i));
            } // if
        } // for
        System.out.println("::nonDuplicateList Size:: " + nonDupUrlList.size());
    } // checkDuplicate

    /**
     * setImages into a image List.
     */
    private void setImages() {
        images = new ArrayList<Image>();
        currentImg = new ArrayList<Image>();
        for (int i = 0; i < nonDupUrlList.size(); i++) {
            images.add(new Image (nonDupUrlList.get(i)));
            pBar.setProgress(1.0 * i / nonDupUrlList.size());
        } // for
        for (int k = 0; k < 20; k++) {
            currentImg.add(new Image (nonDupUrlList.get(k)));
        }
        pBar.setProgress(1);
        Platform.runLater(() -> {
            System.out.println("Set Images");
            for (int j = 0; j < 20; j++) {
                System.out.println("Updata Image:::" + j);
                imgView = new ImageView();
                img = images.get(j);
                imgView.setImage(img);
                imgView.setFitHeight(100);
                imgView.setFitWidth(100);
                imgTilePane.getChildren().set(j, imgView);
            } // for
        });
    } // setImages

    /**
     * play Images.
     */
    private void playImg() {
        playButton.setText("Pause");
        EventHandler<ActionEvent> handler = (event) -> checkAndSet();
        System.out.println(LocalTime.now());
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), handler);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        playButton.setOnAction((play) -> {
            if (playButton.getText().equals("Play")) {
                playButton.setText("Pause");
                checkAndSet();
            } else {
                playButton.setText("Play");
                timeline.pause();
                playButton.setOnAction((handler1) -> playImg());
            }
        });
        getImgButton.setOnMouseClicked((click) -> {
            timeline.pause();
            playButton.setText("Play");
            playButton.setOnAction((handler1) -> playImg());
        });
    } // checkAndPlace

    /**
     * Change Image on Image Tile Pane when user Click the play Button.
     */
    private void checkAndSet () {
        int randImg = (int)(Math.random() * (images.size() - 1));
        System.out.println("RandImg :" + randImg);
        int randPos = (int)(Math.random() * 20);
        System.out.println("RandPos: " + randPos);
        ImageView imgViewTemp  = new ImageView();
        Image temp = images.get(randImg);
        if (!currentImg.contains(temp)) {
            imgViewTemp.setImage(temp);
            imgViewTemp = new ImageView(images.get(randImg));
            imgViewTemp.setFitHeight(100);
            imgViewTemp.setFitWidth(100);
            imgTilePane.getChildren().set(randPos, imgViewTemp);
            currentImg.remove(randPos);
            currentImg.add(randPos, temp);
        }
    } // playImg

    /**
     * Creates and immediately starts a new daemon thread that executes
     * {@code target.run()}. This method, which may be called from any thread,
     * will return immediately its the caller.
     * @param target the object whose {@code run} method is invoked when this
     *               thread is started
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runNow

    /**
     * Get the url of a specifc index of a list.
     * @param index - int
     * @param isDup - boolean
     * @return a URL string from nonDupUrlList (true) / a URL string from imgUrlList (false)
     */
    private String getString(int index, boolean isDup) {
        if (isDup == true) {
            return nonDupUrlList.get(index);
        } else {
            return imgUrlList.get(index);
        } // else
    } // getString

} // GalleryApp
