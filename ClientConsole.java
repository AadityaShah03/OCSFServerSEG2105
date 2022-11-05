// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF
{
  //Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;



  /**
   * Scanner to read from the console
   */
  Scanner fromConsole;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port,String uid)
  {
    if(uid.equals("")){
      System.out.println("ENTER A UID");
      System.exit(1);
    }

    try
    {
      client= new ChatClient(uid,host, port, this);
    }
    catch(IOException exception)
    {
      System.out.println("ERROR - Can't setup connection! Terminating client");
      System.exit(1);
    }

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in);
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {
      String message;
      while (true)
      {
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("> " + message);
  }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args)
  {
    String uid = "" ;
    String host = "";
    int port = DEFAULT_PORT;
    try
    {
      uid = args[0];
      host = args[1];
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException|NumberFormatException e)
    {
      try{
      uid = args[0];
      host = "localhost";
      port = DEFAULT_PORT;
      }
      catch(ArrayIndexOutOfBoundsException f){
        System.out.println("ERROR - No login ID specified.  Connection aborted");
        System.exit(1);
      }
    }//TODO

    ClientConsole chat= new ClientConsole(host, port,uid);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
