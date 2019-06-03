/**
 * Created by Lauren on 28/04/2017.
 */

import java.io.*;
import java.util.Scanner;

//Class for krushkals Graph
class KrushkalGraph{
    static int e,v;

    class Edge{
        int source;
        int dest;
        int weight;
    }

    //arrays to hold dsorted and unsorted edges of graph
    Edge unsort[];
    Edge sorted[];

    KrushkalGraph(String filename) throws IOException{
        try {
            //scanner to read in from file
            Scanner sc = new Scanner(new File(filename));

            System.out.println("Reading file...\n");

            int i = 0;

            v = sc.nextInt();
            e = sc.nextInt();

            unsort = new Edge[e];

            for (int j = 0; j < e; j++) {
                unsort[j] = new Edge();
            }

            System.out.println("Edges = " + e);
            System.out.println("Vertices = " + v);
            System.out.println("\n");
            System.out.println("Edge 1 --- Weight --- Edge 2");

            while (sc.hasNextLine()) {

                String[] edges = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                int e1 = sc.nextInt();
                int e2 = sc.nextInt();
                int wgt = sc.nextInt();

                //adds information from txt file to edge array
                unsort[i].source = e1;
                unsort[i].dest = e2;
                unsort[i].weight = wgt;

                i++;

                System.out.println("   " + edges[e1] + " ------- " + wgt + " -------- " + edges[e2]);
            }
            System.out.println("\n");

            sc.close();

            sorted = sort(unsort);
            kruskal_MST(sorted);
        }
        catch(Exception e){
            System.out.println("Exception thrown:" + e);
        }
    }

    //sorting function
    Edge[] sort(Edge[] unsorted) {
        Edge temp;

        for(int i =0; i < (unsorted.length);i++){
            for(int j=0; j <(unsorted.length);j++) {
                if (unsorted[i].weight <= unsorted[j].weight){
                    temp = unsorted[i];
                    unsorted[i] = unsorted[j];
                    unsorted[j] = temp;
                }
            }
        }

        return unsorted;
    }

    void kruskal_MST(Edge[] sorted){
        Union u = new Union(v+1);

        int weightsum =0;

        Edge[] mst = new Edge[v-1];

        int j = 0;
        int i = 0;

        while(j<(v-1)){
            Edge temp = sorted[i++];
            int x = u.find(temp.source);
            int y = u.find(temp.dest);
            if(x!=y){
                mst[j++] = temp;
                u.union(x,y);
                weightsum += temp.weight;
            }

            System.out.println("Current Weight: " + weightsum);
            u.showSets();
        }
    }
}

//Union class to create sets and parent arrays
class Union{

    int[] parent;
    int N;

    //String array of letters instead of using toChar function
    String[] alpbt = {"","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    Union(int n){
        parent = new int[n];
        N=n;
        for(int i =0;i<n;i++){
            this.parent[i] = i;
        }
    }

    int find(int i){
        if(parent[i] == i){
            return i;
        }
        else{
            return find(parent[i]);
        }
    }

    void union(int x, int y){
        int xroot = find(x);
        int yroot = find(y);

        parent[xroot] = yroot;
    }

    //shows sets for MST graph
    void showSets(){
        int u,root;
        int[] shown = new int[N+1];
        for(u=1;u<N;++u){
            root = find(u);
            if(shown[root] != 1){
                showSet(root);
                shown[root]=1;
            }
        }
        System.out.println("\n");
    }

    private void showSet(int root) {
        int v;
        System.out.print("Set{");
        for (v = 1; v <N; ++v) {
            if (find(v) == root) {
                System.out.print(alpbt[v] + " ");
            }
        }
        System.out.print("}  ");
    }

}

//Heap class for Primms algorithm
class Heap
{
    private int[] h;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size

    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos)
    {
        N = 0;
        h = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty()
    {
        return N == 0;
    }


    public void siftUp( int k)
    {
        int v = h[k];

        h[0] = 0;
        dist[0] = Integer.MIN_VALUE;

        while( dist[v] < hPos[h[k]] ) {
            h[k] = h[k/2];

            k = k/2;
        }
        h[k] = v;
        hPos[v] = k;

    }


    public void siftDown( int k)
    {
        int v, j;

        v = h[k];

        while( k <= N/2) {
            j = 2 * k;
            if(j < N && dist[h[j]] < dist[h[j+1]]){
                ++j;
            }
            if( dist[v] >= dist[h[j]]){
                break;
            }
            h[k] = h[j];
            hPos[h[k]] = k;

            k = j;
        }
        h[k] = v;
        hPos[v] = k;
    }


    public void insert( int x)
    {
        h[++N] = x;
        siftUp( N);
    }


    public int remove()
    {
        int v = h[1];
        hPos[v] = 0; // v is no longer in heap
        h[N+1] = 0;  // put null node into empty spot

        h[1] = h[N--];
        siftDown(1);

        return v;
    }

}

class PrimmsGraph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;


    // default constructor
    public PrimmsGraph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        //read from file
        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        System.out.println("Reading file...\n");

        String splits = " +";  // multiple whitespace as delimiter
        String line = reader.readLine();
        String[] parts = line.split(splits);
        System.out.println("Edges = " + parts[0]);
        System.out.println("Vertices = " + parts[1]);
        System.out.println("\n");
        System.out.println("Edge 1 --- Weight --- Edge 2");

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        // create sentinel node
        z = new Node();
        z.next = z;

        // create adjacency lists, initialised to sentinel node z
        adj = new Node[V+1];
        for(v = 1; v <= V; ++v)
            adj[v] = z;

        // read the edges
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]);
            wgt = Integer.parseInt(parts[2]);

            System.out.println("   " + toChar(u) + " ------- " + wgt + " -------- " + toChar(v));

            //create two new nodes for ints u and v and add corresponding values into nodes and then add node to adjacency matrix
            Node uadj = new Node();
            uadj.vert=u;
            uadj.wgt=wgt;
            uadj.next=adj[v];
            adj[v]=uadj;

            Node vadj = new Node();
            vadj.vert=v;
            vadj.wgt=wgt;
            vadj.next=adj[u];
            adj[u]=vadj;

        }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {
        return (char)(u + 64);
    }

    // method to display the graph representation
    public void display() {
        int v;
        Node n;

        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next)
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
        }
        System.out.println("");
    }



    public void MST_Prim(int s)
    {
        int v;
        int wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //initialise variables
        dist =new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];

        //adding values to variables
        for(v=0;v<V;v++){
            dist[v]=Integer.MAX_VALUE;
            parent[v]=0;
            hPos[v]=0;
        }

        //set the source node as first priority
        dist[s] = 0;

        Heap pq =  new Heap(V, dist, hPos);
        pq.insert(s);

        while (pq.isEmpty()==false)
        {
            v = pq.remove();
            dist[v] = -dist[v];

            for(t=adj[v];t!=z;t=t.next){
                if(t.wgt<dist[t.vert]){
                    dist[t.vert] = t.wgt;
                    parent[t.vert] =v;
                    //if vertice doesn't exist in hPos, insert into heap
                    if(hPos[t.vert]==0){
                        pq.insert(t.vert);
                        wgt_sum+=t.wgt;
                    }
                    else{
                        pq.siftUp(hPos[t.vert]);
                    }
                }
            }
        }
        System.out.print("\nWeight of MST = " + wgt_sum + "\n");

        mst = parent;

        showMST();
    }

    public void showMST()
    {
        System.out.print("\nMinimum Spanning tree parent array is:\n");
        for(int v = 1; v <= V; ++v)
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        System.out.println("");
    }
}

public class Assignment {

    public static void main(String[] args) throws IOException {
        String fname = "myGraph.txt";

        System.out.println("Krushkals Alogorithm\n");
        KrushkalGraph kg = new KrushkalGraph(fname);

        System.out.println("---------------------------------------------------------------------\n");
        System.out.println("Primms Alogorithm\n");
        PrimmsGraph pg = new PrimmsGraph(fname);

        pg.display();

        pg.MST_Prim(1);
    }
}

