<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<style type="text/css">
.table-c table {
	border-right: 1px solid;border-bottom: 1px solid;
}
.table-c table td{
	border-left: 1px solid;border-top: 1px solid;
}
</style>
<title>用途列表</title>
<%@include file="/inc/inc.jsp"%>
<input type="hidden" id="sysduadIdUpdate" />
</head>
<body>
	<div class="content-wrapper-iframe">
		<!--右侧灰色背景-->
		<!--框架标签结束-->
		<div class="row">
			<!--外围框架-->
			<div class="col-lg-12">
				<!--删格化-->
				<div class="row">
					<!--内侧框架-->
					<div class="col-lg-12">
						<!--删格化-->
						<div class="main-box clearfix">
							<!--白色背景-->
							<div class="form-label">
								<!--查询条件-->
								<div id="selectDiv">
									<ul>
										<li class="col-md-6">
											<p class="word">语言方向</p>
											<p>
												<select class="select select-medium" id="langugePaire">
													<option value="">全部</option>
												</select>
											</p>
										</li>
										<!-- <li class="col-md-6">
											<p class="word">地区语言</p>
											<p>
												<select class="select select-medium" id="language">
													<option value="" selected="selected">全部</option>
													启用
													<option value="1">中文</option>
													禁用 
													<option value="2">英文</option>
												</select>
											</p>
										</li> -->
										<li class="col-md-6">
											<p class="word">站点</p>
											<p>
												<select class="select select-medium" id="site">
													<option value="" selected="selected">全部</option>
													<!-- pc -->
													<option value="1">译云-中文站</option>
													<!-- app -->
													<option value="2">译云-英文站</option>
													<!-- app -->
													<option value="3">wap-中文</option>
													<!-- app-hd -->
													<option value="4">wap-英文</option>
													<!-- pc -->
													<option value="5">找翻译</option>
													<!-- app -->
													<option value="6">微信助手</option>
												</select>
											</p>
										</li>
										<li class="col-md-6">
											<p class="word">状态</p>
											<p>
												<select class="select select-medium" id="state">
													<option value="" selected="selected">全部</option>
													<!-- 启用 -->
													<option value="0">显示</option>
													<!-- 禁用 -->
													<option value="1">隐藏</option>
												</select>
											</p>
										</li>
										<li class="col-md-6">
											<p class="word">创建人</p>
											<p><input class="int-text int-medium" id="createOperator" value="" type="text"></p>
										</li>
									</ul>
								</div>
								<ul>
									<li class="width-xlag">
										<p class="word">&nbsp;&nbsp;&nbsp;</p>
										<p class="word">
											<input type="button"
												class="biu-btn  btn-primary btn-blue btn-medium ml-5"
												id="search" value="查  询">
										</p>
									</li>
								</ul>
							</div>
							<!--查询结束-->
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<!--外围框架-->
			<div class="col-lg-12">
				<!--删格化-->
				<div class="row">
					<!--内侧框架-->
					<div class="col-lg-12">
						<!--删格化-->
						<div class="main-box clearfix">
							<!--白色背景-->
							<!--标题-->
							<div class="main-box-body clearfix">
								<div class="order-list-table">
									<p class="word">
										<input type="button"
											class="biu-btn  btn-primary btn-blue btn-medium ml-5"
											id="add" value="新 增">
									</p>
								</div>
							</div>
							<!--标题结束-->
							<div class="main-box-body clearfix">
								<!--table表格-->
								<div class="table-responsive clearfix">
									<!--table表格-->
									<div class="table-responsive clearfix mt-10">
										<table class="table table-hover table-border table-bordered ">
											<thead>
												<tr>
													<th>序号</th>
													<th>源语言</th>
													<th>目标语言</th>
													<th>站点</th>
													<th>地区语言</th>
													<th>普通级(￥/$/字词)</th>
													<th>专业级(￥/$/字词)</th>
													<th>出版级(￥/$/字词)</th>
													<th>排序</th>
													<th>创建时间</th>
													<th>状态</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody id="sysDuadListData"></tbody>
										</table>
										<div id="showMessage"></div>
									</div>
									<!--/table表格结束-->
								</div>
								<!--分页-->
								<div>
									<nav style="text-align: center">
										<ul id="pagination"></ul>
									</nav>
								</div>
								<!--分页结束-->
								<!-- 弹出框开始 -->
								<form id="dataForm" method="post">
									<div class="eject-big">
										<div class="eject-medium" id="add-samll"style="overflow:auto;width: 70%; left: 40%; top: 35%;height:500px;">
											<div class="eject-medium-title">
												<p>修改</p><input type="hidden" id="duadId" name="duadId">
												<p class="img" id="colseImage">
													<i class="fa fa-times"></i>
												</p>
											</div>
											<div class="form-label mt-10">
												<!-- <ul>
													<li>
														<p class="word">地区语言</p>
														<p>
															<select class="select select-medium" id="language"
																name="language">
																<option value="1">中文</option>
																<option value="2">英语</option>
															</select>
													</li>
												</ul> -->
												<ul>
													<li>
														<p class="word">站点*</p>
														<p>
															&nbsp;&nbsp; <input type="radio" class="site" name="site"
																value="1" />译云-中文站 <input type="radio" class="site"
																name="site" value="2" />译云-英文站
															<input type="radio" class="site" name="site" value="3" />wap-中文 <br />&nbsp;&nbsp;
															<input type="radio" class="site" name="site" value="4" />wap-英文
															<input type="radio" class="site" name="site" value="5" />找翻译
															<input type="radio" class="site" name="site" value="6" />微信助手
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">源语言中文*</p>
														<p>
															<input type="text" id="sourceCn" name="sourceCn" class="int-text int-small">
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">源语言英文*</p>
														<p>
															<input type="text" id="sourceEn" name="sourceEn" class="int-text int-small">
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">源语言编码*</p>
														<p>
															<input type="text" id="sourceCode" name="sourceCode" class="int-text int-small">
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">目标语言中文*:</p>
														<p>
															<input type="text" id="targetCn" name="targetCn" class="int-text int-small">
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">目标语言英文*:</p>
														<p>
															<input type="text" id="targetEn" name="targetEn" class="int-text int-small">
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">目标语言编码*:</p>
														<p>
															<input type="text" id="targetCode" name="targetCode" class="int-text int-small">
														</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">地区语言</p>
														<p>
															<select class="select select-medium" id="orderType" name="orderType">
																<option value="0">快速翻译</option>
																<option value="1">文档翻译</option>
																<option value="2">口译</option>
															</select>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">价格*(￥/字词)</p>
														<div class="table-c" style="float: right;">
															<table width="400px;" style="float: right;">
																<tr>
																	<td>翻译级别</td>
																	<td>翻译价格</td>
																	<td>加急翻译价格</td>
																</tr>
																<tr>
																	<td>普通级</td>
																	<td><input type="text" name="ordinary"
																		id="ordinary"
																		style="border: 1px solid; height: 30px; margin: 2px;"></td>
																	<td><input type="text" name="ordinaryUrgent"
																		id="ordinaryUrgent"
																		style="border: 1px solid; height: 30px; margin: 2px;"></td>
																</tr>
																<tr>
																	<td>专业级</td>
																	<td><input type="text" name="professional"
																		id="professional"
																		style="border: 1px solid; height: 30px; margin: 2px;"></td>
																	<td><input type="text" name="professionalUrgent"
																		id="professionalUrgent"
																		style="border: 1px solid; height: 30px; margin: 2px;"></td>
																</tr>
																<tr>
																	<td>出版级</td>
																	<td><input type="text" name="publish" id="publish"
																		style="border: 1px solid; height: 30px; margin: 2px;"></td>
																	<td><input type="text" name="publishUrgent"
																		id="publishUrgent"
																		style="border: 1px solid; height: 30px; margin: 2px;"></td>
																</tr>
															</table>
														</div>
													</li>
												</ul>
												<ul>
													<li>
													    <p class="word">价格*($/字词)</p>
													    <div class="table-c" style="float: right;">
															<table width="400px;" style="float: right;">
																<tr>
																	<td>翻译级别</td>
																	<td>翻译价格</td>
																	<td>加急翻译价格</td>
																</tr>
																<tr>
																	<td>普通级</td>
																	<td><input type="text" id="ordinaryDollar" name="ordinaryDollar" style="border: 1px solid;height: 30px;margin: 2px;"></td>
																	<td><input type="text" id="ourgentDollar" name="ourgentDollar" style="border: 1px solid;height: 30px;margin: 2px;"></td>
																</tr>
																<tr>
																	<td>专业级</td>
																	<td><input type="text" id="professionalDollar" name="professionalDollar" style="border: 1px solid;height: 30px;margin: 2px;"></td>
																	<td><input type="text" id="purgentDollar" name="purgentDollar" style="border: 1px solid;height: 30px;margin: 2px;"></td>
																</tr>
																<tr>
																	<td>出版级</td>
																	<td><input type="text" id="publishDollar" name="publishDollar" style="border: 1px solid;height: 30px;margin: 2px;"></td>
																	<td><input type="text" id="puburgentDollar" name="puburgentDollar" style="border: 1px solid;height: 30px;margin: 2px;"></td>
																</tr>
															</table>
														</div>
													</li>
												 </ul>
												<ul>
													<li>
														<p class="word">排序:</p>
														<p>
															<input type="text" id="sort" name="sort"
																class="int-text int-small">
														</p>
														<p>数字越大越靠前</p>
													</li>
												</ul>
												<ul>
													<li>
														<p class="word">状态:</p>
														<p>
															<input name="state" type="radio" value="0" />显示 <input
																name="state" type="radio" value="1" />隐藏
														</p>
													</li>
												</ul>
											</div>
											<!--按钮-->
											<div class="row mt-15">
												<!--删格化-->
												<p class="center pr-30 mt-30">
													<input type="button" id="update"
														class="biu-btn  btn-primary  btn-auto  ml-5" value="提  交">
													<input id="add-close" type="button"
														class="biu-btn  btn-primary  btn-auto  ml-5 edit-close"
														value="取  消">
												</p>
											</div>
										</div>
										<div class="mask" id="eject-mask"></div>
									</div>
								</form>
								<!-- 弹出框结束 -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script id="sysDuadListTemple" type="text/template">
	<tr>
  	  <td>{{:duadId}}</td>
      <td>{{:sourceCn}}</td>
	  <td>{{:targetCn}}</td>
	  <td>
		  {{if  site == '1'}}
		     译云-中文站
		  {{else site == '2'}}
		    译云-英文站
		  {{else site == '3'}}
		  wap-中文
		  {{else site == '4'}}
		  wap-英文
		  {{else site == '5'}}
		      找翻译
		  {{else site == '6'}}
		      微信助手
		  {{/if}}
	  </td>      
      <td>
		 {{if  language == '1'}}
		  	中文
		  {{else language == '2'}}
		  	英语
		  {{/if}}
	  </td>
	  <td>
		普通￥{{:ordinary}}/\${{:ordinaryDollar}}<br/>
		加急￥{{:ordinaryUrgent}}/\${{:ourgentDollar}}
	  </td>
	  <td>
		普通￥{{:professional}}/\${{:professionalDollar}}<br/>
		加急￥{{:professionalUrgent}}/\${{:purgentDollar}}
	  </td>
	  <td>
		普通￥{{:publish}}/\${{:publishDollar}}<br/>
		加急￥{{:publishUrgent}}/\${{:puburgentDollar}}
	  </td>
	  <td>{{:sort}}</td>
	  <td>
			{{:~timestampToDate('yyyy-MM-dd hh:mm:ss', updatetime)}}
	  </td>
	  <td>
		  {{if  state == '0'}}
		  	显示
		  {{else state == '1'}}
		  	隐藏
		  {{/if}}
	  </td>     
	  <td>
 		  <a href="javascript:void(0);" class="adopt" onclick="pager._delete('{{:duadId}}')">查看</a>
		  <a href="javascript:void(0);" class="adopt" onclick="pager._show('{{:duadId}}','{{:site}}','{{:sourceCn}}','{{:sourceEn}}','{{:sourceCode}}','{{:targetCn)}}','{{:targetEn)}}','{{:targetCode)}}','{{:orderType)}}','{{:ordinary}}','{{:ordinaryUrgent}}','{{:professional}}','{{:professionalUrgent}}','{{:publish}}','{{:publishUrgent}}','{{:sort}}','{{:state}}','{{:ordinaryDollar}}','{{:ourgentDollar}}','{{:professionalDollar}}','{{:purgentDollar}}','{{:publishDollar}}','{{:puburgentDollar}}')">修改</a>
	  </td>
    </tr>
</script>
	<script type="text/javascript">
		var pager;
		(function() {
			seajs.use('app/jsp/sysduad/sysDuadList', function(OrderListPager) {
				pager = new OrderListPager({element : document.body});
				pager.render();
			});
		})();
	</script>
</body>