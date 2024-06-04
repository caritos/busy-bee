<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
            ${match.id}
        </h3>
        <hr>
        <p>
            <a href="/matches/${match.id}/edit">Edit match</a>
        </p>
    </div>
</@layout.header>
