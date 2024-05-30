<#import "../_layout.ftl" as layout />
<@layout.header>
    <#if matches?has_content>
        <#list matches?reverse as match>
            <div>
                <h3>
                    <a href="/matches/${match.id}">${match.loserId}</a>
                </h3>
            </div>
        </#list>
    <#else>
        <p>No matches available</p>
    </#if>

    <p>
        <a href="/matches/new">Add a match</a>
    </p>

</@layout.header>