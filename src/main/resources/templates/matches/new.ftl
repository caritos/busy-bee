<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create Player</h3>
        <form action="/players" method="post">
            <p>
                <input type="text" name="name">
            </p>
            <p>
                <input type="submit">
            </p>
        </form>
    </div>
</@layout.header>
