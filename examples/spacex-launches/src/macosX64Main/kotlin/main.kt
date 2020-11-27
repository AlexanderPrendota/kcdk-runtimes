import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    getLaunches().map { it.rocket }.forEach(::println)
}