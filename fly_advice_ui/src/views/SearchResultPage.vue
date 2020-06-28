<template>
  <div class="SearchResultPage">
    <div style="background-color:#f4f0f0;">
      <SearchBar />
    </div>
    <!-- 消息提示公告 -->
    <el-container direction="vertical" style="display: inline-block;width:900px;">
      <el-alert
        class="alertInfo"
        title="消息提示"
        type="info"
        description="以防您的旅行计划有变或取消。请务必在预订前阅读航空公司的具体条款。"
        show-icon
        style="text-align:left;margin:5px auto;"
      ></el-alert>
      <el-alert
        class="alertInfo"
        title="消息提示"
        type="info"
        description="我们建议您在预订前咨询那些可能允许机票退改的航空公司和旅行中介。"
        show-icon
        style="text-align:left;margin:5px auto;"
      ></el-alert>
    </el-container>
    <!-- 没有从homepage跳转过来的提示信息 -->

    <!-- 结果页面 -->
    <SearchResult
      v-loading="loading"
      :resultList="flightList"
      v-show="true"
      element-loading-text="航班搜索中"
      element-loading-spinner="fa fa-refresh fa-spin fa-3x fa-fw"
      element-loading-background="#e7dfd5"
    />
  </div>
</template>

<script>
import SearchBar from "@/components/SearchBar.vue";
import SearchResult from "@/components/SearchResult.vue";
import { searchFlight } from "@/utils/FlightSearch";
export default {
  name: "SearchResultPage",
  props: [],
  components: {
    SearchResult,
    SearchBar
  },
  data() {
    return {
      loading: true,
      fromHomePage: false,
      warnShowing: false,
      flightList: [],

      muti: false,
      toSearch: null
    };
  },
  methods: {
    notFromHomeWarningInfo() {
      if (this.warnShowing) {
        return;
      }
      const h = this.$createElement;
      this.$notify({
        title: "错误操作警告",
        message: h(
          "i",
          { style: "color: teal" },
          "尊敬的同志您好，貌似您没有在Home页面搜索而直接进入结果页。建议您在这里输入搜索信息然后等待结果"
        ),
        type: "warning"
      });
      this.warnShowing = true;
    },
    testFromHomePage() {
      if (this.muti != null && this.toSearch != null) {
        this.fromHomePage = true;
      } else {
        this.fromHomePage = false;
      }
      if (!this.fromHomePage) {
        this.notFromHomeWarningInfo();
      }
    },
    async getFlightList() {
      this.flightList = [];
      this.loading = true;
      var flightList;
      var params;
      if (!this.muti) {
        params ={
          muti:false,
          target:this.toSearch,
        }
      } else {
        params ={
          muti:true,
          searchList:[]
        }
        for (let index = 0; index < this.toSearch.items.length; index++) {
          const element = this.toSearch.items[index];
          params.searchList.push(element);
        }
      }
      console.log(params)
      flightList = await searchFlight(params);
      // console.log(flightList);
      this.loading = false;
      this.flightList = flightList;
    }
  },
  created() {
    this.muti = this.$route.params.muti;
    this.toSearch = this.$route.params.toSearch;
    this.testFromHomePage();
    this.getFlightList();
  },
  mounted() {
    this.testFromHomePage();
  }
};
</script>
<style>
.el-loading-spinner i {
  color: #204051;
}
.el-loading-spinner .el-loading-text {
  color: #204051;
  margin: 3px 0;
  font-size: 24px;
}
</style>