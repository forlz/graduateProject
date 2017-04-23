package pso;

public class Start {
	public static void main(String[] args){
		MGraph g = new MGraph();
		String filePath = "F:\\karate.txt";
	    g.readTxtFile(filePath);
	/*	LinJie linJie = new LinJie(g);
		linJie.initLinJie1();*/
		ParticleSwarm swarm = new ParticleSwarm();
		swarm.init();
		swarm.search();
	}
}
