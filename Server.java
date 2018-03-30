import java.io.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Server 
{
	private static ServerSocket ss;
    	private static Socket cs = null;
	public static void main(String[] args) throws IOException {
	try {
            ss = new ServerSocket(1234);
            System.out.println("Waiting.......");
        } 
	catch (Exception e) 
	{
            System.err.println("Port already in use.");
            System.exit(1);
        }
	while (true) 
	{
                try 
		{
                	cs = ss.accept();
                	System.out.println("Connection Estabished:" + cs);
			Thread t = new Thread(new Conn(cs));
			t.start();

                } 
		catch (Exception e) 
		{
                	System.err.println("Error in connection attempt.");
            	}
        }
    }
}


