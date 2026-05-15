package dev.therealashik.github

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.therealashik.github.explore.ExploreScreen
import dev.therealashik.github.home.HomeScreen
import dev.therealashik.github.inbox.InboxScreen
import github.composeapp.generated.resources.Res
import github.composeapp.generated.resources.app_name
import github.composeapp.generated.resources.coming_soon
import github.composeapp.generated.resources.profile_name
import github.composeapp.generated.resources.tab_copilot
import github.composeapp.generated.resources.tab_explore
import github.composeapp.generated.resources.tab_home
import github.composeapp.generated.resources.tab_inbox
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onNavigateToProfile: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        Res.string.tab_home,
        Res.string.tab_inbox,
        Res.string.tab_explore,
        Res.string.tab_copilot
    )
    val selectedIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Email,
        Icons.Filled.Search,
        Icons.Filled.Person
    )
    val unselectedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Email,
        Icons.Outlined.Search,
        Icons.Outlined.Person
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = stringResource(Res.string.profile_name),
                        modifier = Modifier
                            .padding(end = Dimens.spacingMedium)
                            .clip(CircleShape)
                            .clickable { onNavigateToProfile() }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, titleRes ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = stringResource(titleRes)
                            )
                        },
                        label = { Text(stringResource(titleRes)) }
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
                0 -> HomeScreen()
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
