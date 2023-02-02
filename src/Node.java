package src;
import java.io.Serializable;

public class Node implements Serializable{
    int xPos;
    int yPos;
    int size;
    int shape = 0; // 0 = circle, 1 = square, 2 = uptriangle
    String title = "";
    String text = "";

    public Node(int xPos, int yPos, int size, int shape) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.shape = shape;
    }

    public Node(int xPos, int yPos, int size, int shape, String title) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.shape = shape;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }

        Node n = (Node)o;
        return n.xPos == xPos && n.yPos == yPos && n.shape == shape;
    }

}
