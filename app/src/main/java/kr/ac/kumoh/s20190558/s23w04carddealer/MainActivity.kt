package kr.ac.kumoh.s20190558.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.s20190558.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        model = ViewModelProvider(this)[CardDealerViewModel::class.java]
        // !!는 null 이 될수가 절대 없다는 걸 알려줌
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in res.indices) {
                val resourceId = resources.getIdentifier(
                    getCardName(it[i]), "drawable", packageName
                )
                val cardView: ImageView? = when (i) {
                    0 -> main.card1
                    1 -> main.card2
                    2 -> main.card3
                    3 -> main.card4
                    4 -> main.card5
                    else -> null
                }
                cardView?.setImageResource(resourceId)
            }
            main.txtRank.text = getPedigree(res)
        })

        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }

    }
    private fun getCardName(c: Int): String {
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
        return "c_${number}_of_${shape}"
    }
    private fun getPedigree(deck: IntArray): String {
        var pedigree = "No Hand"
        val cardArray = IntArray(13) {0}
        val patternArray = IntArray(4) {0}


        return pedigree
    }
}