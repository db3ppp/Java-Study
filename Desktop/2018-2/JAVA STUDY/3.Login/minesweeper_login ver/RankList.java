
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RankList extends JFrame implements ActionListener{
	Vector<Vector> vector;
	Vector<String> cols;
	DefaultTableModel model;
	JTable table;
	JScrollPane pane;
//	JPanel jp;
//	JButton insert, delete, edit;
	
	DAO dao;
	
	public RankList() {
		dao = new DAO();
		
		vector = dao.getRankList();
		cols = getColumn();
		
		model = new DefaultTableModel(vector, cols);
		table = new JTable(model);
		pane = new JScrollPane(table);
		this.add(pane);
		
		setSize(600,200);
		setVisible(true);
		setTitle("Member Ranking List");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Vector<String> getColumn() {
		Vector<String> col = new Vector<String>();
		col.add("id");
		col.add("score");
		
		return col;
	}
	
	public void tableUpdate() {
		DAO dao = new DAO();
		DefaultTableModel model = new DefaultTableModel(dao.getRankList(), getColumn());
		table.setModel(model);
	}
	
	public void actionPerformed(ActionEvent e) {

	}
	


}
