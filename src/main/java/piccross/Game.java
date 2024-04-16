package piccross;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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


/**This class builds the piccross UI and contains the controller for the UI to interact with the game logic.
 * 
 * @author Ryanh
 *
 */
public class Game extends Application{
	//UI components
	private PiccrossNetworkModalJFX NetworkDialog;
	private TextArea chatPanel = new TextArea();
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
	private Scene scene = new Scene(root, 923, 927);
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
	private HBox termArea = new HBox();
	private Label termLabel = new Label("Terminal");
	private TextField terminal = new TextField();
	//Graphics
	private Image logoMin = new Image(getClass().getResourceAsStream("../images/piccrossNameMin.jpg"));
	private Image wrongChoiceImage = new Image(getClass().getResourceAsStream("../images/xmark.png"));
	private Image newGameImage = new Image(getClass().getResourceAsStream("../images/piciconnew.gif"));
	private ImageView newGameView = new ImageView(newGameImage);
	private Image exitGameImage = new Image(getClass().getResourceAsStream("../images/piciconext.gif"));
	private ImageView exitGameView = new ImageView(exitGameImage);
	private Image solutionImage = new Image(getClass().getResourceAsStream("../images/piciconabt.gif"));
	private ImageView solutionView = new ImageView(solutionImage);
	private Image winnerImage = new Image(getClass().getResourceAsStream("../images/gamepicwinner.png"));
	private ImageView winnerView = new ImageView(winnerImage);
	private Image endImage = new Image(getClass().getResourceAsStream("../images/gamepicend.png"));
	private ImageView endView = new ImageView(endImage);
	//Menus
	private MenuItem newGame = new MenuItem("New",newGameView);
	private MenuItem debug1 = new MenuItem("Debug1");
	private MenuItem debug2 = new MenuItem("Debug2");
	private MenuItem debug3 = new MenuItem("Debug3");
	private Menu debug = new Menu("Debug",null,debug1,debug2,debug3);
	private MenuItem exit = new MenuItem("Exit",exitGameView);
	private Menu gameMenu = new Menu("Game",null, newGame, debug, exit);
	private MenuItem connect = new MenuItem("Connect");
	private MenuItem disconnect = new MenuItem("Disconnect");
	private Menu netMenu = new Menu("Networking", null, connect, disconnect);
	private MenuItem solution = new MenuItem("Solution",solutionView);
	private MenuItem about = new MenuItem("About");
	private Menu helpMenu = new Menu("Help",null, solution, about);
	//Alerts
	private Alert aboutAlert = new Alert(AlertType.INFORMATION);
	private Alert alert = new Alert(AlertType.INFORMATION,"Would you like to upload your score?", ButtonType.YES, ButtonType.NO);
	//Networking
	private int portNumber;
	private String address;
	private String name;
	private ConnectionManager connection = new ConnectionManager();



	/**Run the preloader before the application starts
	 * 
	 */
	@Override
	public void init() throws Exception{
		int waitTime = (int)(5000+(Math.random()*1500));
		Thread.sleep(waitTime);
		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
	}


	/**Sets the time field with the elapsed time.
	 * 
	 * @param time The elapsed time since the timer started.
	 */
	public void setTime(int time) {
		timeField.setText(""+time);
	}

	public String getTime() {
		return timeField.getText();
	}

	/**Sets the current score on the score board.
	 * 
	 */
	public void setScore() {
		pointsField.setText(""+gameLogic.getScore());
	}

	public String getScore() {
		return pointsField.getText();
	}

	/** The method updates the game grid when the user clicks on a button. In non mark mode, If the user selects the right square it will turn black and the score will be updated. If incorrent it will have a red x on it.
	 * If it in mark mode, the square will turn white if the selection is valid and will have a red x if the selection in not valid.
	 * The method also checks to see if the user has ended the game on each click. It will put up an alert if the user has reach end of game conditions.
	 * 
	 * @param index This is the index of the button being pressed.
	 * @param answer This is whether the button is a valid choice or not depending on the mode.
	 */
	public void updateGameGrid(int index, boolean answer) {
		Optional<ButtonType> response;
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
					gameLogic.setMistake();
				}
				if(gameLogic.checkWin()) {
					if(!gameLogic.getMistake()) {
						alert.setHeaderText(" ");
						alert.setGraphic(winnerView);
						alert.setTitle("Winner !");
						response = alert.showAndWait();
						handle.handleDialog(response);
						gameLogic.setGameOver();
						timer.setStatus(ControllableTimer.STOP);

					}
					else {
						alert.setHeaderText(" ");
						alert.setGraphic(endView);
						alert.setTitle("Game Over :(");
						alert.showAndWait();
						gameLogic.setGameOver();
						timer.setStatus(ControllableTimer.STOP);
					}
				}
				if(gameLogic.checkOver()) {
					if(!gameLogic.checkWin()) {
						alert.setHeaderText(" ");
						alert.setGraphic(endView);
						alert.setTitle("Game Over :(");
						alert.showAndWait();
						gameLogic.setGameOver();
						timer.setStatus(ControllableTimer.STOP);
					}
				}

			}
			else {
				chatPanel.setText(chatPanel.getText() +"That square was already clicked\n");
			}
		}else {
			chatPanel.setText(chatPanel.getText() +"Game is over. Please Play new Game or reset\n");
		}
	}

	/**Sets both the vertical and horizontal hints on the UI.
	 * 
	 */
	public void setHints() {
		// Builds the vertical hints bar.
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

	/**Provides the solution to the user by writing valid or empty square on each button.
	 * 
	 */
	public void solution() {
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 5; j++){
				if(gameLogic.markGrid((i*5)+j)) {
					buttonGrid[(i*5)+j].setText("Empty Square");
				}
				else {
					buttonGrid[(i*5)+j].setText("Valid Square");
				}
			}
		}
	}

	/** Writes button grid to UI.
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

	/**Resets the hints on the board when a new game is made.
	 * 
	 */
	public void resetHints() {
		//reset the vertical hints bar
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
		//reset the horizontal hints bar
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

	/**Builds a new game for the user to play.
	 * 
	 */
	public void newGame() {
		gameLogic.newGame();
		setGrid();
		timer.setStatus(ControllableTimer.RESET);
		timer.setStatus(ControllableTimer.START);
		resetHints();
		setScore();
		chatPanel.setText("");
	}

	/**Resets the board for the user.
	 * 
	 */
	public void resetGame() {
		setGrid();
		timer.setStatus(ControllableTimer.RESET);
		timer.setStatus(ControllableTimer.START);
		gameLogic.resetGame();
		setScore();
		chatPanel.setText("");
	}

	/**Resets the board for the user when the game is gotten from the server.
	 * 
	 */
	public void resetGameFromServer() {
		setGrid();
		timer.setStatus(ControllableTimer.RESET);
		timer.setStatus(ControllableTimer.START);
		gameLogic.resetGameFromServer();
		setScore();
		chatPanel.setText("");
	}

	/**Allows the user to play the customer debug options.
	 * 
	 */
	public void debug() {
		setGrid();
		resetHints();
		setScore();
		timer.setStatus(ControllableTimer.RESET);
		timer.setStatus(ControllableTimer.START);
	}

	/**Exits the game gracefully when the exit menu option is chosen.
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
	
	/**Writes content from the terminal to the chat panel.
	 * 
	 * @param message message to be written.
	 */
	public void writeTerminal(String message) {
		chatPanel.setText(chatPanel.getText() + message);
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
		//new Game Loaded
		gameLogic.newGame();
		//Menus built
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
		connect.setId("connect");
		connect.setOnAction(handle);
		disconnect.setId("disconnect");
		disconnect.setOnAction(handle);
		disconnect.setDisable(true);

		solution.setId("solution");
		solution.setOnAction(handle);
		KeyCombination solutionKC = new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN);
		solution.setAccelerator(solutionKC);
		about.setId("about");
		about.setOnAction(handle);

		menuBar.getMenus().add(gameMenu);
		menuBar.getMenus().add(netMenu);
		menuBar.getMenus().add(helpMenu);
	

		//colours - Custome Background colour.
		Color aqua = Color.web("#94D4D1");

		//base scene
		scene.setFill(aqua);
		//set up grid pane and add to scene
		root.getChildren().add(grid);
		//option buttons
		mark.setUserData(mark.getText());
		mark.setOnAction(handle);
		reset.setUserData(reset.getText());
		reset.setId("reset");
		reset.setOnAction(handle);

		//Output text panel and points and score board
		chatPanel.setWrapText(true);
		chatPanel.setEditable(false);

		pointsField.setEditable(false);
		timeField.setEditable(false);

		//Game Board build game board with buttons
		gameGrid.setPrefWidth(600);
		gameGrid.setPrefHeight(600);
		gameGrid.setHgap(5);
		gameGrid.setVgap(5);
		gameGrid.setStyle("-fx-background-color:#C5A4CC;" + "-fx-border-color:BLACK;");

		//build the title bar
		titleArea.setPrefSize(600, 115);
		titleArea.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;" + "-fx-font-size:40px;");
		titleArea.setAlignment(Pos.CENTER);

		//builds the score board.
		scoreBoard.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;");

		//builds the control panel
		controlPanel.setPrefSize(85,600);
		chatPanel.setPrefSize(85, 600);

		//Builds terminal
		termLabel.setStyle("-fx-background-color:#FA8775;" + "-fx-border-color:BLACK;" + "-fx-font-size:15px;");
		terminal.setPrefWidth(537);
		terminal.setOnAction(handle);


		//add menu
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
		controlPanel.getChildren().add(chatPanel);
		termArea.getChildren().add(termLabel);
		termArea.getChildren().add(terminal);
		grid.add(termArea, 1, 11, 11, 1);


		//Sets hints and board according to game logic
		this.setHints();
		this.setGrid();

		//Makes timer start
		timer.setDaemon(true);
		timer.start();
		timer.setStatus(ControllableTimer.START);




		//Primary stage start
		primaryStage.setScene(scene);
		primaryStage.show();

	}



	/**The class holds the handler for game grids action events. 
	 * 
	 * @author Ryanh
	 *
	 */
	public class Controller implements EventHandler<ActionEvent>  {

		/**When a button is activated in the dialogue pane it is handled here.
		 * 
		 * @param b event being handled
		 */
		public void handleDialog(Optional<ButtonType> b) {
			if(b.get().getText().equals("Yes")) {
				if(NetworkDialog.checkConnection()) {
					connection.sendGame(gameLogic.getGame());
				}
			} 
			else if(b.get().getText().equals("Connect")) {

			}
			else if(b.get().getText().equals("Cancel")){

			}
			else if(b.get().getText().equals("No")){

			}
			else {

				System.out.println("No Configuration for this button.");
			}
		}

		@Override
		/**When button is activated, do the expected method of the button
		 * 
		 * @param e event being handled
		 */
		public void handle(ActionEvent e) {
			if(e.getSource() instanceof CheckBox) {

			}
			else if(e.getSource() instanceof Button) {
				String buttonType = ((Button)e.getSource()).getId();
				switch(buttonType) {
				case "reset":
					resetGame();
					break;
				case "gridButton":
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
					newGame();
					break;
				case "debug1":
					gameLogic.debug1();
					resetGame();
					debug();
					break;
				case "debug2":
					gameLogic.debug2();
					resetGame();
					debug();
					break;
				case "debug3":
					gameLogic.debug3();
					resetGame();
					debug();
					break;
				case "connect":
					//creates the network dialogue, ensure the information for connect is correct and then attempts to connect to the server.
					NetworkDialog = new PiccrossNetworkModalJFX();
					if(NetworkDialog.checkConnection()) {
						portNumber = NetworkDialog.getPort();
						address = NetworkDialog.getAddress();
						name = NetworkDialog.getName();
						disconnect.setDisable(false);
						connect.setDisable(true);
						connection.connection();
					}
					break;
				case "disconnect":
					connect.setDisable(false);
					disconnect.setDisable(true);
					connection.disconnet();
					break;
				case "exit":
					exitGame();
					break;
				case "about":
					aboutAlert.setTitle("About");
					aboutAlert.setHeaderText("Piccross-with-two-c ");
					aboutAlert.setContentText("By: Ryan Heffernan\n"
							+ "Winter Term");
					aboutAlert.showAndWait();
					break;
				case "solution":
					solution();
					break;
				default:
					System.out.println("No button set up" + e.getSource().toString());
					break;
				}
			}
			else if(e.getSource() instanceof TextField) {
				if(NetworkDialog.checkConnection()) {
					try {
						connection.sendMessage(((TextField)e.getSource()).getText().toString());
					}catch(Exception errorConnection) {
						System.out.println("Error");
					}
				} 
				else {
					chatPanel.setText(chatPanel.getText() +((TextField)e.getSource()).getText().toString() + "\n");
				}
				terminal.clear();
			}
		}

	}
	
/**Manages the connection between the server and the client.
 * 
 * @author Ryanh
 *
 */
	public class ConnectionManager {
		private Socket s = null;
		private ThreadedNetworkConnection r;
		private Thread t;
		
		/**Creates the connection to the server and starts a thread for it.
		 * 
		 */
		public void connection() {
			try
			{
				s = new Socket();
				s.connect(new InetSocketAddress(InetAddress.getByName(address),portNumber),10000);
				chatPanel.setText(chatPanel.getText() +"Connected to Server\n");
				r = new ThreadedNetworkConnection(s);
				t = new Thread(r);
				t.start();

			}
			catch(UnknownHostException ukh){

				chatPanel.setText(chatPanel.getText() +"Unable to connect to server. Check and see if server is up.\n");
				connect.setDisable(false);
				disconnect.setDisable(true);
			}
			catch(ConnectException ce){

				chatPanel.setText(chatPanel.getText() +"Unable to connect to server. Check and see if server is up.\n");
				connect.setDisable(false);
				disconnect.setDisable(true);
			}
			catch(EOFException f){

				chatPanel.setText(chatPanel.getText() +"Disconnected.\n");
				connect.setDisable(false);
				disconnect.setDisable(true);
			}catch(SocketException s){

				chatPanel.setText(chatPanel.getText() +"You have been disconnected from the server.\n");
				connect.setDisable(false);
				disconnect.setDisable(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		
		/**Disconnects from the server.
		 * 
		 */
		public void disconnet() {
			r.closeConnection();

		}
	
		/**Sends a message to the server.
		 * 
		 * @param message message to be sent
		 */
		public void sendMessage(String message) {
			r.sendData(message);
		}
		
		/**Sends a game to the server.
		 * 
		 * @param game The game to be sent.
		 */
		public void sendGame(int[][] game) {
			r.sendData("HighScorePushedToServer");
			r.sendGame(game);
		}
	}
	
	/**Creates a thread connection to the server.
	 * 
	 * @author Ryanh
	 *
	 */
	class ThreadedNetworkConnection implements Runnable{
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Socket connection;
		private String outputString;
		//private String input;
		
		/**Constructor for the threaded connection
		 * 
		 * @param socket the connection to be put in the thread.
		 */
		public ThreadedNetworkConnection(Socket socket) {
			connection = socket;
		}

		@Override
		/**The threaded connection running.
		 * 
		 */
		public void run() {
			String message = "";
			try
			{  
				output = new ObjectOutputStream(connection.getOutputStream());
				output.flush();
				input = new ObjectInputStream(connection.getInputStream());
				//Sends the name of the client to the server.
				sendData(name);
				//main client loop
				boolean done = false;
				while (!done)
				{  
					try {
						message = (String)input.readObject(); 
						switch(message) {
						case "GameToBePlayedFromServer":
							int[][] gotGame = (int[][])input.readObject();
							gameLogic.setGame(gotGame);
							Platform.runLater( new Runnable() {
								public void run() {
									resetGameFromServer();
									resetHints();
								}	
							});
							break;
						case "/bye":
							break;
						default:
							displayMessage(message);
							outputString = message;
						}
					}
					catch(ClassNotFoundException ce) {

					}

					if (message.equals("/bye"))
						done = true;
					Platform.runLater( new Runnable() {
						public void run() {
							connect.setDisable(false);
							disconnect.setDisable(true);
						}	
					});

				}


			}
			catch(UnknownHostException ukh){
				Platform.runLater( new Runnable() {
					public void run() {
						displayMessage("Unable to connect to server. Check and see if server is up.\n");
					}	
				});
			}
			catch(ConnectException ce){
				Platform.runLater( new Runnable() {
					public void run() {
						displayMessage("Unable to connect to server. Check and see if server is up.\n");
					}	
				});
			}
			catch(EOFException f){
				Platform.runLater( new Runnable() {
					public void run() {
						displayMessage("Disconnected.\n");
					}	
				});
			}catch(SocketException s){
				Platform.runLater( new Runnable() {
					public void run() {
						displayMessage("You have been disconnected from the server.\n");
					}	
				});
			}
			catch (Exception e)
			{  
				e.printStackTrace();
			}
			finally
			{
				closeConnection();
			}
		}
		
		/**Sends messages from the client to the server.
		 * 
		 * @param message message to be sent.
		 */
		private void sendData(String message) {
			try {
				output.writeObject(message);
				output.flush();
			}
			catch(IOException io) {

			}
		}
		
		/**Sends a game to the server.
		 * 
		 * @param game game to be sent
		 */
		public void sendGame(int[][] game) {
			try {
				output.writeObject(game);
				output.flush();
				output.writeObject(getScore());
				output.flush();
				output.writeObject(getTime());
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		/**Displays messages to the chat window.
		 * 
		 * @param messageGot Message to be displayed.
		 */
		private void displayMessage(final String messageGot) {
			writeTerminal(messageGot);
		}
		
		/**Closes the connection to the server.
		 * 
		 */
		private void closeConnection() {
			try {
				output.close();
				input.close();
				connection.close();
			}
			catch(IOException io) {}
		}
		
		/**Sets the output message
		 * 
		 * @return 
		 */
		
		public String getOutputString() {
			return outputString;
		}

		public void setOutputString(String outputString) {
			this.outputString = outputString;
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
