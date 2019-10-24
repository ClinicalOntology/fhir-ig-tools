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
		    <#list elements>
		    	
			    	<table class="table">
			    	<thead>
			    	<tr><th scope="col">Profiles:</th></tr>
			    	</thead>
			    	<tbody>
			    	<#items as element>
			    		<tr><td>${element.path}</a></td></tr>
			    	</#items>
			    	</tbody>
			    	</table>
		    	
		    <#else>
		    	<h2>No profiles found</h2>
		    </#list>	
		    </div>
		</div>
    </div>
<#include "footer.ftl">
   </body>
</html>
