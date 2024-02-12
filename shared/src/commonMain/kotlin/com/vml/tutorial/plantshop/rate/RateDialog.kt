package com.vml.tutorial.plantshop.rate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vml.tutorial.plantshop.MR.strings.cancel_text
import com.vml.tutorial.plantshop.MR.strings.rate_dialog_body
import com.vml.tutorial.plantshop.MR.strings.rate_dialog_comment_placeholder_text
import com.vml.tutorial.plantshop.MR.strings.rate_dialog_title
import com.vml.tutorial.plantshop.MR.strings.submit_text
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.rate.domain.Rating
import com.vml.tutorial.plantshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateDialog(
    orderId: String,
    rating: Rating? = null,
    onSubmitRating: (rating: Rating) -> Unit,
    onDismissRequest: () -> Unit
) {
    var currentRating by remember { mutableIntStateOf(rating?.rating.orZero()) }
    var currentComment by remember { mutableStateOf(rating?.comment.orEmpty()) }

    Dialog(onDismissRequest = onDismissRequest) {
        val focusManager = LocalFocusManager.current

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = UiText.StringRes(rate_dialog_title).asString(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = UiText.StringRes(rate_dialog_body).asString(),
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                RatingBar(
                    currentRating = currentRating,
                    tintColor = MaterialTheme.colorScheme.primary,
                    onRatingChanged = {
                        currentRating = it
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = currentComment,
                    placeholder = {
                        Text(
                            text = UiText.StringRes(rate_dialog_comment_placeholder_text).asString(),
                            style = Typography.labelLarge,
                        )},
                    onValueChange = { currentComment = it },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = {
                            onDismissRequest.invoke()
                        }
                    ) {
                        Text(UiText.StringRes(cancel_text).asString())
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        enabled = currentRating > 0,
                        onClick = {
                            onSubmitRating(rating?.copy(comment = currentComment, rating = currentRating, orderId = orderId)
                                ?: Rating(comment = currentComment, rating = currentRating, orderId = orderId))
                        }
                    ) {
                        Text(UiText.StringRes(submit_text).asString())
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    currentRating: Int,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    tintColor: Color = Color.Yellow,
    onRatingChanged: (Int) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        for (i in 1 .. maxRating) {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = {
                    onRatingChanged(i)
                }
            ) {
                Icon(
                    imageVector = if (i <= currentRating) Icons.Filled.Star else Icons.Filled.StarOutline,
                    contentDescription = null,
                    tint = if (i <= currentRating) tintColor else Color.Unspecified,
                )
            }
        }
    }
}