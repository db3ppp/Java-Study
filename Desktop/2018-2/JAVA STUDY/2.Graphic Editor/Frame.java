import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Frame extends JFrame implements ActionListener{
	JFrame frame = new JFrame();
	JPanel buttons = new JPanel();
	JButton line = new JButton("Line");
	JButton sketch = new JButton("Sketch");
//	JButton spray = new JButton("Spray");
//	JButton text = new JButton("Text");
	JButton circle = new JButton("Circle");
	JButton rect = new JButton("Rect");
	JButton erase = new JButton("Erase");
	JButton clear = new JButton("Clear");
	BufferedImage image = new BufferedImage(700,700,BufferedImage.TYPE_INT_ARGB);
	JMenuItem[] menuItem;
	JMenuItem[] menuItem2;
	String message;
	
	final int TEXT=0;
	final int LINE=1;
	final int RECT=2;
	final int CIRCLE=3;
	final int SKETCH=4;
	final int SPRAY=5;
	final int ERASE=6;
	final int ERASER=7;
	final int MOVE=8;
	final int RESIZE=9;
	final int FILL=10;
	int x,y,x1,y1;
	
	Draw draw = new Draw();

	public Frame() {
		this.setTitle("Graphic Editor");
		draw.setOpaque(true);
		draw.setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//메뉴바설정 
		JMenuBar menuBar = new JMenuBar();
		//File save/load 
		JMenu f = new JMenu("File");
		String[] itemTitle = {"Save", "Load"};
		menuItem = new JMenuItem[itemTitle.length];
		for(int i=0; i<menuItem.length; i++) {
			menuItem[i] = new JMenuItem(itemTitle[i]);
			menuItem[i].addActionListener(this);
			f.add(menuItem[i]);
			f.addSeparator();
		}
		menuBar.add(f);
		
		//속성설정 
		JMenu m = new JMenu("Properties");
		String[] itemTitle2 = {"Color", "Fill", "Empty"};
		menuItem2 = new JMenuItem[itemTitle2.length];
		for(int i=0; i<menuItem2.length; i++) {
			menuItem2[i] = new JMenuItem(itemTitle2[i]);
			menuItem2[i].addActionListener(this); 
			m.add(menuItem2[i]);
			m.addSeparator();
		}
		
		//굵기설정 
		JMenu thickness = new JMenu("Thickness");
		String[] thicknessDetail = {"1","2","3"};
		JRadioButtonMenuItem[] thickLevel = new JRadioButtonMenuItem[thicknessDetail.length];
		ButtonGroup thickGroup = new ButtonGroup();
		for(int i=0; i<thickLevel.length; i++) {
			thickLevel[i] = new JRadioButtonMenuItem(thicknessDetail[i]);
			thickGroup.add(thickLevel[i]);	
			thickLevel[i].addActionListener(this);
			thickness.add(thickLevel[i]);
		}
		thickLevel[0].setSelected(true);
		m.add(thickness);
		menuBar.add(m);
		
		//부가기능 
		JMenu m2 = new JMenu("Addition");
		String[] itemTitle3 = {"Move","Resize","Eraser"};
		JMenuItem[] menuItem3 = new JMenuItem[itemTitle3.length];
		for(int i=0; i<menuItem3.length; i++) {
			menuItem3[i] = new JMenuItem(itemTitle3[i]);
			menuItem3[i].addActionListener(this);
			m2.add(menuItem3[i]);
			m2.addSeparator();
		}
		menuBar.add(m2);

		//그림판 화면 구성 
		setButtons();
		this.add(draw, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.NORTH);
		this.setJMenuBar(menuBar);
		this.setLocationRelativeTo(null);
		this.setSize(700, 700);
		this.setVisible(true);
		
		//event활성화 시켜주기 
		line.addActionListener(this);
		circle.addActionListener(this);
		rect.addActionListener(this);
//		text.addActionListener(this);
		sketch.addActionListener(this);
//		spray.addActionListener(this);
		erase.addActionListener(this);
		clear.addActionListener(this);
		m.addActionListener(this);
		m2.addActionListener(this);
	}
	
	void setButtons() {
		buttons.add(line);
		buttons.add(sketch);
//		buttons.add(spray);
//		buttons.add(text);
		buttons.add(circle);
		buttons.add(rect);
		buttons.add(erase);
		buttons.add(clear);
	}
	
	//Override
	public void actionPerformed(ActionEvent e) {
		JColorChooser chooser=new JColorChooser();
		Color color, fill;
		
		//Basic Functions 
//		if(e.getSource().equals(text))
//			draw.type = TEXT;
		if(e.getSource().equals(line))
			draw.type = LINE;
		else if(e.getSource().equals(circle))
			draw.type = CIRCLE;
		else if(e.getSource().equals(rect)) 
			draw.type = RECT;
		else if(e.getSource().equals(sketch))
			draw.type = SKETCH;
//		else if(e.getSource().equals(spray))
//			draw.type = SPRAY;
		else if(e.getSource().equals(erase))
			draw.type = ERASE;
		else if(e.getSource().equals(clear)) 
			draw.clear();
		
		//Functions in MenuBar
		else if(e.getActionCommand() == "Save")
			fileSave();
		else if(e.getActionCommand() == "Load")
			fileLoad();
			
		else if(e.getActionCommand() == "Color") {
			color = chooser.showDialog(null, "Color", Color.black);
			draw.color = color;
			draw.temp = color;
		}
		else if(e.getActionCommand() == "Fill") {
			fill = chooser.showDialog(null, "Fill", null);
			draw.fill = fill;
			draw.isFill = true;
		}
		else if(e.getActionCommand() == "Empty") {
			draw.isFill = false;
			draw.fill = null;
		}
		else if(e.getActionCommand() == "Move") 
			draw.type = MOVE;
		else if(e.getActionCommand() == "Resize")
			draw.type = RESIZE;
		else if(e.getActionCommand() == "Eraser")
			draw.type = ERASER;
		else if(e.getActionCommand() == "1") 
			draw.weight = 2;
		else if(e.getActionCommand() == "2") 
			draw.weight = 4;
		else 
			draw.weight = 8;

		//repaint();
	}
	void fileSave() {
		draw.isSave = true;
		
		try {
			draw.paintComponent(image.getGraphics());
			ImageIO.write(image, "PNG", new File("/Users/kimhyewon/Desktop/2018-2/JAVA STUDY/graphic editor.png"));
		}catch(IOException e1) {
			e1.printStackTrace();
		}
		message = String.format("저장되었습니다.");
		JOptionPane.showMessageDialog(null, message,"Save", JOptionPane.PLAIN_MESSAGE);
		draw.isSave = false;
	}
	void fileLoad() {
		draw.isLoad = true;
		
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG&PNG Images", "jpg","png");
		fc.setFileFilter(filter);
		
		int result = fc.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			//File file = fc.getSelectedFile();
			draw.img = Toolkit.getDefaultToolkit().getImage(path);
//			File file = new File(path);
//			try {
//				draw.image = ImageIO.read(file);
//			} catch(IOException e1) {
//				e1.printStackTrace();
//			}
		}
	}
	
}
