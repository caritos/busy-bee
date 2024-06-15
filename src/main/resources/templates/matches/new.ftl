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

            <label for="matchType">Match Type:</label>
            <select id="matchType" name="isDoubles" onchange="togglePlayerFields()">
                <option value="singles">Singles</option>
                <option value="doubles">Doubles</option>
            </select><br>

            <select id="winnerId" name="player1" required>
                <option value="" disabled selected>Select a player 1</option>
                <#list players as player>
                    <option value="${player.id}">${player.name}</option>
                </#list>
            </select>

            <select id="loserId" name="player2" required>
                <option value="" disabled selected>Select a player 2</option>
                <#list players as player>
                    <option value="${player.id}">${player.name}</option>
                </#list>
            </select>

            <div id="doublesFields" style="display:none;">
                <select id="player3" name="player3">
                    <option value="" disabled selected>Select a player 3</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name}</option>
                    </#list>
                </select>

                <select id="player4" name="player4">
                    <option value="" disabled selected>Select a player 4</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name}</option>
                    </#list>
                </select>
            </div>

            <select id="courtId" name="court" required>
                <option value="" disabled selected>Select a court </option>
                <#list courts as court>
                    <option value="${court.id}">${court.name}</option>
                </#list>
            </select>

            <h2>Set Scores</h2>

<div id="setScoresContainer">
    <div>
        <label for="set1_player1">Set 1 - Player 1:</label>
        <input type="number" id="set1_player1" name="set1_player1" min="0" required="">
        <label for="set1_player2">Set 1 - Player 2:</label>
        <input type="number" id="set1_player2" name="set1_player2" min="0" required=""><br>
    </div>
    <!-- Existing set score input fields here -->
</div>

<button type="button" id="addSetScore">Add Set Score</button>
<button type="button" id="removeSetScore">Remove Set Score</button>

            <button type="submit">Create Match</button>
        </form>
        <script>
            function togglePlayerFields() {
                var matchType = document.getElementById('matchType').value;
                var doublesFields = document.getElementById('doublesFields');
                if (matchType === 'doubles') {
                    doublesFields.style.display = 'block';
                    document.getElementById('player3').required = true;
                    document.getElementById('player4').required = true;
                } else {
                    doublesFields.style.display = 'none';
                    document.getElementById('player3').required = false;
                    document.getElementById('player4').required = false;
                }
            }
        </script>
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
