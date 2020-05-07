package com.kopyrin.vasily.app.calc;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
	
	Model model;
	View view;
	Controller controller;
	
	public static void main(String[] args){
		Application.launch();
	}	
	
	@Override
	public void init(){
		model = new Model();
		controller = new Controller(model);
		view = new View(controller);
	}	
	
	@Override
	public void start(Stage stage){
		view.create(stage);
	}	

}