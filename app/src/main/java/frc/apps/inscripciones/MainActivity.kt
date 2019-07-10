package frc.apps.inscripciones

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import frc.apps.inscripciones.models.BDregistro
import frc.apps.inscripciones.models.Inscrito

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), InscritoFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    val db: BDregistro = BDregistro(this)
    var spinnerIglesias: Spinner? = null
    var spinnerColores: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this, registroActivity::class.java)
            startActivity(intent)
        }

        //SPINNER COLORES
        spinnerColores = findViewById(R.id.spinnerColores)
        var adapterColores: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.colors_array, android.R.layout.simple_spinner_item)
        adapterColores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColores!!.setAdapter(adapterColores)
        spinnerColores!!.onItemSelectedListener = this

        //SPINNER IGLESIAS
        spinnerIglesias = findViewById(R.id.spinnerIglesias)
        var listaIglesias = arrayListOf<String>()
        var listaIglesiasBD = db.cantIglesias()
        listaIglesias.add("Todos")
        for(i in listaIglesiasBD){
            listaIglesias.add(i.nombre)
        }
        var adapterIglesias = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaIglesias)
        adapterIglesias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIglesias!!.adapter = adapterIglesias
        spinnerIglesias!!.onItemSelectedListener = this

        //LISTA INSCRITOS
        actualizarLista()

    }

    private fun actualizarLista() {
        var fragment = InscritoFragment()
        val b = Bundle()
        b.putString("iglesia", spinnerIglesias!!.selectedItem.toString())
        b.putString("color", spinnerColores!!.selectedItem.toString())
        fragment.arguments = b
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment, fragment)
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onListFragmentInteraction(item: Inscrito?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        actualizarLista()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
