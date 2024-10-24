package com.oleksandrpriadko.backend.data.repository

import com.oleksandrpriadko.Helper
import com.oleksandrpriadko.backend.data.datasource.BackendNetworkDataSource
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList

class BackendRepositoryImplTest : BehaviorSpec({
    Given("map to result, nullable") {
        val dataSource = mockk<BackendNetworkDataSource>()
        coEvery { dataSource.observeRaces() } returns flowOf(Result.failure(NullPointerException()))
        val repositoryImpl = BackendRepositoryImpl(dataSource)
        When("map to result") {
            Then("failure") {
                repositoryImpl.observerRaces().toList().first().isFailure.shouldBeTrue()
            }
        }
    }

    Given("map to result, filled") {
        val dataSource = mockk<BackendNetworkDataSource>()
        coEvery { dataSource.observeRaces() } returns
                flowOf(Result.success(Helper.getResponseBackend()))
        val repositoryImpl = BackendRepositoryImpl(dataSource)
        When("map to result") {
            Then("success") {
                repositoryImpl.observerRaces().toList().first().isSuccess.shouldBeTrue()
            }
        }
    }
})
