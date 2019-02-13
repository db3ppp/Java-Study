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

public class RegisterView extends JFrame implements ActionListener{
	JLabel Title = new JLabel("<html><h1><strong><i>Register Form</i></strong></h1><hr></html>");
	JLabel alert = new JLabel("");
	JButton checkId = new JButton("중복확인");
	JButton submit = new JButton("Submit");
	JButton cancel = new JButton("Cancel");
	JRadioButton male;
	JRadioButton female;
	ButtonGroup bg = new ButtonGroup();
	
	JComboBox<String> jcb;
	String major[] = {"전산전자공학부","생명과학부","기계제어공학부","공간환경시스템공학부","GLS","콘텐츠융합디자인학부","ICT창업학부","경영경제학부","상담심리복지학부","국제어문학부","언론정보문화학부","법학부"};
	String userType;
	
	JLabel label[] = new JLabel[5];
	JTextField textfield[] = new JTextField[5];
	JPanel jp1,jp2,jp3,jp4,jp5,jp6,jp;
	JPanel jp11,jp12,jp21,jp22,jp31,jp41,jp51;

	DTO dto = new DTO();
	DAO dao = new DAO();
	
	public RegisterView(String userType){
		this.userType = userType;
		init();
	
		setTitle("Register Form");
		setSize(400,400);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		checkId.addActionListener(this);
		submit.addActionListener(this);
		cancel.addActionListener(this);
	}
	
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
		
		jcb = new JComboBox<String>(major);
		
		jp1 = new JPanel();	jp11 = new JPanel(); jp12 = new JPanel();
		jp2 = new JPanel();	jp21 = new JPanel(); jp22 = new JPanel();
		jp3 = new JPanel();	jp31 = new JPanel();
		jp4 = new JPanel();	jp41 = new JPanel();
		jp5 = new JPanel();	jp51 = new JPanel();
		jp6 = new JPanel();	jp = new JPanel();
		
		jp11.add(label[0]);
		jp11.add(textfield[0]); 
		jp1.add("West",jp11);
		jp1.add("East",jp12);	//name
		
		jp21.add(label[1]);
		jp21.add(textfield[1]);	//id
		jp22.add(checkId); 
		jp2.add("West",jp21);
		jp2.add("East", jp22);	//chekcid
		
		jp31.add(label[2]);
		jp31.add(textfield[2]);
		jp3.add("West",jp31);	//pw
		
		jp41.add(label[3]);
		jp41.add(jcb);
		jp4.add("West",jp41);	//major
		
		jp51.add(label[4]);
		jp51.add(male);
		jp51.add(female);
		jp5.add("West", jp51);	//gender
		
		jp6.add(cancel);		
		jp6.add(submit);
		jp6.add(alert);
		alert.setForeground(Color.RED);

//		GridLayout g = new GridLayout(5,2);
//		jp.setLayout(g);

//		jp.add(Title);
//		jp.add(jp1);
//		jp.add(jp2);
//		jp.add(jp3);
//		jp.add(jp4);
//		jp.add(jp5);
//		jp.add(jp6);
//		jp.setBorder(BorderFactory.createTitledBorder(""));
//		this.add(jp);
		
		this.add(Title,BorderLayout.NORTH);
		this.add(jp1);//name
		this.add(jp2);//id
		this.add(jp3);//pw
		this.add(jp4);//major
		this.add(jp5);//gender
		this.add(jp6);//button
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(submit)) {
			submitToDB();
		}
		else if(e.getSource().equals(cancel)) {
			this.dispose();
			
			if(userType.equals("user"))
				new LoginView();
		}
		else if(e.getSource().equals(checkId)) {
			checkID();
		}
	}
	
	public void submitToDB() {
		//작성하지 않은 창이 있는 경우 
		if(textfield[0].getText().isEmpty()) {
			submit.setEnabled(false);
			alert.setText("이름을 입력해주세요:)");
			submit.setEnabled(true);
		}
		else if(textfield[1].getText().isEmpty()) {
			submit.setEnabled(false);
			alert.setText("id를 입력해주세요:)");
			submit.setEnabled(true);
		}
		else if(textfield[2].getText().isEmpty()) {
			submit.setEnabled(false);
			alert.setText("password를 입력해주세요:)");
			submit.setEnabled(true);
		}
		else if(!male.isSelected() && !female.isSelected()) {
			submit.setEnabled(false);
			alert.setText("성별을 선택해주세요:)");
			submit.setEnabled(true);
		}
		
		//비어있는칸이 없는경우 회원가입가능 
		else {
			dto.setName(textfield[0].getText());
			dto.setId(textfield[1].getText());
			dto.setPw(textfield[2].getText());
			dto.setMajor((String)jcb.getSelectedItem());
		
			if(male.isSelected())
				dto.setGender("M");
			else if(female.isSelected())
				dto.setGender("W");
		
			try {
				dao.insertAccount(dto);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		String message = String.format("회원가입되었습니다!");
		JOptionPane.showMessageDialog(null, message,"Save", JOptionPane.PLAIN_MESSAGE);
		
		//로그인페이지로 넘어가기 
		this.dispose();
		if(userType.equals("user"))
			new LoginView();
		}
	}
	
	void checkID() {
		boolean availableId;
		String message;
		String id = textfield[1].getText();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul","hyewon","gpdnjs206");
			stmt=(Statement)conn.createStatement();
			rs = stmt.executeQuery("select * from guest where id='"+id+"';");
			
			if(rs.next())
				availableId = false;
			else 
				availableId = true;
			
			if(availableId == true) {
				message = String.format("<"+id+">"+" 는 사용하실 수 있는 id입니다.");
				JOptionPane.showMessageDialog(null, message,"OK", JOptionPane.PLAIN_MESSAGE);
				alert.setText("");
				submit.setEnabled(true);
			}else {
				message = String.format("<"+id+">"+" 는 이미 사용중인 id입니다.");
				JOptionPane.showMessageDialog(null, message,"Try Again", JOptionPane.PLAIN_MESSAGE);
				alert.setText("다른 id를 입력해주세요:)");
				submit.setEnabled(false);
			}
			
			conn.close();
			stmt.close();
			rs.close();
			
		} catch(Exception ee) {
			System.out.println("Exception");
			System.exit(0);
		}
	}
	
}
