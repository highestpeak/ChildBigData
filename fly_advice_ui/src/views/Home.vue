<template>
  <div class="home">
    <div class="appTitle">
      FLIGHT SEARCH
      <i class="fa fa-plane" aria-hidden="true" style="margin-left:20px"></i>
    </div>
    <div style="background-color:#f4f0f0;">
      <SearchBar></SearchBar>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import SearchBar from "@/components/SearchBar.vue";

export default {
  name: "Home",
  components: {
    SearchBar
  }
};
</script>

<style scoped>
@import "../assets/svg.css";
.appTitle {
  font-family: arial;
  font-size: 50px;
  font-weight: 700;
  font-style: oblique 25deg;
  margin: 50px;
  color: #10375c;
}
</style>