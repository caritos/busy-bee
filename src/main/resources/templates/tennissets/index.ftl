<#import "../_layout.ftl" as layout />
<@layout.header>
    <h3 class="text-center text-2xl text-green-700">Sets</h3>
    <#if tennissets?has_content>
        <table class="table-auto w-full bg-white border border-gray-300">
        <thead class="bg-pink-500">
        <tr>
            <th class="border border-gray-300 px-4 py-2">
               Match Id 
            </th>
            <th class="border border-gray-300 px-4 py-2">
                Set Number
            </th>
            <th class="border border-gray-300 px-4 py-2">
                Team A Score
            </th>
             <th class="border border-gray-300 px-4 py-2">
                Team B Score
            </th>
        </tr>
        </thead>
        <tbody>
                <#list tennissets?reverse as tennisset>
            <tr>
  <td class="border border-gray-300 px-4 py-2">
    ${tennisset.matchId}
  </td>
  <td class="border border-gray-300 px-4 py-2">
    ${tennisset.setNumber}
  </td>
  <td class="border border-gray-300 px-4 py-2">
    ${tennisset.teamAScore} 
  </td>
  <td class="border border-gray-300 px-4 py-2">
    ${tennisset.teamBScore} 
  </td>
</tr>

        </#list>
        </tbody>
    </table>

    <#else>
        <p>No tennissets available</p>
    </#if>

    <p>
        <a href="/tennissets/new">Add a tennisset</a>
    </p>

</@layout.header>


