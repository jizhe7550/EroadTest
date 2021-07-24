# EroadTest
A sensor accelerometer

This project can get sensor accelerometer data and record them in the app-specific external space.

There are some packages:

component:
 1. MainActivity: A toggleButton can start or stop service; check button will the recorded files.
 2. SensorService: get sensor data when it is running.

logic:
 1. SensorDataManager: A class for handling sensor data which is using a channel to get and send them sequentially
  and then write to file
 2. SearchDataHelper: A class for search data with a given interval

TODO: 
 1.handle edge values in the earliest file and the latest file with filter files [sensorDataModels]
 2.add Unit testing.