<#import "../_layout.ftl" as layout />
<@layout.header>
  <div class="flex justify-center md:justify-end">
    <a href="/matches/new" class="btn text-primary border-primary md:border-2 hover:bg-primary hover:text-white transition ease-out duration-500">Record a Match</a>
  </div>



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
    <#list doublePlayerTeamsWithNames as team>
      <tr>
        <td class="border border-gray-300 px-4 py-2">
          ${team_index + 1}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.first?join(",")}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.second}</p>
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
    <#list singlePlayerTeamsWithNames as team>
      <tr>
        <td class="border border-gray-300 px-4 py-2">
          ${team_index + 1}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.first?join(",")}
        </td>
        <td class="border border-gray-300 px-4 py-2">
          ${team.second}</p>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>
  
  <h1 class="text-3xl font-bold text-center mt-8">Recent Matches</h1>
  <#list recentMatches as match>
    <#include "../matches/match.ftl">
  </#list>
</@layout.header>