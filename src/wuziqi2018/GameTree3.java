package wuziqi2018;


import java.util.ArrayList;
import java.util.Vector;



public class GameTree3 {
	private int MAX=0;
	private int MIN=0;
	private int depth=3;											//默认博弈深度
	private ChessBoard cB;
	private Vector<Integer> result=null;							//最优解
	public Vector<Integer> getRut(){
		return result;
	}
	public GameTree3(ChessBoard cBoard) {
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
	public GameTree3(ChessBoard cBoard ,int deep) {					//可修改默认博弈深度
		this(cBoard);
		depth=deep;
	}
	public Vector<Integer> searchTree(){
		Nodes rNodes=new Nodes(cB.getPos(), MAX, depth);
		int [] b= rNodes.findBestSNod(); 
		Vector<Integer> bVec=new Vector<>();
		bVec.add(b[0]);
		bVec.add(b[1]);
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
		public Nodes(int[][] pos,int Max,int deep,int weight) {
			// TODO Auto-generated constructor stub
			Nnum++;
			nodeId=Nnum;
	//		System.out.println("当前Nod数"+Nnum+"深度:"+deep+"Max:"+Max);
			myPos=posCopy(pos);							//!!
			setWeight(weight);
			this.Max=Max;
			this.deep=deep;
			Min=(Max==1? 2:1);
			if (deep>0) {
				genNodeList();
			}
			if (deep==0) {												//自动生成叶子结点的权重
				setWeight(getStWeight(myPos, Max));
			}//System.out.println("weight:"+weight);
		}
		
		private void genNodeList() {									//生成子节点   自己是否已有权重赋值 若没有则生成子节点 获取子节点的
			ArrayList<Nodes>  ndList=new ArrayList<Nodes>();
			int[][] gPos=posCopy(myPos);	
			int is=0;
			int js=0;
			if (!gotWeight) {											//还没有权重	先构造无权重节点 为了得到初始权重	
				if(deep==1){											//此时最小deep为1 0则不生成子节点
					int fWeight=0;										//第一次需要遍历全部节点
					int ix = 0,jx = 0;
					int cutb=0;
					for(int i=0;i<15;i++){
						for(int j=0;j<15;j++){
							if (gPos[i][j]==0&&testPos1(gPos, i, j)) {	//第一个被选中的叶子结点
								gPos[i][j]=Max;
								ix=i;jx=j;
								is=i;js=j;
								Nodes fNodes= new Nodes(gPos, Min, 0);
								fNodes.setNodeAxis(i,j);//System.out.print("("+i+","+j+",");
								fWeight=fNodes.getWeight();				//将第一个子节点权值返回 得到权值						
								ndList.add(fNodes);
								gPos[i][j]=0;
								cutb=1;
								break;
							}
						}
						if (cutb!=0) {
							break;
						}
					}
					for(int i=ix;i<15;i++){                           	//第一次需要遍历全部节点		
						int j=jx;
						for(j=((i==ix&&j==jx)?jx+1:jx);j<15;j++){
							if (gPos[i][j]==0&&testPos1(gPos, i, j)) {
								gPos[i][j]=Max;
								Nodes fNodes= new Nodes(gPos, Min, deep-1);
								fNodes.setNodeAxis(i, j);//System.out.print("("+i+","+j+",");
								int fnWght=fNodes.getWeight();
								if (MAX==Max) {
									fWeight=(fnWght>fWeight?fnWght:fWeight);
								}else {
									fWeight=(fnWght<fWeight?fnWght:fWeight);
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
				else {															//deep>1节点  未赋值的节点
					int i=0;
					for(;i<15;i++){
						int a=0;
						for(int j=0;j<15;j++){
							if (gPos[i][j]==0&&testPos1(gPos, i, j)) {
								gPos[i][j]=Max;
								System.out.println("当前节点("+i+","+j+")"+"深度:"+(deep-1));
								is=i;
								js=j;
								Nodes fNodes= new Nodes(gPos, Min, deep-1);
								fNodes.setNodeAxis(i,j);//System.out.print("("+i+","+j+",");
								setWeight(fNodes.getWeight());					//将第一个子节点权值返回 得到权值 之后进行有权值搜索						
								ndList.add(fNodes);								//bug
								gPos[i][j]=0;
								a=1;
								break;
							}
						}
						if (a!=0) {
							break;
						}
					}
					int i3=is;													//横向遍历  bug 得到第一个节点权值 
					for(;i3<15;i3++){
						for(int j3=js;j3<15;j3++){
							if (gPos[i3][j3]==0&&testPos1(gPos, i3, j3)) {
								gPos[i3][j3]=Max;
								System.out.println("当前节点("+i+","+j3+")"+"深度:"+(deep-1));
								Nodes fNodes= new Nodes(gPos, Min, deep-1,weight);
								fNodes.setNodeAxis(i3,j3);						//System.out.print("("+i+","+j+",");
								int sWeight=fNodes.getWeight();
								//setWeight(fNodes.getWeight());				横向子节点与权重的比较					
								if (Max==MAX) {
									if (weight<sWeight) {
										weight=sWeight;
									}	
								}else {
									if (weight>sWeight) {
										weight=sWeight;
									}
								}
								ndList.add(fNodes);								//bug
								gPos[i3][j3]=0;	
							}
						}
					}
					sNodelist=ndList;
					System.out.println("返回权值:"+weight+"返回depth:"+deep);
					return;
				}
			}else{
			if(gotWeight) {											//已有权值约束
				int retWeight=1;									//不剪枝最好的权值
				int i2=is;
				int cut=0;
				for(;i2<15;i2++){
					int j=js;
					for(j=((i2==is&&j==js)?js+1:js);j<15;j++){
						if (gPos[i2][j]==0&&testPos1(gPos, i2, j)) {
							gPos[i2][j]=Max;
							Nodes sNodes=new Nodes(gPos, Min, deep-1, weight);
							sNodes.setNodeAxis(i2, j);		
							int sWeight=sNodes.getWeight();
							if (Max==MAX) {									//其 父节点MIn B剪枝
								if (sWeight>=weight) {			//该节点为max节点  子节点     
									System.out.println("B剪： 子w>父"+"子deep:"+sNodes.deep+",w:"+sNodes.weight+"父deep:"+deep+"/W:"+weight);
									System.out.println("子坐标("+i2+","+j+")");
									weight=sWeight;				//阻止自己被选中		
									cut=1;
								}
								else {										//if (sNodes.getWeight()<weight){
									if (retWeight==1) {
										retWeight=sWeight;
									}else{
										int sw=sWeight;
										retWeight=(retWeight>sw?retWeight:sw);
									}
								}
							}else {											// 其 父Max节点 A剪枝 
								if (sWeight<=weight) {			//子节点 min 
									System.out.println("A剪: 子<父"+"子deep:"+sNodes.deep+",w:"+sNodes.weight+"父deep:"+deep+"/W:"+weight);
									System.out.println("子坐标("+i2+","+j+")");
									weight=sWeight;				//该节点等于子节点权值 
									cut=1;
								}
								else {										//if (sNodes.getWeight()<weight){
									if (retWeight==1) {
										retWeight=sWeight;
									}else{
										int sw=sWeight;
										retWeight=(retWeight<sw?retWeight:sw);
									}
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
					weight=retWeight;System.out.println();
				}
				System.out.println("返回权值:"+weight+"返回depth:"+deep);
				sNodelist=ndList;							// 重载nodes 有初始ab值
				}
			}
		}	
		public int[] findBestSNod(){							//找到最佳下一步
			//System.out.println("找最好的节点");
			int bestWegt=sNodelist.get(0).getWeight();
			int[] bestAxis=sNodelist.get(0).getNodeAxis();
			if (MAX==Max) {												//当前max节点 
				for (Nodes nod : sNodelist) {							//找到最大的子节点 得到其 nodvec
					//System.out.println("maxS:"+getMax()+"方  ，"+nod.getWeight()+"坐标"+nod.nodeAxis[0]+nod.nodeAxis[1]+" ,算："+getStWeight(nod));
					if (nod.getWeight()>bestWegt) {
						bestWegt=nod.getWeight();
						bestAxis=nod.getNodeAxis();
					}
				}
			}else{
				for (Nodes nod : sNodelist) {		
					//System.out.println("minS"+getMax()+"方  ，"+nod.getWeight()+"坐标"+nod.nodeAxis[0]+nod.nodeAxis[1]);
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
			//System.out.print("Max:"+max);
			talWt=fig2(chPos, MAX)-fig(chPos, MIN);				//max节点   下一步是MAX
			//System.out.println("");
		}else{														//min节点  下一步是MIN
			//System.out.print("Min:");
			talWt=fig(chPos, MAX)-fig2(chPos, MIN);
			//System.out.println("");
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
//			System.out.println(Max+"方的"+"纵"+bigCol+"横"+bigRow+"左"+lDiagonal+"右"+rDiagonal+"PW:"+posWeight);
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
//			System.out.println(Max+"方的"+"纵"+bigCol+"横"+bigRow+"左"+lDiagonal+"右"+rDiagonal+"PW:"+posWeight);
		return 3*(total+cm)+chMt3*2400+chMt4*20000;
		}

}
