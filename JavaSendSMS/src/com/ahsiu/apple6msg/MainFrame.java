package com.ahsiu.apple6msg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.gson.Gson;

public class MainFrame extends JFrame implements ActionListener {
	JPanel panel;
	private JLabel labelIp;
	private JLabel labelSms;
	private JTextField tfIpAddress;
	private JTextField tfSMS;
	private JButton btnOK;
	private JButton btnExit;
	private JButton btnConnect;
	private JLabel labelStatus, labelStuResult;
	private JTextField smsContent;
	JPanel ipPanel;
	private Clipboard clipbd = getToolkit().getSystemClipboard();
	private Gson gson = new Gson();

	public MainFrame() {
		init();
		this.setLocation(400, 300);
		this.setSize(430, 180);
		this.setTitle("iphone6短信自动发送器");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	JTextField tfText;

	private void init() {
		this.setLayout(new GridLayout(5, 1));
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		labelStatus = new JLabel("状态：");
		labelStuResult = new JLabel("waiting");
		statusPanel.add(labelStatus, BorderLayout.WEST);
		statusPanel.add(labelStuResult);

		JPanel p1 = new JPanel(new GridLayout(1, 2));
		ipPanel = new JPanel();
		ipPanel.setLayout(new BorderLayout());
		ipPanel.setMaximumSize(new Dimension(getWidth(), 10));
		labelIp = new JLabel("手机IP:");
		tfIpAddress = new JTextField(10);
		tfIpAddress.setText("192.168.1.2");
		btnConnect = new JButton();
		btnConnect.setText("连接");
		btnConnect.addActionListener(this);

		ipPanel.add(labelIp, BorderLayout.WEST);
		ipPanel.add(tfIpAddress);
		ipPanel.add(btnConnect, BorderLayout.EAST);

		JPanel copyPanel = new JPanel(new BorderLayout());
		final JTextField mobileTxt = new JTextField(7);
		mobileTxt.setText("W93956295");
		mobileTxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mobileTxt.selectAll();
				StringSelection clipString = new StringSelection(mobileTxt
						.getText());
				clipbd.setContents(clipString, clipString);
				super.mouseClicked(e);

			}
		});
		copyPanel.add(mobileTxt, BorderLayout.WEST);
		final JTextField txz = new JTextField(8);
		txz.setText("56027685");
		txz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mobileTxt.selectAll();
				StringSelection clipString = new StringSelection(txz.getText());
				clipbd.setContents(clipString, clipString);
				super.mouseClicked(e);

			}
		});
		copyPanel.add(txz, BorderLayout.EAST);

		p1.add(ipPanel);
		p1.add(copyPanel, BorderLayout.EAST);

		JPanel p2 = new JPanel();

		labelSms = new JLabel("apple服务号码:");
		tfSMS = new JTextField(10);
		tfSMS.setText("0085264500366");
		tfSMS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					tfSMS.setText("64500366");
				} else {
					tfSMS.setText("0085264500366");
				}
				super.mouseClicked(e);

			}
		});
		;
		JLabel labelText = new JLabel("验证码：");
		tfText = new JTextField(10);
		p2.add(labelSms, BorderLayout.WEST);
		p2.add(tfSMS);
		p2.add(labelText, BorderLayout.CENTER);
		p2.add(tfText, BorderLayout.EAST);
		tfText.addMouseListener(mouseAdp);

		JPanel p3 = new JPanel();
		btnOK = new JButton("发送短信");
		btnOK.addActionListener(this);
		btnExit = new JButton("取消");
		btnExit.addActionListener(this);
		p3.add(btnOK);
		p3.add(btnExit);

		JPanel p4 = new JPanel();
		JLabel label = new JLabel("读取短信内容（双击复制）:");
		smsContent = new JTextField(16);
		smsContent.addMouseListener(copySms);
		p4.add(label, BorderLayout.WEST);
		p4.add(smsContent);

		this.add(statusPanel, BorderLayout.NORTH);
		this.add(p1, BorderLayout.CENTER);
		this.add(p2, BorderLayout.CENTER);
		this.add(p4, BorderLayout.CENTER);
		this.add(p3, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
		new MainFrame();
	}

	private MouseAdapter mouseAdp = new MouseAdapter() {
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getClickCount() == 2) {
				/*
				 * StringSelection clipString = new StringSelection(
				 * tfText.getText()); clipbd.setContents(clipString,
				 * clipString);
				 */
				Transferable clipData = clipbd.getContents(MainFrame.this);
				String clipString = "";
				try {
					clipString = (String) clipData
							.getTransferData(DataFlavor.stringFlavor);
				} catch (UnsupportedFlavorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tfText.setText(clipString);

			}
		};
	};

	private MouseAdapter copySms = new MouseAdapter() {
		public void mousePressed(java.awt.event.MouseEvent e) {
			 
			if (e.getClickCount() == 2) {
				StringSelection clipString = new StringSelection(
						smsContent.getSelectedText());
				clipbd.setContents(clipString, clipString);
			}
		};
	};
	MyClient client;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("发送短信")) {
			// JOptionPane.showMessageDialog(
			// this,
			// "您的用户名为" + tfIpAddress.getText() + "n" + "你的密码为"
			// + String.valueOf(tfSMS.getText()));
			String phone = tfSMS.getText();
			Transferable clipData = clipbd.getContents(MainFrame.this);
			String content = tfText.getText();

			try {
				String temp = (String) clipData
						.getTransferData(DataFlavor.stringFlavor);
				if (content != null && !"".equals(content)) {
					// 用输入框中的
				} else {
					content = temp;
				}
			} catch (UnsupportedFlavorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			tfText.setText(content);
			SMSData data = new SMSData();
			data.setPhoneNum(phone);
			data.setContent(content);
			Message msg = new Message();
			msg.setType(2);
			msg.setData(gson.toJson(data));
			client.write(msg);
		} else if (e.getActionCommand().equals("取消")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("连接")) {
			String ip = tfIpAddress.getText();
			if (ip == null && "".equals(ip)) {
				ip = "192.168.1.2";
			}
			client = new MyClient(ip);
			client.setClientInterface(clientInterface);
			client.getConnect();
			Message msg = new Message();
			msg.setType(1);
			msg.setData("你好,连接成功，准备就绪");
			client.write(msg);
		}
	}

	ClientInterface clientInterface = new ClientInterface() {

		@Override
		public void write(Message msg) {

		}

		@Override
		public void read(Message msg) {
			switch (msg.getType()) {
			case 3:
				labelStuResult.setText("读取成功，请双击短信复制");
				smsContent.setText(msg.getData());
				break;
			default:
				labelStuResult.setText(msg.getData());
				break;
			}
		}
	};

}
