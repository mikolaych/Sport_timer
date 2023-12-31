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

//Входящие данные
  var timeToWork = 0
  var timeToRest = 0
  var numberOfHumanses = 1

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
        openModel.numberOfHuman.observe(activity as LifecycleOwner) {
            numberOfHumanses = it
        }
        openModel.workTime.observe(activity as LifecycleOwner) {
            timeToWork = it
        }
        openModel.restTime.observe(activity as LifecycleOwner) {
            timeToRest = it
        }


        //Заполнение данных
        inputData()
        //Конвертация
        convertInMin()
        //Звук вкл/выкл
        muteOnOff()
        //Общий таймер
        fullTimer()
        // +/- люди
        plusMinusHumans()
        // +/- время работы
        plusMinusWorkTime()
        // +/- время отдыха
        plusMinusRestTime()

        //Основная логика

        var numberOfCycle = 0
        binding.buttonWork.setOnClickListener {
            if (numberOfCycle < numberOfHumanses) {
                work(numberOfCycle, numberOfHumanses)
                numberOfCycle++
            } else {
                rest()
                numberOfCycle = 0
            }
        }


    }

    //Таймер общего времени
    private fun fullTimer() {
        var timer = object : CountDownTimer(1000000.toLong() * 1000, 1000){
            override fun onTick(p0: Long) {
                binding.fullTimer.text = (1000000 - (p0 / 1000)).toString()

            }

            override fun onFinish() {
               alarm(false)
            }

        }.start()
    }

  //Конвертация потраченного времени в минуты
    private fun convertInMin() {
        binding.convButton.setOnClickListener {
            if (binding.statWorkTime.text.isNullOrEmpty() || binding.statRestTime.text.isNullOrEmpty()){
                var conAll: Float = binding.fullTimer.text.toString().toFloat() / 60

                binding.convertTimer.text = String.format("%.1f", conAll)
                binding.conv1.text = "0"
                binding.conv2.text = "0"

            } else {
                var con1: Float = binding.statWorkTime.text.toString().toFloat() / 60
                var con2: Float = binding.statRestTime.text.toString().toFloat() / 60
                var conAll: Float = binding.fullTimer.text.toString().toFloat() / 60

                binding.convertTimer.text = String.format("%.1f", conAll)
                binding.conv1.text = String.format("%.1f", con1)
                binding.conv2.text = String.format("%.1f", con2)
            }
        }
    }

    //Запись данных о тренировке
    private fun inputData() {
            binding.numberOfHuman.text = numberOfHumanses.toString()
            binding.workTime.text = timeToWork.toString()
            binding.restTime.text = timeToRest.toString()


    }

    //Цикл работы
    private fun work(cycleNumber: Int, numberOfHuman: Int) {
        binding.apply {
                       var timer = object : CountDownTimer(timeToWork.toLong() * 1000, 1000) {
                           override fun onTick(p0: Long) {
                               buttonWork.visibility = View.INVISIBLE
                               mute.visibility = View.INVISIBLE
                               info.text = "Проход № ${cycleNumber + 1}"
                               timer.text = (p0 / 1000).toString()
                               alarm(false)
                           }

                           override fun onFinish() {
                               workTimePlus += timeToWork
                               buttonWork.visibility = View.VISIBLE
                               statWorkTime.text = workTimePlus.toString()
                               mute.visibility = View.VISIBLE
                               timer.text = null
                               if ((cycleNumber+1) == numberOfHuman) {
                                   info.text = "След. отдых"
                               } else {
                                   info.text = "След. проход № ${cycleNumber + 2}"
                               }
                               binding.mute.isChecked = true
                               alarm(true)
                           }

                       }.start()
        }
    }

    //Цикл отдыха

    private fun rest() {
        binding.apply {
                var timer = object : CountDownTimer(timeToRest.toLong() * 1000, 1000){
                    override fun onTick(p0: Long) {
                        buttonWork.visibility = View.INVISIBLE
                        timer.text = (p0 / 1000).toString()
                        info.text = "Отдых"
                        mute.visibility = View.INVISIBLE
                        alarm(false)

                    }
                    override fun onFinish() {
                        restTimePlus += timeToRest
                        buttonWork.visibility = View.VISIBLE
                        statRestTime.text = restTimePlus.toString()
                        mute.visibility = View.VISIBLE
                        timer.text = null
                        info.text = "След. проход № 1"
                        binding.mute.isChecked = true
                        alarm(true)
                    }

                }.start()

        }
    }

    private fun alarm(status: Boolean){
        if (status){
            if (alarm == null){
                alarm = MediaPlayer.create(context, R.raw.alarm)
                alarm?.start()
                alarm?.isLooping = true
            }
        } else {
            alarm?.stop()
            alarm?.release()
            alarm = null
        }


    }

    //Мут сигнала
    private fun muteOnOff() {
        binding.mute.setOnClickListener {
            if (binding.mute.isChecked) {
                alarm(true)
            } else {
                alarm(false)
            }
        }
    }

    private fun plusMinusWorkTime() {
        binding.apply {
            plusWorkTime.setOnClickListener {
                timeToWork+=10
                workTime.text = timeToWork.toString()
            }
                minusWorkTime.setOnClickListener {
                    if (timeToWork > 10) {
                    timeToWork -= 10
                    workTime.text = timeToWork.toString()
                }
            }
        }
    }

    private fun plusMinusRestTime() {
        binding.apply {
            plusRestTime.setOnClickListener {
                timeToRest+=10
                restTime.text = timeToRest.toString()
            }
                minusRestTime.setOnClickListener {
                    if (timeToRest > 10) {
                    timeToRest -= 10
                    restTime.text = timeToRest.toString()
                }
            }
        }
    }

    private fun plusMinusHumans() {
        binding.apply {
            plusHumans.setOnClickListener {
                if (numberOfHumanses < 4) {
                    numberOfHumanses+=1
                    numberOfHuman.text = numberOfHumanses.toString()
                }
            }
            minusHumans.setOnClickListener {
                if (numberOfHumanses > 1) {
                    numberOfHumanses-=1
                    numberOfHuman.text = numberOfHumanses.toString()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        alarm(false)
    }



    }

