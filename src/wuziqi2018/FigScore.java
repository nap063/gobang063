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
	public int figR(int [][] chessPos ,int Max){						//棋盘数组  max节点是
		
		int max=Max;												//哪一方
		int total=0;
		int bigCol=0;												//计算横纵对角 每个方向最大的权重
		int bigRow=0;
		int lDiagonal=0;
		int rDiagonal=0;
		for(int i=0;i<15;i++){										//计算最大纵向估值 
			int rWeight=0;
			for(int j=0;j<=10;j++){									//计算该纵所有可能5个子的权值Max
				int tal=0;
				for(int b=0;b<5;b++){								//当前5子
					if(chessPos[i][j+b]!=max&&chessPos[i][j+b]!=0){	//存在对方棋子
						tal=-1;
						break;										//无法胜利
					}		
					else{		
						tal+=chessPos[i][j+b];					
					}
				}
				if(tal<0){
					continue;											//终止当前5子遍历
				}else if (tal==max) {								//一个子
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			bigCol+=rWeight;
		}
		for(int i=0;i<15;i++){										//计算横向估值 
			int rWeight=0;
			for(int j=0;j<=10;j++){									//计算该列所有可能5个子的权值Max
				int tal=0;
				for(int b=0;b<5;b++){								//当前5子
					if(chessPos[j+b][i]!=max&&chessPos[j+b][i]!=0){	//存在对方棋子
						tal=-1;
						break;										//无法胜利
					}
					else{
						tal+=chessPos[j+b][i];					
					}
				}
				if(tal<0){
					continue;											//终止当前5子遍历
				}else if (tal==max) {								//一个子
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			bigRow+=rWeight;
		}
		for(int i=0;i<11;i++){										//判断左对角
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
				}else if (tal==max) {								//一个子
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=6250 ? rWeight : 6250 );
				}	
			}
			lDiagonal+=rWeight;
		}
		for(int i=0;i<11;i++){									//判断右对角
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
				else if (tal==max) {								//一个子
					rWeight=(rWeight>=10 ? rWeight : 10 );
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					rWeight=(rWeight>=50 ? rWeight : 50 );
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					rWeight=(rWeight>=250 ? rWeight : 250 );
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					rWeight=(rWeight>=1250 ? rWeight : 1250 );
				}
				else if (tal==5*max) {								//5个子 不存在其他子
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
