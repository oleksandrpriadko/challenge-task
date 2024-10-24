package com.oleksandrpriadko.entain.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.oleksandrpriadko.VisibleForTesting
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.domain.model.Race
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * I remember that you use Hilt, I used it before either and I can use it if needed but I want to
 * show you how koin works :)
 */
@KoinViewModel
class MainViewModel(
    private val observeRacesUseCase: ObserveRacesUseCase
) : MainViewModelContract.ViewModel() {
    private val viewModelState = MutableStateFlow(ViewModelState())

    val uiState = viewModelState
        .map(ViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    /**
     * All race categories selected by default
     */
    override val selectedCategories: MutableSet<RaceCategory> =
        mutableSetOf(RaceCategory.HORSE, RaceCategory.HARNESS, RaceCategory.GREYHOUND)

    init {
        observeRaces()
    }

    /**
     * I replicated the behaviour of https://www.neds.com.au/next-to-go
     * When the last selected chip is unselected -> All chips get selected
     */
    override fun changeCategorySelection(raceCategory: RaceCategory, isSelected: Boolean) {
        if (isSelected)
            selectedCategories.add(raceCategory)
        else {
            selectedCategories.remove(raceCategory)
            if (selectedCategories.isEmpty()) {
                selectedCategories.addAll(RaceCategory.entries)
            }
        }
        // update chip selection
        viewModelState.update { it.copy(selectedCategories = selectedCategories) }
    }

    override fun observeRaces() {
        viewModelScope.launch {
            // show loading
            viewModelState.update { it.copy(isLoading = true) }
            observeRacesUseCase(
                ObserveRacesUseCase.InputParams(selectedCategories)
            ).collect { result ->
                when (result) {
                    is ObserveRacesUseCase.Result.Success.Filled ->
                        // has races
                        viewModelState.update { it.copy(races = result.races, isLoading = false) }
                    ObserveRacesUseCase.Result.Success.Empty ->
                        // no races
                        viewModelState.update { it.copy(isEmpty = true, isLoading = false) }
                    is ObserveRacesUseCase.Result.Error ->
                        // error
                        viewModelState.update { it.copy(hasError = true, isLoading = false) }
                }
            }
        }
    }
}

@Stable
sealed interface UiState {
    val isLoading: Boolean
    val selectedCategories: Set<RaceCategory>

    @Stable
    data class HasRaces(
        val races: List<Race>, override val isLoading: Boolean,
        override val selectedCategories: Set<RaceCategory>
    ) : UiState

    @Stable
    data class NoRaces(
        override val isLoading: Boolean,
        override val selectedCategories: Set<RaceCategory>
    ) : UiState

    @Stable
    data class Error(
        override val isLoading: Boolean,
        override val selectedCategories: Set<RaceCategory>
    ) : UiState
}

@Stable
private data class ViewModelState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val hasError: Boolean = false,
    val selectedCategories: Set<RaceCategory> = setOf(
        RaceCategory.HORSE,
        RaceCategory.HARNESS,
        RaceCategory.GREYHOUND
    ),
    val races: List<Race> = emptyList()
) {
    fun toUiState(): UiState {
        return when {
            isEmpty ->
                UiState.NoRaces(
                    isLoading = isLoading,
                    selectedCategories = selectedCategories
                )
            hasError ->
                UiState.Error(
                    isLoading = isLoading,
                    selectedCategories = selectedCategories
                )
            else ->
                UiState.HasRaces(
                    races = races,
                    isLoading = isLoading,
                    selectedCategories = selectedCategories
                )
        }
    }
}

interface MainViewModelContract {
    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        @VisibleForTesting
        abstract val selectedCategories: MutableSet<RaceCategory>
        abstract fun changeCategorySelection(raceCategory: RaceCategory, isSelected: Boolean)

        @VisibleForTesting
        abstract fun observeRaces()
    }
}