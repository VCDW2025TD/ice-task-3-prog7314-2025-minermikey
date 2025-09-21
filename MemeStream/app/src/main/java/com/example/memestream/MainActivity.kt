package com.example.memestream

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Get NavController from FragmentContainerView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Link BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        // Setup ComposeView with hardcoded buttons
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            NavigationButtons(navController)
        }
    }

    @Composable
    fun NavigationButtons(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Navigate to Fragment",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(R.id.feedFragment) }) {
                Text("Go to Feed")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(R.id.createMemeFragment) }) {
                Text("Go to Create Meme")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(R.id.mapFragment) }) {
                Text("Go to Map")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate(R.id.profileFragment) }) {
                Text("Go to Profile")
            }
        }
    }
}
