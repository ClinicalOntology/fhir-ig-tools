<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<#include "header.ftl">
</head>
    <body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-secondary">
		<a class="navbar-brand" href="#">Implementation Guide: ${name}</a>

		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarToggler" aria-controls="navbarTogglerDemo02"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
	</nav>
	<div class="container-fluid">
	    <div class="row justify-content-center">
		    <div class="col-4">
			<#if baseName??>
				<p>Base Definition: ${baseName}</p>
			</#if>
			<div id="elements">
 			<@element differential></@element>
 			</div>

		    </div>
		</div>
    </div>
<#include "footer.ftl">
   </body>
  <#include "scripts.ftl">
     <script type="text/javascript">
   		$(function () { 
   			$('#elements').jstree();
   		});
   	  </script>
</html>

<#macro element local> 
	${local.name!}
	<ul>
	<#list local.children as item>
		<li>
			<@element item></@element>
		</li>
	</#list>
	</ul>
</#macro>
