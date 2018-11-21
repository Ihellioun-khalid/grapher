package grapher.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;


/*
 * 
 */
public class Main extends Application
{

	public void start(Stage stage)
	{
		
		BorderPane root = new BorderPane();
		BorderPane borderP = new BorderPane();
		GrapherCanvas gc = new GrapherCanvas( getParameters() );
		GrapherSplit splitP = new GrapherSplit( getParameters(), gc );
		Scene s = new Scene( root );

		splitP.getItems().add( borderP );
		root.setCenter( splitP );
		borderP.setCenter( gc );
		stage.setTitle( "grapher" );
		stage.setScene( s );
		stage.show();
	}
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}