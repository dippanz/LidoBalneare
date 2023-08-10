package com.example.lidobalneare


import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class DBMSboundary {

    fun getDataHomePage(context: Context, callback: QueryReturnCallback<List<ViewModelHomePage>>) {
        //serve aggiungere una tabella dove memorizzare le immagini dei servizi
        val query = "SELECT * FROM webmobile.home"
        var c = 0

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {

                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset != null && queryset.size() > 0) {
                        val list = mutableListOf<ViewModelHomePage>()

                        // Dichiarazione del contatore latch
                        val latch = CountDownLatch(queryset.size())

                        for (item in queryset) {
                            val itemJson = item.asJsonObject
                            Log.i("msg", "dato: $itemJson")

                            //val imageUrl = "http://10.0.2.2:8000/webmobile/server/serverdj/" + itemJson.get("Immagine").asString
                            val imageUrl = itemJson.get("Immagine").asString


                            Log.i("msg", imageUrl)

                            //richiedo immagine da server
                            ClientNetwork.retrofit.getImage(imageUrl).enqueue(object : Callback<ResponseBody>{
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    Log.i("msg", "prima entro")
                                    val title = itemJson.get("Titolo").asString
                                    val desc = itemJson.get("Descrizione").asString

                                    if(response.isSuccessful){
                                        c++
                                        Log.i("msg", "entro $c volte")

                                        val imageByteArray = response.body()?.bytes()
                                        val imageDrawable = BitmapDrawable.createFromStream(imageByteArray?.inputStream(), null)
                                        list.add(ViewModelHomePage(imageDrawable, title, desc))

                                    }else{
                                        Log.i("msg", response.toString())
                                        callback.onQueryError(context.getString(R.string.query_format_error_image))
                                    }

                                    // Contatore decrementato
                                    latch.countDown()
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    callback.onQueryError(context.getString(R.string.query_error_image))
                                    Log.e("msg", "Errore durante la chiamata API: ${t.message}")
                                    t.printStackTrace()

                                    // Contatore decrementato
                                    latch.countDown()
                                }

                            })
                        }


                        // Attendi il completamento di tutte le chiamate
                        Log.i("msg", "${latch.count}")
                        latch.await()

                        // Verifica se hai completato il caricamento di tutti i dati
                        if (list.size == queryset.size()) {
                            callback.onReturnValue(list, context.getString(R.string.query_successful))
                        }else{
                            callback.onQueryError(context.getString(R.string.query_format_errorDatiMancanti))
                        }

                    } else {
                        callback.onQueryFailed(context.getString(R.string.query_failed))
                    }
                } else {
                    callback.onQueryError(context.getString(R.string.query_format_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryError(context.getString(R.string.query_error))
                Log.e("msg", "Errore durante la chiamata API: ${t.message}")
                t.printStackTrace()
            }
        })






    }
/**
 * query per prelevare prezzo di un servizio
 * @param nome identifica il nome del servizio che fa da chiave primaria per ottnere il prezzo
 * */
    fun getPrezzoServizio(context: Context, callback: QueryReturnCallback<MyMoney>,nome: String){
        //query
        val query = "SELECT prezzo FROM webmobile.prezzoServizi where nome = $nome"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    //deve restituire solo un oggetto o nessuno
                    val result = response.body()!!.getAsJsonObject("queryset")
                    Log.i("msg", result.toString())
                    val money = MyMoney(result.get("prezzo").asBigDecimal)
                    callback.onReturnValue(money, context.getString(R.string.query_successful))
                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }

        })




    }



}
