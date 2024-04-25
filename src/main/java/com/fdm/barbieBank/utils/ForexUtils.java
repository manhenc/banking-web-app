package com.fdm.barbieBank.utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class ForexUtils {
	
	public Double roundToFiveSF(Double rate) {
		BigDecimal bd = BigDecimal.valueOf(rate);
		bd = bd.round(new MathContext(6));
		Double rounded = bd.doubleValue();
		return rounded;
	}
}
