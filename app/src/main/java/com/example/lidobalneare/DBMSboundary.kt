package com.example.lidobalneare


import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
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
                                        //list.add(ViewModelHomePage(imageDrawable, title, desc))

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
        val query = "SELECT prezzo FROM webmobile.servizi where nome = \"$nome\""

    Log.i("msg", nome)

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){

                    //deve restituire solo un oggetto o nessuno
                    val result = response.body()!!.getAsJsonArray("queryset")

                    Log.i("msg", result.toString())

                    val money = MyMoney(result.get(0).asJsonObject.get("prezzo").asBigDecimal)
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

    fun getCredenziali(context: Context, callback: QueryReturnCallback<Utente>,email: String, password: String){
        val query = "SELECT * FROM webmobile.utente where email = \"$email\""

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){

                    //deve restituire solo un oggetto o nessuno
                    val result = response.body()!!.getAsJsonArray("queryset")

                    Log.i("msg", result.toString())

                    if(result.size() > 0 && checkPassword(result.get(0).asJsonObject.get("pass").asString)){
                        Utente.getInstance().setId(result.get(0).asJsonObject.get("id").asInt)
                        Utente.getInstance().setNome(result.get(0).asJsonObject.get("nome").asString)
                        Utente.getInstance().setCognome(result.get(0).asJsonObject.get("cognome").asString)
                        Utente.getInstance().setTelefono(result.get(0).asJsonObject.get("telefono").asString)
                        Utente.getInstance().setEmail(result.get(0).asJsonObject.get("email").asString)
                    }

                    callback.onReturnValue(Utente.getInstance(), context.getString(R.string.query_successful))

                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            /**
             * @return ritorna true se la password corrisponde altrimenti false
             */
            private fun checkPassword(pass: String?): Boolean {
                return pass != null && pass == password
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }

        })
        
    }

    fun getAltreInfoUtente(context: Context, callback: QueryReturnCallback<Int>,id: String){
        val queryCartaPrepagata = "SELECT * FROM webmobile.carta_prepagata where utente_id = \"$id\""

        //query per cartaprepagata
        ClientNetwork.retrofit.select(queryCartaPrepagata).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")

                    Log.i("msg", result.toString())

                    if(result.size() > 0){
                        try{
                            //setto la carta
                            Utente.getInstance().setCarta(
                                result.get(0).asJsonObject.get("numeroCarta").asString,
                                result.get(0).asJsonObject.get("cvv").asString,
                                LocalDate.parse(result.get(0).asJsonObject.get("data_scadenza").asString))
                        }catch (e: IllegalArgumentException){
                            Utente.getInstance().generateCarta()
                        }

                    }
                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }

        })

        val queryCC = "SELECT * FROM webmobile.cc where utente_id = \"$id\""

        //query per cc
        ClientNetwork.retrofit.select(queryCC).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")

                    Log.i("msg", result.toString())

                    if(result.size() > 0) {
                        //setto cc
                        Utente.getInstance().setCc(
                            result.get(0).asJsonObject.get("numeroConto").asString,
                            result.get(0).asJsonObject.get("iban").asString,
                            result.get(0).asJsonObject.get("nomeTitolare").asString
                        )
                    }
                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }

        })

        val queryPaypal = "SELECT * FROM webmobile.paypal where utente_id = \"$id\""

        //query per cc
        ClientNetwork.retrofit.select(queryPaypal).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")

                    Log.i("msg", result.toString())
                    if(result.size() > 0) {
                        //setto paypal
                        Utente.getInstance().setPaypal(
                            result.get(0).asJsonObject.get("numeroConto").asString,
                            result.get(0).asJsonObject.get("email").asString,
                            result.get(0).asJsonObject.get("nomeTitolare").asString,
                            result.get(0).asJsonObject.get("telefono").asString
                        )
                    }
                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }

        })

    }

    /**
     * @param list deve contenere in ordine 5 elementi: nome, cognome, telefono, email, pass
      */
    fun insertUtente(context: Context, callback: QueryReturnCallback<Int>, list: List<String>){
        if(list.size > 4){
            val query = "insert into utente(nome, cognome, telefono, email, pass) values ('${list[0]}', '${list[1]}', '${list[2]}', '${list[3]}', '${list[4]}')"

            ClientNetwork.retrofit.insert(query).enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        callback.onReturnValue(200, context.getString(R.string.query_successful))
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

    /**
     * @param list contiene 7 elementi in questo ordine: nome, cognome, tel, mail, Oldpass, newPass
     */
    fun changeDatiUtente(context: Context, callback: QueryReturnCallback<Int>, list: List<String>, id: Int){
        var changment = false
        val query = StringBuilder("UPDATE utente SET")
        for((i, e) in list.withIndex()){
            if(e.isNotEmpty()){
                when(i){
                    0 -> {
                        query.append(" nome = '${list[0]}'")
                        Utente.getInstance().setNome(list[0])
                        changment = true
                    }
                    1 -> {
                        query.append(", cognome = '${list[1]}'")
                        Utente.getInstance().setCognome(list[1])
                        changment = true
                    }
                    2 -> {
                        if(isPhoneNumberValid(list[2])){
                            query.append(", telefono = '${list[2]}'")
                            Utente.getInstance().setTelefono(list[2])
                            changment = true
                        }else{
                            Toast.makeText(context, "numero di telefono formato errato", Toast.LENGTH_SHORT).show()
                        }
                    }
                    3 -> {
                        //query per cambiare la mail
                        //per cambiare la mail devo verificare che non ne esista gia una
                        val queryMail = "select id from webmobile.utente where email = '${list[3]}'"

                        ClientNetwork.retrofit.select(queryMail).enqueue(object : Callback<JsonObject>{
                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                if(response.isSuccessful){
                                   val result = response.body()!!.getAsJsonArray("queryset")
                                    if(result.size() == 0 && isEmailValid(list[3])){
                                        //se entro non esistono email uguali a quella nuova inserita quindi aggiorno
                                        ClientNetwork.retrofit.update("UPDATE utente SET email = '${list[3]}' where id = $id").
                                        enqueue(object : Callback<JsonObject>{
                                            override fun onResponse(
                                                call: Call<JsonObject>,
                                                response: Response<JsonObject>
                                            ) {
                                                if(!response.isSuccessful){
                                                    Toast.makeText(context, "errore inserimento mail", Toast.LENGTH_SHORT).show()
                                                }else{
                                                    Utente.getInstance().setEmail(list[3])
                                                    callback.onReturnValue(200, context.getString(R.string.query_successful))
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<JsonObject>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(context, "failed inserimento mail", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }

                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                            }
                        })
                    }
                    4 -> {
                        //query per aggiornare la password
                        val queryPass = "select pass from webmobile.utente where id = $id"
                        ClientNetwork.retrofit.select(queryPass).enqueue(object : Callback<JsonObject>{
                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                if(response.isSuccessful){
                                    val result = response.body()!!.getAsJsonArray("queryset")[0].asJsonObject.get("pass").asString
                                    if(result == list[4] && isPasswordStrong(list[5])){
                                        if(result == list[5]){
                                            Toast.makeText(context, "inserire password diversa da quella vecchia", Toast.LENGTH_SHORT).show()
                                        }else{
                                            //pass vecchia corretta e quella ha il formato giusto
                                            ClientNetwork.retrofit.update("UPDATE utente SET pass = '${list[5]}' where id = $id").
                                            enqueue(object : Callback<JsonObject>{
                                                override fun onResponse(
                                                    call: Call<JsonObject>,
                                                    response: Response<JsonObject>
                                                ) {
                                                    if(!response.isSuccessful){
                                                        Toast.makeText(context, "errore inserimento pass", Toast.LENGTH_SHORT).show()
                                                    }else{
                                                        callback.onReturnValue(200, context.getString(R.string.query_successful))
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<JsonObject>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(context, "failed inserimento pass", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }

                                    }
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }
        }
        query.append(" WHERE id = $id")
        Log.i("msg", query.toString())

        if(changment){
            ClientNetwork.retrofit.update(query.toString()).enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(!response.isSuccessful){
                        Toast.makeText(context, "errore aggiornamento dati", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "failed aggiornamento dati", Toast.LENGTH_SHORT).show()
                }
            })
            callback.onReturnValue(200, context.getString(R.string.query_successful))
        }else{
            Toast.makeText(context, "inserisci almeno un dato", Toast.LENGTH_SHORT).show()
        }
    }

     fun isPhoneNumberValid(input: String): Boolean {
        // Rimuove tutti i caratteri non numerici dalla stringa
        val digitsOnly = input.replace("[^\\d]".toRegex(), "")
        return digitsOnly.length == 10
    }

     fun isEmailValid(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(pattern))
    }

     fun isPasswordStrong(password: String): Boolean {
        // Esempio di controllo per una password "forte":
        // Almeno 8 caratteri, almeno una lettera maiuscola, almeno un numero
        val pattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$"
        return password.matches(Regex(pattern))
    }

    fun getRecensioni(context: Context, callback: QueryReturnCallback<List<ModelRecensioni>>, id: Int?){
        val query =  if(id != null){
            "select * from webmobile.recensioni where utente_id = $id"
        }else{
            "select * from webmobile.recensioni"
        }

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.i("msg", response.toString())
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")
                    val listResult = ArrayList<ModelRecensioni>()

                    for(i in result){
                        listResult.add(
                            ModelRecensioni(
                                i.asJsonObject.get("titolo").asString,
                                i.asJsonObject.get("descrizione").asString,
                                i.asJsonObject.get("nomeP").asString,
                                i.asJsonObject.get("valutazione").asFloat
                            )
                        )
                    }
                    callback.onReturnValue(listResult,context.getString(R.string.query_successful) )


                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }
        })



    }

    fun insertPrenotazione(context: Context, pagInApp: Boolean, dataPrenotazioneInizio: LocalDate,dataPrenotazioneFine: LocalDate, nomeServizio: String, id: Int, numpersone: Int){
        val queryInsert = "insert into prenotazioni(dataPrenotazioneInizio,dataPrenotazioneFine, numPersone, nomeServizio, utente_id, pagInApp) " +
                "values ('${dataPrenotazioneInizio}','${dataPrenotazioneFine}', '$numpersone', '$nomeServizio', $id, $pagInApp)"

        ClientNetwork.retrofit.insert(queryInsert).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Toast.makeText(context, context.getString(R.string.query_successful), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })


    }





}
