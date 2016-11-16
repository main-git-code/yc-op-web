package com.ai.yc.op.web.constant;



public final class Constants {
	private Constants() {

	}
	public static final String TENANT_ID = "yeecloud";
	/** 订单typeCode */
	public static final String TYPE_CODE = "ORD_ORDER";
	/** 订单状态paramCode */
	public static final String ORD_STATE = "STATE";
	/** 订单类型*/
	public static final String ORDER_TYPE = "ORDER_TYPE";
	/** 订单来源*/
	public static final String ORD_CHL_ID = "CHL_ID";
	/** 订单级别*/
	public static final String ORD_ORDER_LEVEL = "ORDER_LEVEL";
	/** 支付方式*/
	public static final String ORD_PAY_STYLE = "PAY_STYLE";
	/** 翻译类型*/
	public static final String ORD_TRANSLATE_TYPE = "TRANSLATE_TYPE";
	/** 翻译级别*/
	public static final String ORD_TRANSLATE_LEVEL = "TRANSLATE_LEVEL";
	/** 美元*/
	public static final String CURRENCY_UNIT_S = "2";
	/** 人民币*/
	public static final String CURRENCY_UNIT_R = "1";
	/** 中文站*/
	public static final String ZH_LANGE = "zh_CN";
	/** 英文站*/
	public static final String EN_LANGE = "1";
	
	public static final class ExcelConstants{
    	private ExcelConstants(){}
    	
    	/**excel导出最大行数**/
    	public static final String EXCEL_OUTPUT_MAX_ROW = "/excel_output_max_row";
    }
	public static final class State{
		private State(){}
		/**
		 * 已取消
		 */
		public static final String CANCEL_STATE = "91";
		/**
		 * 待报价
		 */
		public static final String WAIT_PRICE_STATE = "13";
		/**
		 * 待支付
		 */
		public static final String WAIT_PAY_STATE = "11";
		/**
		 * 待领取
		 */
		public static final String UN_CLAIM_STATE = "20";
		/**
		 * 翻译中
		 */
		public static final String TRANSLTING_STATE = "23";
		/**
		 * 待确认
		 */
		public static final String TBC_STATE = "50";
		/**
		 * 修改中
		 */
		public static final String UPDATING_STATE = "25";
		/**
		 * 已完成
		 */
		public static final String DONE_STATE = "90";
		
		
	}
	public static final class CancelType{
		private CancelType(){}
		/** 系统操作编码*/
		public static final String SYSTEM_OPER = "1000011";
		/** 系统操作名称*/
		public static final String SYSTEM_OPER_NAME = "系统取消";
		/** 用户操作名称*/
		public static final String USER_OPER_NAME = "用户取消";
		
	}
	
}
