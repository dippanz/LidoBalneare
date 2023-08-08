package com.example.lidobalneare
/*

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DBMSboundary {

    fun getOspiti(context: Context, callback: QueryReturnCallback<List<Utente>>) {
        val query = "SELECT * FROM webmobile.Clienti WHERE check-in=true and check-out=false;"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val queryset = response.body()?.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val list = mutableListOf<Utente>()

                        for (item in queryset) {
                            val utenteJson = item.asJsonObject
                            val utente = Gson().fromJson(utenteJson, Utente::class.java)
                            list.add(utente)
                        }

                        callback.onReturnValue(list, context.getString(R.string.query_successful))
                    } else {
                        callback.onQueryFailed(context.getString(R.string.query_failed))
                    }
                } else {
                    callback.onQueryError(context.getString(R.string.query_format_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryError(context.getString(R.string.query_error))
            }
        })
    }

    fun getPrenotazioni(context: Context, callback: QueryReturnCallback<List<Prenotazioni>>) {
        val query = "SELECT * FROM webmobile.Prenotazioni WHERE check-in=false and check-out=false;"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val queryset = response.body()?.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val list = mutableListOf<Prenotazioni>()

                        for (item in queryset) {
                            val prenotazioniJson = item.asJsonObject
                            val prenotazione = Gson().fromJson(prenotazioniJson, Prenotazioni::class.java)
                            list.add(prenotazione)
                        }

                        callback.onReturnValue(list, context.getString(R.string.query_successful))
                    } else {
                        callback.onQueryFailed(context.getString(R.string.query_failed))
                    }
                } else {
                    callback.onQueryError(context.getString(R.string.query_format_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryError(context.getString(R.string.query_error))
            }
        })
    }

    fun getDataHomePage(context: Context, callback: QueryReturnCallback<List<ViewModelHomePage>>) {
        //serve aggiungere una tabella dove memorizzare le immagini dei servizi
        val query = "SELECT titoloServ, descServ, image FROM webmobile.Servizi"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val queryset = response.body()?.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val list = mutableListOf<ViewModelHomePage>()

                        for (item in queryset) {
                            val itemJson = item.asJsonObject

                            // todo Ottieni l'URL completo dell'immagine concatenando l'URL di base e il percorso restituito
                            val imageUrl = "https://www.mettere indirizzo.com" + itemJson.get("image").asString

                            // Carica l'immagine utilizzando Glide
                            Glide.with(context)
                                .load(imageUrl)
                                .into(object : CustomTarget<Drawable>() {
                                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                        // Carico l'immagine il titolo e la desc creando oggetto ViewModelHomePage
                                        list.add(ViewModelHomePage(resource,itemJson.get("titoloServ").asString,itemJson.get("descServ").asString ))


                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // Se necessario, gestisci la cancellazione del caricamento dell'immagine
                                    }
                                })
                        }

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
            }
        })
    }

    fun getDataCamere(context: Context, callback: QueryReturnCallback<List<ViewModelHomePage>>) {
        //serve aggiungere una tabella dove memorizzare le immagini dei servizi
        //todo le varie tipologie di camere in caso aggiustare query se logica non va bene
        val query = "SELECT titoloServ, descServ, image FROM webmobile.Camere"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val queryset = response.body()?.getAsJsonArray("queryset")
                    if (queryset != null && queryset.size() > 0) {
                        val list = mutableListOf<ViewModelHomePage>()

                        for (item in queryset) {
                            val itemJson = item.asJsonObject

                            // todo Ottieni l'URL completo dell'immagine concatenando l'URL di base e il percorso restituito
                            val imageUrl = "https://www.mettere indirizzo.com" + itemJson.get("image").asString

                            // Carica l'immagine utilizzando Glide
                            Glide.with(context)
                                .load(imageUrl)
                                .into(object : CustomTarget<Drawable>() {
                                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                        // Carico l'immagine il titolo e la desc creando oggetto ViewModelHomePage
                                        list.add(ViewModelHomePage(resource,itemJson.get("titoloServ").asString,itemJson.get("descServ").asString ))


                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // Se necessario, gestisci la cancellazione del caricamento dell'immagine
                                    }
                                })
                        }

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
            }
        })
    }
}
*/