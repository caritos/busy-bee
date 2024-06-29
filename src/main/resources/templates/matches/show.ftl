<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <#include "match.ftl"/>
        <p>
            <a href="/matches/${match.id}/edit">Edit match</a>
            <a href="/matches">List matches</a>
        </p>
    </div>
</@layout.header>
