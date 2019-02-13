import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

class Board extends Canvas implements MouseListener  {
	final int size   = 15 ;   // 오목판 줄 수
	final int cell = 30; // 오목판 줄 간격
	//final int BOARD_SIZE = cell * (size-1); // 오목판의 크기
	
	final int BLACK=-1;
	final int WHITE=1;
	
	int [][]map;
	int color;
	int stoneCount=0;
	boolean enable = false;
//	boolean isplaying = false;

	Image img = null;
	Graphics gImg = null;
	
	public Board() {
		map = new int[size+2][];
		for(int i=0; i<map.length; i++)
			map[i] = new int[size + 2];
		setBackground(new Color(205,165,60));
		setSize(size*(cell+1)+size, size*(cell+1)+size);
		
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		if(gImg == null) {
			img = createImage(getWidth(), getHeight());
			gImg = img.getGraphics();
		}
		
		if(img==null) return;
		drawBoard(g);
	}
	
	public void mousePressed(MouseEvent e) {
		if(!enable)
			return;
		
		int x = (int)Math.round(e.getX()/(double)cell);
		int y = (int)Math.round(e.getY()/(double)cell);
		
		if(x<=0 || y<=0 || x>size || y>size)
			return;
		
		if(map[x][y] == BLACK || map[x][y]==WHITE)
			return;
		
		if(enable) {
			drawStone(x,y,color);
			//상대방에게 놓은 돌의 좌표 전송 
			Frame.ClientSender.sendMsg("[Stone],"+x+","+y+","+color+","+Main.nickname);
		}
		else
			Frame.info.setText("===PAUSE STATE===");
		
		repaint();
	}
	
	public void drawBoard(Graphics g) {
		gImg.setColor(Color.BLACK);
		
		for(int i=1; i<=size;i++) {
			gImg.drawLine(cell, i * cell, cell * size, i * cell);
			gImg.drawLine(i * cell, cell, i * cell, cell * size);
		}
		
		g.drawImage(img,0,0,this);
	}
	
	public void drawStone(int x, int y, int color) {

		if((stoneCount%4==0||stoneCount%4==3) && color==BLACK) {
			map[x][y] = BLACK;
		
			gImg.setColor(Color.black);
			gImg.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
			
			checkWin(x,y);
			stoneCount++;
		}
		if((stoneCount%4==1||stoneCount%4==2) && color==WHITE) {
			map[x][y] = WHITE;
		
			gImg.setColor(Color.white);
			gImg.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
			
			checkWin(x,y);
			stoneCount++;
		}
		
		repaint();
	}

	private void checkWin(int x, int y) {
		if(checkLR(x,y) >= 6 || checkUD(x,y) >= 6 || checkSlash(x,y) >= 6 ||checkBackSlash(x,y) >= 6)
			Frame.ClientSender.sendMsg("[Win],"+Main.nickname);
			//Frame.info.setText("===축하합니다! 승리하셨습니다.===");
		//enable = false;
			//System.out.println("LRwin");
			//JOptionPane.showMessageDialog(null, "축하합니다! 승리하셨습니다.");
//		if(checkUD(x,y) >= 6)
//			System.out.println("UDwin");
//		if(checkSlash(x,y) >= 6)
//			System.out.println("Slash");
//		if(checkBackSlash(x,y) >= 6)
//			System.out.println("BackSlash");
	}
	
	private int checkLR(int x, int y) {
		int count =1;
		for(int i=1;  i<6; i++) {
			 if(x+i <=size && map[x+i][y] == map[x][y])
				count++;
			else break;
		}
		for(int i=1;  i<6; i++) {
			 if(x-i>=0 && map[x-i][y] == map[x][y])
				count++;
			else break;
		}
		return count;
	}
	private int checkUD(int x, int y) {
		int count =1;
		for(int i=1; i<6; i++) {
			 if(y+i<=size && map[x][y+i] == map[x][y])
				count++;
			else break;
		}
		for(int i=1; i<6; i++) {
			 if(y-i>=0 && map[x][y-i] == map[x][y])
				count++;
			else break;
		}
		return count;
	}
	private int checkBackSlash(int x, int y) {
		int count=1;
		for(int i=1; i<6; i++) {
			if(x+i<=size && y-i>=0 && map[x+i][y-i] == map[x][y])
				count++;
			else break;
		}
		for(int i=1; i<6; i++) {
			if(x-i>=0 && y+i<=size && map[x-i][x+i] == map[x][y])
				count++;
			else break;
		}
		return count;
	}
	private int checkSlash(int x, int y) {
		int count=1;
		
		for(int i=1; i<6; i++) {
			 if(x+i<=size && y+i<=size && map[x+i][y+i] == map[x][y])
				count++;
			else break;
		}
		for(int i=1; i<6; i++) {
			 if(x-i>=0 && y-i>=0 && map[x-i][y-i] == map[x][y])
				count++;
			else break;
		}
		return count;
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}	
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void restart() {//restart
		//if(Frame.isEnableReset) {
//		for(int i=0; i<size; i++) {
//			for(int j=0; j<size; j++) {
//				map[i][j]=0;
//			}
//		}
		//new Frame();
		
		//}
		repaint();
	}

}