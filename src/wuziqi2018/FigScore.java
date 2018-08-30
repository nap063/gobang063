package wuziqi2018;

public class FigScore {
	ChessBoard cB;
	public FigScore(ChessBoard cBoard) {
		// TODO Auto-generated constructor stub
		cB=cBoard;
	}
	public int getScore(){
		if(cB.playFirst){
			return figR(cB.getPos(), 1);
		}else {
			return figR(cB.getPos(), 2);
		}
	}
	public int figR(int [][] chessPos ,int Max){						//��������  max�ڵ���
		
		int max=Max;												//��һ��
		int total=0;
		int bigCol=0;												//������ݶԽ� ÿ����������Ȩ��
		int bigRow=0;
		int lDiagonal=0;
		int rDiagonal=0;
		for(int i=0;i<15;i++){										//������������ֵ 
			int rWeight=0;
			for(int j=0;j<=10;j++){									//����������п���5���ӵ�ȨֵMax
				int tal=0;
				for(int b=0;b<5;b++){								//��ǰ5��
					if(chessPos[i][j+b]!=max&&chessPos[i][j+b]!=0){	//���ڶԷ�����
						tal=-1;
						break;										//�޷�ʤ��
					}		
					else{		
						tal+=chessPos[i][j+b];					
					}
				}
				if(tal<0){
					continue;											//��ֹ��ǰ5�ӱ���
				}else if (tal==max) {								//һ����
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//������ ������������
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3���� ������������
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4���� ������������
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5���� ������������
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			bigCol+=rWeight;
		}
		for(int i=0;i<15;i++){										//��������ֵ 
			int rWeight=0;
			for(int j=0;j<=10;j++){									//����������п���5���ӵ�ȨֵMax
				int tal=0;
				for(int b=0;b<5;b++){								//��ǰ5��
					if(chessPos[j+b][i]!=max&&chessPos[j+b][i]!=0){	//���ڶԷ�����
						tal=-1;
						break;										//�޷�ʤ��
					}
					else{
						tal+=chessPos[j+b][i];					
					}
				}
				if(tal<0){
					continue;											//��ֹ��ǰ5�ӱ���
				}else if (tal==max) {								//һ����
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//������ ������������
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3���� ������������
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4���� ������������
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5���� ������������
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			bigRow+=rWeight;
		}
		for(int i=0;i<11;i++){										//�ж���Խ�
			int rWeight=0;
			for(int j=0;j<11;j++){	
				int tal=0;
				for(int b=0;b<5;b++){
					if(chessPos[i+b][j+b]!=max&&chessPos[i+b][j+b]!=0){
						tal=-1;
						break;
					}else {
						tal+=chessPos[i+b][j+b];	
					}		
				}
				if (tal<0) {
					continue;	
				}else if (tal==max) {								//һ����
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//������ ������������
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3���� ������������
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4���� ������������
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5���� ������������
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			lDiagonal+=rWeight;
		}
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
				else if (tal==max) {								//һ����
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//������ ������������
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3���� ������������
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4���� ������������
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5���� ������������
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			rDiagonal+=rWeight;
		}
		int posWeight=0;
		for(int i=0;i<15;i++){
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
		return total;
	}

}
