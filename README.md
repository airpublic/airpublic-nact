To compile
```
mvn clean package
```

To run:
Copy the compiled JAR into Sensinact's bundle directory

```
curl -v
-d'lonet_bat=50&sample_flow_rate=1.0&longitude=3.4&w_pm2_5=100.0&co_w=100.0&heading=0.0&temp=5.0&pm1=100.0&no2_a=100.0&humidity=0.1&pm10=100.0&id=1&pm2_5=100.0&w_pm10=100.0&latitude=4.3&w_pm1=100.0&co_a=100.0&retries=1&no2_w=100.0&date=1.0&speed=0.0&gps_fix=1&sampling_period=15.0&last_organicity_sync=2017-01-02
12:09:23.467845+00:00&pt=100.0&timestamp=2017-01-02
12:09:23.467845+00:00&altitude=2.5' -XPOST http://localhost:8789/
```
