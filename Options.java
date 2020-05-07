package com.kopyrin.vasily.app.calc;
import java.math.MathContext;
import java.math.RoundingMode;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

class Options{
	static SimpleIntegerProperty precision = new SimpleIntegerProperty(50);
	static SimpleObjectProperty<RoundingMode> roundingMode = new SimpleObjectProperty<>(RoundingMode.HALF_DOWN);
	static MathContext mathContext;
	
	static{
		mathContext = new MathContext(precision.get(), roundingMode.get());
		
		precision.addListener((obs,oldV,newV) -> {
			mathContext = new MathContext(newV.intValue(), mathContext.getRoundingMode());
		});
		
		roundingMode.addListener((obs,oldV,newV) -> {
			mathContext = new MathContext(mathContext.getPrecision(), newV);
		});
	}	
	
	static final int displayVisibleLength = 21;
	static final int generalWidth = 260;
}	