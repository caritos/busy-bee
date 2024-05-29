<#import "../_layout.ftl" as layout />
<@layout.header>
    <#if teams?has_content>
        <#list teams?reverse as team>
            <div>
                <h3>
                    <a href="/teams/${team.id}">${team.name}</a>
                </h3>
            </div>
        </#list>
    <#else>
        <p>No teams available</p>
    </#if>

    <p>
        <a href="/teams/new">Add a team</a>
    </p>

</@layout.header>