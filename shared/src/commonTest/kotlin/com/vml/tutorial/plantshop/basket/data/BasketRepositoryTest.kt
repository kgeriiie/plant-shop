package com.vml.tutorial.plantshop.basket.data

import com.vml.tutorial.plantshop.basket.domain.BasketDataSource
import com.vml.tutorial.plantshop.basket.domain.BasketItem
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class BasketRepositoryTest {
    @Mock
    private val dataSourceMock: BasketDataSource = mock(classOf<BasketDataSource>())
    private lateinit var repository: BasketRepositoryImpl

    @BeforeTest
    fun setupTests() = runTest {
        repository = BasketRepositoryImpl(dataSourceMock)
    }

    // region GET BASKET ITEMS
    @Test
    fun getBasketItems_whenDataSourceHasItem_thenResultsReceived() = runTest {
        // Given
        val expectedResults = listOf(BasketItem(plantId = 1, quantity = 1))

        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(expectedResults))

        val basketItemsFlowResults = mutableListOf<List<BasketItem>>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            // This will collects forever, we need to cancel it manually.
            repository.basketItemsFlow.toList(basketItemsFlowResults)
        }

        // When
        val results = repository.getBasketItems()

        // Then
        coVerify { dataSourceMock.getBasketItems() }.wasInvoked(exactly = 1)
        assertEquals(expected = expectedResults, actual = basketItemsFlowResults.last(), message = "Assert basketItemsFlow latest emitted value")
        assertEquals(expected = expectedResults, actual = results, message = "Assert getBasketItems() return value")

        job.cancel()
    }
    // endregion

    // region INSERT ITEM
    @Test
    fun insertItem_whenItemWithQuantityDoesNotExistsInDb_thenInsertItem() = runTest {
        // Given
        val testPlantId = 1
        val testQuantity = 1

        val existingBasketItems = listOf<BasketItem>()
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(existingBasketItems))

        // When
        repository.insertItem(testPlantId, testQuantity)

        // Then
        coVerify { dataSourceMock.insertItem(any()) }.wasInvoked(exactly = 1)
        coVerify { dataSourceMock.updateQuantity(any(), any()) }.wasNotInvoked()
        coVerify { dataSourceMock.getBasketItems() }.wasInvoked(exactly = 2)
    }

    @Test
    fun insertItem_whenItemWithNegativeQuantityDoesNotExistsInDb_thenThrowException() = runTest {
        // Given
        val testPlantId = 1
        val testQuantity = -1

        val existingBasketItems = listOf<BasketItem>()
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(existingBasketItems))

        // When
        assertFailsWith<RuntimeException> {
            repository.insertItem(testPlantId, testQuantity)
        }

        // Then
        coVerify { dataSourceMock.insertItem(any()) }.wasNotInvoked()
        coVerify { dataSourceMock.updateQuantity(any(), any()) }.wasNotInvoked()
        coVerify { dataSourceMock.getBasketItems() }.wasNotInvoked()
    }

    @Test
    fun insertItem_whenItemWhichExistsInDb_thenUpdateItem() = runTest {
        // Given
        val testPlantId = 1
        val testQuantity = 1
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(listOf()))

        val existingBasketItems = listOf(BasketItem(plantId = 1, quantity = 1))
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(existingBasketItems))

        // When
        repository.insertItem(testPlantId, testQuantity)

        // Then
        coVerify { dataSourceMock.insertItem(any()) }.wasNotInvoked()
        coVerify { dataSourceMock.updateQuantity(any(), any()) }.wasInvoked(exactly = 1)
        coVerify { dataSourceMock.getBasketItems() }.wasInvoked(exactly = 2)
    }
    // endregion

    // region UPDATE ITEM QUANTITY
    @Test
    fun updateItemQuantity_whenItemHasQuantity_thenUpdateQuantityInvoked() = runTest {
        // Given
        val testPlantId = 1
        val testQuantity = 1
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(listOf()))

        // When
        repository.updateItemQuantity(testPlantId, testQuantity)

        // Then
        coVerify { dataSourceMock.updateQuantity(any(), any()) }.wasInvoked(exactly = 1)
        coVerify { dataSourceMock.getBasketItems() }.wasInvoked(exactly = 1)
    }

    @Test
    fun updateItemQuantity_whenItemQuantityLessThanEqualZero_thenThrowException() = runTest {
        // Given
        val testPlantId = 1
        val testQuantity = 0

        // When
        assertFailsWith<RuntimeException> {
            repository.updateItemQuantity(testPlantId, testQuantity)
        }

        // Then
        coVerify { dataSourceMock.updateQuantity(any(), any()) }.wasNotInvoked()
        coVerify { dataSourceMock.getBasketItems() }.wasNotInvoked()
    }
    // endregion

    // region DELETE ITEM
    @Test
    fun deleteItem_whenInvokesDeleteItem_thenRefreshBasketItems() = runTest {
        // Given
        val testPlantId = 1
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(listOf()))

        // When
        repository.deleteItem(testPlantId)

        // Then
        coVerify { dataSourceMock.deleteItem(any()) }.wasInvoked()
        coVerify { dataSourceMock.getBasketItems() }.wasInvoked()
    }
    // endregion

    // region DELETE ALL
    @Test
    fun deleteAll_whenInvokesDeleteAll_thenRefreshBasketItems() = runTest {
        // Given
        every { dataSourceMock.getBasketItems() }.returns(MutableStateFlow(listOf()))

        // When
        repository.deleteAll()

        // Then
        coVerify { dataSourceMock.deleteAll() }.wasInvoked()
        coVerify { dataSourceMock.getBasketItems() }.wasInvoked()
    }
    // endregion
}