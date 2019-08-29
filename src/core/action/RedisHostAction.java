package core.action;

import org.eclipse.jface.action.Action;

/**
 * @author liu_wp
 * @date 2018年1月9日
 * @see
 */
public class RedisHostAction extends Action {
	public RedisHostAction(String host) {
		super();
		setText(host);
		// this.setAccelerator(SWT.ALT + SWT.SHIFT + 'N');
		setToolTipText(host);
		// setImageDescriptor(ImageDescriptor.createFromFile(ServiceLinkAction.class,
		// "icons\\new.gif"));
	}

	@Override
	public boolean isChecked() {
		return super.isChecked();
	}

	@Override
	public void run() {
	}
}
