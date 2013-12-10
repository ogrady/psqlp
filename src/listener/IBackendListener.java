package listener;

import parser.objects.RelOptInfo;

public interface IBackendListener extends IListener {
	public void onNewRelOptInfo(RelOptInfo roi);
}
