package listener;

import listener.IListener;

public interface INotifier<L extends IListener> {
	void notify(L listener);
}
