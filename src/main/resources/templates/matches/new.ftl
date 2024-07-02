<#import "../_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3 class="text-center text-2xl text-green-700">Record a Match</h3>
        <form action="/matches" method="post">
            <label for="date">Date:</label>
            <div id="date">
                <input type="date" id="date" name="date" required><br><br>
            </div>

            <!-- beginning of teams -->

            <label for="teamA">Team A:</label>
            <div id="teamAContainer">
                <select id="teamAContainerPlayer1" name="teamAContainerPlayer1" required>
                    <option value="" disabled selected>Select a player</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name}</option>
                    </#list>
                </select>
            </div>
                <!-- Additional player select elements for Team A will be added here -->
                <div>
                    <button type="button" id="addPlayerToTeamA" class="ml-4">Add Player to Team A</button>
                    <button type="button" id="removePlayerFromTeamA" class="ml-4">Remove Player from Team A</button>
                </div>

            <label for="teamB">Team B</label>
            <div id="teamBContainer">
                <select id="teamBContainerPlayer1" name="teamBContainerPlayer1" required>
                    <option value="" disabled selected>Select a player</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name}</option>
                    </#list>
                </select>
            </div>
                <!-- Additional player select elements for Team B will be added here -->
                <div>
                    <button type="button" id="addPlayerToTeamB" class="ml-4">Add Player to Team B</button>
                    <button type="button" id="removePlayerFromTeamB" class="ml-4">Remove Player from Team B</button>
                </div>
            <!-- end of teams -->

            <label for="court">Court</label>
            <div id="court">
                <select id="courtId" name="court" required>
                    <option value="" disabled selected>Select a court </option>
                    <#list courts as court>
                        <option value="${court.id}">${court.name}</option>
                    </#list>
                </select>
            </div>


            <label for="setScore">Set Scores</label>

<div>
    <div id=setScoresContainer>
        <label for="set1_player1">Set 1 - Team A:</label>
        <input type="number" id="set1_teamA" name="set1_teamA" min="0" required="">
        <label for="set1_player2">Set 1 - Team B:</label>
        <input type="number" id="set1_teamB" name="set1_teamB" min="0" required=""><br>
    </div>
    <!-- Existing set score input fields here -->
    <div>
        <button type="button" id="addSetScore" class="ml-4">Add Set Score</button>
        <button type="button" id="removeSetScore" class="ml-4">Remove Set Score</button>
    </div>
</div>

            <button type="submit" class="btn text-primary border-primary md:border-2 hover:bg-primary hover:text-white transition ease-out duration-500">Create Match</button>
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
