package piccross;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.stage.Stage;


/**This class builds the piccross UI.
 * 
 * @author Ryanh
 *
 */
public class Game extends Application{
	private TextArea output = new TextArea();
	private Controller handle = new Controller();
	private GameLogic gameLogic = new GameLogic();
	private TextField timeField = new TextField();
	private ControllableTimer timer = new ControllableTimer(this);
	private CheckBox mark = new CheckBox("Mark");
	private Button[] buttonGrid = new Button[25];
	private TextField pointsField = new TextField();
	private HBox time = new HBox(); 
	private Label timeLabel = new Label("Time:");
	private HBox points = new HBox(); 
	private Label pointsLabel = new Label("Points:");
	private MenuBar menuBar = new MenuBar();
	private Group root = new Group();
	private Scene scene = new Scene(root, 923, 877);
	private GridPane grid = new GridPane();
	private Button reset = new Button("Reset");
	private HBox titleArea = new HBox();
	private Label title = new Label("PicCross");
	private GridPane gameGrid = new GridPane();
	private VBox optionButtons = new VBox();
	private VBox vHints = new VBox();
	private HBox hHints = new HBox();
	private VBox scoreBoard = new VBox();
	private VBox controlPanel = new VBox();
	private Label[] vHint = new Label[5];
	private Label[] hHint = new Label[5];
	private Image logoMin = new Image("file:piccrossNameMin.jpg");
	private Image wrongChoiceImage = new Image("file:xmark.png");
	
	private Image newGameImage = new Image("file:piciconnew.gif");
	private ImageView newGameView = new ImageView(newGameImage);
	private Image exitGameImage = new Image("file:piciconext.gif");
	private ImageView exitGameView = new ImageView(exitGameImage);
	private Image solutionImage = new Image("file:piciconabt.gif");
	private ImageView solutionView = new ImageView(solutionImage);
	private Image winnerImage = new Image("file:gamepicwinner.png");
	private ImageView winnerView = new ImageView(winnerImage);
	private Image endImage = new Image("file:gamepicend.png");
	private ImageView endView = new ImageView(endImage);
	private MenuItem newGame = new MenuItem("New",newGameView);
	
	private MenuItem debug1 = new MenuItem("Debug1");
	private MenuItem debug2 = new MenuItem("Debug2");
	private MenuItem debug3 = new MenuItem("Debug3");
	private Menu debug = new Menu("Debug",null,debug1,debug2,debug3);
	private MenuItem exit = new MenuItem("Exit",exitGameView);
	private Menu gameMenu = new Menu("Game",null, newGame, debug, exit);
	private MenuItem solution = new MenuItem("Solution",solutionView);
	private MenuItem about = new MenuItem("About");
	private Menu helpMenu = new Menu("Help",null, solution, about);
	private Alert aboutAlert = new Alert(AlertType.INFORMATION);
	private Alert alert = new Alert(AlertType.INFORMATION);
	

	@Override
	public void init() throws Exception{
		//int waitTime = (int)(5000+(Math.random()*1500));
		int waitTime = 100;
		Thread.sleep(waitTime);
		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
	}
	
	public void winScreen() throws Exception{
		Stage winScreen = new Stage();
		winScreen.setTitle("Winner!");
		Button closeButton = new Button("Close");
		Group root = new Group();
		Scene scene = new Scene(root, 923, 877);
		
		VBox winMenu = new VBox();
		
		root.getChildren().add(winMenu);
		winMenu.getChildren().add(closeButton);
		
		winScreen.setScene(scene);
		winScreen.show();
	}
	/**Sets the time field with the elapsed time.
	 * 
	 * @param time The elapsed time since the timer started.
	 */
	public void setTime(int time) {
		timeField.setText(""+time);
	}
	
	
	public void setScore() {
		pointsField.setText(""+gameLogic.getScore());
	}
	/**
	 * 
	 * @param index
	 * @param answer
	 */
	public void updateGameGrid(int index, boolean answer) {
		if(!gameLogic.getGameOver()) {
			if(gameLogic.isClicked((int)(buttonGrid[index]).getUserData()) == 0) {
				gameLogic.updateCheck();
				if(answer == true) {
					if(getMark()) {
						buttonGrid[index].setStyle("-fx-background-color:WHITE;");
					} 
					else {
						buttonGrid[index].setStyle("-fx-background-color:BLACK;");
					}
					this.setScore();
				}else {
					ImageView wrongChoiceView = new ImageView(wrongChoiceImage);
					buttonGrid[index].setGraphic(wrongChoiceView);
					//buttonGrid[index].setStyle("-fx-background-color:RED;");
					gameLogic.setMistake();
				}
				if(gameLogic.checkWin()) {
					if(!gameLogic.getMistake()) {
						alert.setHeaderText(" ");
						alert.setGraphic(winnerView);
						alert.setTitle("Winner !");
						alert.showAndWait();
						gameLogic.setGameOver();
					}
					else {
						alert.setHeaderText(" ");
						alert.setGraphic(endView);alert.setTitle("Game Over :(");
						alert.showAndWait();
						gameLogic.setGameOver();
					}
				}
				if(gameLogic.checkOver()) {
					if(!gameLogic.checkWin()) {
						alert.setHeaderText(" ");
						alert.setGraphic(endView);alert.setTitle("Game Over :(");
						alert.showAndWait();
						gameLogic.setGameOver();
					}
				}

			}
			else {
				output.setText(output.getText() +"That square was already clicked\n");
			}
		}else {
			output.setText(output.getText() +"Game is over. Please Play new Game or reset\n");
		}
	}
	
	/**
	 * 
	 */
	public void setHints() {
		vHints.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;");
		
		String y;
		for(int i = 0; i < 5; i++) {
			y = "";
			StringBuilder x = new StringBuilder();
			for(int j = 0; j < gameLogic.getHHints(i).length(); j++){
				x.append(gameLogic.getHHints(i).charAt(j));
				x.append(" ");
				y = x.toString();
			}
			if(y=="") {y = "0";}
			vHint[i] = new Label(y);
			vHint[i].setPrefSize(115, 115);
			vHint[i].setAlignment(Pos.CENTER);
			vHint[i].setStyle("-fx-font-size:20px;");
			vHints.getChildren().add(vHint[i]);
		}
		
		//build horizontal hints bar
		
		hHints.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;");
		
		for(int i = 0; i < 5; i++) {
			y = "";
			StringBuilder x = new StringBuilder();
			for(int j = 0; j < gameLogic.getVHints(i).length(); j++){
				x.append(gameLogic.getVHints(i).charAt(j));
				x.append("\n");
				y = x.toString();
			}
			if(y=="") {y = "0";}
			hHint[i] = new Label(y);
			hHint[i].setPrefSize(115, 115);
			hHint[i].setAlignment(Pos.CENTER);
			hHint[i].setStyle("-fx-font-size:20px;");
			hHints.getChildren().add(hHint[i]);
		}
	}
	
	/**
	 * 
	 */
	public void solution() {
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 5; j++){
				if(gameLogic.checkGrid((i*5)+j)) {
					buttonGrid[(i*5)+j].setText("Valid Square");
				}
				else {
					buttonGrid[(i*5)+j].setText("Empty Square");
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void setGrid() {
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 5; j++){
				buttonGrid[(i*5)+j] = new Button();
				buttonGrid[(i*5)+j].setPrefHeight(115);
				buttonGrid[(i*5)+j].setPrefWidth(115);
				gameGrid.add(buttonGrid[(i*5)+j],i,j,1,1);
				
				buttonGrid[(i*5)+j].setUserData((j+(i*5)));
				buttonGrid[(i*5)+j].setId("gridButton");
				buttonGrid[(i*5)+j].setStyle("-fx-background-color:DARKGREY;");
				buttonGrid[(i*5)+j].setOnAction(handle);
				
			}
		}
	}
	/**
	 * 
	 */
	public void resetHints() {
			String y;
			for(int i = 0; i < 5; i++) {
				y = "";
				StringBuilder x = new StringBuilder();
				for(int j = 0; j < gameLogic.getHHints(i).length(); j++){
					x.append(gameLogic.getHHints(i).charAt(j));
					x.append(" ");
					y = x.toString();
				}
				if(y=="") {y = "0";}
				vHint[i].setText(y);
				
			}
			
			//build horizontal hints bar
			
			for(int i = 0; i < 5; i++) {
				y = "";
				StringBuilder x = new StringBuilder();
				for(int j = 0; j < gameLogic.getVHints(i).length(); j++){
					x.append(gameLogic.getVHints(i).charAt(j));
					x.append("\n");
					y = x.toString();
				}
				if(y=="") {y = "0";}
				hHint[i].setText(y);
			}
		}
	
	/**
	 * 
	 */
	public void newGame() {
		gameLogic.newGame();
		setGrid();
		timer.setStatus(ControllableTimer.RESET);
		gameLogic.resetScore();
		gameLogic.resetCheckGrid();
		resetHints();
		setScore();
	}
	
	public void debug() {
		setGrid();
		resetHints();
		setScore();
		timer.setStatus(ControllableTimer.RESET);
	}
	
	/**
	 * 
	 */
	public void exitGame() {
		timer.setStatus(ControllableTimer.TERMINATE);
		Platform.exit();
	}
	
	/**Get the status of the mark.
	 * 
	 * @return Returns Status of the mark
	 */
	public boolean getMark() {
		return mark.isSelected();
	}
	
	@Override
	/**
	 * The primary stage for the UI. 
	 */
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("PicCross");
		
		try {
			
			
			primaryStage.getIcons().add(logoMin);
		}catch(Exception e){
			System.err.println("Image not loaded");
		}
		
		gameLogic.newGame();
		//Menu
		
		newGame.setId("newGame");
		newGame.setOnAction(handle);
		
		KeyCombination newGameKC = new KeyCodeCombination(KeyCode.N, KeyCombination.ALT_DOWN);
		newGame.setAccelerator(newGameKC);

		
		
		debug.setId("debug");
		debug1.setId("debug1");
		debug2.setId("debug2");
		debug3.setId("debug3");
		debug1.setOnAction(handle);
		debug2.setOnAction(handle);
		debug3.setOnAction(handle);
		
		exit.setId("exit");
		exit.setOnAction(handle);
		
		
		solution.setId("solution");
		solution.setOnAction(handle);
		KeyCombination solutionKC = new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN);
		solution.setAccelerator(solutionKC);
		
		about.setId("about");
		about.setOnAction(handle);
		
	
		
		menuBar.getMenus().add(gameMenu);
		menuBar.getMenus().add(helpMenu);
		
		//colours
		Color aqua = Color.web("#94D4D1");
		//colours I will use the in the future
		//Color rose = Color.web("#FA8775");
		//Color lavender = Color.web("#C5A4CC");
		
		//image for the application Icon
		
		
		//base scene
		
		scene.setFill(aqua);
		
		//set up grid pane and add to scene
		
		root.getChildren().add(grid);
		
		//option buttons
		
		mark.setUserData(mark.getText());
		//mark.setOnAction(value -> output.setText((String) mark.getClass().toString()));
		mark.setOnAction(handle);
		
		reset.setUserData(reset.getText());
		reset.setId("reset");
		reset.setOnAction(handle);
		
		
		output.setWrapText(true);

		
		//Game BoardL build game board with buttons
		
		gameGrid.setPrefWidth(600);
		gameGrid.setPrefHeight(600);
		gameGrid.setHgap(5);
		gameGrid.setVgap(5);
		gameGrid.setStyle("-fx-background-color:#C5A4CC;" + "-fx-border-color:BLACK;");
		
		
		
		
		//build the title bar
		
		titleArea.setPrefSize(600, 115);
		titleArea.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;" + "-fx-font-size:40px;");
		titleArea.setAlignment(Pos.CENTER);
		
		//builds the options box
		
		
		//Build vertical hints bar
		
		
		
		
		//builds the score board.
		
		scoreBoard.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;");
		
		
		
		//builds the control panel
		
		controlPanel.setPrefSize(85,600);
		output.setPrefSize(85, 600);
		
		//add menu
		//HBox menuBox = new HBox(menuBar);
		grid.add(menuBar,0,0,11,1);
		
		//Adding game areas to grid
		grid.setVgap(5);
		grid.setHgap(5);
		grid.add(titleArea,0,1,11,1);
		titleArea.getChildren().add(title);
		grid.add(optionButtons,0,2,1,1);
		optionButtons.getChildren().add(mark);
		optionButtons.getChildren().add(reset);
		grid.add(vHints,0,3,1,8);
		grid.add(hHints,1,2,8,1);
		grid.add(gameGrid,1,3,8,8);
		grid.add(scoreBoard,10,2,1,1);
		scoreBoard.getChildren().add(points);
		points.getChildren().add(pointsLabel);
		points.getChildren().add(pointsField);
		scoreBoard.getChildren().add(time);
		time.getChildren().add(timeLabel);
		time.getChildren().add(timeField);
		grid.add(controlPanel,10,3,1,8);
		controlPanel.getChildren().add(output);
		
		
		gameLogic.printGrid();
		this.setHints();
		this.setGrid();
		//gameLogic.generateHints();
		
		
		timer.start();
			
		timer.setStatus(ControllableTimer.START);
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		
	}
	/**The class holds the handler for game grids action events. 
	 * 
	 * @author Ryanh
	 *
	 */
	public class Controller implements EventHandler<ActionEvent>  {

		@Override
		/**When button is activated output text to the control Panel
		 * 
		 */
		public void handle(ActionEvent e) {
			
			if(e.getSource() instanceof CheckBox) {
				System.out.println("Tests");
			}
			else if(e.getSource() instanceof Button) {
				String buttonType = ((Button)e.getSource()).getId();
				switch(buttonType) {
				case "reset":
					setGrid();
					timer.setStatus(ControllableTimer.RESET);
					gameLogic.resetGame();
					setScore();
					System.out.println("reset");
					break;
				case "gridButton":
					System.out.println(((Button)e.getSource()).getUserData());
					System.out.println("grid");
					if(getMark()) {
						updateGameGrid((int)((Button)e.getSource()).getUserData(),gameLogic.markGrid((int)((Button)e.getSource()).getUserData()));
						gameLogic.clickedGrid((int)((Button)e.getSource()).getUserData());
					} else {
						updateGameGrid((int)((Button)e.getSource()).getUserData(),gameLogic.checkGrid((int)((Button)e.getSource()).getUserData()));
						gameLogic.clickedGrid((int)((Button)e.getSource()).getUserData());
					}
					break;
				default:
					System.out.println("No button set up");
					break;
				}
			}
			else if(e.getSource() instanceof MenuItem) {
				String menuType = ((MenuItem)e.getSource()).getId();
				switch(menuType) {
				case "newGame":
					System.out.println("newgame");
					newGame();
					break;
				case "debug1":
					gameLogic.debug1();
					debug();
					System.out.println("debug");
					break;
				case "debug2":
					gameLogic.debug2();
					debug();
					System.out.println("debug");
					break;
				case "debug3":
					gameLogic.debug3();
					debug();
					System.out.println("debug");
					break;
				case "exit":
					exitGame();
					System.out.println("exit");
					break;
				case "about":
					aboutAlert.setTitle("About");
					aboutAlert.setHeaderText("Piccross-with-two-c ");
					aboutAlert.setContentText("By: Ryan Heffernan\n"
							+ "Winter Term");
					aboutAlert.showAndWait();
					System.out.println("about");
					break;
				case "solution":
					solution();
					System.out.println("solution");
					break;
				default:
					System.out.println("No button set up" + e.getSource().toString());
					break;
				}
			}
			//Buttnon sourceButton=(Button)e.getSource();
			//String buttonOutput=((Button)e.getSource()).getId();
			//boolean x = (buttonOutput == e.getSource());
			//output.setText(buttonOutput);
			
			
			
		}

	}
	/**Main Program, it Launches the Piccross application.
	 * 
	 * @param args Arguments from the command line
	 */
	public static void main(String[] args) {
		System.setProperty("javafx.preloader", GamePreloader.class.getCanonicalName());
		Application.launch(args);
		

	}

}
