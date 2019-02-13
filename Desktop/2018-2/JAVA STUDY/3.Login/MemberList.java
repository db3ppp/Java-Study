import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MemberList extends JFrame implements ActionListener{
	Vector<Vector> vector;
	Vector<String> cols;
	DefaultTableModel model;
	JTable table;
	JScrollPane pane;
	JPanel jp;
	JButton insert, delete, edit;
	
	DAO dao;
	
	public MemberList() {
		dao = new DAO();
		
		vector = dao.getMemberList();
		cols = getColumn();
		
		model = new DefaultTableModel(vector, cols);
		table = new JTable(model);
		pane = new JScrollPane(table);
		this.add(pane);
		
		jp = new JPanel();
		insert = new JButton("추가");
		delete = new JButton("삭제");
		edit = new JButton("수정");
		jp.add(insert);
		jp.add(edit);
		jp.add(delete);
		this.add(jp,BorderLayout.NORTH);
		
		insert.addActionListener(this);
		edit.addActionListener(this);
		delete.addActionListener(this);
		
		setSize(600,200);
		setVisible(true);
		setTitle("Member Account List");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Vector<String> getColumn() {
		Vector<String> col = new Vector<String>();
		col.add("name");
		col.add("id");
		col.add("password");
		col.add("major");
		col.add("gender");
		
		return col;
	}
	
	public void tableUpdate() {
		DAO dao = new DAO();
		DefaultTableModel model = new DefaultTableModel(dao.getMemberList(), getColumn());
		table.setModel(model);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(insert)) {
			new RegisterView("admin");
			//this.dispose();
		}
		else if(e.getSource().equals(edit)) {
			int r = table.getSelectedRow();
			
			String name = (String)table.getValueAt(r,0);		
			String id = (String)table.getValueAt(r,1);
			String pw = (String)table.getValueAt(r,2);
			String major = (String)table.getValueAt(r,3);
			String gender = (String)table.getValueAt(r,4);
		
			new EditView(name, id, pw, major, gender, "admin");
			
			//this.dispose();
			//table.repaint();
		}
		
		else if(e.getSource().equals(delete)) {
			int r = table.getSelectedRow();
			if(r<0) return;
			String id = (String)table.getValueAt(r, 1);
			
			int checkAnswer =JOptionPane.showConfirmDialog(null, "탈퇴시키시겠습니까?", "Drop the Account", JOptionPane.YES_NO_OPTION);
			if(checkAnswer ==JOptionPane.YES_OPTION) {
				deleteAccount(id);
				JOptionPane.showMessageDialog(null, "탈퇴가 완료되었습니다.");
	
				//this.dispose();
			}
		}
		tableUpdate();
	}
	
	public void deleteAccount(String id) {
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
