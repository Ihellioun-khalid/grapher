package grapher.ui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class Interaction implements EventHandler<MouseEvent>{
	private enum State {IDLE, WAITING_G, WAITING_D, DRAGGING_G, DRAGGING_D};
	private State state = State.IDLE;
	private Point2D p;
	private GrapherCanvas grapher;

	private static final int DEPLACEMENT_MIN = 5;
	private static final int ZOOM_FACTOR = 5;

	public Interaction(GrapherCanvas g) {
		this.grapher = g;
	}

	@Override
	public void handle(MouseEvent e) {
		switch(state) {
		case IDLE:
			switch(e.getEventType().getName()) {
			case "MOUSE_PRESSED":
				switch(e.getButton().name()) {
				case "PRIMARY":
					p = new Point2D(e.getX(), e.getY());
					state = State.WAITING_G;
					break;
				case "SECONDARY":
					p = new Point2D(e.getX(), e.getY());
					state = State.WAITING_D;
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			break;
		case WAITING_G:
			switch(e.getEventType().getName()) {
			case "MOUSE_RELEASED":
				switch(e.getButton().name()) {
				case "PRIMARY":
					grapher.zoom(p, ZOOM_FACTOR);
					state = State.IDLE;
					break;
				default:
					break;
				}
				break;
			case "MOUSE_DRAGGED":
				Point2D position = new Point2D(e.getX(), e.getY());
				if (p.distance(position) > DEPLACEMENT_MIN) {
					this.grapher.setCursor(Cursor.CLOSED_HAND);
					grapher.translate(e.getX() - p.getX(), e.getY() - p.getY());
					p = position;
					state = State.DRAGGING_G;
				}
				break;
			default:
				break;
			}
			break;
		case DRAGGING_G:
			switch(e.getEventType().getName()) {
			case "MOUSE_DRAGGED":
				Point2D position = new Point2D(e.getX(), e.getY());
				grapher.translate(e.getX() - p.getX(), e.getY() - p.getY());
				p = position;
				break;
			case "MOUSE_RELEASED":
				this.grapher.setCursor(Cursor.DEFAULT);
				state = State.IDLE;
				break;
			default:
				break;
			}
			break;
		case WAITING_D:
			switch(e.getEventType().getName()) {
			case "MOUSE_RELEASED":
				switch(e.getButton().name()) {
				case "SECONDARY":
					grapher.zoom(p, -ZOOM_FACTOR);
					state = State.IDLE;
					break;
				default:
					break;
				}
				break;
			case "MOUSE_DRAGGED":
				Point2D position = new Point2D(e.getX(), e.getY());
				if (p.distance(position) > DEPLACEMENT_MIN) {
					this.grapher.setCursor(Cursor.H_RESIZE);
					grapher.getGraphicsContext2D().setLineDashes( null );
	    			grapher.redraw();
					this.grapher.dessineRectangleZoom(p, new Point2D(e.getX(), e.getY()));
					state = State.DRAGGING_D;
				}
				break;
			default:
				break;
			}
			break;
		case DRAGGING_D:
			switch(e.getEventType().getName()) {
			case "MOUSE_DRAGGED":
				this.grapher.dessineRectangleZoom(p, new Point2D(e.getX(), e.getY()));
				break;
			case "MOUSE_RELEASED":
				this.grapher.zoom(p, new Point2D(e.getX(), e.getY()));
				this.grapher.setCursor(Cursor.DEFAULT);
				state = State.IDLE;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
}
