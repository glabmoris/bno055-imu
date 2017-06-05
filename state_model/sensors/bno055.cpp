#ifndef BNO055_CPP
#define BNO055_CPP

#include "bno055.hpp"

void bno055::init(){
        char filename[20];

        snprintf(filename, 19, "/dev/i2c-%d", I2C_ADAPTER);
        device = open(filename, O_RDWR);

        if (device < 0) {
		perror("BNO055");
                exit(1);
        }

        if (ioctl(device, I2C_SLAVE, I2C_ADDR) < 0) {
		perror("BNO055");
                exit(1);
        }

	/* Check that we have a connected device */
	uint8_t id = i2c_smbus_read_byte_data(device,BNO055_CHIP_ID_ADDR);

	if(id != BNO055_ID){
		usleep(1000000); // hold on for boot
		if(id != BNO055_ID) {
			//TODO add sane error feedback
			exit(1);  // still not? ok bail
		}
	}


	/* Set CONFIG mode */
	i2c_smbus_write_byte_data(device,BNO055_OPR_MODE_ADDR, OPERATION_MODE_CONFIG);
	usleep(30000);

	/* Reset */
	i2c_smbus_write_byte_data(device,BNO055_SYS_TRIGGER_ADDR, 0x20);
	while (i2c_smbus_read_byte_data(device,BNO055_CHIP_ID_ADDR) != BNO055_ID){
		usleep(10000);
	}
	usleep(50000);

	/* Set to normal power mode */
	i2c_smbus_write_byte_data(device,BNO055_PWR_MODE_ADDR, POWER_MODE_NORMAL);
	usleep(10000);

	i2c_smbus_write_byte_data(device,BNO055_PAGE_ID_ADDR, 0);

	/* Set the output units */
 	uint8_t unitsel = 0b00000000 | // Accel / lia / gravity = m/s^2
	                  0b00000010 | // Gyro = radians
        	          0b00000000 | // Euler = radians
                	  0b00000000 | // Temp = Celsius
                    	  0b10000000;  // Orientation = android
    	i2c_smbus_write_byte_data(device,BNO055_UNIT_SEL_ADDR, unitsel);


	/* set temperature source */
	i2c_smbus_write_byte_data(device,BNO055_TEMP_SOURCE_ADDR, 0x00);
	usleep(10000);

	/* lock down reset */
	i2c_smbus_write_byte_data(device,BNO055_SYS_TRIGGER_ADDR, 0x0);
	usleep(10000);

	/* Set the requested operating mode (see section 3.3) */
        i2c_smbus_write_byte_data(device,BNO055_OPR_MODE_ADDR, OPERATION_MODE_NDOF);
        usleep(50000);

	/* calibrate
	uint8_t calibration_status  = 0x00;

	do{
		calibration_status = i2c_smbus_read_byte_data(device,BNO055_CALIB_STAT_ADDR);
	}while(calibration_status != 0xFF);
	*/
}

void bno055::readIMU(){
	memset(rawData,0,45);
	memset(imu,0,sizeof(double) * 23);

	//i2c_smbus_read_block_data(device,BNO055_ACCEL_DATA_X_LSB_ADDR,rawData);


	for(uint8_t idx = BNO055_ACCEL_DATA_X_LSB_ADDR ; idx <= BNO055_TEMP_ADDR ; idx++){
		rawData[idx - BNO055_ACCEL_DATA_X_LSB_ADDR] = i2c_smbus_read_byte_data(device, idx);
		//printf("0x%02X\t%X\n",idx,(uint8_t)rawData[idx - BNO055_ACCEL_DATA_X_LSB_ADDR]);
	}


	//process data
	vectorize(rawData,imu,100);        	// accleration:  1m/s^2 = 100 LSB
	vectorize(rawData+6,imu+3,16);   	//magnetometer:  1uT = 16 LSB
	vectorize(rawData+12,imu+6,900); 	//gyroscope 1rps = 900 LSB 
	vectorize(rawData+18,imu+9,16);  	//Euler 1 degree = 900 LSB
	quaternionize(rawData+24,imu+12,1); //quaternion doesnt need normalizing
	vectorize(rawData+32,imu+16,100); 	//linear acceleration  1m/s^2 = 100 LSB
	vectorize(rawData+38,imu+19,100); 	//gravity 1m/s^2 = 100 LSB
	imu[TEMPERATURE] = (double)((int8_t) rawData[44]);
}

void bno055::showRawData(){
	for(uint8_t i = 0; i < 45 ; i++){
		printf("0x%02X\t%X\n",BNO055_ACCEL_DATA_X_LSB_ADDR + i,(uint8_t)rawData[i]);
	}
}

void bno055::showCurrentState(){

	std::cout << "-------------------------------" << std::endl;

	std::cout << "ACCEL_X: " << imu[ACCEL_X] << std::endl;
	std::cout << "ACCEL_Y: " << imu[ACCEL_Y] << std::endl;
	std::cout << "ACCEL_Z: " << imu[ACCEL_Z] << std::endl;

	std::cout << "MAGNET_X: " << imu[COMPASS_X] << std::endl;
        std::cout << "MAGNET_Y: " << imu[COMPASS_Y] << std::endl;
        std::cout << "MAGNET_Z: " << imu[COMPASS_Z] << std::endl;

        std::cout << "GYRO_X: " << imu[GYRO_X] << std::endl;
        std::cout << "GYRO_Y: " << imu[GYRO_Y] << std::endl;
        std::cout << "GYRO_Z: " << imu[GYRO_Z] << std::endl;

        std::cout << "HEADING: " << imu[HEADING] << std::endl;
        std::cout << "ROLL: " << imu[ROLL] << std::endl;
        std::cout << "PITCH: " << imu[PITCH] << std::endl;

        std::cout << "QUATERNION_W: " << imu[QUATERNION_W] << std::endl;
        std::cout << "QUATERNION_X: " << imu[QUATERNION_X] << std::endl;
        std::cout << "QUATERNION_Y: " << imu[QUATERNION_Y] << std::endl;
        std::cout << "QUATERNION_Z: " << imu[QUATERNION_Z] << std::endl;

        std::cout << "LINEAR_ACCEL_X: " << imu[LIA_X] << std::endl;
        std::cout << "LINEAR_ACCEL_Y: " << imu[LIA_Y] << std::endl;
        std::cout << "LINEAR_ACCEL_Z: " << imu[LIA_Z] << std::endl;

        std::cout << "GRAVITY_X: " << imu[GRAVITY_X] << std::endl;
        std::cout << "GRAVITY_Y: " << imu[GRAVITY_Y] << std::endl;
        std::cout << "GRAVITY_Z: " << imu[GRAVITY_Z] << std::endl;

	std::cout << "TEMPERATURE: " << imu[TEMPERATURE] << std::endl;

	std::cout << "-------------------------------" << std::endl;
}

void bno055::vectorize(uint8_t * buffer, double * output, double coefficient){

	int16_t x = ((int16_t)buffer[0]) | (((int16_t)buffer[1]) << 8);
	int16_t y = ((int16_t)buffer[2]) | (((int16_t)buffer[3]) << 8);
	int16_t z = ((int16_t)buffer[4]) | (((int16_t)buffer[5]) << 8);

/*
        int16_t x = (((int16_t)buffer[0]) << 8)  | ((int16_t)buffer[1]) ;
        int16_t y = (((int16_t)buffer[2]) << 8)  | ((int16_t)buffer[3]) ;
        int16_t z = (((int16_t)buffer[4]) << 8)  | ((int16_t)buffer[5]) ;
*/
	output[0] = ((double)x)/(double)coefficient;
	output[1] = ((double)y)/(double)coefficient;
	output[2] = ((double)z)/(double)coefficient;
}

void bno055::quaternionize(uint8_t * buffer, double * output, double coefficient){
	int16_t w = (((uint16_t)buffer[1]) << 8) | ((uint16_t)buffer[0]);
	int16_t x = (((uint16_t)buffer[3]) << 8) | ((uint16_t)buffer[2]);
	int16_t y = (((uint16_t)buffer[5]) << 8) | ((uint16_t)buffer[4]);
	int16_t z = (((uint16_t)buffer[7]) << 8) | ((uint16_t)buffer[6]);

	output[0] = ((double)w)/coefficient;
        output[1] = ((double)x)/coefficient;
        output[2] = ((double)y)/coefficient;
	output[3] = ((double)z)/coefficient;
}
#endif

