<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create tennisset</h3>
        <form action="/tennissets" method="post">
            <p>
                <input type="text" name="setNumber">
            </p>
            <p>
                <input type="submit">
            </p>
        </form>
    </div>
</@layout.header>
