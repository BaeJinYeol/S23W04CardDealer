package kr.ac.kumoh.s20190558.s23w04carddealer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.s20190558.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        model = ViewModelProvider(this)[CardDealerViewModel::class.java]

        model.cards.observe(this) {
            val res = IntArray(5)
            for (i in res.indices) {
                res[i] = getCardDrawableId(it[i])
                val cardView: ImageView? = when (i) {
                    0 -> main.card1
                    1 -> main.card2
                    2 -> main.card3
                    3 -> main.card4
                    4 -> main.card5
                    else -> null
                }
                cardView?.setImageResource(res[i])
            }
        }
        model.handRank.observe(this) {
            main.txtRank.text = it
        }
        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }

    }
    @SuppressLint("DiscouragedApi")
    private fun getCardDrawableId(c: Int): Int {
        var shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "error"
        }

        val number = when (c % 13) {
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> {
                shape = shape.plus("2")
                "jack"
            }
            11 -> {
                shape = shape.plus("2")
                "queen"
            }
            12 -> {
                shape = shape.plus("2")
                "king"
            }
            else -> "error"
        }
        val resName = "c_${number}_of_${shape}"
        return resources.getIdentifier(resName, "drawable", packageName)
    }
}