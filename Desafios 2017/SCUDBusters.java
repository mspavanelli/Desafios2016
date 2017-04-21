import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Comparator;
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
            Polygon poligonoAtual = itPolygon.next();
            while ( itMissile.hasNext() ) {
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

    double polarAngle(Point pivot) {
        if ( this.x == pivot.x && this.y == pivot.y )
            return 0;
        if ( this.x < pivot.x )
            return 180 - Geometry.angle(Geometry.projection(this, pivot), pivot, this);    
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
    public ArrayList<Point> findMinimumBoundingBox(ArrayList<Point> points) {
        ArrayList<Point> boudingPoints = new ArrayList<>();
        // determina ponto pivô
        Point pivot = Geometry.findPivot(points);
        // ordena pontos pelo ângula polar (sentido anti-horário)
        Collections.sort(points, new Comparator<Point>() {
            public int compare(Point a, Point b) {
                return Double.compare(a.polarAngle(pivot), b.polarAngle(pivot));
            }
        });

        // 1) Insere os pontos ordenados na lista de vértices
        // 2) Após cada inserção medir angulo formado pelos 3 últimos
        // 3) Caso ângulo seja maior que 180, remover penultimo vertice
        // 4) Repetir 3 até que seja falso

        while ( points.size() > 0 ) {
            boudingPoints.add(points.remove(0));
            while (rotaObtusa(boudingPoints) )
                boudingPoints.remove(boudingPoints.size() - 2);
        }
        return boudingPoints;
    }

    public boolean rotaObtusa(ArrayList<Point> points) {
        int tamanho = points.size();
        if ( tamanho < 3 ) return false;
        return Geometry.counterClockwiseTurn(points.get(tamanho-1), points.get(tamanho-2), points.get(tamanho-3));
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
        vertices.add(vertices.get(0));
        double total = 0;
        Iterator<Point> it = vertices.iterator();
        Point atual = it.next();
        while (it.hasNext()) {
            Point prox = it.next();
            total += (atual.x + prox.x) * (atual.y - prox.y);
            atual = prox;
        }
        vertices.remove(vertices.size() - 1);
        return Math.abs(total / 2);
    }

    public String toString() {
        return this.vertices.toString();
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

    static double angle(Point p1, Point p2, Point p3) {
        double a = distance(p1,p2);
        double b = distance(p2,p3);
        double c = distance(p1,p3);
        return Math.toDegrees(Math.acos((Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b,2)) / -(2 * a * b)));
    }

    static boolean counterClockwiseTurn(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) > (p2.y - p1.y) * (p3.x - p1.x);
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