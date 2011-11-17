

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import javax.crypto.KeyGenerator
import org.junit.Test
import static groovyx.gpars.GParsPool.withPool
import static Utils.time

@TestMixin(GrailsUnitTestMixin)
class Example3Tests {

    @Test
    void "Example 3: Parallel encryption"() {

        def text = "http://www.wikipedia.org/wiki/Superman".toURL().text + "http://www.wikipedia.org/wiki/Batman".toURL().text + "http://www.wikipedia.org/wiki/Spiderman".toURL().text
        def words = text.split("\\s+")

        DesEncrypter encrypter = new DesEncrypter(KeyGenerator.getInstance("DES").generateKey())

        time {
            assert words == words.collect { encrypter.encrypt(it) }.collect { encrypter.decrypt(it) }
        }

        time {
            withPool {
                assert words == words.collectParallel { encrypter.encrypt(it) }.collectParallel { encrypter.decrypt(it) }
            }
        }

        time {
            withPool {
                words.asConcurrent { assert words == words.collect { encrypter.encrypt(it) }.collect { encrypter.decrypt(it) } }
            }
        }
    }
}
