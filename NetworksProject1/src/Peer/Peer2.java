package Peer;

import java.io.*;
import java.net.*;

import Crypto.CryptoUtils;

public class Peer2  extends Thread {
	
	// Instances
	ServerSocket serverSocket;
	Boolean isOnline;
	String received;
	String[] message;
	
	// Initializer
	public Peer2(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		isOnline = true;
	}
	
	public void disconnect() {
		isOnline = false;
	}
	
	public void run() {
		while(isOnline) {
			try {
				Socket socket = serverSocket.accept();
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				DataInputStream din = new DataInputStream(socket.getInputStream());
				DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
				
				received = inputStream.readLine();
				System.out.println(received);
				message = received.split("\\s");
				
				if (message[0].equals("Download:")) {
					String filename = message[1];
					System.out.println("Sending file " + filename);
					dout.writeUTF(filename);
					dout.flush();
					
					File file = new File(System.getProperty("user.dir") + "/sharedFiles/client1/" + filename);
                    FileInputStream fin = new FileInputStream(file);
                    long sz = (int) file.length();

//                    String key = "we need A grades";
//                    try {
//                    	CryptoUtils.encrypt(key, file, file);
//                    } catch (Exception e) {
//                    	System.out.println("Error encrypting: " + e);
//                    }
                    
                    byte b[] = new byte [1024];

                    int read;

                    dout.writeUTF(Long.toString(sz));

                    System.out.println ("Size: "+sz);
                    System.out.println ("Buf size: " + socket.getReceiveBufferSize());

                    while((read = fin.read(b)) != -1){
                        dout.write(b, 0, read);
                        dout.flush();
                    }

                    dout.close();		
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
