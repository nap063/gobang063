package wuziqi2018;

import java.util.ArrayList;
import java.util.Vector;

public class GameTree2 { 						//返回当前最优下法
	private int MAX=0;
	private int MIN=0;
	private int depth=3;											//默认博弈深度
	private ChessBoard cB;
	private Vector<Integer> result=null;							//最优解
	public Vector<Integer> getRut(){
		return result;
	}
	public GameTree2(ChessBoard cBoard) {
		System.out.println("当前棋盘："+cBoard.getpID());
		if(cBoard.whoNext()==0){										//当前落子为黑棋
			MAX=1;														//黑下
			MIN=2;
		}else {															//白棋
			MAX=2;														//白下
			MIN=1;
		}
		System.out.println("博弈树Max是"+MAX+"下一手");
		cB=cBoard;
	}
	public GameTree2(ChessBoard cBoard ,int deep) {					//可修改默认博弈深度
		this(cBoard);
		depth=deep;
	}
	static int Nnum=0;
	class Nodes{
		public int nodeId=0;
		private int[][] myPos=new int[15][15];								//当前节点的 棋盘
		private Vector<Integer> nodVec=new Vector<>();						//当前节点   走的那一步
		private int deep;
		private ArrayList<Nodes> sNodelist=new ArrayList<Nodes>();
		private int Max;
		private int Min;
		private int weight=0;
		public Nodes( int[][] pos,int Max,int deep) {					//max是要下的棋
			Nnum++;
			nodeId=Nnum;
			myPos=posCopy(pos);							//!!
			this.Max=Max;
			this.deep=deep;
			Min=(Max==1? 2:1);
			if (deep>0) {
				sNodelist=genNodeList2(myPos, Min, deep-1);
			}
			if (deep==0) {												//自动生成叶子结点的权重
				setWeight(getStWeight(pos, Max));
			}
		}
		public Vector<Integer> findBestSNod(){							//找到最佳下一步
			System.out.println("找最好的节点");
			int bestWegt=sNodelist.get(0).getWeight();
			Vector<Integer> bestVec=sNodelist.get(0).getNodVec();
			if (MAX==Max) {												//当前max节点 
				for (Nodes nod : sNodelist) {							//找到最大的子节点 得到其 nodvec
					System.out.print("maxS:"+getMax()+"方  ，"+nod.getWeight()+"坐标"+nod.nodVec+" ,算："+getStWeight(nod));
					if (nod.getWeight()>bestWegt) {
						bestWegt=nod.getWeight();
						bestVec=nod.getNodVec();
					}
				}
			}else{
				for (Nodes nod : sNodelist) {		
					System.out.print("minS"+getMax()+"方  ，"+nod.getWeight()+"坐标"+nod.nodVec);
					if (nod.getWeight()<bestWegt) {
						bestWegt=nod.getWeight();
						bestVec=nod.getNodVec();
					}
				}
			}
			return bestVec;	
		}
		public ArrayList<Nodes> genNodeList2(int[][] pos ,int Max ,int deep){		//先生成一个子节点 ，在比较a b生成其他
			ArrayList<Nodes> ndList=new ArrayList<>();
			if (deep>=0) {
				int i = 0;
				int max1=Max;
				for (; i < 15; i++) {												//这里只生成一个子节点 方便后续的比较
					int a=0;
					for (int j = 0; j < 15; j++) { 									// 可改进的 减少遍历 非全部
						if (pos[i][j] == 0&&testPos2(pos, i, j)) {
							pos[i][j] = (max1==MAX?MIN:MAX);							//传入1 则说明要下的是2白棋
							Nodes nod = new Nodes(pos, max1, deep);
							this.setWeight((nod).getWeight());						//  在这加入获取该子节点的 ab值 函数
								//作为初始的ab值
							nod.setNodVec(i, j);
							ndList.add(nod);
							pos[i][j] = 0;
							a=1;
							break;													//只生成一个  获取ab值 进行比较后面的
						}
					}
					if (a!=0) 
						break;
				} 
				int i2 = 0;
				for (; i2 < 15; i2++) {				
					for (int j2 = 1; j2 < 15; j2++) { 								// 可改进的 减少遍历 非全部
						if (pos[i2][j2] == 0&&testPos2(pos, i2, j2)) {
							pos[i2][j2] = (Max==MAX?MIN:MAX);						//传入1 则说明要下的是2白棋
							Nodes nod = new Nodes(pos, Max, deep);
							//若是该节点  的ab值 不符合要求  返回ab值   continue  下一轮						
							if(Max==MAX){											//当前子节点若是MAX节点			
								if((nod).getWeight()>this.weight){					//a权值大于min父节点b值	
									
									pos[i2][j2] = 0;
									continue;	
								}													//不添加这个节点
								else if(nod.getWeight()<this.weight){  				//若子节点更优 ,更新父节点的weight
									this.setWeight(nod.getWeight());				//没必要去掉之前的节点 有权重决定
								}
							}else{													//若当前子节点为min节点
								if(nod.getWeight()<this.weight){
									pos[i2][j2] = 0;
									continue;	
								}													//b值小于当前父节点的a值 不添加这个节点
								else if(nod.getWeight()>this.weight){
									this.setWeight(nod.getWeight());
								}
							}
							nod.setNodVec(i2, j2);
							ndList.add(nod);
							pos[i2][j2] = 0;
						}
					}
				} 
			}
//			if(deep==0){
//				int i2 = 0;
//				for (; i2 < 15; i2++) {	
//					for (int j2 = 0; j2 < 15; j2++) { // 可改进的 减少遍历 非全部
//						if (pos[i2][j2] == 0&&testPos2(pos, i2, j2)) {
//							pos[i2][j2] = (Max==MAX?MIN:MAX);							//传入1 则说明要下的是2白棋
//							Nodes nod = new Nodes(pos, Max, deep);
//							nod.setWeight(getStWeight(nod));
//							nod.setNodVec(i2, j2);
//							ndList.add(nod);
//							pos[i2][j2] = 0;
//						}
//					}
//				} 
//				
//			}
			return ndList;
		}
		
		public int[][] getMyPos() {
			return myPos;
		}
		public int getMax() {
			return Max;
		}
		public Vector<Integer> getNodVec() {
			return nodVec;
		}
		public void setNodVec(int x,int y) {
			Vector<Integer> nodVec = new Vector<>();
			nodVec.addElement(x);
			nodVec.addElement(y);
			this.nodVec = nodVec;
		}
		public void setNodVec(Vector<Integer> nodVec) {
			this.nodVec = nodVec;
		}
		
		public int getDeep() {
			return deep;
		}
		public ArrayList<Nodes> getsNodelist() {
			return sNodelist;
		}
		public int getWeight() {
			return weight;
		}
		public void setWeight(int weight) {
			this.weight = weight;
		}
	}
	public Vector<Integer> searchTree(){
		Vector<Integer> vector=new Vector<>();
		Nodes rNodes=new Nodes(cB.getPos(), MAX, depth);		//搜索节点 从根节点开始				
		vector=rNodes.findBestSNod();							// 找到权值最好的下法
		System.out.println("得到的是"+vector);
		return vector;
	}
	public int[][] posCopy(int[][] pos){
		int pos2[][]=new int[15][15];
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				pos2[i][j]=pos[i][j];
			}	
		}
		return pos2;
	}
	
	public static boolean testPos2(int[][] pos,int x,int y){
		int iMin=(x-2<=0?0:(x-2));
		int iMax=(x+2>=14?14:(x+2));
		int jMin=(y-2<=0?0:(y-2));
		int jMax=(y+2>=14?14:(y+2));
		int i=iMin;
		for(;i<=iMax;i++){
			for(int j=jMin;j<=jMax;j++){
				if (pos[i][j]!=0) {									//当棋盘（i,j）不为空时
					if((x-i<3||i-x<3)&&(y-j<3||j-y<3))				// 间隔为二的地方可以落子
						{	
						return true;
						}
				}
			}
		}
		return false;
	}
	public int getStWeight(int[][] chPos,int max){
		int  talWt = 0;
		int min=(max==1? 2:1);
		if(max==MAX){											//判断是MAX节点还是MIN节点
			talWt=4*fig(chPos, MAX)-fig(chPos, MIN);				//max节点   下一步是MAX
		}else{														//min节点  下一步是MIN
			talWt=fig(chPos, MAX)-4*fig(chPos, MIN);	
		}
		return talWt;
	}
	private int getStWeight(Nodes node) {
		return getStWeight(node.getMyPos(), node.getMax());
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
			rDiagonal=(rDiagonal>=rWeight?rDiagonal:rWeight);
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

}