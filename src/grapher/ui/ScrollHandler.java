package grapher.ui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ScrollEvent;

public class ScrollHandler implements EventHandler <ScrollEvent> {
	
	public static Double ZOOM = 1.0;
	Point2D p;
	enum State {IDLE,CLIC_OR_DRAG,DRAG};
	State state=State.IDLE;
	GrapherCanvas grapher;
	public ScrollHandler(GrapherCanvas grapherCanvas) {
		this.grapher=grapherCanvas;
	}

	@Override
	public void handle(ScrollEvent e) {
		Point2D p=new Point2D(e.getX(),e.getY());
		if(e.getDeltaX()>0 || e.getDeltaY()>0 ){
			this.grapher.zoom(p,1);
		}
		else if(e.getDeltaX()<0 || e.getDeltaY()<0 ){
			this.grapher.zoom(p,-1);
		}	
	}
}