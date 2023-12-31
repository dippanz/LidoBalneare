package com.example.lidobalneare


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class DBMSboundary {

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

    fun getCredenziali(context: Context, callback: QueryReturnCallback<Utente>, email: String, password: String){
        val query = "SELECT * FROM webmobile.utente where email = \"$email\""

        Log.i("msg", query)

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){

                    //deve restituire solo un oggetto o nessuno
                    val result = response.body()!!.getAsJsonArray("queryset")

                    Log.i("msg", result.toString())

                    if(result.size() > 0 && verifyPassword(password, result.get(0).asJsonObject.get("pass").asString)){
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



            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }

        })
        
    }

    /**
     * @param pass password senza hash
     * @return ritorna true se la password corrisponde altrimenti false
     */
    private fun verifyPassword(pass: String?, hashedPassword: String): Boolean {
        return pass != null && BCrypt.checkpw(pass, hashedPassword)
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

    private fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt(12) // Genera un salt con un costo di 12 (valore consigliato)
        return BCrypt.hashpw(password, salt)
    }

    /**
     * @param list deve contenere in ordine 5 elementi: nome, cognome, telefono, email, pass
      */
    fun insertUtente(context: Context, callback: QueryReturnCallback<Int>, list: List<String>){
        if(list.size > 4){
            val passDB = hashPassword(list[4])
            val query = "insert into utente(nome, cognome, telefono, email, pass) values ('${list[0]}', '${list[1]}', '${list[2]}', '${list[3]}', '$passDB')"

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
                            Toast.makeText(context,
                                context.getString(R.string.numero_di_telefono_formato_errato), Toast.LENGTH_SHORT).show()
                        }
                    }
                    3 -> {
                        if(!isEmailValid(list[3])){
                            Toast.makeText(context,
                                context.getString(R.string.inserisci_mail_valida), Toast.LENGTH_SHORT).show()
                        }else {
                            //query per cambiare la mail
                            //per cambiare la mail devo verificare che non ne esista gia una
                            val queryMail =
                                "select id from webmobile.utente where email = '${list[3]}'"

                            ClientNetwork.retrofit.select(queryMail)
                                .enqueue(object : Callback<JsonObject> {
                                    override fun onResponse(
                                        call: Call<JsonObject>,
                                        response: Response<JsonObject>
                                    ) {
                                        if (response.isSuccessful) {
                                            val result =
                                                response.body()!!.getAsJsonArray("queryset")
                                            if (result.size() == 0) {
                                                //se entro non esistono email uguali a quella nuova inserita quindi aggiorno
                                                ClientNetwork.retrofit.update("UPDATE utente SET email = '${list[3]}' where id = $id")
                                                    .enqueue(object : Callback<JsonObject> {
                                                        override fun onResponse(
                                                            call: Call<JsonObject>,
                                                            response: Response<JsonObject>
                                                        ) {
                                                            if (!response.isSuccessful) {
                                                                Toast.makeText(
                                                                    context,
                                                                    context.getString(R.string.errore_inserimento_mail),
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Utente.getInstance()
                                                                    .setEmail(list[3])
                                                                callback.onReturnValue(
                                                                    200,
                                                                    context.getString(R.string.query_successful)
                                                                )
                                                            }
                                                        }

                                                        override fun onFailure(
                                                            call: Call<JsonObject>,
                                                            t: Throwable
                                                        ) {
                                                            Toast.makeText(
                                                                context,
                                                                "failed inserimento mail",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    })
                                            }

                                        }
                                    }

                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                                    }
                                })
                        }
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
                                    val oldPass = list[4]
                                    if(!verifyPassword(oldPass, result)){
                                        Toast.makeText(context,
                                            context.getString(R.string.la_vecchia_password_non_corretta), Toast.LENGTH_SHORT).show()
                                    }else if(!isPasswordStrong(list[5])){
                                        Toast.makeText(context,
                                            context.getString(R.string.la_nuova_password_troppo_debole), Toast.LENGTH_SHORT).show()
                                    }else{
                                        val newPass = list[5]
                                        if(verifyPassword(newPass, result)){
                                            Toast.makeText(context,
                                                context.getString(R.string.inserire_una_password_diversa_da_quella_vecchia), Toast.LENGTH_SHORT).show()
                                        }else{
                                            val newPassDB = hashPassword(list[5])
                                            //pass vecchia corretta e quella ha il formato giusto
                                            ClientNetwork.retrofit.update("UPDATE utente SET pass = '$newPassDB' where id = $id").
                                            enqueue(object : Callback<JsonObject>{
                                                override fun onResponse(
                                                    call: Call<JsonObject>,
                                                    response: Response<JsonObject>
                                                ) {
                                                    if(!response.isSuccessful){
                                                        Toast.makeText(context,
                                                            context.getString(R.string.errore_inserimento_pass), Toast.LENGTH_SHORT).show()
                                                    }else{
                                                        callback.onReturnValue(200, context.getString(R.string.query_successful))
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<JsonObject>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(context,
                                                        context.getString(R.string.failed_inserimento_pass), Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                callback.onQueryFailed(context.getString(R.string.query_failed))
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
        }
    }

     fun isPhoneNumberValid(input: String): Boolean {
        // Rimuove tutti i caratteri non numerici dalla stringa
        val digitsOnly = input.replace("\\D".toRegex(), "")
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

    fun getRecensioniUtente(context: Context, callback: QueryReturnCallback<List<ModelRecensioni>>, id: Int){
        getRecensioni(context, callback, "select * from webmobile.recensioni where utente_id = $id")
    }

    fun getRecensioniServizio(context: Context, callback: QueryReturnCallback<List<ModelRecensioni>>, nomeServizio: String){
        getRecensioni(context, callback, "select * from webmobile.recensioni where nomeServizio = '$nomeServizio'")
    }

    private fun getRecensioni(context: Context, callback: QueryReturnCallback<List<ModelRecensioni>>, query: String){
        Log.i("msg", query)

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")
                    val listResult = ArrayList<ModelRecensioni>()

                    for(i in result){
                        listResult.add(
                            ModelRecensioni(
                                i.asJsonObject.get("titolo").asString,
                                i.asJsonObject.get("descrizione").asString,
                                i.asJsonObject.get("nomeP").asString,
                                i.asJsonObject.get("valutazione").asFloat,
                                i.asJsonObject.get("nomeServizio").asString
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

    fun insertRecensione(context: Context, titolo: String, desc: String, nomePublic: String, nomeServizio: String, valutazione: Float, id: Int){
        val queryInsert =  "insert into recensioni(titolo, descrizione, nomeP, nomeServizio, valutazione, utente_id) values "+
                "('$titolo', '$desc', '$nomePublic', '$nomeServizio', $valutazione, $id)"

        ClientNetwork.retrofit.insert(queryInsert).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Toast.makeText(context, context.getString(R.string.query_successful), Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, context.getString(R.string.query_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.query_failed), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getPrenotazioni(context: Context, callback: QueryReturnCallback<List<ModelPrenotazione>>, id: Int){
        val query = "select * from webmobile.prenotazioni where utente_id = $id"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")
                    val listResult = ArrayList<ModelPrenotazione>()

                    for(i in result){
                        val dateFormatStringData = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val dataI = dateFormatStringData.parse(i.asJsonObject.get("dataPrenotazioneInizio").asString)
                        val dataF = dateFormatStringData.parse(i.asJsonObject.get("dataPrenotazioneFine").asString)

                        // Formatta la data in "EEE dd MMM" (sab 12 ago)
                        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.ITALIAN)

                        val data = if(dataI == dataF){
                            dateFormat.format(dataF ?: Calendar.getInstance().time)
                        }else{
                            val dateIString = dateFormat.format(dataI ?: Calendar.getInstance().time)
                            val dateFString = dateFormat.format(dataF ?: Calendar.getInstance().time)
                            "$dateIString - $dateFString"
                        }

                        listResult.add(
                            ModelPrenotazione(
                                i.asJsonObject.get("id").asInt,
                                data,
                                i.asJsonObject.get("numPersone").asInt
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
    
    fun getDatiPagCarta(context: Context, callback: QueryReturnCallback<CartaPrepagata>, id: Int){

        val query = "select * from webmobile.carta_prepagata where utente_id = $id"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!.getAsJsonArray("queryset")

                    if (result.size() > 0){
                        callback.onReturnValue(
                            CartaPrepagata(
                                result[0].asJsonObject.get("numeroCarta").asString,
                                result[0].asJsonObject.get("cvv").asString,
                                LocalDate.parse(result[0].asJsonObject.get("data_scadenza").asString)
                        ), context.getString(R.string.query_successful))
                    }else{
                        callback.onQueryError("nessun dato")
                    }
                } else {
                    callback.onQueryError(context.getString(R.string.query_error))
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }
        })
    }

    fun getDatiPagCC(context: Context, callback: QueryReturnCallback<ContoCorrente>, id: Int){

        val query = "select * from webmobile.cc where utente_id = $id"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!.getAsJsonArray("queryset")

                    if (result.size() > 0){
                        callback.onReturnValue(
                            ContoCorrente(
                                result[0].asJsonObject.get("numeroConto").asString,
                                result[0].asJsonObject.get("iban").asString,
                                result[0].asJsonObject.get("nomeTitolare").asString
                            ), context.getString(R.string.query_successful))
                    }else{
                        callback.onQueryError("nessun dato")
                    }
                } else {
                    callback.onQueryError(context.getString(R.string.query_error))
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }
        })
    }

    fun getDatiPagPaypal(context: Context, callback: QueryReturnCallback<PayPal>, id: Int){

        val query = "select * from webmobile.paypal where utente_id = $id"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()!!.getAsJsonArray("queryset")

                    if (result.size() > 0){
                        callback.onReturnValue(
                            PayPal(
                                result[0].asJsonObject.get("numeroConto").asString,
                                result[0].asJsonObject.get("email").asString,
                                result[0].asJsonObject.get("nomeTitolare").asString,
                                result[0].asJsonObject.get("telefono").asString
                            ), context.getString(R.string.query_successful))
                    }else{
                        callback.onQueryError("nessun dato")
                    }
                } else {
                    callback.onQueryError(context.getString(R.string.query_error))
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryFailed(context.getString(R.string.query_failed))
            }
        })
    }

    fun insertDatiCarta(context: Context, numeroCarta: String, cvv: String, data: LocalDate){

        val insertQuery = "INSERT INTO carta_prepagata (utente_id, numeroCarta, cvv, data_scadenza)\n " +
                "VALUES (${Utente.getInstance().getId()}, '$numeroCarta', '$cvv', '$data')"

        ClientNetwork.retrofit.insert(insertQuery).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Toast.makeText(context, context.getString(R.string.query_successful), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.query_failed), Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun insertPaypal(context: Context, email: String){
        val query = "INSERT INTO paypal (utente_id, numeroConto, email, nomeTitolare, telefono)\n " +
                "VALUES (${Utente.getInstance().getId()}, '${generateRandomAlphanumericString()}', '$email', '${Utente.getInstance().getNome()} ${Utente.getInstance().getCognome()}', '${Utente.getInstance().getTelefono()}')"

        ClientNetwork.retrofit.insert(query).enqueue(object : Callback<JsonObject>{
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

    private fun generateRandomAlphanumericString(length: Int = 10): String {
        val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun insertCC(context: Context, iban: String){
        val query = "INSERT INTO cc (utente_id, numeroConto, iban, nomeTitolare)\n " +
                "VALUES (${Utente.getInstance().getId()}, '${generateRandomAlphanumericString()}', '$iban', '${Utente.getInstance().getNome()} ${Utente.getInstance().getCognome()}');"

        ClientNetwork.retrofit.insert(query).enqueue(object : Callback<JsonObject>{
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

    fun insertNotifica(title: String, desc: String, id: Int) {

        val query = "INSERT INTO notifiche (title, descrizione, utente_id) " +
                "VALUES ('$title', '$desc', $id)"

        ClientNetwork.retrofit.insert(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })

    }

    fun removeNotifica(id: String) {
        val query = "DELETE FROM notifiche WHERE id = $id"

        ClientNetwork.retrofit.remove(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Log.i("msg", "Correttamente eliminato")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })
    }

    fun removePrenotazione(id: String) {
        val query = "DELETE FROM prenotazioni WHERE id = $id"

        ClientNetwork.retrofit.remove(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Log.i("msg", "Correttamente eliminato")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })
    }



    fun getNotifiche(context: Context, callback: QueryReturnCallback<List<ModelNotifica>>, id: Int) {

        val query = "select * from notifiche where utente_id = $id"

        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")
                    val listResult = ArrayList<ModelNotifica>()

                    for(i in result) {
                        listResult.add(
                            ModelNotifica(
                                i.asJsonObject.get("id").asString,
                                i.asJsonObject.get("title").asString,
                                i.asJsonObject.get("descrizione").asString
                            )
                        )
                    }

                    callback.onReturnValue(listResult, context.getString(R.string.query_successful))



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
     * @param listNomeImage deve contenere il nome delle immagine del tipo: nome.png
     */
     fun getImagePromo(context: Context, callback: QueryReturnCallback<Bitmap>, listNomeImage: List<String>){

        for(e in listNomeImage) {

            val imageUrl = "http://10.0.2.2:8000/webmobile/media/images/promo/$e"

            Log.i("msg", imageUrl)

            // Esegui la richiesta per ottenere l'immagine
            ClientNetwork.retrofit.getImage(imageUrl).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val imageBytes = response.body()?.bytes()

                        if (imageBytes != null) {
                            // Converti l'array di byte in un Bitmap
                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            callback.onReturnValue(bitmap, context.getString(R.string.query_successful))

                        } else {
                            Toast.makeText(context, "dati nulli, errore", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                       callback.onQueryError(context.getString(R.string.query_error))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onQueryError(context.getString(R.string.query_failed))
                }
            })
        }


    }


    fun getImageHome(context: Context, callback: QueryReturnCallback<ViewModelHomePage>){


            val query = "select * from webmobile.servizi"

            ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val result = response.body()!!.getAsJsonArray("queryset")

                        for (e in result){
                            val imageUrl = "http://10.0.2.2:8000/webmobile${e.asJsonObject.get("mediaPath").asString}"

                            // Esegui la richiesta per ottenere l'immagine
                            ClientNetwork.retrofit.getImage(imageUrl).enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    if (response.isSuccessful) {
                                        val imageBytes = response.body()?.bytes()

                                        if (imageBytes != null) {
                                            callback.onReturnValue(
                                                ViewModelHomePage(imageBytes,
                                                    e.asJsonObject.get("nome").asString.uppercase(),
                                                    e.asJsonObject.get("descrizione").asString),
                                                context.getString(R.string.query_successful))

                                        } else {
                                            Toast.makeText(context, "dati nulli, errore", Toast.LENGTH_SHORT).show()
                                        }

                                    } else {
                                        callback.onQueryError(context.getString(R.string.query_error))
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    callback.onQueryError(context.getString(R.string.query_failed))
                                }
                            })
                        }
                    }else{
                        callback.onQueryError(context.getString(R.string.query_error))
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onQueryError(context.getString(R.string.query_failed))
                }
            })
    }

    fun checkPrenotazioni(startDate: LocalDate, endDate: LocalDate, nomeServizio: String?, id: Int, context: Context, callback: QueryReturnCallback<Boolean>){
        val query = "select * from webmobile.prenotazioni where dataPrenotazioneInizio = '$startDate' and" +
                " dataPrenotazioneFine = '$endDate' and nomeServizio = '$nomeServizio' and utente_id = $id"



        ClientNetwork.retrofit.select(query).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()!!.getAsJsonArray("queryset")
                    if(result.size() == 0){
                        callback.onReturnValue(false, context.getString(R.string.query_successful))
                    }else{
                        callback.onReturnValue(true, "")
                    }
                }else{
                    callback.onQueryError(context.getString(R.string.query_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onQueryError(context.getString(R.string.query_failed))
            }
        })

    }


}
