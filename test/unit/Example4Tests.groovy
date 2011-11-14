

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Test
import static groovyx.gpars.GParsPool.withPool

@TestMixin(GrailsUnitTestMixin)
class Example4Tests {

    @Test
    void "Sequential iteration"() {

        Utils.time {
            def list = []
            (0..9).each {list[it]=0}

            (0..200000).each {
                if((it as BigInteger).isProbablePrime(100)) {
                    String stringNumber = it as String
                    def lastDigit = stringNumber[stringNumber.size() - 1] as Integer
                    list[lastDigit]++
                }
            }

            println list
        }
    }

    @Test
    void "Wrong parallel iteration"() {
        Utils.time {
            def list = []
            (0..9).each {list[it]=0}

            withPool{
                (0..200000).eachParallel {
                    if((it as BigInteger).isProbablePrime(100)) {
                        String stringNumber = it as String
                        def lastDigit = stringNumber[stringNumber.size() - 1] as Integer
                        list[lastDigit]++
                    }
                }
            }

            println list
        }
    }

    @Test
    void "Correct parallel iteration"() {
        Utils.time {

            def list = []
            (0..9).each {list[it]=new AtomicInteger(0)}

            withPool{
                (0..200000).eachParallel {
                    if((it as BigInteger).isProbablePrime(100)) {
                        String stringNumber = it as String
                        def lastDigit = stringNumber[stringNumber.size() - 1] as Integer
                        list[lastDigit].incrementAndGet()
                    }
                }
            }

            println list
        }
    }
}
