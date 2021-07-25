package com.eroadtest.eroadtest.logic

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eroadtest.eroadtest.util.FileHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith

/**
 * mock data are ready in test/res
 */
@RunWith(AndroidJUnit4::class)
class SearchDataHelperTest {

    val context = ApplicationProvider.getApplicationContext<Context>()
    val fileHelper = FileHelper(context)

    /**
     * Sensor_2021-07-24-05-40-04-553.sns - start-end - Sensor_2021-07-24-11-47-03-056.sns
     */
    @Test
    fun `given interval in the record data range`(start:Long,end:Long)= runBlocking {
        // WHEN
        // TODO mock filenames->data in some lists

        // THEN
        // TODO filter data according to filenames

        // GIVE
        // TODO assert filtered data the start/the end/the specific index should be same as expected
    }

    /**
     * start - Sensor_2021-07-24-05-40-04-553.sns -end -  Sensor_2021-07-24-11-47-03-056.sns
     */
    @Test
    fun `given interval earlier than the record data range`(start:Long,end:Long)= runBlocking {
        // WHEN
        // TODO mock filenames->data in some lists

        // THEN
        // TODO filter data according to filenames

        // GIVE
        // TODO assert filtered data the start/the end/the specific index should be same as expected
    }

    /**
     * Sensor_2021-07-24-05-40-04-553.sns - start - Sensor_2021-07-24-11-47-03-056.sns - end
     */
    @Test
    fun `given interval later the record data range`(start:Long,end:Long)= runBlocking {
        // WHEN
        // TODO mock filenames->data in some lists

        // THEN
        // TODO filter data according to filenames

        // GIVE
        // TODO assert filtered data the start/the end/the specific index should be same as expected
    }

    /**
     * start - Sensor_2021-07-24-05-40-04-553.sns - Sensor_2021-07-24-11-47-03-056.sns - end
     */
    @Test
    fun `given interval cover the record data range`(start:Long,end:Long)= runBlocking {
        // WHEN
        // TODO mock filenames->data in some lists

        // THEN
        // TODO filter data according to filenames

        // GIVE
        // TODO assert filtered data the start/the end/the specific index should be same as expected
    }

    /**
     * start- end - Sensor_2021-07-24-05-40-04-553.sns - Sensor_2021-07-24-11-47-03-056.sns
     * Sensor_2021-07-24-05-40-04-553.sns - Sensor_2021-07-24-11-47-03-056.sns - start- end
     */
    @Test
    fun `given interval out of the record data range`(start:Long,end:Long)= runBlocking {
        // WHEN
        // TODO mock filenames->data in some lists

        // THEN
        // TODO filter data according to filenames

        // GIVE
        // TODO assert filtered data the start/the end/the specific index should be same as expected
    }
}