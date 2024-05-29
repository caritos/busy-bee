<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create courts</h3>
        <form action="/courts" method="post">
            <p>
                <input type="text" name="name">
            </p>
            <p>
                <input type="text" name="location">
            </p>
            <p>
                <input type="submit">
            </p>
        </form>
    </div>
</@layout.header>
