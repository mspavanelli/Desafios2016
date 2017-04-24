import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
                if ( poligonoAtual.containsPoint(itMissile.next()) && !poligonoAtual.bombardeado ) {
                    double a = poligonoAtual.getArea();
                    poligonoAtual.bombardeado = true;
                    area += a;
                }
            }
        }
        System.out.printf(String.format(Locale.US, "%.2f\n", area));
    }
}

class Point {
    double x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean equalTo(Point other) {
        return this.x == other.x && this.y == other.y;
    }

    double polarAngle(Point pivot) {
        if ( this.y == pivot.y )
            return 0;
        if ( this.x == pivot.x )
            return 90;
        if ( this.x < pivot.x )
            return 180 - Geometry.angle(Geometry.projection(this, pivot), pivot, this);    
        return Geometry.angle(Geometry.projection(this, pivot), pivot, this);
    }

    public String toString() {
        return String.format("(%.0f,%.0f)", x, y);
    }
}

class Polygon {
    ArrayList<Point> vertices;
    boolean bombardeado = false;
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
        // if ( !boudingPoints.contains(pivot))
            // boudingPoints.add(0, pivot);
        return boudingPoints;
    }

    public boolean containsPoint(Point p) {
        // busca valores extremos
        // int minX = (int) vertices.get(0).x;
        // int maxX = (int) minX;
        // int minY = (int) vertices.get(0).y;
        // int maxY = (int) minY;
        // Iterator it = vertices.iterator();
        // while ( it.hasNext() ) {
        //     Point atual = (Point) it.next();
        //     if ( atual.x < minX ) minX = (int) atual.x;
        //     if ( atual.x > maxX ) maxX = (int) atual.x;
        //     if ( atual.y < minY ) minY = (int) atual.y;
        //     if ( atual.y > maxY ) maxY = (int) atual.y;
        // }
        // if ( p.x < minX || p.x > maxX || p.y < minY || p.y > maxY )
        //     return false;

        if ( wn(p) )
            return true;
        else return pointOnEdge(p);
    }

    // retorna verdadeiro se o ponto estiver dentro dos limites do polígono
    public boolean wn(Point p) {
        int windingNumber = 0;
        vertices.add(vertices.get(0));
        for ( int i = 0; i < vertices.size() - 1; i++ ) {
            if ( vertices.get(i).y < p.y ) {
                if ( vertices.get(i+1).y >= p.y ) {
                    if ( Geometry.counterClockwiseTurn(vertices.get(i), vertices.get(i+1), p) ) {
                        windingNumber++;
                    }
                }
            }
            else {
                if ( vertices.get(i+1).y < p.y ) {
                    if ( !Geometry.counterClockwiseTurn(vertices.get(i), vertices.get(i+1), p) ) {
                        windingNumber--;
                    }
                }
            }
        }

        vertices.remove(vertices.size() - 1);
        return windingNumber != 0;
    }

    public boolean pointOnEdge(Point p) {
        // para cada aresta -> calcular distancia p para aresta (segmento de reta)
        // se distancia for zero, retorna verdadeiro
        vertices.add(vertices.get(0));
        for ( int i = 0; i < vertices.size() - 1; i++ ) {
            if ( Geometry.distancePointToEdge(p, vertices.get(i), vertices.get(i+1)) == 0 )
                return true;
        }
        vertices.remove(vertices.size() - 1);
        return false;
    }

    public boolean rotaObtusa(ArrayList<Point> points) {
        int tamanho = points.size();
        if ( tamanho < 3 ) return false;
        return Geometry.counterClockwiseTurn(points.get(tamanho-1), points.get(tamanho-2), points.get(tamanho-3));
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

class Vector {
    Point p;
    Vector(Point p1, Point p2) {
        p = new Point(p2.x - p1.x, p2.y - p1.y);
    }

    public double dotProduct(Vector v) {
        double ax = p.x;
        double bx = v.p.x;
        double ay = p.y;
        double by = v.p.y;
        return ax * bx + ay * by;
    }
    public String toString() {
        return String.format("(%.1f,%.1f)", p.x, p.y);
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

    static double distancePointToEdge(Point p, Point e1, Point e2) {
        Vector v = new Vector(e1,e2);
        Vector w = new Vector(e1,p);
        double c1 = v.dotProduct(w);
        if ( c1 <= 0 )
            return Geometry.distance(p, e1);
        
        double c2 = v.dotProduct(v);
        if ( c2 <= c1 )
            return Geometry.distance(p, e2);

        double b = c1 / c2;
        Point bv = new Point(v.p.x * b, v.p.y * b);
        Point pb = new Point(e1.x + bv.x, e1.y + bv.y);
        return Geometry.distance(p, pb);
    }

    // projeção de target em relação a base (eixo x)
    static Point projection(Point target, Point base) {
        return new Point(target.x, base.y);
    }

}