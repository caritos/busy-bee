<#macro header>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <!-- Google tag (gtag.js) -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=G-GSTY6KYLEQ"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());

            gtag('config', 'G-GSTY6KYLEQ');
        </script>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Tennis</title>
        <link href="/static/css/tailwind.css" rel="stylesheet"/>
    </head>
    <body class="text-gray-600 font-body">
    <div class="grid md:grid-cols-3">
        <div class="md:col-span-1 md:flex md:justify-end">
            <nav class="text-right">
                <div class="flex justify-between items-center">
                    <h1 class="font-bold uppercase p-4 border-b border-gray-100">
                        <a href="/">Tennis</a>
                    </h1>
                    <div class="px-4 cursor-pointer md:hidden" id="burger">
                        <svg class="w-6 " data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"></path>
                        </svg>
                    </div>
                </div>
                <ul class="text-sm mt-6 hidden md:block" id="menu">
                    <li class="text-gray-900 font-bold py-1 ">
                        <a href="/dashboard" class="px-4 flex justify-end border-r-4 border-primary">
                            <span>Home</span>
                            <svg class="w-5 ml-2" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round" d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"></path>
                            </svg>
                        </a>
                    </li>
                    <li class="text-gray-900 font-bold py-1 ">
                        <a href="/matches" class="px-4 flex justify-end border-r-4 border-primary">
                            <span>Matches</span>
                            <svg class="w-5 ml-2" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round" d="m3.75 13.5 10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75Z" />
                            </svg>
                        </a>
                    </li>
                    <li class="text-gray-900 font-bold py-1 ">
                        <a href="/players" class="px-4 flex justify-end border-r-4 border-primary">
                            <span>Players</span>
                            <svg class="w-5 ml-2" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
                            </svg>
                        </a>
                    </li>
                    <li class="text-gray-900 font-bold py-1 ">
                        <a href="/courts" class="px-4 flex justify-end border-r-4 border-primary">
                            <span>Courts</span>
                            <svg class="w-5 ml-2" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M12 21a9.004 9.004 0 0 0 8.716-6.747M12 21a9.004 9.004 0 0 1-8.716-6.747M12 21c2.485 0 4.5-4.03 4.5-9S14.485 3 12 3m0 18c-2.485 0-4.5-4.03-4.5-9S9.515 3 12 3m0 0a8.997 8.997 0 0 1 7.843 4.582M12 3a8.997 8.997 0 0 0-7.843 4.582m15.686 0A11.953 11.953 0 0 1 12 10.5c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0 1 21 12c0 .778-.099 1.533-.284 2.253m0 0A17.919 17.919 0 0 1 12 16.5c-3.162 0-6.133-.815-8.716-2.247m0 0A9.015 9.015 0 0 1 3 12c0-1.605.42-3.113 1.157-4.418" />
                            </svg>
                        </a>
                    </li>
                    <li class="text-gray-900 font-bold py-1 ">
                        <a href="/about"  class="px-4 flex justify-end border-r-4 border-white">
                            <span>About</span>
                            <svg class="w-5 ml-2" data-slot="icon" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 5.25h.008v.008H12v-.008Z"></path>
                            </svg>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>

        <main class="md:col-span-2 px-16 py-6 bg-gray-100">
          <#nested>
        </main>
    </div>
    <script src="/static/js/index.js"></script>
    </body>
    </html>
</#macro>