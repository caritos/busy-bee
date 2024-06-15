function addPlayerToTeam(teamContainerId) {
    console.log('addPlayerToTeam called'); // Print a message to the console
    console.log(window.playersJson); // Print the playersJson variable to the console

    var teamContainer = document.getElementById(teamContainerId);
    var playerSelect = document.createElement('select');
    playerSelect.name = teamContainerId + 'Player' + (teamContainer.childElementCount + 1);
    var defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.disabled = true;
    defaultOption.selected = true;
    defaultOption.textContent = 'Select a player';
    playerSelect.appendChild(defaultOption);
    console.log("begin addPlayerToTeam");
    var playersJson = window.playersJson
    console.log("end addPlayerToTeam");
    playersJson.forEach(player => {
        var playerOption = document.createElement('option');
        playerOption.value = player.id;
        playerOption.textContent = player.name;
        playerSelect.appendChild(playerOption);
    });
    // The player list will be populated from the server-side
    teamContainer.appendChild(playerSelect);
}

function removePlayerFromTeam(teamContainerId) {
    var teamContainer = document.getElementById(teamContainerId);
    if (teamContainer.childElementCount > 1) {
        teamContainer.removeChild(teamContainer.lastChild);
    }
}

document.getElementById('addPlayerToTeamA').addEventListener('click', function() {
    addPlayerToTeam('teamAContainer');
});

document.getElementById('removePlayerFromTeamA').addEventListener('click', function() {
    removePlayerFromTeam('teamAContainer');
});

document.getElementById('addPlayerToTeamB').addEventListener('click', function() {
    addPlayerToTeam('teamBContainer');
});

document.getElementById('removePlayerFromTeamB').addEventListener('click', function() {
    removePlayerFromTeam('teamBContainer');
});