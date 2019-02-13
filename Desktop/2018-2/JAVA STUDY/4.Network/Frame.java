import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener{
	static JTextArea chating = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(chating);
	JTextField sendBox = new JTextField(12);
	JTextField nameBox = new JTextField();
	JTextField roomBox = new JTextField("0");
	
	//JLabel pInfo = new JLabel("대기실: ");
	//JList pList = new JList(); //user 명단 
	JButton lose = new JButton("기권");
	JButton ready = new JButton("ready");
	JButton wait = new JButton("wait");
	JButton send = new JButton("send");
	JButton start = new JButton("Game Start");
	JButton exit = new JButton("Game exit");
	
	static JLabel info = new JLabel("info");
//	JMenuItem[] menuItem;
//	JMenu reset;

	Board board;
	ClientSender cs ;
	Socket socket;
	boolean isReady;
	static boolean isEnableReset = false;
	
	public Frame(String nickname) {
		//Frame setting
		init();
		
		//Client connect to Server
		connect(nickname);	
		
		this.setVisible(true);
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(send)) {		//chating message send
			if(sendBox.getText().equals(""))
				return;
			cs.sendMsg("[Msg],"+"["+Main.nickname+"]: ,"+sendBox.getText());
			sendBox.setText("");
		}
		else if(e.getSource().equals(ready)) {	//ready check
			isReady = !isReady;
			if(isReady) {
				ready.setForeground(Color.GREEN);
				ready.setText("ready");
				cs.sendMsg("[Ready],"+Main.nickname);
			}
			else {
				ready.setForeground(Color.RED);
				ready.setText("wait");
				cs.sendMsg("[Wait],"+Main.nickname);
			}
		}
		else if(e.getSource().equals(start)){	//Game start
			cs.sendMsg("[Start],"+Main.nickname);
		}
		else if(e.getSource().equals(lose)) {	//기권 
			cs.sendMsg("[Lose],"+Main.nickname);
			//isEnableReset = true;
		}
//		else if(e.getSource().equals(reset)) {
//			//isEnableReset = true;
//			board.reset();
//		}
		else if(e.getSource().equals(exit)) {	//Game exit
			cs.sendMsg("[Exit]");
			//isEnableReset = true;
			this.dispose();
		}
	}
	
	public void chat_out(String msg) {
		msg+="\n";
		chating.append(msg);
	}
	
	private void connect(String nickname) {
		try {
			String serverIp = "127.0.0.1";
			Socket socket = new Socket(serverIp, 7777);
			System.out.println("client connected");
			Thread sender = new Thread(new ClientSender(socket, nickname)); 
			Thread receiver = new Thread(new ClientReceiver(socket));
			
			cs = new ClientSender(socket, nickname);
			
			sender.start();
			receiver.start();
		} catch(ConnectException ce) {
			ce.printStackTrace();
		} catch(Exception e){}
	}
	
	private void init() {
		setLayout(null);
		board = new Board();
		
		//chating.setEditable(false);
		info.setBounds(10,30,480,30);
		board.setLocation(10,70);
		add(info);
		add(board);
		
		JPanel jp = new JPanel();

		jp.add(ready);
		jp.add(wait);
		jp.add(start);
		jp.setBounds(500,110,250,150);
		
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BorderLayout());
		JPanel jp21 = new JPanel();
		jp21.add(lose);
		jp21.add(exit);
		jp2.add(jp21,"South");
		jp2.setBounds(500,110,250,180);

		JPanel jp3 = new JPanel();
		JPanel jp31 = new JPanel();
		jp3.setLayout(new BorderLayout());
		jp3.add(chating,"Center");	//채팅내용이 띄워짐 
		jp31.add(sendBox);	//내가 작성하고있는 채팅 창 
		jp31.add(send);
		jp3.add(jp31,"South");
		jp3.setBounds(500,300,250,250);
		
		add(jp);
		add(jp2);
		add(jp3);
		
		sendBox.addActionListener(this);
		send.addActionListener(this);
		ready.addActionListener(this);
		lose.addActionListener(this);
		start.addActionListener(this);
		exit.addActionListener(this);
	
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);   
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  
		chating.setLineWrap(true);
		chating.setEditable(false);
		
		start.setEnabled(false);
		wait.setEnabled(false);
		
		//메뉴바설정 
//		JMenuBar mb = new JMenuBar();
//		reset = new JMenu("reset");
//		mb.add(reset);
//		setJMenuBar(mb);
//		reset.addActionListener(this);
	}
	
	static class ClientSender extends Thread{
		Socket socket;
		static DataOutputStream out;
		String nickname;
		String id;
		ClientSender(Socket socket, String nickname){
			this.socket = socket;
			this.nickname = nickname;
			
			try {
				out = new DataOutputStream(socket.getOutputStream());
				//this.name = name;
				//id = socket.getInetAddress()+":"+socket.getPort();
			} catch(Exception e) {}
		}
		
		public void run() {
			Scanner scanner = new Scanner(System.in);
			try {
				if(out!=null)
					out.writeUTF(nickname);
				
				while(out != null)
					out.writeUTF("["+nickname+"]"+scanner.nextLine());//input
					//out.writeUTF(scanner.nextLine());//input
			} catch(IOException e) {}
		}
		
		public static void sendMsg(String msg) {
			try {
				out.writeUTF(msg);
			}catch(Exception e) {}
		}
	}
	
	class ClientReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		
		ClientReceiver(Socket socket){
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			}catch(IOException e) {}
		}
		
		public void run() {
			while(in!=null) {
				try {
					String message = in.readUTF();
					
					if(message.contains("[Msg]")) {
						String str[] = message.split(",");
						chat_out(str[1]+str[2]);
					}
					else if(message.contains("[Stone]")) {
						String str[] = message.split(",");
						int x = Integer.parseInt(str[1]);
						int y = Integer.parseInt(str[2]);
						int color = Integer.parseInt(str[3]);
						String nickname = str[4];
						
						if(!nickname.equals(Main.nickname)) {
							//board.putOpponent(x,y,color);
							board.drawStone(x,y,color);
							board.setEnabled(true);
						}
					}
					else if(message.contains("[Ready]")) {
						String str[] = message.split(",");
						String nickname = str[1];
						
						if(!nickname.equals(Main.nickname))
							wait.setText("ready");
						
						if(ready.getForeground()==Color.GREEN && wait.getText().equals("ready"))
							start.setEnabled(true);
						else 
							start.setEnabled(false);
					}
					else if(message.contains("[Wait]")) {
						String str[] = message.split(",");
						String nickname = str[1];
						
						if(!nickname.equals(Main.nickname))
							wait.setText("wait");
						
						if(ready.getForeground()==Color.GREEN && wait.getText().equals("ready"))
							start.setEnabled(true);
						else 
							start.setEnabled(false);
					}
					else if(message.contains("[Start]")) {
						String str[] = message.split(",");
						String nickname = str[1];
						
						if(nickname.equals(Main.nickname)) {
							info.setText("Black");
							board.color = board.BLACK;
						}
						else {
							info.setText("White");
							board.color = board.WHITE;
						}
					
						board.setEnabled(true);
						start.setEnabled(false);
						
						board.enable = true;
						ready.setEnabled(false);
						chat_out("===Game Start===");
					}
					else if(message.contains("[Win]")) {
						String str[] = message.split(",");
						String nickname = str[1];
						
						if(!nickname.equals(Main.nickname))
							info.setText("===["+Main.nickname+"]님 축하합니다 승리하셨습니다.===");
						else
							info.setText("===패배===");
						
						lose.setEnabled(false);
						board.setEnabled(false);
					}
					else if(message.contains("[Lose]")) {
						String str[] = message.split(",");
						String nickname = str[1];
						
						chat_out("===["+nickname+"]님이 기권하셨습니다.===");
							
						if(!nickname.equals(Main.nickname) )
							info.setText("===["+Main.nickname+"]님 축하합니다 승리하셨습니다.===");
						else
							info.setText("===기권===");
							
						lose.setEnabled(false);
						board.enable = false;
					}
					else if(message.contains("[Exit]")) {
						chat_out("===상대방이 나가셨습니다.===");
						info.setText("Game Over");
						board.enable = false;
					}
				}catch(IOException e) {}
				
			}//while
		}//run
	}
	
}
