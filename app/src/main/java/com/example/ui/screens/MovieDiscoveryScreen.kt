package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.local.entities.MovieReviewEntity
import com.example.data.model.MovieItem
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.GoldPro
import com.example.ui.theme.VioletSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDiscoveryScreen(
    initialToolId: String? = null,
    movies: List<MovieItem>,
    movieReviews: List<MovieReviewEntity>,
    onToggleWatchlist: (MovieItem) -> Unit,
    onSaveReview: (String, String, String, String, Int, String) -> Unit,
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var selectedMovieForReview by remember { mutableStateOf<MovieItem?>(null) }
    var selectedMovieForTrailer by remember { mutableStateOf<MovieItem?>(null) }

    val genres = listOf("All", "Sci-Fi", "Action", "Drama", "Animation", "Biography")

    val filteredMovies = movies.filter { m ->
        val matchesGenre = selectedGenre == "All" || m.genre.contains(selectedGenre, ignoreCase = true)
        val matchesSearch = searchQuery.isEmpty() || m.title.contains(searchQuery, ignoreCase = true) || m.director.contains(searchQuery, ignoreCase = true)
        matchesGenre && matchesSearch
    }

    val watchlistMovieIds = movieReviews.filter { it.isWatchlist }.map { it.movieId }.toSet()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("movie_discovery_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Movie & Cinema Discovery",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "ENTERTAINMENT", containerColor = GoldPro, contentColor = Color.Black)
                        }
                        Text(
                            text = "Curated database, ratings, watchlist journal & trailer search",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // SEARCH & GENRE FILTER BAR
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search movies, cast, director...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = CyanPrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { g ->
                    FilterChip(
                        selected = selectedGenre == g,
                        onClick = { selectedGenre = g },
                        label = { Text(g) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                            selectedLabelColor = CyanPrimary
                        )
                    )
                }
            }
        }

        // MOVIES LIST
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredMovies) { movie ->
                val isWatchlisted = watchlistMovieIds.contains(movie.id)
                val userReviewItem = movieReviews.find { it.movieId == movie.id }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        // Poster Image
                        AsyncImage(
                            model = movie.posterUrl,
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(90.dp)
                                .height(130.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )

                        Spacer(Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                IconButton(
                                    onClick = { onToggleWatchlist(movie) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isWatchlisted) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Watchlist",
                                        tint = if (isWatchlisted) GoldPro else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = GoldPro, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "${movie.rating}/10",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPro
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "• ${movie.year} • ${movie.duration}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = movie.genre,
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanPrimary
                            )

                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 16.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { selectedMovieForTrailer = movie },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Icon(Icons.Default.OndemandVideo, null, Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Trailer", style = MaterialTheme.typography.labelSmall)
                                }

                                Button(
                                    onClick = { selectedMovieForReview = movie },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = VioletSecondary,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.height(32.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Icon(Icons.Default.RateReview, null, Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text(if (userReviewItem != null) "Edit Review" else "Rate & Review", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // REVIEW MODAL DIALOG
    if (selectedMovieForReview != null) {
        val m = selectedMovieForReview!!
        val existing = movieReviews.find { it.movieId == m.id }
        var ratingStars by remember { mutableIntStateOf(existing?.userRating ?: 5) }
        var reviewText by remember { mutableStateOf(existing?.userReview ?: "") }

        Dialog(onDismissRequest = { selectedMovieForReview = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Review ${m.title}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Text("Your Score: $ratingStars / 5 Stars", color = GoldPro, fontWeight = FontWeight.Bold)
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { ratingStars = star }) {
                                Icon(
                                    Icons.Default.Star,
                                    null,
                                    tint = if (star <= ratingStars) GoldPro else MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        label = { Text("Your Critique & Thoughts") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = { selectedMovieForReview = null }) { Text("Cancel") }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onSaveReview(m.id, m.title, m.year, m.posterUrl, ratingStars, reviewText)
                                selectedMovieForReview = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        ) {
                            Text("Save Review")
                        }
                    }
                }
            }
        }
    }

    // TRAILER SEARCH MODAL DIALOG
    if (selectedMovieForTrailer != null) {
        val m = selectedMovieForTrailer!!
        Dialog(onDismissRequest = { selectedMovieForTrailer = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${m.title} Official HD Trailer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { selectedMovieForTrailer = null }) {
                            Icon(Icons.Default.Close, null)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            AsyncImage(
                                model = m.posterUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.75f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.OndemandVideo, null, tint = CyanPrimary, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Streaming Trailer Link Ready", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            onShowNotification("Opening official YouTube trailer for ${m.title}!")
                            selectedMovieForTrailer = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                    ) {
                        Text("Stream Official Trailer in 4K", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
