package rabbitmq;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import mainGUI.RabbitmqMainGUI;

public class SendMsgRabbitmq {
	public static int TIMES = 0;
	public ReturnMsg rm = new ReturnMsg();

	// 处理参数
	public ReturnMsg rabbitmqController(BaseMsg baseMsg) throws Exception {
		rabbitmqMsg rabbitmqMsg = new rabbitmqMsg();
		handleParams(baseMsg, rabbitmqMsg);

		// send
		ReturnMsg returnMsg = sendMsg(rabbitmqMsg, baseMsg);
		return returnMsg;

	}

	private void handleParams(BaseMsg baseMsg, rabbitmqMsg rabbitmqMsg) {
		rabbitmqMsg.setIpPort(baseMsg.getRabbitmqIp() + ":" + baseMsg.getRabbitmqPort());
		rabbitmqMsg.setUserName(baseMsg.getUserName());
		rabbitmqMsg.setPassword(baseMsg.getPassword());
		rabbitmqMsg.setQueueName(baseMsg.getQueueName());
		rabbitmqMsg.setAccount(Integer.valueOf(baseMsg.getRabbitmqAccount()));
		rabbitmqMsg.setTime(Integer.valueOf(baseMsg.getRabbitmqTimes()));
		rabbitmqMsg.setStartId(Integer.valueOf(baseMsg.getRabbitmqStartId()));

		if (baseMsg.isRabbitmqIsDefault()) {
			String msgJson = "{\"name\":\"testName\",\"age\":1,\"address\":\"address\",\"otherMsg\":\"msgtest\"}";
			rabbitmqMsg.setMessage(msgJson);
		} else {
			rabbitmqMsg.setMessage(baseMsg.getRabbitmqMessage());
		}
	}

	// send
	public ReturnMsg sendMsg(rabbitmqMsg rabbitmqMsg, BaseMsg baseMsg) throws IOException, Exception {
		PrintStream out = null;
		PrintStream mytxt = null;
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddhhmmss");
			mytxt = new PrintStream("./rabbitmqlog" + sd.format(new Date()) + ".txt");
			out = System.out;
			System.setOut(mytxt);
			System.out.println("Start：");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		TIMES = 0;

		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();

		// 设置RabbitMQ相关信息
		factory.setHost(baseMsg.getRabbitmqIp());
		factory.setUsername(baseMsg.getUserName());
		factory.setPassword(baseMsg.getPassword());
		factory.setPort(Integer.valueOf(baseMsg.getRabbitmqPort()));

		// 创建一个新的连接
		Connection connection = factory.newConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();

		// 声明一个队列
		channel.queueDeclare(rabbitmqMsg.getQueueName(), false, false, false, null);

		ObjectMapper objectMapper = new ObjectMapper();
		int i = 0;
		int acc = rabbitmqMsg.getAccount();
		while (i < acc) {
			JSONObject data = new JSONObject();

			// handlemessage
			String msg = hanleMsg(rabbitmqMsg, i, acc);

			JsonNode jsonNode = objectMapper.readTree(msg);
			// 发送消息到队列中
			channel.basicPublish("", rabbitmqMsg.getQueueName(),
					new AMQP.BasicProperties.Builder().contentType("application/json").build(),
					jsonNode.toString().getBytes());

			System.out.println(jsonNode);
			Thread.sleep(rabbitmqMsg.getTime());
			i++;
			TIMES++;
			rm.setComTimes(TIMES);
			if (!RabbitmqMainGUI.isRunning) {
				rm.setCompleted(false);
				System.setOut(out);
				mytxt.close();
				// 关闭通道和连接
				channel.close();
				connection.close();
				return rm;
			}
		}
		rm.setCompleted(true);
		System.out.println("End");
		System.setOut(out);
		mytxt.close();
		// 关闭通道和连接
		channel.close();
		connection.close();
		return rm;

	}

	// handlemsg
	private String hanleMsg(rabbitmqMsg rabbitmqMsg, int i, int acc) throws IOException {
		com.alibaba.fastjson.JSONObject json = JSON.parseObject(rabbitmqMsg.getMessage());
		// 只发送一条信息则不做更改
		if (acc != 1) {
			for (Entry<String, Object> entry : json.entrySet()) {
				if ("age".equals(entry.getKey())) {
					int age = Integer.valueOf(entry.getValue().toString()) + i;
					entry.setValue(age);
				} else {
					entry.setValue(entry.getValue().toString() + i);
				}
			}
		}
		String s = json.toString();
		int idn = i + rabbitmqMsg.getStartId();
		s = "{" + "\"id\":" + idn + "," + s.substring(1);
		return s;
	}
}
