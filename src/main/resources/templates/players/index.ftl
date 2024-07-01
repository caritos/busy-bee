<#import "../_layout.ftl" as layout />
<@layout.header>
    <div class="flex justify-center md:justify-end">
        <a href="/players/new" class="btn text-primary border-primary md:border-2 hover:bg-primary hover:text-white transition ease-out duration-500">Add a Player</a>
    </div>

    <#if players?has_content>
        <h3 class="text-center text-2xl text-green-700">Recent Matches</h3>
        <table class="table-auto w-full bg-white border border-gray-300">
            <thead class="bg-pink-500">
            <tr>
                <th class="border border-gray-300 px-4 py-2">
                    Name
                </th>
            </tr>
            </thead>
            <tbody>
            <#list players?reverse as player>
                <tr>
                    <td class="border border-gray-300 px-4 py-2">
                        <a href="/players/${player.id}">${player.name}</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>No players available</p>
    </#if>
</@layout.header>