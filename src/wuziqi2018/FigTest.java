package wuziqi2018;


public class FigTest {

	

	 static	public int fig(int [][] chessPos ,int Max){						//��������  max�ڵ���
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
	static 	public int fig2(int [][] chessPos ,int Max){						//��������  max�ڵ���
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
