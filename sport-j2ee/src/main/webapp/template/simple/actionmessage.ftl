
<#if (actionMessages?exists && actionMessages?size > 0)>
	<ul class="actionMessages">
		<#list actionMessages as message>
			<li<#rt/>
<#if parameters.cssClass?exists>
 class="${parameters.cssClass?html} "<#rt/>
<#else>
 class="actionMessage"<#rt/>
</#if>
<#if parameters.cssStyle?exists>
 style="${parameters.cssStyle?html}"<#rt/>
</#if>
>${message}</li>
		</#list>
	</ul>
</#if>