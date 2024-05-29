<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Edit match</h3>
        <form action="/matches/${match.id}" method="post">
            <p>
                <input type="text" name="date" value="${match.date}">
            </p>
            <p>
                <input type="submit" name="_action" value="update">
            </p>
        </form>
    </div>
    <div>
        <form action="/matches/${match.id}" method="post">
            <p>
                <input type="submit" name="_action" value="delete">
            </p>
        </form>
    </div>
</@layout.header>
