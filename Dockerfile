# 베이스 이미지 선택 (예: OpenJDK 11)
FROM openjdk:17
# 애플리케이션 파일 복사 (빌드된 JAR 파일)
COPY build/libs/SecureSchedulerAppServer-0.0.1-SNAPSHOT.jar SecureSchedulerAppServer.jar
# 도커 컨테이너가 시작될 때 실행할 명령어
ENTRYPOINT ["java","-jar","/SecureSchedulerAppServer.jar"]
