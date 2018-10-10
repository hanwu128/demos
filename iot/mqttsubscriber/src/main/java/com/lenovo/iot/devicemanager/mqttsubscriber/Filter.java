package com.lenovo.iot.devicemanager.mqttsubscriber;

public class Filter {

	private String propertyName;
	private String dataType;
	private String operator;
	private String value;
	private String conjunction;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConjunction() {
		return conjunction;
	}

	public void setConjunction(String conjunction) {
		this.conjunction = conjunction;
	}
	
	public Filter(String propertyName, String dataType, String operator, String value) {
		this.propertyName = propertyName;
		this.dataType = dataType;
		this.operator = operator;
		this.value = value;
	}
	
	public Filter(String propertyName, String dataType, String operator, String value, String conjunction) {
		this.propertyName = propertyName;
		this.dataType = dataType;
		this.operator = operator;
		this.value = value;
		this.conjunction = conjunction;
	}
}
