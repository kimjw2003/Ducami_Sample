package com.example.ducami_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ducamiandroid.data.food.FoodBase
import com.example.ducamiandroid.service.FoodClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val currentTime = Calendar.getInstance().time
    private  val time = SimpleDateFormat("YYYYMMdd", Locale.KOREA).format(currentTime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getHana()
    }

    private fun getHana(){
        FoodClient.retrofitService.getFood("e40fc13904d84da4a8d398649c324133", "JSON", "1", "100",
        "D10", "7240393", time
        ).enqueue(object : retrofit2.Callback<FoodBase>{
            override fun onResponse(call: Call<FoodBase>, response: Response<FoodBase>) {
                food1.text ="급식이 없어요"
                food2.text = "급식이없어요"
                food3.text = "급식이없어요"

                response.body()?.mealServiceDietInfo?.let {
                    val cnt = it[0].head?.get(0)?.list_total_count ?: return

                    it[1].row?.let{ list ->
                        val regex = Regex("[0~9]+.")
                        if(cnt >= 1){
                            food1.text = regex.replace(list[0].DDISH_NM ?: return, "")
                                .replace("<br/>", "\n")
                        }
                        if(cnt >= 2){
                            food2.text = regex.replace(list[1].DDISH_NM ?: return, "")
                                .replace("<br/>", "\n")
                        }
                        if(cnt >= 3){
                            food3.text = regex.replace(list[2].DDISH_NM ?: return, "")
                                .replace("<br/>", "\n")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FoodBase>, t: Throwable) {
                Log.d("Logd", t.message.toString())
            }
        })
    }
}