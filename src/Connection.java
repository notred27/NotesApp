package src;
import java.awt.Color;
import java.io.Serializable;

public class Connection implements Serializable{
    Node p;
    Node q;
    int lineWeight = 1;
    Color color;

    public Connection(Node p, Node q) {
        this.p = p;
        this.q = q;
        color = Color.red;
    }

    @Override
    public boolean equals(Object o) {
        Connection c = (Connection)o;

        if(c.p.equals(p) && c.q.equals(q)) {
            return true;
        } else if(c.p.equals(q) && c.q.equals(p)) {
            return true;
        }
        return false;
    }
}
