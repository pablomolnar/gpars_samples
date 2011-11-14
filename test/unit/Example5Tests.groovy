

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Test
import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.GParsPool.executeAsync

@TestMixin(GrailsUnitTestMixin)
class Example5Tests {

    @Test
    void "Parallel API Calls"() {


        Utils.time {
            def item
            new ItemValidator().validate(item)
        }
    }


    class ItemValidator {
        def data = [:]


        def validate(item) {

            def apis = [
                {data.sites = callAPI("/sites", 10)},
                {data.categories = callAPI("/categories", 40)},
                {data.currencies = callAPI("/currencies", 10) },
                {data.listing_types = callAPI("/listing_types", 150)},
                {data.user = callAPI("/user", 10)            },
                {data.categoriesAttributes = callAPI("/categoriesAttributes", 350)},
                {data.address = callAPI("/address", 20)                     },
                {data.catalog_products = callAPI("/catalog_products", 25)},
                {data.shipping_profile = callAPI("/shipping_profile", 25)},
                {data.locations = callAPI("/locations", 50)        }
            ]

            withPool(10) {
                executeAsync(apis)
            }

            // Execute validations
        }

        def callAPI(String s, int i) {
            println System.currentTimeMillis()
            sleep(i)
        }
    }
}
