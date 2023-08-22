package com.example.sporttimer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.sporttimer.databinding.MainWindowBinding
import kotlinx.coroutines.NonCancellable.start

//Входящие данные
var allTime = 0
var timeToWork = 0
var timeToRest = 0

//Свои переменные
var workTimePlus: Long = 0
var restTimePlus: Long = 0
var alarm:  MediaPlayer? = null



class MainWindow : Fragment() {
    private lateinit var binding: MainWindowBinding
    private val openModel: OpenModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainWindowBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return (binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //LifeData
        openModel.allTime.observe(activity as LifecycleOwner) {
            allTime = it
        }
        openModel.workTime.observe(activity as LifecycleOwner) {
            timeToWork = it
        }
        openModel.restTime.observe(activity as LifecycleOwner) {
            timeToRest = it
        }




        //Заполнение данных
        inputData()
        //Работа
        workBtn()
        //Отдых
        restBtn()
        //Конвертация
        convertInMin()
        //Звук вкл/выкл
        muteOnOff()
        //Общий таймер
        fullTimer()


    }

    private fun fullTimer() {
        var timer = object : CountDownTimer(1000000.toLong() * 1000, 1000){
            override fun onTick(p0: Long) {
                binding.fullTimer.text = (1000000 - (p0 / 1000)).toString()

            }

            override fun onFinish() {
               alarmStop()
            }

        }.start()
    }


    private fun convertInMin() {
        binding.convButton.setOnClickListener {
            if (binding.statWorkTime.text.isNullOrEmpty() && binding.statRestTime.text.isNullOrEmpty()){
                binding.conv1.text = null
                binding.conv2.text = null
            } else if (!binding.statWorkTime.text.isNullOrEmpty() && binding.statRestTime.text.isNullOrEmpty()){
                binding.conv1.text = (binding.statWorkTime.text.toString().toInt() / 60).toString()
                binding.conv2.text = null
            }
            else {
                var con1: Float = binding.statWorkTime.text.toString().toFloat() / 60
                var con2: Float = binding.statRestTime.text.toString().toFloat() / 60
                binding.conv1.text = String.format("%.1f",con1).toString()
                binding.conv2.text = String.format("%.1f",con2).toString()
            }
        }
    }


    private fun inputData() {
        if (allTime == 0) {
            binding.allTime.text = "Бесконечно"
            binding.workTime.text = timeToWork.toString()
            binding.restTime.text = timeToRest.toString()
        } else {
            binding.allTime.text = allTime.toString()
            binding.workTime.text = timeToWork.toString()
            binding.restTime.text = timeToRest.toString()
        }

    }

    private fun workBtn() {
        binding.apply {
            buttonWork.setOnClickListener {
                buttonWork.visibility = View.INVISIBLE
               var timer = object : CountDownTimer(timeToWork.toLong() * 1000, 1000){
                   override fun onTick(p0: Long) {
                       timer.text = (p0 / 1000).toString()
                       info.text = "Работа"
                       mute.visibility = View.INVISIBLE
                       alarmStop()

                   }

                   override fun onFinish() {
                       workTimePlus += timeToWork
                       buttonRest.visibility = View.VISIBLE
                       statWorkTime.text = workTimePlus.toString()
                       mute.visibility = View.VISIBLE
                       binding.mute.isChecked = true
                       alarmStart()

                   }

               }.start()
            }
        }

    }

    private fun restBtn() {
        binding.apply {
            buttonRest.setOnClickListener {
                buttonRest.visibility = View.INVISIBLE
                var timer = object : CountDownTimer(timeToRest.toLong() * 1000, 1000){
                    override fun onTick(p0: Long) {
                        timer.text = (p0 / 1000).toString()
                        info.text = "Отдых"
                        mute.visibility = View.INVISIBLE
                        alarmStop()

                    }

                    override fun onFinish() {
                        restTimePlus += timeToRest
                        buttonWork.visibility = View.VISIBLE
                        statRestTime.text = restTimePlus.toString()
                        mute.visibility = View.VISIBLE
                        binding.mute.isChecked = true
                        alarmStart()
                    }

                }.start()
            }
        }
    }

    private fun alarmStart(){
        if (alarm == null){
            alarm = MediaPlayer.create(context, R.raw.alarm)
            alarm?.start()
            alarm?.isLooping = true
        }

    }
    private fun alarmStop(){
        alarm?.stop()
        alarm?.release()
        alarm = null


        }

    private fun muteOnOff() {
        binding.mute.setOnClickListener {
            if (binding.mute.isChecked) {
                alarmStart()
            } else {
                alarmStop()
            }
        }
    }



    }

