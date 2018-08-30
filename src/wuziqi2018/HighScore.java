package wuziqi2018;

import java.io.Serializable;


class HighScore implements Serializable ,Comparable<HighScore>{
	final private long serialVersionUID=1L;
	
	private String name="John S";
	int a=0;
	
	public HighScore(String name, int a) {
		
		this.name = name;
		this.a = a;
	}

	public HighScore(int a) {
		// TODO Auto-generated constructor stub
		this.a=a;
	}

	@Override
	public int compareTo(HighScore o) {	
		return o.a-this.a;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return " "+a+"";
	}

}

