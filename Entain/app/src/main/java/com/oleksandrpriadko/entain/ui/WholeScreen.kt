package com.oleksandrpriadko.entain.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.domain.model.Race
import com.oleksandrpriadko.entain.ui.theme.EntainTheme
import com.oleksandrpriadko.kermit
import com.oleksandrpriadkoentain.R

@Composable
fun RaceList(
    modifier: Modifier = Modifier,
    commonPadding: Dp = 10.dp,
    uiState: UiState,
    onCategorySelectionChanged: (RaceCategory, Boolean) -> Unit
) {
    if (!LocalInspectionMode.current) kermit.d { "New UiState ${uiState.javaClass.simpleName}" }
    Column(
        modifier = modifier.padding(horizontal = commonPadding),
        verticalArrangement = Arrangement.spacedBy(commonPadding)
    ) {
        LoadingIndicator(uiState, commonPadding)
        Races(uiState, commonPadding, onCategorySelectionChanged)
    }
}

@Composable
private fun LoadingIndicator(uiState: UiState, commonPadding: Dp) {
    if (uiState.isLoading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = commonPadding),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun Races(
    uiState: UiState,
    commonPadding: Dp,
    onCategorySelectionChanged: (RaceCategory, Boolean) -> Unit
) {
    when (uiState) {
        is UiState.HasRaces -> {
            Filters(uiState, onCategorySelectionChanged)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    uiState.races.forEach { race ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // replicated part of the UI from https://www.neds.com.au/next-to-go
                                // category, race number, meeting name, time to start
                                TextRace(text = "(${race.category.name})".lowercase())
                                TextRace(text = "R${race.number} ${race.meetingName.uppercase()}")
                                TextRace(text = race.timeToStart)
                            }
                            Spacer(Modifier.height(commonPadding))
                        }
                    }
                }
            }
        }
        is UiState.NoRaces -> Text(modifier = Modifier, text = stringResource(R.string.no_races))
        is UiState.Error -> Text(
            modifier = Modifier,
            text = stringResource(R.string.error_try_again)
        )
    }
}

@Composable
fun Filters(
    uiState: UiState,
    onCategorySelectionChanged: (RaceCategory, Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        RaceCategory.entries.forEach { raceCategory ->
            val selected = uiState.selectedCategories.contains(raceCategory)
            FilterChip(
                modifier = Modifier,
                onClick = { onCategorySelectionChanged(raceCategory, !selected) },
                label = { Text(raceCategory.name, overflow = TextOverflow.Ellipsis) },
                selected = selected,
                leadingIcon = if (selected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}

@Composable
private fun TextRace(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold
    )
}

@Preview(showBackground = true)
@Composable
fun WholeScreenHasRaces() {
    EntainTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RaceList(
                modifier = Modifier.padding(innerPadding),
                uiState = UiState.HasRaces(
                    isLoading = true,
                    races = listOf(
                        Race(
                            id = "",
                            meetingName = "first name",
                            number = 1,
                            startSeconds = 0,
                            timeToStart = "50s",
                            category = RaceCategory.HORSE
                        ),
                        Race(
                            id = "",
                            meetingName = "second name",
                            number = 2,
                            startSeconds = 0,
                            timeToStart = "40s",
                            category = RaceCategory.GREYHOUND
                        ),
                        Race(
                            id = "",
                            meetingName = "third name",
                            number = 3,
                            startSeconds = 0,
                            timeToStart = "30s",
                            category = RaceCategory.GREYHOUND
                        ),
                        Race(
                            id = "",
                            meetingName = "fourth name",
                            number = 4,
                            startSeconds = 0,
                            timeToStart = "30s",
                            category = RaceCategory.HORSE
                        ),
                        Race(
                            id = "",
                            meetingName = "fifth name",
                            number = 5,
                            startSeconds = 0,
                            timeToStart = "30s",
                            category = RaceCategory.GREYHOUND
                        )
                    ),
                    selectedCategories = setOf(
                        RaceCategory.HORSE,
                        RaceCategory.GREYHOUND
                    )
                ),
                onCategorySelectionChanged = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WholeScreenNoRaces() {
    EntainTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RaceList(
                modifier = Modifier.padding(innerPadding),
                uiState = UiState.NoRaces(
                    isLoading = true,
                    selectedCategories = setOf(
                        RaceCategory.HORSE,
                        RaceCategory.GREYHOUND
                    )
                ),
                onCategorySelectionChanged = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WholeScreenError() {
    EntainTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RaceList(
                modifier = Modifier.padding(innerPadding),
                uiState = UiState.Error(
                    isLoading = true,
                    selectedCategories = setOf(
                        RaceCategory.HORSE,
                        RaceCategory.GREYHOUND
                    )
                ),
                onCategorySelectionChanged = { _, _ -> }
            )
        }
    }
}