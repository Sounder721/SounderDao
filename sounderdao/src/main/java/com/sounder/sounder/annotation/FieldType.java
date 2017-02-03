package com.sounder.sounder.annotation;

public enum FieldType {
	TEXT("TEXT"),
	INTEGER("INTEGER");
	private String type;
	FieldType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
}
