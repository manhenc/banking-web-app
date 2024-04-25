package com.fdm.barbieBank.model;

import java.util.List;

public class ChartLine {

	private String label;
	private List<Double> data;
	private String borderColor;
	private boolean fill;
	
	public ChartLine(String label, List<Double> data, String borderColor, boolean fill) {
		super();
		this.label = label;
		this.data = data;
		this.borderColor = borderColor;
		this.fill = fill;
	}
	
	
	public String getLabel() {
		return label;
	}
	public List<Double> getData() {
		return data;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public boolean isFill() {
		return fill;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setData(List<Double> data) {
		this.data = data;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	

}
