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




public class Conn implements Runnable 
{
    private Socket clientSocket;
    String clientSelection;
    private BufferedReader in = null;
    public Conn(Socket client) 
    {
        this.clientSocket = client;
    }
    @Override
    public void run() 
    {
        try 
	{
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while ((clientSelection = in.readLine()) != null) 
	    {
                switch (clientSelection) 
		{
                    case "1":
                   	receiveFile();
                        break;
                    case "2":
                        String outGoingFileName;
                        while ((outGoingFileName = in.readLine()) != null) 
			{
                            sendFile(outGoingFileName);
                        }

                        break;
                    default:
                        System.out.println("Incorrect command received.");
                        break;
                }
                in.close();
                break;
            }

        } 
	catch (IOException ex) 
	{
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void receiveFile() 
{
        try 
	{
            int bytesRead;
	    DataInputStream clientData = new DataInputStream(clientSocket.getInputStream());
	    String fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(("received_from_client_" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
	    {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            output.close();
            clientData.close();
	    System.out.println("File "+fileName+" received from client.");
        } 
	catch (IOException ex) 
	{
            System.err.println("Client error. Connection closed.");
        }
}
public void sendFile(String fileName) 
{
        try 
	{
            
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
	    FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);
            OutputStream os = clientSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File "+fileName+" sent to client.");
        } 
	catch (Exception e) 
	{
            System.err.println("File does not exist!");
        } 
    }
}
