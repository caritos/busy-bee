<#import "../_layout.ftl" as layout />
<@layout.header>
    <#if tennissets?has_content>
        <#list tennissets?reverse as tennisset>
            <div>
                <h3>
                    <a href="/tennissets/${tennisset.id}">${tennisset.id}</a>
                    <a href="/tennissets/${tennisset.id}">${tennisset.setNumber}</a>
                    <a href="/tennissets/${tennisset.id}">${tennisset.player1Score}</a>
                    <a href="/tennissets/${tennisset.id}">${tennisset.player2Score}</a>
                </h3>
            </div>
        </#list>
    <#else>
        <p>No tennissets available</p>
    </#if>

    <p>
        <a href="/tennissets/new">Add a tennisset</a>
    </p>

</@layout.header>