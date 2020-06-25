<template>
  <div class="SearchResultPage">
    <div style="background-color:#f4f0f0;">
      <SearchBar />
    </div>
    
    <SearchResult/>
  </div>
</template>

<script>
import SearchBar from '@/components/SearchBar.vue'
import SearchResult from "@/components/SearchResult.vue";
export default {
  name:"SearchResultPage",
  components:{
    SearchResult,SearchBar
  }
}
</script>
