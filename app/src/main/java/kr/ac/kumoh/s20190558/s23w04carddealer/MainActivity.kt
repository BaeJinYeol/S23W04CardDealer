package kr.ac.kumoh.s20190558.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
                res[i] = resources.getIdentifier(
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
                Log.i("Card NUM!!", "${res[i]} + ${i+1} + ${it[i]}")
                cardView?.setImageResource(res[i])
            }
            main.txtRank.text = getPedigree(it)
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
        val cardMatrix = Array(4) { Array(13) { 0 } }

        for (cardNum in deck) {
            val cPattern = cardNum / 13
            val cNum = cardNum % 13
            cardMatrix[cPattern][cNum]++
        }

        // 로얄 스트레이트 플러쉬
        for (i in 0..3) {
            if (cardMatrix[i][8] == 1 && cardMatrix[i][9] == 1 && cardMatrix[i][10] == 1 &&
                cardMatrix[i][11] == 1 && cardMatrix[i][12] == 1){
                return "Royal Straight Flush"
            }
        }
        // 스트레이트 플러쉬
        for (i in 0..3) {
            for (j in 1..8) {
                if (cardMatrix[i][j] == 1 && cardMatrix[i][j+1] == 1 && cardMatrix[i][j+2] == 1 &&
                    cardMatrix[i][j+3] == 1 && cardMatrix[i][j+4] == 1) {
                    return "Straight Flush"
                }
            }
        }
        // 포카드
        for (j in 0..12) {
            var count = 0
            for (i in 0..3){
                if (cardMatrix[i][j] == 1) count++
            }
            if (count >= 4) {
                return "Four of a Kind"
            }
        }
        // 풀 하우스

        // 플러쉬 (같은 무늬 5장)
        for (i in 0..3) {
            var count = 0
            for (j in 0..12){
                if (cardMatrix[i][j] == 1) count++
            }
            if (count >= 5) {
                return "Flush"
            }
        }
        // 스트레이트
        val uniqueNumbers: IntArray = IntArray(5)
        for (i in deck.indices) {
            uniqueNumbers[i] = deck[i] % 13
        }
        uniqueNumbers.sort()
        for (i in 0..8) {
            if (uniqueNumbers[0] == i && uniqueNumbers[1] == i + 1 && uniqueNumbers[2] == i + 2 &&
                uniqueNumbers[3] == i + 3 && uniqueNumbers[4] == i + 4) {
                return "Straight"
            }
        }
        // 쓰리 오브 어 카인드, 트립스, 세트
        for (j in 0..12) {
            var count = 0
            for (i in 0..3){
                if (cardMatrix[i][j] == 1) count++
            }
            if (count >= 3) {
                return "Three of a Kind"
            }
        }
        // 투 페어
        var onePair = false
        for (j in 0..12) {
            var count = 0
            for (i in 0..3){
                if (cardMatrix[i][j] == 1) count++
            }
            if (onePair && count >= 2){
                return "Two Pairs"
            }
            if (count >= 2) {
                onePair = true
            }
        }
        // 원 페어
        for (j in 0..12) {
            var count = 0
            for (i in 0..3){
                if (cardMatrix[i][j] == 1) count++
            }
            if (count >= 2) {
                return "One Pair"
            }
        }
        // 노페어 High Card
        return "No Pairs"
    }
}