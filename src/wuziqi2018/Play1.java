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

class EndBoard extends WindowAdapter{						//�����߷ְ�
	public void windowClosing(WindowEvent e){
		Father.alread2=false;								//��ֹ�ظ���
	}
}
class EndGame extends WindowAdapter{						//�����Ծ�
	public void windowClosing(WindowEvent e){
		Father.alread1=false;
	}
}
class HighScores{
	public HighScores(){
														//�������ʾ������� ���ù��з���
		ArrayList<HighScore> hllist=SeriUtil.showFi();
		JTextArea HsText=new JTextArea();
		HsText.setFont(new Font("Dialog",0,20));
		String HsRec="";
		for( int i=0;i<hllist.size() ;i++ ){
			HsRec+=("��"+(i+1)+"λ:"+hllist.get(i)+"��\n");
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

class ChessBoard extends JPanel implements GobangRule {					//�������
	private int pos [][]=new int[15][15];						//����������
	private static int blank= 40;								//���ü��
	private int pID=0;	 //���ü����Ⱥ���	
	LinkedList<Vector> cbRecd= new LinkedList<>();				//��¼���岽��
	boolean playFirst=true;
	private int readRec=0;										//�������  ������ʾ
	private GoGame goGame1;										//������ϷFrame
	private GoGame2 goGame2;
	private int victor=0;								
	
	public ChessBoard(int c,GoGame2 goGame2) {					//�����˻�����	
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
	public ChessBoard(int c,GoGame goGame) {					//�����û���˫�˶������ط���
		this.goGame1=goGame;
		if(c==0){
			this.setBackground(new Color(232,220,0));			//����������ɫ
		}else {
			this.setBackground(Color.blue);	
		}
		this.setPreferredSize(new Dimension(650, 650));
		//this.setBounds(5,5,630,630);					//��ο��Ƕ�̬��С
		this.setBorder(BorderFactory.createLineBorder(new Color(180,130,0), 5));//�������̱߿�
		this.addMouseListener(new chessMove(this));
		this.setVisible(true);
	}
	public void take1Back(){							//����������巽��
		Vector<Integer> lastMov = cbRecd.removeLast(); //����֮������ɾ�����  ����������� repaint()
		int x = (int) lastMov.elementAt(0);
		int y = (int) lastMov.elementAt(1);
		System.out.println("rm��1=" + lastMov.elementAt(0));
		System.out.println("rm��2=" + lastMov.elementAt(1));
		pos[x-1][y-1]=0;
		pID--;
		repaint();
	}
	public int GG(){											//Ӯ���ж�
		if(victor==1)
			return 1;
		else if (victor==2) {
			return 2;
		}else {
			return 0;
		}
	}
	public void judgeSieg(){								    //�ж��Ƿ����ʤ��
		System.out.println("�ж�");
		for(int i=0;i<15;i++){
			if (i==7) {
				for(int k=0;k<15;k++){
				
				}
			}
			for(int j=0;j<11;j++){			
				int total=1;
				for(int k=0;k<5;k++){						   // �������ж� 5�����
					total=pos[i][j+k]*total;	
				}
				
				if (total==1) {			//System.out.println((i+1)+"��"+(j+1)+"��"+pos[i][j]+pos[i][j+1]+pos[i][j+2]+pos[i][j+3]+pos[i][j+4]);
					victor=1;
					break;
				}
				else if (total==32) {
					victor=2;
					break;
				}
				total=1;									
				for(int k=0;k<5;k++){						// �ٺ����ж�5��
					total=pos[j+k][i]*total;				
				}
				if (total==1) {			//System.out.println((i+1)+"��"+(j+1)+"��"+pos[j][i]+pos[j+1][i]+pos[j+1][i]+pos[j+1][i]+pos[j+1][i]);
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
					for(int k=0;k<5;k++){						//�����Խ�
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
						total*=pos[i-k+4][j+k];					//����ҶԽ�
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
			Vector<Integer> movPos=new Vector<Integer>(2);				//��¼ÿһ��
			if (pID%2==0) {											//��������
				cbg.setColor(Color.black);
				cbg.fillOval(x*40+25, y*40+25, 30, 30);
				pos[x][y]=1;
				movPos.add(x+1);
				movPos.add(y+1);
				cbRecd.add(movPos);
				pID++;
			}else {
				cbg.setColor(Color.white);							//��������
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
	public int whoNext(){										//��һ��˭��
		return pID%2;
	}
	public int getpID() {
		return pID;
	}
	class chessMove extends MouseAdapter{						// ���Ӽ����� �ڲ�
		private ChessBoard cb;
		private int x=0,y=0;
		public chessMove(ChessBoard cb) {
			this.cb=cb;
		}
		public void mousePressed(MouseEvent e){															//ȷ������λ��
			x=e.getX();
			y=e.getY();
			if(x>600)x=600;
			else if(x<40)x=40;
			if(y>600)y=600;
			else if(y<40)y=40;
			Graphics2D cbg=(Graphics2D)cb.getGraphics();		//����������ͼƬ
			System.out.println("x="+x+"  y="+y);
			int col,row;
			col=(x-20)/40;
			row=(y-20)/40;System.out.println("col"+(col+1)+"  row"+(row+1));
			movec(col, row);
			setReadRec();
			repaint();
			System.out.println("pid="+pID);
			judgeSieg();									//�ж�Ӯ��
			System.out.println("ʤ����="+cb.victor);
			if(cb.victor!=0){
				cb.removeMouseListener(this);
				System.out.println("VVV="+cb.victor);
				JDialog jD=new JDialog();
				jD.setTitle("��Ϸ������");
				jD.setBounds(450,350,300,100);
				String st="";
				if (victor==1) {
					st="�ڷ���ʤ��";
				}else {
					st="�׷���ʤ��";
				}
				JLabel jL=new JLabel(st);
				jL.setFont(new Font("Dialog",0,20));
				jD.add(jL);
				jD.setVisible(true);
			}
		}
	}
	public void showBlack(){
		System.out.print("���壺");
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if (pos[i][j]==1) {
					System.out.println("("+i+","+j+"),");
				}
			}
		}
	}
	public void showWhite(){
		System.out.print("���壺");
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
			//�ڵ�ǰ����ѡ��������һ��
			this.cb=cB;
			}
		public void mouseClicked(MouseEvent e){	
			int col,row;
			if ((victor==0)&&(playFirst&&whoNext()==0||!playFirst&&whoNext()==1)) {	//����� ����Һ�
				x=e.getX();
				y=e.getY();
				if(x>600)x=600;
				else if(x<40)x=40;
				if(y>600)y=600;
				else if(y<40)y=40;
				Graphics2D cbg=(Graphics2D)cb.getGraphics();		//����������ͼƬ
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
																	//�ж�Ӯ��
			System.out.println("ʤ����="+cb.victor);
			if(cb.victor==2&&!cb.playFirst||cb.victor==1&&cb.playFirst){
				removeMouseListener(this);
				System.out.println("VVV="+cb.victor);
				JDialog jD=new JDialog();
				jD.setTitle("��Ϸ������");
				jD.setBounds(450,350,300,100);
				String st="";
				int  scr=new FigScore(cb).getScore();
				
				SeriUtil.writeFi(new HighScore(scr));
				if (victor==1) {
					st="�ڷ���ʤ���ܷ�Ϊ"+scr;
				}else {
					st="�׷���ʤ���ܷ�Ϊ"+scr;
				}
				JLabel jL=new JLabel(st);
				jL.setFont(new Font("Dialog",0,20));
				jD.add(jL);
				jD.setVisible(true);
			}else if(victor!=0){
				JDialog jD=new JDialog();
				jD.setTitle("��Ϸ������");
				jD.setBounds(450,350,200,100);
				JLabel jL=new JLabel("���Ի�ʤ��");
				jL.setFont(new Font("Dialog",0,20));
				jD.add(jL);
				jD.setVisible(true);
				removeMouseListener(this);
			}
		}
	}
	public void paint(Graphics graphics){							//����JPanel��paint���� ����
		super.paint(graphics);
		Graphics2D CBgragh=(Graphics2D)graphics;					//ǿ��ת����������
		for(int i=0;i<row;i++){										//15��
			CBgragh.drawLine(40,blank*(1+i),600,blank*(1+i));
			CBgragh.drawString(String.valueOf(i+1), 10, blank*(1+i));
		}
		for(int j=0;j<col;j++){										//15��
			CBgragh.drawLine(blank*(1+j),40,blank*(1+j),600);
			CBgragh.drawString(String.valueOf(j+1), blank*(1+j), 20);
		}
		CBgragh.fillOval(115,115,10,10);
		CBgragh.fillOval(515,515,10,10);
		CBgragh.fillOval(515,115,10,10);
		CBgragh.fillOval(115,515,10,10);
		CBgragh.fillOval(315,315,10,10);
		
		for(int i=0;i<15;i++){									 	// �������� repaint ������
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
class OperatBoard extends JPanel{									//�����������
	private JTextArea recdTxa=new JTextArea();
	private JScrollPane texPan=new JScrollPane(recdTxa);
	
	class TakeBackChess implements ActionListener{					//����ʵ��
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
						cBoard.take1Back(); //���û��巽��
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
				str+=("��"+i+"��,");
				if (i%2==1) {
					str+="��>>";
				}else {
					str+="��>>";
				}
				str+=(""+vec.elementAt(1)+"��,"+vec.elementAt(0)+"��"+"\n");
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
						str+=("��"+i+"��,");
						if (i%2==1) {
							str+="��>>";
						}else {
							str+="��>>";
						}
						str+=(""+vec.elementAt(1)+"��,"+vec.elementAt(0)+"��"+"\n");
						i++;
					}
					recdTxa.setText(str);
					cB.resetR();
				}
			}.start();
			JScrollBar bar= texPan.getVerticalScrollBar();
			bar.setValue(bar.getMaximum());							//���ֹ���������
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
				System.out.println("�Ƴ�������������");
				return;
				
			}
			if (cB.playFirst&&cB.whoNext()==0||!cB.playFirst&&cB.whoNext()==1) {	//����� ����Һ�
				if(cB.getpID()==0){
					Graphics2D cbg=(Graphics2D)cB.getGraphics();
					cB.highlight(cbg, 7, 7, Color.red);
					return;
				};
				GameTree5 gT=new GameTree5(cB);
				Vector<Integer> vec=gT.searchTree();
				Graphics2D cbg=(Graphics2D)cB.getGraphics();		//����������ͼƬ
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
				System.out.println("����Ϊ��������");
				cB.movec(7, 7);
				cB.repaint();
			}
			System.out.println("�Ƴ����ּ���");
			((JButton)e.getSource()).removeActionListener(this);			
		}
	}
	public OperatBoard(ChessBoard cBoard ,int a){				//������Ϸ�������  �� �˻�
		this.setLayout(new GridLayout(2,1));
		this.setPreferredSize(new Dimension(150, 650));			//dimension setsize	 ������С
		Dimension Dbt=new Dimension(100, 20);
		Container container=this;
		JPanel op1=new JPanel();
		JPanel op2=new JPanel();
		cBoard.addMouseListener(new ShowRecdLis(cBoard));
		recdTxa.setEditable(false);
		texPan.setPreferredSize(new Dimension(150, 280));
		texPan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		if(a==2){					
			JButton obt1=new JButton("��������");
			JButton obt2=new JButton("����");
			JButton obt3=new JButton("��ʾ");
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
			JButton obt2=new JButton("����");
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
class GoGame extends JFrame{		//������Ϸ�Ľ���
	public GoGame(){
		super("˫�˶���");
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
class GoGame2 extends JFrame{		//˫����Ϸ
	public GoGame2(){
		super("�˻�����");
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