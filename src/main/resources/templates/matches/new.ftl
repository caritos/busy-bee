<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create match</h3>
        <form action="/matches" method="post">
            <label for="date">Date:</label>
            <input type="datetime-local" id="date" name="date" required><br><br>

            <!-- beginning of teams -->

            <h2>Team A</h2>
            <div id="teamAContainer">
                <select id="teamAPlayer1" name="teamAPlayer1" required>
                    <option value="" disabled selected>Select a player</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name}</option>
                    </#list>
                </select>
                <!-- Additional player select elements for Team A will be added here -->
            </div>
            <button type="button" id="addPlayerToTeamA">Add Player to Team A</button>
            <button type="button" id="removePlayerFromTeamA">Remove Player from Team A</button>

            <h2>Team B</h2>
            <div id="teamBContainer">
                <select id="teamBPlayer1" name="teamBPlayer1" required>
                    <option value="" disabled selected>Select a player</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name}</option>
                    </#list>
                </select>
                <!-- Additional player select elements for Team B will be added here -->
            </div>
            <button type="button" id="addPlayerToTeamB">Add Player to Team B</button>
            <button type="button" id="removePlayerFromTeamB">Remove Player from Team B</button>
            <!-- end of teams -->

            <select id="courtId" name="court" required>
                <option value="" disabled selected>Select a court </option>
                <#list courts as court>
                    <option value="${court.id}">${court.name}</option>
                </#list>
            </select>

            <h2>Set Scores</h2>

<div id="setScoresContainer">
    <div>
        <label for="set1_player1">Set 1 - Team A:</label>
        <input type="number" id="set1_teamA" name="set1_teamA" min="0" required="">
        <label for="set1_player2">Set 1 - Team B:</label>
        <input type="number" id="set1_teamB" name="set1_teamB" min="0" required=""><br>
    </div>
    <!-- Existing set score input fields here -->
</div>

<button type="button" id="addSetScore">Add Set Score</button>
<button type="button" id="removeSetScore">Remove Set Score</button>

            <button type="submit">Create Match</button>
        </form>
        <script>
            console.log('begin new.ftl')
            console.log('${playersJson}'); // Print the playersJson variable to the console
            console.log('end new.ftl')
            window.playersJson = JSON.parse('${playersJson?no_esc}');
        </script>
        <script src="/static/js/add_set_score.js"></script>
        <script src="/static/js/add_player_to_team.js"></script>
    </div>
</@layout.header>
