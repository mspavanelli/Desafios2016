import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Scanner;

public class SCUDBusters {
    public static void main(String[] args) {
        run();

        if ( false ) {
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
    public static void run() {
        ArrayList<Point> pontos = new ArrayList<>();
        pontos.add(new Point(0,0));
        pontos.add(new Point(10,0));
        pontos.add(new Point(10,10));
        pontos.add(new Point(0,10));
        pontos.add(new Point(50,50));
        pontos.add(new Point(60,50));
        System.out.println(Geometry.findPivot(pontos));
        //Polygon p = new Polygon(pontos);
        //System.out.println(p.containsPoint(new Point(2, 1)));
        System.exit(0);
    }
}

class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    double polarAngle(Point pivot) {
        return Geometry.angle(Geometry.projection(this, pivot), pivot, this);
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}

class Polygon {
    ArrayList<Point> vertices;
    Polygon(ArrayList<Point> points) {
        vertices = findMinimumBoundingBox(points); 
    }
    // http://alienryderflex.com/smallest_enclosing_polygon/
    public ArrayList<Point> findMinimumBoundingBox(ArrayList<Point> points) {
        ArrayList<Point> boudingPoints = new ArrayList<>();
        Point pivot = Geometry.findPivot(points);
        // sort points by polar angles (counterclockwise)
        Collections.sort(points, new Comparator<Point>() {
            public int compare(Point a, Point b) {
                return Double.compare(a.polarAngle(pivot), b.polarAngle(pivot));
            }
        });
        // push the points into the stack as you go
        boudingPoints.add(points.remove(0));
        boudingPoints.add(points.remove(1));
        for ( int i = 2; i < points.size(); i++ ) {
            boudingPoints.add(points.remove(i));
            while ( rotaObtusa(boudingPoints) )
                boudingPoints.remove(boudingPoints.size() - 2); // remove o penultimo ponto (ele está dentro do poligono)               
        }
        return boudingPoints;
    }

    public boolean rotaObtusa(ArrayList<Point> points) {
        Point[] last = Geometry.lastThree(points);
            return Geometry.angle(last[0], last[1], last[2]) > 180;
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

// classe com métodos auxiliares de geometria
class Geometry {

    static Point findPivot(ArrayList<Point> points) {
        Point pivot = points.get(0);
        Iterator it = points.iterator();
        while (it.hasNext()) {
            Point atual = (Point) it.next();
            if ( atual.y < pivot.y ) {
                pivot = atual;
            }
            else if ( atual.y == pivot.y ) {
                if ( atual.x < pivot.x ) {
                    pivot = atual;
                }
            }
        }
        return pivot;
    }
    static ArrayList<Point> sortByPolarAngle(ArrayList<Point> points) {
        return null;
    }

    static double angle(Point p1, Point p2, Point p3) {
        double a = distance(p1,p2);
        double b = distance(p2,p3);
        double c = distance(p1,p3);
        return Math.toDegrees(Math.acos((Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b,2)) / -(2 * a * b)));
    }

    static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    static Point[] lastThree(ArrayList<Point> p) {
        int t = p.size();
        if ( t >= 3 )
            return new Point[] {p.get(t - 1), p.get(t - 2), p.get(t - 3)};
        return null;
    }

    // projeção de target em relação a base (eixo x)
    static Point projection(Point target, Point base) {
        return new Point(target.x, base.y);
    }

}