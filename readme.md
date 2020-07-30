### To run

####Develop stage
1. `mvn clean -pl abtests -Denv=develop -am test`
2. `mvn clean -pl acms -Denv=develop -am test`
3. `mvn clean -pl cities -Denv=develop -am test`
4. `mvn clean -pl contentstore -Denv=develop -am test`
5. `mvn clean -pl tranbilog -Denv=develop -am test`
6. `mvn clean -pl geofacade -Denv=develop -am test`
7. `mvn clean -pl feedback -Denv=develop -am test`
8. `mvn clean -pl offices -Denv=develop -Ddblogin={login} -Ddbpassword={password} -am test`
9. `mvn clean -pl tranbilog -Denv=develop -am test`

####PreProd stage
1. `mvn clean -pl abtests -Denv=preprod -am test`
2. `mvn clean -pl acms -Denv=preprod -am test`
3. `mvn clean -pl cities -Denv=preprod -am test`
4. `mvn clean -pl contentstore -Denv=preprod -am test`
5. `mvn clean -pl tranbilog -Denv=preprod -am test`
6. `mvn clean -pl geofacade -Denv=preprod -am test`
7. `mvn clean -pl feedback -Denv=preprod -am test`
8. `mvn clean -pl offices -Denv=preprod -Ddblogin={login} -Ddbpassword={password} -am test`
9. `mvn clean -pl tranbilog -Denv=preprod -am test`

####Prod stage
1. `mvn clean -pl offices -Denv=prod -Ddblogin={login} -Ddbpassword={password} -am test`

####ACMS-feature stage
1. `mvn clean -pl abtests -Denv=acms_feature-#### -am test`
2. `mvn clean -pl acms -Denv=acms_feature-#### -am test`
3. `mvn clean -pl cities -Denv=acms_feature-#### -am test`
4. `mvn clean -pl contentstore -Denv=acms_feature-#### -am test`
5. `mvn clean -pl tranbilog -Denv=acms_feature-#### -am test`
6. `mvn clean -pl geofacade -Denv=acms_feature-#### -am test`
7. `mvn clean -pl feedback -Denv=acms_feature-#### -am test`
8. `mvn clean -pl tranbilog -Denv=acms_feature-#### -am test`

####CS-feature stage
1. `mvn clean -pl abtests -Denv=cs_feature-#### -am test`
2. `mvn clean -pl acms -Denv=cs_feature-#### -am test`
3. `mvn clean -pl cities -Denv=cs_feature-#### -am test`
4. `mvn clean -pl contentstore -Denv=cs_feature-#### -am test`
5. `mvn clean -pl tranbilog -Denv=cs_feature-#### -am test`
6. `mvn clean -pl geofacade -Denv=cs_feature-#### -am test`
7. `mvn clean -pl feedback -Denv=cs_feature-#### -am test`
8. `mvn clean -pl tranbilog -Denv=cs_feature-#### -am test`