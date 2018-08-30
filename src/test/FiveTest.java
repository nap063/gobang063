package test;

public class FiveTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [][] p={{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		
		int [][] t={{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,2,0,0,0,0,0,1,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, 
		            {0,0,0,0,0,0,2,2,2,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},				
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		System.out.println(fiveEst(p,10,14));
	}
	static int MAX=2;
	static public boolean fiveEst(int[][] pos,int x,int y){
		pos[x][y]=MAX;
		int miX,maX,miY,maY;
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
			//System.out.println("lie:("+x+","+(miY+i)+")");
			int tal=1;
			for(int j=0;j<5;j++){
				tal*=pos[x][miY+i+j];
			//System.out.println(" "+tal);
			}
			if (tal==MAX) {
				return true;
			}
		}
		for(int i=0;i<=rowL-4;i++){
			//System.out.println("row:("+(miX+i)+","+y+")");
			int tal=1;
			for(int j=0;j<5;j++){
				tal*=pos[miX+i+j][y];
			//	System.out.println(" "+tal);
			}
			if (tal==MAX) {
				return true;
			}
		}
		if (leftL+leftR>=4) {						//间隔大于等于4 可能五子
			int LL=leftL+leftR;
			int lMiX=x-leftL;
			int lMiY=y-leftL;
			for(int i=0;i<=LL-4;i++){				//从左上遍历
				int tal=1;
				//System.out.println("起点"+"("+(lMiX+i)+","+(lMiY+i)+")");
				for(int j=0;j<5;j++){
				//	System.out.print("  乘以"+"("+(lMiX+j)+","+(lMiY+j)+")");
					tal*=pos[lMiX+j+i][lMiY+j+i];
				//	System.out.println(tal);
				}
				if (tal==MAX) {
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
				//System.out.println("R起点"+"("+(RMiX+i)+","+(RMiY-i)+")");
				for(int j=0;j<5;j++){
					tal*=pos[RMiX+j+i][RMiY-j-i];
				//System.out.println(tal);
				}
				if (tal==MAX) {
					return true;
				}
			}
		}return false;
	}
}
