GNL=`pwd`

cd $GNL/granula-archiver
mvn clean install

cd $GNL/granula-modeller/giraph/1.1.0/analyzer
mvn clean install

cd $GNL/granula-modeller/graphx/1.1.1/analyzer
mvn clean install

cd $GNL/granula-modeller/mapreducev2/2.4.1/analyzer
mvn clean install
