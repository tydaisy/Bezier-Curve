import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BezierCurve extends JPanel implements MouseListener, MouseMotionListener {
	// private static final long serialVersionUID = 1L;
	private Point2D[] controlPoint;
	private Ellipse2D.Double[] dot;
	private static final double diameter = 10;
	private int numPoints;
	private double t = 1.0/41;
	public void setT(double t) {
        this.t = t;
    }

    private Point2D[] points;
	private Calculation cal;

	public BezierCurve() {
		numPoints = 0;
		controlPoint = new Point2D[5];
		dot = new Ellipse2D.Double[5];
		this.setBackground(Color.PINK);
		this.addMouseListener(this);
	}

	// @Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.BLUE);

		if (numPoints < 5) {
			for (int i = 0; i < numPoints; i++) {
				g.fillOval((int) dot[i].getX(), (int) dot[i].getY(), (int) diameter, (int) diameter);

				if (i < (numPoints - 1)) 
					g.drawLine((int) controlPoint[i].getX(), (int) controlPoint[i].getY(),
							(int) controlPoint[i + 1].getX(), (int) controlPoint[i + 1].getY());
			}
		}
		if (numPoints >= 5) {
			for (int i = 0; i < numPoints - 1; i++) {
				g.fillOval((int) dot[i].getX(), (int) dot[i].getY(), (int) diameter, (int) diameter);

				if (i < (numPoints - 2)) 
					g.drawLine((int) controlPoint[i].getX(), (int) controlPoint[i].getY(),
							(int) controlPoint[i + 1].getX(), (int) controlPoint[i + 1].getY());
			}
			g.setColor(Color.WHITE);
			g.fillOval((int) dot[4].getX(), (int) dot[4].getY(), (int) diameter, (int) diameter);
			cal.dotProduct(controlPoint[4]);
			lighten(cal.getDotProduct(), cal.getMidPoints(), g);

			System.out.println("dotPro: " + cal.getDotProduct());
		}

		double x, y;
		if (numPoints == 3) {

			g.setColor(Color.BLACK);
			int priorx = 0;
			int priory = 0;
			for (double k = 0; k <= 1; k += t) {
				x = (1 - k) * (1 - k) * controlPoint[0].getX() + 2 * k * (1 - k) * controlPoint[1].getX()
						+ k * k * controlPoint[2].getX();
				y = (1 - k) * (1 - k) * controlPoint[0].getY() + 2 * k * (1 - k) * controlPoint[1].getY()
						+ k * k * controlPoint[2].getY();
				
				if (k == 0) {
					g.drawLine((int) x, (int) y, (int) x, (int) y);
				} else {
					if (k <= 1 - t) {
						g.fillOval((int) x - 5, (int) y - 5, 10, 10);
					}
					g.drawLine((int) x, (int) y, priorx, priory);
				}
				if (k >= 1 - t) {
					g.drawLine((int) controlPoint[2].getX(), (int) controlPoint[2].getY(), (int) x, (int) y);
				}
				priorx = (int) x;
				priory = (int) y;
			}

		}

		if (numPoints == 4 | numPoints == 5) {
			points = new Point2D[(int) (Math.ceil(1 / t) + 1)];
			g.setColor(Color.BLACK);
			int priorx = 0;
			int priory = 0;
			int i = 0;
			for (double k = 0; k <= 1; k += t) {
				x = (1 - k) * (1 - k) * (1 - k) * controlPoint[0].getX()
						+ 3 * k * (1 - k) * (1 - k) * controlPoint[1].getX()
						+ 3 * k * k * (1 - k) * controlPoint[2].getX() + k * k * k * controlPoint[3].getX();
				y = (1 - k) * (1 - k) * (1 - k) * controlPoint[0].getY()
						+ 3 * k * (1 - k) * (1 - k) * controlPoint[1].getY()
						+ 3 * k * k * (1 - k) * controlPoint[2].getY() + k * k * k * controlPoint[3].getY();				// g2.setStroke(new BasicStroke(3.0f));

				if (k == 0) {
					g.drawLine((int) x, (int) y, (int) x, (int) y);
				} else {
					if (k <= 1 - t) {
						g.fillOval((int) x - 5, (int) y - 5, 10, 10);
					}
					g.drawLine((int) x, (int) y, priorx, priory);
				}
				if (k >= 1 - t) {
					g.drawLine((int) controlPoint[3].getX(), (int) controlPoint[3].getY(), (int) x, (int) y);
				}

				priorx = (int) x;
				priory = (int) y;

				points[i] = new Point2D.Double(x, y);
				System.out.println(i);
				i++;
			}

			cal = new Calculation(points, t);
			cal.calNormal();
			Point2D[] midpoints = cal.getMidPoints();
			Point2D[] normal = cal.getNormal();

			for (int j = 0; j < midpoints.length-1; j++) {
				g.setColor(Color.GREEN);
				g.fillOval((int) midpoints[j].getX() - 3, (int) midpoints[j].getY() - 3, 6, 6);
				g.setColor(Color.RED);
				g.fillOval((int) normal[j].getX() - 5, (int) normal[j].getY() - 5, 10, 10);
				g.drawLine((int) midpoints[j].getX(), (int) midpoints[j].getY(), (int) normal[j].getX(),
						(int) normal[j].getY());

			}

		}
	}

	public void lighten(ArrayList<Double> dotPro, Point2D[] points, Graphics g) {
		for (int i = 0; i < dotPro.size(); i++) {
			if (lineIntersect(i, g) == false && dotPro.get(i) >= 0 && dotPro.get(i) <= 1) {
				int greySc = (int) (dotPro.get(i) * 255);
				g.setColor(new Color(greySc, greySc, greySc));
				g.fillOval((int) points[i].getX()-10, (int) points[i].getY()-10, 20, 20);
			}else {
				g.setColor(Color.BLACK);
				g.fillOval((int) points[i].getX()-10, (int) points[i].getY()-10, 20, 20);
			}
		}
	}
	
	public boolean lineIntersect(int dotProIndex, Graphics g) {
	    boolean intersect = false;
	    int dpi = dotProIndex;
	    Point2D[] midpoints = cal.getMidPoints();
        Point2D[] normal = cal.getNormal();
        Point2D light = controlPoint[4];
        
        for (int i = 0; i < midpoints.length-2; i++) {
            if (i != dpi && i != dpi-1) {
                if (Line2D.linesIntersect((int)midpoints[dpi].getX(),(int)midpoints[dpi].getY(),(int)light.getX(), (int)light.getY(), (int)midpoints[i].getX(),(int)midpoints[i].getY(), (int)midpoints[i+1].getX(),(int)midpoints[i+1].getY()))
                    intersect = true;
                g.setColor(Color.YELLOW);
                g.drawLine((int)midpoints[dpi].getX(),(int)midpoints[dpi].getY(),(int)light.getX(), (int)light.getY());
            }
        }
       
        if (dpi != 0 && Line2D.linesIntersect((int)midpoints[dpi].getX(),(int)midpoints[dpi].getY(),(int)light.getX(), (int)light.getY(), (int)midpoints[0].getX(),(int)midpoints[0].getY(), (int)controlPoint[0].getX(),(int)controlPoint[0].getY()))
            intersect = true;
        g.drawLine((int)midpoints[dpi].getX(),(int)midpoints[dpi].getY(),(int)light.getX(), (int)light.getY());
       
        if (dpi != midpoints.length-2 && Line2D.linesIntersect((int)midpoints[dpi].getX(),(int)midpoints[dpi].getY(),(int)light.getX(), (int)light.getY(), (int)midpoints[midpoints.length-2].getX(),(int)midpoints[midpoints.length-2].getY(), (int)controlPoint[3].getX(),(int)controlPoint[3].getY()))
            intersect = true;
        g.drawLine((int)midpoints[dpi].getX(),(int)midpoints[dpi].getY(),(int)light.getX(), (int)light.getY());

	    return intersect;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (numPoints < 5) {
			double x = e.getX();
			double y = e.getY();
			controlPoint[numPoints] = new Point2D.Double(x, y);
			dot[numPoints] = new Ellipse2D.Double(x - diameter / 2, y - diameter / 2, diameter, diameter);
			numPoints++;
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
