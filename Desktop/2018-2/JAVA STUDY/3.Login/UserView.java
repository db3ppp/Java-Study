import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;



public class UserView extends JFrame implements ActionListener{
	JButton edit = new JButton("Edit UserInfo ");
	JButton game = new JButton("Play Minesweeper");
	JButton logout = new JButton("Logout");
	JButton delete= new JButton("Delete User Account");
	JPanel buttons = new JPanel();
	
	int checkAnswer;
	Color color = new Color(243,222,210);
	String name, id, pw, major, gender;
	
	public UserView() {}
	public UserView(String name, String id, String pw, String major, String gender){
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.major = major;
		this.gender = gender;
		
		setTitle("UserView");
		setSize(500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buttons.add(edit);
		buttons.add(game);
		buttons.add(logout);
		delete.setForeground(Color.RED);
		buttons.add(delete);
		
        // 생성된 패널의 레이아웃 관리자를 GridLayout 으로 변경 
//		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
	    buttons.setLayout(new GridLayout(6,1));	
		buttons.setBackground(color);
		
        // 패널의 크기와 경계선을 설정한다
        //buttons.setPreferredSize(new Dimension(100,10));
        buttons.setBorder((Border) new TitledBorder(new LineBorder(Color.gray), "Welcome, " +name+ "님 "));

		this.add(buttons);
		this.setVisible(true);
		
		edit.addActionListener(this);
		game.addActionListener(this);
		logout.addActionListener(this);
		delete.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(edit)) {
			new EditView(name, id, pw, major, gender, "user");
			this.dispose();
		}
		else if(e.getSource().equals(game)) {
			Frame frame = new Frame(id);
			
			//new UserView();
		}
		else if(e.getSource().equals(logout)) {
			new LoginView();
			this.dispose();
		}
		else if(e.getSource().equals(delete)) {
			checkAnswer =JOptionPane.showConfirmDialog(null, "탈퇴하시겠습니까?", "Drop the Account", JOptionPane.YES_NO_OPTION);
			if(checkAnswer ==JOptionPane.YES_OPTION) {
				deleteAccount();
				JOptionPane.showMessageDialog(null, "탈퇴가 완료되었습니다.");
	
				new LoginView();
				this.dispose();
			}
		}
		
	}
	
	public void deleteAccount() {
		Connection conn = null;
		Statement stmt = null;
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul","hyewon","gpdnjs206");
			stmt=(Statement)conn.createStatement();
			stmt.executeUpdate("delete from guest where id='"+id+"';");
			
			conn.close();
			stmt.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}
