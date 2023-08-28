package com.example.lidobalneare

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewListPrenotazioniBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class AdapterListPrenotazioni(val context: Context, val mList: List<ModelPrenotazione>) : RecyclerView.Adapter<AdapterListPrenotazioni.ViewHolder>() {

    inner class ViewHolder(val binding: CardViewListPrenotazioniBinding) : RecyclerView.ViewHolder(binding.root) {
        val cod = binding.codPrenotazione
        val date = binding.textDataPrenotazione
        val qr = binding.codiceQR
        val numPerson = binding.textNumPersonePrenotazione
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