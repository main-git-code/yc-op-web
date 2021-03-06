define('app/jsp/order/tbcOrderList', function (require, exports, module) {
    'use strict';
    var $=require('jquery'),
    Widget = require('arale-widget/1.2.0/widget'),
    Dialog = require("optDialog/src/dialog"),
    Paging = require('paging/0.0.1/paging-debug'),
    AjaxController = require('opt-ajax/1.0.0/index');
    require("jsviews/jsrender.min");
    require("jsviews/jsviews.min");
    require("bootstrap-paginator/bootstrap-paginator.min");
    require("app/util/jsviews-ext");
    
    require("jquery-validation/1.15.1/jquery.validate");
	require("app/util/aiopt-validate-ext");
    
    require("opt-paging/aiopt.pagination");
    require("twbs-pagination/jquery.twbsPagination.min");
    require('bootstrap/js/modal');
    var SendMessageUtil = require("app/util/sendMessage");
    //实例化AJAX控制处理对象
    var ajaxController = new AjaxController();
    //定义页面组件类
    var TbcOrdListPager = Widget.extend({
    	
    	Implements:SendMessageUtil,
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	Statics: {
    		DEFAULT_PAGE_SIZE: 10
    	},
    	//事件代理
    	events: {
    		 "click #showQuery":"_showQueryInfo",
    		//查询
            "click #search":"_searchOrderList",
            "click #edit-close":"_closeDialog",
            "click #colseImage":"_closeDialog",
            "click #export":"_export",
            "click #update":"_returnWork"
            
        },
    	//重写父类
    	setup: function () {
    		TbcOrdListPager.superclass.setup.call(this);
    		// 初始化执行搜索
    		this._searchOrderList();
    		this._bindChlIdSelect();
    		this._bindOrdTypeSelect();
    		this._bindLanguageSelect();
    		var formValidator=this._initValidate();
			$(":input").bind("focusout",function(){
				formValidator.element(this);
			});
    	},
    	_showQueryInfo: function(){
			//展示查询条件
			var info= $("#selectDiv").is(":hidden"); //是否隐藏
		    if(info==true){
		    	$("#selectDiv").show();
		    }else{
		    	$("#selectDiv").hide();
		    }
		},
		// 下拉 语种方向
		_bindLanguageSelect:function() {
			var this_=this;
			$.ajax({
				type : "post",
				processing : false,
				url : _base+ "/getLangugeSelect",
				dataType : "json",
				data : {
				},
				message : "正在加载数据..",
				success : function(data) {
					var d=data.data;
					$.each(d,function(index,item){
						var langugeName = d[index].transTypeName+"->"+d[index].sourceCn+"->"+d[index].targetCn;
						var langugeCode = d[index].duadId;
						$("#langugePaire").append('<option value="'+langugeCode+'">'+langugeName+'</option>');
					})
				}
			});
		},
		// 下拉订单类型（对应库中的翻译类型）
		_bindOrdTypeSelect:function() {
			var this_=this;
			$.ajax({
				type : "post",
				processing : false,
				url : _base+ "/getSelect",
				dataType : "json",
				data : {
					paramCode:"TRANSLATE_TYPE",
					typeCode:"ORD_ORDER"
				},
				message : "正在加载数据..",
				success : function(data) {
					var d=data.data;
					$.each(d,function(index,item){
						var paramName = d[index].columnDesc;
						var paramCode = d[index].columnValue;
						$("#orderType").append('<option value="'+paramCode+'">'+paramName+'</option>');
					})
				}
			});
		},
		// 下拉 订单来源
		_bindChlIdSelect : function() {
			var this_=this;
			$.ajax({
				type : "post",
				processing : false,
				url : _base+ "/getSelect",
				dataType : "json",
				data : {
					paramCode:"CHL_ID",
					typeCode:"ORD_ORDER"
				},
				message : "正在加载数据..",
				success : function(data) {
					var d=data.data;
					$.each(d,function(index,item){
						var paramName = d[index].columnDesc;
						var paramCode = d[index].columnValue;
						$("#orderSource").append('<option value="'+paramCode+'">'+paramName+'</option>');
					})
				}
			});
		},
		_export:function(){
			var _this=this;
			var orderTimeS=jQuery.trim($("#orderTimeBegin").val());
			var orderTimeE=jQuery.trim($("#orderTimeEnd").val());
			var submitTimeS=jQuery.trim($("#stateTimeBegin").val());
			var submitTimeE=jQuery.trim($("#stateTimeEnd").val());
			if(orderTimeS=="" || orderTimeS==null){
				orderTimeS="";
			}else{
				orderTimeS= new Date( Date.parse( $("#orderTimeBegin").val().replace(/-/g,"/")) ).getTime();
			}
			if(orderTimeE=="" || orderTimeE==null){
				orderTimeE="";
			}else{
				orderTimeE= new Date( Date.parse( $("#orderTimeEnd").val().replace(/-/g,"/")) ).getTime();
			}
			//提交时间
			if(submitTimeS=="" || submitTimeS==null){
				submitTimeS="";
			}else{
				submitTimeS= new Date( Date.parse( $("#stateTimeBegin").val().replace(/-/g,"/")) ).getTime();
			}
			if(submitTimeE=="" || submitTimeE==null){
				submitTimeE="";
			}else{
				submitTimeE= new Date( Date.parse( $("#stateTimeEnd").val().replace(/-/g,"/")) ).getTime();
			}
			var userName=jQuery.trim($("#nickName").val());
			var chlId=jQuery.trim($("#orderSource option:selected").val());
			var translateType=jQuery.trim($("#orderType option:selected").val());
			var langungePaire=jQuery.trim($("#langugePaire option:selected").val());
			var orderPageId=jQuery.trim($("#orderId").val());
			var interperName = jQuery.trim($("#interperName").val());
			var url = _base+'/tbcExport?orderTimeS='+orderTimeS+'&orderTimeE='+orderTimeE+'&submitTimeS='+submitTimeS+
			'&userName='+userName+'&chlId='+chlId+'&translateType='+translateType+'&langungePaire='+langungePaire+
		    '&orderPageId='+orderPageId+'&submitTimeE='+submitTimeE+'&interperName='+interperName+"&offset="+today.stdTimezoneOffset();
			window.location.href=encodeURI(url);
		},
		_searchOrderList:function(){
			var _this=this;
			var url = _base+"/getTbcOrdData";
			var queryData = this._getSearchParams();
			$("#pagination").runnerPagination({
				url:url,
				method: "POST",
				dataType: "json",
				messageId:"showMessage",
				renderId:"orderListData",
				data : queryData,
				pageSize: TbcOrdListPager.DEFAULT_PAGE_SIZE,
				visiblePages:5,
				message: "正在为您查询数据..",
				render: function (data) {
					if(data&&data.length>0){
						var template = $.templates("#orderListTemple");
						var htmlOut = template.render(data);
						$("#orderListData").html(htmlOut);
					}else{
						$("#orderListData").html("未搜索到信息");
					}
				},
			});
		},
	
		_getSearchParams:function(){
			var orderTimeS = $("#orderTimeBegin").val();
			var orderTimeE = $("#orderTimeEnd").val();
			var submitTimeS =  $("#stateTimeBegin").val();
			var submitTimeE =  $("#stateTimeEnd").val();
			if(orderTimeS=="" || orderTimeS==null){
				orderTimeS=null;
			}else{
				orderTimeS= new Date( Date.parse( $("#orderTimeBegin").val().replace(/-/g,"/")) ).getTime();
			}
			if(orderTimeE=="" || orderTimeE==null){
				orderTimeE="";
			}else{
				orderTimeE= new Date( Date.parse( $("#orderTimeEnd").val().replace(/-/g,"/")) ).getTime();
			}
			//提交时间
			if(submitTimeS=="" || submitTimeS==null){
				submitTimeS="";
			}else{
				submitTimeS= new Date( Date.parse( $("#stateTimeBegin").val().replace(/-/g,"/")) ).getTime();
			}
			if(submitTimeE=="" || submitTimeE==null){
				submitTimeE="";
			}else{
				submitTimeE= new Date( Date.parse( $("#stateTimeEnd").val().replace(/-/g,"/")) ).getTime();
			}
    		return {
    			"orderTimeS":orderTimeS,
    			"orderTimeE":orderTimeE,
    			"submitTimeS":submitTimeS,
    			"submitTimeE":submitTimeE,
    			"userName":jQuery.trim($("#nickName").val()),
    			"interperName":jQuery.trim($("#interperName").val()),
    			"chlId":jQuery.trim($("#orderSource option:selected").val()),
    			"translateType":jQuery.trim($("#orderType option:selected").val()),
    			"langungePaire":jQuery.trim($("#langugePaire option:selected").val()),
    			"orderPageId":jQuery.trim($("#orderId").val())
    		}
    	},
    	_detailPage:function(orderId){
			window.location.href = _base+"/order/orderdetails?orderId="
            + orderId+'&mod=edit'+"&random="+Math.random();
    	},
    	_rejectReviewOrder:function(orderId){
			var _this = this;
			var param={};
			param.orderId=orderId;
			var d = Dialog({
				content:'<textarea id="reasonDesc" style="width:200px;" class="int-text"  maxlength="100"></textarea>',
				padding: 0,
				okValue: '确认',
				title: '退款原因:',
				ok:function(){
					param.busiType='2';
					param.reasonDesc = $("#reasonDesc").val();
					param.state = '40';
					param.displayFlag='40';
					_this._handReviewOrder(param);
				},
				cancelValue: '取消',
			    cancel: function () {}
			});
			d.showModal();
		},
    	_handReviewOrder:function(param){
			var _this=this;
			ajaxController.ajax({
				type: "post",
				processing: true,
				message: "保存数据中，请等待...",
				url: _base + "/refundApply",
				data: param,
				success: function (data) {
					window.location.href=_base+"/toTbcOrderList";
				}
			});
		},
		//弹出框
    	_popUp:function(orderId,nickName,time,confirmTime){
    		var _this= this;
    		$("#orderIdShow").val("");
    		$("#nickNameShow").val("");
    		$("#remaingTimeShow").val("");
    		$("#remainingTimePageShow").val("");
			//弹出框展示
			$('#eject-mask').fadeIn(100);
			$('#edit-medium').slideDown(200);
			$("#orderIdShow").val(orderId);
    		$("#nickNameShow").val(nickName);
    		$("#remaingTimeShow").val(time);
    		$("#remainingTimePageShow").val(confirmTime);
    	},
    	_closeDialog:function(){
    		$("#hourShow-error").html("");
    		$("#dayShow-error").html("");
    		$("#remark-error").html("");
    		$('#eject-mask').fadeOut(100);
    		$('#edit-medium').slideUp(150);
    	},
    	_initValidate:function(){
    		var formValidator=$("#dataForm").validate({
    			rules: {
					remark: {
    					required: true,
    					maxlength:100
    				},
    				dayShow:{
    					required: true,
    					regexp: /^[0-9]*[1-9][0-9]*$/,
    				},
    				hourShow: {
    					required:true,
    					regexp: /^[0-9]*[1-9][0-9]*$/,
    					max:24
    				}
    			},
    			messages: {
    				remark: {
    					required: "请输入备注!",
    					maxlength:"最大长度不能超过{0}"
    				},
    				dayShow: {
    					required: "请输入需耗天!",
    					regexp:"输入格式不正确"
    				},
    				hourShow: {
    					required: "请输入需耗时!",
    					regexp:"输入格式不正确",
    					max:"最大值为24"
    				}
    			}
    		});
    		return formValidator;
    	},
    	_returnWork:function(){
    		var _this= this;
    		var formValidator=_this._initValidate();
			formValidator.form();
			if(!$("#dataForm").valid()){
				return false;
			}
    		var orderId = $("#orderIdShow").val();
    		var dayShow = $("#dayShow").val();
    		var remak = $("#remark").val();
    		var hourShow=$("#hourShow").val();
    		$.ajax({
				type : "post",
				processing : false,
				url : _base+ "/returnWork",
				dataType : "json",
				data : {
					orderId:orderId,
					remark:remak,
					takeDay:dayShow,
					takeTime:hourShow
				},
				message : "正在加载数据..",
				success : function(data) {
					if(data.statusCode==1){
						//跳到列表页面
						window.location.href=_base+"/toTbcOrderList?random="+Math.random();
					}else{
						var d = Dialog({
							title: '消息',
							content:"修改失败",
							icon:'prompt',
							okValue: '确 定',
							ok:function(){
								this.close();
							}
						});
						d.show();
					}
				}
			});
    	}
		
    });
    
    module.exports = TbcOrdListPager
});

