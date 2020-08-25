package nz.co.shawcong.androidtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.unmockkAll
import io.mockk.every
import io.mockk.slot
import io.mockk.coVerify
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import nz.co.shawcong.androidtest.manager.ConfigManager
import nz.co.shawcong.androidtest.viewmodel.MainViewModel
import org.junit.Rule
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 *
 * Created by Shaw Cong on 25/08/20
 */
@ExperimentalCoroutinesApi
class MainViewModelTests {

    @get: Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var configManager: ConfigManager

    private lateinit var mainViewModel: MainViewModel

    @RelaxedMockK
    private lateinit var pinBlockObserver: Observer<String>

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)

        mainViewModel = MainViewModel(configManager, coroutinesTestRule.testDispatcherProvider)
        mainViewModel.pinBlockLiveData.observeForever(pinBlockObserver)
    }

    @AfterTest
    fun cleanup() {
        unmockkAll()
    }

    @Test
    fun `isValidPinNumber() should return false when minLengthOfPin is bigger than maxLengthOfPin`() {
        every { configManager.minLengthOfPin } returns 4
        every { configManager.maxLengthOfPin } returns 3

        assert(!mainViewModel.isValidPinNumber(4))
    }

    @Test
    fun `isValidPinNumber() should return true when both minLengthOfPin and maxLengthOfPin equal to length of pin number input by user`() {
        every { configManager.minLengthOfPin } returns 4
        every { configManager.maxLengthOfPin } returns 4

        assert(mainViewModel.isValidPinNumber(4))
    }

    @Test
    fun `isValidPinNumber() should return false when length of pin number input by user is smaller than minLengthOfPin or bigger than maxLengthOfPin`() {
        every { configManager.minLengthOfPin } returns 4
        every { configManager.maxLengthOfPin } returns 12

        assert(!mainViewModel.isValidPinNumber(3))
        assert(!mainViewModel.isValidPinNumber(13))
    }

    @Test
    fun `isValidPinNumber() should return true when length of pin number input by user is between minLengthOfPin and maxLengthOfPin inclusively`() {
        every { configManager.minLengthOfPin } returns 4
        every { configManager.maxLengthOfPin } returns 12

        assert(mainViewModel.isValidPinNumber(4))
        assert(mainViewModel.isValidPinNumber(12))
        assert(mainViewModel.isValidPinNumber(7))
    }

    @Test
    fun `When length of defaultPAN is smaller than length of pin number input by user getISO3PinBlock() should do nothing`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val captor = slot<String>()

            every { configManager.defaultPAN } returns "1234"
            val testPinNumber = "12345"

            mainViewModel.calculateISO3PinBlock(testPinNumber)

            coVerify(exactly = 0) { pinBlockObserver.onChanged(capture(captor)) }
        }
    }

    @Test
    fun `When length of defaultPAN equals to length of pin number input by user getISO3PinBlock() should return right result`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val captor = slot<String>()

            every { configManager.defaultPAN } returns "1111222233334444"
            val testPinNumber = "1111222233334444"

            mainViewModel.calculateISO3PinBlock(testPinNumber)

            coVerify(exactly = 1) { pinBlockObserver.onChanged(capture(captor)) }
            assert( captor.captured == "0000000000000000")
        }
    }

    @Test
    fun `When length of defaultPAN is bigger than length of pin number input by user getISO3PinBlock() should return right result`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val captor = slot<String>()

            every { configManager.defaultPAN } returns "1111222233334444"
            val testPinNumber = "123456789123"

            mainViewModel.calculateISO3PinBlock(testPinNumber)

            coVerify(exactly = 1) { pinBlockObserver.onChanged(capture(captor)) }
            assert( captor.captured == "11113016654BD567")
        }
    }
}