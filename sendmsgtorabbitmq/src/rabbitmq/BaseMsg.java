package rabbitmq;

public class BaseMsg {
	// ip
	public String rabbitmqIp;
	// port
	public String rabbitmqPort;

	public String userName;
	public String password;

	public String queueName;

	// account
	public String rabbitmqAccount;
	// times
	public String rabbitmqTimes;
	// is msg type
	public boolean rabbitmqIsDefault;
	// msg
	public String rabbitmqMessage;
	// startid
	public String rabbitmqStartId;

	public String getRabbitmqIp() {
		return rabbitmqIp;
	}

	public void setRabbitmqIp(String rabbitmqIp) {
		this.rabbitmqIp = rabbitmqIp;
	}

	public String getRabbitmqPort() {
		return rabbitmqPort;
	}

	public void setRabbitmqPort(String rabbitmqPort) {
		this.rabbitmqPort = rabbitmqPort;
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

	public String getRabbitmqAccount() {
		return rabbitmqAccount;
	}

	public void setRabbitmqAccount(String rabbitmqAccount) {
		this.rabbitmqAccount = rabbitmqAccount;
	}

	public String getRabbitmqTimes() {
		return rabbitmqTimes;
	}

	public void setRabbitmqTimes(String rabbitmqTimes) {
		this.rabbitmqTimes = rabbitmqTimes;
	}

	public boolean isRabbitmqIsDefault() {
		return rabbitmqIsDefault;
	}

	public void setRabbitmqIsDefault(boolean rabbitmqIsDefault) {
		this.rabbitmqIsDefault = rabbitmqIsDefault;
	}

	public String getRabbitmqMessage() {
		return rabbitmqMessage;
	}

	public void setRabbitmqMessage(String rabbitmqMessage) {
		this.rabbitmqMessage = rabbitmqMessage;
	}

	public String getRabbitmqStartId() {
		return rabbitmqStartId;
	}

	public void setRabbitmqStartId(String rabbitmqStartId) {
		this.rabbitmqStartId = rabbitmqStartId;
	}

	@Override
	public String toString() {
		return "BaseMsg [rabbitmqIp=" + rabbitmqIp + ", rabbitmqPort=" + rabbitmqPort + ", userName=" + userName
				+ ", password=" + password + ", queueName=" + queueName + ", rabbitmqAccount=" + rabbitmqAccount
				+ ", rabbitmqTimes=" + rabbitmqTimes + ", rabbitmqIsDefault=" + rabbitmqIsDefault + ", rabbitmqMessage="
				+ rabbitmqMessage + ", rabbitmqStartId=" + rabbitmqStartId + "]";
	}

}
