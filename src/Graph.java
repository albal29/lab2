import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.DelayQueue;

public class Graph {
    private Map<Integer, ArrayList<Integer>> g = new HashMap<>();
    private String fName;
    private Integer n;

    public Graph(String fName) {
        this.fName = fName;
        readFromFile();
    }

    public void setN(Integer n) {
        this.n = n;
    }


    public void addVertex(Integer v){
        g.put(v,new ArrayList<Integer>());
    }

    public void addEdge(Integer s,Integer d){
        if (!g.containsKey(s))
            addVertex(s);
        if (!g.containsKey(d))
            addVertex(d);
        if(!g.get(s).contains(d)){
            g.get(s).add(d);
        }
        if(!g.get(d).contains(s)){
            g.get(d).add(s);

        }
    }


//    private void readFromFile(){
//        try(BufferedReader br = new BufferedReader(new FileReader(fName))){
//            String line;
//            line = br.readLine();
//            setN(Integer.valueOf(line));
//            Integer i =0,j=0;
//            while((line = br.readLine()) != null){
//                if(!line.isEmpty()) {
//                    j=0;
//                    List<String> attributes = Arrays.asList(line.split(" "));
//                    for(String a: attributes){
//                        if(Integer.valueOf(a)!=0){
//                            addEdge(i,j);
//                        }
//                        j++;
//                    }
//                }
//                i++;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void readFromFile(){
        try(BufferedReader br = new BufferedReader(new FileReader(fName))){
            String line;
            line = br.readLine();
            Integer x =0,y=0,max=0;
            while((line = br.readLine()) != null){
                if(!line.isEmpty()) {
                    List<String> attributes = Arrays.asList(line.split(" "));
                    x = Integer.valueOf(attributes.get(0));
                    y = Integer.valueOf(attributes.get(1));
                    addEdge(x-1,y-1);
                    if(x>max) max = x;
                    if(y>max) max = y;
                }

            } setN(max);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print(){
        for(Integer i : g.keySet()){
            System.out.println(i+":");
            for(Integer j : g.get(i)){
                System.out.println(j+" ");
            }
        }
    }

    public Map<Edge,Double> getEdgesCost(Integer root){
        Map<Edge,Double> costs = new HashMap<>();
        LinkedList<Integer> q = new LinkedList<Integer>();
        ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(n+1,0));
        ArrayList<Integer> cnt = new ArrayList<>(Collections.nCopies(n+1,0));
        ArrayList<Integer> order = new ArrayList<>();
        dist.set(root,1);
        cnt.set(root,1);
        q.push(root);
        ArrayList<Double> val = new ArrayList<Double>(Collections.nCopies(n+1,0.0));

        while(!q.isEmpty()){
            int nod = q.pop();
//            System.out.println(nod);
            order.add(nod);
            int ok = 0;
            if(g.get(nod)==null) g.put(nod,new ArrayList<Integer>());
            for(Integer vecin : g.get(nod)){
//                System.out.println("Vecin:"+vecin);
//                System.out.println("Nod"+vecin+":"+dist.get(vecin)+" "+"Nod"+nod+":"+dist.get(nod));
                if(dist.get(vecin)==0){
                    dist.set(vecin,dist.get(nod)+1);
                    q.add(vecin);
                    cnt.set(vecin,cnt.get(nod));
                    ok = 1;
                }
                else if (dist.get(nod)+1==dist.get(vecin)||dist.get(vecin)+1==dist.get(nod)){
                    cnt.set(vecin,cnt.get(nod)+cnt.get(vecin));}

            }
        }
//       cnt.forEach(System.out::println);
        Collections.reverse(order);
//        order.forEach(System.out::println);
        for(Integer nod : order){
            for(Integer vecin : g.get(nod)){
                if(dist.get(vecin)+1==dist.get(nod)){
                    Integer x,y;
                    if(nod>vecin) {x = vecin; y = nod;}
                        else { x = nod; y = vecin;}
                Double cost = ((1+val.get(nod)))/cnt.get(nod);

                 Edge e = new Edge(x,y);
                 if(costs.containsKey(e)){

                     costs.put(e,costs.get(e)+cost);
                 }
                 else{
                    costs.put(e, cost);}

                 val.set(nod,val.get(nod)+cost);
                }

            }

        }
//        cnt.forEach(System.out::println);
        return costs;
    }

    public Map<Edge,Double> totalCosts(){
        Map<Edge,Double> total = getEdgesCost(0);
        for(int i=1 ; i<n; i++){
            Map<Edge,Double> aux = getEdgesCost(i);
            for(Edge j : aux.keySet()){
                if(total.containsKey(j)){
                total.put(j,total.get(j)+aux.get(j));}
                else{
                    total.put(j,aux.get(j));}
            }
            }

       return total;
    }

    public Double getMax(Map<Edge,Double> costs){
        double max = 111110.0;
        for(Double x : costs.values()){
            if(x < max) max = x;
        }
        return max;
    }

    void DFSUtil(int v, boolean[] visited,
                 ArrayList<Integer> al)
    {
        visited[v] = true;
        al.add(v);
        if(g.get(v)!=null){
       for (Integer vecin : g.get(v)) {
            if (!visited[vecin])
                DFSUtil(vecin, visited, al);
        }}
    }

    public ArrayList<ArrayList<Integer>> DFS()
    {
        ArrayList<ArrayList<Integer>> components = new ArrayList<>();
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            ArrayList<Integer> al = new ArrayList<>();
            if (!visited[i]) {
                DFSUtil(i, visited, al);
                components.add(al);
            }
        }
        return components;
    }


    public void removeVertex(Integer x, Integer y){
        g.get(x).remove(y);
        g.get(y).remove(x);
    }

    int[][] convert()
    {

        // Initialize a matrix
        int [][]matrix = new int[this.n][n];

        for(Integer x : g.keySet()){
            for(Integer aux: g.get(x)){
                matrix[x][aux] = 1;
            }
        }
        return matrix;
    }

    // Function to display adjacency matrix
     void printMatrix(int[][] adj)
    {
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(adj[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    public ArrayList<ArrayList<Integer>> getCommunities(Integer k){
        ArrayList<ArrayList<Integer>> communities = new ArrayList<>();
        communities = DFS();
//        System.out.println(communities.size());
        while(communities.size() < k){
            Map<Edge,Double> costs = totalCosts();
//            System.out.println("done");
            double maxC = getMax(costs);
            for(Edge e : costs.keySet()){
                if(costs.get(e) == maxC){
                    removeVertex(e.getX(),e.getY());
                    break;
                }
//                System.out.println("done");
            }
            communities = DFS();
//            System.out.println("sizeafter:"+communities.size());


        }

        return communities;
    }

}
