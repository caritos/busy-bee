<#import "../_layout.ftl" as layout />
<@layout.header>
<#--    <#if matches?has_content>-->
<#--        <#list matches?reverse as match>-->
<#--            <#include "../matches/match.ftl">-->
<#--        </#list>-->
<#--    <#else>-->
<#--        <p>No matches available</p>-->
<#--    </#if>-->

    <#list matches as match>
        <#include "../matches/match.ftl">
    </#list>
    <p>
        <a href="/matches/new">Add a match</a>
    </p>

</@layout.header>