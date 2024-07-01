<#import "../_layout.ftl" as layout />
<@layout.header>
    <div class="flex justify-center md:justify-end">
        <a href="/matches/new" class="btn text-primary border-primary md:border-2 hover:bg-primary hover:text-white transition ease-out duration-500">Record a Match</a>
    </div>

    <h3 class="text-center text-2xl text-green-700">Matches</h3>
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
        <#list matches as match>
            <#include "../matches/match.ftl">
        </#list>
        </tbody>
    </table>
</@layout.header>