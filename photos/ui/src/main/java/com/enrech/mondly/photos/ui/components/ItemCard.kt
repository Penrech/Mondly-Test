package com.enrech.mondly.photos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.enrech.mondly.core.ui.extension.shimmerEffect
import com.enrech.mondly.design_system.theme.MondlyTheme
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.ui.R

@Composable
fun ItemCard(data: PhotoEntity) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.imageUrl)
                .crossfade(true)
                .build(),
            error = painterResource(id = com.enrech.mondly.design_system.R.drawable.ic_placeholder),
            placeholder = painterResource(id = com.enrech.mondly.design_system.R.drawable.ic_placeholder),
            contentDescription = stringResource(R.string.image_cd, data.name),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .aspectRatio(1f)
        )
        Column(
            Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 14.dp)
        ) {
            Text(
                text = data.name.ifBlank {
                    stringResource(
                        id = R.string.image_cd,
                        data.id
                    )
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.description,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ItemCardPlaceholder() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .shimmerEffect()
    )
}

@Preview
@Composable
private fun ItemCardPreview() {
    MondlyTheme {
        ItemCard(
            data = PhotoEntity(
                1,
                name = "Some Image Name",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque auctor, magna tempus molestie vestibulum, nulla libero imperdiet risus, vel vehicula.",
                ""
            )
        )
    }
}

@Preview
@Composable
private fun ItemCardPreviewWithoutName() {
    MondlyTheme {
        ItemCard(
            data = PhotoEntity(
                1,
                name = "",
                description = "",
                imageUrl = ""
            )
        )
    }
}

@Preview
@Composable
private fun ItemCardPlaceholderPreview() {
    MondlyTheme {
        ItemCardPlaceholder()
    }
}