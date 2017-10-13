package procyk.industries.mpbot;

public class Message {
	private String message;
	private String sender;
	private long timeStamp;
	public Message(String sender, String message){
		this.setMessage(message);
		this.setSender(sender);
		timeStamp = System.currentTimeMillis();
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTimeStamp(){
		return timeStamp;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
}
