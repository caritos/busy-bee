<#-- @ftlvariable name="court" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
            ${court.name}
        </h3>
        <p>
            ${court.location}
        </p>
        <hr>
        <p>
            <a href="/courts/${court.id}/edit">Edit court</a>
        </p>
    </div>
</@layout.header>
