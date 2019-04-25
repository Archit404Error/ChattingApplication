import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatServer extends Thread {
	ServerSocket server;
	ArrayList<ServerThread> clients = new ArrayList<ServerThread>();
	HashMap<String, ServerThread> names = new HashMap<String, ServerThread>();

	public ChatServer() {
		try {
			server = new ServerSocket(4444);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startServer() throws IOException {
		while (true) {
			Socket client = server.accept();

			System.out.println("Creating Client Socket: " + client);
			ServerThread s = new ServerThread(client);
			clients.add(s);
			s.start();

		}
	}

	public class ServerThread extends Thread {
		private Socket socket;
		public PrintWriter writer;

		public ServerThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));

				OutputStream output = socket.getOutputStream();
				writer = new PrintWriter(output, true);

				String serverName = "";

				String text;
				int read = 0;
				do {
					text = reader.readLine();
					if (text.length() >= 6) {
						if (text.substring(0, 5).equals("UCon:")) {
							names.put(text.substring(6), this);
							for(ServerThread st : clients) {
								st.writer.println(names.keySet());
							}
						}
					}
					String reverseText = new StringBuilder(text).toString();

					if (reverseText.contains("@")) {
						writeToOneChatter(reverseText);
					} else {
						writeToAllChatters(reverseText);
					}
				} while (!text.equals("bye"));

				socket.close();
			} catch (IOException ex) {
				System.out.println("Server exception: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	public void writeToAllChatters(String s) {
		for(ServerThread st : clients) {
			st.writer.println(s);
		}
	}
	
	public void writeToOneChatter(String reverseText) {
		int start = reverseText.indexOf(";");
		String receiver = reverseText.substring(reverseText.indexOf("@") + 1, start);
		String message = reverseText.substring(start + 1);
		System.out.println(receiver + " " + message);
		names.get(reverseText.substring(1, reverseText.indexOf(":"))).writer.println("{Private}To " + receiver + ": " + message);
		names.get(receiver).writer.println("{Private}From " + reverseText.substring(1, reverseText.indexOf(":") + 1) + " " + message);
	}
	
	public static void main(String[] args) {
		try {
			ChatServer s = new ChatServer();
			s.startServer();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
