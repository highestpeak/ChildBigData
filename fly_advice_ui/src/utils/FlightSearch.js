import axios from 'axios';
import { flightSearchApi } from '@/api';

import mockJson from '@/mockData/flightSearchOnlyDirectCTU-SHA.json';
async function searchFlight(searchParamInput) {
    // var flightListOrign = [];
    // var getParams = {
    //     startPlace:'CTU',
    //     endPlace:'SHA',
    //     startDate:'20200701',
    //     cabinClass:'economy',
    //     rtn:0,
    //     rtnDate:'20200702',
    //     preferDirects:0,
    //     preferTransfer:0,
    //     preferStop:0,
    //     outboundAltEnabled:0,
    //     inboundAltEnabled:0,
    //     remainingVotes:3,
    // }
    // // flightSearchApi
    // await axios.get("", {
    //         params: getParams,
    //     })
    //     .then(function (response) {
    //         flightListOrign = response.data;
    //         // console.log(flightListOrign)
    //     })
    //     .catch(function (error) {
    //         // handle error
    //         console.log(error);
    //     })
    //     .then(function () {
    //         // always executed
    //     });
    return mockJson;
}
export {
    searchFlight
}