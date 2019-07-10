package frc.apps.inscripciones

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import frc.apps.inscripciones.InscritoFragment.OnListFragmentInteractionListener
import frc.apps.inscripciones.models.Inscrito
import kotlinx.android.synthetic.main.fragment_inscrito.view.*

class InscritoAdapter(
    private val mValues: ArrayList<Inscrito>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<InscritoAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Inscrito
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_inscrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.nombre.text = item.nombre
        holder.genero.text = item.genero
        holder.edad.text = item.edad.toString()
        holder.color.text = item.color

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val nombre: TextView = mView.nombre
        val genero: TextView = mView.genero
        val edad: TextView = mView.edad
        val color: TextView = mView.color

        override fun toString(): String {
            return super.toString() + " '" + nombre.text + "'"
        }
    }
}
