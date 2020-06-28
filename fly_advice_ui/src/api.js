// 高德地图 key
const MapKey = '3465aff0ab46da6e4c6ca443a34741fc'
const MapCityName = "成都"
// server地址
const mockServer = "https://201a6beb-3c4e-4f3c-b6ad-48c43c6a5d32.mock.pstmn.io/";
const localServer = "http://localhost:8081/";
const server = localServer;

// string format from stackoverflow
// https://stackoverflow.com/questions/610406/javascript-equivalent-to-printf-string-format/32202320#32202320
const airportPrefixApi = server + '/api/airport/prefix'
const flightSearchApi = server + '/api/flight/'
export{
    MapKey,MapCityName,
    server,
    airportPrefixApi,
    flightSearchApi
}