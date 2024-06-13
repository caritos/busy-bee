<table class="table-auto">
<thead>
<tr>
  <th>Date</th>
  <th>Court ID</th>
  <th>Winner ID</th>
  <th>Loser ID</th>
  <th>Match Type</th>
</tr>
</thead>
<tbody>
<tr>
  <td>${match.date}</td>
  <td>${courts[match.courtId?string]}</td>
  <td>${players[match.winnerId?string]}</td>
  <td>${players[match.loserId?string]}</td>
  <td>${match.isDoubles()?string("Doubles", "Singles")}</td>
</tr>
</tbody>
</table>
