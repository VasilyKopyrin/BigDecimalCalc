package com.kopyrin.vasily.app.calc;
import java.math.BigDecimal;

class Controller{
	
	private Model model;
	private boolean newOperand;
	
	Controller(Model model){
		this.model = model;
		newOperand = true;
	}
	
	String pressButton(Buttons btn,String display){
		if((display.charAt(0) == 'E') && (!btn.equals(Buttons.C))) //if exception message on display
			return display;
		try{
			if(btn.equals(Buttons.DIVISION) || btn.equals(Buttons.MULTIPLY) || 
			btn.equals(Buttons.MINUS) || btn.equals(Buttons.PLUS) || btn.equals(Buttons.PERCENT)){
				return pressBinaryOperation(btn,display);
			}
			else if(btn.equals(Buttons.SQRT)){
				return pressUnaryOperation(btn,display);
			}
			else if(btn.equals(Buttons.ONE) || btn.equals(Buttons.TWO)
			|| btn.equals(Buttons.THREE) || btn.equals(Buttons.FOUR) || btn.equals(Buttons.FIVE)
			|| btn.equals(Buttons.SIX) || btn.equals(Buttons.SEVEN) || btn.equals(Buttons.EIGHT) 
			|| btn.equals(Buttons.NINE) || btn.equals(Buttons.ZERO) || btn.equals(Buttons.POINT)){
				return pressNumber(btn,display);	
			}
			else if(btn.equals(Buttons.C)){
				return pressC();
			}
			else if(btn.equals(Buttons.CE)){
				return pressCE();
			}
			else if(btn.equals(Buttons.MS)){
				return pressMS(display);
			}
			else if(btn.equals(Buttons.MR)){
				return pressMR(display);
			}
			else if(btn.equals(Buttons.MC)){
				return pressMC(display);
			}
			else if(btn.equals(Buttons.SIGN)){
				return pressSIGN(display);
			}
			else if(btn.equals(Buttons.CALC)){
				return pressCalc(display);
			}
			else{
				throw new Exception("Unknown command");
			}
		}
		catch(Exception e){
			return "Error. Press \'C\' and try again. Describe: " + e.toString() 
					+ ". \"To make mistakes is human, to forgive is divine!\"";
		}
	}
	
	private String pressBinaryOperation(Buttons btn, String display) throws Exception{
		model.operand2 = null;
		if(model.operand1 == null){
			model.operand1 = new BigDecimal(display, Options.mathContext);
		}
		else if(!newOperand){
			if(btn.equals(Buttons.PERCENT) && model.operation.equals(Buttons.MULTIPLY))
				model.operation = btn;
			model.operand1 = model.calc(model.operand1, new BigDecimal(display, Options.mathContext));
			display = model.operand1.toEngineeringString();
		}
		newOperand = true;
		model.operation = btn;
		return display;
	}
	
	private String pressUnaryOperation(Buttons btn, String display) throws Exception{
		model.reset();
		model.operation = btn;
		newOperand = true;
		return model.calc(new BigDecimal(display, Options.mathContext), null).toEngineeringString();
	}
	
	private String pressNumber(Buttons btn, String display) throws Exception{
		if(display.equals("0")){
			newOperand = btn.equals(Buttons.ZERO) ? true : false; 
			return btn.title;
		}
		if(btn.equals(Buttons.POINT) && display.contains(".") && !newOperand)
			return display;
		String res = newOperand ? btn.title : display + btn.title;
		if(res.equals("."))
			res = "0.";
		newOperand = false;
		return res;
	}
	
	private String pressC() throws Exception{
		model.reset();
		newOperand = true;
		return "0";
	}
	
	private String pressCE() throws Exception{
		model.operand2 = null;
		newOperand = true;
		return "0";
	}	
	
	private String pressMS(String display) throws Exception{
		model.memory = new BigDecimal(display, Options.mathContext);
		return display;
	}	
	
	private String pressMR(String display) throws Exception{
		if(model.memory != null){
			display = model.memory.toEngineeringString();
			newOperand = false;
		}
		return display;
	}	
	
	private String pressMC(String display) throws Exception{
		model.memory = null;
		return display;
	}	
	
	private String pressSIGN(String display) throws Exception{
		return new BigDecimal(display, Options.mathContext).negate().toEngineeringString();
	}	
	
	private String pressCalc(String display) throws Exception{
		if(model.operation !=null && model.operation.equals(Buttons.SQRT))
			return pressUnaryOperation(Buttons.SQRT,display);
		if(model.operand1 != null && !newOperand){
			model.operand2 = new BigDecimal(display, Options.mathContext);
			display = model.calc(model.operand1, model.operand2).toEngineeringString();
			model.operand1 = null;
		}
		else if(model.operand2 != null){
			display = model.calc(new BigDecimal(display, Options.mathContext),model.operand2).toEngineeringString();
		}	
		
		return display;
	}
}