package grapher.fc;

import grapher.ui.GrapherCanvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class FunctionStyle
{
	public static final int DEFAULT = 2;
	public static final int BOLD = 4;
	private Function function;
	private String name;
	private ColorPicker colorpicker;
	private String colorName;
	private int lineWidth;
	private GrapherCanvas grapherCanvas;
	
	
	public int getLineWidth() {
		return lineWidth; 
		}
	
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		}
	
	public ColorPicker getColor() {
		return colorpicker;
		}
	
	public void setColor(ColorPicker colorpicker) {
		this.colorpicker = colorpicker;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
	public String toString()
	{
		return this.getFunction().toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorString) {
		this.colorName = colorString;
	}

	public GrapherCanvas getGrapherCanvas() {
		return grapherCanvas;
	}

	public void setGrapherCanvas(GrapherCanvas grapherCanvas) {
		this.grapherCanvas = grapherCanvas;
	}
	public FunctionStyle( Function function, GrapherCanvas grapherCanvas )
	{
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue( Color.BLACK );
		this.setColor(colorPicker );
		this.setColorName( colorPicker.getValue().toString() );
		this.setLineWidth( DEFAULT );
		this.setFunction( function );
		this.setName( function.toString() );
		this.setGrapherCanvas( grapherCanvas );
		this.getColor().setOnAction( colorPickerHandler );
	}
	
	EventHandler<ActionEvent> colorPickerHandler = new EventHandler<ActionEvent>()
	{
		@Override
        public void handle(ActionEvent e) {
            setColorName( colorpicker.getValue().toString() );
            colorpicker.getValue().toString();
			getGrapherCanvas().redraw();
        }
	};
}
