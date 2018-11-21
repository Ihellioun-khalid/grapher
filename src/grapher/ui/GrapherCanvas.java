package grapher.ui;

import static java.lang.Math.*;
import java.util.Vector;
import javafx.util.converter.DoubleStringConverter;
import javafx.application.Application.Parameters;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.canvas.Canvas;

import grapher.fc.*;
/*
 * 
 */
public class GrapherCanvas extends Canvas
{
	/*
	 * Attributes
	 */
	static final double MARGIN = 40;
	static final double STEP = 5;

	static final double WIDTH = 600;
	static final double HEIGHT = 500;
	
	private static DoubleStringConverter d2s = new DoubleStringConverter();
	
	protected double W = WIDTH;
	protected double H = HEIGHT;

	protected double xmin, xmax;
	protected double ymin, ymax;

	protected Vector<Function> functions = new Vector<Function>();
	protected Vector<FunctionStyle> functionstyle = new Vector<FunctionStyle>();
	
	/*
	 * Constructor
	 */
	public GrapherCanvas(Parameters params)
	{
		super(WIDTH, HEIGHT);
		xmin = -PI/2.; xmax = 3*PI/2;
		ymin = -1.5;   ymax = 1.5;
		
		for(String param: params.getRaw())
		{
			Function f = FunctionFactory.createFunction(param);
			FunctionStyle fp = new FunctionStyle( f, this );
			functions.add( f );
			functionstyle.add( fp );
		}
		
		// Interaction avec la souris
		this.addEventHandler( MouseEvent.ANY, new Interaction ( this ) );
		// Scroll
		this.addEventHandler( ScrollEvent.SCROLL, new ScrollHandler( this ) );
	}
	/*
	 * --
	 */
	public Vector<FunctionStyle> getPropertiesList()
	{
		return this.functionstyle;
	}
	/*
	 * --
	 */
	public boolean F_exist( String function)
	{
		for( Function f : functions )
		{
			if( f.toString().equals( function ) )
				return true;
		}
		return false;
	}
	/*
	 * for Print function given by user in to plot graph
	 */
	public void F_print( String function )
	{
		Function f = FunctionFactory.createFunction( function );
		this.functions.add( f );
		this.functionstyle.add( new FunctionStyle( f, this ) );
		this.redraw();
	}
	/*
	 * Fonction pour supprimer une fonction
	 */
	public void F_Delete( FunctionStyle functionStyle )
	{
		Vector<Function> flist = new Vector<Function>();
		Vector<FunctionStyle> fstyle = new Vector<FunctionStyle>();
		for( FunctionStyle fct : this.functionstyle )
		{
			if( !fct.equals( functionStyle ) )
			{
				flist.add( fct.getFunction() );
				fstyle.add( fct );
			}	
		}
		this.functions = flist;
		this.functionstyle = fstyle;
		this.redraw();
	}
	
	/*
	 * Fonction pour r�cup�rer les propri�t�s d'une fonction
	 */
	public FunctionStyle getFunctionProperties( Function function )
	{
		for( FunctionStyle functionstyle : this.functionstyle )
		{
			if( function.equals( functionstyle.getFunction() ) )
				return functionstyle;
		}
		return null;
	}
	/*
	 * --
	 */
	public int getIndexFunction( Function f )
	{
		for( int i = 0; i < functionstyle.size(); i++ )
		{
			if( functionstyle.get( i ).getFunction().equals( f ) )
				return i;
		}
		return -1;
	}	
	/*
	 * Function pour reset les function qui sont en gras
	 */
	public void resetBold()
	{
		for( FunctionStyle fctProp : functionstyle )
		{
			fctProp.setLineWidth( FunctionStyle.DEFAULT );
		}
	}
	/*
	 * Fonction pour ajouter en gras une fonction
	 */
	public void setBold( ObservableList<FunctionStyle> list )
	{
		resetBold();
		for( FunctionStyle s : list )
		{
			s.setLineWidth( FunctionStyle.BOLD );
		}
		this.redraw();
	}
	
	public void F_update( String oldValue, String newValue )
	{
		for( int i = 0; i < functions.size(); i++  )
		{
			Function tmp = functions.get( i );
			if( oldValue.equals( tmp.toString() ) )
			{
				// --
				Function newF = FunctionFactory.createFunction( newValue );
				// --
				functions.set( i, newF );
				// --
				int index = this.getIndexFunction( tmp );
				functionstyle.get( index ).setFunction( newF );
				functionstyle.get( index ).setName( newF.toString() );
			}
		}
		this.redraw();
	}


	
	public void addSplitPanel()
	{
		 SplitPane sp = new SplitPane();
		 sp.getItems().add(new Button("Button One"));
		 sp.getItems().add(new Button("Button Two"));
		 sp.getItems().add(new Button("Button Three"));
		 sp.setDividerPositions(0.3f, 0.6f, 0.9f);
	}
	
	public double minHeight(double width)  { return HEIGHT;}
	public double maxHeight(double width)  { return Double.MAX_VALUE; }
	public double minWidth(double height)  { return WIDTH;}
	public double maxWidth(double height)  { return Double.MAX_VALUE; }

	public boolean isResizable() { return true; }
	public void resize(double width, double height) {
		super.setWidth(width);
		super.setHeight(height);
		redraw();
	}	
	
	public void redraw() {
		GraphicsContext gc = getGraphicsContext2D();
		W = getWidth();
		H = getHeight();
		
		// background
		gc.clearRect(0, 0, W, H);
		
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		
		// box
		gc.save();
		gc.translate(MARGIN, MARGIN);
		W -= 2*MARGIN;
		H -= 2*MARGIN;
		if(W < 0 || H < 0) {
			return;
		}
		
		gc.strokeRect(0, 0, W, H);
		
		gc.fillText("x", W, H+10);
		gc.fillText("y", -10, 0);
		
		gc.beginPath();
		gc.rect(0, 0, W, H);
		gc.closePath();
		gc.clip();

	
		// plot		
		gc.translate(-MARGIN, -MARGIN);

		// x values
		final int N = (int)(W/STEP + 1);
		final double dx = dx(STEP);
		double xs[] = new double[N];
		double Xs[] = new double[N];
		for(int i = 0; i < N; i++) {
			double x = xmin + i*dx;
			xs[i] = x;
			Xs[i] = X(x);
		}

		for(Function f: functions)
		{
			FunctionStyle fp = this.getFunctionProperties( f );
			int lineWidth = fp.getLineWidth();
			String color = fp.getColorName();
			gc.setStroke( Paint.valueOf( color ) );
			gc.setLineWidth( lineWidth );
			// y values
			double Ys[] = new double[N];
			for(int i = 0; i < N; i++) {
				Ys[i] = Y(f.y(xs[i]));
			}
			gc.strokePolyline(Xs, Ys, N);
		}
		gc.setStroke( Color.BLACK );
		gc.setLineWidth( FunctionStyle.DEFAULT );
		gc.restore(); // restoring no clipping
		
		
		// axes
		drawXTick(gc, 0);
		drawYTick(gc, 0);
		
		double xstep = unit((xmax-xmin)/10);
		double ystep = unit((ymax-ymin)/10);

		gc.setLineDashes(new double[]{ 4.f, 4.f });
		for(double x = xstep; x < xmax; x += xstep)  { drawXTick(gc, x); }
		for(double x = -xstep; x > xmin; x -= xstep) { drawXTick(gc, x); }
		for(double y = ystep; y < ymax; y += ystep)  { drawYTick(gc, y); }
		for(double y = -ystep; y > ymin; y -= ystep) { drawYTick(gc, y); }
		
		gc.setLineDashes(null);
	}
	
	protected double dx(double dX) { return  (double)((xmax-xmin)*dX/W); }
	protected double dy(double dY) { return -(double)((ymax-ymin)*dY/H); }

	protected double x(double X) { return xmin+dx(X-MARGIN); }
	protected double y(double Y) { return ymin+dy((Y-MARGIN)-H); }
	
	protected double X(double x) {
		double Xs = (x-xmin)/(xmax-xmin)*W;
		return Xs + MARGIN;
	}
	protected double Y(double y) {
		double Ys = (y-ymin)/(ymax-ymin)*H;
		return (H - Ys) + MARGIN;
	}
		
	protected void drawXTick(GraphicsContext gc, double x) {
		if(x > xmin && x < xmax) {
			final double X0 = X(x);
			gc.strokeLine(X0, MARGIN, X0, H+MARGIN);
			gc.fillText(d2s.toString(x), X0, H+MARGIN+15);
		}
	}
	
	protected void drawYTick(GraphicsContext gc, double y) {
		if(y > ymin && y < ymax) {
			final double Y0 = Y(y);
			gc.strokeLine(0+MARGIN, Y0, W+MARGIN, Y0);
			gc.fillText(d2s.toString(y), 5, Y0);
		}
	}
	
	protected static double unit(double w) {
		double scale = pow(10, floor(log10(w)));
		w /= scale;
		if(w < 2)      { w = 2; }
		else if(w < 5) { w = 5; }
		else           { w = 10; }
		return w * scale;
	}
	
	
	protected void translate(double dX, double dY) {
		double dx = dx(dX);
		double dy = dy(dY);
		xmin -= dx; xmax -= dx;
		ymin -= dy; ymax -= dy;
		redraw();
	}
	
	protected void zoom(Point2D center, double dz) {
		double x = x(center.getX());
		double y = y(center.getY());
		double ds = exp(dz*.01);
		xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
		ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
		redraw();
	}
	
	protected void zoom(Point2D p0, Point2D p1) {
		double x0 = x(p0.getX());
		double y0 = y(p0.getY());
		double x1 = x(p1.getX());
		double y1 = y(p1.getY());
		xmin = min(x0, x1); xmax = max(x0, x1);
		ymin = min(y0, y1); ymax = max(y0, y1);
		redraw();
	}
	protected void dessineRectangleZoom(Point2D p0, Point2D p1) {
		redraw();
		GraphicsContext gc = getGraphicsContext2D();
		
		Rectangle2D zone;
		double x0 = p0.getX();
		double y0 = p0.getY();
		double x1 = p1.getX();
		double y1 = p1.getY();
		
		if (x0 <= x1) {
			if (y0 >= y1) {
				zone = new Rectangle2D(x0, y1, x1 - x0, y0 - y1);
			}
			else {
				zone = new Rectangle2D(x0, y0, x1 - x0, y1 - y0);
			}
		}
		else {
			if (y0 >= y1) {
				zone = new Rectangle2D(x1, y1, x0 - x1, y0 - y1);
			}
			else {
				zone = new Rectangle2D(x1, y0, x0 - x1, y1 - y0);
			}
		}
		
		gc.setStroke(Color.RED);
		gc.setLineDashes(new double[]{ 3.f, 3.f });
		gc.strokeRect(zone.getMinX(), zone.getMinY(), zone.getWidth(), zone.getHeight());
		gc.setLineDashes(null);
	}
}
