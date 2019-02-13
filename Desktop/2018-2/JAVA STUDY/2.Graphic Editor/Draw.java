import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;

//public class Draw extends JPanel{
public class Draw extends JComponent{
	Vector <Data>detail = new Vector<Data>();
	Vector <Object>object = new Vector<Object>();
	
	Color color=Color.black, fill,temp;
	int weight;
	int type;
	int offX, offY;
	int x,y,x1,y1,x2,y2;
	
	Rectangle rect;
	Ellipse2D circle;
	Line2D line;
	Image img;
	
	boolean isDragged;
	boolean isFill;
	boolean isSave;
	boolean isLoad;
	
	
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
	
	public Draw() {
		isDragged = false;
		isFill = false;
		
		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//File Save / Load
		if(isLoad == true)
			g.drawImage(img, 0, 0, 700, 700, this);		
		if(isSave == true) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 700, 700);
		}

		for(Data d : detail) {
			g.setColor(d.lineColor);
			g2.setStroke(new BasicStroke(d.lineWeight));
			
			switch(d.type) {
			
			case LINE:
				g.drawLine(d.x, d.y, d.x1, d.y1);
				line = new Line2D.Double(d.x, d.y, d.x1, d.y1);
				break;
				
			case RECT: 
				g.drawRect(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				rect = new Rectangle(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				if(d.fillColor!=null ) {
					g.setColor(d.fillColor);
					g.fillRect(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				}
				break;
				
			case CIRCLE: 
				g.drawOval(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				circle = new Ellipse2D.Double(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				if(d.fillColor!=null ) {
					g.setColor(d.fillColor);
					g.fillOval(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				}
				break;
				
			case SKETCH: 
				g.drawLine(d.x, d.y, d.x1, d.y1);
				break;
//			case SPRAY:
//				int x2[] = { 210,175,60,150,110,210,310,270,360,245,210 }; 
//				int y2[] = { 60,160,160,225,340,270,340,225,160,160,60 };
//				g.drawPolygon(x2, y2, 10);
//				break;
			case ERASE: 
				g.drawLine(d.x, d.y, d.x1, d.y1);	
				break;
				
			case ERASER:
				g.clearRect(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				//g.setColor(color);
				break;
				
			case MOVE: 
				if(type == RECT)
					g.drawRect(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				else if(type == CIRCLE)
					g.drawOval(Math.min(d.x, d.x1), Math.min(d.y, d.y1), Math.abs(d.x1-d.x), Math.abs(d.y1-d.y));
				else if(type == LINE)
					g.drawLine(d.x, d.y, d.x1, d.y1);
				break;
			}
		}
		
		repaint();
	}
	
	public class Mouse extends MouseAdapter implements MouseListener, MouseMotionListener {
		
		public void mousePressed(MouseEvent e) {//눌렸을때 
			x = e.getX();
			y = e.getY();
			x1 = e.getX();
			y1 = e.getY();
			
			//for rubber banding
//			Data data = new Data();
//			data.x = x;
//			data.y = y;
//			data.x1 = x1;
//			data.y1 = y1;
//			data.type = type;
//			data.lineColor = color;
//			data.fillColor = fill;
//			data.lineWeight = weight;
//			
//			detail.add(data);
			if(type == MOVE || type == RESIZE) {
				if(type == RECT) {
					if(rect.contains(new Point(x,y))){
					//사각형내 마우스 클릭 상대 좌표를 구함
					//현재 마우스 스크린 좌표에서 사각형 위치 좌표의 차이를 구함
						offX = x - rect.x;
						offY = y - rect.y;
					}
				}
				else if(type == CIRCLE) {
					if(circle.contains(new Point(x,y))) {
						offX = x - (int)circle.getX();
						offY = y - (int)circle.getY();
					}
				}
				else if(type == LINE) {
					if(line.contains(new Point(x,y))) {
						offX = x - (int)line.getX1();
						offY = y - (int)line.getX2();
					}
				}
				
				//isDragged = true;
			}
			isDragged = true;
		}
		public void mouseReleased(MouseEvent e) {//떼었을때 
			x1 = e.getX();
			y1 = e.getY();
			//g2.setPaintMode();
			
			if(type != MOVE && type != RESIZE) {
				Data data = new Data();			
				data.x = x;
				data.y = y;
				data.x1 = x1;
				data.y1 = y1;
				data.type = type;
				data.lineColor = color;
				data.fillColor = fill;
				data.lineWeight = weight;
				
				if(type == ERASE)
					data.lineColor = Color.white;
			
					detail.add(data);
			}
			
			isDragged = false;
			repaint();
		}
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				//clear();
			}			
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {
			x1=e.getX();
			y1=e.getY();
			
			//rubber-banding
//			detail.lastElement().x1 = x1;
//			detail.lastElement().y1 = y1;

			if(type == SKETCH)	
				pen();
			
			else if(type == ERASE) {
				temp = new Color(color.getRed(), color.getGreen(), color.getBlue());
				color = Color.white;
				pen();
				
				color = temp;
			}
			
			if(isDragged){
				//rect.x = x1 - offX;
				//rect.y = y1 - offY;
				if(type == MOVE) {	
					int width = detail.lastElement().x1 - detail.lastElement().x;
					int height = detail.lastElement().y1 - detail.lastElement().y;
					
					detail.lastElement().x = x1 - offX;
					detail.lastElement().y = y1 - offY;
					detail.lastElement().x1 = detail.lastElement().x + width;
					detail.lastElement().y1 = detail.lastElement().y + height;
				}
				else if(type == RESIZE) {
					detail.lastElement().x = x1 - offX ;
					detail.lastElement().y = y1 -offY;
				}
			
				//repaint();
			}
				
				
			repaint();
		}
		public void mouseMoved(MouseEvent e) {}
		public void pen() {
			Data data = new Data();
			data.x = x;
			data.y = y;
			data.x1 = x1;
			data.y1 = y1;
			data.type = type;
			data.lineColor = color;
			data.fillColor = fill;
			data.lineWeight = weight;
			
			detail.add(data);
			
			x=x1;
			y=y1;		
		}

	}

	public void clear() {
		detail.clear();
	}
}

class Data{
	int x,y;
	int x1,y1;
	
	int type; //모양 
//	int style; //실선, 점선..
	int lineWeight;
	Color lineColor;
	Color fillColor;
		
	public Data(){
		x=0;
		y=0;
		x1=0;
		y1=0;
		type = 1; //default line 
//		style =1; //default 실선 
		lineColor = Color.black;
		fillColor = null;
		lineWeight = 1;
	}	 
}