import org.json.JSONArray;
import org.json.JSONObject;
import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 */
class SockClient {
  static Socket sock = null;
  static String host = "localhost";
  static int port = 8888;
  static OutputStream out;
  // Using and Object Stream here and a Data Stream as return. Could both be the same type I just wanted
  // to show the difference. Do not change these types.
  static ObjectOutputStream os;
  static DataInputStream in;
  public static void main (String args[]) {

    if (args.length != 2) {
      System.out.println("Expected arguments: <host(String)> <port(int)>");
      System.exit(1);
    }

    try {
      host = args[0];
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException nfe) {
      System.out.println("[Port|sleepDelay] must be an integer");
      System.exit(2);
    }

    try {
      connect(host, port); // connecting to server
      System.out.println("Client connected to server.");
      boolean requesting = true;
      while (requesting) {
        System.out.println("What would you like to do: 1 - echo, 2 - add, 3 - addmany, 4 - roller, 5 - inventory (0 to quit)");
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine());
        // You can assume the user put in a correct input, you do not need to handle errors here
        // You can assume the user inputs a String when asked and an int when asked. So you do not have to handle user input checking
        JSONObject json = new JSONObject(); // request object
        switch(choice) {
          case 0:
            System.out.println("Choose quit. Thank you for using our services. Goodbye!");
            requesting = false;
            break;
          case 1:
            System.out.println("Choose echo, which String do you want to send?");
            String message = scanner.nextLine();
            json.put("type", "echo");
            json.put("data", message);
            break;
          case 2:
            System.out.println("Choose add, enter first number:");
            String num1 = scanner.nextLine();
            json.put("type", "add");
            json.put("num1", num1);

            System.out.println("Enter second number:");
            String num2 = scanner.nextLine();
            json.put("num2", num2);
            break;
          case 3:
            System.out.println("Choose addmany, enter as many numbers as you like, when done choose 0:");
            JSONArray array = new JSONArray();
            String num = "1";
            while (!num.equals("0")) {
              num = scanner.nextLine();
              array.put(num);
              System.out.println("Got your " + num);
            }
            json.put("type", "addmany");
            json.put("nums", array);
            break;
        }
        if(!requesting) {
          continue;
        }

        // write the whole message
        os.writeObject(json.toString());
        // make sure it wrote and doesn't get cached in a buffer
        os.flush();

        // handle the response
        // - not doing anything other than printing payload
        // !! you will most likely need to parse the response for the other 2 services!
        String i = (String) in.readUTF();
        JSONObject res = new JSONObject(i);
        System.out.println("Got response: " + res);
        if (res.getBoolean("ok")){
          if (res.getString("type").equals("echo")) {
            System.out.println(res.getString("echo"));
          } else {
            System.out.println(res.getInt("result"));
          }
        } else {
          System.out.println(res.getString("message"));
        }
      }
      // want to keep requesting services so don't close connection
      //overandout();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void overandout() throws IOException {
    //closing things, could
    in.close();
    os.close();
    sock.close(); // close socked after sending
  }

  public static void connect(String host, int port) throws IOException {
    // open the connection
    sock = new Socket(host, port); // connect to host and socket on port 8888

    // get output channel
    out = sock.getOutputStream();

    // create an object output writer (Java only)
    os = new ObjectOutputStream(out);

    in = new DataInputStream(sock.getInputStream());
  }
}