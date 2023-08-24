package com.example.sporttimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sporttimer.databinding.SettingsBinding
import com.google.android.material.slider.Slider

private var checkStatus = true
private var numberOfHumans = 1

class Settings : Fragment() {
    private lateinit var binding: SettingsBinding
    private val openModel: OpenModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        //Сохранение данных
        saveButton()
        //Выбор количества человек
        slide()




    }



    private fun saveButton() {
        binding.apply {
            buttonSaveSettings.setOnClickListener {
               if (inputTimeWork.text.isNullOrEmpty() || inputTimeToRest.text.isNullOrEmpty()) {
                    info.text = "Заполните поля!"
                }  else {
                    if (!checkStatus){
                        openModel.workTime.value = inputTimeWork.text.toString().toInt()
                        openModel.restTime.value = inputTimeToRest.text.toString().toInt()
                        openModel.numberOfHuman.value = slider.value.toInt()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment, MainWindow()).commit()
                    } else {
                        openModel.workTime.value = inputTimeWork.text.toString().toInt()
                        openModel.restTime.value = inputTimeToRest.text.toString().toInt()
                        openModel.numberOfHuman.value = slider.value.toInt()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment, MainWindow()).commit()
                        parentFragmentManager.beginTransaction().remove(this@Settings).commit()
                    }

                }
            }
        }
    }

    private fun slide() {
        binding.apply {

            slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {
                    when (slider.value) {
                        1f ->info.text = "1 человек"
                        2f ->info.text = "2 человека"
                        3f ->info.text = "3 человека"
                        4f ->info.text = "4 человека"
                    }
                }
                override fun onStopTrackingTouch(slider: Slider) {
                    when (slider.value) {
                        1f -> {
                            numberOfHumans = 1
                            info.text = "1 человек"
                        }
                        2f -> {
                            numberOfHumans = 2
                            info.text = "2 человека"
                        }
                        3f -> {
                            numberOfHumans = 3
                            info.text = "3 человека"
                        }
                        4f -> {
                            numberOfHumans = 4
                            info.text = "4 человека"
                        }
                    }
                }
            })

        }


    }


}