var setScoresContainer = document.getElementById('setScoresContainer');
var addSetScoreButton = document.getElementById('addSetScore');
var removeSetScoreButton = document.getElementById('removeSetScore');
var setScoreCount = 2; // Start from 3 if you already have 3 sets of input fields

addSetScoreButton.addEventListener('click', function() {
    var setScoreDiv = document.createElement('div');
    setScoreDiv.innerHTML = `
        <label for="set${setScoreCount}_player1">Set ${setScoreCount} - Player 1:</label>
        <input type="number" id="set${setScoreCount}_player1" name="set${setScoreCount}_player1" min="0" required>
        <label for="set${setScoreCount}_player2">Set ${setScoreCount} - Player 2:</label>
        <input type="number" id="set${setScoreCount}_player2" name="set${setScoreCount}_player2" min="0" required><br>
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