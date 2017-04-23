package pso;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class ParticleSwarm {
	private static int PSO_popsize = 34;//ָ�������Ĵ�С
	private static final int PSOMAXGEN = 50;//��������
	private  ArrayList<Particle> PSO_pop ;//�������Ӷ���Ϊ����Ⱥ�������
	private HashMap<Integer, Integer> mapPso = new HashMap<Integer, Integer>();
	public static int gbest;//�������ӵ����
	public static float pbest;
	public static int[] gbestPos = new int[MGraph.MAX_VERTS];
	public ParticleSwarm(){
		
	}
	public void init(){
		this.PSO_pop = new ArrayList<Particle>(PSO_popsize);
		int n = 0;
    	for(int i = 0;i < MGraph.MAX_VERTS;i++){
    		Particle particle = new Particle(i);
    		
    		particle.pbest = -100;//���ֲ�������Ӧ�ȳ�ʼ��
    	    particle.igmrw(i);
	        particle.rrm();
	        PSO_pop.add(particle);
	        int j;
	        for ( j = 0; j < n; j++) {//����ظ���ɾ��
	                int k;
	            	for (k = 0; k < MGraph.MAX_VERTS; k++) {
	                    if (PSO_pop.get(j).getPos(k) != PSO_pop.get(n).getPos(k)) {
	                        //j = i;
	                        break;
	                    }
	            	}
	                if (k == MGraph.MAX_VERTS) {
	                    //ɾ����һ����
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
    	//�����������ӣ�����Ӧ���������ӵ���Ӧ�Ⱥ�λ�ø���ȫ������
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
   		                map.put(n[j][i], formerValue + 1);    // �����ֳ��ֵĴ�����1
   		            }
   		            else
   		            {
   		                map.put(n[j][i], 1);    // �����ֵ�һ�γ���
   		            }
   			 } 
   		  Collection<Integer> count = map.values();
   	        // �ҳ�map��value�����ֵ��Ҳ���������г����������������ֵĴ���
   	        int maxCount = Collections.max(count);
   	        int maxNumber = 0;
   	        for(Map.Entry<Integer, Integer> entry : map.entrySet())
   	        {
   	            //�õ�valueΪmaxCount��key��Ҳ���������г��ִ�����������
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
    	float pm = (float) 0.1;//ͻ�����
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
    		//ÿ�����ӽ����˶���ֵ����pbest
    		for(int k = 0;k < PSO_pop.size();k++){
    			//����ÿ�����ӵ�λ��
    			
    			int i = PSO_pop.get(k).getT();
    			PSO_pop.get(k).updatePosition();
    			//����ÿ�����ӵľֲ�������Ӧ��
    			PSO_pop.get(k).updatePBest();
    			
    			nbm(i,k);
    			
    		}
    		//����ȫ�����ţ�ע����°�����������ͨ�ŵ�ȫ�ֱ���������
    		
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


 
    