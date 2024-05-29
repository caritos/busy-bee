<#-- @ftlvariable name="articles" type="kotlin.collections.List<com.example.models.Article>" -->
<#import "../_layout.ftl" as layout />
<@layout.header>



        <header>
            <h2 class="text-gray-700 text-6xl font-semibold">Recipes</h2>
            <h3 class="txt-2xl font-semibold">For Ninjas</h3>
        </header>

        <div>
            <h4 class="font-bold mt-12 pb-2 border-b border-gray-200">Latest Recipes</h4>

            <div class="mt-8 grid lg:grid-cols-3 gap-10">
                <!-- cards go here -->
                <div class="card">
                    <img src="/static/img/stew.jpg" alt="stew" class="w-full h-32 sm:h-48 object-cover">
                    <div class="m-4">
                        <span class="font-bold">5 Bean Chili Stew</span>
                        <span class="block text-gray-500 text-sm">Recipe by Mario</span>
                    </div>
                    <div class="badge">
                        <svg class="w-5 inline-block" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"></path>
                        </svg>
                        <span>25 mins</span>
                    </div>
                </div>
                <div class="card">
                    <img src="/static/img/curry.jpg" alt="curry" class="w-full h-32 sm:h-48 object-cover">
                    <div class="m-4">
                        <span class="font-bold">Tofu Curry</span>
                        <span class="block text-gray-500 text-sm">Recipe by Mario</span>
                    </div>
                    <div class="badge">
                        <svg class="w-5 inline-block" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"></path>
                        </svg>
                        <span>25 mins</span>
                    </div>
                </div>
                <div class="card">
                    <img src="/static/img/noodles.jpg" alt="stew" class="w-full h-32 sm:h-48 object-cover">
                    <div class="m-4">
                        <span class="font-bold">Veg Noodles</span>
                        <span class="block text-gray-500 text-sm">Recipe by Mario</span>
                    </div>
                    <div class="badge">
                        <svg class="w-5 inline-block" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"></path>
                        </svg>
                        <span>25 mins</span>
                    </div>
                </div>
            </div>

            <h4 class="font-bold mt-12 pb-2 border-b border-gray-200">Most Popular</h4>

            <div class="mt-8">
                <!-- cards go here -->
            </div>
        </div>

        <div class="flex justify-center">
            <div class="btn bg-secondary-100 text-secondary-200 hover:shadow-inner transform hover:scale-125 hover:bg-opacity-50 transition ease-out duration-300">Load more</div>
        </div>

        <#list articles?reverse as article>
            <div>
                <h3>
                    <a href="/articles/${article.id}">${article.title}</a>
                </h3>
                <p>
                    ${article.body}
                </p>
            </div>
        </#list>
        <hr>
        <p>
            <a href="/articles/new">Create article</a>
        </p>

        <a href="/">Back to the main page</a>


</@layout.header>