package wuziqi2018;

import java.util.ArrayList;
import java.util.Vector;

public class GameTree5 {
	private int MAX=0;
	private int MIN=0;
	private int depth=3;											//Ĭ�ϲ������
	private ChessBoard cB;
	private Vector<Integer> result=null;							//���Ž�
	public Vector<Integer> getRut(){
		return result;
	}
	public GameTree5(ChessBoard cBoard) {
		System.out.println("��ǰ���̣�"+cBoard.getpID());
		if(cBoard.whoNext()==0){										//��ǰ����Ϊ����
			MAX=1;														//����
			MIN=2;
		}else {															//����
			MAX=2;														//����
			MIN=1;
		}
		System.out.println("������Max��"+MAX+"��һ��");
		cB=cBoard;
	}
	public GameTree5(ChessBoard cBoard ,int deep) {					//���޸�Ĭ�ϲ������
		this(cBoard);
		depth=deep;
	}
	public Vector<Integer> searchTree(){
		Nodes rNodes=new Nodes(cB.getPos(), MAX, depth);
		int [] b= rNodes.findBestSNod(); 
		Vector<Integer> bVec=new Vector<>();
		bVec.add(b[0]);
		bVec.add(b[1]);
		System.out.println("�ܽ������"+Nnum);
		return bVec;
	}
	static int Nnum=0;
	class Nodes{
		
		public int nodeId=0;
		private int[][] myPos=new int[15][15];								//��ǰ�ڵ�� ����
		private int[]nodeAxis=new int[2];									//��ǰ�ڵ�   �ߵ���һ��
		private int deep;
		private ArrayList<Nodes> sNodelist=new ArrayList<Nodes>();
		private int Max;
		private int Min;
		private int weight=0;
		private boolean gotWeight = false;
		private int cutFlag=0;
		
		public Nodes( int[][] pos,int Max,int deep) {					//max��Ҫ�µ���
			Nnum++;
			nodeId=Nnum;
//			System.out.println("");
//			System.out.print("��ǰNod��"+Nnum+"���:"+deep+"Max:"+Max);
			myPos=posCopy(pos);							//!!
			this.Max=Max;
			this.deep=deep;
			Min=(Max==1? 2:1);
			if (deep>0) {
				genNodeList();
			}
			if (deep==0) {												//�Զ�����Ҷ�ӽ���Ȩ��
				setWeight(getStWeight(myPos, Max));
			}
	//		System.out.println("weight:"+weight);
			if (Max==MAX) {
	//			System.out.println("MAX��,"+Max+"��"+"");			
			}
		}
		public Nodes(int[][] pos,int Max,int deep,int weight ,int cutF) {
			// TODO Auto-generated constructor stub
			Nnum++;
			nodeId=Nnum;
	//		System.out.println("��ǰNod��"+Nnum+"���:"+deep+"Max:"+Max);
			myPos=posCopy(pos);							//!!
			setWeight(weight);
			this.Max=Max;
			cutFlag=cutF;
			this.deep=deep;
			Min=(Max==1? 2:1);
			if (deep>0) {
				genNodeList();
			}
			if (deep==0) {												//�Զ�����Ҷ�ӽ���Ȩ��
				setWeight(getStWeight(myPos, Max));
			}//System.out.println("weight:"+weight);
		}
		public Nodes(int weight) {
			setWeight(weight);
			
		}
		//�ж��Ƿ��ӽڵ�������� ��
		private void genNodeList() {									//�����ӽڵ�   �Լ��Ƿ�����Ȩ�ظ�ֵ ��û���������ӽڵ� ��ȡ�ӽڵ��
			ArrayList<Nodes>  ndList=new ArrayList<Nodes>();
			int[][] gPos=posCopy(myPos);	
			if (!gotWeight) {											//��û��Ȩ��	�ȹ�����Ȩ�ؽڵ� Ϊ�˵õ���ʼȨ��	
				if(deep==1){											//��ʱ��СdeepΪ1 0�������ӽڵ�
					int fWeight=0;										//��һ����Ҫ����ȫ���ڵ�
					for(int i=0;i<15;i++){                           	//��һ����Ҫ����ȫ���ڵ�		
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
					int i3=0;													//�������  bug �õ���һ���ڵ�Ȩֵ 
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
									setWeight(fNodes.getWeight());					//����һ���ӽڵ�Ȩֵ���� �õ�Ȩֵ ֮�������Ȩֵ����						
								}else {												//�ѻ��Ȩֵ ���ɺ���						
									if (Max==MAX) 
										fNodes= new Nodes(gPos, Min, deep-1,weight,1);	//A��֦								
									else
										fNodes= new Nodes(gPos, Min, deep-1,weight,2);	//B��֦		
									
									int sWeight=fNodes.getWeight();
									//											�����ӽڵ���Ȩ�صıȽ�					
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
			if(gotWeight) {											//����ȨֵԼ��
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
							int sWeight=sNodes.getWeight();				//�����ӽڵ�Ȩֵ �����֦
							if (Max==MAX) {
								if (sWeight>=weight) {						//�ýڵ�Ϊmax�ڵ�  �ӽڵ�     
									weight=sWeight;	
									if(cutFlag==2){									//B��֦�� �ȱ��ڵ�MIn  B��֦
										cut=1;
									}
								}
								if (retWeight==1) {							//max�ڵ�������ӽڵ�Ȩֵ
									retWeight=sWeight;
								}else{
									int sw=sWeight;
									retWeight=(retWeight>sw?retWeight:sw);
								}
							}else {											// �� �ȱ�Max�ڵ� A��֦  ��ǰMin�ڵ�
								if (sWeight<=weight) {			
									weight=sWeight;	
									if (cutFlag==1) {							//�ӽڵ� min 
												//�ýڵ�����ӽڵ�Ȩֵ 
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
		public int[] findBestSNod(){							//�ҵ������һ��
		
			int bestWegt=sNodelist.get(0).getWeight();
			int[] bestAxis=sNodelist.get(0).getNodeAxis();
			if (MAX==Max) {												//��ǰmax�ڵ� 
				for (Nodes nod : sNodelist) {							//�ҵ������ӽڵ� �õ��� nodvec
				
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
		if(max==MAX){											//�ж���MAX�ڵ㻹��MIN�ڵ�
			
			talWt=fig2(chPos, MAX)-fig(chPos, MIN);				//max�ڵ�   ��һ����MAX
		}else{														//min�ڵ�  ��һ����MIN
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
	public static boolean testPos2(int[][] pos,int x,int y){
		int iMin=(x-2<=0?0:(x-2));
		int iMax=(x+2>=14?14:(x+2));
		int jMin=(y-2<=0?0:(y-2));
		int jMax=(y+2>=14?14:(y+2));
		int i=iMin;
		for(;i<=iMax;i++){
			for(int j=jMin;j<=jMax;j++){
				if (pos[i][j]!=0) {									//�����̣�i,j����Ϊ��ʱ
					if((x-i<3||i-x<3)&&(y-j<3||j-y<3))				// ���Ϊ���ĵط���������
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
		int leftR=(14-x>14-y?14-y:14-x);			//�Խ����˼��
		int rightL=(x>14-y?14-y:x);
		int rightR=(14-x>y?y:14-x);
		for(int i=0;i<=colL-4;i++){					//�����СΪ4
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
		if (leftL+leftR>=4) {						//������ڵ���4 ��������
			int LL=leftL+leftR;
			int lMiX=x-leftL;
			int lMiY=y-leftL;
			for(int i=0;i<=LL-4;i++){				//�����ϱ���
				int tal=1;
				for(int j=0;j<5;j++){
					tal*=pos[lMiX+j+i][lMiY+j+i];
				}
				if (tal==(max5)) {
					return true;
				}
			}
		}
		if (rightL+rightR>=4) {						//������ڵ���4 ��������
			int RL=rightL+rightR;
			int RMiX=x-rightL;
			int RMiY=y+rightL;
			for(int i=0;i<=RL-4;i++){				//�����±���
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
	public int fig(int [][] chessPos ,int Max){						//��������  max�ڵ���
			//System.out.println("��ǰ����:"+Max);
			int max=Max;												//��һ��
			int total=0;
			int bigCol=0;												//������ݶԽ� ÿ����������Ȩ��
			int bigRow=0;
			int lDiagonal=0;
			int rDiagonal=0;
			int posWeight=0;
			int chMt3=0;												//���ɱ��  ��������
			int chMt4=0;	
			int CM4=0;
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
					}else if (tal==max) {								//��һ����
						if (chessPos[i][j]!=0||chessPos[i][j+4]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//���ڿ�����  ���ɻ�1
							if (chessPos[i][j+1]*chessPos[i][j]!=0||
								chessPos[i][j+3]*chessPos[i][j+4]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//���
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
					}
					else if (tal==3*max) {								//3���� ������������
						if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//���ڿ����� ���ɻ�2 ��3 ��3
							if ((chessPos[i][j+1]*chessPos[i][j+2]*chessPos[i][j]!=0)||
								(chessPos[i][j+4]*chessPos[i][j+2]*chessPos[i][j+3]!=0)){//����mei0
								rWeight=(rWeight>=50 ? rWeight : 80 );		//�� 3
							}
							else if(chessPos[i][j]==0){					//���ڶϵ� �п��� ��3ɱ��
								if (j==10||chessPos[i][j+5]!=0) {			//�����ұ߽�  ��������
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;				
								}
							}
							else if (chessPos[i][j+4]==0) {
								if (j==0||chessPos[i][j-1]!=0) {			// ��߽� ���������
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}					
							}
							else {
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
							}
						}
						else{
							rWeight=(rWeight>=250 ? rWeight : 250 );		//��3
							chMt3++;
						}
					}
					else if (tal==4*max) {								//4���� ������������
						chMt4++;
						if (chessPos[i][j]==0) {						//�����ж���߿�
							if (j==10||chessPos[i][j+5]!=0) {			//�����ұ߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4
								CM4++;
							}
						}else if(chessPos[i][j+4]==0) {					 // �ұ�Ϊ��
							if (j==0||chessPos[i][j-1]!=0) {			// ��߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else {						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
								CM4++;
							}
						}else {											//�м��п�  ���ɳ�4--
							rWeight=(rWeight>=600 ? rWeight : 600 );
						}
					}
					else if (tal==5*max) {								//5���� ������������
						rWeight=(rWeight>=25000 ? rWeight : 25000 );
					}	
				}
				bigCol=(bigCol>=rWeight ? bigCol : rWeight );
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
						if (chessPos[j][i]!=0||chessPos[j+4][i]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//���ڿ�����
							if (chessPos[j+1][i]*chessPos[j][i]!=0||
								chessPos[j+3][i]*chessPos[j+4][i]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//��2	
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );		  //���ɻ�1
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
					}
					else if (tal==3*max) {								//3���� ������������
						if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//���ڿ����� ���ɻ�2 ��3
							if( (chessPos[j][i]*chessPos[j+1][i]*chessPos[j+2][i]!=0)|| 
								(chessPos[j+4][i]*chessPos[j+3][i]*chessPos[j+2][i]!=0)){//����mei��0
								rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3//����
								}	
							else if(chessPos[j][i]==0){	
								if (j==10||chessPos[j+5][i]!=0) {
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}
							}
							else if(chessPos[j+4][i]==0){
								if (j==0||chessPos[j-1][i]!=0) {
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}							
							}
							else {													
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
							}
						}
						else{
							rWeight=(rWeight>=250 ? rWeight : 250 );		//��3
							chMt3++;
						}
					}
					else if (tal==4*max) {								//4���� ������������
						chMt4++;
						if (chessPos[j][i]==0) {						//�����ж���߿�
							if (j==10||chessPos[j+5][i]!=0) {			//�����ұ߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
								CM4++;
							}
						}else if(chessPos[j+4][i]==0) {					 // �ұ�Ϊ��
							if (j==0||chessPos[j-1][i]!=0) {			// ��߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else {						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
								CM4++;
							}
						}else {											
							rWeight=(rWeight>=600 ? rWeight : 600 );	//�м��п�  ���ɻ�3++
						}
					}
					else if (tal==5*max) {								//5���� ������������
						rWeight=(rWeight>=25000 ? rWeight : 25000 );
					}	
				}
				bigRow=(bigRow>=rWeight ? bigRow : rWeight );
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
						if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){	//���ڿ�����  ���ɻ�1
							if (chessPos[i+1][j+1]*chessPos[i][j]!=0||
								chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//���
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
					}
					else if (tal==3*max) {								//3���� ������������
						if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){		//���ڿ����� ���ɻ�2 ��3
							if( (chessPos[i][j]*chessPos[i+1][j+1]*chessPos[i+2][j+2]!=0)|| 
								(chessPos[i+2][j+2]*chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0)){//����mei��0
								rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3 ����
							}
							else if (chessPos[i][j]==0) {					//���������ұ�
								if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//�������±߽�  ���ұ����� 
									rWeight=(rWeight>=80?rWeight:80 );	//��3
								}else {								
									rWeight=(rWeight>=200 ? rWeight : 200 );	//������3+
									chMt3++;
									}
								}							
							else if (chessPos[i+4][j+4]==0) {		
								if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// ���ϱ߽� ���������
									rWeight=(rWeight>=80?rWeight:80 );		// ��3
								}else {						
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}
							}
							else {
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
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
							if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//�������±߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}
							else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
								CM4++;
							}
						}else if(chessPos[i+4][j+4]==0) {				// ���±�Ϊ��
							if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// ���ϱ߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}
							else{ 						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
								CM4++;
							}
						}else {											
							rWeight=(rWeight>=600 ? rWeight : 600 );	//�м��п�  ���ɻ�3++
						}					
					}
					else if (tal==5*max) {								//5���� ������������
						rWeight=(rWeight>=25000 ? rWeight : 25000 );
					}	
				}
				lDiagonal=(lDiagonal>=rWeight?lDiagonal:rWeight);
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
				else if (tal==max) {									//һ����
						if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0){	//���ڿ�����  ���ɻ�1
							if (chessPos[i+1][j-1]*chessPos[i][j]!=0||
								chessPos[i+3][j-3]*chessPos[i+4][j-4]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//���
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
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
									}
								}							
							else if(chessPos[i+4][j-4]==0) {				// ���ϱ�Ϊ��	
								if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// ���±߽� ���������
									rWeight=(rWeight>=80?rWeight:80 );		// ��3
								}else {					
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}
							}
							else {
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
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
							if ((i==10||j==4)||chessPos[i+5][j-5]!=0) {//�������±߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}
							else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
								CM4++;
							}
						}else if(chessPos[i+4][j-4]==0) {				// ���±�Ϊ��
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// ���ϱ߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else{ 						
								rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
								CM4++;
							}
						}else {											
							rWeight=(rWeight>=600 ? rWeight : 600 );	//�м��п�  ���ɻ�3++
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
					cm=5000;
				}else if(chMt4==1){
					cm=2500;
				}else {
					cm=400;
				}
			}

		return total+cm+(CM4>0?10000:0);
		}
	public int fig2(int [][] chessPos ,int Max){						//��������  max�ڵ���

			int max=Max;												//��һ��
			int total=0;
			int bigCol=0;												//������ݶԽ� ÿ����������Ȩ��
			int bigRow=0;
			int lDiagonal=0;
			int rDiagonal=0;
			int posWeight=0;
			int chMt3=0;												//���ɱ��  ��������
			int chMt4=0;												
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
					}else if (tal==max) {								//��һ����
						if (chessPos[i][j]!=0||chessPos[i][j+4]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//���ڿ�����  ���ɻ�1
							if (chessPos[i][j+1]*chessPos[i][j]!=0||
								chessPos[i][j+3]*chessPos[i][j+4]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//���
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
					}
					else if (tal==3*max) {								//3���� ������������
						if (chessPos[i][j]!=0||chessPos[i][j+4]!=0){	//���ڿ����� ���ɻ�2 ��3 ��3
							if ((chessPos[i][j+1]*chessPos[i][j+2]*chessPos[i][j]!=0)||
								(chessPos[i][j+4]*chessPos[i][j+2]*chessPos[i][j+3]!=0)){//����mei0
								rWeight=(rWeight>=50 ? rWeight : 80 );		//�� 3
							}
							else if(chessPos[i][j]==0){					//���ڶϵ� �п��� ��3ɱ��
								if (j==10||chessPos[i][j+5]!=0) {			//�����ұ߽�  ��������
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;				
								}
							}
							else if (chessPos[i][j+4]==0) {
								if (j==0||chessPos[i][j-1]!=0) {			// ��߽� ���������
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}					
							}
							else {
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
							}
						}
						else{
							rWeight=(rWeight>=250 ? rWeight : 250 );		//��3
							chMt3++;
						}
					}
					else if (tal==4*max) {								//4���� ������������
						chMt4++;
						if (chessPos[i][j]==0) {						//�����ж���߿�
							if (j==10||chessPos[i][j+5]!=0) {			//�����ұ߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4
							}
						}else if(chessPos[i][j+4]==0) {					 // �ұ�Ϊ��
							if (j==0||chessPos[i][j-1]!=0) {			// ��߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else 						
							rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
						}else {											//�м��п�  ���ɳ�4--
							rWeight=(rWeight>=600 ? rWeight : 600 );
						}
					}
					else if (tal==5*max) {								//5���� ������������
						rWeight=(rWeight>=25000 ? rWeight : 25000 );
					}	
				}
				bigCol=(bigCol>=rWeight ? bigCol : rWeight );
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
						if (chessPos[j][i]!=0||chessPos[j+4][i]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//���ڿ�����
							if (chessPos[j+1][i]*chessPos[j][i]!=0||
								chessPos[j+3][i]*chessPos[j+4][i]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//��2	
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );		  //���ɻ�1
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
					}
					else if (tal==3*max) {								//3���� ������������
						if (chessPos[j][i]!=0||chessPos[j+4][i]!=0){	//���ڿ����� ���ɻ�2 ��3
							if( (chessPos[j][i]*chessPos[j+1][i]*chessPos[j+2][i]!=0)|| 
								(chessPos[j+4][i]*chessPos[j+3][i]*chessPos[j+2][i]!=0)){//����mei��0
								rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3//����
								}	
							else if(chessPos[j][i]==0){	
								if (j==10||chessPos[j+5][i]!=0) {
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}
							}
							else if(chessPos[j+4][i]==0){
								if (j==0||chessPos[j-1][i]!=0) {
									rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3
								}else {
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}							
							}
							else {													
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
							}
						}
						else{
							rWeight=(rWeight>=250 ? rWeight : 250 );		//��3
							chMt3++;
						}
					}
					else if (tal==4*max) {								//4���� ������������
						chMt4++;
						if (chessPos[j][i]==0) {						//�����ж���߿�
							if (j==10||chessPos[j+5][i]!=0) {			//�����ұ߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
							}
						}else if(chessPos[j+4][i]==0) {					 // �ұ�Ϊ��
							if (j==0||chessPos[j-1][i]!=0) {			// ��߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else 						
							rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
						}else {											
							rWeight=(rWeight>=600 ? rWeight : 600 );	//�м��п�  ���ɻ�3++
						}
					}
					else if (tal==5*max) {								//5���� ������������
						rWeight=(rWeight>=25000 ? rWeight : 25000 );
					}	
				}
				bigRow=(bigRow>=rWeight ? bigRow : rWeight );
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
						if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){	//���ڿ�����  ���ɻ�1
							if (chessPos[i+1][j+1]*chessPos[i][j]!=0||
								chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//���
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
					}
					else if (tal==3*max) {								//3���� ������������
						if (chessPos[i][j]!=0||chessPos[i+4][j+4]!=0){		//���ڿ����� ���ɻ�2 ��3
							if( (chessPos[i][j]*chessPos[i+1][j+1]*chessPos[i+2][j+2]!=0)|| 
								(chessPos[i+2][j+2]*chessPos[i+3][j+3]*chessPos[i+4][j+4]!=0)){//����mei��0
								rWeight=(rWeight>=80 ? rWeight : 80 );		//�� 3 ����
							}
							else if (chessPos[i][j]==0) {					//���������ұ�
								if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//�������±߽�  ���ұ����� 
									rWeight=(rWeight>=80?rWeight:80 );	//��3
								}else {								
									rWeight=(rWeight>=200 ? rWeight : 200 );	//������3+
									chMt3++;
									}
								}							
							else if (chessPos[i+4][j+4]==0) {		
								if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// ���ϱ߽� ���������
									rWeight=(rWeight>=80?rWeight:80 );		// ��3
								}else {						
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}
							}
							else {
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
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
							if ((j==10||i==10)||chessPos[i+5][j+5]!=0) {//�������±߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}
							else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
							}
						}else if(chessPos[i+4][j+4]==0) {				// ���±�Ϊ��
							if ((j==0||i==0)||chessPos[i-1][j-1]!=0) {	// ���ϱ߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}
							else 						
							rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
						}else {											
							rWeight=(rWeight>=600 ? rWeight : 600 );	//�м��п�  ���ɻ�3++
						}					
					}
					else if (tal==5*max) {								//5���� ������������
						rWeight=(rWeight>=25000 ? rWeight : 25000 );
					}	
				}
				lDiagonal=(lDiagonal>=rWeight?lDiagonal:rWeight);
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
				else if (tal==max) {									//һ����
						if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0) {	//������
							rWeight=(rWeight>=2?rWeight:2);
						}else {
							rWeight=(rWeight>=10 ? rWeight : 10 );		//��1	
						}
					}
					else if (tal==2*max) {								//������ ������������
						if (chessPos[i][j]!=0||chessPos[i+4][j-4]!=0){	//���ڿ�����  ���ɻ�1
							if (chessPos[i+1][j-1]*chessPos[i][j]!=0||
								chessPos[i+3][j-3]*chessPos[i+4][j-4]!=0) {
								rWeight=(rWeight>=20 ? rWeight : 20 );	//���
							}else
							rWeight=(rWeight>=10 ? rWeight : 10 );
						}else {			
							rWeight=(rWeight>=50 ? rWeight : 50 );		//��2
						}
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
									}
								}							
							else if(chessPos[i+4][j-4]==0) {				// ���ϱ�Ϊ��	
								if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// ���±߽� ���������
									rWeight=(rWeight>=80?rWeight:80 );		// ��3
								}else {					
									rWeight=(rWeight>=200 ? rWeight : 200 );	//��3
									chMt3++;
								}
							}
							else {
								rWeight=(rWeight>=50 ? rWeight : 50 );	//���ɻ�2+
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
							if ((i==10||j==4)||chessPos[i+5][j-5]!=0) {//�������±߽�  ���ұ����� 
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}
							else {
								rWeight=(rWeight>=2500 ? rWeight : 2500 );	//��4g
							}
						}else if(chessPos[i+4][j-4]==0) {				// ���±�Ϊ��
							if ((i==0||j==14)||chessPos[i-1][j+1]!=0) {	// ���ϱ߽� ���������
								rWeight=(rWeight>=600?rWeight:600 );	//��4
							}else 						
							rWeight=(rWeight>=2500 ? rWeight : 2500 );		//��4
						}else {											
							rWeight=(rWeight>=600 ? rWeight : 600 );	//�м��п�  ���ɻ�3++
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
