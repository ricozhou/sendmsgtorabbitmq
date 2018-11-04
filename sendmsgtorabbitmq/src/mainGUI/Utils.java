package mainGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import rabbitmq.BaseMsg;
import rabbitmq.rabbitmqMsg;

public class Utils {

	// basecheck
	public boolean checkBase1(BaseMsg baseMsg, int i) {
		// not null ""
		if ("".equals(baseMsg.getRabbitmqIp()) || "".equals(baseMsg.getRabbitmqPort())
				|| "".equals(baseMsg.getUserName()) || "".equals(baseMsg.getPassword())) {
			return false;
		}
		if (i == 0) {
			if ("".equals(baseMsg.getRabbitmqAccount()) || "".equals(baseMsg.getRabbitmqTimes())
					|| "".equals(baseMsg.getQueueName()) || "".equals(baseMsg.getRabbitmqStartId())) {
				return false;
			}
		}

		return true;
	}

	public boolean checkBase2(BaseMsg baseMsg, int i) {
		// only number
		if (!baseMsg.getRabbitmqPort().matches("^[0-9]*$")) {
			return false;
		}
		if (i == 0) {
			if (!baseMsg.getRabbitmqAccount().matches("^[0-9]*$") || !baseMsg.getRabbitmqTimes().matches("^[0-9]*$")
					|| !baseMsg.getRabbitmqStartId().matches("^[0-9]*$")) {
				return false;
			}
		}
		return true;
	}

	// checkip
	public boolean checkIp(String ip) {
		// 定义正则表达式
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		if (ip.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkAccTimeId(BaseMsg baseMsg) {
		if (baseMsg.getRabbitmqAccount().startsWith("0") || baseMsg.getRabbitmqTimes().startsWith("0")) {
			return false;
		}
		if (baseMsg.getRabbitmqAccount().length() > 9 || baseMsg.getRabbitmqTimes().length() > 9
				|| baseMsg.getRabbitmqStartId().length() > 9) {
			return false;
		}
		return true;
	}

	public boolean checkMsg(String rabbitmqMessage) {
		if ("".equals(rabbitmqMessage)) {
			return false;
		}
		if (!checkJson(rabbitmqMessage)) {
			return false;
		}
		return true;
	}

	private boolean checkJson(String rabbitmqMessage) {
		try {
			JSONObject jsonStr = JSONObject.parseObject(rabbitmqMessage);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public BaseMsg getDefaultMsg(File f, int flag) throws Exception {
		Properties pro = new Properties();
		// InputStream inputStream =
		// this.getClass().getResourceAsStream("rabbitmqmodel.properties");
		// RandomAccessFile ff = new RandomAccessFile(new
		// File("rabbitmqlog20180329012249.txt"), "rw");
		FileInputStream fis = new FileInputStream(f);
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		BaseMsg bm = new BaseMsg();
		bm.setRabbitmqIp(pro.getProperty("ip"));
		bm.setRabbitmqPort(pro.getProperty("port"));
		bm.setUserName(pro.getProperty("username"));
		bm.setPassword(pro.getProperty("password"));
		bm.setQueueName(pro.getProperty("queuename"));
		// 0表示发送
		if (flag == 0) {
			bm.setRabbitmqAccount(pro.getProperty("account"));
			bm.setRabbitmqTimes(pro.getProperty("times"));
			bm.setRabbitmqMessage(formatJson(pro.getProperty("msg")));
			bm.setRabbitmqStartId(pro.getProperty("startid"));
			if (pro.getProperty("msgisdefault").equals("0")) {
				bm.setRabbitmqIsDefault(true);
			} else {
				bm.setRabbitmqIsDefault(false);
			}
		}
		fis.close();
		bf.close();
		return bm;
	}

	public boolean BaseMsgToPro(BaseMsg baseMsg3, int flag) throws Exception {
		File f;
		String content = "";
		if (flag == 0) {
			f = new File("sendrabbitmqmodel.properties");
			content = "ip=" + baseMsg3.getRabbitmqIp() + "\r\nport=" + baseMsg3.getRabbitmqPort() + "\r\nusername="
					+ baseMsg3.getUserName() + "\r\npassword=" + baseMsg3.getPassword() + "\r\naccount="
					+ baseMsg3.getRabbitmqAccount() + "\r\ntimes=" + baseMsg3.getRabbitmqTimes() + "\r\nmsgisdefault="
					+ (baseMsg3.isRabbitmqIsDefault() ? "0" : "1") + "\r\nmsg="
					+ baseMsg3.getRabbitmqMessage().replaceAll("\n", "") + "\r\nqueuename=" + baseMsg3.getQueueName()
					+ "\r\nstartid=" + baseMsg3.getRabbitmqStartId();
		} else {
			f = new File("consumerrabbitmqmodel.properties");
			content = "ip=" + baseMsg3.getRabbitmqIp() + "\r\nport=" + baseMsg3.getRabbitmqPort() + "\r\nusername="
					+ baseMsg3.getUserName() + "\r\npassword=" + baseMsg3.getPassword() + "\r\nqueuename="
					+ baseMsg3.getQueueName();
		}
		// 先删除隐藏文件再重新创建，隐藏文件不支持修改
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);// 创建文件输出流对象
			// 设置文件的隐藏属性
			String set = "attrib +H " + f.getAbsolutePath();
			Runtime.getRuntime().exec(set);
			// 将字符串写入到文件中
			fos.write(content.getBytes());
			return true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public boolean isFileExit(File file) {
		if (file.exists()) {
			return true;
		}
		return false;
	}

	// public void createHiddFile(File f, int flag) throws IOException {
	// String content = "";
	// if (flag == 0) {
	// content =
	// "ip=192.168.1.144\r\nport=9092\r\ngroupid=groupid\r\ntopic=test1\r\naccount=10\r\ntimes=1000\r\nmsgisdefault=0\r\nmsg=\r\nkey=key\r\nstartid=1";
	// } else {
	// content =
	// "ip=192.168.1.144\r\nport=9092\r\ngroupid=groupid\r\ntopic=test1";
	// }
	// FileOutputStream fos = null;
	// try {
	// fos = new FileOutputStream(f);// 创建文件输出流对象
	// // 设置文件的隐藏属性
	// String set = "attrib +H " + f.getAbsolutePath();
	// Runtime.getRuntime().exec(set);
	// // 将字符串写入到文件中
	// fos.write(content.getBytes());
	// } catch (FileNotFoundException e1) {
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// } finally {
	// try {
	// fos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }

	// format jsonstring
	public String formatJson(String content) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		int count = 0;
		while (index < content.length()) {
			char ch = content.charAt(index);
			if (ch == '{' || ch == '[') {
				sb.append(ch);
				sb.append('\n');
				count++;
				for (int i = 0; i < count; i++) {
					sb.append('\t');
				}
			} else if (ch == '}' || ch == ']') {
				sb.append('\n');
				count--;
				for (int i = 0; i < count; i++) {
					sb.append('\t');
				}
				sb.append(ch);
			} else if (ch == ',') {
				sb.append(ch);
				sb.append('\n');
				for (int i = 0; i < count; i++) {
					sb.append('\t');
				}
			} else {
				sb.append(ch);
			}
			index++;
		}
		return sb.toString();
	}

	// json变一行
	public static String compactJson(String content) {
		String regEx = "[\t\n]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(content);
		return m.replaceAll("").trim();
	}

}
