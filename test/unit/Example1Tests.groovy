

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.Test
import static groovyx.gpars.GParsPool.withPool

@TestMixin(GrailsUnitTestMixin)
class Example1Tests {

    static final basicParams = [access_token: 'AAAAAAITEghMBAPfQSloYaUWFcCtZAKzvXuz8mZAGAFJ7xMYdWRkTpYRImWheYnjnn13dtdRgZC54zimInbs127UiZCY1R3mzYIHvr3TsfQZDZD', limit: 5000]

    @Test
    void "Example 1 - Fork Join: search users from facebook"() {
        withPool {
            Utils.time {

                List list = ['Chuck Norris', 'Jack Bauer', 'Bruce Lee', 'Harry Potter'].collectParallel {

                    def params = basicParams.clone()
                    params.q = it.replace(' ', '%20')
                    params.type = 'user'

                    def url = "https://graph.facebook.com/search?" + params.collect {k, v -> "$k=$v"}.join("&")
                    def json = grails.converters.JSON.parse(new URL(url).text)

                    [name: it, size: json.data.size()]
                }

                println list

                def max
                list.each {
                    if (it.size > max?.size) max = it

                }

                println "Max: " + max
            }
        }
    }
}
