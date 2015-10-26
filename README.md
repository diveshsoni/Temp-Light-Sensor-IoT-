# Temp-Light-Sensor-IoT-
Temp-light sensor system is an automatic window blinds controller system which manipulates the blinds(LEDs in our case) considering the user preferences and the real-time sensor readings.

A server runs continuously on the Raspberry Pi and checks the temperature and light ambient readings constantly. Any changes in the sensor readings over a user-specified threshold triggers window blinds(LEDs) to be manipulated accordingly.Fuzzy logic is used to factor in the user preferences and then map them to current position of the window blinds(LEDS).

An android application is developed which complements this system by providing the users to specify additional preferences and view the current readings/log pertaining to the sensor data.
A server runs in the background on the Android application that continuously listens for any notifications from the server on the Pi as to changes in sensor data over the threshold.
The code for the clienr-side (Android application) is in the SmartBlinds folder whereas that for the server-side is in the Pervasive Computing folder.
