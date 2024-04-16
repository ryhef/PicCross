package piccross;

import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**This is the prompt that gets the connection information from the user. It collects the information of the requested server address and port number and the name it will send to the server.
 * 
 * @author Ryanh
 *
 */
public class PiccrossNetworkModalJFX{
	private Controller handle = new Controller();
	private Optional<ButtonType> response;
	private VBox content = new VBox();
	private HBox row1 = new HBox();
	private HBox row2 = new HBox();
	private HBox row3 = new HBox();
	private HBox row4 = new HBox();
	private HBox row5 = new HBox();
	private HBox row6 = new HBox();
	private TextField addressField = new TextField();
	private TextField nameField = new TextField();
	private ComboBox<String> portField = new ComboBox<String>();
	private Label address = new Label("_Addess: ");
	private Label port = new Label("  _Port: ");
	private Label name = new Label("  _Name: ");
	private Label addressErr = new Label(" ");
	private Label portErr = new Label(" ");
	private Label nameErr = new Label(" ");
	private ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OTHER);
	private ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

	private boolean hasConnected = false;
	private int portnum = -1;

	/**Builds the UI for the connection prompt.
	 * 
	 */
	public PiccrossNetworkModalJFX() {

		//Port combobox
		portField.getItems().addAll(new String[] {"","32150","42150","52150"});
		portField.setEditable(true);
		//address label
		address.setLabelFor(addressField);
		address.setMnemonicParsing(true);
		//port label
		port.setLabelFor(portField);
		port.setMnemonicParsing(true);
		//name label
		name.setLabelFor(nameField);
		name.setMnemonicParsing(true);
		//label error formatting
		addressErr.setTextFill(Color.RED);
		portErr.setTextFill(Color.RED);
		nameErr.setTextFill(Color.RED);

		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Network Connection");
		//Rows on the UI
		row1.getChildren().add(address);
		row1.getChildren().add(addressField);

		row2.getChildren().add(addressErr);

		row3.getChildren().add(port);
		row3.getChildren().add(portField);

		row4.getChildren().add(portErr);

		row5.getChildren().add(name);
		row5.getChildren().add(nameField);

		row6.getChildren().add(nameErr);

		content.getChildren().add(row1);
		content.getChildren().add(row2);
		content.getChildren().add(row3);
		content.getChildren().add(row4);
		content.getChildren().add(row5);
		content.getChildren().add(row6);


		//buttons on the UI
		dialog.getDialogPane().setContent(content);
		dialog.getDialogPane().getButtonTypes().add(connectButtonType);
		dialog.getDialogPane().getButtonTypes().add(cancelButtonType);
		//Button action
		final Button checkConnectButton = (Button) dialog.getDialogPane().lookupButton(connectButtonType);
		checkConnectButton.addEventFilter(ActionEvent.ACTION, event -> {
			if(!checkConnection()) {
				if (!hasConnected) {
					event.consume();
				}
			}
		});
		//show UI until it is responded to.
		response = dialog.showAndWait();
		handle.handleDialog(response);

	}

	/**Verifies that the port number entered in the UI is a valid port Number.
	 * 
	 * @return The port number entered or an error number
	 */
	public int getPort() {

		try
		{
			portnum = Integer.parseInt((String)portField.getValue());
		}
		catch(NumberFormatException nfe)
		{
			portnum = -1;
		}
		if (portnum>9999 && portnum<65536)
		{
			return portnum;
		}
		else return -2;

		//return -1;
	}

	/** Getter for the address field
	 * 
	 * @return The address of the server
	 */
	public String getAddress() {
		return addressField.getText();
	}

	/**Getter for the name field
	 * 
	 * @return Name the user wants on the server
	 */
	public String getName() {
		return nameField.getText();
	}

	/**Ensures all the info entered on the UI is valid before the prompt closes. 
	 * 
	 * @return returns if the user has valid connection info.
	 */
	public boolean checkConnection() {
		hasConnected=true;
		nameErr.setText(" ");
		portErr.setText(" ");
		addressErr.setText(" ");

		int x=getPort();

		if (x==-1) //non-integer input.
		{
			portErr.setText("The port must be an integer.");
			hasConnected=false;
		}
		else if (x==-2)//out-of-range input
		{
			portErr.setText("Port must be >10000 and <65535.");
			hasConnected=false;
		}

		if (getName().length()<3)//Insufficient name length
		{
			nameErr.setText("Name too short.");
			hasConnected=false;
		}

		if (getAddress().length()<1)//Blank addresses.  Not much other testing can be done here.
		{
			addressErr.setText("The address must not be blank.");
			hasConnected=false;
		}

		return hasConnected;
	}
	/**Controller for the buttons on the UI. Configured to do nothing at the moment but are needed to handle the button events.
	 * 
	 * @author Ryanh
	 *
	 */
	public class Controller implements EventHandler<ActionEvent>  {

	
		public void handleDialog(Optional<ButtonType> b) {
			if(b.get().getText().equals("Connect")) {

			}
			else if(b.get().getText().equals("Cancel")){

			}
		
			else {

				System.out.println("No Configuration for this button.");
			}
		}

		@Override
		/**Needed for the controller instance but no buttons use it at the moment.
		 * 
		 */
		public void handle(ActionEvent arg0) {

		}
	}


}
