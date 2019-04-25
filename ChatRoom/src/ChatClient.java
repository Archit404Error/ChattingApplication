

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import java.io.OutputStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ChatClient{
	Socket client;
	InputStream input;
	PrintWriter writer;
	
	public ChatClient() throws IOException{
		
		client = new Socket("localhost", 4444);
		input = client.getInputStream();
        System.out.println("Established connection:" + client);
	}
	
	public BufferedReader getReader() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		return reader;
	}	
	
	public PrintWriter getWriter() throws IOException {
		OutputStream output = client.getOutputStream();
        writer = new PrintWriter(output, true);
        return writer;   
	}
        
		
}
