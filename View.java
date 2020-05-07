package com.kopyrin.vasily.app.calc;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Pos;
import java.util.Optional;
import java.math.RoundingMode;

public class View{
	 
	Controller controller;
	 
	View(Controller controller){
		this.controller = controller;
	}	

	void create(Stage stage){
		BorderPane root = new BorderPane();
		MenuBar menuBar = createMenuBar();
		TextField display = createDisplay();
		display.focusedProperty().addListener((obs,oldV,newV) -> root.requestFocus());
		ScrollBar sb = createScrollBar();
		bindDisplayWithScrollBar(display, sb);
		FlowPane topPane = new FlowPane(menuBar, display, sb);
		root.setTop(topPane);
		
		FlowPane centerPane = new FlowPane();
		for(Buttons button : Buttons.values()){
			Button btn = new Button(button.title);
			btn.setPrefSize(65,39);
			btn.setOnAction((ae) -> display.setText(
				controller.pressButton(Buttons.getButton(ae),display.getText())
			));
			btn.focusedProperty().addListener((obs,oldV,newV) -> root.requestFocus());
			centerPane.getChildren().add(btn);	
		}
		root.setCenter(centerPane);
		root.setOnKeyPressed((ke) ->{
			Buttons btn = Buttons.getButton(ke);
			if(btn != null)
				display.setText(
					controller.pressButton(btn,display.getText()));
		});
				
		stage.setScene(new Scene(root,Options.generalWidth,308));
		stage.sizeToScene();
		stage.setTitle("Kalculator");
		stage.setResizable(false);
		stage.show();
	}
	
	private TextField createDisplay(){
		TextField display = new TextField("0");
		display.setMaxHeight(35.0);
		display.setMinHeight(35.0);
		display.setMaxWidth(Options.generalWidth);
		display.setMinWidth(Options.generalWidth);
		display.setFont(new Font("Arial", 20));
		display.setAlignment(Pos.CENTER_RIGHT);
		display.setEditable(false);
		display.setFocusTraversable(false);
		return display;
	}
	
	private ScrollBar createScrollBar(){
		ScrollBar sb = new ScrollBar();
		sb.setValue(0);
		sb.setVisibleAmount(0);
		sb.setUnitIncrement(1);
		sb.setMaxWidth(Options.generalWidth);
		sb.setMinWidth(Options.generalWidth);
		sb.setVisible(false);
		return sb;
	}
	
	private void bindDisplayWithScrollBar(TextField display, ScrollBar sb){
		display.setTextFormatter(new TextFormatter<>((TextFormatter.Change change) -> { 
			if(change.isAdded()){
				String newV = change.getControlNewText();
				String oldV = change.getControlText();
				int diff = newV.length() - Options.displayVisibleLength;
				if(diff > 0){
					// if exception message on display, position caret move to 27
					int pos = (newV.charAt(0) == 'E') ? 27 :
						(oldV.equals(newV.substring(0,newV.length() - 1))) ? newV.length() : Options.displayVisibleLength;
					change.setCaretPosition(pos);
					sb.setVisible(true);
					sb.setMin(0);
					sb.setMax(diff);
					sb.setBlockIncrement(diff);
					sb.setVisibleAmount(diff - Math.pow(diff,2)/newV.length());
					sb.setValue(pos - Options.displayVisibleLength);
				}
				else{
					sb.setVisible(false);
				}	
			}	
			return change;
		}));
		
		sb.valueProperty().addListener((obs, oldV, newV) -> {
			int pos = (newV.intValue() > oldV.intValue()) ? Options.displayVisibleLength + newV.intValue() : newV.intValue();
 			display.positionCaret(pos);
		});
	}
	
	private MenuBar createMenuBar(){
		MenuBar menuBar = new MenuBar();
			Menu menuFile = new Menu("File");
				MenuItem menuItemClose = new MenuItem("Exit");
				menuItemClose.setOnAction((ae) -> Platform.exit());
			menuFile.getItems().add(menuItemClose);
			Menu menuOptions = new Menu("Options");
				MenuItem menuItemPrecision = new MenuItem("Precision (" + Options.precision.get() + ")");
					TextInputDialog precisionDialog = new TextInputDialog(String.valueOf(Options.precision.get()));
					precisionDialog.setTitle("Set precision");
					precisionDialog.setHeaderText(null);
					precisionDialog.setGraphic(null);
					precisionDialog.getEditor().textProperty().addListener((obs, oldV, newV) ->{
						if(!newV.isEmpty())
							if(!newV.matches("^[1-9]\\d*"))
								precisionDialog.getEditor().setText(oldV);						
					});
					menuItemPrecision.setOnAction((ae) -> {
						Optional<String> res = precisionDialog.showAndWait();
						if(res.filter((s) -> !s.equals("")).isPresent()){
							Options.precision.setValue(Integer.valueOf(res.get()).intValue());
							menuItemPrecision.setText("Precision (" + Options.precision.get() + ")");
						}
					});
				Menu menuItemRounding = new Menu("Rounding mode");
					RadioMenuItem ceiling = 	new RadioMenuItem("CEILING");
					ceiling.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.CEILING));
					RadioMenuItem down = 		new RadioMenuItem("DOWN");
					down.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.DOWN));
					RadioMenuItem floor = 		new RadioMenuItem("FLOOR");
					floor.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.FLOOR));
					RadioMenuItem half_down = 	new RadioMenuItem("HALF_DOWN");
					half_down.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.HALF_DOWN));
					RadioMenuItem half_even = 	new RadioMenuItem("HALF_EVEN");
					half_even.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.HALF_EVEN));
					RadioMenuItem half_up = 	new RadioMenuItem("HALF_UP");
					half_up.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.HALF_UP));
					RadioMenuItem unnecessary = new RadioMenuItem("UNNECESSARY");
					unnecessary.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.UNNECESSARY));
					RadioMenuItem up = 			new RadioMenuItem("UP");
					up.setOnAction((ae) -> Options.roundingMode.set(RoundingMode.UP));
					ToggleGroup groupRound = new ToggleGroup();
					groupRound.getToggles().addAll(ceiling, down, floor, half_down, half_even, half_up, unnecessary, up);
					half_down.setSelected(true);
				menuItemRounding.getItems().addAll(ceiling, down, floor, half_down, half_even, half_up, unnecessary, up);	
			menuOptions.getItems().addAll(menuItemPrecision, menuItemRounding);
			Menu menuHelp = new Menu("Help");
				Menu menuItemHelp = new Menu("Help");
					MenuItem menuItemHelpPrecision = new MenuItem("Precision ?");
						Alert precisionHelpInfo = new Alert(Alert.AlertType.INFORMATION);
						precisionHelpInfo.setTitle("Precision ?");
						precisionHelpInfo.setHeaderText(null);
						precisionHelpInfo.setContentText("The number of digits to be used for an operation. \nResults are rounded to this precision");
					menuItemHelpPrecision.setOnAction((ae) -> precisionHelpInfo.show());
					MenuItem menuItemHelpRoundingMode = new MenuItem("RoundingMode ?");
						Alert roundingModeHelpInfo = new Alert(Alert.AlertType.INFORMATION);
						roundingModeHelpInfo.setTitle("RoundingMode ?");
						roundingModeHelpInfo.setHeaderText("Specifies a rounding behavior for numerical operations \ncapable of discarding precision");
						roundingModeHelpInfo.setContentText("CEILING - \t\tRounding mode to round towards positive infinity.\n" +
															"DOWN - \t\t\tRounding mode to round towards zero.\n" +
															"FLOOR - \t\t\tRounding mode to round towards negative infinity.\n" +
															"HALF_DOWN - \tRounding mode to round towards\n" +
															"\t\t\t\t\"nearest neighbor\"unless both neighbors are equidistant,\n" +
															"\t\t\t\tin which case round down.\n" +
															"HALF_EVEN - \t\tRounding mode to round towards the\n" +
															"\t\t\t\t\"nearest neighbor\" tunless both neighbors are\n" +
															"\t\t\t\tequidistant, in which case, round towards the even\n" +
															"\t\t\t\tneighbor.\n" +
															"HALF_UP	- \t\tRounding mode to round towards\n" +
															"\t\t\t\t\"nearest neighbor\"tunless both neighbors are\n" +
															"\t\t\t\tequidistant, in which case round up.\n" +
															"UNNECESSARY - \tRounding mode to assert that the requested\n" +
															"\t\t\t\toperation has an exact result, hence no rounding is\n" +
															"\t\t\t\tnecessary.\n" +
															"UP - \t\t\tRounding mode to round away from zero.");
					menuItemHelpRoundingMode.setOnAction((ae) -> roundingModeHelpInfo.show());
				menuItemHelp.getItems().addAll(menuItemHelpPrecision, menuItemHelpRoundingMode);
				MenuItem menuItemAboute = new MenuItem("About");
					Alert aboutHelpInfo = new Alert(Alert.AlertType.INFORMATION);
					aboutHelpInfo.setTitle("About");
					aboutHelpInfo.setHeaderText(null);
					aboutHelpInfo.setContentText("This program is free software.\n" + 
												"Author and developer Kopyrin Vasily (vasua84@yandex.ru).");
				menuItemAboute.setOnAction((ae) -> aboutHelpInfo.show());
			menuHelp.getItems().addAll(menuItemHelp, menuItemAboute);
		menuBar.getMenus().addAll(menuFile, menuOptions, menuHelp);	
		menuBar.setMaxWidth(Options.generalWidth);
		menuBar.setMinWidth(Options.generalWidth);
		
		return menuBar;
	}
}