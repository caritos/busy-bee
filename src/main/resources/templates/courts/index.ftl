<#import "../_layout.ftl" as layout />
<@layout.header>
  inside courts
    <#if courts?has_content>
        <#list courts?reverse as court>
          <div>
            <h3>
              <a href="/courts/${court.id}">${court.name}</a>
            </h3>
            <p>
                ${court.location}
            </p>
          </div>
        </#list>
      <#else>
        <p>No courts available</p>
    </#if>

</@layout.header>