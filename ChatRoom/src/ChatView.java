import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.Border;

public class ChatView implements ActionListener {
	JFrame chat;
	JTextArea view, userList;
	JScrollPane viewPane;
	JTextField input;
	String name;
	ChatClient client;
	ChatReader cr;
	PrintWriter p;
	BufferedReader b;
	JPanel menu;
	JComboBox<String> color, mode, font;

	public ChatView(String name) throws IOException {
		chat = new JFrame("Java Chat!");
		chat.setSize(1000, 800);
		chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.name = name;

		client = new ChatClient();

		menu = new JPanel();
		color = new JComboBox<String>();
		color.addItem("Black");
		color.addItem("Blue");
		color.addItem("Red");
		color.addItem("White");
		color.addActionListener(this);

		mode = new JComboBox<String>();
		mode.addItem("Day Mode");
		mode.addItem("Night Mode");
		mode.addActionListener(this);

		font = new JComboBox<String>();
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		for(String s : fonts) {
			font.addItem(s);
		}
		font.addActionListener(this);
		
		menu.add(color);
		menu.add(mode);
		menu.add(font);

		p = client.getWriter();
		b = client.getReader();

		view = new JTextArea();
		view.setEditable(false);
		view.setSize(600, 600);
		view.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		view.setFont(new Font("Agency FB", Font.PLAIN, 30));
		view.setLineWrap(true);
		view.setText("");

		userList = new JTextArea();
		userList.setEditable(false);
		userList.setFont(new Font("Times New Roman", Font.ITALIC, 25));
		userList.setLineWrap(true);
		userList.setText("");
		userList.setSize(new Dimension(100, 300));

		viewPane = new JScrollPane(view);
		viewPane.setMinimumSize(new Dimension(800, 800));

		input = new JTextField();
		input.setEditable(true);
		input.setFont(view.getFont());
		input.setText("Enter Text to send to chat");
		input.setSize(60, chat.getHeight());
		input.addKeyListener(new MyKeyListener());

		chat.add(viewPane, BorderLayout.CENTER);
		chat.add(input, BorderLayout.SOUTH);
		chat.add(menu, BorderLayout.NORTH);
		chat.add(userList, BorderLayout.WEST);
		chat.setVisible(true);

		cr = new ChatReader();
		cr.start();
	}

	public PrintWriter getP() {
		return p;
	}

	class MyKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				viewPane.getVerticalScrollBar().setValue(viewPane.getVerticalScrollBar().getMaximum());
				
				if(!(input.getText().contains("@")))
					sendMessage(input.getText());
				else
					p.println(">" + name + ": " + input.getText());
				input.setText("");
				viewPane.getVerticalScrollBar().setValue(viewPane.getVerticalScrollBar().getMaximum());

				/*
				 * try { view.setText(b.readLine()); } catch (Exception e) {
				 * e.printStackTrace(); }
				 */
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	public void sendMessage(String text) {
		int load = (input.getText().length() - name.length()) / 2;
		load = Math.abs(load) + 2;
		printDash(load);
		p.print(name);
		printDash(load);

		p.println();
		p.print(text);
		p.println();
		
		printDash(load*2 + name.length());
		p.println();
	}

	public void printDash(int len) {
		for (int i = 0; i < len; i++) {
			p.print("-");
		}
	}

	class ChatReader extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					String text = b.readLine();
					System.out.println(text);
					if (!(text.equals(null))) {
						if (text.charAt(0) != '[') {
							view.setText(view.getText() + "\n" + text);
						} else {
							userList.setText("Users\n");
							System.out.println("TEXT: " + text);
							String[] names = text.split(",");
							for (String s : names) {
								userList.append(s + "\n");
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			String name = JOptionPane.showInputDialog("ENTER NAME");
			ChatView c = new ChatView(name);
			c.getP().println("UCon: " + name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource().equals(color)) {
			if (color.getSelectedItem().equals("Red")) {
				view.setForeground(Color.RED);
			} else if (color.getSelectedItem().equals("Blue")) {
				view.setForeground(Color.BLUE);
			} else if (color.getSelectedItem().equals("Black")) {
				view.setForeground(Color.BLACK);
			} else if (color.getSelectedItem().equals("White")) {
				view.setForeground(Color.WHITE);
			}
		} else if (arg0.getSource().equals(mode)) {
			if (mode.getSelectedItem().equals("Day Mode")) {
				view.setBackground(Color.white);
			} else {
				view.setBackground(Color.BLACK);
			}
		} else if (arg0.getSource().equals(font)) {
			view.setFont(new Font((String) font.getSelectedItem(), Font.PLAIN, 30));
			input.setFont(view.getFont());
		}
	}

}
