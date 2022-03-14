import java.util.Objects;

public class Edge {
    Integer x;
    Integer y;
    int hashcode;
    public Edge(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.hashcode = Objects.hash(x,y);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return x == edge.x && y == edge.y;
    }

    

    @Override
    public int hashCode() {
        return x*y/hashcode;
    }
}
