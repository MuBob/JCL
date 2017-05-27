package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;


public class MessageBean extends BaseBean {

	private Data data;

	public List<MessageInfo> getData() {
		return data != null ? data.getMessages() : null;
	}

	public void setData(List<MessageInfo> data) {
		this.data.setMessages(data);
	}

	public class Data {
		private List<MessageInfo> messages;

		public List<MessageInfo> getMessages() {
			return messages;
		}

		public void setMessages(List<MessageInfo> messages) {
			this.messages = messages;
		}

	}

	public class MessageInfo implements Serializable {

		private String _id;
		private String title;// 标题
		private String content;// 内容
		private String sender;// 发送者
		private String receiver;// 接收者
		private String isread ;// 是否已读 　 0.未读 1.已读
		private String createdate;// 发送时间
		private int type;
		private String bizid;
		
		public String getBizid() {
			return bizid;
		}
		public void setBizid(String bizid) {
			this.bizid = bizid;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getIsread() {
			return isread;
		}
		public void setIsread(String isread) {
			this.isread = isread;
		}
		public String getCreatedate() {
			return createdate;
		}
		public void setCreatedate(String createdate) {
			this.createdate = createdate;
		}
		
	}
	
	public class IdInfo implements Serializable{
		private String date;
		private String inc;
		private String machine;
		private String time;
		private String timeSecond;
		private String timestamp;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getInc() {
			return inc;
		}
		public void setInc(String inc) {
			this.inc = inc;
		}
		public String getMachine() {
			return machine;
		}
		public void setMachine(String machine) {
			this.machine = machine;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTimeSecond() {
			return timeSecond;
		}
		public void setTimeSecond(String timeSecond) {
			this.timeSecond = timeSecond;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		
	}
	
}

