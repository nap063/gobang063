package wuziqi2018;


public class FigTest {

	

	 static	public int fig(int [][] chessPos ,int Max){						//棋盘数组  max节点是
		//System.out.println("当前落子:"+Max);
		int max=Max;												//哪一方
		int total=0;
		int bigCol=0;												//计算横纵对角 每个方向最大的权重
		int bigRow=0;
		int lDiagonal=0;
		int rDiagonal=0;
		int posWeight=0;
		int chMt3=0;												//检测杀棋  活三和四
		int chMt4=0;												
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
				}else if (tal==max) {								//有一个子
					if (chessPos[i][j]!=0||chessPos[i][j+4]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//存在靠边子  当成活1
						if (chessPos[i][j+1]*chessPos[i][j]!=0||
							chessPos[i][j+3]*chessPos[i][j+4]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲二
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//存在靠边子 当成活2 冲3 眠3
						if ((chessPos[i][j+1]*chessPos[i][j+2]*chessPos[i][j]!=0)||
							(chessPos[i][j+4]*chessPos[i][j+2]*chessPos[i][j+3]!=0)){//当中mei0
							rWeight=(rWeight>=50 ? rWeight : 80 );		//冲 3
						}
						else if(chessPos[i][j]==0){					//存在断点 有可能 眠3杀棋
							if (j==10||chessPos[i][j+5]!=0) {			//到达右边界  或右有子
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;				
							}
						}
						else if (chessPos[i][j+4]==0) {
							if (j==0||chessPos[i][j-1]!=0) {			// 左边界 或左边有子
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}					
						}
						else {
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
						}
					}
					else{
						rWeight=(rWeight>=250 ? rWeight : 250 );		//活3
						chMt3++;
					}
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					chMt4++;
					if (chessPos[i][j]==0) {						//首先判断左边空
						if (j==10||chessPos[i][j+5]!=0) {			//到达右边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4
						}
					}else if(chessPos[i][j+4]==0) {					 // 右边为空
						if (j==0||chessPos[i][j-1]!=0) {			// 左边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											//中间有空  当成冲4--
						rWeight=(rWeight>=600 ? rWeight : 600 );
					}
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			bigCol=(bigCol>=rWeight ? bigCol : rWeight );
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
					if (chessPos[j][i]!=0||chessPos[j+4][i]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//存在靠边子
						if (chessPos[j+1][i]*chessPos[j][i]!=0||
							chessPos[j+3][i]*chessPos[j+4][i]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲2	
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );		  //当成活1
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//存在靠边子 当成活2 冲3
						if( (chessPos[j][i]*chessPos[j+1][i]*chessPos[j+2][i]!=0)|| 
							(chessPos[j+4][i]*chessPos[j+3][i]*chessPos[j+2][i]!=0)){//当中mei有0
							rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3//连着
							}	
						else if(chessPos[j][i]==0){	
							if (j==10||chessPos[j+5][i]!=0) {
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}
						}
						else if(chessPos[j+4][i]==0){
							if (j==0||chessPos[j-1][i]!=0) {
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}							
						}
						else {													
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
						}
					}
					else{
						rWeight=(rWeight>=250 ? rWeight : 250 );		//活3
						chMt3++;
					}
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					chMt4++;
					if (chessPos[j][i]==0) {						//首先判断左边空
						if (j==10||chessPos[j+5][i]!=0) {			//到达右边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[j+4][i]==0) {					 // 右边为空
						if (j==0||chessPos[j-1][i]!=0) {			// 左边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=600 ? rWeight : 600 );	//中间有空  当成活3++
					}
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			bigRow=(bigRow>=rWeight ? bigRow : rWeight );
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
					if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){	//存在靠边子  当成活1
						if (chessPos[i+1][j+1]*chessPos[i][j]!=0||
							chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲二
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){		//存在靠边子 当成活2 冲3
						if( (chessPos[i][j]*chessPos[i+1][j+1]*chessPos[i+2][j+2]!=0)|| 
							(chessPos[i+2][j+2]*chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0)){//当中mei有0
							rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3 连着
						}
						else if (chessPos[i][j]==0) {					//靠边子在右边
							if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//到达右下边界  或右边有子 
								rWeight=(rWeight>=80?rWeight:80 );	//冲3
							}else {								
								rWeight=(rWeight>=200 ? rWeight : 200 );	//当成眠3+
								chMt3++;
								}
							}							
						else if (chessPos[i+4][j+4]==0) {		
							if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// 左上边界 或左边有子
								rWeight=(rWeight>=80?rWeight:80 );		// 冲3
							}else {						
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}
						}
						else {
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
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
						if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//到达右下边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}
						else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[i+4][j+4]==0) {				// 右下边为空
						if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// 左上边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}
						else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=600 ? rWeight : 600 );	//中间有空  当成活3++
					}					
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			lDiagonal=(lDiagonal>=rWeight?lDiagonal:rWeight);
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
			else if (tal==max) {									//一个子
					if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0){	//存在靠边子  当成活1
						if (chessPos[i+1][j-1]*chessPos[i][j]!=0||
							chessPos[i+3][j-3]*chessPos[i+4][j-4]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲二
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
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
								}
							}							
						else if(chessPos[i+4][j-4]==0) {				// 右上边为空	
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左下边界 或左边有子
								rWeight=(rWeight>=80?rWeight:80 );		// 冲3
							}else {					
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}
						}
						else {
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
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
						if ((i==10||j==4)||chessPos[i+5][j-5]!=0) {//到达右下边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}
						else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[i+4][j-4]==0) {				// 右下边为空
						if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左上边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=600 ? rWeight : 600 );	//中间有空  当成活3++
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
				cm=5000;
			}else if(chMt4==1){
				cm=2500;
			}else {
				cm=400;
			}
		}
//		System.out.println(Max+"方的"+"纵"+bigCol+"横"+bigRow+"左"+lDiagonal+"右"+rDiagonal+"PW:"+posWeight);
	return total+cm;
	}
	static 	public int fig2(int [][] chessPos ,int Max){						//棋盘数组  max节点是
		//System.out.println("当前落子:"+Max);
		int max=Max;												//哪一方
		int total=0;
		int bigCol=0;												//计算横纵对角 每个方向最大的权重
		int bigRow=0;
		int lDiagonal=0;
		int rDiagonal=0;
		int posWeight=0;
		int chMt3=0;												//检测杀棋  活三和四
		int chMt4=0;												
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
				}else if (tal==max) {								//有一个子
					if (chessPos[i][j]!=0||chessPos[i][j+4]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//存在靠边子  当成活1
						if (chessPos[i][j+1]*chessPos[i][j]!=0||
							chessPos[i][j+3]*chessPos[i][j+4]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲二
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//存在靠边子 当成活2 冲3 眠3
						if ((chessPos[i][j+1]*chessPos[i][j+2]*chessPos[i][j]!=0)||
							(chessPos[i][j+4]*chessPos[i][j+2]*chessPos[i][j+3]!=0)){//当中mei0
							rWeight=(rWeight>=50 ? rWeight : 80 );		//冲 3
						}
						else if(chessPos[i][j]==0){					//存在断点 有可能 眠3杀棋
							if (j==10||chessPos[i][j+5]!=0) {			//到达右边界  或右有子
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;				
							}
						}
						else if (chessPos[i][j+4]==0) {
							if (j==0||chessPos[i][j-1]!=0) {			// 左边界 或左边有子
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}					
						}
						else {
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
						}
					}
					else{
						rWeight=(rWeight>=250 ? rWeight : 250 );		//活3
						chMt3++;
					}
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					chMt4++;
					if (chessPos[i][j]==0) {						//首先判断左边空
						if (j==10||chessPos[i][j+5]!=0) {			//到达右边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4
						}
					}else if(chessPos[i][j+4]==0) {					 // 右边为空
						if (j==0||chessPos[i][j-1]!=0) {			// 左边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											//中间有空  当成冲4--
						rWeight=(rWeight>=600 ? rWeight : 600 );
					}
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			bigCol=(bigCol>=rWeight ? bigCol : rWeight );
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
					if (chessPos[j][i]!=0||chessPos[j+4][i]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//存在靠边子
						if (chessPos[j+1][i]*chessPos[j][i]!=0||
							chessPos[j+3][i]*chessPos[j+4][i]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲2	
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );		  //当成活1
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//存在靠边子 当成活2 冲3
						if( (chessPos[j][i]*chessPos[j+1][i]*chessPos[j+2][i]!=0)|| 
							(chessPos[j+4][i]*chessPos[j+3][i]*chessPos[j+2][i]!=0)){//当中mei有0
							rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3//连着
							}	
						else if(chessPos[j][i]==0){	
							if (j==10||chessPos[j+5][i]!=0) {
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}
						}
						else if(chessPos[j+4][i]==0){
							if (j==0||chessPos[j-1][i]!=0) {
								rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3
							}else {
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}							
						}
						else {													
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
						}
					}
					else{
						rWeight=(rWeight>=250 ? rWeight : 250 );		//活3
						chMt3++;
					}
				}
				else if (tal==4*max) {								//4个子 不存在其他子
					chMt4++;
					if (chessPos[j][i]==0) {						//首先判断左边空
						if (j==10||chessPos[j+5][i]!=0) {			//到达右边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[j+4][i]==0) {					 // 右边为空
						if (j==0||chessPos[j-1][i]!=0) {			// 左边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=600 ? rWeight : 600 );	//中间有空  当成活3++
					}
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			bigRow=(bigRow>=rWeight ? bigRow : rWeight );
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
					if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){	//存在靠边子  当成活1
						if (chessPos[i+1][j+1]*chessPos[i][j]!=0||
							chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲二
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
				}
				else if (tal==3*max) {								//3个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){		//存在靠边子 当成活2 冲3
						if( (chessPos[i][j]*chessPos[i+1][j+1]*chessPos[i+2][j+2]!=0)|| 
							(chessPos[i+2][j+2]*chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0)){//当中mei有0
							rWeight=(rWeight>=80 ? rWeight : 80 );		//冲 3 连着
						}
						else if (chessPos[i][j]==0) {					//靠边子在右边
							if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//到达右下边界  或右边有子 
								rWeight=(rWeight>=80?rWeight:80 );	//冲3
							}else {								
								rWeight=(rWeight>=200 ? rWeight : 200 );	//当成眠3+
								chMt3++;
								}
							}							
						else if (chessPos[i+4][j+4]==0) {		
							if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// 左上边界 或左边有子
								rWeight=(rWeight>=80?rWeight:80 );		// 冲3
							}else {						
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}
						}
						else {
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
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
						if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//到达右下边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}
						else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[i+4][j+4]==0) {				// 右下边为空
						if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// 左上边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}
						else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=600 ? rWeight : 600 );	//中间有空  当成活3++
					}					
				}
				else if (tal==5*max) {								//5个子 不存在其他子
					rWeight=(rWeight>=25000 ? rWeight : 25000 );
				}	
			}
			lDiagonal=(lDiagonal>=rWeight?lDiagonal:rWeight);
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
			else if (tal==max) {									//一个子
					if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0) {	//靠边子
						rWeight=(rWeight>=2?rWeight:2);
					}else {
						rWeight=(rWeight>=10 ? rWeight : 10 );		//活1	
					}
				}
				else if (tal==2*max) {								//两个子 不存在其他子
					if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0){	//存在靠边子  当成活1
						if (chessPos[i+1][j-1]*chessPos[i][j]!=0||
							chessPos[i+3][j-3]*chessPos[i+4][j-4]!=0) {
							rWeight=(rWeight>=20 ? rWeight : 20 );	//冲二
						}else
						rWeight=(rWeight>=10 ? rWeight : 10 );
					}else {			
						rWeight=(rWeight>=50 ? rWeight : 50 );		//活2
					}
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
								}
							}							
						else if(chessPos[i+4][j-4]==0) {				// 右上边为空	
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左下边界 或左边有子
								rWeight=(rWeight>=80?rWeight:80 );		// 冲3
							}else {					
								rWeight=(rWeight>=200 ? rWeight : 200 );	//眠3
								chMt3++;
							}
						}
						else {
							rWeight=(rWeight>=50 ? rWeight : 50 );	//当成活2+
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
						if ((i==10||j==4)||chessPos[i+5][j-5]!=0) {//到达右下边界  或右边有子 
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}
						else {
							rWeight=(rWeight>=2500 ? rWeight : 2500 );	//活4g
						}
					}else if(chessPos[i+4][j-4]==0) {				// 右下边为空
						if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左上边界 或左边有子
							rWeight=(rWeight>=600?rWeight:600 );	//冲4
						}else 						
						rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
					}else {											
						rWeight=(rWeight>=600 ? rWeight : 600 );	//中间有空  当成活3++
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
				cm=5000;
			}else if(chMt4==1){
				cm=2500;
			}else {
				cm=400;
			}
		}
//		System.out.println(Max+"方的"+"纵"+bigCol+"横"+bigRow+"左"+lDiagonal+"右"+rDiagonal+"PW:"+posWeight);
	return 3*(total+cm)+chMt3*2400+chMt4*20000;
	}

}
