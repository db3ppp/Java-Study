import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

public class EditView extends JFrame implements ActionListener{
	JLabel Title = new JLabel("<html><h1><strong><i>Edit User Info</i></strong></h1><hr></html>");
	JLabel alert = new JLabel("");
	JLabel alertId = new JLabel("id는 수정불가합니다.");
//	JButton checkId = new JButton("중복확인");
	JButton edit = new JButton("Edit");
	JButton cancel = new JButton("Cancel");
	JRadioButton male;
	JRadioButton female;
	ButtonGroup bg = new ButtonGroup();
	
	JComboBox<String> jcb;
	String majors[] = {"전산전자공학부","생명과학부","기계제어공학부","공간환경시스템공학부","GLS","콘텐츠융합디자인학부","ICT창업학부","경영경제학부","상담심리복지학부","국제어문학부","언론정보문화학부","법학부"};
	String name, id, pw, major, gender, userType;
	String new_name, new_pw, new_major, new_gender;
	
	JLabel label[] = new JLabel[5];
	JTextField textfield[] = new JTextField[5];
	JPanel jp1,jp2,jp3,jp4,jp5,jp6,jp;
	JPanel jp11,jp12,jp21,jp22,jp31,jp41,jp51;
	
	DTO dto = new DTO();
	DAO dao = new DAO();
	
	public EditView() {}
	//마지막 파라미터는 회원정보를 수정한 user type을 알기위한 인자. 
	public EditView(String name, String id, String pw, String major, String gender, String userType){
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.major = major;
		this.gender = gender;
		this.userType = userType;
		
		init();
		
		setTitle("Edit User Info");
		setSize(400,430);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		checkId.addActionListener(this);
		edit.addActionListener(this);
		cancel.addActionListener(this);
	}
	
//	public EditView(String id, MemberList mList) {
//		this.mList = mList;
//		
//	}
	
	public void init() {
		GridLayout frameLayout = new GridLayout(7,2);
		frameLayout.setVgap(3);
		this.setLayout(frameLayout);
		
		label[0] = new JLabel("name");		
		label[1] = new JLabel("id");
		label[2] = new JLabel("pw");
		label[3] = new JLabel("major");
		label[4] = new JLabel("gender");
		
		textfield[0] = new JTextField(10); // name 
		textfield[1] = new JTextField(10); // id 
		textfield[2] = new JTextField(10); // pw
		
		male = new JRadioButton("Male");
		female = new JRadioButton("Female");
		bg.add(male);
		bg.add(female);
		
		jcb = new JComboBox<String>(majors);
		
		//수정되기 전 원래 정보 띄우기 
		textfield[0].setText(name);
		textfield[1].setText(id);
		textfield[2].setText(pw);
		jcb.setSelectedItem(major);
		
		if(gender.equals("W"))
			female.setSelected(true);
		else
			male.setSelected(true);
		
		textfield[1].setEnabled(false);	//id는 수정 못함 
		
		jp1 = new JPanel();	jp11 = new JPanel(); jp12 = new JPanel();
		jp2 = new JPanel();	jp21 = new JPanel(); jp22 = new JPanel();
		jp3 = new JPanel();	jp31 = new JPanel();
		jp4 = new JPanel();	jp41 = new JPanel();
		jp5 = new JPanel();	jp51 = new JPanel();
		jp6 = new JPanel();	jp = new JPanel();
		
		jp11.add(label[0]);
		jp11.add(textfield[0]); 
		jp1.add("West",jp11);
		jp1.add("East",jp12); //name
		
		jp21.add(label[1]);
		jp21.add(textfield[1]); //id
		jp22.add(alertId);	//id수정불가 메세지 
		jp2.add("West",jp21);
		jp2.add("East", jp22);
		
		jp31.add(label[2]);
		jp31.add(textfield[2]);
		jp3.add("West",jp31); //pw
		
		jp41.add(label[3]);
		jp41.add(jcb);
		jp4.add("West",jp41); //major
		
		jp51.add(label[4]);
		jp51.add(male);
		jp51.add(female);
		jp5.add("West", jp51); //gender
		
		jp6.add(cancel);		
		jp6.add(edit);
		jp6.add(alert);
		alert.setForeground(Color.RED);
		
		this.add(Title,BorderLayout.NORTH);
		this.add(jp1);//name
		this.add(jp2);//id
		this.add(jp3);//pw
		this.add(jp4);//major
		this.add(jp5);//gender
		this.add(jp6);//button
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(edit)) {
			editToDB();
		}
		else if(e.getSource().equals(cancel)) {
			this.dispose();
			if(userType.equals("user"))
				new UserView(name, id, pw, major, gender);
		}
	}
	
	public void editToDB() {
		//작성하지 않은 창이 있는 경우 
		if(textfield[0].getText().isEmpty()) {
			edit.setEnabled(false);
			alert.setText("이름을 입력해주세요:)");
			edit.setEnabled(true);
		}
		else if(textfield[1].getText().isEmpty()) {
			edit.setEnabled(false);
			alert.setText("id를 입력해주세요:)");
			edit.setEnabled(true);
		}
		else if(textfield[2].getText().isEmpty()) {
			edit.setEnabled(false);
			alert.setText("password를 입력해주세요:)");
			edit.setEnabled(true);
		}
		else if(!male.isSelected() && !female.isSelected()) {
			edit.setEnabled(false);
			alert.setText("성별을 선택해주세요:)");
			edit.setEnabled(true);
		}
		//비어있는칸이 없는경우 수정가능 
		else {
			editAccount();
		
			//수정된 정보와 함께 user페이지로 넘어가기 
			this.dispose();
			if(userType.equals("user"))	//user가 수정한 경우
				new UserView(new_name, id, new_pw, new_major, new_gender);
		}
	}
	
	void editAccount() {
		int r;
		String message;
		new_name = textfield[0].getText();
//		String new_id = textfield[1].getText();
		new_pw = textfield[2].getText();
		new_major = (String) jcb.getSelectedItem();
		new_gender="";
		
		if(male.isSelected())
			new_gender = "M";
		else if(female.isSelected())
			new_gender = "W";
		
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul","hyewon","gpdnjs206");
			stmt=(Statement)conn.createStatement();
		
			r = stmt.executeUpdate("update guest set name='" + new_name+"',password='"+new_pw+"',major='"+new_major+"',gender='"+new_gender+"' where id='"+id+"';");
			if(r == 1) {
				message = String.format("수정되었습니다!");
				JOptionPane.showMessageDialog(null, message,"Edit", JOptionPane.PLAIN_MESSAGE);
			}
			else {
				message = String.format("수정에 실패하였습니다.");
				JOptionPane.showMessageDialog(null, message,"Fail to Edit", JOptionPane.PLAIN_MESSAGE);
			}
			
			conn.close();
			stmt.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}
