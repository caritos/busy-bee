<#import "../_layout.ftl" as layout />
<@layout.header>
    <#if players?has_content>
        <#list players?reverse as player>
            <div>
                <h3>
                    <a href="/players/${player.id}">${player.name}</a>
                </h3>
            </div>
        </#list>
    <#else>
        <p>No players available</p>
    </#if>

    <p>
        <a href="/players/new">Add a Player</a>
    </p>

</@layout.header>