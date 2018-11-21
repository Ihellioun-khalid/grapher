package grapher.ui;

import java.util.ArrayList;
import grapher.fc.FunctionStyle;
import javafx.application.Application.Parameters;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


public class GrapherStyle extends TableView<FunctionStyle>
{
	
	private GrapherCanvas grapher;
	private ObservableList<FunctionStyle> items = FXCollections.observableArrayList();
	private ObservableList<FunctionStyle> selected = FXCollections.observableArrayList();
	
    public GrapherCanvas getGc() {
		return grapher;
	}
	public void setGrapherCanvas(GrapherCanvas grapher) {
		this.grapher = grapher;
	}
	
	@SuppressWarnings("unchecked")
	public GrapherStyle( Parameters params, GrapherCanvas grapher )
	{
		
		this.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
		
		TableColumn<FunctionStyle, String> function = new TableColumn<FunctionStyle, String>( "Function" );
        TableColumn<FunctionStyle, String> color = new TableColumn<FunctionStyle, String>( "Color" );
		
        this.setEditable(true);
        function.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        function.setCellFactory( TextFieldTableCell.forTableColumn() );
        color.setCellValueFactory( new PropertyValueFactory<>( "color" ) );
        function.setOnEditCommit( updateFunctionHandler );
        
		this.setItems( items );
		this.getColumns().addAll( function, color );
		this.setGrapherCanvas( grapher );
		this.getSelectionModel().selectedItemProperty().addListener( tableViewHandler );
		this.refreshList();
	}
	
	EventHandler<CellEditEvent<FunctionStyle, String>> updateFunctionHandler = new EventHandler<CellEditEvent<FunctionStyle, String>>()
	{
		@Override
		public void handle(CellEditEvent<FunctionStyle, String> e)
		{
			try
			{
				if( !grapher.F_exist( e.getNewValue() ) )
				{
					grapher.F_update( e.getOldValue(), e.getNewValue() );
				}
			}
			catch( Exception exception )
			{
				String  warning2 ="Please enter a valid expression.";
				Alert alert2 =new Alert(AlertType.ERROR);
				alert2.setTitle("Error");
				alert2.setHeaderText(warning2);
			}
		}
		
	};
	
	public void refreshList()
	{
		ArrayList<String> selectedString = new ArrayList<String>();
		if( selected.size() > 0 )
		{
			for( FunctionStyle f1 : selected )
			{
				selectedString.add( f1.toString() );
			}
		}
		items.clear();
		for( FunctionStyle f : this.getGc().getPropertiesList() )
		{
			items.add( f );
		}
		// --
		if( selectedString.size() > 0)
		{
			for( String s : selectedString )
			{
				for( FunctionStyle f : items )
				{
					if( f.toString().equals( s ) )
					{
						int index = items.indexOf( f );
						this.getSelectionModel().select( index );
						break;
					}
				}
			}
		}
	}
	
	ChangeListener<FunctionStyle> tableViewHandler = new ChangeListener<FunctionStyle>()
	{
		@Override
		public void changed(ObservableValue<? extends FunctionStyle> observable, FunctionStyle oldValue,
				FunctionStyle newValue) {
			ObservableList<FunctionStyle> list = getSelectionModel().getSelectedItems();
			selected.clear();
			for( FunctionStyle f : list )
				selected.add( f );
			grapher.setBold( list );
		}
	};	
}
