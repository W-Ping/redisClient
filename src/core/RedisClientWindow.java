package core;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.wb.swt.SWTResourceManager;

import com.alibaba.fastjson.JSON;

import core.action.ServiceLinkAction;
import core.dialog.TemplateDialog;
import core.message.RedisConnectMsg;
import core.pojo.RedisResultPo;
import core.utils.CacheConstant;
import core.utils.Constants;
import core.utils.JSONFormatUtil;
import core.utils.PropertiesUtil;
import core.utils.RedisUtil;

/**
 * @author liu_wp
 * @date 2018年1月6日
 * @see
 */
public class RedisClientWindow extends ApplicationWindow {

	private static RedisClientWindow redisQtWindow;

	public static String getClipboardString() {
		// 获取系统剪贴板
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// 获取剪贴板中的内容
		Transferable trans = clipboard.getContents(null);

		if (trans != null) {
			// 判断剪贴板中的内容是否支持文本
			if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					// 获取剪贴板中的文本内容
					String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
					return text;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static RedisClientWindow getRedisClientWindow() {
		return redisQtWindow;
	}

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			RedisClientWindow window = new RedisClientWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setClipboardString(String text) {
		// 获取系统剪贴板
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 封装文本内容
		Transferable trans = new StringSelection(text);
		// 把文本内容设置到系统剪贴板
		clipboard.setContents(trans, null);
	}

	private String host;
	private String port;
	private String dbIndex;
	private Text redisKeyText;
	private Text resultText;
	private MenuManager server;
	private Combo rdPrefixCombo;
	private Combo rdNamespaceCombo;

	private Combo linkSymbolCombo;
	private ServiceLinkAction serviceLinkAction;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text expireTimeText;
	private Text dbIndexText;

	private Text fullKeyText;
	private RedisResultPo rrp = null;
	private Label resultLabelNums;

	/**
	 * Create the application window.
	 */
	public RedisClientWindow() {
		super(null);
		setShellStyle(SWT.MIN);
		redisQtWindow = this;
		init();
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();

	}

	public Combo getRdNamespaceCombo() {
		return rdNamespaceCombo;
	}

	public Combo getRdPrefixCombo() {
		return rdPrefixCombo;
	}

	public MenuManager getServer() {
		return server;
	}

	@Override
	public StatusLineManager getStatusLineManager() {
		return super.getStatusLineManager();
	}

	/**
	 * 启动初始化配置文件
	 */
	public void init() {
		PropertiesUtil.initReisKeyProperties();
		PropertiesUtil.initReisHostProperties(CacheConstant.REDIS_HOST_FILE);
		// PropertiesUtil.initReisHostProperties(CacheConstant.REDIS_SERVER_FILE);
	}

	public void setRdNamespaceCombo(Combo rdNamespaceCombo) {
		this.rdNamespaceCombo = rdNamespaceCombo;
	}

	public void setRdPrefixCombo(Combo rdPrefixCombo) {
		this.rdPrefixCombo = rdPrefixCombo;
	}

	@Override
	protected void addStatusLine() {
		super.addStatusLine();
	}

	/**
	 * Configure the shell.
	 *
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		Toolkit kit = Toolkit.getDefaultToolkit();
		newShell.setSize(800, 600);
		newShell.setLocation((kit.getScreenSize().width - 800) / 2, (kit.getScreenSize().height - 600) / 2);
		newShell.setText("Redis查询工具");
		newShell.setMinimumSize(800, 600);

	}

	/**
	 * Create contents of the application window.
	 *
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NO_MERGE_PAINTS);
		// 命名空间
		rdNamespaceCombo = new Combo(container, SWT.NONE);
		rdNamespaceCombo.setBounds(279, 15, 170, 25);
		String[] items = CacheConstant.ppsMap.get(CacheConstant.NAMESPACE);
		rdNamespaceCombo.setItems(items);
		rdNamespaceCombo.select(0);
		formToolkit.adapt(rdNamespaceCombo);
		formToolkit.paintBordersFor(rdNamespaceCombo);
		// redis 连接符
		linkSymbolCombo = new Combo(container, SWT.NONE);
		linkSymbolCombo.setBounds(279, 46, 88, 25);
		linkSymbolCombo.setItems("_", "~");
		linkSymbolCombo.select(0);
		formToolkit.adapt(linkSymbolCombo);
		formToolkit.paintBordersFor(linkSymbolCombo);
		// redis 前缀
		rdPrefixCombo = new Combo(container, SWT.NONE);
		rdPrefixCombo.setBounds(279, 77, 170, 25);
		String[] preItems = CacheConstant.ppsMap.get(CacheConstant.PREFIX);
		rdPrefixCombo.setItems(preItems);
		rdPrefixCombo.select(0);
		formToolkit.adapt(rdPrefixCombo);
		formToolkit.paintBordersFor(rdPrefixCombo);
		// redis key
		redisKeyText = new Text(container, SWT.BORDER);
		redisKeyText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		redisKeyText.setBounds(278, 110, 259, 25);
		// 查询按钮
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(557, 108, 88, 27);
		btnNewButton.setText("查 询");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				resultText.setText("");
				String redisKey = redisKeyText.getText();
				if (StringUtils.isBlank(redisKey)) {
					MessageDialog.openWarning(getShell(), "警告", "查找的数据不能为空！ ");
				} else {
					host = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_HOST);
					port = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_PORT);
					dbIndex = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_DB);
					String msg = "【" + host + ":" + port + "】 查询中...";
					RedisClientWindow.getRedisClientWindow().getStatusLineManager().setMessage(msg);
					if (Constants.isQueryLike()) {
						rrp = RedisUtil.getLike(redisKey.trim());
						fullKeyText.setText("*" + redisKey + "*");
					} else if (Constants.isIgnore()) {
						rrp = RedisUtil.get(redisKey.trim());
						fullKeyText.setText(redisKey);
					} else {
						String namespace = rdNamespaceCombo.getText();
						String prefix = rdPrefixCombo.getText();
						String linkSymbol = linkSymbolCombo.getText();
						StringBuilder sb = new StringBuilder();
						sb.append(prefix);
						sb.append(linkSymbol);
						sb.append(redisKey);
						redisKey = sb.toString().trim();
						fullKeyText.setText(namespace + "." + redisKey);
						rrp = RedisUtil.getFromDbIndex(redisKey, namespace.trim());

					}
					queryRedisResult(rrp, resultText, dbIndexText, expireTimeText);

				}
			}

		});
		// 查询结果
		resultText = new Text(container,
				SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		resultText.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		resultText.setBounds(10, 241, 764, 254);

		Label namespaceLab = new Label(container, SWT.NONE);
		namespaceLab.setAlignment(SWT.RIGHT);
		namespaceLab.setBounds(211, 18, 61, 17);
		namespaceLab.setText("命名空间：");

		Label prefixLab = new Label(container, SWT.NONE);
		prefixLab.setAlignment(SWT.RIGHT);
		prefixLab.setBounds(211, 83, 61, 17);
		prefixLab.setText("前缀：");

		Label lblKey = new Label(container, SWT.NONE);
		lblKey.setAlignment(SWT.RIGHT);
		lblKey.setBounds(210, 113, 61, 17);
		lblKey.setText("查找数据：");
		Hyperlink prefixLink = formToolkit.createHyperlink(container, "新增模板", SWT.NONE);
		prefixLink.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		prefixLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		prefixLink.addHyperlinkListener(new IHyperlinkListener() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				TemplateDialog dialog = new TemplateDialog(redisQtWindow.getShell(), CacheConstant.PREFIX);
				dialog.open();
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {
			}

			@Override
			public void linkExited(HyperlinkEvent e) {
			}
		});
		prefixLink.setBounds(455, 83, 50, 19);
		formToolkit.paintBordersFor(prefixLink);

		Hyperlink namespaceLink = formToolkit.createHyperlink(container, "新增模板", SWT.NONE);
		namespaceLink.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		namespaceLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		namespaceLink.addHyperlinkListener(new IHyperlinkListener() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				TemplateDialog dialog = new TemplateDialog(redisQtWindow.getShell(), CacheConstant.NAMESPACE);
				dialog.open();
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {

			}

			@Override
			public void linkExited(HyperlinkEvent e) {

			}
		});
		namespaceLink.setBounds(455, 21, 50, 19);
		formToolkit.paintBordersFor(namespaceLink);

		Button button_check = new Button(container, SWT.CHECK);
		button_check.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		button_check.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (button_check.getSelection()) {
					Constants.setIgnore(true);
				} else {
					Constants.setIgnore(false);
					button_check.setSelection(false);
				}

			}
		});
		button_check.setBounds(279, 141, 129, 17);
		formToolkit.adapt(button_check, true, true);
		button_check.setText("忽略命名空间和前缀");

		expireTimeText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		expireTimeText.setEnabled(false);
		expireTimeText.setBounds(220, 212, 110, 23);
		formToolkit.adapt(expireTimeText, true, true);

		dbIndexText = formToolkit.createText(container, "New Text", SWT.READ_ONLY);
		dbIndexText.setText("");
		dbIndexText.setEnabled(false);
		dbIndexText.setEditable(true);
		dbIndexText.setBounds(77, 212, 51, 23);

		Label label_4 = formToolkit.createLabel(container, "键：", SWT.NONE);
		label_4.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		label_4.setAlignment(SWT.RIGHT);
		label_4.setBounds(332, 213, 35, 17);

		fullKeyText = formToolkit.createText(container, "New Text", SWT.READ_ONLY | SWT.MULTI);
		fullKeyText.setText("");
		fullKeyText.setBounds(373, 212, 401, 23);

		Label lblNewLabel = formToolkit.createLabel(container, "生存时间(s)：", SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel.setBounds(134, 213, 80, 17);

		Label label_3 = formToolkit.createLabel(container, "数据库：", SWT.NONE);
		label_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(10, 213, 61, 17);

		Label label_2 = formToolkit.createLabel(container, "连接符号：", SWT.NONE);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		label_2.setBounds(211, 49, 61, 17);

		Button btnkey = new Button(container, SWT.CHECK);
		btnkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnkey.getSelection()) {
					Constants.setQueryLike(true);
				} else {
					Constants.setQueryLike(false);
					btnkey.setSelection(false);
				}

			}
		});
		btnkey.setText("模糊查询 格式：*key*");
		btnkey.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		btnkey.setBounds(414, 141, 147, 17);
		formToolkit.adapt(btnkey, true, true);

		Button btnNewButton_1 = new Button(container, SWT.NONE);
		btnNewButton_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setClipboardString(resultText.getText());
			}
		});
		btnNewButton_1.setBounds(694, 179, 80, 27);
		formToolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("复制到剪贴板");

		Button btnNewButton_2 = new Button(container, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (rrp != null && rrp.getResult() != null) {
					String value = String.valueOf(rrp.getResult());
					if (value.indexOf("{") != -1) {
						String json = JSON.toJSONString(value);
						resultText.setText(JSONFormatUtil.jsonFormat(json));
					} else {
						resultText.setText(String.valueOf(value));
					}
				}
			}
		});
		btnNewButton_2.setBounds(608, 179, 80, 27);
		formToolkit.adapt(btnNewButton_2, true, true);
		btnNewButton_2.setText("JSON格式化");

		resultLabelNums = new Label(container, SWT.NONE);
		resultLabelNums.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		resultLabelNums.setBounds(694, 156, 75, 17);
		formToolkit.adapt(resultLabelNums, true, true);

		Label label = new Label(container, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		label.setBounds(627, 156, 61, 17);
		formToolkit.adapt(label, true, true);
		label.setText("结果数量：");
		return container;
	}

	/**
	 * Create the menu manager.
	 *
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();
		server = new MenuManager("服务器");
		server.add(serviceLinkAction);
		menuBar.add(server);
		return menuBar;
	}

	/**
	 * Create the status line manager.
	 *
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		host = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_HOST);
		port = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_PORT);
		dbIndex = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_DB);
		statusLineManager.setMessage(host);
		return statusLineManager;
	}

	/**
	 * Create the toolbar manager.
	 *
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(800, 633);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		serviceLinkAction = new ServiceLinkAction();
	}

	private boolean queryRedisResult(RedisResultPo result, Text resultText, Text dbIndexText, Text expireTimeText) {
		if (result == null || result.getResult() == null) {
			resultText.setText("没数据");
		} else {
			Long expireTime = result.getExpireTime();
			if (expireTime == null || expireTime == -1) {
				if (expireTime == null || "List".equals(result.getType())) {
					expireTimeText.setText("--");
				} else {
					expireTimeText.setText("永久");
				}

			} else {
				expireTimeText.setText(String.valueOf(expireTime));
			}
			dbIndexText.setText(String.valueOf(result.getDbIndex()));
			if (result != null) {
				if ("List".equals(result.getType())) {
					@SuppressWarnings("unchecked")
					List<Object> list = (List<Object>) result.getResult();
					resultLabelNums.setText(list.size() + "");
				} else {
					resultLabelNums.setText("1");
				}
				resultText.setText(result.getResult().toString());
			}
		}
		if (StringUtils.isNotBlank(host)) {
			RedisConnectMsg redisConnectMsg = CacheConstant.redisConnectMsgMap.get(host);
			if (redisConnectMsg != null) {
				String msg = redisConnectMsg.getResultMsg() + "【" + host + ":" + port + "】 ";
				if (dbIndex != null) {
					msg += (" db：" + dbIndex);
				}
				RedisClientWindow.getRedisClientWindow().getStatusLineManager().setMessage(msg);
			}
		}
		return true;
	}
}
