// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;
  String UID;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String userID,String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.UID = userID;//set userid for late accesss

    openConnection();
    sendToServer("#login "+ this.UID);
  }

  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String msg)
  {
    String message = msg.trim();
    try {
      if((message.replace(" ","").toCharArray())[0]=='#'){

        if(message.equalsIgnoreCase("#quit")){
          quit();
        }

        else if(message.toLowerCase().contains("#login")){
          if(!this.isConnected()){
            try {
              clientUI.display("Logging in");
              this.UID = (message.split(" "))[1];
              sendToServer(msg);
              this.openConnection();
              clientUI.display("Logged in");
            }
            catch (ArrayIndexOutOfBoundsException e) {
              clientUI.display("Please enter a UID");
            }
          }
          else{
            clientUI.display("You are already logged in");
          }
        }

        else if(message.equalsIgnoreCase("#logoff")){
          if(this.isConnected()){
            clientUI.display("Logging off");
            this.closeConnection();
          }else{
            clientUI.display("You are already logged off");
          }
        }

        else if(message.toLowerCase().contains("#sethost")){
          if(this.isConnected()){
            this.setHost((message.split(" "))[1]);
            clientUI.display("Host set to "+ ((message.split(" "))[1]));
          }
          else{
            clientUI.display("You must login to set host");
          }
        }

        else if(message.toLowerCase().contains("#setport")){
          if(this.isConnected()){
            this.setPort(Integer.parseInt((message.split(" "))[1]));
            clientUI.display("Port set to "+ ((message.split(" "))[1]));
          }else{
            clientUI.display("You must login to set port");
          }
        }

        else if(message.equalsIgnoreCase("#gethost")){
          clientUI.display(this.getHost());
        }

        else if(message.equalsIgnoreCase("#getport")){
          clientUI.display(String.valueOf(this.getPort()));
        }
      }
      else{
        try
        {
          sendToServer(message);
        }
        catch(IOException e)
        {
          clientUI.display
            ("Could not send message to server.  Terminating client.");
          quit();
        }
      }
    } catch (ArrayIndexOutOfBoundsException|IOException|NumberFormatException e) {}
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  @Override
  public void connectionClosed() {
      clientUI.display("Connection closed");
  }

  @Override
  public void connectionException(Exception e) {
    clientUI.display("The server has shutdown");
    System.exit(0);
	}

}
//End of ChatClient class
