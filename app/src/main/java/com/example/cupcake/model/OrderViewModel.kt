package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    //usando o método Transformations.map() para formatar o preço de acordo com a moeda local.
    // Você transformará o preço original de um valor decimal (LiveData<Double>)
    // em um valor de string (LiveData<String>).
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        upDatePrice()
    }

    fun setFlavor(desireFlavor: String){
        _flavor.value = desireFlavor
    }

    fun setDate(pickupDate: String){
        _date.value = pickupDate
        upDatePrice()
    }

    fun hasNoFlavorSet(): Boolean{
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MM d", Locale.getDefault())
        //calendar vai pegar a data e hora atual
        val calendar = Calendar.getInstance()
        //cria uma list de datas começando com a data atual e as próximas 3 datas
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options

    }

    fun resetOrder(){
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun upDatePrice(){
        //operador elvis (?:). O operador elvis (?:) significa que,
        // se a expressão à esquerda não for nula, ela será usada.
        // Caso a expressão à esquerda seja nula, use a expressão à direita do operador elvis,
        // que é 0 neste caso.
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE

        if(dateOptions[0] == date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }

        _price.value = calculatedPrice

    }
}