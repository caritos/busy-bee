<table class="table-auto">
<thead>
<tr>
  <th>Date</th>
  <th>Court</th>
  <th>Team A</th>
  <th>Team B</th>
</tr>
</thead>
<tbody>
<tr>
  <td>${match.date}</td>
  <td>${courts[match.courtId?string]}</td>
  <td>${teams[match.teamAId?string]}</td>
  <td>${teams[match.teamBId?string]}</td>
</tr>
</tbody>
</table>
