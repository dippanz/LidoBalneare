package com.example.lidobalneare

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewListPrenotazioniBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class AdapterListPrenotazioni(val context: Context, val mList: MutableList<ModelPrenotazione>) : RecyclerView.Adapter<AdapterListPrenotazioni.ViewHolder>() {

    inner class ViewHolder(val binding: CardViewListPrenotazioniBinding) : RecyclerView.ViewHolder(binding.root) {
        val cod = binding.codPrenotazione
        val date = binding.textDataPrenotazione
        val qr = binding.codiceQR
        val numPerson = binding.textNumPersonePrenotazione
        val buttonCancella = binding.buttonCancellaPrenotazioni
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterListPrenotazioni.ViewHolder {
        val binding = CardViewListPrenotazioniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    override fun onBindViewHolder(holder: AdapterListPrenotazioni.ViewHolder, position: Int) {
        val modelPrenotazione = mList[position]

        holder.cod.text = context.getString(R.string.codice_prenotazione_1_s, modelPrenotazione.codPrenotazione.toString())
        holder.date.text = context.getString(R.string.data_1_s, modelPrenotazione.data)
        holder.numPerson.text = context.getString(R.string.n_persone_1_s, modelPrenotazione.numPerson.toString())
        holder.qr.setImageBitmap(generateQRCode(
            modelPrenotazione.codPrenotazione,
            holder.qr.layoutParams.width,
            holder.qr.layoutParams.height
        ))

        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.ITALIAN)

            val date = dateFormat.parse(modelPrenotazione.data)
            val localDate =
                date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.withYear( LocalDate.now().year)

            if(localDate != null && localDate.isAfter(LocalDate.now())){
                holder.binding.attivo.visibility = View.VISIBLE
                holder.buttonCancella.visibility = View.VISIBLE

                //se la prenotazione è ancora attiva posso cancellarla altrimenti no
                holder.buttonCancella.setOnClickListener {
                    showConfirmationDialog(holder.itemView.context, position)
                }

            }else{
                holder.binding.attivo.visibility = View.GONE
                holder.buttonCancella.visibility = View.GONE
            }
    }

    private fun showConfirmationDialog(c: Context, position: Int) {
        val builder = AlertDialog.Builder(c)
        builder.setTitle("Conferma cancellazione")
        builder.setMessage("Sei sicuro di voler cancellare la prenotazione?\nSi ricorda che non sono previsti rimborsi!")

        // Aggiungere il pulsante "OK" per confermare la cancellazione
        builder.setPositiveButton("OK") { dialog, which ->
            // Questa parte verrà eseguita quando l'utente preme il pulsante "OK"

            // Rimuovi l'elemento dalla lista e dal database
            val removedItem = mList.removeAt(position)
            DBMSboundary().removePrenotazione(removedItem.codPrenotazione.toString())

            // Notifica all'adapter dei cambiamenti
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mList.size)
        }

        // Aggiungere il pulsante "Annulla" per annullare l'operazione
        builder.setNegativeButton("Annulla") { dialog, which ->

        }

        // Mostra il messaggio di conferma
        val dialog: AlertDialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    // Funzione per generare un codice QR da un ID numerico
    private fun generateQRCode(id: Int, width: Int, height: Int): Bitmap? {
        try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix: BitMatrix = qrCodeWriter.encode(id.toString(), BarcodeFormat.QR_CODE, width, height)

            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            return bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }
    }


}