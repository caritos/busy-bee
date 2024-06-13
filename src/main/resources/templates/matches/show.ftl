<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <#include "match.ftl"/>
        <#-- Display TennisSet details -->
        <#list tennisSets as tennisSet>
            <#include "../tennissets/tennis_set.ftl"/>
        </#list>
        <hr>
        <p>
            <a href="/matches/${match.id}/edit">Edit match</a>
            <a href="/matches">List matches</a>
        </p>
    </div>
</@layout.header>
