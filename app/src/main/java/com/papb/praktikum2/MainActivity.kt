package com.papb.praktikum2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.firebase.auth.FirebaseAuth
import com.papb.praktikum2.navigation.NavigationItem
import com.papb.praktikum2.navigation.Screen
import com.papb.praktikum2.ui.theme.Praktikum2Theme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import com.papb.praktikum2.screen.GithubProfileScreen
import com.papb.praktikum2.screen.MatkulScreen

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            Praktikum2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call the MainActivityContent composable
                    MainActivityContent(auth)
                }
            }
        }
    }
}





@Composable
fun LoginScreen(auth: FirebaseAuth, navController: NavHostController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isFormFilled = email.isNotBlank() && password.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isFormFilled) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                // Navigate to the main content
                                navController.navigate(Screen.Matkul.route) {
                                    // Clear the back stack to prevent returning to the login screen
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            },
            enabled = isFormFilled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormFilled) MaterialTheme.colorScheme.primary else Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}





@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.matkul),
                icon = Icons.Default.Search,
                screen = Screen.Matkul
            ),
            NavigationItem(
                title = stringResource(R.string.tugas),
                icon = Icons.Default.Check,
                screen = Screen.Tugas
            ),
            NavigationItem(
                title = stringResource(R.string.profil),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profil
            )
        )


        val currentDestination = navController.currentBackStackEntryAsState().value?.destination


        navigationItems.map { item ->
            val isSelected = currentDestination?.route == item.screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.screen.route) {

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        restoreState = true

                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun MainActivityContent(
    auth: FirebaseAuth,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val isUserLoggedIn = auth.currentUser != null


    if (isUserLoggedIn) {
        Scaffold(bottomBar = { BottomBar(navController) },
            modifier = modifier) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Matkul.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Matkul.route) {
                    MatkulScreen()
                }
                composable(Screen.Tugas.route) {

                }
                composable(Screen.Profil.route) {
                    GithubProfileScreen()
                }
            }
        }
    } else {

        LoginScreen(auth = auth, navController = navController)
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Praktikum2Theme {
        LoginScreen(FirebaseAuth.getInstance(), rememberNavController())
    }
}
