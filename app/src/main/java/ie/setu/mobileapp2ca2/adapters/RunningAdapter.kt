package ie.setu.mobileapp2ca2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.mobileapp2ca2.R
import ie.setu.mobileapp2ca2.databinding.CardTracksBinding
import ie.setu.mobileapp2ca2.models.RunningModel
import ie.setu.mobileapp2ca2.utils.customTransformation
import com.google.firebase.database.*

interface RunningClickListener {
    fun onTrackClick(track: RunningModel)
}

class RunningAdapter constructor(private var tracks: ArrayList<RunningModel>,
                                  private val listener: RunningClickListener,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<RunningAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTracksBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val track = tracks[holder.adapterPosition]
        holder.bind(track,listener)
    }

    fun removeAt(position: Int) {
        tracks.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = tracks.size

    inner class MainHolder(val binding : CardTracksBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        val readOnlyRow = readOnly

        fun bind(track: RunningModel, listener: RunningClickListener) {
            binding.root.tag = track
            binding.running = track
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onTrackClick(track) }

            var database: DatabaseReference = FirebaseDatabase.getInstance().reference
            val favouriteQuery = database.child("favourites").child(track.uid!!)
            //TODO: Rewrite this, dataSnapshot.exists() does everything,
            // is favouriteQuery !== null even needed?
            if (favouriteQuery !== null) {
                favouriteQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.imagefavourite.setImageResource(R.drawable.ic_card_favourite)
                        } else {
                            binding.imagefavourite.setImageResource(R.drawable.ic_card_regular)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        //
                    }
                })
            }

            binding.executePendingBindings()
            customTransformation()?.let {
                Picasso.get().load(track.profilepic.toUri())
                    .resize(200, 200)
                    .transform(it)
                    .centerCrop()
                    .into(binding.imageIcon)
            }
        }
    }
}