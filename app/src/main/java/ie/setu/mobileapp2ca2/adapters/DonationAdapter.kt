package ie.setu.mobileapp2ca2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.mobileapp2ca2.R
import ie.setu.mobileapp2ca2.databinding.CardDonationBinding
import ie.setu.mobileapp2ca2.models.DonationModel
import ie.setu.mobileapp2ca2.utils.customTransformation

interface DonationClickListener {
    fun onDonationClick(donation: DonationModel)
}

class DonationAdapter constructor(private var donations: ArrayList<DonationModel>,
                                  private val listener: DonationClickListener,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<DonationAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDonationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val donation = donations[holder.adapterPosition]
        holder.bind(donation,listener)
    }

    fun removeAt(position: Int) {
        donations.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = donations.size

    inner class MainHolder(val binding : CardDonationBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        val readOnlyRow = readOnly

        fun bind(donation: DonationModel, listener: DonationClickListener) {
            binding.root.tag = donation
            binding.donation = donation
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onDonationClick(donation) }
            binding.executePendingBindings()
            customTransformation()?.let {
                Picasso.get().load(donation.profilepic.toUri())
                    .resize(200, 200)
                    .transform(it)
                    .centerCrop()
                    .into(binding.imageIcon)
            }
        }
    }
}