package wuziqi2018;

import java.util.ArrayList;
import java.util.Vector;

public class GameTree5 {
	private int MAX=0;
	private int MIN=0;
	private int depth=3;											//默认博弈深度
	private ChessBoard cB;
	private Vector<Integer> result=null;							//最优解
	public Vector<Integer> getRut(){
		return result;
	}
	public GameTree5(ChessBoard cBoard) {
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
	public GameTree5(ChessBoard cBoard ,int deep) {					//可修改默认博弈深度
		this(cBoard);
		depth=deep;
	}
	public Vector<Integer> searchTree(){
		Nodes rNodes=new Nodes(cB.getPos(), MAX, depth);
		int [] b= rNodes.findBestSNod(); 
		Vector<Integer> bVec=new Vector<>();
		bVec.add(b[0]);
		bVec.add(b[1]);
		System.out.println("总结点数："+Nnum);
		return bVec;
	}
	static int Nnum=0;
	class Nodes{
		
		public int nodeId=0;
		private int[][] myPos=new int[15][15];								//当前节点的 棋盘
		private int[]nodeAxis=new int[2];									//当前节点   走的那一步
		private int deep;
		private ArrayList<Nodes> sNodelist=new ArrayList<Nodes>();
		private int Max;
		private int Min;
		private int weight=0;
		private boolean gotWeight = false;
		private int cutFlag=0;
		
		public Nodes( int[][] pos,int Max,int deep) {					//max是要下的棋
			Nnum++;
			nodeId=Nnum;
//			System.out.println("");
//			System.out.print("当前Nod数"+Nnum+"深度:"+deep+"Max:"+Max);
			myPos=posCopy(pos);							//!!
			this.Max=Max;
			this.deep=deep;
			Min=(Max==1? 2:1);
			if (deep>0) {
				genNodeList();
			}
			if (deep==0) {												//自动生成叶子结点的权重
				setWeight(getStWeight(myPos, Max));
			}
	//		System.out.println("weight:"+weight);
			if (Max==MAX) {
	//			System.out.println("MAX节,"+Max+"下"+"");			
			}
		}
		public Nodes(int[][] pos,int Max,int deep,int weight ,int cutF) {
			// TODO Auto-generated constructor stub
			Nnum++;
			nodeId=Nnum;
	//		System.out.println("当前Nod数"+Nnum+"深度:"+deep+"Max:"+Max);
			myPos=posCopy(pos);							//!!
			setWeight(weight);
			this.Max=Max;
			cutFlag=cutF;
			this.deep=deep;
			Min=(Max==1? 2:1);
			if (deep>0) {
				genNodeList();
			}
			if (deep==0) {												//自动生成叶子结点的权重
				setWeight(getStWeight(myPos, Max));
			}//System.out.println("weight:"+weight);
		}
		public Nodes(int weight) {
			setWeight(weight);
			
		}
		//判断是否子节点出现五子 是
		private void genNodeList() {									//生成子节点   自己是否已有权重赋值 若没有则生成子节点 获取子节点的
			ArrayList<Nodes>  ndList=new ArrayList<Nodes>();
			int[][] gPos=posCopy(myPos);	
			if (!gotWeight) {											//还没有权重	先构造无权重节点 为了得到初始权重	
				if(deep==1){											//此时最小deep为1 0则不生成子节点
					int fWeight=0;										//第一次需要遍历全部节点
					for(int i=0;i<15;i++){                           	//第一次需要遍历全部节点		
						for(int j=0;j<15;j++){
							if (gPos[i][j]==0&&testPos2(gPos, i, j)) {
								gPos[i][j]=Max;							//
								
								Nodes fNodes= new Nodes(gPos, Min, deep-1);
								fNodes.setNodeAxis(i, j);
								int fnWght=fNodes.getWeight();
								if (fWeight==0) {
									fWeight=fnWght;
								}else {				
									if (MAX==Max) {
										fWeight=(fnWght>fWeight?fnWght:fWeight);
									}else {
										fWeight=(fnWght<fWeight?fnWght:fWeight);
									}
								}
								ndList.add(fNodes);
								gPos[i][j]=0;
							}
						}
					}
					setWeight(fWeight);	
					sNodelist=ndList;
					return ;
				}
				else {
					int i3=0;													//横向遍历  bug 得到第一个节点权值 
					for(;i3<15;i3++){
						for(int j3=0;j3<15;j3++){
							if (gPos[i3][j3]==0&&testPos2(gPos, i3, j3)) {
								gPos[i3][j3]=Max;
								if(deep==depth){
									if(fiveEst(gPos, i3, j3)){
										Nodes fnod=new Nodes(150000);
										fnod.setNodeAxis(i3,j3);
										ndList.add(fnod);	
										sNodelist=ndList;
										return;
									}
								}
								Nodes fNodes;
								if (gotWeight==false) {
									fNodes= new Nodes(gPos, Min,deep-1);
									setWeight(fNodes.getWeight());					//将第一个子节点权值返回 得到权值 之后进行有权值搜索						
								}else {												//已获得权值 生成后续						
									if (Max==MAX) 
										fNodes= new Nodes(gPos, Min, deep-1,weight,1);	//A剪枝								
									else
										fNodes= new Nodes(gPos, Min, deep-1,weight,2);	//B剪枝		
									
									int sWeight=fNodes.getWeight();
									//											横向子节点与权重的比较					
									if (Max==MAX) {
										if (weight<sWeight) {
											weight=sWeight;
										}	
									}else {
										if (weight>sWeight) {
											weight=sWeight;
										}
									}
								}
								fNodes.setNodeAxis(i3,j3);						//System.out.print("("+i+","+j+",");
								ndList.add(fNodes);								//bug
								gPos[i3][j3]=0;	
							}
						}
					}
					sNodelist=ndList;			
					return;
				}
			}else{
			if(gotWeight) {											//已有权值约束
				int i2=0;//is;
				int cut=0;
				int retWeight=1;
				for(;i2<15;i2++){
					int j=0;//js;
					for(;j<15;j++){
						if (gPos[i2][j]==0&&testPos2(gPos, i2, j)) {
							gPos[i2][j]=Max;
							Nodes sNodes=new Nodes(gPos, Min, deep-1, weight,cutFlag);
							sNodes.setNodeAxis(i2, j);		
							int sWeight=sNodes.getWeight();				//计算子节点权值 参与剪枝
							if (Max==MAX) {
								if (sWeight>=weight) {						//该节点为max节点  子节点     
									weight=sWeight;	
									if(cutFlag==2){									//B剪枝其 先辈节点MIn  B剪枝
										cut=1;
									}
								}
								if (retWeight==1) {							//max节点存最大的子节点权值
									retWeight=sWeight;
								}else{
									int sw=sWeight;
									retWeight=(retWeight>sw?retWeight:sw);
								}
							}else {											// 其 先辈Max节点 A剪枝  当前Min节点
								if (sWeight<=weight) {			
									weight=sWeight;	
									if (cutFlag==1) {							//子节点 min 
												//该节点等于子节点权值 
										cut=1;
									}
								}	
								if (retWeight==1) {
									retWeight=sWeight;
								}else{
									int sw=sWeight;
									retWeight=(retWeight<sw?retWeight:sw);
								}
							} 
							ndList.add(sNodes);
							gPos[i2][j]=0;
							if (cut!=0) {
								break;
							}
						}
					}
					if (cut!=0) {
						break;
					}
				}
				if (cut==0) {					
					weight=retWeight; 
				}
				sNodelist=ndList;							
				}
			}
		}	
		public int[] findBestSNod(){							//找到最佳下一步
		
			int bestWegt=sNodelist.get(0).getWeight();
			int[] bestAxis=sNodelist.get(0).getNodeAxis();
			if (MAX==Max) {												//当前max节点 
				for (Nodes nod : sNodelist) {							//找到最大的子节点 得到其 nodvec
				
					if (nod.getWeight()>bestWegt) {
						bestWegt=nod.getWeight();
						bestAxis=nod.getNodeAxis();
					}
				}
			}else{
				for (Nodes nod : sNodelist) {		
					if (nod.getWeight()<bestWegt) {
						bestWegt=nod.getWeight();
						bestAxis=nod.getNodeAxis();
					}
				}
			}
			return bestAxis;	
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

		public int[] getNodeAxis() {
			int[] a= new int[2];
			a[0]=nodeAxis[0];
			a[1]=nodeAxis[1];
			return a;
		}
		public void setNodeAxis(int[] nodeAxis) {
			this.nodeAxis[0] = nodeAxis[0];
			this.nodeAxis[1] = nodeAxis[1];
		}
		public void setNodeAxis(int x,int y) {
			this.nodeAxis[0] =x;
			this.nodeAxis[1] =y;
		}
		public void setWeight(int weight) {
			this.weight = weight;
			gotWeight=true;
		}
		public int getWeight() {
			return weight;
		}
		public int[][] getMyPos() {
			int[][] pos=posCopy(myPos);
			return pos;
		}
		public int getMax(){
			return Max;
		}
	}
	
	
	public int getStWeight(int[][] chPos,int max){
		int  talWt = 0;
		if(max==MAX){											//判断是MAX节点还是MIN节点
			
			talWt=fig2(chPos, MAX)-fig(chPos, MIN);				//max节点   下一步是MAX
		}else{														//min节点  下一步是MIN
			talWt=fig(chPos, MAX)-fig2(chPos, MIN);
		}
		return talWt;
	}
	
	private int getStWeight(Nodes node) {
		return getStWeight(node.getMyPos(), node.getMax());
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
	public boolean fiveEst(int[][] pos,int x,int y){
		int miX,maX,miY,maY;
		int max5=(MAX==1?1:32);
		miX=(x<=4?0:x-4);
		maX=(x>=10?14:x+4);
		miY=(y<=4?0:y-4);
		maY=(y>=10?14:y+4);
		int colL=maY-miY;
		int rowL=maX-miX;	
		int leftL=(x>y?y:x);
		int leftR=(14-x>14-y?14-y:14-x);			//对角两端间距
		int rightL=(x>14-y?14-y:x);
		int rightR=(14-x>y?y:14-x);
		for(int i=0;i<=colL-4;i++){					//间距最小为4
			int tal=1;
			for(int j=0;j<5;j++){
				int n=pos[x][miY+i+j];
				tal*=n;
			}
			if (tal==(max5)) {
				return true;
			}
		}
		for(int i=0;i<=rowL-4;i++){
			int tal=1;
			for(int j=0;j<5;j++){
				tal*=pos[miX+i+j][y];
			}
			if (tal==(max5)) {
				return true;
			}
		}
		if (leftL+leftR>=4) {						//间隔大于等于4 可能五子
			int LL=leftL+leftR;
			int lMiX=x-leftL;
			int lMiY=y-leftL;
			for(int i=0;i<=LL-4;i++){				//从左上遍历
				int tal=1;
				for(int j=0;j<5;j++){
					tal*=pos[lMiX+j+i][lMiY+j+i];
				}
				if (tal==(max5)) {
					return true;
				}
			}
		}
		if (rightL+rightR>=4) {						//间隔大于等于4 可能五子
			int RL=rightL+rightR;
			int RMiX=x-rightL;
			int RMiY=y+rightL;
			for(int i=0;i<=RL-4;i++){				//从左下遍历
				int tal=1;
				for(int j=0;j<5;j++){
					tal*=pos[RMiX+j+i][RMiY-j-i];
				}
				if (tal==(max5)) {
					return true;
				}
			}
		}return false;
	}
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
			int CM4=0;
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
								CM4++;
							}
						}else if(chessPos[i][j+4]==0) {					 // 右边为空
							if (j==0||chessPos[i][j-1]!=0) {			// 左边界 或左边有子
								rWeight=(rWeight>=600?rWeight:600 );	//冲4
							}else {						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
								CM4++;
							}
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
								CM4++;
							}
						}else if(chessPos[j+4][i]==0) {					 // 右边为空
							if (j==0||chessPos[j-1][i]!=0) {			// 左边界 或左边有子
								rWeight=(rWeight>=600?rWeight:600 );	//冲4
							}else {						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
								CM4++;
							}
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
								CM4++;
							}
						}else if(chessPos[i+4][j+4]==0) {				// 右下边为空
							if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// 左上边界 或左边有子
								rWeight=(rWeight>=600?rWeight:600 );	//冲4
							}
							else{ 						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
								CM4++;
							}
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
								CM4++;
							}
						}else if(chessPos[i+4][j-4]==0) {				// 右下边为空
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// 左上边界 或左边有子
								rWeight=(rWeight>=600?rWeight:600 );	//冲4
							}else{ 						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//活4
								CM4++;
							}
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

		return total+cm+(CM4>0?10000:0);
		}
	public int fig2(int [][] chessPos ,int Max){						//棋盘数组  max节点是

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
		return 3*(total+cm)+(chMt3>0?2400:0)+(chMt4>0?15000:0);
		}

}
