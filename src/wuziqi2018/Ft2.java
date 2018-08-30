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
				if (pos[i][j]!=0) {									//当棋盘（i,j）不为空时
					if((x-i<2||i-x<2)&&(y-j<2||j-y<2))				// 间隔为1的地方可以落子
						{	
						return true;
						}
				}
			}
		}
		return false;
	}
	static 	public int fig(int [][] chessPos ,int Max){						//棋盘数组  max节点是
		//System.out.println("当前落子:"+Max);
		int max=Max;												//哪一方
		int total=0;
		int bigCol=0;												//计算横纵对角 每个方向最大的权重
		int bigRow=0;
		int lDiagonal=0;
		int rDiagonal=0;
		int posWeight=0;
		int chMt3=0;												//检测杀棋  活三和四
		int chMt3F=0;												//眠三
		int chMt4=0;												
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
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0){	//存在靠边子 当成活2 冲3
						if( (chessPos[i][j]*chessPos[i+1][j-1]*chessPos[i+2][j-2]!=0)|| 
							(chessPos[i+2][j-2]*chessPos[i+3][j-3]*chessPos[i+4][j-4]!=0)){//当中mei有0
							rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3 连着
							}	
						else if (chessPos[i][j]==0) {					//坐下为空
							if ((i==10||j==4)||chessPos[i+5][j-5]!=0) {	//到达右下边界  或右边有子 
								rWeight=(rWeight>=80?rWeight:80 );		//冲3
							}else {								
								rWeight=(rWeight>=200 ? rWeight : 200 );	//当成眠3+
								chMt3++;
								chMt3F++;
								}
							}							
						else if(chessPos[i+4][j-4]==0) {				// 右上边为空	
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左下边界 或左边有子
								rWeight=(rWeight>=80?rWeight:80 );		// 冲3
							}else {					
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
								chMt3F++;
							}
						}
					}
					else{
						rWeight=(rWeight>=250 ? rWeight : 250 );		//活3
						chMt3++;
					}
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					chMt4++;
					if (chessPos[i][j]==0) {						//首先判断左上边空
						if ((j==4||i==10)||chessPos[i+5][j-5]!=0) {//到达右下边界  或右边有子 
							rWeight=(rWeight>=240?rWeight:240 );	//冲4
						}
						else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[i+4][j-4]==0) {				// 右下边为空
						if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左上边界 或左边有子
							rWeight=(rWeight>=240?rWeight:240 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=230 ? rWeight : 230 );	//中间有空  当成活3++
					}	
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			rDiagonal=(rDiagonal>=rWeight?rDiagonal:rWeight);
		}
		for(int i=0;i<15;i++){										//靠近中心						
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
		System.out.println(Max+"方的"+"纵"+bigCol+"横"+bigRow+"左"+lDiagonal+"右"+rDiagonal+"PW:"+posWeight);
	return total;
	}

}
