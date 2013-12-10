package io;

import java.util.List;

public interface IMessageReceiver {
	public void receive(List<String> buffer);
}
