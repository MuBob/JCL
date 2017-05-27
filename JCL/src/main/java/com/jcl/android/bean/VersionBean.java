package com.jcl.android.bean;


public class VersionBean extends BaseBean {
	
	private Data data;
	
	public Data getData() {
		if(data!=null)
			return data;
		else 
			return null;
	}

	public void setData(Data data) {
		if(data!=null)
			this.data=data;;
	}

	public class Data{
		
		private String _id;
		private String content;
		private String createtime;
		private String filename;
		private String versionname;
		private String path;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		public String getVersionname() {
			return versionname;
		}
		public void setVersionname(String versionname) {
			this.versionname = versionname;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		
		
		

	}

}
