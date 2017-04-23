package pso;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class ParticleSwarm {
	private static int PSO_popsize = 34;//指定容量的大小
	private static final int PSOMAXGEN = 50;//迭代次数
	private  ArrayList<Particle> PSO_pop ;//单个粒子定义为粒子群类的属性
	private HashMap<Integer, Integer> mapPso = new HashMap<Integer, Integer>();
	public static int gbest;//最优粒子的序号
	public static float pbest;
	public static int[] gbestPos = new int[MGraph.MAX_VERTS];
	public ParticleSwarm(){
		
	}
	public void init(){
		this.PSO_pop = new ArrayList<Particle>(PSO_popsize);
		int n = 0;
    	for(int i = 0;i < MGraph.MAX_VERTS;i++){
    		Particle particle = new Particle(i);
    		
    		particle.pbest = -100;//将局部所有适应度初始化
    	    particle.igmrw(i);
	        particle.rrm();
	        PSO_pop.add(particle);
	        int j;
	        for ( j = 0; j < n; j++) {//如果重复则删除
	                int k;
	            	for (k = 0; k < MGraph.MAX_VERTS; k++) {
	                    if (PSO_pop.get(j).getPos(k) != PSO_pop.get(n).getPos(k)) {
	                        //j = i;
	                        break;
	                    }
	            	}
	                if (k == MGraph.MAX_VERTS) {
	                    //删除这一粒子
	                	PSO_pop.remove(PSO_pop.get(PSO_pop.size() - 1));
	                	mapPso.put(i, j);
	                	break;
	                }
	                
	        }
	        if(j == n){
	            mapPso.put(i, n);
	            n++;
	            
	        }         
    	}
    }    
    public void getGBest(){
    	//遍历所有粒子，将适应度最大的粒子的适应度和位置赋给全局最优
    	for(int i = 0;i < PSO_pop.size();i++){
    		if((PSO_pop.get(i).pbest > pbest)){
    			pbest = PSO_pop.get(i).pbest;
    			gbest = i;
    			gbestPos[i] = PSO_pop.get(i).getPbestPos(i);
    		}
    	}
    }
    ///////////
    public void mico(){
    	int m;
    	Random random = new Random();
    	m = random.nextInt(PSO_pop.size());
    	if(m < 3){
    		m = m + 3;
    	}
    	int[][] n = new int[m][MGraph.MAX_VERTS];
    	Particle a = new Particle(PSO_pop.size()); 
    	for(int i = 0;i < m;i++){
    		Random random1 = new Random();
    		for(int j = 0;j < MGraph.MAX_VERTS;j++){
    			int f = random1.nextInt(PSO_pop.size());
    			n[i][j] = PSO_pop.get(f).getPos(j);
    		}
    	}
    	for(int i = 0;i < MGraph.MAX_VERTS;i++){
    		 HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
   		  	 for(int j = 0;j < m;j++){
   				 if(map.containsKey(n[j][i]))        
   		            {
   		                int formerValue = map.get(n[j][i]);
   		                map.put(n[j][i], formerValue + 1);    // 该数字出现的次数加1
   		            }
   		            else
   		            {
   		                map.put(n[j][i], 1);    // 该数字第一次出现
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
   	        a.setGroup(i, maxNumber);
   	       
    	}
    	 a.rrm();
    	 for(int i = 0;i < MGraph.MAX_VERTS;i++){
	        a.setIndex(i, i);
    	 }
	        if(a.pbest > pbest){
	        	pbest = a.pbest;
	        	gbest = -1;
	        	for(int i = 0;i < MGraph.MAX_VERTS;i++){
	        		gbestPos[i] = a.getPbestPos(i);
	        	}
	        }
    	}
    public void nbm(int k,int j){
    	float pm = (float) 0.1;//突变概率
    	Random random = new Random();
    	float m = random.nextFloat();
    	if(m < pm){
    		Random random1 = new Random();
    		int x = random1.nextInt(MGraph.MAX_VERTS);
    		for(int i = 0;i < k;i++){
    			if((MGraph.getadjMat(i,k) == 1)){
    				PSO_pop.get(mapPso.get(i)).setPos(x, PSO_pop.get(j).getPos(x));  
    				PSO_pop.get(mapPso.get(i)).rrm();
    			}
    		}
    	}
    	else return;
    }
    public void search(){
    	int gen = 0;
    	while(gen < PSOMAXGEN){
    		//每个粒子进行运动求值更新pbest
    		for(int k = 0;k < PSO_pop.size();k++){
    			//更新每个粒子的位置
    			
    			int i = PSO_pop.get(k).getT();
    			PSO_pop.get(k).updatePosition();
    			//更新每个粒子的局部最优适应度
    			PSO_pop.get(k).updatePBest();
    			
    			nbm(i,k);
    			
    		}
    		//更新全局最优，注意更新包含更新用于通信的全局变量？？？
    		
    		gen++;
    		getGBest();
    		mico();
      }
    	for(int j = 0;j < PSO_pop.size();j++){
    			for(int i = 0; i < MGraph.MAX_VERTS;i++){
    				System.out.print((i+1)+"    ");
    				System.out.println(PSO_pop.get(j).getPbestPos(i));
    			}
    		System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
    	}
    	for(int i = 0;i <  MGraph.MAX_VERTS;i++){
    		System.out.print((i+1)+"    ");
    		System.out.println(gbestPos[i]);
    }

}
}  


 
    