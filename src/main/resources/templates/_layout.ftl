<#macro header>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Eladio Caritos</title>
        <link href="/static/css/tailwind.css" rel="stylesheet"/>
    </head>
    <body class="text-gray-600 font-body">
    <div>
        <div>
            <nav>
                <div>
                    <h1 class="font-bold uppercase p-4 border-b border-gray-100">
                        <a href="/">Food Ninja</a>
                    </h1>
                </div>
                <ul>
                    <li class="text-gray-900 font-bold">
                        <a href="#">
                            <span>Home</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <span>About</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <span>Contact</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>

        <main class="px-16 py-6 bg-gray-100">
            <div class="flex justify-center md:justify-end">
                <a href="#" class="text-primary">Log in</a>
                <a href="#" class="text-primary ml-2 ">Sign up</a>
            </div>

            <header>
                <h2 class="text-gray-700 text-6xl font-semibold">Recipes</h2>
                <h3 class="txt-2xl font-semibold">For Ninjas</h3>
            </header>

            <div>
                <h4 class="font-bold mt-12 pb-2 border-b border-gray-200">Latest Recipes</h4>

                <div class="mt-8">
                    <!-- cards go here -->
                    <div class="bg-white rounded overflow-hidden shadow-md relative">
                        <img src="/static/img/stew.jpg" alt="stew" class="w-full h-32 sm:h-48 object-cover">
                        <div class="m-4">
                            <span class="font-bold">5 Bean Chili Stew</span>
                            <span class="block text-gray-500 text-sm">Recipe by Mario</span>
                        </div>
                        <div class="bg-secondary-100 text-secondary-200 text-xs uppercase font-bold rounded-full p-2 absolute top-0 ml-2 mt-2">
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
                <div class="bg-secondary-100 text-secondary-200">Load more</div>
            </div>
        </main>
    </div>
    <#nested>
    <a href="/">Back to the main page</a>
    </body>
    </html>
</#macro>