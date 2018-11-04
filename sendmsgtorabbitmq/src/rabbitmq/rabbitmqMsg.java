package rabbitmq;

public class rabbitmqMsg {
	// ip:port
	public String ipPort;
	public String userName;
	public String password;

	public String queueName;
	// account
	public int account;
	// time
	public int time;
	// msg
	public String message;
	// startid
	public int startId;

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStartId() {
		return startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@Override
	public String toString() {
		return "rabbitmqMsg [ipPort=" + ipPort + ", userName=" + userName + ", password=" + password + ", queueName="
				+ queueName + ", account=" + account + ", time=" + time + ", message=" + message + ", startId="
				+ startId + "]";
	}

}
