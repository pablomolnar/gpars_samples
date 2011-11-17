

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovyx.gpars.dataflow.Dataflows
import static groovyx.gpars.dataflow.Dataflow.task
import org.junit.Test

@TestMixin(GrailsUnitTestMixin)
class Example6Tests {

    @Test
    void "Sequential multiget"() {
        Utils.time {
            def itemsIds = (1..50)
            def itemsService = new ItemsService()

            def itemsMemcached = itemsService.getItemsFromMemcached(itemsIds)
            def itemsDB = itemsService.getItemsFromDB(itemsIds - itemsMemcached)
            itemsService.saveItemsInMemcached(itemsDB)
            def items = itemsService.marhsallItems(itemsMemcached + itemsDB)

            println items
        }
    }

    @Test
    void "Dataflow multiget"() {
        Utils.time {
            def itemsIds = (1..50)
            def itemsService = new ItemsService()

            final Dataflows data = new Dataflows()

            task {
                def itemsMemcached = itemsService.getItemsFromMemcached(itemsIds)
                data.itemsIdDB  = itemsIds - itemsMemcached

                data.itemsMemcached = itemsMemcached
            }

            task {
                data.itemsDB =  itemsService.getItemsFromMemcached(data.itemsIdDB)
            }

            task {
                itemsService.saveItemsInMemcached(data.itemDB)
            }

            task {
                data.items = itemsService.marhsallItems(data.itemsMemcached) + itemsService.marhsallItems(data.itemsDB)
            }

            println data.items
        }
    }


    class ItemsService {

        Collection getItemsFromMemcached(itemsIds) {
            sleep(30 * 2)
            (1..30)
        }

        Collection getItemsFromDB(itemsIds) {
            sleep(20 * 50)
            (30..50)
        }

        Map marhsallItems(items) {
            sleep(items.size() * 10)
        }

        void saveItemsInMemcached(items) {
            sleep(items.size() * 10)

        }
    }

}
