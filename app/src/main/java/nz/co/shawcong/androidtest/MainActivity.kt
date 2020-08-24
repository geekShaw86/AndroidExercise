package nz.co.shawcong.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.experimental.xor

class MainActivity : AppCompatActivity() {

    private val tvISO3PinBlock: TextView by lazy { findViewById<TextView>(R.id.tv_iso_3_pin_block) }

    private val pinByteListInput = arrayListOf<Byte>()

    companion object {
        private val HARD_CODED_PAN = listOf<Byte>(4, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.edt_pin_number).apply {
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    textView: TextView?,
                    actionId: Int,
                    eventKey: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        textView?.text?.let {
                            pinByteListInput.clear()
                            if (it.length > 3) {
                                it.reversed().forEach { char ->
                                    pinByteListInput.add(Character.getNumericValue(char).toByte())
                                }
                                if (pinByteListInput.size < HARD_CODED_PAN.size) {
                                    repeat(HARD_CODED_PAN.size - pinByteListInput.size) {
                                        pinByteListInput.add(0)
                                    }
                                }
                                val result = ByteArray(HARD_CODED_PAN.size)
                                for (i in HARD_CODED_PAN.indices) {
                                    result[i] = HARD_CODED_PAN[i] xor pinByteListInput[i]
                                }
                                val resultString = result.reversed().joinToString("") { "%01X".format(it) }

                                tvISO3PinBlock.text = resultString
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Pin should be at least has 4 digits",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        return true
                    }
                    return false
                }
            })
        }
    }
}