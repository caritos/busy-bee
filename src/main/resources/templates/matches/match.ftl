<tr>
  <td class="border border-gray-300 px-4 py-2">
    ${match.date}
  </td>
  <td class="border border-gray-300 px-4 py-2">
    <a href="/matches/${match.id}">${match.teamANames} vs ${match.teamBNames}</a>
  </td>
  <td class="border border-gray-300 px-4 py-2">
    <#list match.score as set>
      Set ${set.setNumber}: ${set.teamAScore} - ${set.teamBScore}<#if set_has_next>, </#if>
    </#list>
  </td>
</tr>
