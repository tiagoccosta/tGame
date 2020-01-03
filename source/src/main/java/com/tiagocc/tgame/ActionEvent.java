package com.tiagocc.tgame;

public class ActionEvent {
	Object source;
	Object data;

	public ActionEvent(Object source, Object data) {
		this.source = source;
		this.data = data;
	}
}
