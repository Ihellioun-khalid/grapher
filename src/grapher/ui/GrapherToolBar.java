package grapher.ui;

import java.util.Optional;
import grapher.fc.FunctionStyle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
/*
 * 
 */
public class GrapherToolBar extends ToolBar 
{
	private GrapherStyle grapherTableView;
	
	public GrapherStyle getgrapherTableView( ) {
		return grapherTableView ;
	}
	public void setGrapherTableView(GrapherStyle grapherTableView) {
		this.grapherTableView = grapherTableView;
	}
	public GrapherToolBar( GrapherCanvas grapher, GrapherStyle grapherTableView )
	{
		Button buttonAdd_fonction = new Button( "+" );
		Button buttonDelete_fonction = new Button( "-" );
		buttonAdd_fonction.setMinWidth(50);
		buttonDelete_fonction.setMinWidth(50);
	
		this.getItems().add( buttonAdd_fonction );
		this.getItems().add( buttonDelete_fonction );
		this.setGrapherTableView( grapherTableView );
		buttonAdd_fonction.setOnAction(new EventHandler<ActionEvent>(){
		    @Override
		    public void handle(ActionEvent e)
		    {
		        TextInputDialog textInputDialog = new TextInputDialog();
		        textInputDialog.setTitle( "Expressio" );
		        textInputDialog.setHeaderText("Expression");
		        textInputDialog.setContentText("Nouvelle expression");
		        Optional<String> function = textInputDialog.showAndWait();
		        if( function.get().length() > 0 ){
		        		try
		        		{
		        			if(! grapher.F_exist( function.get() ) )
		        			{
		        				grapher.F_print( function.get() );
		        				grapherTableView.refreshList();
		        			}
		        		}
		        		catch( Exception Exception )
		        		{
		        				String  warning2 ="Please enter a valid expression.";
		        				Alert alert2 =new Alert(AlertType.ERROR);
		        				alert2.setTitle("Error");
		        				alert2.setHeaderText(warning2);
		        				alert2.showAndWait();
		        		}
		        }
		    }
		});
		buttonDelete_fonction.setOnAction( new EventHandler<ActionEvent>()
		{
		    @Override
		    public void handle(ActionEvent e)
		    {
		    	ObservableList<FunctionStyle> fList = grapherTableView.getSelectionModel().getSelectedItems();

		        	for( FunctionStyle function : fList )
		        	{
		        		grapher.F_Delete( function );
		        	}
		        	grapherTableView.refreshList();
		    }
		});
	}
}
