package dev.therealashik.github

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.therealashik.github.explore.ExploreScreen
import dev.therealashik.github.inbox.InboxScreen
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.coming_soon
import github.composeapp.generated.resources.tab_copilot
import github.composeapp.generated.resources.tab_explore
import github.composeapp.generated.resources.tab_home
import github.composeapp.generated.resources.tab_inbox
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        Res.string.tab_home,
        Res.string.tab_inbox,
        Res.string.tab_explore,
        Res.string.tab_copilot
    )
    val outlinedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Email,
        Icons.Outlined.Search,
        Icons.Outlined.Person
    )
    val filledIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Email,
        Icons.Filled.Search,
        Icons.Filled.Person
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, titleRes ->
                    val title = stringResource(titleRes)
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            if (selectedTab == index) {
                                Icon(filledIcons[index], contentDescription = title)
                            } else {
                                Icon(outlinedIcons[index], contentDescription = title)
                            }
                        },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                1 -> InboxScreen()
                2 -> ExploreScreen()
                else -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.coming_soon))
                }
            }
        }
    }
}
