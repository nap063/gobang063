package wuziqi2018;
import java.util.ArrayList;
import java.util.Vector;

public class GameTree { 						//返回当前最优下法
	private int MAX=0;
	private int MIN=0;
	private int depth=4;											//默认博弈深度
	private ChessBoard cB;
	private Vector<Integer> result=null;							//最优解
	public Vector<Integer> getRut(){
		return result;
	}
	public GameTree(ChessBoard cBoard) {
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
	public GameTree(ChessBoard cBoard ,int deep) {					//可修改默认博弈深度
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
		public Nodes( int[][] pos,int Max,int deep) {						//max是要下的棋
			Nnum++;
			nodeId=Nnum;
																			//		System.out.println("当前Nod数"+Nnum);
			myPos=posCopy(pos);												//!!
			this.Max=Max;
			this.deep=deep;
			Min=(Max==1? 2:1);
																			//		System.out.println("节点下一步是"+Max);
			if (deep>0) {
				sNodelist=genNodelist(myPos, Min, deep-1);
			}
		}
		public Vector<Integer> findBestSNod(){					//找到最佳下一步
			System.out.println("找最好的节点");
			int bestWegt=sNodelist.get(0).getWeight();
			Vector<Integer> bestVec=sNodelist.get(0).getNodVec();
			if (MAX==Max) {											//当前max节点 
				for (Nodes nod : sNodelist) {						//找到最大的子节点 得到其 nodvec
					System.out.print("maxS:"+getMax()+"方  ，"+nod.getWeight()+"坐标"+nod.nodVec+" ,算："+getStWeight(nod));
					if (nod.getWeight()>bestWegt) {
						bestWegt=nod.getWeight();
						bestVec=nod.getNodVec();
		//				System.out.println("MAX节点下法"+nod.getNodVec());
					}
				}
			}else{
				for (Nodes nod : sNodelist) {		
					System.out.print("minS"+getMax()+"方  ，"+nod.getWeight()+"坐标"+nod.nodVec);
					if (nod.getWeight()<bestWegt) {
						bestWegt=nod.getWeight();
						bestVec=nod.getNodVec();
		//				System.out.println("MIN节点下法"+nod.getNodVec());
					}
				}
			}
		//	System.out.println("是"+bestVec.elementAt(0)+","+bestVec.elementAt(1));
			return bestVec;	
		}
		public void pickNodeWeight(){							//权重返回方法  更新返回值
			  if (deep>=1) {
				for (Nodes nod : sNodelist) {
					nod.pickNodeWeight();
					}
				if (Max==MAX) {									//极大节点 找最大的子权重
					int maxWeight=sNodelist.get(0).weight;		//先返回第一个
					
					for (Nodes nod : sNodelist) {
						if(nod.weight>maxWeight){
							maxWeight=nod.weight;	
						}
					} 
					weight=maxWeight;
					
					if (deep==3) {
						for(int i=0;4-i>deep;i++){
							System.out.print("    ");
						}
						System.out.println("pos:("+nodVec.elementAt(0)+","+nodVec.elementAt(1)+")"+"更新quan值"+weight);						
					}
					else if(deep==4){
						System.out.println("更新返回值"+weight+"节点"+nodeId);
					}
					
				}
				else if (Max!=MAX) {									//极小节点 找最小的子权重
					int minWeight = sNodelist.get(0).weight;	
					for (Nodes nod : sNodelist) {
						if(nod.weight<minWeight){
							minWeight=nod.weight;	
						}
					}
					weight=minWeight;
					
					if (deep==3) {
						for(int i=0;4-i>deep;i++){
							System.out.print("    ");
						}
						System.out.println("pos:("+nodVec.elementAt(0)+","+nodVec.elementAt(1)+")"+"更新quan值"+weight);					
					}
					else if(deep==4){
						System.out.println("更新返回值"+weight+"节点"+nodeId);
					}
					
				}
			  }
			  else if(deep==0){							 		//算 叶子结点权值	
				  setWeight(getStWeight(this));					//保存叶子结点权值
			  }
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
		rNodes.pickNodeWeight();								//从叶子结点 返回权值
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
	public ArrayList<Nodes> genNodelist (int[][] pos ,int Max ,int deep){  		 // 生成min节点   还是max节点  深度多少的节点
																					//		System.out.println("在生成"+deep+"dep的节点");
		ArrayList<Nodes> ndList=new ArrayList<>();
		if (deep>=0) {
			int i = 0;
			for (; i < 15; i++) {	
				for (int j = 0; j < 15; j++) { 										// 可改进的 减少遍历 非全部
					if (pos[i][j] == 0&&testPos1(pos, i, j)) {
						pos[i][j] = (Max==MAX?MIN:MAX);								//传入1 则说明要下的是2白棋
						Nodes nod = new Nodes(pos, Max, deep);
						nod.setNodVec(i, j);
						ndList.add(nod);
						pos[i][j] = 0;
					}
				}
			} 
		}
		return ndList;
	}
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
	public boolean testPos(int [][] pos,int x,int y){							//
		if ((x<=13&&x>=1)&&(y<=13&&y>=1)) {
			if (pos[x-1][y-1]!=0||pos[x-1][y]!=0||pos[x-1][y+1]!=0||pos[x][y-1]!=0||
					pos[x][y+1]!=0||pos[x+1][y-1]!=0||pos[x+1][y]!=0||pos[x+1][y+1]!=0) {
				return true;
			}else {
				return false;
			}
		}else {
			if (x==0) {
				if (y>=2&&y<=13) {
					if (pos[x][y-1]!=0||pos[x][y+1]!=0||pos[x+1][y-1]!=0||pos[x+1][y]!=0||pos[x+1][y+1]!=0) {
						return true;
					}else return false;	
				}else return false;
			}else if(x==14){
				if (y>=2&&y<=13) {
					if (pos[x][y-1]!=0||pos[x][y+1]!=0||pos[x-1][y-1]!=0||pos[x-1][y]!=0||pos[x-1][y+1]!=0) {
						return true;
					}else return false;
				}else return false;
			}else if (y==0) {
				if (x>=2&&x<=13) {
					if (pos[x-1][y]!=0||pos[x+1][y]!=0||pos[x-1][y+1]!=0||pos[x][y+1]!=0||pos[x-1][y+1]!=0) {
						return true;
					}else return false;
				}else return false;
				
			}else if(y==14){
				if (x>=2&&x<=13) {
					if (pos[x-1][y]!=0||pos[x+1][y]!=0||pos[x-1][y-1]!=0||pos[x][y-1]!=0||pos[x-1][y-1]!=0) {
						return true;
					}else return false;
				}else return false;
			}			
		}
		return false;
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
	//		System.out.print("这里是2*MAX-min");
			talWt=fig2(chPos, MAX)-fig(chPos, MIN);				//max节点   下一步是MAX
	//		System.out.println("///"+MAX+"方权值"+fig(chPos, MAX)+MIN+"方权值"+fig(chPos, MIN));
		}else{														//min节点  下一步是MIN
	//		System.out.print("这里是2*MIN-max");
			talWt=fig(chPos, MAX)-fig2(chPos, MIN);
	//		System.out.println("///"+MAX+"方权值"+fig(chPos, MAX)+MIN+"方权值"+fig(chPos, MIN));
		}
		return talWt;
	}
	private int getStWeight(Nodes node) {
		return getStWeight(node.getMyPos(), node.getMax());
	}
//	public void fitTest(){
//		System.out.println(getStWeight(cB.getPos(), MAX));
//	}
	public int fig(int [][] chessPos ,int Max){						//棋盘数组  max节点是
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
	public int fig2(int [][] chessPos ,int Max){						//棋盘数组  max节点是
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
