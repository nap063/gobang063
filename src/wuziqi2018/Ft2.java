package wuziqi2018;

public class Ft2 {
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		int pos[][]={
//				{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},//0	1,0,1,1
//				{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},//1
//				{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
//				{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},//3
//				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//5
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//7
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//9
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//11
//				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//13
//				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},};
//		System.out.println(fig(pos, 1));
//		
//	}

	public  boolean testPos1(int[][] pos,int x,int y){
		int iMin=(x-1<=0?0:(x-1));
		int iMax=(x+1>=14?14:(x+1));
		int jMin=(y-1<=0?0:(y-1));
		int jMax=(y+1>=14?14:(y+1));
		int i=iMin;
		for(;i<=iMax;i++){
			for(int j=jMin;j<=jMax;j++){
				if (pos[i][j]!=0) {									//�����̣�i,j����Ϊ��ʱ
					if((x-i<2||i-x<2)&&(y-j<2||j-y<2))				// ���Ϊ1�ĵط���������
						{	
						return true;
						}
				}
			}
		}
		return false;
	}
	static 	public int fig(int [][] chessPos ,int Max){						//��������  max�ڵ���
		//System.out.println("��ǰ����:"+Max);
		int max=Max;												//��һ��
		int total=0;
		int bigCol=0;												//������ݶԽ� ÿ����������Ȩ��
		int bigRow=0;
		int lDiagonal=0;
		int rDiagonal=0;
		int posWeight=0;
		int chMt3=0;												//���ɱ��  ��������
		int chMt3F=0;												//����
		int chMt4=0;												
		for(int i=0;i<11;i++){									//�ж��ҶԽ�
			int rWeight=0;
			for(int j=4;j<15;j++){
				int tal=0;
				for(int b=0;b<5;b++){
					if(chessPos[i+b][j-b]!=max&&chessPos[i+b][j-b]!=0){
						tal=-1;
						break;
					}else {
						tal+=chessPos[i+b][j-b];
					}
				}
				if(tal<0){
					continue;
				}
				else if (tal==3*max) {								//3���� ������������
					if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0){	//���ڿ����� ���ɻ�2 ��3
						if( (chessPos[i][j]*chessPos[i+1][j-1]*chessPos[i+2][j-2]!=0)|| 
							(chessPos[i+2][j-2]*chessPos[i+3][j-3]*chessPos[i+4][j-4]!=0)){//����mei��0
							rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3 ����
							}	
						else if (chessPos[i][j]==0) {					//����Ϊ��
							if ((i==10||j==4)||chessPos[i+5][j-5]!=0) {	//�������±߽�  ���ұ����� 
								rWeight=(rWeight>=80?rWeight:80 );		//��3
							}else {								
								rWeight=(rWeight>=200 ? rWeight : 200 );	//������3+
								chMt3++;
								chMt3F++;
								}
							}							
						else if(chessPos[i+4][j-4]==0) {				// ���ϱ�Ϊ��	
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// ���±߽� ���������
								rWeight=(rWeight>=80?rWeight:80 );		// ��3
							}else {					
								rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
								chMt3++;
								chMt3F++;
							}
						}
					}
					else{
						rWeight=(rWeight>=250 ? rWeight : 250 );		//��3
						chMt3++;
					}
				}
				else if (tal==4*max) {								//4���� ������������
					chMt4++;
					if (chessPos[i][j]==0) {						//�����ж����ϱ߿�
						if ((j==4||i==10)||chessPos[i+5][j-5]!=0) {//�������±߽�  ���ұ����� 
							rWeight=(rWeight>=240?rWeight:240 );	//��4
						}
						else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
						}
					}else if(chessPos[i+4][j-4]==0) {				// ���±�Ϊ��
						if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// ���ϱ߽� ���������
							rWeight=(rWeight>=240?rWeight:240 );	//��4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
					}else {											
						rWeight=(rWeight>=230 ? rWeight : 230 );	//�м��п�  ���ɻ�3++
					}	
				}
				else if (tal==5*max) {								//5���� ������������
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			rDiagonal=(rDiagonal>=rWeight?rDiagonal:rWeight);
		}
		for(int i=0;i<15;i++){										//��������						
			for(int j=0;j<15;j++){
				if (chessPos[i][j]==max) {
					int xp=Math.abs(7-i);
					int yp=Math.abs(7-j);
					if(xp>yp){
						posWeight+=(7-xp);
					}else
						posWeight+=(7-yp);				
				}
			}
		}
		total=bigRow+bigCol+rDiagonal+lDiagonal+posWeight;
		int cm=0;
		if (chMt3+chMt4>2) {
			if (chMt4>=2) {
				cm=posWeight+5000;
			}else if(chMt4==1){
				cm=posWeight+2500;
			}else {
				cm=posWeight+2000;
			}
		}
		System.out.println(Max+"����"+"��"+bigCol+"��"+bigRow+"��"+lDiagonal+"��"+rDiagonal+"PW:"+posWeight);
	return total;
	}

}
