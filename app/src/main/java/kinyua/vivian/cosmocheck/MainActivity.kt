package kinyua.vivian.cosmocheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kinyua.vivian.core.designSystem.theme.CosmoCheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CosmoCheckTheme {
//                CosmoCheckApp()
            }
        }
    }
}

//@PreviewScreenSizes
//@Composable
//fun CosmoCheckApp() {
//    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
//
//    NavigationSuiteScaffold(
//        navigationSuiteItems = {
//            AppDestinations.entries.forEach {
//                item(
//                    icon = {
//                        Icon(
//                            it.icon,
//                            contentDescription = it.label
//                        )
//                    },
//                    label = { Text(it.label) },
//                    selected = it == currentDestination,
//                    onClick = { currentDestination = it }
//                )
//            }
//        }
//    ) {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            Greeting(
//                name = "Android",
//                modifier = Modifier.padding(innerPadding)
//            )
//        }
//    }
//}

//enum class AppDestinations(
//    val label: String,
//    val icon: ImageVector,
//) {
//    HOME("Home", Icons.Default.Home),
//    FAVORITES("Favorites", Icons.Default.Favorite),
//    PROFILE("Profile", Icons.Default.AccountBox),
//}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CosmoCheckTheme {
//        Greeting("Android")
//    }
//}