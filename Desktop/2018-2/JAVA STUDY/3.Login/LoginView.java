import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.awt.Color;
import javax.swing.*;

public class LoginView extends JFrame implements ActionListener{
	JLabel Title = new JLabel("<html><h1><strong><i>Login</i></strong></h1><hr></html>");
	JButton login = new JButton("Login");
	JButton signin = new JButton("Sign in");
	JPanel in = new JPanel();
	
	JPanel loginInfo = new JPanel();
	JLabel idLabel = new JLabel("ID:           ");
	JLabel pwLabel = new JLabel("Password:");
	JTextField id = new JTextField(10);
	JPasswordField pw = new JPasswordField(10);
	
	JPanel userInfo = new JPanel();
	JLabel userLabel = new JLabel("User Type");
	JRadioButton user = new JRadioButton("User");
	JRadioButton admin = new JRadioButton("Admin");
	ButtonGroup bg = new ButtonGroup();
	
	JPanel jp = new JPanel();
	UserView userview;
	AdminView adminview;
	
	public LoginView() {
		setTitle("Login");
		setSize(229,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loginInfo.add(idLabel);
		loginInfo.add(id);	
		loginInfo.add(pwLabel);
		loginInfo.add(pw);
		
		userInfo.add(userLabel);
		userInfo.add(user);
		userInfo.add(admin);	
		
		in.add(signin);
		in.add(login);
		
		bg.add(user);
		bg.add(admin);
		user.setSelected(true);
		
		GridLayout g = new GridLayout(3,2);
		jp.setLayout(g);
		jp.add(loginInfo);
		jp.add(userInfo);
		jp.add(in);
		
		this.add(Title,BorderLayout.NORTH);
		this.add(jp,BorderLayout.CENTER);
		//this.add(loginInfo, BorderLayout.CENTER);
		//this.add(in, BorderLayout.PAGE_END);
		this.setVisible(true);
		
		login.addActionListener(this);
		signin.addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Login") {
				loginCheck();	
		}
		else if(e.getActionCommand() == "Sign in") {//회원가입창 보이기 
			this.dispose();
			new RegisterView("user");
		}
	}
	
	public void loginCheck(){
		String ID = id.getText();
		String passText = new String(pw.getPassword()); //JTextfield가 아닌 JTextPassword를 사용했기 때문에 
		String password;
		String name, major,gender;
		
		Connection conn;
		Statement stmt;
		ResultSet rs;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul","hyewon","gpdnjs206");
			stmt = (Statement)conn.createStatement();
			rs = stmt.executeQuery("select * from guest where id='"+ID+"';");
			
			//id가 없을경우 
			if(!rs.next()) {
				JOptionPane.showMessageDialog(null, "Wrong ID!");
			}
			
			//id가 존재하는 경우 
			else {
				
				//id에 해당하는 값들 가져오기 
				password = rs.getString("password");
				name = rs.getString("name");
				major = rs.getString("major");
				gender = rs.getString("gender");
				
				//비밀번호가 일치하는경우 
				if(password.equals(passText)) {
					JOptionPane.showMessageDialog(null, "Login Successful!");
					
					//user창 열기
					if(user.isSelected()) {
						
						if(name.equals("superuser"))
							JOptionPane.showMessageDialog(null, "Choose again UserType Admin");
						
						else {
							userview = new UserView(name, ID, password, major, gender);
							this.dispose();
						}
					}
					//admin창 열기 
					else {
						if(name.equals("superuser")) {
							adminview = new AdminView();
							this.dispose();
						}
						else
							JOptionPane.showMessageDialog(null, "Choose again UserType User");
						
						
					}
				}
				//비밀번호가 틀린경우 
				else {
					JOptionPane.showMessageDialog(null, "Wrong Password!");
				}
			}

			rs.close();
			stmt.close();
			conn.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
