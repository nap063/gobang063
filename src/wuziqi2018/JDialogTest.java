package wuziqi2018;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class JDialogTest extends JDialog{
  public JDialogTest(){
  //ʵ����һ��JDialog�����ָ���Ի���ĸ����壬������������
  super();
  Container container=getContentPane();
  container.setBackground(Color.green);
  container.add(new JLabel("����һ���Ի���"));
  setBounds(120,120,100,100);
 }
  public void MyFrame(){
  JFrame jf=new JFrame();//ʵ����JFrame����
  Container container=jf.getContentPane();//������ת��Ϊ����
  JButton jb=new JButton("�����Ի���");
  jb.setBounds(10, 10, 100, 20);//���ð�ť�Ĵ�С
  jb.addActionListener(new ActionListener() {
   //���������ڲ��࣬�����ſ��Ե�����ַ�Ӧ
   @Override
   public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    new JDialogTest().setVisible(true);;
   }
  });
  container.add(jb);//����ť��ӵ������У����ǳ���Ҫ����Ȼ�޷���ʾ
  //���������Ľṹ������
  jf.setTitle("���Ǵ���ת��Ϊ����");
  jf.setSize(200,200);//���������Ĵ�С
  jf.setVisible(true);//ʹ����ɼ�
  //���ô���Ĺر�ģʽ
  jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 }
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  JDialogTest jd=new JDialogTest();
  jd.MyFrame();
 }
 
}