package Assign32starter;

import java.awt.Dimension;

import org.json.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * The ClientGui class is a GUI frontend that displays an image grid, an input text box,
 * a button, and a text area for status. 
 * 
 * Methods of Interest
 * ----------------------
 * show(boolean modal) - Shows the GUI frame with current state
 *     -> modal means that it opens GUI and suspends background processes. 
 * 		  Processing still happens in the GUI. If it is desired to continue processing in the 
 *        background, set modal to false.
 * newGame(int dimension) - Start a new game with a grid of dimension x dimension size
 * insertImage(String filename, int row, int col) - Inserts an image into the grid
 * appendOutput(String message) - Appends text to the output panel
 * submitClicked() - Button handler for the submit button in the output panel
 * 
 * Notes
 * -----------
 * > Does not show when created. show() must be called to show he GUI.
 * 
 */
public class ClientGui implements Assign32starter.OutputPanel.EventHandlers {
	JDialog frame;
	PicturePanel picPanel;
	OutputPanel outputPanel;
	String currentMess;

	Socket sock;
	OutputStream out;
	ObjectOutputStream os;
	BufferedReader bufferedReader;

	// TODO: SHOULD NOT BE HARDCODED change to spec
	String host = "localhost";
	int port = 9000;

	/**
	 * Construct dialog
	 * @throws IOException 
	 */
	public ClientGui(String host, int port) throws IOException {
		this.host = host; 
		this.port = port; 
	
		frame = new JDialog();
		frame.setLayout(new GridBagLayout());
		frame.setMinimumSize(new Dimension(500, 500));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// setup the top picture frame
		picPanel = new PicturePanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.25;
		frame.add(picPanel, c);

		// setup the input, button, and output area
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.75;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		outputPanel = new OutputPanel();
		outputPanel.addEventHandlers(this);
		frame.add(outputPanel, c);

		picPanel.newGame(1);
		insertImage("img/Berlin1.png", 0, 0);

		open(); // opening server connection here
		currentMess = "{'type': 'start'}"; // very initial start message for the connection
		try {
			os.writeObject(currentMess);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String string = this.bufferedReader.readLine();
		System.out.println("Got a connection to server");
		JSONObject json = new JSONObject(string);
		outputPanel.appendOutput(json.getString("value")); // putting the message in the outputpanel

		// reading out the image (abstracted here as just a string)
		System.out.println("Pretend I got an image: " + json.getString("image"));
		/// would put image in picture panel
		close(); //closing the connection to server

		// Now Client interaction only happens when the submit button is used, see "submitClicked()" method
	}

	/**
	 * Shows the current state in the GUI
	 * @param makeModal - true to make a modal window, false disables modal behavior
	 */
	public void show(boolean makeModal) {
		frame.pack();
		frame.setModal(makeModal);
		frame.setVisible(true);
	}

	/**
	 * Creates a new game and set the size of the grid 
	 * @param dimension - the size of the grid will be dimension x dimension
	 * No changes should be needed here
	 */
	public void newGame(int dimension) {
		picPanel.newGame(1);
		outputPanel.appendOutput("Started new game with a " + dimension + "x" + dimension + " board.");
	}

	/**
	 * Insert an image into the grid at position (col, row)
	 * 
	 * @param filename - filename relative to the root directory
	 * @param row - the row to insert into
	 * @param col - the column to insert into
	 * @return true if successful, false if an invalid coordinate was provided
	 * @throws IOException An error occured with your image file
	 */
	public boolean insertImage(String filename, int row, int col) throws IOException {
		System.out.println("Image insert");
		String error = "";
		try {
			// insert the image
			if (picPanel.insertImage(filename, row, col)) {
				// put status in output
				outputPanel.appendOutput("Inserting " + filename + " in position (" + row + ", " + col + ")"); // you can of course remove this
				return true;
			}
			error = "File(\"" + filename + "\") not found.";
		} catch(PicturePanel.InvalidCoordinateException e) {
			// put error in output
			error = e.toString();
		}
		outputPanel.appendOutput(error);
		return false;
	}

	/**
	 * Submit button handling
	 * 
	 * TODO: This is where your logic will go or where you will call appropriate methods you write. 
	 * Right now this method opens and closes the connection after every interaction, if you want to keep that or not is up to you. 
	 */
	@Override
	public void submitClicked() {
		try {
		open(); // opening a server connection again
		System.out.println("submit clicked ");

		// Pulls the input box text
		String input = outputPanel.getInputText();

		// TODO evaluate the input from above and create a request for client. 

		// send request to server
		try {
			  os.writeObject("Blub"); // this will crash the server, since it is not a JSON and thus the server will not handle it. 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// wait for an answer and handle accordingly
		try {
			System.out.println("Waiting on response");
			String string = this.bufferedReader.readLine();
			System.out.println(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Key listener for the input text box
	 * 
	 * Change the behavior to whatever you need
	 */
	@Override
	public void inputUpdated(String input) {
		if (input.equals("surprise")) {
			outputPanel.appendOutput("You found me!");
		}
	}

	public void open() throws UnknownHostException, IOException {
		this.sock = new Socket(host, port); // connect to host and socket

		// get output channel
		this.out = sock.getOutputStream();
		// create an object output writer (Java only)
		this.os = new ObjectOutputStream(out);
		this.bufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

	}
	
	public void close() {
        try {
            if (out != null)  out.close();
            if (bufferedReader != null)   bufferedReader.close(); 
            if (sock != null) sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) throws IOException {
		// create the frame


		try {
			String host = "localhost";
			int port = 8888;


			ClientGui main = new ClientGui(host, port);
			main.show(true);


		} catch (Exception e) {e.printStackTrace();}



	}
}
