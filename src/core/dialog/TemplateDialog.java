package core.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import core.RedisClientWindow;
import core.utils.CacheConstant;
import core.utils.PropertiesUtil;

/**
 * @author liu_wp
 * @date 2018年1月8日
 * @see
 */
public class TemplateDialog extends Dialog {
	private String type;

	private Text valueText;

	/**
	 * Create the dialog.
	 *
	 * @param parentShell
	 */
	public TemplateDialog(Shell parentShell, String type) {
		super(parentShell);
		this.type = type;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {// 如果单击确定按钮，
			if (valueText.getText() == null || valueText.getText() == "") {
				MessageDialog.openError(super.getShell(), "错误", "值不能为空");
			} else {
				Map<String, String> map = new HashMap<>();
				String value = valueText.getText().trim();
				if (value.indexOf(",") != -1) {
					String[] valueArr = value.split(",");
					for (int i = 0; i < valueArr.length; i++) {
						map.put(type + "." + valueArr[i], valueArr[i]);
					}
				} else {
					map.put(type + "." + value, value);
				}
				PropertiesUtil.writeAppendProperties(map, CacheConstant.REDIS_PREFIX_FILE);
				if (type.equals(CacheConstant.NAMESPACE)) {
					Combo rdNamespaceCombo = RedisClientWindow.getRedisClientWindow().getRdNamespaceCombo();
					rdNamespaceCombo.setItems(CacheConstant.ppsMap.get(CacheConstant.NAMESPACE));
					rdNamespaceCombo.select(0);
				} else if (type.equals(CacheConstant.PREFIX)) {
					Combo rdPrefixCombo = RedisClientWindow.getRedisClientWindow().getRdPrefixCombo();
					rdPrefixCombo.setItems(CacheConstant.ppsMap.get(CacheConstant.PREFIX));
					rdPrefixCombo.select(0);
				}
				super.okPressed();
			}
		} else if (buttonId == MyIDialogConstants.DELETED_ID) {
			MessageDialog.openConfirm(this.getShell(), "确认", "要删除吗？");
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			super.cancelPressed();
		}

	}

	/**
	 * Create contents of the button bar.
	 *
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, MyIDialogConstants.OK_ID, "保存", true);
		Button button = createButton(parent, MyIDialogConstants.DELETED_ID, "删除", false);
		button.setEnabled(false);
		createButton(parent, MyIDialogConstants.CANCEL_ID, "取消", false);
	}

	/**
	 * Create contents of the dialog.
	 *
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		container.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("值：");

		valueText = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_valueText = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_valueText.heightHint = 117;
		valueText.setLayoutData(gd_valueText);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label lblaabb = new Label(container, SWT.NONE);
		lblaabb.setText("支持批量新增 格式：AA,BB,...");
		return container;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(317, 280);
	}

}
