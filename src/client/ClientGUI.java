package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import utilities.InputListener;
import utilities.Message;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;

public class ClientGUI implements Observer
{
	JButton 	button, button_1, button_2, button_3, button_4, 
				button_5, button_6, button_7, button_8, btnConnect, btnSend,
				btnDisconnect; 
	
	JTextArea 	textDisplay, textSend;
	
	private boolean				newSession;
	private Socket				socket;
	private String 				userName, ip;
	private ObjectOutputStream 	oos;
	private InputListener		inputListener;
	
	
	private JFrame frmTictactoe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frmTictactoe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frmTictactoe = new JFrame();
		frmTictactoe.setResizable(false);
		frmTictactoe.setTitle("Tic-Tac-Toe");
		frmTictactoe.setBounds(100, 100, 877, 524);
		frmTictactoe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTictactoe.getContentPane().setLayout(new BoxLayout(frmTictactoe.getContentPane(), BoxLayout.X_AXIS));
		
		JPanel gamePanel = new JPanel();
		frmTictactoe.getContentPane().add(gamePanel);
		gamePanel.setLayout(null);
		
		button = new JButton("X");
		button.setBounds(75, 77, 89, 89);
		button.setForeground(Color.RED);
		button.setFont(new Font("Tahoma", Font.PLAIN, 96));
		gamePanel.add(button);
		
		button_1 = new JButton("X");
		button_1.setForeground(Color.RED);
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_1.setBounds(176, 77, 89, 89);
		gamePanel.add(button_1);
		
		button_2 = new JButton("X");
		button_2.setForeground(Color.RED);
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_2.setBounds(277, 77, 89, 89);
		gamePanel.add(button_2);
		
		button_3 = new JButton("X");
		button_3.setForeground(Color.RED);
		button_3.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_3.setBounds(277, 179, 89, 89);
		gamePanel.add(button_3);
		
		button_4 = new JButton("X");
		button_4.setForeground(Color.RED);
		button_4.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_4.setBounds(176, 179, 89, 89);
		gamePanel.add(button_4);
		
		button_5 = new JButton("");
		button_5.setForeground(Color.RED);
		button_5.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_5.setBounds(75, 179, 89, 89);
		gamePanel.add(button_5);
		
		button_6 = new JButton("X");
		button_6.setForeground(Color.RED);
		button_6.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_6.setBounds(75, 281, 89, 89);
		gamePanel.add(button_6);
		
		button_7 = new JButton("X");
		button_7.setForeground(Color.RED);
		button_7.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_7.setBounds(176, 281, 89, 89);
		gamePanel.add(button_7);
		
		button_8 = new JButton("X");
		button_8.setForeground(Color.RED);
		button_8.setFont(new Font("Tahoma", Font.PLAIN, 96));
		button_8.setBounds(277, 281, 89, 89);
		gamePanel.add(button_8);
		
		JPanel chatPanel = new JPanel();
		frmTictactoe.getContentPane().add(chatPanel);
		chatPanel.setLayout(null);
		
		btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				newSession = true;
				connectMe();
			}
		});
		
		textDisplay = new JTextArea();
		textDisplay.setWrapStyleWord(true);
		textDisplay.setLineWrap(true);
		
		DefaultCaret caret = (DefaultCaret)textDisplay.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane sp = new JScrollPane(textDisplay);
		sp.setSize(405, 310);
		sp.setLocation(12, 79);
		textDisplay.setEditable(false);
		textDisplay.setBounds(12, 77, 405, 293);
		chatPanel.add(sp);
		btnConnect.setBounds(12, 441, 97, 25);
		chatPanel.add(btnConnect);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				newSession = true;
				Message message = new Message(userName,"has disconnected.",new Date());
				try
				{
					oos.writeObject(message);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				disconnectMe();
			}
		});
		btnDisconnect.setBounds(121, 441, 110, 25);
		chatPanel.add(btnDisconnect);
		
		textSend = new JTextArea();
		textSend.setWrapStyleWord(true);
		textSend.setLineWrap(true);
		textSend.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				boolean trigger = false;
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					trigger = true;
					Message message = new Message(userName,textSend.getText(),new Date());
					
					try
					{
						oos.writeObject(message);
						textDisplay.append("Me: "+message.getMessage()+"\n");
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}	
				}
				
				if(trigger)
				{
					textSend.setText("");
				}
			}
		});
		textSend.setBounds(12, 402, 296, 26);
		JScrollPane spText = new JScrollPane(textSend);
		spText.setLocation(12, 403);
		spText.setSize(303, 25);
		chatPanel.add(spText);
		
		btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				Message message = new Message(userName,textSend.getText(),new Date());
				
				try
				{
					oos.writeObject(message);
					textDisplay.append("Me: "+message.getMessage()+"\n");
					textSend.setText("");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				
			}
		});
		btnSend.setBounds(320, 404, 97, 25);
		chatPanel.add(btnSend);
	}
	
	@Override
	public void update(Observable observable, Object arg)
	{
		Message message = (Message)arg;		
		String msg = message.getUser()+": "+message.getMessage();
		textDisplay.append(msg+"\n");
		
		// connected to another person
		if (message.getMessage().compareTo("You can start chatting!") == 0)
		{
			btnSend.setEnabled(true); // now i can TALK!
			btnDisconnect.setEnabled(true); // and if i don't like the other person, i can run away!
		}
		
		// the other person ran away!
		if (message.getMessage().compareTo("has disconnected.") == 0)
		{
			// i didn't quit the session, so i get to keep my username
			newSession = false;
			disconnectMe(); // drop current session
			connectMe(); // start new session
		}
		
	}
	
	private void connectMe()
	{
		try
		{
			ip = JOptionPane.showInputDialog("Enter IP Address");
			socket = new Socket(ip,5555);
			
			// if i didn't disconnect, i want to keep my username
			if(newSession)
			{
				userName = JOptionPane.showInputDialog("Enter user name");
			}
			btnConnect.setEnabled(false);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			textDisplay.append("Connected! Waiting for a chat partner...\n");
			
			//start an input listener thread
			inputListener = new InputListener(socket,ClientGUI.this);
			Thread t1 = new Thread(inputListener);
			t1.start();
			
		}
		catch (HeadlessException e1)
		{
			e1.printStackTrace();
		}
		catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
	
	private void disconnectMe()
	{
		textDisplay.append("Disconnected.\n");
		btnDisconnect.setEnabled(false);
		btnSend.setEnabled(false);
		btnConnect.setEnabled(true);
		try
		{
			oos.close();
			socket.close();
			inputListener = null;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
	}
	
	public void wipeText()
	{
//		if(textSend.getText().equalsIgnoreCase("") || textSend.getText().equals(null) || textSend.getText().equalsIgnoreCase("\n"))
//		{
			textSend.setText("Hello");
//		}
	}
}
