package com.oleksandrpriadko.backend.data.datasource

import com.oleksandrpriadko.Helper
import com.oleksandrpriadko.Kermit
import com.oleksandrpriadko.kermit
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

class BackendNetworkDataSourceImplTest : BehaviorSpec({
    kermit = Kermit(true)
    // allowRequest
    Given("allowRequest out of range") {
        val api = mockk<BackendApiV1>()
        val backendNetwork = BackendNetworkDataSourceImpl(api)
        When("allowRequest") {
            Then("not allowed") {
                backendNetwork.allowRequest(Clock.System.now().plus(2.minutes)).shouldBeFalse()
            }
        }
    }
    Given("allowRequest in range") {
        val api = mockk<BackendApiV1>()
        val backendNetwork = BackendNetworkDataSourceImpl(api)
        When("allowRequest") {
            Then("allowed") {
                backendNetwork.allowRequest(Clock.System.now()).shouldBeFalse()
            }
        }
    }

    // observeRaces
    Given("observeRaces, nullable response") {
        val api = mockk<BackendApiV1>()
        coEvery { api.getDataResponseBackend() } returns null
        val backendNetwork = BackendNetworkDataSourceImpl(api)
        When("observeRaces") {
            Then("return failure") {
                backendNetwork.observeRaces(isContinuous = false).collect {
                    it.isFailure.shouldBeTrue()
                }
            }
        }
    }
    Given("observeRaces, filled response") {
        val api = mockk<BackendApiV1>()
        coEvery { api.getDataResponseBackend() } returns Helper.getResponseBackend()
        val backendNetwork = BackendNetworkDataSourceImpl(api)
        When("observeRaces") {
            Then("return success") {
                backendNetwork.observeRaces(isContinuous = false).collect {
                    it.isSuccess.shouldBeTrue()
                }
            }
        }
    }
})
