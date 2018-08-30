package wuziqi2018;
import java.util.ArrayList;
import java.util.Vector;

public class GameTree { 						//���ص�ǰ�����·�
	private int MAX=0;
	private int MIN=0;
	private int depth=4;											//Ĭ�ϲ������
	private ChessBoard cB;
	private Vector<Integer> result=null;							//���Ž�
	public Vector<Integer> getRut(){
		return result;
	}
	public GameTree(ChessBoard cBoard) {
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
	public GameTree(ChessBoard cBoard ,int deep) {					//���޸�Ĭ�ϲ������
		this(cBoard);
		depth=deep;
	}
	
	static int Nnum=0;
	class Nodes{
		public int nodeId=0;
		private int[][] myPos=new int[15][15];								//��ǰ�ڵ�� ����
		private Vector<Integer> nodVec=new Vector<>();						//��ǰ�ڵ�   �ߵ���һ��
		private int deep;
		private ArrayList<Nodes> sNodelist=new ArrayList<Nodes>();
		private int Max;
		private int Min;
		private int weight=0;
		public Nodes( int[][] pos,int Max,int deep) {						//max��Ҫ�µ���
			Nnum++;
			nodeId=Nnum;
																			//		System.out.println("��ǰNod��"+Nnum);
			myPos=posCopy(pos);												//!!
			this.Max=Max;
			this.deep=deep;
			Min=(Max==1? 2:1);
																			//		System.out.println("�ڵ���һ����"+Max);
			if (deep>0) {
				sNodelist=genNodelist(myPos, Min, deep-1);
			}
		}
		public Vector<Integer> findBestSNod(){					//�ҵ������һ��
			System.out.println("����õĽڵ�");
			int bestWegt=sNodelist.get(0).getWeight();
			Vector<Integer> bestVec=sNodelist.get(0).getNodVec();
			if (MAX==Max) {											//��ǰmax�ڵ� 
				for (Nodes nod : sNodelist) {						//�ҵ������ӽڵ� �õ��� nodvec
					System.out.print("maxS:"+getMax()+"��  ��"+nod.getWeight()+"����"+nod.nodVec+" ,�㣺"+getStWeight(nod));
					if (nod.getWeight()>bestWegt) {
						bestWegt=nod.getWeight();
						bestVec=nod.getNodVec();
		//				System.out.println("MAX�ڵ��·�"+nod.getNodVec());
					}
				}
			}else{
				for (Nodes nod : sNodelist) {		
					System.out.print("minS"+getMax()+"��  ��"+nod.getWeight()+"����"+nod.nodVec);
					if (nod.getWeight()<bestWegt) {
						bestWegt=nod.getWeight();
						bestVec=nod.getNodVec();
		//				System.out.println("MIN�ڵ��·�"+nod.getNodVec());
					}
				}
			}
		//	System.out.println("��"+bestVec.elementAt(0)+","+bestVec.elementAt(1));
			return bestVec;	
		}
		public void pickNodeWeight(){							//Ȩ�ط��ط���  ���·���ֵ
			  if (deep>=1) {
				for (Nodes nod : sNodelist) {
					nod.pickNodeWeight();
					}
				if (Max==MAX) {									//����ڵ� ��������Ȩ��
					int maxWeight=sNodelist.get(0).weight;		//�ȷ��ص�һ��
					
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
						System.out.println("pos:("+nodVec.elementAt(0)+","+nodVec.elementAt(1)+")"+"����quanֵ"+weight);						
					}
					else if(deep==4){
						System.out.println("���·���ֵ"+weight+"�ڵ�"+nodeId);
					}
					
				}
				else if (Max!=MAX) {									//��С�ڵ� ����С����Ȩ��
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
						System.out.println("pos:("+nodVec.elementAt(0)+","+nodVec.elementAt(1)+")"+"����quanֵ"+weight);					
					}
					else if(deep==4){
						System.out.println("���·���ֵ"+weight+"�ڵ�"+nodeId);
					}
					
				}
			  }
			  else if(deep==0){							 		//�� Ҷ�ӽ��Ȩֵ	
				  setWeight(getStWeight(this));					//����Ҷ�ӽ��Ȩֵ
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
		Nodes rNodes=new Nodes(cB.getPos(), MAX, depth);		//�����ڵ� �Ӹ��ڵ㿪ʼ
		rNodes.pickNodeWeight();								//��Ҷ�ӽ�� ����Ȩֵ
		vector=rNodes.findBestSNod();							// �ҵ�Ȩֵ��õ��·�
		System.out.println("�õ�����"+vector);
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
	public ArrayList<Nodes> genNodelist (int[][] pos ,int Max ,int deep){  		 // ����min�ڵ�   ����max�ڵ�  ��ȶ��ٵĽڵ�
																					//		System.out.println("������"+deep+"dep�Ľڵ�");
		ArrayList<Nodes> ndList=new ArrayList<>();
		if (deep>=0) {
			int i = 0;
			for (; i < 15; i++) {	
				for (int j = 0; j < 15; j++) { 										// �ɸĽ��� ���ٱ��� ��ȫ��
					if (pos[i][j] == 0&&testPos1(pos, i, j)) {
						pos[i][j] = (Max==MAX?MIN:MAX);								//����1 ��˵��Ҫ�µ���2����
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
	public int getStWeight(int[][] chPos,int max){
		int  talWt = 0;
		int min=(max==1? 2:1);
		if(max==MAX){											//�ж���MAX�ڵ㻹��MIN�ڵ�
	//		System.out.print("������2*MAX-min");
			talWt=fig2(chPos, MAX)-fig(chPos, MIN);				//max�ڵ�   ��һ����MAX
	//		System.out.println("///"+MAX+"��Ȩֵ"+fig(chPos, MAX)+MIN+"��Ȩֵ"+fig(chPos, MIN));
		}else{														//min�ڵ�  ��һ����MIN
	//		System.out.print("������2*MIN-max");
			talWt=fig(chPos, MAX)-fig2(chPos, MIN);
	//		System.out.println("///"+MAX+"��Ȩֵ"+fig(chPos, MAX)+MIN+"��Ȩֵ"+fig(chPos, MIN));
		}
		return talWt;
	}
	private int getStWeight(Nodes node) {
		return getStWeight(node.getMyPos(), node.getMax());
	}
//	public void fitTest(){
//		System.out.println(getStWeight(cB.getPos(), MAX));
//	}
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
//		System.out.println(Max+"����"+"��"+bigCol+"��"+bigRow+"��"+lDiagonal+"��"+rDiagonal+"PW:"+posWeight);
	return total+cm;
	}
	public int fig2(int [][] chessPos ,int Max){						//��������  max�ڵ���
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
//		System.out.println(Max+"����"+"��"+bigCol+"��"+bigRow+"��"+lDiagonal+"��"+rDiagonal+"PW:"+posWeight);
	return 3*(total+cm)+chMt3*2400+chMt4*20000;
	}

}
