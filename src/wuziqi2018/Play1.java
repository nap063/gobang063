package wuziqi2018;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.omg.CORBA.PUBLIC_MEMBER;

import wuziqi2018.ChessBoard.chessMove;

public class Play1{}

class EndBoard extends WindowAdapter{						//结束高分榜
	public void windowClosing(WindowEvent e){
		Father.alread2=false;								//防止重复打开
	}
}
class EndGame extends WindowAdapter{						//结束对局
	public void windowClosing(WindowEvent e){
		Father.alread1=false;
	}
}
class HighScores{
	public HighScores(){
														//用数组表示落子情况 设置公有方法
		ArrayList<HighScore> hllist=SeriUtil.showFi();
		JTextArea HsText=new JTextArea();
		HsText.setFont(new Font("Dialog",0,20));
		String HsRec="";
		for( int i=0;i<hllist.size() ;i++ ){
			HsRec+=("第"+(i+1)+"位:"+hllist.get(i)+"分\n");
		}
		HsText.setText(HsRec);
		HsText.setEditable(false);
		JScrollPane HsPan=new JScrollPane(HsText);
		HsPan.createVerticalScrollBar();
		
		JDialog chess =new JDialog();
		chess.setTitle("High Scores");
		chess.add(HsPan);
		chess.setBounds(600, 400, 250, 200);
		chess.setVisible(true);
		chess.addWindowListener(new EndBoard());
	}
}

class ChessBoard extends JPanel implements GobangRule {					//棋盘面板
	private int pos [][]=new int[15][15];						//数组存放棋盘
	private static int blank= 40;								//设置间隔
	private int pID=0;	 //设置计算先后手	
	LinkedList<Vector> cbRecd= new LinkedList<>();				//记录下棋步骤
	boolean playFirst=true;
	private int readRec=0;										//落子完成  可以显示
	private GoGame goGame1;										//两种游戏Frame
	private GoGame2 goGame2;
	private int victor=0;								
	
	public ChessBoard(int c,GoGame2 goGame2) {					//设置人机对弈	
		this.goGame2=goGame2;
		if(c==0){
			this.setBackground(new Color(232,220,0));	
		}
		else {
			this.setBackground(Color.blue);
		}
		this.setPreferredSize(new Dimension(650,650));
		this.setBorder(BorderFactory.createLineBorder(new Color(180,130,0), 5));
		this.addMouseListener(new chessMove2(this));
		this.setVisible(true);
	}
	public ChessBoard(int c,GoGame goGame) {					//可设置基础双人对弈重载方法
		this.goGame1=goGame;
		if(c==0){
			this.setBackground(new Color(232,220,0));			//绘制棋盘颜色
		}else {
			this.setBackground(Color.blue);	
		}
		this.setPreferredSize(new Dimension(650, 650));
		//this.setBounds(5,5,630,630);					//如何考虑动态大小
		this.setBorder(BorderFactory.createLineBorder(new Color(180,130,0), 5));//设置棋盘边框
		this.addMouseListener(new chessMove(this));
		this.setVisible(true);
	}
	public void take1Back(){							//棋盘数组悔棋方法
		Vector<Integer> lastMov = cbRecd.removeLast(); //按下之后将链表删掉最后  棋盘数组更新 repaint()
		int x = (int) lastMov.elementAt(0);
		int y = (int) lastMov.elementAt(1);
		System.out.println("rm坐1=" + lastMov.elementAt(0));
		System.out.println("rm标2=" + lastMov.elementAt(1));
		pos[x-1][y-1]=0;
		pID--;
		repaint();
	}
	public int GG(){											//赢家判断
		if(victor==1)
			return 1;
		else if (victor==2) {
			return 2;
		}else {
			return 0;
		}
	}
	public void judgeSieg(){								    //判断是否出现胜利
		System.out.println("判断");
		for(int i=0;i<15;i++){
			if (i==7) {
				for(int k=0;k<15;k++){
				
				}
			}
			for(int j=0;j<11;j++){			
				int total=1;
				for(int k=0;k<5;k++){						   // 先纵向判断 5子相乘
					total=pos[i][j+k]*total;	
				}
				
				if (total==1) {			//System.out.println((i+1)+"列"+(j+1)+"行"+pos[i][j]+pos[i][j+1]+pos[i][j+2]+pos[i][j+3]+pos[i][j+4]);
					victor=1;
					break;
				}
				else if (total==32) {
					victor=2;
					break;
				}
				total=1;									
				for(int k=0;k<5;k++){						// 再横向判断5子
					total=pos[j+k][i]*total;				
				}
				if (total==1) {			//System.out.println((i+1)+"列"+(j+1)+"行"+pos[j][i]+pos[j+1][i]+pos[j+1][i]+pos[j+1][i]+pos[j+1][i]);
					victor=1;
					break;
				}
				else if (total==32) {
					victor=2;
					break;
				}
				total=1;
			}
			if (victor!=0){
				System.out.println("winner!!="+victor);
				break;
			}
			
		}if(victor==0){
			for(int i=0;i<11;i++){
				for(int j=0;j<11;j++){			
					int total=1;
					for(int k=0;k<5;k++){						//检查左对角
						total*=pos[i+k][j+k];
					}
					if(total==1){
						victor=1;
						break;
					}else if (total==32) {
						victor=2;
						break;
					}total=1;
					for(int k=0;k<5;k++){
						total*=pos[i-k+4][j+k];					//检查右对角
					}
					if(total==1){
						victor=1;
						break;
					}else if (total==32) {
						victor=2;
						break;
					}
				}
				if (victor!=0){
					System.out.println("winner!!="+victor);
					break;
				}
			}
		}
	}															
	public void highlight(Graphics2D cbg,int x,int y){	
		cbg.setColor(Color.blue);
		cbg.drawLine(x*40+20, y*40+20, x*40+60, y*40+20);
		cbg.drawLine(x*40+20, y*40+20, x*40+20, y*40+60);
		cbg.drawLine(x*40+60, y*40+60, x*40+60, y*40+20);
		cbg.drawLine(x*40+60, y*40+60, x*40+20, y*40+60);
	}
	public void highlight(Graphics2D cbg,int x,int y,Color color){	
		cbg.setColor(color);
		cbg.drawLine(x*40+20, y*40+20, x*40+60, y*40+20);
		cbg.drawLine(x*40+20, y*40+20, x*40+20, y*40+60);
		cbg.drawLine(x*40+60, y*40+60, x*40+60, y*40+20);
		cbg.drawLine(x*40+60, y*40+60, x*40+20, y*40+60);
	}
	public void movec(int x,int y){
		 Graphics2D cbg=(Graphics2D)this.getGraphics();
		if (pos[x][y]==0) {
			Vector<Integer> movPos=new Vector<Integer>(2);				//记录每一步
			if (pID%2==0) {											//黑棋落子
				cbg.setColor(Color.black);
				cbg.fillOval(x*40+25, y*40+25, 30, 30);
				pos[x][y]=1;
				movPos.add(x+1);
				movPos.add(y+1);
				cbRecd.add(movPos);
				pID++;
			}else {
				cbg.setColor(Color.white);							//白棋落子
				cbg.fillOval(x*40+25, y*40+25, 30, 30);
				pos[x][y]=2;
				movPos.add(x+1);
				movPos.add(y+1);
				cbRecd.add(movPos);
				pID++;
			}
		}
		//readRec=1;
	}
	public int getReady(){
		return readRec;
	}
	public void resetR(){
		readRec=0;
	}
	public LinkedList<Vector> getCbRec() {
		return cbRecd;
	}
	public int[][] getPos(){
		return pos;
	}
	public void playChg(){
		playFirst=false;
	}
	public int whoNext(){										//下一个谁走
		return pID%2;
	}
	public int getpID() {
		return pID;
	}
	class chessMove extends MouseAdapter{						// 落子监听类 内部
		private ChessBoard cb;
		private int x=0,y=0;
		public chessMove(ChessBoard cb) {
			this.cb=cb;
		}
		public void mousePressed(MouseEvent e){															//确定落子位置
			x=e.getX();
			y=e.getY();
			if(x>600)x=600;
			else if(x<40)x=40;
			if(y>600)y=600;
			else if(y<40)y=40;
			Graphics2D cbg=(Graphics2D)cb.getGraphics();		//获得棋盘面板图片
			System.out.println("x="+x+"  y="+y);
			int col,row;
			col=(x-20)/40;
			row=(y-20)/40;System.out.println("col"+(col+1)+"  row"+(row+1));
			movec(col, row);
			setReadRec();
			repaint();
			System.out.println("pid="+pID);
			judgeSieg();									//判断赢家
			System.out.println("胜利者="+cb.victor);
			if(cb.victor!=0){
				cb.removeMouseListener(this);
				System.out.println("VVV="+cb.victor);
				JDialog jD=new JDialog();
				jD.setTitle("游戏结束了");
				jD.setBounds(450,350,300,100);
				String st="";
				if (victor==1) {
					st="黑方获胜！";
				}else {
					st="白方获胜！";
				}
				JLabel jL=new JLabel(st);
				jL.setFont(new Font("Dialog",0,20));
				jD.add(jL);
				jD.setVisible(true);
			}
		}
	}
	public void showBlack(){
		System.out.print("黑棋：");
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if (pos[i][j]==1) {
					System.out.println("("+i+","+j+"),");
				}
			}
		}
	}
	public void showWhite(){
		System.out.print("白棋：");
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if (pos[i][j]==2) {
					System.out.println("("+i+","+j+"),");
				}
			}
		}

		
	}
	class chessMove2 extends MouseAdapter{
		private ChessBoard cb;
		private int x=0,y=0;
		public chessMove2(ChessBoard cB) {
			//在当前棋盘选出最优下一步
			this.cb=cB;
			}
		public void mouseClicked(MouseEvent e){	
			int col,row;
			if ((victor==0)&&(playFirst&&whoNext()==0||!playFirst&&whoNext()==1)) {	//玩家先 或玩家后
				x=e.getX();
				y=e.getY();
				if(x>600)x=600;
				else if(x<40)x=40;
				if(y>600)y=600;
				else if(y<40)y=40;
				Graphics2D cbg=(Graphics2D)cb.getGraphics();		//获得棋盘面板图片
				System.out.println("x="+x+"  y="+y);
				col=(x-20)/40;
				row=(y-20)/40;System.out.println("col"+(col+1)+"  row"+(row+1));
				movec(col, row);
				judgeSieg();
				}
			if ((victor==0)&&(playFirst&&whoNext()==1||!playFirst&&whoNext()==0)){
				GameTree5 GTr=new GameTree5(cb);
				Vector<Integer> movedVec=GTr.searchTree();
				movec(movedVec.elementAt(0), movedVec.elementAt(1));
				judgeSieg();	
			}
			setReadRec();
			repaint();
			System.out.println("pid="+pID);
			showBlack();
																	//判断赢家
			System.out.println("胜利者="+cb.victor);
			if(cb.victor==2&&!cb.playFirst||cb.victor==1&&cb.playFirst){
				removeMouseListener(this);
				System.out.println("VVV="+cb.victor);
				JDialog jD=new JDialog();
				jD.setTitle("游戏结束了");
				jD.setBounds(450,350,300,100);
				String st="";
				int  scr=new FigScore(cb).getScore();
				
				SeriUtil.writeFi(new HighScore(scr));
				if (victor==1) {
					st="黑方获胜！总分为"+scr;
				}else {
					st="白方获胜！总分为"+scr;
				}
				JLabel jL=new JLabel(st);
				jL.setFont(new Font("Dialog",0,20));
				jD.add(jL);
				jD.setVisible(true);
			}else if(victor!=0){
				JDialog jD=new JDialog();
				jD.setTitle("游戏结束了");
				jD.setBounds(450,350,200,100);
				JLabel jL=new JLabel("电脑获胜！");
				jL.setFont(new Font("Dialog",0,20));
				jD.add(jL);
				jD.setVisible(true);
				removeMouseListener(this);
			}
		}
	}
	public void paint(Graphics graphics){							//重载JPanel的paint方法 画线
		super.paint(graphics);
		Graphics2D CBgragh=(Graphics2D)graphics;					//强制转换绘制棋盘
		for(int i=0;i<row;i++){										//15行
			CBgragh.drawLine(40,blank*(1+i),600,blank*(1+i));
			CBgragh.drawString(String.valueOf(i+1), 10, blank*(1+i));
		}
		for(int j=0;j<col;j++){										//15列
			CBgragh.drawLine(blank*(1+j),40,blank*(1+j),600);
			CBgragh.drawString(String.valueOf(j+1), blank*(1+j), 20);
		}
		CBgragh.fillOval(115,115,10,10);
		CBgragh.fillOval(515,515,10,10);
		CBgragh.fillOval(515,115,10,10);
		CBgragh.fillOval(115,515,10,10);
		CBgragh.fillOval(315,315,10,10);
		
		for(int i=0;i<15;i++){									 	// 画出棋子 repaint 悔棋用
			for(int j=0;j<15;j++){
				if(this.pos[i][j]==1){
					CBgragh.setColor(Color.black);
					CBgragh.fillOval(i*40+25, j*40+25, 30, 30);
				}else if (this.pos[i][j]==2) {
					CBgragh.setColor(Color.white);
					CBgragh.fillOval(i*40+25, j*40+25, 30, 30);
				}
			}
		}
		if (cbRecd.size()!=0) {
			 Vector lMove= cbRecd.getLast();
			 int x=(int)lMove.elementAt(0);
			 int y=(int)lMove.elementAt(1);
			 highlight(CBgragh,x-1,y-1);		 
		}	
	}
	public void setReadRec() {
		this.readRec = 1;
	}
}
class OperatBoard extends JPanel{									//基本操作面板
	private JTextArea recdTxa=new JTextArea();
	private JScrollPane texPan=new JScrollPane(recdTxa);
	
	class TakeBackChess implements ActionListener{					//监听实现
		ChessBoard cBoard;
		private int a=0;
		public TakeBackChess(ChessBoard cBoard) {
			this.cBoard=cBoard;
		}
		public TakeBackChess(ChessBoard cBoard ,int a) {
			this.cBoard=cBoard;
			this.a=a;
		}
		@Override
		public void actionPerformed(ActionEvent e ) {
			if (a==1) {
				if (cBoard.GG() == 0) {
					if (cBoard.cbRecd.size() > 0) {
						cBoard.take1Back(); //调用悔棋方法
					}
				} 
			}else {
				if (cBoard.GG() == 0) {
					if (cBoard.cbRecd.size() >=2) {
						cBoard.take1Back(); 
						cBoard.take1Back(); 
					}
				} 				
			}
			LinkedList<Vector> cbList=cBoard.getCbRec();
			int i=1;
			String  str="";
			for (Vector vec : cbList) {
				str+=("第"+i+"手,");
				if (i%2==1) {
					str+="黑>>";
				}else {
					str+="白>>";
				}
				str+=(""+vec.elementAt(1)+"行,"+vec.elementAt(0)+"列"+"\n");
				i++;
			}
			recdTxa.setText(str);
		}
	}
	class ShowRecdLis extends MouseAdapter{
		private ChessBoard cB;
		public ShowRecdLis( ChessBoard cb) {
			// TODO Auto-generated constructor stub
			cB=cb;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			new Thread(){
				@Override
				public void run() {
					while(cB.getReady()==0){
						try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					LinkedList<Vector> cbList=cB.getCbRec();
					int i=1;
					String  str="";
					for (Vector vec : cbList) {
						str+=("第"+i+"手,");
						if (i%2==1) {
							str+="黑>>";
						}else {
							str+="白>>";
						}
						str+=(""+vec.elementAt(1)+"行,"+vec.elementAt(0)+"列"+"\n");
						i++;
					}
					recdTxa.setText(str);
					cB.resetR();
				}
			}.start();
			JScrollBar bar= texPan.getVerticalScrollBar();
			bar.setValue(bar.getMaximum());							//保持滚动条最下
		}
	}
	class Tip extends MouseAdapter{
		private ChessBoard cB;
		public Tip( ChessBoard cb) {
			// TODO Auto-generated constructor stub
			cB=cb;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			
			if (cB.GG()!=0) {
				((JButton)e.getSource()).removeMouseListener(this);
				System.out.println("移除——————");
				return;
				
			}
			if (cB.playFirst&&cB.whoNext()==0||!cB.playFirst&&cB.whoNext()==1) {	//玩家先 或玩家后
				if(cB.getpID()==0){
					Graphics2D cbg=(Graphics2D)cB.getGraphics();
					cB.highlight(cbg, 7, 7, Color.red);
					return;
				};
				GameTree5 gT=new GameTree5(cB);
				Vector<Integer> vec=gT.searchTree();
				Graphics2D cbg=(Graphics2D)cB.getGraphics();		//获得棋盘面板图片
						//System.out.println("x="+x+"  y="+y);
				if (cB.playFirst&&cB.whoNext()==0||!cB.playFirst&&cB.whoNext()==1) {
					cB.highlight(cbg, vec.elementAt(0), vec.elementAt(1), Color.red);					
				}
			}
		}
	}
	class SetAIFirst implements ActionListener{
		private ChessBoard cB;
		public SetAIFirst(ChessBoard cb) {
			// TODO Auto-generated constructor stub
			cB=cb;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (cB.getpID()==0) {
				cB.playChg();
				System.out.println("已设为电脑先手");
				cB.movec(7, 7);
				cB.repaint();
			}
			System.out.println("移除先手监听");
			((JButton)e.getSource()).removeActionListener(this);			
		}
	}
	public OperatBoard(ChessBoard cBoard ,int a){				//单人游戏操作面板  或 人机
		this.setLayout(new GridLayout(2,1));
		this.setPreferredSize(new Dimension(150, 650));			//dimension setsize	 设面板大小
		Dimension Dbt=new Dimension(100, 20);
		Container container=this;
		JPanel op1=new JPanel();
		JPanel op2=new JPanel();
		cBoard.addMouseListener(new ShowRecdLis(cBoard));
		recdTxa.setEditable(false);
		texPan.setPreferredSize(new Dimension(150, 280));
		texPan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		if(a==2){					
			JButton obt1=new JButton("电脑先手");
			JButton obt2=new JButton("悔棋");
			JButton obt3=new JButton("提示");
			obt1.setPreferredSize(Dbt);
			obt2.setPreferredSize(Dbt);
			obt3.setPreferredSize(Dbt);
			obt1.addActionListener(new SetAIFirst(cBoard));
			op1.add(obt1);
			obt2.addActionListener(new TakeBackChess(cBoard,2));
			op1.add(obt2);
			obt3.addMouseListener(new Tip(cBoard));
			op1.add(obt3);
			
			op2.add(texPan);
		}else{
			JButton obt2=new JButton("悔棋");
			obt2.setPreferredSize(Dbt);
			obt2.addActionListener(new TakeBackChess(cBoard,1));
			op1.add(obt2);	
			op2.add(texPan);
		}
		container.add(op1);
		container.add(op2);
		this.setVisible(true);
		}
	}
class GoGame extends JFrame{		//单人游戏的界面
	public GoGame(){
		super("双人对弈");
		this.setBounds(100,100,820,710);
		this.setResizable(false);
		Container container= this.getContentPane();
		container.setLayout(new FlowLayout());
//		JPanel p1=new JPanel();
//		Dimension d=new Dimension(800,650);	
		ChessBoard cBoard=new ChessBoard(0,this);
		OperatBoard opBoard=new OperatBoard(cBoard,1);
		container.add(cBoard);
		container.add(opBoard);
		this.setVisible(true);
		this.setDefaultCloseOperation(2);//0.1.2.3
		this.addWindowListener(new EndGame());
	}
}
class GoGame2 extends JFrame{		//双人游戏
	public GoGame2(){
		super("人机对弈");
		this.setBounds(100, 100,820,710);
		this.setResizable(false);
		Container container= this.getContentPane();
		container.setLayout(new FlowLayout());
		ChessBoard cBoard=new ChessBoard(0,this);
		container.add(cBoard);
		container.add(new OperatBoard(cBoard,2));
		this.setVisible(true);
		this.setDefaultCloseOperation(2);//0.1.2.3
		this.addWindowListener(new EndGame());
	}
}