<p>${match.date}: ${match.teamANames} vs ${match.teamBNames}</p>
<#list match.score as set>
  <p>Set ${set.setNumber}: ${set.teamAScore} - ${set.teamBScore}</p>
</#list>
