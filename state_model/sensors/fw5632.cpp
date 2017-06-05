#ifndef FW5632_CPP
#define FW5632_CPP

#include "fw5632.hpp"

void fw5632::init(){

}

void fw5632::readPosition(){
	std::string line;
	std::ifstream myfile ("/dev/ttyAMA0");

	if (myfile.is_open()){
		while ( getline (myfile,line) ){


			/* Get the GPGGA sentences such as 
				$GPGGA,002138.000,4826.4088,N,06831.6249,W,1,08,1.19,24.7,M,-25.8,M,,*6D
			*/

			if(line.length() > 6 && line.substr(0,6).compare("$GPGGA") == 0){
				std::cout << line << '\n';
			}
    		}
		myfile.close();
	}
}


void fw5632::showPosition(){
	std::cout << "-------------------------------" << std::endl;

	std::cout << "LONGITUDE: " << position[LONGITUDE] << std::endl;
	std::cout << "LATITUDE:  " << position[LATITUDE]  << std::endl;

        std::cout << "-------------------------------" << std::endl;
}

#endif
