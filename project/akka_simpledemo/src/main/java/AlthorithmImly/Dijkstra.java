package AlthorithmImly;


import java.lang.reflect.Array;

public class Dijkstra {
    private int mEdgeNum;//边数量
    private char [] mVertex; //顶点集合
    private int [][] matrix; //领接矩阵
    private  static final int INTMax =Integer.MAX_VALUE;

    public void DijkstraPath(int source,int [] prev,int [] dist){
        //成功获取source到第i个顶点的最短路径为true
        int mVnum = mVertex.length;
        boolean[] flag =new boolean[mVnum];
        //init

        for(int i=0;i<mVnum;i++){
            flag[i] = false ;
            prev[i] = 0;
            dist[i] = matrix[source][i];//最短路径初始化时应该是source到i权值
        }
        //对源节点进行初始化
        flag[source] =true;
        dist[source] = 0;

        //遍历顶点数-1次每次找到其中一个，不一定按顺序，顶点的最短路径
        int temp=0;
        for(int i=1;i<mVnum;i++){
            //目的是获取在未进行处理计算的集合中找到距离source最近的点temp
            int min =INTMax;
            for(int j=0;j<mVnum;j++){
                //找其他未能确定最短路径的点来和当前确定的min来进行比较来增长一条最短的边
                if(flag[j]==false&&dist[j]<min){
                    min = dist[j];
                    temp =j;
                }
            }
            flag[temp] = true;
            //修正最短路径和prev定点
            //k确定了之后需要将k作为未能获得最短路径节点的前驱节点
            for(int j =0;j<mVnum;j++){
                int tmp =(matrix[temp][j]==INTMax ? INTMax:(min+matrix[temp][j]));
                if(flag[j]==false&&(tmp<dist[j])){
                    dist[j] =tmp;
                    prev[j] =temp;
                }
            }
        }
        System.out.printf("DijkstraPath(%c):\n",mVertex[source]);
        for(int i =0 ;i<mVnum;i++){
            System.out.printf("shortest(%c,%c)=%d\n",mVertex[source],mVertex[i],dist[i]);
        }
    }

}

