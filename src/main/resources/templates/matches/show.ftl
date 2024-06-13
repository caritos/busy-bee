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
        <#-- Display TennisSet details -->
        <#list tennisSets as tennisSet>
            <table class="table-auto">
                <thead>
                    <tr>
                        <th>Set Number</th>
                        <th>Player 1 Score</th>
                        <th>Player 2 Score</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${tennisSet.setNumber}</td>
                        <td>${tennisSet.player1Score}</td>
                        <td>${tennisSet.player2Score}</td>
                    </tr>
                </tbody>
            </table>
        </#list>
        <hr>
        <p>
            <a href="/matches/${match.id}/edit">Edit match</a>
            <a href="/matches">List matches</a>
        </p>
    </div>
</@layout.header>
