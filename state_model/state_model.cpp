#include "sensors/bno055.cpp"
#include "sensors/fw5632.cpp"



int main(){
	//Init IMU
	bno055 imu;
	imu.init();
        std::cout << "[+] INS initialized" << std::endl;

	//Init GPS
	fw5632 gps;
	gps.init();

	while(1){
		imu.readIMU();
//		imu.showRawData();
		imu.showCurrentState();

		gps.readPosition();
		gps.showPosition();
//		usleep(100000);
	}
}
