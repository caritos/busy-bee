<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Edit Court</h3>
        <form action="/courts/${court.id}" method="post">
            <p>
                <input type="text" name="name" value="${court.name}">
            </p>
            <p>
                <textarea name="location">${court.location}</textarea>
            </p>
            <p>
                <input type="submit" name="_action" value="update">
            </p>
        </form>
    </div>
    <div>
        <form action="/courts/${court.id}" method="post">
            <p>
                <input type="submit" name="_action" value="delete">
            </p>
        </form>
    </div>
</@layout.header>
