import java.util.Scanner;
import common.ChatIF;

public class ServerConsole implements ChatIF {

    private static final int DEFAULT_PORT = 5555;
    Scanner fromConsole;
    EchoServer server;

    public ServerConsole(int port){
        server = new EchoServer(port,this);
        fromConsole = new Scanner(System.in);
    }

    @Override
    public void display(String message) {
        System.out.println("SERVER MSG>" +message);
    }

    public void accept()
  {
    try
    {
      String message;
      while (true)
      {
        message = fromConsole.nextLine();
        server.handleMessageFromServerUI(message);
        //server.sendToAllClients("SERVER MSG>"+message);
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

    public static void main(String[] args)
    {
      int port = 0; //Port to listen on
      try
      {
        port = Integer.parseInt(args[0]); //Get port from command line
      }
      catch(Throwable t)
      {
        port = DEFAULT_PORT; //Set port to 5555
      }
      ServerConsole s = new ServerConsole(port);

      try
      {
        s.server.listen(); //Start listening for connections
        s.accept();
      }
      catch (Exception ex)
      {
        System.out.println("ERROR - Could not listen for clients!");
      }
    }
}
