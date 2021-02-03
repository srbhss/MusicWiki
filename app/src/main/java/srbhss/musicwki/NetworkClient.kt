package srbhss.musicwki

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject


class NetworkClient {

    private val baseURL = "http://ws.audioscrobbler.com/2.0/"
    private val apiKey = "60c364101afac2a3d790a39bf5c18b89"

    val client: AsyncHttpClient = AsyncHttpClient()
    val params: RequestParams = RequestParams()

    fun getAbsoluteUrl(relativeUrl: String): String {
        return baseURL + relativeUrl
    }

    fun getApiKey(): String {
        return apiKey
    }

}