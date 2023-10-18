# working-hours

Application for representation working hours in readable condition from api.

How to run application:

1. cd ..\working-hours
2. mvn clean install 
3. cd target
4. java -jar working-hours-0.0.1-SNAPSHOT.jar


Edge cases for discussing:

1. How to check that opening and closing hours goes in ASC order in diapason of one day
2. What to do if for example opening is on monday and working continues till friday without breaks(big period of working hours)