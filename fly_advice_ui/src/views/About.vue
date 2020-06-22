<template>
  <div class="about">
    <div  style="background-color:#f5f5f5">
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
