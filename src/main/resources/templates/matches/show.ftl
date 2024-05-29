<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
            ${player.name}
        </h3>
        <hr>
        <p>
            <a href="/players/${player.id}/edit">Edit Player</a>
        </p>
    </div>
</@layout.header>
