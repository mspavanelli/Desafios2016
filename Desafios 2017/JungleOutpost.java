import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

//uva.onlinejudge.org/external/14/1475.pdf

public class JungleOutpost {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (s.hasNext()) {
            int numberOfPoint = s.nextInt();
            ArrayList<Point> points = new ArrayList<>();
            for ( int i = 0; i < numberOfPoint; i++ )
                points.add(new Point(s.nextInt(), s.nextInt()));
            Polygon p = new Polygon(points);
            // System.out.println("AREA: " + p.getArea());
            System.out.println(numberOfPoint - 2);
        }
    }
}

class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}

class Polygon {
    ArrayList<Point> points;
    Polygon(ArrayList<Point> points) {
        this.points = points;
    }
    public int getArea() {
        int total = 0;
        Iterator<Point> it = points.iterator();
        Point atual = it.next();
        while (it.hasNext()) {
            Point prox = it.next();
            total += (atual.x + prox.x) * (atual.y - prox.y);
            atual = prox;
        }
        return Math.abs(total / 2);
    }
}