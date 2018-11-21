package grapher.ui;

import javafx.application.Application.Parameters;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class GrapherSplit extends SplitPane
{
	public GrapherSplit( Parameters parameters, GrapherCanvas grapher )
	{
		this.setOrientation( Orientation.HORIZONTAL );
		BorderPane bordPane = new BorderPane();
		GrapherStyle grapherTableView = new GrapherStyle( parameters, grapher );
		GrapherToolBar grapherToolBar = new GrapherToolBar( grapher, grapherTableView );
		bordPane.setBottom( grapherToolBar );
		bordPane.setCenter( grapherTableView );
		this.setBorderPane( bordPane );
	}
	public void setBorderPane(BorderPane borderPane) {
		this.getItems().add( borderPane );
	}
}
