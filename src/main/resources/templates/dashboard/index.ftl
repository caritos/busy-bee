<#import "../_layout.ftl" as layout />
<@layout.header>
  <div class="flex justify-center md:justify-end">
    <a href="/matches/new" class="btn text-primary border-primary md:border-2 hover:bg-primary hover:text-white transition ease-out duration-500">Record a Match</a>
  </div>

    <h1 class="text-3xl font-bold text-center mt-8">Singles Leaderboard</h1>
    <#list singlePlayerTeamsWithNames as team>
<#--      <p>${team[0]?join(", ")}: ${team[1]}</p>-->
    </#list>
<#-- ... -->
  <h1 class="text-3xl font-bold text-center mt-8">Doubles Leaderboard</h1>
    <#list doublePlayerTeamsWithNames as team>
<#--      <p>${team[0]?join(", ")}: ${team[1]}</p>-->
    </#list>
</@layout.header>