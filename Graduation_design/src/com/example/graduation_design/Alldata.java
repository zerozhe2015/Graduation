package com.example.graduation_design;

public class Alldata {

	private String pm;
	private String comprehensive;
	private String temperature;
	private String humidity;
	private String wind1;
	private String wind2;
	private String time;

	public Alldata() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Alldata(String pm, String comprehensive, String temperature, String humidity, String wind1, String wind2,
			String time) {
		super();
		this.pm = pm;
		this.comprehensive = comprehensive;
		this.temperature = temperature;
		this.humidity = humidity;
		this.wind1 = wind1;
		this.wind2 = wind2;
		this.time = time;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getComprehensive() {
		return comprehensive;
	}

	public void setComprehensive(String comprehensive) {
		this.comprehensive = comprehensive;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getWind1() {
		return wind1;
	}

	public void setWind1(String wind1) {
		this.wind1 = wind1;
	}

	public String getWind2() {
		return wind2;
	}

	public void setWind2(String wind2) {
		this.wind2 = wind2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
