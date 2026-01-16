package ru.netology.nmedia.util
object NumberUtils {

    fun formatCount(count: Int): String {
        return when {
            count < 1_000 -> count.toString()
            count < 10_000 -> {
                val thousands = count / 1_000
                val remainder = (count % 1_000) / 100
                if (remainder == 0) {
                    "${thousands}K"
                } else {
                    "${thousands}.${remainder}K"
                }
            }
            count < 1_000_000 -> "${count / 1_000}K"
            else -> {
                val millions = count / 1_000_000
                val remainder = (count % 1_000_000) / 100_000
                if (remainder == 0) {
                    "${millions}M"
                } else {
                    "${millions}.${remainder}M"
                }
            }
        }
    }
}