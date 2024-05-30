<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
            ${tennisset.setNumber}
        </h3>
        <hr>
        <p>
            <a href="/tennissets/${tennisset.id}/edit">Edit tennisset</a>
        </p>
    </div>
</@layout.header>
