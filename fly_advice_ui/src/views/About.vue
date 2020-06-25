<template>
  <div class="about">
    <div>
      <SearchBar />
    </div>
    
    <SearchResult/>
  </div>
</template>

<script>
import SearchBar from '@/components/SearchBar.vue'
import SearchResult from "@/views/SearchResultPage.vue";
export default {
  name:"",
  components:{
    SearchResult,SearchBar
  }
}
</script>
