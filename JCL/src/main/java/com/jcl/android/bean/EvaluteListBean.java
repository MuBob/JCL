package com.jcl.android.bean;

import java.io.Serializable;
import java.util.List;

public class EvaluteListBean extends BaseBean {

	private Data data;

	public List<EvaluteInfo> getData() {
		return data != null ? data.getEvalutes() : null;
	}

	public void setData(List<EvaluteInfo> data) {
		this.data.setOthers(data);
		;
	}

	public class Data {
		private List<EvaluteInfo> evalutes;

		public List<EvaluteInfo> getEvalutes() {
			return evalutes;
		}

		public void setOthers(List<EvaluteInfo> evalutes) {
			this.evalutes = evalutes;
		}

	}

	public class EvaluteInfo implements Serializable {

		private String _id;
		private String evalutescore;// 分数
		private String evaluteremark;// 评价
		private String name;//评价人
		private String createdate;//时间
		
		
		
		public String getCreatedate() {
			return createdate;
		}
		public void setCreatedate(String createdate) {
			this.createdate = createdate;
		}
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getEvalutescore() {
			return evalutescore;
		}
		public void setEvalutescore(String evalutescore) {
			this.evalutescore = evalutescore;
		}
		public String getEvaluteremark() {
			return evaluteremark;
		}
		public void setEvaluteremark(String evaluteremark) {
			this.evaluteremark = evaluteremark;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}

}

