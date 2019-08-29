package core.action;

import org.eclipse.jface.action.Action;

import core.RedisClientWindow;
import core.dialog.ServiceConnectDialog;

/**
 * @author liu_wp
 * @date 2018年1月6日
 * @see
 */
public class ServiceLinkAction extends Action {
	public ServiceLinkAction() {
		super();
		setText("添加");
		// this.setAccelerator(SWT.ALT + SWT.SHIFT + 'N');
		setToolTipText("添加");
		// setImageDescriptor(ImageDescriptor.createFromFile(ServiceLinkAction.class,
		// "icons\\new.gif"));
	}

	@Override
	public void run() {
		ServiceConnectDialog ss = new ServiceConnectDialog(RedisClientWindow.getRedisClientWindow().getShell());
		ss.open();

	}
}
