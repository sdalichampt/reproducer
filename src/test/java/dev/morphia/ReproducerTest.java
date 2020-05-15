package dev.morphia;

import com.mongodb.Block;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;


public class ReproducerTest extends TestBase {
    @Test
    public void reproduce() {

        // insert a test entity
        MyEntity myEntity = new MyEntity();
        myEntity.setNumber(10);
        getDs().save(myEntity);

        getDatabase().getCollection("MyEntity").find().forEach((Block<? super Document>) document -> {
            document.getInteger("number");

            try {
                document.getLong("number");
                Assert.fail("number is not au long");
            } catch (Exception e) {
                //  must fail
            }
        });

        // inc is OK and don't change field type
        getDs().findAndModify(
                getDs().createQuery(MyEntity.class).field("_id").equal(myEntity.getId()),
                getDs().createUpdateOperations(MyEntity.class).inc("number", 2)
        );

        getDatabase().getCollection("MyEntity").find().forEach((Block<? super Document>) document -> {
            // after increment, number is a int32
            document.getInteger("number" );

            try {
                document.getLong("number");

                Assert.fail("number is not au long");
            } catch (Exception e) {
                //  must fail
            }
        });

        // dec is not OK and change field type from int32 to int64
        getDs().findAndModify(
                getDs().createQuery(MyEntity.class).field("_id").equal(myEntity.getId()),
                getDs().createUpdateOperations(MyEntity.class).dec("number", 2)
        );

        getDatabase().getCollection("MyEntity").find().forEach((Block<? super Document>) document -> {
            // after decrement, number is now a long
            try {
                document.getInteger("number");

                Assert.fail("number is not au int");
            } catch (Exception e) {
                //  must fail
            }
            document.getLong("number" );
        });

    }

    @Test
    public void reproduce2() {

        // insert a test entity
        MyEntity myEntity = new MyEntity();
        myEntity.setNumber(10);
        getDs().save(myEntity);

        getDatabase().getCollection("MyEntity").find().forEach((Block<? super Document>) document -> {
            document.getInteger("number");

            try {
                document.getLong("number");
                Assert.fail("number is not au long");
            } catch (Exception e) {
                //  must fail
            }
        });

        // inc is OK and don't change field type
        getDs().findAndModify(
                getDs().createQuery(MyEntity.class).field("_id").equal(myEntity.getId()),
                getDs().createUpdateOperations(MyEntity.class).inc("number", 2)
        );

        getDatabase().getCollection("MyEntity").find().forEach((Block<? super Document>) document -> {
            // after increment, number is a int32
            document.getInteger("number" );
        });

        // dec is not OK and change field type from int32 to int64
        getDs().findAndModify(
                getDs().createQuery(MyEntity.class).field("_id").equal(myEntity.getId()),
                getDs().createUpdateOperations(MyEntity.class).dec("number", 2)
        );

        getDatabase().getCollection("MyEntity").find().forEach((Block<? super Document>) document -> {
            // after decrement, number is now a long
            document.getInteger("number");
        });

    }
}
