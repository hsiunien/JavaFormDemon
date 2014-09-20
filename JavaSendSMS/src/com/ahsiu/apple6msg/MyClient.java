package com.ahsiu.apple6msg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MyClient {
	private String host;
	private int port = 9988;
	private ClientInterface client;
	private boolean released;
	private Gson gson;

	MyClient(String host) {
		this.host = host;
		gson = new Gson();

	}

	public void setClientInterface(ClientInterface clientInterface) {
		this.client = clientInterface;
	}

	public void write(Message msg) {
		if (!released && os != null) {
			try {
				System.out.println("发送的：" + gson.toJson(msg));
				os.write(gson.toJson(msg).getBytes());
				os.flush();
				/*
				 * int len = is.available(); byte[] buffer = new byte[len];
				 * is.read(buffer); String jsonStr = new String(buffer);
				 * System.out.println("接收的：" + jsonStr + "  lenth=" + len);
				 * Message cMsg = gson.fromJson(jsonStr, Message.class);
				 * client.read(cMsg);
				 */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				released = true;
			}

		}
	}

	OutputStream os;
	InputStream is;

	public void getConnect() {
		Socket socket;
		try {
			socket = new Socket(host, port);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			new Thread(new ReadDataThread(is)).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client.read(new Message(-1, "连接失败"));
			released = true;
		} catch (Exception e) {
			e.printStackTrace();
			released = true;
			client.read(new Message(-1, "连接失败"));
		}
	}

	class ReadDataThread implements Runnable {
		InputStream is;

		ReadDataThread(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			int len = -1;
			StringBuffer sb = new StringBuffer();
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while (!released) {
				buffer = new byte[32];
				try {
					Thread.sleep(50);
					System.out.println(is.available());
					while ((len = is.read()) != -1) {
						System.out.println("len=" + len + " available"
								+ is.available());
						bao.write(len);
						if (is.available() != 0) {
							buffer = new byte[is.available()];
							is.read(buffer, 0, is.available());
							bao.write(buffer);
							sb.append(bao.toString());
							bao.reset();
							System.out.println("收到数据:" + sb.toString());
							String jsonStr = sb.toString();
							System.out.println("接收的：" + jsonStr + "  lenth="
									+ len);
							Message cMsg = new Message();
							try {
								cMsg = gson.fromJson(jsonStr, Message.class);
							} catch (JsonSyntaxException e) {
								e.printStackTrace();
								cMsg.setData(jsonStr);

							}
							client.read(cMsg);
							sb.setLength(0);
						}
					}
					released = true;
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	// ByteArrayOutputStream bao = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int len = -1;
	// while ((len = is.read(buffer, 0, buffer.length)) != -1) {
	// bao.write(buffer, 0, len);
	// }
}
