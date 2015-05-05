
<#include "public/top.ftl"/>


<div class="z_main">
<div class="z_login">
	<div class="z_logintitle">
        <a href="${ctx}">
        <dl>
            <dt><img src="${ctx}/resources/images/login/login.jpg" width="84" height="84"></dt>
            <dd><span>云票票网</span><em>ypiaopiao.com</em></dd>
        </dl>
        </a>
        <div class="clearfix"></div>
    </div>
    <div class="z_loginmain" style="height:499px;">
        <form action="${ctx}/doLogin${suffix}" method="POST" name="loginForm">
            <div class="z_loginmain_left" style="height:450px;position: relative;">
                <h3>登陆云票票网</h3>
                <#if errorMsg??>
					<div class="hr hr-dotted"></div>
					<div class="text-danger center" style="position:absolute;left:200px;top:67px; color:#f00; font-size:18px;"><i class="icon-remove bigger-110 red"></i>&nbsp;&nbsp;${errorMsg}</div>
				</#if>
                <ul>
                	<li><span>邮箱/手机:</span><input name="accout" type="text" class="z_input1" /></li>
                    <li><span>登陆 密码:</span><input name="pwd" type="password" class="z_input1" /></li>
                    <li><span></span><input type="checkbox" checked /><label>记住密码</label></li>
                    <li><span></span><input type="submit" value="登陆云票票网" class="z_input2" /></li>
                    <!--
                    	<li class="z_li1"><p>使用合作网站账号登陆云票票网</p></li>
                    <li class="z_li1"><span></span><a href="#" class="z_a1">新浪微博</a><a href="#" class="z_a2">QQ</a><a href="#" class="z_a3">360</a></li>
                		-->
                </ul>
            </div>
            <div class="z_loginmain_right" style="height:450px;">
                <h3>我还不是云票票网用户</h3>
                <p>注册即享受在线选座，轻松购票！参加丰富多彩的特价活动！</p>
                <p>注册得积分，抵票价！</p>
                <a href="${ctx}/register${suffix}">立即注册</a>
            </div>
        </form>
        <div class="clearfix"></div>
    </div>
</div>	
<#include "public/login_register_foot.ftl"/>