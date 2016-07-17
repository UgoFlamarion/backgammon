import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageLocation extends Application {

  @Override
  public void start(final Stage primaryStage) {
    final Pane root = new Pane();
    final VBox controls = new VBox(5);
    final TextField xField = new TextField("200");
    final TextField yField = new TextField("300");
    final Button moveButton = new Button("Move");
    controls.getChildren().addAll(xField, yField, moveButton);

    final ImageView image = new ImageView("http://www.oracle.com/ocom/groups/public/@otn/documents/digitalasset/1917282.jpg");
    image.setLayoutX(200);
    image.setLayoutY(300);
    root.getChildren().add(controls);
    root.getChildren().add(image);

    final EventHandler<ActionEvent> moveHandler = event -> {
      final double x = Double.parseDouble(xField.getText());
      final double y = Double.parseDouble(yField.getText());
      image.setTranslateX(x);
      image.setTranslateY(y);
    };
    moveButton.setOnAction(moveHandler);
    xField.setOnAction(moveHandler);
    yField.setOnAction(moveHandler);

    primaryStage.setScene(new Scene(root, 800, 600));
    primaryStage.show();
  }

  public static void main(final String[] args) {
    Application.launch(args);
  }
}