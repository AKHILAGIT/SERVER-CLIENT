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



public class Client 
{
private static Socket sock;
private static String fileName;
private static BufferedReader a;
private static PrintStream os;
private static int b;

public static void main(String[] args) throws IOException 
{
        try 
	{
            sock = new Socket("localhost", 1234);
            a = new BufferedReader(new InputStreamReader(System.in));
        } 
	catch (Exception e) 
	{
            System.err.println("Cannot connect to the server, try again later.");
            System.exit(1);
        }
        os = new PrintStream(sock.getOutputStream());
	try
	{ 
	   
	    System.out.println("1. Send file.");
            System.out.println("2. Recieve file.");
            System.out.print("\nMake selection: ");
	    b=Integer.parseInt(a.readLine());
            switch (b) 
	    {
            	case 1:
                	os.println("1");
                	sendFile();
                	break;
            	case 2:
                	os.println("2");
               		System.err.print("Enter file name: ");
                	fileName = a.readLine();
                	os.println(fileName);
                	receiveFile(fileName);
                	break;
            }
        } 
	catch (Exception e) 
	{
            	System.err.println("not valid input");
        }


        sock.close();
}
public static void sendFile() 
{
        try 
	{
            System.err.print("Enter file name: ");
            fileName = a.readLine();
	    File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
	    DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);
	    OutputStream os = sock.getOutputStream();
	    DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File "+fileName+" sent to Server.");
        } 
	catch (Exception e) 
	{
            System.err.println("File does not exist!");
        }
}
public static void receiveFile(String fileName) 
{
        try 
	{
            int bytesRead;
            InputStream in = sock.getInputStream();
            DataInputStream clientData = new DataInputStream(in);
            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(("received_from_server_" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
	    {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
	    output.close();
            in.close();
	    System.out.println("File "+fileName+" received from Server.");
        } 
	catch (IOException ex) 
	{
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
