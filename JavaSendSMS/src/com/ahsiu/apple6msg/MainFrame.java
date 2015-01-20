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
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.ahsiu.apple6msg.server.Client;
import com.ahsiu.apple6msg.server.SMSListener;
import com.ahsiu.apple6msg.server.ServerClient;
import com.ahsiu.apple6msg.server.moduls.ClientModul;
import com.google.gson.Gson;

public class MainFrame extends JFrame implements ActionListener, SMSListener {
	JPanel panel;
	private JLabel labelIp;
	private JLabel labelSms;
	private JComboBox<Client> jcbClient;
	private JTextField tfSMS;
	private JButton btnOK;
	private JButton btnExit;
	private JLabel labelStatus, labelStuResult;
	private JTextField smsContent;
	JPanel ipPanel;
	private Clipboard clipbd = getToolkit().getSystemClipboard();
	private Gson gson = new Gson();
	private ServerClient server;

	public MainFrame(ServerClient server) {
		this.server = server;
		init();
		this.setLocation(400, 300);
		this.setSize(430, 180);
		this.setTitle("iphone6短信自动发送器");
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
	}

	JTextField tfText;

	public void addComBoxItem() {

		List<Client> lists = server.getClients();
		jcbClient.removeAllItems();
		for (Client c : lists) {
			jcbClient.addItem(c);
		}

	}

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
		jcbClient = new JComboBox<Client>();

		ipPanel.add(labelIp, BorderLayout.WEST);
		ipPanel.add(jcbClient, BorderLayout.EAST);

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
		tfSMS.setText("64500366");
		tfSMS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					tfSMS.setText("0085264500366");
				} else {
					tfSMS.setText("64500366");
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
		new MainFrame(new ServerClient());
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
			Msg msg = new Msg();
			msg.setType(3);
			msg.setData(gson.toJson(data));
			server.sendMsg((Client) jcbClient.getSelectedItem(), msg);
		} else if (e.getActionCommand().equals("取消")) {
			dispose();
		}
	}

	ClientInterface clientInterface = new ClientInterface() {

		@Override
		public void write(Msg msg) {

		}

		@Override
		public void read(Msg msg) {
			switch (msg.getType()) {
			case 3:
				labelStuResult.setText("读取成功，请双击短信复制");
				String smsData=msg.getData();
				if(smsData.length()>12){
					smsData=smsData.substring(12,smsData.length()-1);
				}
				smsContent.setText(smsData);
				break;
			default:
				labelStuResult.setText(msg.getData());
				break;
			}
		}

		@Override
		public void clientConnected(ClientModul clientModul) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void received(SMSData sms) {
		labelStuResult.setText("读取成功，请双击短信复制");
		String smsData=sms.getContent();
		if(smsData.length()>5){
			smsData=smsData.substring(smsData.length()-12);
		}
		smsContent.setText(smsData);
	}

}
