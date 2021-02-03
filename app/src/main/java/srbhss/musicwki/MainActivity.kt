package srbhss.musicwki

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var chipGroup: ChipGroup

    private val networkClient: NetworkClient = NetworkClient()
    private val params: RequestParams = networkClient.params
    private val client: AsyncHttpClient = networkClient.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chipGroup = findViewById(R.id.genreGroup)

        val url = "?method=tag.getTopTags&api_key=${networkClient.getApiKey()}&format=json"

        client.get(networkClient.getAbsoluteUrl(url), params, object : JsonHttpResponseHandler() {
            override fun onSuccess(code: Int, headers: Array<Header?>?, body: JSONObject) {
                try {
                    val tagArray = body.getJSONArray("toptags")
                    for (i in 0 until chipGroup.childCount) {
                        (chipGroup.getChildAt(i) as Chip).text = (tagArray[i] as JSONObject).getJSONObject("tag").getJSONObject("name").toString()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, res: String, t: Throwable) {
            }
        })
    }

    fun goToGenreDetails(view: View) {
        val intent = Intent(this, GenreDetails::class.java)
        intent.putExtra("key", (view as Chip).text)
        startActivity(intent)
    }
}