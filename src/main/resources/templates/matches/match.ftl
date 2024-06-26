<table class="table-auto">
<thead>
<tr>
  <th>Date</th>
  <th>Court</th>
  <th>Team A</th>
  <th>Team B</th>
  <th>Actions</th>
</tr>
</thead>
<tbody>
<tr>
  <td>${match.date}</td>
  <td>${courts[match.courtId?string]}</td>
  <td>${teams[match.teamAId?string]}</td>
  <td>${teams[match.teamBId?string]}</td>
  <td><a href="/matches/${match.id}/edit">Edit Match</a></td>
</tr>
</tbody>
</table>
