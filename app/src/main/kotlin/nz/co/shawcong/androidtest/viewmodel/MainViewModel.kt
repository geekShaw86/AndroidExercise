package nz.co.shawcong.androidtest.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.co.shawcong.androidtest.dispatcher.DispatcherProvider
import nz.co.shawcong.androidtest.manager.ConfigManager
import kotlin.experimental.xor

/**
 *
 * Created by Shaw Cong on 25/08/20
 */
class MainViewModel @ViewModelInject constructor(
    private val configManager: ConfigManager,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val PLACEHOLDER_DIGIT: Byte = 0
        private const val STRING_JOIN_SEPARATOR = ""
        private const val HEX_STRING_FORMAT = "%01X"
    }

    private val pinBlockLiveDataInternal = MutableLiveData<String>()
    val pinBlockLiveData: LiveData<String> = pinBlockLiveDataInternal

    private val pinByteListInput = arrayListOf<Byte>()

    private val panByteList = arrayListOf<Byte>()

    fun calculateISO3PinBlock(pinInputByUser: String) {
        viewModelScope.launch {
            getISO3PinBlock(pinInputByUser)
        }
    }

    fun isValidPinNumber(pinNumberLength: Int): Boolean = pinNumberLength in configManager.minLengthOfPin..configManager.maxLengthOfPin

    private suspend fun getISO3PinBlock(pinInputByUser: String) {
        if (configManager.defaultPAN.length < pinInputByUser.length) return
        withContext(dispatchers.default()) {
            pinByteListInput.clear()
            panByteList.clear()

            configManager.defaultPAN.reversed().forEach { char ->
                panByteList.add(Character.getNumericValue(char).toByte())
            }

            pinInputByUser.reversed().forEach { char ->
                pinByteListInput.add(Character.getNumericValue(char).toByte())
            }

            if (pinByteListInput.size < panByteList.size) {
                repeat(panByteList.size - pinByteListInput.size) {
                    pinByteListInput.add(PLACEHOLDER_DIGIT)
                }
            }

            val result = ByteArray(panByteList.size)
            for (i in panByteList.indices) {
                result[i] = panByteList[i] xor pinByteListInput[i]
            }
            val resultString = result.reversed().joinToString(STRING_JOIN_SEPARATOR) { HEX_STRING_FORMAT.format(it) }
            pinBlockLiveDataInternal.postValue(resultString)
        }
    }
}