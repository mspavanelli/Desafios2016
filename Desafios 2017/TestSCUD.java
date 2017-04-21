import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class TestSCUD {

    public static void main(String[] args) {
        run();
        // System.out.println(new Point(-2,3).polarAngle(new Point(0,1)));
        // System.out.println(Math.atan(1));
        // Point pivot = new Point(3,3);
        // Point p = new Point(5,6);
        // System.out.println(Math.toDegrees(Math.atan(p.y - pivot.y / p.x - pivot.x)));
        // System.out.println(Math.toDegrees(Math.atan(p.y - p.x)));
    }
    public static void run() {
        ArrayList<Point> pontos = new ArrayList<>();
        pontos.add(new Point(3,3));
        pontos.add(new Point(4,6));
        pontos.add(new Point(4,11));
        pontos.add(new Point(4,8));
        pontos.add(new Point(10,6));
        pontos.add(new Point(5,7));
        pontos.add(new Point(6,6));
        pontos.add(new Point(6,3));
        pontos.add(new Point(7,9));
        pontos.add(new Point(10,4));
        pontos.add(new Point(10,9));
        pontos.add(new Point(1,7));
        pontos.add(new Point(3,3));
        Polygon p1 = new Polygon(pontos);
        System.out.println(p1);
        System.out.println(p1.getArea());

        // ArrayList<Point> p2p = new ArrayList<>();
        // p2p.add(new Point(10,10));
        // p2p.add(new Point(21,10));
        // p2p.add(new Point(21,13));
        // Polygon p2 = new Polygon(p2p);
        // Point missel = new Point(10,10);
        // System.out.println(p2.getArea());
        // System.out.println(p2.containsPoint(missel));

        // System.out.printf("%s -> %s -> %s: %b\n", pontos.get(0), pontos.get(1), pontos.get(2), Geometry.angle(pontos.get(0), pontos.get(1), pontos.get(2)) > 180);
        // System.out.printf("%s -> %s -> %s: %b\n", pontos.get(5), pontos.get(4), pontos.get(3), Geometry.angle(pontos.get(5), pontos.get(4), pontos.get(3)) > 180);
        // System.out.println(p.containsPoint(new Point(2, 1)));
    }
}