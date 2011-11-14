


import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.Test
import static groovyx.gpars.GParsPool.withPool

@TestMixin(GrailsUnitTestMixin)
class Example2Tests {

    static final basicParams = [access_token: 'AAAAAAITEghMBAPfQSloYaUWFcCtZAKzvXuz8mZAGAFJ7xMYdWRkTpYRImWheYnjnn13dtdRgZC54zimInbs127UiZCY1R3mzYIHvr3TsfQZDZD', limit: 5000]

    @Test
    void "Example 2 - Map Reduce : search single users by gender from facebook"() {
        withPool {
            Utils.time {

                def friends = grails.converters.JSON.parse(new URL("https://graph.facebook.com/me/friends?" + basicParams.collect {k, v -> "$k=$v"}.join("&")).text)

                def results =

                    friends.data
                    .parallel
                    .map {
                        def friend = grails.converters.JSON.parse(new URL("https://graph.facebook.com/$it.id?" + basicParams.collect {k, v -> "$k=$v"}.join("&")).text)
                        friend
                    }.filter {
                        it.relationship_status == "Single" || it.relationship_status == null
                    }.groupBy {
                        it.gender
                    }.getParallel()
                    .map {
                        it.value = it.value.size()
                        it
                    }.collection

                println results
            }
        }
    }
}
