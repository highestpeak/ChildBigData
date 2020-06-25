import axios from 'axios';
import { airportPrefixApi } from '@/api';
async function searchAirportPrefix(nameInput) {
    if (nameInput){
        return [];
    }
    var airportList = []
    await axios.get(airportPrefixApi, {
            params: {
                'type': 'name',
                'prefix': nameInput,
            },
        })
        .then(function (response) {

            airportList = response.data;
            // console.log(JSON.parse(response.data))
        })
        .catch(function (error) {
            // handle error
            console.log(error);
        })
        .then(function () {
            // always executed
        });
    console.log(airportList)
    var airportStrList = [];
    for (let index = 0; index < airportList.length; index++) {
        const airport = airportList[index];
        var airportStr = airport['name']+"("+airport['IATA']+")";
        // console.log(airport)
        airportStrList.push({
            value: airportStr,
          address: airport['name']
        })
    }
    console.log(airportStrList)
    return airportStrList;
}
export {
    searchAirportPrefix,
}