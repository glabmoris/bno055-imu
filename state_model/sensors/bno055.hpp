#ifndef _BNO055_HPP
#define _BNO055_HPP

#include "../includes.hpp"
#include "bno055_constants.hpp"

#define BNO055_ID        (0xA0)
#define I2C_ADDR 	 (0x28)
#define I2C_ADAPTER 	 (0x01)


#define ACCEL_X 0
#define ACCEL_Y 1
#define ACCEL_Z 2

#define COMPASS_X 3
#define COMPASS_Y 4
#define COMPASS_Z 5

#define GYRO_X 6
#define GYRO_Y 7
#define GYRO_Z 8

#define HEADING 9
#define ROLL 10
#define PITCH 11

#define QUATERNION_W 12
#define QUATERNION_X 13
#define QUATERNION_Y 14
#define QUATERNION_Z 15

#define LIA_X 16
#define LIA_Y 17
#define LIA_Z 18

#define GRAVITY_X 19
#define GRAVITY_Y 20
#define GRAVITY_Z 21

#define TEMPERATURE 22

class bno055{
     public:

    void 		init();
	void		showCurrentState();
	void    	showRawData();
	void    	readIMU(); //TODO make private after testing

     private:

	void    	vectorize(uint8_t * buf, double * output,double coefficient);
	void 		quaternionize(uint8_t * buffer, double * output, double coefficient);
	int     	device;
	uint8_t		rawData[45];
	double 		imu[23];

};


#endif
