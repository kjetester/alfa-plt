### To run

####Develop stage
1. `mvn clean -pl abtests -Denv=develop -am test`
2. `mvn clean -pl acms -Denv=develop -am test`
3. `mvn clean -pl contentstore -Denv=develop -am test`
4. `mvn clean -pl tranbilog -Denv=develop -am test`

####PreProd stage
1. `mvn clean -pl abtests -Denv=preprod -am test`
2. `mvn clean -pl acms -Denv=preprod -am test`
3. `mvn clean -pl contentstore -Denv=preprod -am test`
4. `mvn clean -pl tranbilog -Denv=preprod -am test`

####ACMS-feature stage
1. `mvn clean -pl abtests -Denv=acms_feature-#### -am test`
2. `mvn clean -pl acms -Denv=acms_feature-#### -am test`
3. `mvn clean -pl contentstore -Denv=acms_feature-#### -am test`
4. `mvn clean -pl tranbilog -Denv=acms_feature-#### -am test`

####CS-feature stage
1. `mvn clean -pl abtests -Denv=cs_feature-#### -am test`
2. `mvn clean -pl acms -Denv=cs_feature-#### -am test`
3. `mvn clean -pl contentstore -Denv=cs_feature-#### -am test`
4. `mvn clean -pl tranbilog -Denv=cs_feature-#### -am test`