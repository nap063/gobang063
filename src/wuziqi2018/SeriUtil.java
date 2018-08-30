package wuziqi2018;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SeriUtil {
	/*
	 * 1.��ȡԭ�浵��list ����ԭ�浵�е�list��Ƚ�
	 * 2.�����
	 * 3.���´���
	 * */
	public static void writeFi(HighScore Hic){	//��ooolist��ԭ�ϲ�	
		String fiName="record.txt";
		File fi=new File(fiName);
		if(!fi.exists()){
			wriNewFi(fiName);
		}
		FileOutputStream fos1=null;
		ObjectOutputStream oos1=null;
		ArrayList<HighScore> list=new ArrayList<HighScore>();
		list.add(Hic);
		ArrayList<HighScore> rawList=readIFi();
		if (rawList==null) {
			System.out.println("ԭ�浵Ϊ��");
		}else {
			list.addAll(rawList);
			System.out.println("ԭ�浵��Ϊ��,�������");
			for (HighScore ooo : rawList) {
				System.out.println(ooo);	
			}
		}	
		Collections.sort(list);					//���� ���� 5��
		int leth=8;
		while (list.size()>leth) {
			list.remove(list.size()-1);
		}
		System.out.println("Ҫ�����list");
		for (HighScore ooo : list) {
			System.out.println(ooo);	
		}
		try {
			fos1=new FileOutputStream(fiName);
			oos1=new ObjectOutputStream(fos1);
			oos1.writeObject(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				oos1.close();
				fos1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		 
	}
	public static void wriNewFi(String fiName){	//��ooolist��ԭ�ϲ�	
		ArrayList<HighScore> newList=new ArrayList<>();
		newList.add(new HighScore(0));
		try {
			FileOutputStream fos1=new FileOutputStream(fiName);
			ObjectOutputStream oos1=new ObjectOutputStream(fos1);
			oos1.writeObject(newList);
			oos1.close();
			fos1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			finally{

		}		 
	}
	@SuppressWarnings("finally")
	public static ArrayList<HighScore> readIFi() {		//��ȡԭ����list
		FileInputStream fis1=null;
		ObjectInputStream ois1=null;
		ArrayList<HighScore> oList=null;
		System.out.println("���浵��");
		try {
			fis1=new FileInputStream("record.txt");
			ois1=new ObjectInputStream(fis1);
			oList=(ArrayList<HighScore>) ois1.readObject();
			for (HighScore ooo : oList) {
				System.out.println("Ooo����"+ooo);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				ois1.close();
				fis1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return oList;
		}
	}
	public static ArrayList<HighScore> showFi() {		//ԭ����list
		String fiName="record.txt";
		FileInputStream fis1=null;
		ObjectInputStream ois1=null;
		ArrayList<HighScore> oList=null;
		System.out.println("���浵��");
		try {
			fis1=new FileInputStream(fiName);
			ois1=new ObjectInputStream(fis1);
			oList=(ArrayList<HighScore>) ois1.readObject();
			System.out.println("��ʾ�浵����");
			for (HighScore ooo : oList) {
				System.out.println(ooo);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				ois1.close();
				fis1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return oList;
	}
}