<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<#include "header.ftl">
</head>
    <body>
	<#include "navbar.ftl">
	<div class="container-fluid">
	    <div class="row justify-content-center">
		    <div class="col-4">
		    <#list profiles>
		    	
			    	<table class="table">
			    	<thead>
			    	<tr><th scope="col">Profiles:</th></tr>
			    	</thead>
			    	<tbody>
			    	<#items as profile>
			    		<tr><td><a href="${profile.link}">${profile.name}</a></td></tr>
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
   <#include "scripts.ftl">
</html>
