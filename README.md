# Kotlin News App
A simple plugin style app which delivers news to the user.
Just made for trying out some things
(Replace the API Keys in config.properties with your own if you want to build this)

#### How it works
- On launch, the app finds plugins which have services that conform to the intent scheme
- With IPC messaging, it gets the desired metadata from all plugins and displays the list of plugins for the user to choose
- After selection, gets available categories from the selected plugin
- When user makes a search, app calls the plugin, it fetches the news from the remote source and returns it
- App displays the list of headlines
- Opens the article link in a webview when user clicks on the headline
- Contains 1 built in plugin for newsAPI.org
- Repo has 1 more plugin for New York Times(Needs to be installed separately)

#### How the plugins are implemented
- Plugins need to include the *kotlinnewspluginlib* aar 
- Create a Service that extends *PluginMessengerService*
- Give it an intent filter for *com.jason.experiments.kotlinnewsapp.action_get_plugin*
- Create a concrete implementation of *NewsManager* which fetches the news from a source
- Create a concrete implementation of *PluginIpcHandler* that takes in the *NewsManager*
- Set the concrete *PluginIpcHandler* as the Messenger's Handler for the Service created earlier
- Install the plugin's APK

##### Some tools used
- Uses a plugin architecture to display news sources.
- Toothpick for some DI
- Rx + Okhttp for network calls
- MVVM design pattern
