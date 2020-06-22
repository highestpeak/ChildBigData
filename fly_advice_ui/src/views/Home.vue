<template>
  <div class="home" >
    <div  style="background-color:#f5f5f5">
      <SearchBar />
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import SearchBar from '@/components/SearchBar.vue'

export default {
  name: 'Home',
  components: {
    SearchBar
  }
}
</script>
