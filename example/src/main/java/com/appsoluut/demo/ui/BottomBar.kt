package com.appsoluut.demo.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {
    BottomNavigation(modifier = modifier) {
        BottomNavigationItem(
            selected = true,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Map"
                )
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account"
                )
            }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
        )
    }
}
