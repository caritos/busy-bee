var setScoresContainer = document.getElementById('setScoresContainer');
var addSetScoreButton = document.getElementById('addSetScore');
var removeSetScoreButton = document.getElementById('removeSetScore');
var setScoreCount = 2; // Start from 3 if you already have 3 sets of input fields

addSetScoreButton.addEventListener('click', function() {
    var setScoreDiv = document.createElement('div');
    setScoreDiv.innerHTML = `
        <label for="set${setScoreCount}_teamA">Set ${setScoreCount} - Team A:</label>
        <input type="number" id="set${setScoreCount}_teamA" name="set${setScoreCount}_teamA" min="0" required>
        <label for="set${setScoreCount}_teamB">Set ${setScoreCount} - Team B:</label>
        <input type="number" id="set${setScoreCount}_teamB" name="set${setScoreCount}_teamB" min="0" required><br>
    `;
    setScoresContainer.appendChild(setScoreDiv);
    setScoreCount++;
});

removeSetScoreButton.addEventListener('click', function() {
    if (setScoreCount > 1) {
        setScoresContainer.removeChild(setScoresContainer.lastChild);
        setScoreCount--;
    }
});