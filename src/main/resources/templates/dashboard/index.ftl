<#import "../_layout.ftl" as layout />
<@layout.header>

  <h3 class="text-center text-2xl text-green-700">Recent Matches</h3>
  <table class="table-auto w-full bg-white border border-gray-300">
    <thead class="bg-pink-500">
    <tr>
      <th class="border border-gray-300 px-4 py-2">
        Date
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Team
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Score
      </th>
    </tr>
    </thead>
    <tbody>
    <#list recentMatches as match>
      <#include "../matches/match.ftl">
    </#list>
    </tbody>
  </table>

  <h3 class="text-center text-2xl text-green-700">Doubles Ranking</h3>
  <table class="table-auto w-full bg-white border border-gray-300">
    <thead class="bg-pink-500">
    <tr>
      <th class="border border-gray-300 px-4 py-2">
       Rank
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Team
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Points
      </th>
    </tr>
    </thead>
    <tbody>
    <#list doublesTeamsWithNameAndScore as team>
      <tr>
        <td class="border border-gray-300 px-4 py-2">
          ${team_index + 1}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.name}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.score}</p>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>

  <h3 class="text-center text-2xl text-green-700">Singles Ranking</h3>
  <table class="table-auto w-full bg-white border border-gray-300">
    <thead class="bg-pink-500">
    <tr>
      <th class="border border-gray-300 px-4 py-2">
        Rank
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Team
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Points
      </th>
    </tr>
    </thead>
    <tbody>
    <#list singlesTeamsWithNameAndScore as team>
      <tr>
        <td class="border border-gray-300 px-4 py-2">
          ${team_index + 1}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.name}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.score}</p>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>

  <h3 class="text-center text-2xl text-green-700">Doubles Winning Percentage</h3>
  <table class="table-auto w-full bg-white border border-gray-300">
    <thead class="bg-pink-500">
    <tr>
      <th class="border border-gray-300 px-4 py-2">
        Rank
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Team
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Rate
      </th>
    </tr>
    </thead>
    <tbody>
    <#list doublesTeamsWithNameAndWinningPercentages as team>
      <tr>
        <td class="border border-gray-300 px-4 py-2">
          ${team_index + 1}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.name}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.rate}</p>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>

  <h3 class="text-center text-2xl text-green-700">Singles Winning Percentage</h3>
  <table class="table-auto w-full bg-white border border-gray-300">
    <thead class="bg-pink-500">
    <tr>
      <th class="border border-gray-300 px-4 py-2">
        Rank
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Team
      </th>
      <th class="border border-gray-300 px-4 py-2">
        Rate
      </th>
    </tr>
    </thead>
    <tbody>
    <#list singlesTeamsWithNameAndWinningPercentages as team>
      <tr>
        <td class="border border-gray-300 px-4 py-2">
          ${team_index + 1}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.name}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.rate}</p>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>

</@layout.header>