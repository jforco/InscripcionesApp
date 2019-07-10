package frc.apps.inscripciones

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import frc.apps.inscripciones.models.BDregistro

class registroActivity : AppCompatActivity(), View.OnClickListener {

    var spinnerIglesias: Spinner? = null
    var spinnerColores: Spinner? = null
    var calcularColor: Button? = null
    var registrar: Button? = null

    var nombre: EditText? = null
    var generoGroup: RadioGroup? = null
    var genero: RadioButton? = null
    var edad: EditText? = null
    var pago: EditText? = null


    val db: BDregistro = BDregistro(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        nombre = findViewById(R.id.nombre)
        generoGroup = findViewById(R.id.genero_radio)
        edad = findViewById(R.id.edad)
        pago = findViewById(R.id.pago)

        spinnerIglesias = findViewById(R.id.iglesia)
        var adapterIglesias: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.iglesias_array, android.R.layout.simple_spinner_item)
        adapterIglesias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIglesias!!.setAdapter(adapterIglesias)

        spinnerColores = findViewById(R.id.color)
        calcularColor = findViewById(R.id.calcular)
        calcularColor!!.setOnClickListener(this)
        registrar = findViewById(R.id.registrar)
        registrar!!.setOnClickListener(this)
        registrar!!.isEnabled = false
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.calcular -> {
                if(edad!!.text.isNotEmpty()){
                    //obtener lista
                    genero = findViewById(generoGroup!!.checkedRadioButtonId)
                    var g = genero!!.text.toString()
                    var e = edad!!.text.toString().toInt()
                    var lista = db.coloresSugeridos(g, e)
                    var listaColores = arrayListOf<String>()
                    for(i in lista){
                        listaColores.add(i.nombre)
                    }
                    //ponerlos en el spinner
                    var adapterColores = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaColores)
                    adapterColores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerColores!!.adapter = adapterColores
                    adapterColores.setNotifyOnChange(true)
                    //activar el registro
                    registrar!!.isEnabled = true
                } else {
                    Toast.makeText(this, "Pon la edad primero.", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.registrar -> {

            }
        }
    }
}
