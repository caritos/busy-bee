<#-- @ftlvariable name="article" type="com.example.models.Article" -->
<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <table class="table-auto">
            <thead>
                <tr>
                    <th>Match ID</th>
                    <th>Date</th>
                    <th>Court ID</th>
                    <th>Winner ID</th>
                    <th>Loser ID</th>
                    <th>Match Type</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${match.id}</td>
                    <td>${match.date}</td>
                    <td>${match.courtId}</td>
                    <td>${match.winnerId}</td>
                    <td>${match.loserId}</td>
                    <td>${match.isDoubles()?string("Doubles", "Singles")}</td>
                </tr>
            </tbody>
        </table>
        <hr>
        <p>
            <a href="/matches/${match.id}/edit">Edit match</a>
        </p>
    </div>
</@layout.header>
