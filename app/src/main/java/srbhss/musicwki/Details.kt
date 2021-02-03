package srbhss.musicwki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class Details : AppCompatActivity() {
    lateinit var nameTextView: TextView
    lateinit var detailTextView: TextView

    private val networkClient: NetworkClient = NetworkClient()
    private val params: RequestParams = networkClient.params
    private val client: AsyncHttpClient = networkClient.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val name = (intent.extras as android.os.Bundle).getString("key_name").toString()
        val type = (intent.extras as Bundle).getString("key_type").toString()
        nameTextView = findViewById(R.id.name)
        nameTextView.text = name

        detailTextView = findViewById(R.id.detail)

        val url = "?method=$type.getinfo&$type=$name&api_key=${networkClient.getApiKey()}&format=json"

        client.get(networkClient.getAbsoluteUrl(url), params, object : JsonHttpResponseHandler() {
            override fun onSuccess(code: Int, headers: Array<Header?>?, body: JSONObject) {
                try {
                    val detailObject = body.getJSONObject(type)
                    detailTextView.text = if (type == "artist") {
                        detailObject.getJSONObject("bio").getJSONObject("content").toString()
                    } else {
                        detailObject.getJSONObject("wiki").getJSONObject("content").toString()
                    }
                    nameTextView.setBackgroundResource(
                        if (type == "track") {
                            detailObject.getJSONObject("album").getJSONArray("image")[0] as Int
                        } else {
                            detailObject.getJSONArray("image")[0] as Int
                        }
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, res: String, t: Throwable) {
            }
        })
    }
}