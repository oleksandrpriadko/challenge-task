package com.oleksandrpriadko.entain.ui

import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : BehaviorSpec({
    Dispatchers.setMain(Dispatchers.Unconfined)
    val inputParams = ObserveRacesUseCase.InputParams(
        setOf(
            RaceCategory.HORSE,
            RaceCategory.HARNESS,
            RaceCategory.GREYHOUND
        )
    )
    Given("observeRaces, has races") {
        val useCase = mockk<ObserveRacesUseCase>()
        val viewModel = MainViewModel(useCase)
        coEvery { useCase(inputParams) } returns flowOf(
            ObserveRacesUseCase.Result.Success.Filled(Helper.getRaces())
        )
        When("observeRaces") {
            Then("has races") {
                viewModel.observeRaces()
                val result = viewModel.uiState.value
                result.shouldBeInstanceOf<UiState.HasRaces>()
                result.isLoading.shouldBeFalse()
                result.selectedCategories.shouldContainOnly(inputParams.raceCategories)
                result.races.shouldContainOnly(Helper.getRaces())
            }
        }
    }
    Given("observeRaces, no races") {
        val useCase = mockk<ObserveRacesUseCase>()
        val viewModel = MainViewModel(useCase)
        coEvery { useCase(inputParams) } returns flowOf(ObserveRacesUseCase.Result.Success.Empty)
        When("observeRaces") {
            Then("no races") {
                viewModel.observeRaces()
                val result = viewModel.uiState.value
                result.shouldBeInstanceOf<UiState.NoRaces>()
                result.isLoading.shouldBeFalse()
                result.selectedCategories.shouldContainOnly(inputParams.raceCategories)
            }
        }
    }
    Given("observeRaces, error") {
        val useCase = mockk<ObserveRacesUseCase>()
        val viewModel = MainViewModel(useCase)
        coEvery { useCase(inputParams) } returns
                flowOf(ObserveRacesUseCase.Result.Error(NullPointerException()))
        When("observeRaces") {
            Then("error") {
                viewModel.observeRaces()
                val result = viewModel.uiState.value
                result.shouldBeInstanceOf<UiState.Error>()
                result.isLoading.shouldBeFalse()
                result.selectedCategories.shouldContainOnly(inputParams.raceCategories)
            }
        }
    }
})
