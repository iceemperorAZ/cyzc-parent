# 临时改变 BUILD_ID 值
OLD_BUILD_ID=$BUILD_ID
echo $OLD_BUILD_ID
BUILD_ID=dontKillMe
cp -rf /home/cleancode/.jenkins/workspace/cyzc/shell/cyzc-manage.sh /home/cleancode/app/cyzc/cyzc-manage.sh
chmod 755 /home/cleancode/app/cyzc/cyzc-manage.sh
cp -rf /home/cleancode/.jenkins/workspace/cyzc/cyzc-manage/target/cyzc-manage-1\.0\.0.jar /home/cleancode/app/cyzc/cyzc-manage-1\.0\.0.jar
cd  /home/cleancode/app/cyzc/ && sh cyzc-manage.sh  restart

cp -rf /home/cleancode/.jenkins/workspace/cyzc/shell/cyzc-mobile.sh /home/cleancode/app/cyzc/cyzc-mobile.sh
chmod 755 /home/cleancode/app/cyzc/cyzc-mobile.sh
cp -rf /home/cleancode/.jenkins/workspace/cyzc/cyzc-mobile/target/cyzc-mobile-1\.0\.0.jar /home/cleancode/app/cyzc/cyzc-mobile-1\.0\.0.jar
cd  /home/cleancode/app/cyzc/ && sh cyzc-mobile.sh  restart
#改回原来的BUILD_ID值
BUILD_ID=$OLD_BUILD_ID
echo $BUILD_ID