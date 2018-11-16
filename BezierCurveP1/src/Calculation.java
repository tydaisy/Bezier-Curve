import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Calculation {

    private Point2D[] points;
    private Point2D[] midPoints;
    private Point2D[] normal;
    ArrayList<Double> dotPro = new ArrayList();


    public Calculation(Point2D[] points, double t) {
        this.points = points;
        midPoints = new Point2D[(int) (Math.ceil(1 / t))];
        normal = new Point2D[(int) (Math.ceil(1 / t))];
    }


    public void calNormal() {
        double x, y, a, b;
        for (int i = 1; i < points.length-1; i++) {
            System.out.println(points[i]);
            x = points[i - 1].getX() + (points[i].getX() - points[i - 1].getX()) / 2;
            y = points[i - 1].getY() + (points[i].getY() - points[i - 1].getY()) / 2;
            
            midPoints[i - 1] = new Point2D.Double(x, y);
            a = -(points[i - 1].getY() - y) + x;
            b = points[i - 1].getX() - x + y;
            normal[i - 1] = new Point2D.Double(a, b);
        }
    }


    public void dotProduct(Point2D light) {
        double magnitude1;
        double magnitude2;
        double x, y, a, b;

        for (int i = 0; i < normal.length-1; i++) {
            magnitude1 = Math.sqrt(Math.pow(normal[i].getX() - midPoints[i].getX(), 2) + Math.pow(normal[i].getY() - midPoints[i].getY(), 2));
            x = ((normal[i].getX() - midPoints[i].getX()) / magnitude1) + midPoints[i].getX();
            y = ((normal[i].getY() - midPoints[i].getY()) / magnitude1) + midPoints[i].getY();

            magnitude2 = Math.sqrt(Math.pow(light.getX() - midPoints[i].getX(), 2) + Math.pow(light.getY() - midPoints[i].getY(), 2));
            a = ((light.getX() - midPoints[i].getX()) / magnitude2) + midPoints[i].getX();
            b = ((light.getY() - midPoints[i].getY()) / magnitude2) + midPoints[i].getY();

            dotPro.add((x - midPoints[i].getX()) * (a - midPoints[i].getX()) + (y - midPoints[i].getY()) * (b - midPoints[i].getY()));
        }
    }


    public Point2D[] getNormal() {
        return normal;
    }


    public Point2D[] getMidPoints() {
        return midPoints;
    }


    public ArrayList<Double> getDotProduct() {
        return dotPro;
    }

}
