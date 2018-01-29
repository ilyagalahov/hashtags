package com.example.applemacbookproretina.learningandroid1

import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.core.io.FileSystemResource
import org.springframework.http.*
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList


class ExecuteImaggaActiviti : AsyncTask<Bitmap, Void, List<String>>() {

    override fun doInBackground(vararg p0: Bitmap?): List<String> {


        val authHeader = HttpBasicAuthentication("acc_b672a5eb6f2dfa0", "d2d2d794085b6c289a837691ece87a5e")
        val requestHeaders = HttpHeaders()
        requestHeaders.setAuthorization(authHeader)
        requestHeaders.accept = Collections.singletonList(MediaType.APPLICATION_JSON)




        val path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).toString()
        var fOut: FileOutputStream? = null
        val counter = 0
        val file = File(path, "FitnessGirl$counter.png") // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        fOut = FileOutputStream(file)

        val pictureBitmap = p0[0] // obtaining the Bitmap
        pictureBitmap?.compress(Bitmap.CompressFormat.PNG, 85, fOut) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut!!.flush() // Not really required
        fOut!!.close() // do not forget to close the stream

        val resource = FileSystemResource(file)
        var formData: LinkedMultiValueMap<String, Any> = LinkedMultiValueMap();

        formData.add("image", resource)

        val requestEntity = HttpEntity<MultiValueMap<String, Any>>(
                formData, requestHeaders)


        val postUrl = "https://api.imagga.com/v1/content"
        val restTemplate = RestTemplate()
        restTemplate.getMessageConverters().add(FormHttpMessageConverter())
        val postResponse = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity, String::class.java)

        val contentUuid: String =  (JSONObject(postResponse.body).getJSONArray("uploaded")[0] as JSONObject).getString("id")

        val url = "http://api.imagga.com/v1/tagging?content="+contentUuid
        val restTemplateTagging = RestTemplate()
        restTemplateTagging.getMessageConverters().add(StringHttpMessageConverter())

        val requestEntityTagging = HttpEntity<MultiValueMap<String, Any>>(
                requestHeaders)

        val response = restTemplateTagging.exchange(url, HttpMethod.GET, requestEntityTagging, String::class.java)

        var tagsList : ArrayList<String> = ArrayList()
        var tags:JSONArray = JSONObject(response.body).getJSONArray("results").getJSONObject(0).getJSONArray("tags");
        for (i in 0 until tags.length()) {
            val tag: String = tags.getJSONObject(i).getString("tag")
            tagsList.add(tag)
        }
        return tagsList;
    }


}