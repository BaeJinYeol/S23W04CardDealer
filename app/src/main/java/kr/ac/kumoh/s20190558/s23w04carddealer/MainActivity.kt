package kr.ac.kumoh.s20190558.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        // !!는 널이 될수가 절대 없다는 걸 알려줌
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in res.indices) {
                res[i] = resources.getIdentifier(
                    getCardName(it[i]), // model.cards.values!![i]
                    "drawable",
                    packageName
                )
            }
            main.card1.setImageResource(res[0])
        })

        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }

    }
    private fun getCardName(c: Int): String { // TODO: int형으로 반환 할 것
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
//        return if (number in arrayOf("jack", "queen", "king"))
//            "c_${number}_of_${shape}2"
//        else
//            "c_${number}_of_${shape}"
        return "c_${number}_of_${shape}"
    }

}