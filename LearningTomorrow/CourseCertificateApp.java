import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.util.Scanner;


public class CourseCertificateApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Congratulations!");

        // Create a VBox to hold the certificate content
        VBox certificateLayout = new VBox();
        certificateLayout.setAlignment(Pos.CENTER);
        certificateLayout.setSpacing(10);
        certificateLayout.setPadding(new Insets(20, 50, 20, 50));

        // Certificate title
        Label title = new Label("Certificate of Completion");
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/CERTIFICATE/alex-brush.ttf"), 36);
        title.setFont(customFont);
        title.setUnderline(true);

        // Participant name
        Label participantLabel = new Label("This is to certify that");

        // Enter user name
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for input
        System.out.print("Enter your name: ");

        // Read the user's input as a String
        String name = scanner.nextLine();
        Label participantName = new Label(name);
        customFont = Font.loadFont(getClass().getResourceAsStream("/CERTIFICATE/lobster.otf"), 24);
        participantName.setFont(customFont);

        // Course name
        Label courseLabel = new Label("has successfully completed the course");
        Label courseName = new Label("Course XXXX");
        customFont = Font.loadFont(getClass().getResourceAsStream("/CERTIFICATE/lobster.otf"), 24);
        courseName.setFont(customFont);

        // Date of completion
        Label dateLabel = new Label("Date:");
        customFont = Font.loadFont(getClass().getResourceAsStream("/CERTIFICATE/lobster.otf"), 26);
        dateLabel.setFont(customFont);
        Label completionDate = new Label("December, 2023");
        customFont = Font.loadFont(getClass().getResourceAsStream("/CERTIFICATE/lobster.otf"), 26);
        completionDate.setFont(customFont);
        // Add all components to the VBox
        certificateLayout.getChildren().addAll(
                title,
                participantLabel,
                participantName,
                courseLabel,
                courseName,
                dateLabel,
                completionDate
        );

        // Center Labels both vertically and horizontally

        // Create a rectangle for the border of the certificate

        Rectangle border = new Rectangle();
        border.setStroke(Color.GOLD);
        border.setStrokeWidth(10);
        border.setFill(Color.LIGHTBLUE);

        // StackPane to overlay the rectangle and VBox

        // Adding the image in
        // Create an ImageView for the image
        Image image = new Image(getClass().getResourceAsStream("/CERTIFICATE/stamp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);  // Set the desired width
        imageView.setFitHeight(200); // Set the desired height
        // Set the position of the image on the bottom right
        StackPane.setAlignment(imageView, Pos.BOTTOM_RIGHT);

        StackPane stackPane = new StackPane();
        // Binding the border to the stack pane
        border.widthProperty().bind(stackPane.widthProperty());
        border.heightProperty().bind(stackPane.heightProperty());
        stackPane.getChildren().addAll(border, certificateLayout, imageView);
        StackPane.setAlignment(certificateLayout, Pos.CENTER);

        Scene scene = new Scene(stackPane, 700, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}