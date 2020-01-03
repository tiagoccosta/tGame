package com.tiagocc.tgame;
import java.util.*;
import java.util.concurrent.*;

public class GuiComponent {
	private List<ActionListener> listeners = new CopyOnWriteArrayList<>();

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}

	public void fireActionEvent() {
		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(this, "message"));
		}
	}
}
