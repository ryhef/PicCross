package piccross;

import javafx.application.Preloader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
/**Creates the Preloader splash screen for the Piccross Game.
 * 
 * @author Ryanh
 *
 */
public class GamePreloader extends Preloader{
	private Stage stage;
	private Image splashScreenImage = new Image(getClass().getResourceAsStream("../images/piccross.png"));
	private ImageView splashScreenView = new ImageView(splashScreenImage);

	private Scene createPreloaderScene() {

		BorderPane p = new BorderPane();
		p.setCenter(splashScreenView);
		p.setCursor(Cursor.WAIT);
		return new Scene(p, 630, 300);        
	}
	
	/**
	 * Init for the Game Preloader
	 */
	@Override
	public void init() throws Exception{

	}
	
	/**Creates the UI for the preloader
	 * 
	 */
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setScene(createPreloaderScene());   
		stage.show();

	}
	
	/**
	 * Handles Progrss notification events from the main program.
	 */
	@Override
	public void handleProgressNotification(ProgressNotification info) {

	}
	
	/**
	 * Handles state change notification events from the main program.
	 */
	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
			stage.close();

		}
	}
}
