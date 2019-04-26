FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/user_dashboard.jar /user_dashboard/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/user_dashboard/app.jar"]
