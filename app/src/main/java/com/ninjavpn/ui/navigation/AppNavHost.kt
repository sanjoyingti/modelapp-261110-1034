package com.ninjavpn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ninjavpn.ui.screen.connect.ConnectScreen
import com.ninjavpn.ui.screen.servers.ServerListScreen
import com.ninjavpn.ui.screen.settings.SettingsScreen
import com.ninjavpn.ui.screen.speedtest.SpeedTestScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = ConnectScreen.route) {
        composable(ConnectScreen.route) { 
            ConnectScreen(onServerClick = { navController.navigate(ServerListScreen.route) }) 
        }
        composable(ServerListScreen.route) { 
            ServerListScreen(onServerSelected = { navController.popBackStack() }) 
        }
        composable(SettingsScreen.route) { SettingsScreen() }
        composable(SpeedTestScreen.route) { SpeedTestScreen() }
        // REMOVED: SubscriptionScreen
    }
}