<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create team</h3>
        <form action="/teams" method="post">
            <p>
                <input type="text" name="name">
            </p>
            <p>
                <input type="submit">
            </p>
        </form>
    </div>
</@layout.header>
