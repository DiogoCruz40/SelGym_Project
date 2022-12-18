package pt.selfgym;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.selfgym.database.AppDatabase;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.mappers.Mapper;
import pt.selfgym.services.AppExecutors;


public class SharedViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private final MutableLiveData<List<WorkoutDTO>> workouts = new MutableLiveData<List<WorkoutDTO>>();
    private final MutableLiveData<String> toastMessageObserver = new MutableLiveData<String>();
    //    private static boolean trigger = true;

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getToastMessageObserver() {
        return this.toastMessageObserver;
    }

    public MutableLiveData<List<WorkoutDTO>> getWorkouts() {
        return workouts;
    }

    public void startDB() {
        mDb = AppDatabase.getInstance(getApplication().getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all workouts
                Mapper mapper = new Mapper();
//                List<WorkoutDTO> workoutDTOList = mapper.toDTOs(mDb.WorkoutDAO().(),WorkoutDTO.class);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

//                        if (workoutDTOList == null) {
//                            workouts.setValue(new ArrayList<WorkoutDTO>());
//                        } else {
//                            workouts.setValue(workoutDTOList);
//                        }

                        /*
                        System.out.println("AQUI: " + pointsDTO.size());

                        for(PointDTO p: pointsDTO){
                            System.out.println(p.getTimestamp() + " " + p.getTemperature());
                        }*/

                    }
                });
            }
        });
    }
//
//
//    public void insertPoint(Float temp, Float hum, String timestamp) {
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                // insert point
//                PointMapperInterface noteMapperInterface = new PointMapper();
//                PointDTO pointsDTO = new PointDTO(timestamp, temp, hum);
//                pointsDTO.setId((int) mDb.pointsDAO().insert(noteMapperInterface.toEntityPoint(pointsDTO)));
////                Log.w("mqtt", String.valueOf(pointsDTO.getHumidity()));
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<PointDTO> aux = points.getValue();
//                        if (aux != null) {
//                            aux.add(pointsDTO);
//                            points.setValue(aux);
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    public void deleteAllPoints() {
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                // delete all notes
//                List<Point> pointsDB = mDb.pointsDAO().getAll();
//
//                for (Point n : pointsDB) {
//                    mDb.pointsDAO().delete(n);
//                }
//
//            }
//        });
//    }
//
//    public void connmqtt(ActivityInterface activityInterface) {
//        mqttHelper = new MQTTHelper(getApplication().getApplicationContext(), MqttClient.generateClientId());
//        mqttHelper.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//                subscribeToTopic("TempHumADM");
//                subscribeToTopic("LEDADM");
//                Log.w("mqtt", "connected");
//                toastMessageObserver.setValue("MQTT conn and sub successful");
//            }
//
//            @Override
//            public void connectionLost(Throwable cause) {
//                Log.w("mqtt", cause);
//                toastMessageObserver.setValue("Connection Lost. Restart the app");
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                if (Objects.equals(topic, "TempHumADM")) {
//                    if (trigger) {
//                        publishMessage("state", "LEDADM");
//                        trigger = false;
//                    }
//                    //                {"humidity":12.56,"temperature":44.05,"timestamp":""}
//                    PointDTO pointDTO = new Gson().fromJson(message.toString(), PointDTO.class);
//                    if (pointDTO.getTimestamp() != null) {
//                        activityInterface.insertPointAct(pointDTO);
//
//                    }
//                } else if (Objects.equals(topic, "LEDADM")) {
//                    if (!message.toString().equals("state")) {
//                        activityInterface.setLedAct(message.toString());
//                        unsubscribeToTopic("LEDADM");
//                    }
//                }
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });
//        mqttHelper.connect();
//    }
//
//

}