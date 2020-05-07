package com.kopyrin.vasily.app.calc;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

class Model{
	BigDecimal operand1; 	
	BigDecimal operand2;
	BigDecimal memory;
	Buttons operation;

	Model(){
		operand1 = null;
		operand2 = null;
		memory = null;
		operation = null;
	}
	
	void reset(){
		operand1 = null;
		operand2 = null;
		operation = null;
	}
	
	BigDecimal calc(BigDecimal oper1, BigDecimal oper2) throws Exception{
		if(operation == Buttons.SQRT)
			return new BigDecimal(Math.sqrt(oper1.doubleValue()), Options.mathContext);
		switch(operation){
			case PERCENT:
					return (oper1.multiply(oper2)).divide(new BigDecimal(100), Options.mathContext);
			case DIVISION:
					return oper1.divide(oper2, Options.mathContext);
			case MULTIPLY:
					return oper1.multiply(oper2, Options.mathContext);
			case MINUS:
					return oper1.subtract(oper2, Options.mathContext);
			case PLUS:
					return oper1.add(oper2, Options.mathContext);
			default:
				return new BigDecimal(0, Options.mathContext);	
		}	
	}	
}	