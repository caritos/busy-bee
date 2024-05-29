<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create match</h3>
        <form action="/matches" method="post">
            <p>
                <input type="text" name="date">
            </p>
            <p>
                <input type="submit">
            </p>
        </form>
    </div>
</@layout.header>
