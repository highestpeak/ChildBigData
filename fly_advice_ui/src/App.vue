<template>
  <div id="app">
    <!-- 联调 https://zh.flightaware.com/live/flight/VHVUE 站点 的实时图 -->
    <!-- gmap游览器精确定位 https://lbs.amap.com/api/javascript-api/example/location/custom-location-icon -->

    <div id="nav">
      <router-link to="/">HomeSearch</router-link>|
      <router-link to="/searchResult">SearchResult</router-link>|
      <router-link to="/map">MapAnalysis</router-link>|
      <router-link to="/chart">ChartAnalysis</router-link>|
      <router-link to="/center">Center</router-link>
    </div>
    <transition name="el-fade-in-linear">
      <router-view />
    </transition>

    <div style="margin:50px 0px;font-weight:700;">
      <a class="repoLink" href="https://github.com/highestpeak/ChildBigData" target="_blank">
      BUILD BY CHILD BIG DATA</a> | POWER BY VUE AND SPRING
    </div>
  </div>
</template>

<style>
@import "./assets/svg.css";
body {
  background-color: #f5f5f5 !important;
}
</style>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

.repoLink,
#nav a {
  font-weight: bold;
  color: #2c3e50;
}

.repoLink:hover,
#nav a.router-link-exact-active,
#nav a:hover {
  color: #42b983;
}
</style>
