package wuziqi2018;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RunGame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Father F1=new Father();
	}
}
class DiaTest extends JDialog{	//������
	 public DiaTest(String sss) {
		// TODO Auto-generated constructor stub
		 super();
		 this.setBounds(20, 20, 40, 40);
		 this.getContentPane().add(new Label(sss));
		 this.setVisible(true);
	}
}
////������ļ���
class ShowRecord implements ActionListener{		//����ʵ��
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(Father.alread2==false){
			HighScores D1=new HighScores();
			Father.alread2=true;
		}
	}
}
class StartGame implements ActionListener{		//����ʵ�� ��ʼ����
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(Father.alread1==false){
			new GoGame();			
			Father.alread1=true;
		}else {
			DiaTest alert1=new DiaTest("������");	//���Ѿ���ʼ��Ϸ ��������
			alert1.setBounds(200, 100, 100, 100);
			alert1.setDefaultCloseOperation(2);
			alert1.setVisible(true);
		}
	}
}
class StartGame2 implements ActionListener{		//˫�� 
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(Father.alread1==false){
			new GoGame2();			
			Father.alread1=true;
		}else {
			DiaTest alert1=new DiaTest("������");	//���Ѿ���ʼ��Ϸ ��������
			alert1.setBounds(200, 100, 100, 100);
			alert1.setDefaultCloseOperation(2);
			alert1.setVisible(true);
		}
	}
}
class Father extends JFrame{   //��ʼ����
	private String name="father";
	static boolean alread1=false;
	static boolean alread2=false;
	public Father(){
		super("����������");
		this.setBounds(400,300,600,400);
		this.getContentPane().setLayout(new GridLayout(3, 3));
		//this.getContentPane().setBackground(Color.white);//��ɫ
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel P1=new JPanel();
		JPanel P2=new JPanel();
		JPanel p3=new JPanel();
		P1.add(new JLabel("�鿴��������"));
		JButton bt1 =new JButton("Ӣ �� ��");
		bt1.addActionListener(new ShowRecord());//ע�����
		P1.add(bt1);
		P2.add(new JLabel("��ʼ˫����Ϸ"));
		JButton bt2=new JButton("Start 1!");
		P2.add(bt2);
		bt2.addActionListener(new StartGame());//ע����� ��ʼ����
		
		p3.add(new JLabel("��ʼ�˻�����"));
		JButton bt3=new JButton("Start 2!");
		p3.add(bt3);
		bt3.addActionListener(new StartGame2());
		
		this.getContentPane().add(new JPanel());
		this.getContentPane().add(P1);
		this.getContentPane().add(new JPanel());
		this.getContentPane().add(new JPanel());
		this.getContentPane().add(P2);
		this.getContentPane().add(new JPanel());
		this.getContentPane().add(new JPanel());
		this.getContentPane().add(p3);
		this.getContentPane().add(new JPanel());
		this.setVisible(true);
	}
}
 