<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
            ${team.name}
        </h3>
        <hr>
        <p>
            <a href="/teams/${team.id}/edit">Edit team</a>
        </p>
    </div>
</@layout.header>
