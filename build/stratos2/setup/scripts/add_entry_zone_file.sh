#!/bin/bash
zone_file=$3
subdomain=$1
ip=$2

# check the file
if [ -f {$zone_file} ]; then
	echo "Error: zone does not exist"
	exit 1
fi
echo "File $zone_file exists"

#appending the zone file
echo "$subdomain IN A $ip">> $zone_file
echo "Added subdomain to the file"

# get serial number
serial=$(grep 'Serial' $zone_file | awk '{print $1}')
echo "Serial number " $serial
# get serial number's date
serialdate=$(echo $serial | cut -b 1-8)
# get today's date in same style
date=$(date +%Y%m%d)


#Serial number's date
serialdate=$(echo $serial | cut -b 1-8)
echo "serial date" $serialdate
# get today's date in same style
date=$(date +%Y%m%d)
echo "Now date" $date

# compare date and serial date
if [ $serialdate = $date ]
	then
		# if equal, just add 1
		newserial=$(expr $serial + 1)
		echo "same date"
	else
		# if not equal, make a new one and add 00
		newserial=$(echo $date"00")
fi

echo "Adding subdomain $1 and ip $2 to $3"
sed -i "s/.*Serial.*/ \t\t\t\t$newserial ; Serial./" $zone_file



#reloading bind server
/etc/init.d/bind9 reload
