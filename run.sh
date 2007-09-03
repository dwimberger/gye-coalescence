JAVA_OPTS="-Xms64m -Xmx512m"
JAVA_CP=build/coalescence.jar:lib/commons-logging.jar:lib/log4j.jar:lib/colt.jar:lib/atv.jar:lib/commons-collections.jar:lib/jung.jar

java $JAVA_OPTS -classpath $JAVA_CP mx.unam.ecologia.gye.coalescence.app.$*
