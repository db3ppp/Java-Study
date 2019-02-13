import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


public class AdminView extends JFrame implements ActionListener{
	JButton rank = new JButton("User Ranking List");
	JButton list = new JButton("User Account List");	
	JButton logout = new JButton("Logout");
	JPanel buttons = new JPanel();

//	JTable table = new JTable(model);
//	JScrollPane scroll = new JScrollPane(table);
	
	Color color = new Color(240,248,255);
	
	public AdminView(){
		setTitle("AdminView");
		setSize(500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buttons.add(rank);
		buttons.add(logout);
		//list.setForeground(Color.RED);
		buttons.add(list);
		
	    buttons.setLayout(new GridLayout(6,1));	
		buttons.setBackground(color);
        buttons.setBorder((Border) new TitledBorder(new LineBorder(Color.gray), "Welcome, admin" ));

		this.add(buttons);
		this.setVisible(true);
		
		rank.addActionListener(this);
		logout.addActionListener(this);
		list.addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(rank)) {
			new RankList();
		}
		else if(e.getSource().equals(logout)) {
			new LoginView();
			this.dispose();
		}
		else if(e.getSource().equals(list)) {
			new MemberList();
			//this.dispose();
		}
		
	}
	
	

}


