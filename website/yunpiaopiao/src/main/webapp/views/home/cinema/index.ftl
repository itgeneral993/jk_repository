<#include "../public/top.ftl"/>
<#include "../public/head.ftl"/>
<!--content begin-->
<div class="content">
	<div class="center clearfix">
		<div class="con-l">
			<div class="y-city clearfix">
				<div class="tab-title-box03">
					<ul class="tab-title float-l">
						<li class="active">选择区域</li>
						<li>选择商圈</li>
						<li>选择地铁</li>
					</ul>
				</div>
				<div class="search-box search-box-small float-r">
					<form class="search search-small" method="get" target="_blank">
						<input type="hidden" value="" name="" />
						<input type="submit" value="" class="search-btn serch-btn-small" />
						<input type="text" name="" onblur="if(this.value==''){this.value=this.defaultValue;}" onfocus="if(this.value==this.defaultValue){this.value='';}"  value="搜索影院名称" class="search-text search-text-small">
					</form>
				</div>
				<div class="tab-con tab-con03 float-l">
					<ul class="thisclass">
						<li class="y-active"><a>全部影院</a></li>
						<li><a>热门影院</a></li>
						<li><a>西湖区（10）</a></li>
						<li><a>拱墅区（20）</a></li>
						<li><a>江干区（6）</a></li>
						<li><a>上城区（2）</a></li>
						<li><a>滨江区（1）</a></li>
						<li><a>下城区（4）</a></li>
						<li><a>余杭区（2）</a></li>
						<li><a>萧山区（1）</a></li>
					</ul>
					<ul>
						<li class="y-active"><a>全部影院</a></li>
						<li><a>热门影院</a></li>
						<li><a>西城广场（）</a></li>
						<li><a>拱墅区（20）</a></li>
						<li><a>江干区（6）</a></li>
						<li><a>上城区（2）</a></li>
						<li><a>滨江区（1）</a></li>
						<li><a>下城区（4）</a></li>
						<li><a>余杭区（2）</a></li>
						<li><a>萧山区（1）</a></li>
					</ul>
					<ul>
						<li class="y-active"><a>全部影院</a></li>
						<li><a>热门影院</a></li>
						<li><a>地铁一号线（）</a></li>
						<li><a>拱墅区（20）</a></li>
						<li><a>江干区（6）</a></li>
						<li><a>上城区（2）</a></li>
						<li><a>滨江区（1）</a></li>
						<li><a>下城区（4）</a></li>
						<li><a>余杭区（2）</a></li>
						<li><a>萧山区（1）</a></li>
					</ul>
				</div>
			</div>
			<!--y-city end-->
			<div class="y-cinema">
					<#list pager.itemList as u>
						<div class="cinema-box">
							<div class="y-cinema-con cinema-current">
								<div class="y-cinema-con-l float-l"><img src="${tagUtils.getFileFullPath(u.icon)}"></div>
								<div class="y-cinema-con-r float-r">
									<div class="y-cinema-tit">
										<a href="${ctx}/cinema/${u.id}/view${suffix}" class="float-l yy_ljx">${u.name}</a>
										<div class="tit-score float-r">
											<span>评分：</span>
											<span class="tit-num">${u.mark}</span>
										</div>
									</div>
									<p><label>详细地址：</label>${u.address}</p>
									<p><label>联系电话：</label>${u.contact}</p>
									<p><label>影院特色：</label>${u.feature}</p>
									<p><label>优势服务：</label>
									<#if tagUtils.logic2(u.supportService,1)><i class="i01" title="支持3D"></i></#if>
									<#if tagUtils.logic2(u.supportService,2)><i class="i02" title="有停车场"></i></#if>
									<#if tagUtils.logic2(u.supportService,4)><i class="i03" title="儿童免费"></i></#if>
									</p>
								</div>
							</div>
						</div>
					</#list>
					
				 <@h.page pager=pager action="${ctx}/cinema${suffix}" />
				<!-- End .pagination -->
			</div>
			<!--y-cinema end-->
		</div>
		<!--con-l end-->
		<div class="con-r">
			<#include "../notice/notice_thumb.ftl"/>
			<!--notice-column end-->
			<div class="hot-column">
				<div class="tab-title-box02">
					<span class="title-blue">热映TOP排行</span>
				</div>
				<ul class="hot-list">
					<li class="hot-fir">
						<a>
							<div class="hot-img-box"><img src="${ctx}/resources/images//demoimg/hot_img.jpg" class="hot-img"></div>
							<dl class="hot-infor">
								<dt>忍者神龟</dt>
								<dd>主演：梅根·福克斯/梅根·福克斯/</dd>
								<dd>类型：动作/科幻</dd>
								<dd>上映时间：10月31日</dd>
							</dl>
						</a>
					</li>
					<li><a><i>2</i>一个人的武林</a><span class="hot-score"><span class="hot-num">7.7</span>分</span></li>
					<li><a><i>3</i>一个人的武林<span class="hot-score"><span class="hot-num">7.7</span>分</span></a></li>
					<li><a><i>4</i>一个人的武林<span class="hot-score"><span class="hot-num">7.7</span>分</span></a></li>
					<li><a><i>5</i>一个人的武林<span class="hot-score"><span class="hot-num">7.7</span>分</span></a></li>
					<li class="last-li"><a><i>6</i>一个人的武林<span class="hot-score"><span class="hot-num">7.7</span>分</span></a></li>
				</ul>
			</div>
			<!--hot-column end-->
			<div class="comment-column">
				<div class="tab-title-box02">
					<span class="title-blue">精彩点评</span>
				</div>
				<ul class="comment-list">
					<li>
						<a>
							<div class="comment-infor">
								<div class="comment-img-box">
									<img src="${ctx}/resources/images//demoimg/hot_img.jpg" class="comment-img">
								</div>
								<span>晃着晃着</span>
							</div>
							<span class="comment-talk">《沉睡魔咒》先吐槽下，在环球港海上国际影城看的，屏幕还是暗，摩尔森林的那些景致</span>
						</a>
					</li>
					<li>
						<a>
							<div class="comment-infor">
								<div class="comment-img-box">
									<img src="${ctx}/resources/images//demoimg/hot_img.jpg" class="comment-img">
								</div>
								<span>晃着晃着</span>
							</div>
							<span class="comment-talk">《沉睡魔咒》先吐槽下，在环球港海上国际影城看的，屏幕还是暗，摩尔森林的那些景致</span>
						</a>
					</li>
					<li>
						<a>
							<div class="comment-infor">
								<div class="comment-img-box">
									<img src="${ctx}/resources/images//demoimg/hot_img.jpg" class="comment-img">
								</div>
								<span>晃着晃着</span>
							</div>
							<span class="comment-talk">《沉睡魔咒》先吐槽下，在环球港海上国际影城看的，屏幕还是暗，摩尔森林的那些景致</span>
						</a>
					</li>
					<li>
						<a>
							<div class="comment-infor">
								<div class="comment-img-box">
									<img src="${ctx}/resources/images//demoimg/hot_img.jpg" class="comment-img">
								</div>
								<span>晃着晃着</span>
							</div>
							<span class="comment-talk">《沉睡魔咒》先吐槽下，在环球港海上国际影城看的，屏幕还是暗，摩尔森林的那些景致</span>
						</a>
					</li>
					<li>
						<a>
							<div class="comment-infor">
								<div class="comment-img-box">
									<img src="${ctx}/resources/images/demoimg/hot_img.jpg" class="comment-img">
								</div>
								<span>晃着晃着</span>
							</div>
							<span class="comment-talk">《沉睡魔咒》先吐槽下，在环球港海上国际影城看的，屏幕还是暗，摩尔森林的那些景致</span>
						</a>
					</li>
				</ul>
			</div>
			<!--comment-column end-->
		</div>
		<!--con-r end-->
	</div>
</div>
<!--content end-->
<#include "../public/foot.ftl"/>