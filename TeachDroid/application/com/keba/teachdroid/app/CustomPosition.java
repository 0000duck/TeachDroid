package com.keba.teachdroid.app;

public class CustomPosition {

	private String unit;
	private String name;
	private float value;

	public CustomPosition(String _unit, String _name, float _value) {
		unit = _unit;
		name = _name;
		value = _value;
	}

	public void setUnit(String _unit) {
		unit = _unit;
	}

	public void setName(String _name) {
		name = _name;
	}

	public void setValue(float _value) {
		value = _value;
	}

	public String getUnit() {
		return unit;
	}

	public String getName() {
		return name;
	}

	public float getValue() {
		return value;
	}

}
