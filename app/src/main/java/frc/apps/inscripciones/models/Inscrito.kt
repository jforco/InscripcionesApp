package frc.apps.inscripciones.models
//nombre, sexo, edad, iglesia, pago, telefono, color
class Inscrito {
    var id:Int = 0
    var nombre:String = ""
    var genero:String = ""
    var edad:Int = 0
    var iglesia:String = ""
    var pago:Int = 0
    var color:String = ""

    override fun toString(): String {
        return nombre
    }
}