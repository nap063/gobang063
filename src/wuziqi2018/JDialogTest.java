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
  //实例化一个JDialog类对象，指定对话框的父窗体，窗体标题和类型
  super();
  Container container=getContentPane();
  container.setBackground(Color.green);
  container.add(new JLabel("这是一个对话框"));
  setBounds(120,120,100,100);
 }
  public void MyFrame(){
  JFrame jf=new JFrame();//实例化JFrame对象
  Container container=jf.getContentPane();//将窗体转化为容器
  JButton jb=new JButton("弹出对话框");
  jb.setBounds(10, 10, 100, 20);//设置按钮的大小
  jb.addActionListener(new ActionListener() {
   //定义匿名内部类，这样才可以点击出现反应
   @Override
   public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    new JDialogTest().setVisible(true);;
   }
  });
  container.add(jb);//将按钮添加到容器中，这点非常重要，不然无法显示
  //设置容器的结构的特性
  jf.setTitle("这是窗体转化为容器");
  jf.setSize(200,200);//设置容器的大小
  jf.setVisible(true);//使窗体可见
  //设置窗体的关闭模式
  jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 }
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  JDialogTest jd=new JDialogTest();
  jd.MyFrame();
 }
 
}