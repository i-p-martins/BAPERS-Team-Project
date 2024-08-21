package bapers.bapgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BapersApplication extends Application {

	/**
	 * 
	 * @param stage
	 */
	public void start(Stage stage) {
		try{



			Parent root = FXMLLoader.load(getClass().getResource("view/fxml/Login.fxml"));

			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int width = gd.getDisplayMode().getWidth();
			int height = gd.getDisplayMode().getHeight();

			Scene scene = new Scene(root,width*.666,height*.61);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());





			stage.getIcons().add(new Image(String.valueOf(getClass().getResource("view/fxml/assets/app.png"))));

			stage.setTitle("BAPERS");
			stage.setScene(scene);
			stage.show();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}