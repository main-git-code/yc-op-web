package com.ai.yc.op.web.controller.order;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.vo.PageInfo;
import com.ai.opt.sdk.components.ccs.CCSClientFactory;
import com.ai.opt.sdk.components.excel.client.AbstractExcelHelper;
import com.ai.opt.sdk.components.excel.factory.ExcelFactory;
import com.ai.opt.sdk.dubbo.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.BeanUtils;
import com.ai.opt.sdk.util.CollectionUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.paas.ipaas.ccs.IConfigClient;
import com.ai.yc.common.api.cache.interfaces.ICacheSV;
import com.ai.yc.common.api.cache.param.SysParam;
import com.ai.yc.common.api.cache.param.SysParamSingleCond;
import com.ai.yc.op.web.constant.Constants;
import com.ai.yc.op.web.constant.Constants.ExcelConstants;
import com.ai.yc.op.web.model.order.ExAllOrder;
import com.ai.yc.op.web.model.order.OrderPageQueryParams;
import com.ai.yc.op.web.model.order.OrderPageResParam;
import com.ai.yc.op.web.utils.AmountUtil;
import com.ai.yc.order.api.orderquery.interfaces.IOrderQuerySV;
import com.ai.yc.order.api.orderquery.param.OrdOrderVo;
import com.ai.yc.order.api.orderquery.param.QueryOrderRequest;
import com.ai.yc.order.api.orderquery.param.QueryOrderRsponse;

@Controller
public class UpdatingOrdListController {
private static final Logger logger = Logger.getLogger(UpdatingOrdListController.class);
	
	@RequestMapping("/toUpdateOrderList")
	public ModelAndView toAlertOrder(HttpServletRequest request) {
		return new ModelAndView("jsp/order/updatingOrderList");
	}
	/**
     * 订单信息查询
     */
    @RequestMapping("/getUpdatingOrdData")
    @ResponseBody
    public ResponseData<PageInfo<OrderPageResParam>> getList(HttpServletRequest request,OrderPageQueryParams queryRequest){
    	ResponseData<PageInfo<OrderPageResParam>> responseData = null;
    	List<OrderPageResParam> resultList = new ArrayList<OrderPageResParam>();
    	PageInfo<OrderPageResParam> resultPageInfo  = new PageInfo<OrderPageResParam>();
	    try{
	    	QueryOrderRequest ordReq = new QueryOrderRequest();
	    	BeanUtils.copyProperties(ordReq, queryRequest);
	    	String pgeOrderId = queryRequest.getOrderPageId();
	    	if(!StringUtil.isBlank(pgeOrderId)) {
				boolean isNum = pgeOrderId.matches("[0-9]+");
				if(isNum) {
					ordReq.setOrderId(Long.parseLong(pgeOrderId));
				}else {
					ordReq.setOrderId(0l);
				}
			}
	    	Long orderTimeBegin = queryRequest.getOrderTimeS();
			if (orderTimeBegin!=null) {
				Timestamp orderTimeS = new Timestamp(orderTimeBegin);
				ordReq.setOrderTimeStart(orderTimeS);
			}
			Long orderTimeEnd = queryRequest.getOrderTimeE();
			if (orderTimeEnd!=null) {
				Timestamp orderTimeE = new Timestamp(orderTimeEnd);
				ordReq.setOrderTimeEnd(orderTimeE);
			}
			//领取时间
			Long lockTimeBegin = queryRequest.getLockTimeS();
			if (lockTimeBegin!=null) {
				Timestamp lockTimeS = new Timestamp(lockTimeBegin);
				ordReq.setLockTimeStart(lockTimeS);
			}
			Long lockTimeEnd = queryRequest.getLockTimeE();
			if (lockTimeEnd!=null) {
				Timestamp lockTimeE = new Timestamp(lockTimeEnd);
				ordReq.setLockTimeEnd(lockTimeE);
			}
			ordReq.setState(Constants.State.UPDATING_STATE);
			String strPageNo=(null==request.getParameter("pageNo"))?"1":request.getParameter("pageNo");
		    String strPageSize=(null==request.getParameter("pageSize"))?"10":request.getParameter("pageSize");
		    ordReq.setPageNo(Integer.parseInt(strPageNo));
		    ordReq.setPageSize(Integer.parseInt(strPageSize));
		    IOrderQuerySV orderQuerySV = DubboConsumerFactory.getService(IOrderQuerySV.class);
		    ICacheSV iCacheSV = DubboConsumerFactory.getService(ICacheSV.class);
		    QueryOrderRsponse orderListResponse = orderQuerySV.queryOrder(ordReq);
			if (orderListResponse != null && orderListResponse.getResponseHeader().isSuccess()) {
				PageInfo<OrdOrderVo> pageInfo = orderListResponse.getPageInfo();
				BeanUtils.copyProperties(resultPageInfo, pageInfo);
				List<OrdOrderVo> orderList = pageInfo.getResult();
				if(!CollectionUtil.isEmpty(orderList)){
					for(OrdOrderVo vo:orderList){
						OrderPageResParam resParam = new OrderPageResParam();
						BeanUtils.copyProperties(resParam, vo);
						//翻译订单来源
    					SysParamSingleCond	paramCond = new SysParamSingleCond();
    					paramCond.setTenantId(Constants.TENANT_ID);
    					paramCond.setColumnValue(vo.getChlId());
    					paramCond.setTypeCode(Constants.TYPE_CODE);
    					paramCond.setParamCode(Constants.ORD_CHL_ID);
                		SysParam chldParam = iCacheSV.getSysParamSingle(paramCond);
                		if(chldParam!=null){
                			resParam.setChlIdPage(chldParam.getColumnDesc());
                		}
                		//翻译翻译类型
                		paramCond = new SysParamSingleCond();
                		paramCond.setTenantId(Constants.TENANT_ID);
                		paramCond.setColumnValue(vo.getTranslateType());
        				paramCond.setTypeCode(Constants.TYPE_CODE);
        				paramCond.setParamCode(Constants.ORD_TRANSLATE_TYPE);
                		SysParam orderTypeParam = iCacheSV.getSysParamSingle(paramCond);
                		if(orderTypeParam!=null){
                			resParam.setTranslateTypePage(orderTypeParam.getColumnDesc());
                		}
                		//翻译订单状态
                		paramCond = new SysParamSingleCond();
                		paramCond.setTenantId(Constants.TENANT_ID);
    					paramCond.setColumnValue(vo.getState());
    					paramCond.setTypeCode(Constants.TYPE_CODE);
    					paramCond.setParamCode(Constants.ORD_STATE);
                		SysParam stateParam = iCacheSV.getSysParamSingle(paramCond);
                		if(stateParam!=null){
                			resParam.setStatePage(stateParam.getColumnDesc());
                		}
                		//翻译订单级别
                		paramCond = new SysParamSingleCond();
                		paramCond.setTenantId(Constants.TENANT_ID);
    					paramCond.setColumnValue(vo.getOrderLevel());
    					paramCond.setTypeCode(Constants.TYPE_CODE);
    					paramCond.setParamCode(Constants.ORD_ORDER_LEVEL);
                		SysParam levelParam = iCacheSV.getSysParamSingle(paramCond);
                		if(levelParam!=null){
                			resParam.setOrderLevelPage(levelParam.getColumnDesc());
                		}
                		//转换金额格式
                		if(!StringUtil.isBlank(vo.getCurrencyUnit())){
                			if(Constants.CURRENCY_UNIT_S.equals(vo.getCurrencyUnit())){
                				resParam.setTotalFeePage(vo.getTotalFee()+"$");
                			}else{
                				resParam.setTotalFeePage(AmountUtil.LiToYuan(vo.getTotalFee())+"¥");
                			}
                		}
						resultList.add(resParam);
					}
				}
				resultPageInfo.setResult(resultList);
				responseData = new ResponseData<PageInfo<OrderPageResParam>>(ResponseData.AJAX_STATUS_SUCCESS, "查询成功",resultPageInfo);
			} else {
				responseData = new ResponseData<PageInfo<OrderPageResParam>>(ResponseData.AJAX_STATUS_FAILURE, "查询失败", null);
			}
		} catch (Exception e) {
			logger.error("查询修改中订单列表失败：", e);
			responseData = new ResponseData<PageInfo<OrderPageResParam>>(ResponseData.AJAX_STATUS_FAILURE, "查询信息异常", null);
		}
	    return responseData;
    }
    /**
     * 订单信息导出
     */
    @RequestMapping("/updatingExport")
    @ResponseBody
    public void  export(HttpServletRequest request, HttpServletResponse response, OrderPageQueryParams queryRequest) {
    	QueryOrderRequest ordReq = new QueryOrderRequest();
    	BeanUtils.copyProperties(ordReq, queryRequest);
    	String pgeOrderId = queryRequest.getOrderPageId();
    	if(!StringUtil.isBlank(pgeOrderId)) {
			boolean isNum = pgeOrderId.matches("[0-9]+");
			if(isNum) {
				ordReq.setOrderId(Long.parseLong(pgeOrderId));
			}else {
				ordReq.setOrderId(0l);
			}
		}
    	Long orderTimeBegin = queryRequest.getOrderTimeS();
		if (orderTimeBegin!=null) {
			Timestamp orderTimeS = new Timestamp(orderTimeBegin);
			ordReq.setOrderTimeStart(orderTimeS);
		}
		Long orderTimeEnd = queryRequest.getOrderTimeE();
		if (orderTimeEnd!=null) {
			Timestamp orderTimeE = new Timestamp(orderTimeEnd);
			ordReq.setOrderTimeEnd(orderTimeE);
		}
		//领取时间
		Long lockTimeBegin = queryRequest.getLockTimeS();
		if (lockTimeBegin!=null) {
			Timestamp lockTimeS = new Timestamp(lockTimeBegin);
			ordReq.setLockTimeStart(lockTimeS);
		}
		Long lockTimeEnd = queryRequest.getLockTimeE();
		if (lockTimeEnd!=null) {
			Timestamp lockTimeE = new Timestamp(lockTimeEnd);
			ordReq.setLockTimeEnd(lockTimeE);
		}
		ordReq.setState(Constants.State.UPDATING_STATE);
	    ordReq.setPageNo(1);
	    try {
	  //获取配置中的导出最大数值
	    IConfigClient configClient = CCSClientFactory.getDefaultConfigClient();
        String maxRow =  configClient.get(ExcelConstants.EXCEL_OUTPUT_MAX_ROW);
        int excelMaxRow = Integer.valueOf(maxRow);
	    ordReq.setPageSize(excelMaxRow);
	    IOrderQuerySV orderQuerySV = DubboConsumerFactory.getService(IOrderQuerySV.class);
	    ICacheSV iCacheSV = DubboConsumerFactory.getService(ICacheSV.class);
	    QueryOrderRsponse orderListResponse = orderQuerySV.queryOrder(ordReq);
	    PageInfo<OrdOrderVo> pageInfo = orderListResponse.getPageInfo();
		List<OrdOrderVo> orderList = pageInfo.getResult();
		List<ExAllOrder> exportList = new ArrayList<ExAllOrder>();
		if(!CollectionUtil.isEmpty(orderList)){
			for(OrdOrderVo vo:orderList){
				if(!CollectionUtil.isEmpty(vo.getOrdProdExtendList())){
					for(int i=0;i<vo.getOrdProdExtendList().size();i++){
						ExAllOrder exOrder = new ExAllOrder();
						////翻译订单来源
						SysParamSingleCond	paramCond = new SysParamSingleCond();
						paramCond.setTenantId(Constants.TENANT_ID);
						paramCond.setColumnValue(vo.getChlId());
						paramCond.setTypeCode(Constants.TYPE_CODE);
						paramCond.setParamCode(Constants.ORD_CHL_ID);
		        		SysParam chldParam = iCacheSV.getSysParamSingle(paramCond);
		        		if(chldParam!=null){
		        			exOrder.setChlId(chldParam.getColumnDesc());
		        		}
		        		//翻译订单类型
		        		paramCond = new SysParamSingleCond();
		        		paramCond.setTenantId(Constants.TENANT_ID);
		        		paramCond.setColumnValue(vo.getTranslateType());
						paramCond.setTypeCode(Constants.TYPE_CODE);
						paramCond.setParamCode(Constants.ORD_TRANSLATE_TYPE);
		        		SysParam orderTypeParam = iCacheSV.getSysParamSingle(paramCond);
		        		if(orderTypeParam!=null){
		        			exOrder.setOrderType(orderTypeParam.getColumnDesc());
		        		}
		        		//翻译订单状态
		        		paramCond = new SysParamSingleCond();
		        		paramCond.setTenantId(Constants.TENANT_ID);
						paramCond.setColumnValue(vo.getState());
						paramCond.setTypeCode(Constants.TYPE_CODE);
						paramCond.setParamCode(Constants.ORD_STATE);
		        		SysParam stateParam = iCacheSV.getSysParamSingle(paramCond);
		        		if(stateParam!=null){
		        			exOrder.setState(stateParam.getColumnDesc());
		        		}
		        		//翻译订单级别
                		paramCond = new SysParamSingleCond();
                		paramCond.setTenantId(Constants.TENANT_ID);
    					paramCond.setColumnValue(vo.getOrderLevel());
    					paramCond.setTypeCode(Constants.TYPE_CODE);
    					paramCond.setParamCode(Constants.ORD_ORDER_LEVEL);
                		SysParam levelParam = iCacheSV.getSysParamSingle(paramCond);
                		if(levelParam!=null){
                			exOrder.setOrderLevel(levelParam.getColumnDesc());
                		}
		        		//转换金额格式
                		if(!StringUtil.isBlank(vo.getCurrencyUnit())){
                			if(Constants.CURRENCY_UNIT_S.equals(vo.getCurrencyUnit())){
                				exOrder.setRealFee(vo.getTotalFee()+"$");
                				exOrder.setTotalFee(vo.getTotalFee()+"$");
                			}else{
                				exOrder.setRealFee(AmountUtil.LiToYuan(vo.getTotalFee())+"¥");
                				exOrder.setTotalFee(AmountUtil.LiToYuan(vo.getTotalFee())+"¥");
                			}
                		}
                		if(vo.getOrderTime()!=null){
                			exOrder.setOrderTime(vo.getOrderTime().toString());
                		}
		        		exOrder.setUserName(vo.getUserName());
		        		exOrder.setOrderId(vo.getOrderId());
		        		if(vo.getLockTime()!=null){
		        			exOrder.setLockTime(vo.getLockTime().toString());
		        		}
		        		if(vo.getRemainingTime()!=null){
		        			exOrder.setRemaningTime(vo.getRemainingTime().toString());
		        		}
		        		exOrder.setLangire(vo.getOrdProdExtendList().get(i).getLangungePairChName());
		        		exportList.add(exOrder);
					}
				}else{
					ExAllOrder exOrder = new ExAllOrder();
					//翻译订单来源
					SysParamSingleCond	paramCond = new SysParamSingleCond();
					paramCond.setTenantId(Constants.TENANT_ID);
					paramCond.setColumnValue(vo.getChlId());
					paramCond.setTypeCode(Constants.TYPE_CODE);
					paramCond.setParamCode(Constants.ORD_CHL_ID);
	        		SysParam chldParam = iCacheSV.getSysParamSingle(paramCond);
	        		if(chldParam!=null){
	        			exOrder.setChlId(chldParam.getColumnDesc());
	        		}
	        		//翻译订单类型
	        		paramCond = new SysParamSingleCond();
	        		paramCond.setTenantId(Constants.TENANT_ID);
	        		paramCond.setColumnValue(vo.getTranslateType());
					paramCond.setTypeCode(Constants.TYPE_CODE);
					paramCond.setParamCode(Constants.ORD_TRANSLATE_TYPE);
	        		SysParam orderTypeParam = iCacheSV.getSysParamSingle(paramCond);
	        		if(orderTypeParam!=null){
	        			exOrder.setOrderType(orderTypeParam.getColumnDesc());
	        		}
	        		//翻译订单状态
	        		paramCond = new SysParamSingleCond();
	        		paramCond.setTenantId(Constants.TENANT_ID);
					paramCond.setColumnValue(vo.getState());
					paramCond.setTypeCode(Constants.TYPE_CODE);
					paramCond.setParamCode(Constants.ORD_STATE);
	        		SysParam stateParam = iCacheSV.getSysParamSingle(paramCond);
	        		if(stateParam!=null){
	        			exOrder.setState(stateParam.getColumnDesc());
	        		}
	        		//翻译订单级别
            		paramCond = new SysParamSingleCond();
            		paramCond.setTenantId(Constants.TENANT_ID);
					paramCond.setColumnValue(vo.getOrderLevel());
					paramCond.setTypeCode(Constants.TYPE_CODE);
					paramCond.setParamCode(Constants.ORD_ORDER_LEVEL);
            		SysParam levelParam = iCacheSV.getSysParamSingle(paramCond);
            		if(levelParam!=null){
            			exOrder.setOrderLevel(levelParam.getColumnDesc());
            		}
	        		//转换金额格式
            		if(!StringUtil.isBlank(vo.getCurrencyUnit())){
            			if(Constants.CURRENCY_UNIT_S.equals(vo.getCurrencyUnit())){
            				exOrder.setRealFee(vo.getTotalFee()+"$");
            				exOrder.setTotalFee(vo.getTotalFee()+"$");
            			}else{
            				exOrder.setRealFee(AmountUtil.LiToYuan(vo.getTotalFee())+"¥");
            				exOrder.setTotalFee(AmountUtil.LiToYuan(vo.getTotalFee())+"¥");
            			}
            		}
            		if(vo.getOrderTime()!=null){
            			exOrder.setOrderTime(vo.getOrderTime().toString());
            		}
            		if(vo.getLockTime()!=null){
	        			exOrder.setLockTime(vo.getLockTime().toString());
	        		}
	        		if(vo.getRemainingTime()!=null){
	        			exOrder.setRemaningTime(vo.getRemainingTime().toString());
	        		}
	        		exOrder.setUserName(vo.getUserName());
	        		exOrder.setOrderId(vo.getOrderId());
	        		exportList.add(exOrder);
				}
			}
		}
			ServletOutputStream outputStream = response.getOutputStream();
			response.reset();// 清空输出流
            response.setContentType("application/msexcel");// 定义输出类型
            response.setHeader("Content-disposition", "attachment; filename=order"+new Date().getTime()+".xls");// 设定输出文件头
            String[] titles = new String[]{"订单来源", "订单类型", "订单编号", "下单时间", "昵称", "语种方向","订单金额","订单级别","领取时间","修改剩余时间","订单状态"};
    		String[] fieldNames = new String[]{"chlId", "orderType", "orderId", "orderTime",
    				"userName", "langire","totalFee","orderLevel","lockTime","remaningTime","state"};
			 AbstractExcelHelper excelHelper = ExcelFactory.getJxlExcelHelper();
             excelHelper.writeExcel(outputStream, "订单信息"+new Date().getTime(), ExAllOrder.class, exportList,fieldNames, titles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
