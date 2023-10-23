package kr.ac.kumoh.s20190558.s23w04carddealer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CardDealerViewModel : ViewModel() {
    private var _cards = MutableLiveData<IntArray>(IntArray(5) {-1})
    private var _handRank = MutableLiveData<String>()
    val cards: LiveData<IntArray>
        get() = _cards
    val handRank: LiveData<String>
        get() = _handRank
    fun shuffle() {
        var num = 0
        val newCards = IntArray(5) {-1}

        for (i in newCards.indices) {
            do {
                num = Random.nextInt(52)
            } while (num in newCards)
            newCards[i] = num
        }
        newCards.sort()
        _cards.value = newCards
        _handRank.value = getPedigree(newCards) // 족보 계산
    }
    private fun getPedigree(deck: IntArray): String {
        var cardMatrix = Array(4) { IntArray(13) { 0 } }

        for (cardNum in deck) {
            val cPattern = cardNum / 13
            val cNum = cardNum % 13
            cardMatrix[cPattern][cNum]++
        }

        // 로얄 스트레이트 플러쉬
        for (i in 0..3) {
            if (cardMatrix[i][8] == 1 && cardMatrix[i][9] == 1 && cardMatrix[i][10] == 1 &&
                cardMatrix[i][11] == 1 && cardMatrix[i][12] == 1
            ) {
                return "Royal Straight Flush"
            }
        }
        // 스트레이트 플러쉬
        for (i in 0..3) {
            for (j in 0..8) {
                if (cardMatrix[i][j] == 1 && cardMatrix[i][j + 1] == 1 && cardMatrix[i][j + 2] == 1 &&
                    cardMatrix[i][j + 3] == 1 && cardMatrix[i][j + 4] == 1
                ) {
                    return "Straight Flush"
                }
            }
        }
        // 포카드
        for (j in 0..12) {
            var count = 0
            for (i in 0..3) {
                if (cardMatrix[i][j] == 1) count++
            }
            if (count >= 4) {
                return "Four of a Kind"
            }
        }
        // 풀 하우스
        var onePair = false
        var trips = false
        for (j in 0..12) {
            var count = 0
            for (i in 0..3) {
                if (cardMatrix[i][j] == 1) count++
            }
            if (count == 2) onePair = true
            if (count == 3) trips = true
            if (onePair && trips) return "Full House"
        }
        // 플러쉬 (같은 무늬 5장)
        for (i in 0..3) {
            var count = 0
            for (j in 0..12) {
                if (cardMatrix[i][j] == 1) count++
            }
            if (count >= 5) {
                return "Flush"
            }
        }
        //  스트레이트
        var uniqueNumbers = IntArray(5)
        for (i in uniqueNumbers.indices) {
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
        if (trips) return "Three of a Kind"
        // 투 페어
        onePair = false
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
        if (onePair)
            return "One Pair"
        // 노페어 High Card
        return "No Pairs"
    }
}