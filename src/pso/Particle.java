package pso;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class Particle {
	    private static final double RAND_MAX = 1;
//
	    private int[] pbestPos = new int [MGraph.MAX_VERTS];//个体最优解坐标
	    float pbest;//个体最优适应度值
	    private int tag;
//
		private int[] pos = new int[MGraph.MAX_VERTS];//绮掑瓙浣嶇疆
	    private int[] group = new int[MGraph.MAX_VERTS];//group用来存储节点的社区标号；
	    public static int l = 2;//缁忚繃l姝ユ父璧帮紝鍙互灏濊瘯涓嶅悓鐨刲鍊?
	    private float[] n = new float[MGraph.MAX_VERTS] ;
	    private int[] index = new int[MGraph.MAX_VERTS];
	    private int[] v = new int[MGraph.MAX_VERTS];//粒子速度
	    public float Q;
	    public float QMax;
	    public int Vmax;
	    public double C1 = 2.0;//认知系数
	    public double C2 = 2.0;//社会系数，一般取2.0
	    private  int c = 0;//最初的社区编号；
	    public Particle(int i) {
			// TODO Auto-generated constructor stub
	    	tag = i;
		}
	    public int getT(){
	    	return tag;
	    }
		//
	    public void setPbestPos(int i,int data){
	    	pbestPos[i] = data;
	    }
	    public int getPbestPos(int i){
	        return pbestPos[i];
	    }
//
	    public void setGroup(int i,int data){//设置节点i的分组为data;
	        group[i] = data;
	    }
	    public int getGroup(int i){
	        return group[i];
	    }
	    public void setIndex(int i,int data){
	        index[i] = data;
	    }
	    public int getIndex(int i){
	        return index[i];
	    }
	    public void setV(int i,int data){
	        v[i] = data;
	    }
	    public int getV(int i){
	        return v[i];
	    }
	    public float p(int i,int k){//姹備竴鐐瑰埌鍙︿竴鐐圭殑姒傜巼锛屽鏋滀竴鐐逛笌鍙︿竴鐐归偦鎺ュ嵆缁熻鏈夊灏戦偦鎺ョ偣锛屽鏋滀笉閭绘帴鍒欎负0
	        float n = 0;
	        if(MGraph.getadjMat(i,k) == 0) return 0;
	        n = MGraph.getVertexDegree(i);
	        return 1/n;
	    }
	  public void makeN(int t) {//t是目的节点，从节点1开始，计算每个节点到节点t,经过L=2步随机游走的概率
	        for(int i =0;i < MGraph.MAX_VERTS; i++){
	            for(int j = 0; j < MGraph.MAX_VERTS; j++){
	            	if(i==t){
	            		n[i]=1;
	            	}else{
	            	/*	if(j==i ||j== t){
	            			continue;
	            		}*/
	            			float k = p(i,j);
			                float m = p(j,t);
			                n[i] = k * m + n[i];
	            	}
	            }
	        }
	  }
	    public void sort() {	//鎺掑簭绠楁硶
	        for(int i = 0;i < MGraph.MAX_VERTS;i++){
	            index[i] = i;
	        }
	        int a = n.length;
	        for (int i = 0; i < a-1; i++){
	        	 for (int j = i+1; j <a ; j++){
		                if (n[i] <n[j]) {// if (n[i] > n[j])是从小到大排序 ,应该是从大到小排序；
		                    float temp = n[i];
		                    n[i] = n[j];
		                    n[j] = temp;
		                    int _temp = index[i];
		                    index[i] = index[j];
		                    index[j] = _temp;
		                }
		            }
	        	}
	          // group=index;为节点排序了，但是group里面存储的是社区的标号；
	    }
	    public int b(int t, int f) {
	        if(getPos(t) == getPos(f))
	            return 1;
	        else return 0;
	    }
	    public float Q(){//计算Q函数，并返回；根据节点的序列index[]来计算；eg:index[]={5,1,2,3,4,6,7,8,9},也需要用到group;
	        int m = MGraph.nVerts;//图中所有边的数量
	        float Q = 0;
	        for(int t = 0; t < MGraph.MAX_VERTS; t++){
	            for(int f = 0; f < MGraph.MAX_VERTS; f++){
	                //int t = index[i];
	                //int f = index[j];
	                Q += (float)(MGraph.getadjMat(t,f) -  MGraph.getVertexDegree(t) * MGraph.getVertexDegree(f) / (2 * m)) * b(t,f);
	            }
	        }
	        return Q/(2*m) ;
	    }
	     public float QChange(int i){//表示在i处发生分裂
	    	int m = MGraph.nVerts;
	    	//float QChange = 0;
	    	float Q=0;
	    	float Q2=0;
	    	for(int j = 0;j < i;j++){
	    		int t = index[i];
                int f = index[j];
	    		Q+= (float) MGraph.getadjMat(t,f)
	    				- (float)(MGraph.getVertexDegree(t) * MGraph.getVertexDegree(f) / (2 * m));
	    	}
	     	for(int j =i+1;j<MGraph.MAX_VERTS;j++){
	    		int t = index[i];
                int f = index[j];
	    		Q2+= (float) MGraph.getadjMat(t,f)
	    				- (float)(MGraph.getVertexDegree(t) * MGraph.getVertexDegree(f) / (2 * m)) ;
	    	}
	    	return (Q2-Q)/ m;
	    	}
	   public void subGroup(int a,int b){
	    	c++;
	    	int i ;
	    	int d = getGroup(a);
	    	if(a==b)return;
	    	for(i = a;i < b + 1;i++){///////////////////
	    		if(QChange(i) < 0) {
	    			break;
	    		}else{
	    			//for(int j=i;j<b;j++){
	    			//	setGroup(j,c);
	    			//}
	    			setGroup(i,c);
	    		}
	    	}
	    	if(i >= b + 1) return;     //////////////////////
	       if(Q() < QMax) {//若该划分不是最优的划分，则恢复原来的社区划分情况。
	        	for(int j = a;j < i;j++){  /////////////////////////////
	        		setGroup(j,d);
	        	}
	        	return;
	       }
	        else {
	        	QMax = Q();
	        	if(a < i-1){
	        	subGroup(a,i-1);
	        	subGroup(i,b);
	        	}
	        	else return;
	        }
	    }
	    public void igmrw(int t){
	        makeN(t);
	        sort();
	        for(int i = 0; i < MGraph.MAX_VERTS; i++){
	            setGroup(i,0);
	        }
	        subGroup(0,MGraph.MAX_VERTS -1);
	        for(int i = 0;i < MGraph.MAX_VERTS; i++){
	        	setPos(index[i],group[i]);
	        }
	    }
	    public void setPos(int i, int j) {
			// TODO Auto-generated method stub
			pos[i] = j;
		}
		public void rrm() {//修改过
//
			int[] u = new int[MGraph.MAX_VERTS];
//
			u[0] = 1;
			int max = u[0];
	        for (int i = 1; i < MGraph.MAX_VERTS; i++) {
	            int j;
	        	for (j = 0; j < i; j++) {
	                if (pos[i] != pos[j]) continue;
	                else {
	                    u[i] = u[j];
	                    break;
	                }
	            }
	            if ((j == i) && (u[i] == 0)) {
	                u[i] = ++max;
	            }
	        }
//	        //新增加了将u【i】的值重新赋值给pos【i】
	        for(int i = 0; i <  MGraph.MAX_VERTS;i++){
	        	pos[i] = u[i];
	        }
	    }
	   public double rnd(int low,int uper){
		   Random random  = new Random();
		   return (random.nextDouble() / (double)RAND_MAX) * ((uper) - (low)) + (low);
	   }
	   public int sig(double x){
		   Random random  = new Random();
		   if(random.nextDouble() < (1/(1 + Math.pow(Math.E,x)))) return 1;
		   else return 0;
	   }
	   public int nBest(int i){
		// map的key存放数组中的数字，value存放该数字出现的次数
	     HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		  for(int j = 0;j < MGraph.MAX_VERTS;j++){
			 if (MGraph.getadjMat( i, j) == 1){
				 if(map.containsKey(pos[j]))        
		            {
		                int formerValue = map.get(pos[j]);
		                map.put(pos[j], formerValue + 1);    // 该数字出现的次数加1
		            }
		            else
		            {
		                map.put(pos[j], 1);    // 该数字第一次出现
		            }
			 } 
		  }
		  Collection<Integer> count = map.values();
	        // 找出map的value中最大值，也就是数组中出现最多的数字所出现的次数
	        int maxCount = Collections.max(count);
	        int maxNumber = 0;
	        for(Map.Entry<Integer, Integer> entry : map.entrySet())
	        {
	            //得到value为maxCount的key，也就是数组中出现次数最多的数字
	            if(entry.getValue() == maxCount)
	            {
	                maxNumber = entry.getKey();
	            }
	        }
	        return maxNumber;
	   }
//
	   public void updatePosition(){
		   double r1,r2;
		   Random random  = new Random();
		   r1 = random.nextDouble();
		   r2 = random.nextDouble();
		   int i;
		   for(i = 0;i < MGraph.MAX_VERTS;i++){
			   //产生惯性权值
			   double paramater_w = 0.5 + rnd(0,1) / 2;
			   //更新速度，利用粒子的pbestPos和全局的gbestPosGlobal[]
			   double y = paramater_w * getV(i) + C1 * r1 * (pbestPos[i] ^ pos[i]) + C2 * r2 *(ParticleSwarm.gbestPos[i] ^ pos[i]); 
			   setV(i, sig(y));
			//   判断超出最大速度和最小速度
			   if(v[i] < -Vmax) v[i] = -Vmax;
			   else if(v[i] > Vmax) v[i] = Vmax;
		   //更新位置
			   if(v[i] == 0) pos[i] = pos[i];
			   else pos[i] = nBest(i);
		   }
	   }
	   public void updatePBest(){
		   //如果当前的适应度大于局部最优的适应度，则将当前的位置赋给局部最优的位置
		   if(this.Q() > pbest){
			   //改变局部最优的适应度
			   pbest = this.Q();
			   //改变局部最优的位置
			   for(int i = 0;i < MGraph.MAX_VERTS;i++){
				   pbestPos[i] = pos[i];
			   }
		   }
	   }
//
	   public int getPos(int j) {
		// TODO Auto-generated method stub
		return pos[j];
	}
}