package com.example.sporttimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sporttimer.databinding.SettingsBinding

private var checkStatus = true

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

        //Переключатель общего времени
        switchAllTime()

        //Сохранение данных
        saveButton()




    }

    private fun switchAllTime() {
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                checkStatus = false
                binding.inputAllTime.visibility = View.VISIBLE
            }
            else {
                checkStatus = true
                binding.inputAllTime.visibility = View.INVISIBLE
                binding.inputAllTime.text = null
            }
        }
    }

    private fun saveButton() {
        binding.apply {
            buttonSaveSettings.setOnClickListener {
                if (!switch1.isChecked && inputAllTime.text.isNullOrEmpty()){
                    info.text = "Введите общее время!"
                } else if (inputTimeWork.text.isNullOrEmpty() || inputTimeToRest.text.isNullOrEmpty()) {
                    info.text = "Заполните поля!"
                }  else {
                    if (!checkStatus){
                        openModel.allTime.value = inputAllTime.text.toString().toInt()
                        openModel.workTime.value = inputTimeWork.text.toString().toInt()
                        openModel.restTime.value = inputTimeToRest.text.toString().toInt()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment, MainWindow()).commit()
                    } else {
                        openModel.allTime.value = 0
                        openModel.workTime.value = inputTimeWork.text.toString().toInt()
                        openModel.restTime.value = inputTimeToRest.text.toString().toInt()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment, MainWindow()).commit()
                        parentFragmentManager.beginTransaction().remove(this@Settings).commit()
                    }

                }
            }
        }
    }


}