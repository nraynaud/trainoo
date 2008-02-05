
<#if (actionMessages?exists && actionMessages?size > 0)>
	<ul class="actionMessages">
		<#list actionMessages as privateMessage>
			<li<#rt/>
<#if parameters.cssClass?exists>
 class="${parameters.cssClass?html} "<#rt/>
<#else>
 class="actionMessage"<#rt/>
</#if>
<#if parameters.cssStyle?exists>
 style="${parameters.cssStyle?html}"<#rt/>
</#if>
>${privateMessage}</li>
		</#list>
	</ul>
</#if>