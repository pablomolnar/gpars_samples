

/**
 * User: Pablo Molnar
 * Date: 11/9/11
 */
class Utils {
    static def time(Closure closure) {
        def start = System.currentTimeMillis()
        closure.call()
        println "Took: " + (System.currentTimeMillis() - start) + " ms"
    }
}
