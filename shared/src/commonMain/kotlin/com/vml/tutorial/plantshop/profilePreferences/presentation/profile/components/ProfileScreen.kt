package com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR.strings.get_help
import com.vml.tutorial.plantshop.MR.strings.log_out
import com.vml.tutorial.plantshop.MR.strings.my_orders
import com.vml.tutorial.plantshop.MR.strings.preferences
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.ActionItem
import com.vml.tutorial.plantshop.profilePreferences.presentation.profile.ProfileState
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun ProfileScreen(profileState: ProfileState, onEvent: (ProfileEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {
                onEvent(ProfileEvent.NavigateBack)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        UserInfo(user = profileState.user, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))
        UserActions { onEvent(it) }
    }
}

@Composable
private fun UserInfo(user: User?, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ProfileLettermark(
            user = user,
            enabled = false,
            modifier = Modifier.size(96.dp),
            onClicked = { /* Do nothing */ }
        )
        Column {
            Text(
                text = "${user?.firstName} ${user?.lastName}",
                style = Typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            UserInfoItem(Icons.Default.Email, user?.email.orEmpty())
            UserInfoItem(Icons.Default.Phone, user?.phoneNumber.orEmpty())
        }
    }
}

@Composable
private fun UserInfoItem(
    iconResource: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Icon(
            iconResource,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.width(16.dp))
        Text(text = text, style = Typography.bodySmall)
    }
}

@Composable
private fun UserActions(modifier: Modifier = Modifier, onEvent: (ProfileEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        ActionItem(Icons.Default.Inventory, UiText.StringRes(my_orders).asString(), {
            onEvent(ProfileEvent.OnMyOrdersClicked)
        })
        ActionItem(Icons.Default.Help, UiText.StringRes(get_help).asString(), {
            onEvent(ProfileEvent.OnGetHelpClicked)
        })
        ActionItem(Icons.Default.Settings, UiText.StringRes(preferences).asString(), {
            onEvent(ProfileEvent.OnPreferencesClicked)
        })
        ActionItem(Icons.Default.Logout, UiText.StringRes(log_out).asString(), {
            onEvent(ProfileEvent.OnLogOutClicked)
        })
    }
}
