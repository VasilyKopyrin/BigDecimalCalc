package com.kopyrin.vasily.app.calc;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

enum Buttons{
	C			("C", KeyCode.ESCAPE),
	MS			("MS", null),
	MR			("MR", null),
	MC			("MC", null),
	CE			("CE", null),
	SQRT		("\u221A", null),
	PERCENT		("\u0025", null),
	DIVISION	("\u00F7", KeyCode.DIVIDE),
	SEVEN		("7", KeyCode.DIGIT7),
	EIGHT		("8", KeyCode.DIGIT8),
	NINE		("9", KeyCode.DIGIT9),
	MULTIPLY	("*", KeyCode.MULTIPLY),
	FOUR		("4", KeyCode.DIGIT4),
	FIVE		("5", KeyCode.DIGIT5),
	SIX			("6", KeyCode.DIGIT6),
	MINUS		("-", KeyCode.SUBTRACT),
	ONE			("1", KeyCode.DIGIT1),
	TWO			("2", KeyCode.DIGIT2),
	THREE		("3", KeyCode.DIGIT3),
	PLUS		("+", KeyCode.ADD),
	ZERO		("0", KeyCode.DIGIT0),
	POINT		(".", KeyCode.DECIMAL),
	SIGN		("+/-", null),
	CALC		("=", KeyCode.ENTER);
	
	String title;
	KeyCode keyCode;
	
	Buttons(String title, KeyCode keyCode){
		this.title = title;
		this.keyCode = keyCode;
	}

	static Buttons getButton(ActionEvent ae){
		String btn_title = ((Button) ae.getSource()).getText();
		for(Buttons btn : Buttons.values()){
			if(btn.title.equals(btn_title))
				return btn;
		}	
		throw new IllegalArgumentException("Unknown button. Press \'C\' and try again.");
	}	
	
	static Buttons getButton(KeyEvent ke){
		KeyCode key = ke.getCode();
		switch(key){
			case DIGIT8:
				key = ke.isShiftDown() ? KeyCode.MULTIPLY : KeyCode.DIGIT8;
				break;
			case SLASH:
				key = ke.isShiftDown() ? KeyCode.DIVIDE : null;
				break;
			case EQUALS:
				key = ke.isShiftDown() ? KeyCode.ADD : KeyCode.EQUALS;
				break;
			case MINUS:
				key = KeyCode.SUBTRACT;
				break;	
			case PERIOD:
				key = KeyCode.DECIMAL;
				break;	
			case NUMPAD7:
				key = KeyCode.DIGIT7;
				break;
			case NUMPAD8:
				key = KeyCode.DIGIT8;
				break;	
			case NUMPAD9:
				key = KeyCode.DIGIT9;
				break;	
			case NUMPAD4:
				key = KeyCode.DIGIT4;
				break;	
			case NUMPAD5:
				key = KeyCode.DIGIT5;
				break;	
			case NUMPAD6:
				key = KeyCode.DIGIT6;
				break;	
			case NUMPAD1:
				key = KeyCode.DIGIT1;
				break;	
			case NUMPAD2:
				key = KeyCode.DIGIT2;
				break;
			case NUMPAD3:
				key = KeyCode.DIGIT3;
				break;	
			case NUMPAD0:
				key = KeyCode.DIGIT0;
				break;			
		}	
		for(Buttons btn : Buttons.values()){
			if((btn.keyCode != null) && btn.keyCode.equals(key))
				return btn;
		}
		return null;
	}
}