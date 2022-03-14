import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph("football.txt");
//        g.print();

//        Map<Edge,Double> costs1 = g.totalCosts();
//        for(Edge x : costs1.keySet()) {
//            System.out.println(x.getX() + " " + x.getY() + ":" + costs1.get(x));}
//            System.out.println(x.getX()+" "+x.getY());
//        }
//        int[][] m = g.convert();
//        g.printMatrix(m);
        ArrayList<ArrayList<Integer>> l = g.getCommunities(12);
        for(List<Integer> x : l ){
            System.out.println("Comunitate");
            for(Integer aux : x){
                System.out.println(aux+1);
            }
        }
    }

}
