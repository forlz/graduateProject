package pso;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Integer;
public class MGraph {
	public class Vertex {
	    //椤剁偣绫�
	    //public char lab;
	    public boolean wasVisited;
	    public char label;
	    public Vertex(char lab){
	        label = lab;
	        wasVisited = false;
	    }
	}

	static int MAX_VERTS = 34;
    Vertex[] vertexList;
    private static int[][] adjMat = new int[MAX_VERTS][MAX_VERTS];
    static int nVerts;
    //private static int[][] neighbor = new int[MAX_VERTS][MAX_VERTS];
    public static int getadjMat(int i,int j){
    	return adjMat[i][j];
    }
    public  static int getVertexDegree(int i){
        int n = 0;
        for(int j = 0 ; j < MAX_VERTS ; j++) {
            if (adjMat[i][j] != 0) n++;
        }
        return n;
    }
    public void getNVerts(){
    	
    }
    public void addEdge(int start, int end) {
        adjMat[start][end] = 1;
        adjMat[end][start] = 1;
        nVerts++;
    }
    public void readTxtFile(String filePath){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    //读入到需要的地方
                   // System.out.println(lineTxt);
                    String pattern = "(\\D*)(\\d+)(.*)";
                    // 创建 Pattern 对象
                    Pattern r = Pattern.compile(pattern);
                  
                    // 现在创建 matcher 对象
                    Matcher m = r.matcher(lineTxt);
                    if (m.find( )) {
                        int x = Integer.parseInt(m.group(2));
                        String t = m.group(3).trim();
                        int y = Integer.parseInt(t);
                        addEdge(x-1,y-1);
                     } else {
                        System.out.println("NO MATCH");
                     }
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }
}
