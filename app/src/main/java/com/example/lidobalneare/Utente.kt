package com.example.lidobalneare

import android.graphics.Bitmap
import android.util.Log
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class Utente private constructor(){

    companion object {
        private var instance: Utente? = null

        fun getInstance(): Utente {
            if (instance == null) {
                instance = Utente()
            }
            return instance as Utente
        }
    }

    private var id: Int = -1
    private var nome: String = ""
    private var cognome: String = ""
    private var email: String = ""
    private var telefono: String = ""

    private var carta: CartaPrepagata? = null
    private var cc: ContoCorrente? = null
    private var paypal: PayPal? = null

    private var isLoggedIn: Boolean = false
    private val imageHome: MutableList<Bitmap> = mutableListOf()
    private val imageHomeCenter: MutableList<ViewModelHomePage> = mutableListOf()

    fun haveImagePromo(): Boolean{
        return imageHome.isNotEmpty()
    }

    fun getImagePromo(): MutableList<Bitmap>?{
       return if(haveImagePromo()){
           imageHome
       }else {
           null
       }
    }

    fun addImagePromo(images: Bitmap){
        imageHome.add(images)
    }

    fun setLoggedIn() {
        isLoggedIn = true
    }

    fun setId(value: Int){
        id = value
    }

    fun getId(): Int{
        return id
    }

    // Getter e setter per il nome
    fun getNome(): String {
        return nome
    }

    fun setNome(nuovoNome: String) {
        nome = nuovoNome
    }

    // Getter e setter per il cognome
    fun getCognome(): String {
        return cognome
    }

    fun setCognome(nuovoCognome: String) {
        cognome = nuovoCognome
    }

    // Getter e setter per l'email
    fun getEmail(): String {
        return email
    }

    fun setEmail(nuovaEmail: String) {
        email = nuovaEmail
    }

    // Getter e setter per il telefono
    fun getTelefono(): String {
        return telefono
    }

    fun setTelefono(nuovoTelefono: String) {
        telefono = nuovoTelefono
    }

    fun clearUtente(){
        id = -1
        nome = ""
        cognome = ""
        telefono = ""
        email = ""
        isLoggedIn = false
        carta = null
        cc = null
        paypal = null
    }

    fun isLoggedIn(): Boolean {
        return isLoggedIn
    }

    fun getCarta(): CartaPrepagata?{
        return carta
    }

    fun getCc(): ContoCorrente?{
        return cc
    }

    fun getPaypal(): PayPal?{
        return paypal
    }

    fun setCc(numeroConto: String, iban: String, nomeTitolare: String){
        cc = ContoCorrente(numeroConto, iban, nomeTitolare)
    }

    fun setPaypal(numeroConto: String, email: String, nomeTitolare: String, numTelefono: String){
        paypal = PayPal(numeroConto, email, nomeTitolare, numTelefono)
    }

    /**
     * setta una carta per l'utente
     *
     * @throws IllegalArgumentException se il numeroCarta non è di 10 cifre, il cvv di 3 cifre
     * e dataScadenza sia un giorno superiore a oggi
     *
     */
    fun setCarta(numeroCarta: String, cvv: String,dataScadenza: LocalDate){
        val numeroCartaRegex = Regex("\\d{10}")
        val cvvRegex = Regex("\\d{3}")

        // Se le condizioni sono soddisfatte, crea e assegna la carta
        if (carta == null) {

            if(!numeroCarta.matches(numeroCartaRegex)) {
                //todo butto eccezzione da gestire chi la chiama
                throw IllegalArgumentException("numero carta non valido")
            }

            if (!cvv.matches(cvvRegex)) {
                // todo CVV non valido: non ha 3 cifre
                throw IllegalArgumentException("cvv non valido")
            }

            val dataOdierna = LocalDate.now()
            if (dataScadenza <= dataOdierna) {
                // todo Data di scadenza non valida: è inferiore o uguale alla data odierna
                throw IllegalArgumentException("data non valida")
            }

            Log.i("msg", "carta generata")
            carta = CartaPrepagata(numeroCarta, cvv, dataScadenza)

        }
        Log.i("msg", "$carta")
    }

    fun clearCarta(): Utente{
        carta = null
        return this
    }

    fun generateCarta() {
        if(carta == null){
            val numeroCarta = generateNumeroCarta()
            val cvv = generateCVV()
            val dataScadenza = generateDataScadenza()

            setCarta(numeroCarta, cvv, dataScadenza)
        }
    }

    private fun generateNumeroCarta(): String {
        val numeroCarta = StringBuilder()
        repeat(10) {
            val cifra = Random.nextInt(10)
            numeroCarta.append(cifra)
        }
        return numeroCarta.toString()
    }

    private fun generateCVV(): String {
        val cvv = StringBuilder()
        repeat(3) {
            val cifra = Random.nextInt(10)
            cvv.append(cifra)
        }
        return cvv.toString()
    }

    private fun generateDataScadenza(): LocalDate {
        val today = LocalDate.now()
        val fiveYearsLater = today.plusYears(5)
        val randomDays = Random.nextLong(1, ChronoUnit.DAYS.between(today, fiveYearsLater))
        return today.plusDays(randomDays)
    }

    fun haveImageHome(): Boolean {
        return imageHomeCenter.isNotEmpty()
    }

    fun getImageHome(): MutableList<ViewModelHomePage> {
        return imageHomeCenter
    }

    fun addImageHome(viewModelHomePage: ViewModelHomePage){
        imageHomeCenter.add(viewModelHomePage)
    }

}