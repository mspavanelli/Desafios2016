import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Scanner;

public class SCUDBusters {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        List<Point> missiles = new ArrayList<>();
        List<Polygon> kingdoms = new ArrayList<>();
        while (s.hasNext()) {
            int numberOfPoints = s.nextInt();
            if ( numberOfPoints == -1 ) {
                while (s.hasNext())
                    missiles.add(new Point(s.nextInt(), s.nextInt()));
            }
            else {
                ArrayList<Point> points = new ArrayList<>();
                for ( int i = 0; i < numberOfPoints; i++ )
                    points.add(new Point(s.nextInt(), s.nextInt()));
                kingdoms.add(new Polygon(points));
            }
        }
        double area = 0;
        Iterator<Polygon> itPolygon = kingdoms.iterator();
        while ( itPolygon.hasNext() ) {
            Iterator<Point> itMissile = missiles.iterator();
            while ( itMissile.hasNext() ) {
                Polygon poligonoAtual = itPolygon.next();
                if ( poligonoAtual.containsPoint(itMissile.next()) )
                    area += poligonoAtual.getArea();
            }
        }
        System.out.println(area);
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
    List<Point> vertices;
    Polygon(List<Point> points) {
        vertices = findMinimumBoundingBox(points); 
    }
    public ArrayList<Point> findMinimumBoundingBox(List<Point> points) {
        return null;
    }
    // http://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon 
    public boolean containsPoint(Point p) {
        boolean result = false;
        int i, j;
        for ( i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++ ) {
            if ( (vertices.get(i).y > p.y) != (vertices.get(j).y > p.y) &&
             (p.x < (vertices.get(j).x - vertices.get(i).x) * (p.y - vertices.get(i).y / (vertices.get(j).y - vertices.get(i).y) + vertices.get(i).x))) {
                result = !result;
            }
        }
        return result;
    }

    public double getArea() {
        double total = 0;
        Iterator<Point> it = vertices.iterator();
        Point atual = it.next();
        while (it.hasNext()) {
            Point prox = it.next();
            total += (atual.x + prox.x) * (atual.y - prox.y);
            atual = prox;
        }
        return Math.abs(total / 2);
    }
}