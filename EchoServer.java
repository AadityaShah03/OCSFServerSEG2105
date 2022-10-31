// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  public static final int DEFAULT_PORT = 5555;
  ChatIF s;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ServerConsole serverConsole) {
    super(port);
    s = serverConsole;
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String message = msg.toString().trim();

    if (client.getInfo("uid") == null &&
        (message.replace(" ", "").toCharArray())[0] == '#'
        && (message.toLowerCase().contains("#login"))) {
          s.display("Message received: " +message+ " from " + client.getInfo("uid"));
          client.setInfo("uid", (message.split(" "))[1]);
          s.display(((message.split(" "))[1])+" logged in");
          this.sendToAllClients(((message.split(" "))[1])+ " has logged in");
    } else {
      if ((message.replace(" ", "").toCharArray())[0] == '#' && (message.toLowerCase().contains("#login"))) {
        try {
          client.close();
        } catch (IOException e) {
          s.display("Error closing client");
        }
      } else {
        s.display("Message received: " + message + " from " + client.getInfo("uid"));
        this.sendToAllClients(client.getInfo("uid") + ">" + message);
      }
    }
  }

  public void handleMessageFromServerUI(String msg) {
    try {
      String message = msg.trim();
      if ((message.replace(" ", "").toCharArray())[0] == '#') {

        if (message.equalsIgnoreCase("#quit")) {
          this.stopListening();
          this.close();
          System.exit(0);
        }

        else if (message.toLowerCase().contains("#start")) {
          if (this.isListening()) {
            s.display("Server is already listening for new clients");
          } else {
            this.listen();
          }
        }

        else if (message.equalsIgnoreCase("#stop")) {
          if (this.isListening()) {
            this.stopListening();
          } else {
            s.display("Server is not listening for new clients");
          }
        }

        else if (message.equalsIgnoreCase("#close")) {
          if (this.isListening()) {
            this.stopListening();
          }
          this.close();
        }

        else if (message.toLowerCase().contains("#setport")) {
          if (!this.isListening() && this.getNumberOfClients() == 0) {
            this.setPort(Integer.parseInt((message.split(" "))[1]));
            s.display("Port set to " + ((message.split(" "))[1]));
          } else {
            s.display("You must close the server first");
          }
        }

        else if (message.equalsIgnoreCase("#getport")) {
          s.display(String.valueOf(this.getPort()));
        }
      } else {
        s.display(message);
        this.sendToAllClients("SERVER MSG>" + message);
      }
    } catch (Exception e) {
    }
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    s.display("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    s.display("Server has stopped listening for connections.");
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555
   *                if no argument is entered.
   */

  @Override
  public void clientConnected(ConnectionToClient client) {
    s.display("A new client has connected");
  }

  @Override
  public synchronized void clientDisconnected(ConnectionToClient client) {
    s.display(client.getInfo("uid")+" Discconnected");
  }

}
// End of EchoServer class
