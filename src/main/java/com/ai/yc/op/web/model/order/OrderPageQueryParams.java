package com.ai.yc.op.web.model.order;

import com.ai.yc.order.api.orderquery.param.QueryOrderRequest;

public class OrderPageQueryParams extends QueryOrderRequest {

	private static final long serialVersionUID = 1L;

	private String orderTimeS;
	private String orderTimeE;
	private String payTimeS;
	private String payTimeE;
	private String orderPageId;
	private String langungePaire;
	public String getOrderTimeS() {
		return orderTimeS;
	}

	public void setOrderTimeS(String orderTimeS) {
		this.orderTimeS = orderTimeS;
	}

	public String getOrderTimeE() {
		return orderTimeE;
	}

	public void setOrderTimeE(String orderTimeE) {
		this.orderTimeE = orderTimeE;
	}

	public String getPayTimeS() {
		return payTimeS;
	}

	public void setPayTimeS(String payTimeS) {
		this.payTimeS = payTimeS;
	}

	public String getPayTimeE() {
		return payTimeE;
	}

	public void setPayTimeE(String payTimeE) {
		this.payTimeE = payTimeE;
	}

	public String getOrderPageId() {
		return orderPageId;
	}

	public void setOrderPageId(String orderPageId) {
		this.orderPageId = orderPageId;
	}

	public String getLangungePaire() {
		return langungePaire;
	}

	public void setLangungePaire(String langungePaire) {
		this.langungePaire = langungePaire;
	}

}
