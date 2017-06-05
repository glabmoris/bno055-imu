#ifndef FW5632_HPP
#define FW5632_HPP

#include "../includes.hpp"


/*
 * FW5632 GPS sensor
 */

#define LONGITUDE 0
#define LATITUDE 1

class fw5632{
	public:
		void init();
		void showPosition();
		void readPosition(); //TODO make private once we have multithreading going
	private:
		int	device;
		double  position[2];
};


#endif
