package piccross;

import javafx.application.Preloader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GamePreloader  extends Preloader{
    private Stage stage;
	private Image splashScreenImage = new Image("file:piccross.png");
	private ImageView splashScreenView = new ImageView(splashScreenImage);
	
    private Scene createPreloaderScene() {
    	
       
        BorderPane p = new BorderPane();
        p.setCenter(splashScreenView);
        p.setCursor(Cursor.WAIT);
        return new Scene(p, 630, 300);        
    }
    
	@Override
	public void init() throws Exception{
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		 this.stage = stage;
	     stage.setScene(createPreloaderScene());   
	     stage.show();
		
	}
	
	@Override
	public void handleProgressNotification(ProgressNotification info) {
		
	}
	
	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.close();
            
        }
	}
}
