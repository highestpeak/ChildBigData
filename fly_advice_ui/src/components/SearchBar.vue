<template>
  <div class="searchBar" style="display: inline-block;width:1000px;">
    <el-radio-group v-model="flightType" class="searchOption" @change="flightTypeChanged">
      <el-radio label="单程">单程</el-radio>
      <el-radio label="往返">往返</el-radio>
      <el-radio label="多程">多程</el-radio>
    </el-radio-group>
    <div class="searchContent">
      <!-- 单程和往返的UI -->
      <transition :name="!isMultiPass?'el-zoom-in-center':'el-zoom-out-center'">
        <div class="oneLineInput" v-show="!isMultiPass">
          <div class="searchInput">
            <el-autocomplete
              v-model="sourcePlace"
              :fetch-suggestions="querySearchAsync"
              :trigger-on-focus="false"
              placeholder="出发地 城市/机场"
              @select="handleSourceSelect(0)"
            ></el-autocomplete>

            <el-button
              class="fa fa-exchange fa-3g"
              type="success"
              @click="switchSourchDestination"
            ></el-button>

            <el-autocomplete
              v-model="destinationPlace"
              :fetch-suggestions="querySearchAsync"
              :trigger-on-focus="false"
              placeholder="到达地 城市/机场"
              @select="handleDestinationSelect(0)"
            ></el-autocomplete>

            <el-date-picker
              v-model="startDate"
              type="date"
              style="max-width:150px;"
              placeholder="出发日期"
            ></el-date-picker>
            <el-date-picker
              v-model="endDate"
              type="date"
              style="max-width:150px;"
              placeholder="返回日期"
              :disabled="flightType==='单程'"
            ></el-date-picker>

            <el-select v-model="cabinClass" multiple collapse-tags placeholder="舱位类型">
              <el-option
                v-for="item in cabinOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-button icon="el-icon-search" type="danger" @click="oneLineSearch"></el-button>
          </div>

          <div class="searchOption">
            <el-checkbox v-model="sourceNear">出发地附近机场</el-checkbox>
            <el-checkbox v-model="destinationNear">到达地附近机场</el-checkbox>
          </div>
          <div class="searchOption">
            <el-checkbox v-model="onlyDirect">仅直飞</el-checkbox>
          </div>
        </div>
      </transition>

      <!-- 多程的UI -->
      <transition :name="isMultiPass?'el-zoom-in-center':'el-zoom-out-center'">
        <div class="mutiLineInput" v-show="isMultiPass">
          <div class="searchInput" v-for="(currPass,index) in mutiPassList" :key="index">
            <el-autocomplete
              v-model="currPass.sourcePlace"
              :fetch-suggestions="querySearchAsync"
              :trigger-on-focus="false"
              placeholder="出发地 城市/机场"
              @select="handleSourceSelect(index)"
            ></el-autocomplete>

            <el-button
              class="fa fa-arrow-right fa-3g"
              style="max-width:400px;margin-left:10px;margin-right:10px"
              type="danger"
              disabled
            ></el-button>

            <el-autocomplete
              v-model="currPass.destinationPlace"
              :fetch-suggestions="querySearchAsync"
              :trigger-on-focus="false"
              placeholder="到达地 城市/机场"
              @select="handleDestinationSelect(index)"
            ></el-autocomplete>

            <el-date-picker
              v-model="currPass.startDate"
              type="date"
              style="max-width:400px;margin-left:10px;margin-right:10px"
              placeholder="出发日期"
            ></el-date-picker>

            <el-button icon="el-icon-close" type="danger" :disabled="index===0" 
              @click="delOneOfMuti(index)"
            ></el-button>
          </div>

          <div class="searchOption">
            <el-button icon="el-icon-plus" style="max-width:400px;margin-bottom:10px"
            @click="addOneOfMuti"
            >添加另一个航班</el-button>
            <el-select
              v-model="cabinClass"
              multiple
              collapse-tags
              placeholder="舱位类型"
              style="max-width:400px;margin-bottom:10px;margin-left:20px;"
            >
              <el-option
                v-for="item in cabinOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-button icon="el-icon-search" type="danger" style="margin-left:20px;width:100px"  @click="mutiLineSearch"></el-button>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
// import HelloWorld from '@/components/HelloWorld.vue'
import { searchAirportPrefix } from "@/utils/SimpleSearch";
import { airportsCached } from "@/utils/LocalCacheData";
export default {
  name: "SearchBar",
  components: {},
  data() {
    return {
      flightType: "单程",
      // 单程输入选项
      sourcePlace: "",
      destinationPlace: "",
      startDate: "",
      endDate: "",
      cabinClass: "",
      cabinOptions: [
        {
          value: "选项1",
          label: "经济舱"
        },
        {
          value: "选项2",
          label: "商务舱"
        },
        {
          value: "选项3",
          label: "头等舱"
        },
        {
          value: "选项4",
          label: "超级经济舱"
        }
      ],

      // 多程输入选项
      mutiPassList:[
        {sourcePlace:"",destinationPlace:"",startDate:""}
      ],

      // timeout: null,

      isMultiPass: false, // 是否多程
      sourceNear: false,
      destinationNear: false,
      onlyDirect: false
    };
  },
  methods: {
    /* 联想查询 */
    // 从本地缓存查找 机场列表
    querySearchAsync(queryString, cb) {
      var results = queryString
        ? airportsCached.filter(this.createStateFilter(queryString))
        : airportsCached;
      var finalResults = [];
      for (let index = 0; index < results.length; index++) {
        const airportFound = results[index];
        finalResults.push({
          value: airportFound["name"] + "(" + airportFound["IATA"] + ")",
          airport: airportFound
        });
      }
      // console.log(finalResults)
      cb(finalResults);
      // clearTimeout(this.timeout);
      // this.timeout = setTimeout(() => {
      //   cb(finalResults);
      // }, 1000);
    },
    // 过滤字符串，改变格式
    createStateFilter(queryString) {
      return airport => {
        var airportStr = airport["name"] + "(" + airport["IATA"] + ")";
        return airportStr.indexOf(queryString) != -1;
      };
    },

    switchSourchDestination() {
      var tmp = this.sourcePlace;
      this.sourcePlace = this.destinationPlace;
      this.destinationPlace = tmp;
    },

    // future:
    handleSourceSelect(source) {

    },
    handleDestinationSelect(destination) {

    },
    addOneOfMuti(){
      this.mutiPassList.push({sourcePlace:"",destinationPlace:"",startDate:""});
    },
    delOneOfMuti(delIndex){
      this.mutiPassList.splice(delIndex, 1);
    },
    flightTypeChanged(flightTypeSelected) {
      if (flightTypeSelected === "单程") {
        // console.log("单程");
        this.isMultiPass = false;
      }
      if (flightTypeSelected === "往返") {
        // console.log("往返");
        this.isMultiPass = false;
      }
      if (flightTypeSelected === "多程") {
        // console.log("多程");
        this.isMultiPass = true;
      }
    },

    // 点击搜索
    oneLineSearch(){
      // todo: 处理输入错误
      // 页面跳转 https://router.vuejs.org/zh/guide/essentials/navigation.html
      this.$router.push({ name: 'SearchResult', params: { userId: '123' }})
    },
    mutiLineSearch(){
      // todo: 处理输入错误
    },
  },
  mounted() {}
};
</script>

<style scoped>
/* 联想查询 */
.my-autocomplete li {
  line-height: normal;
  padding: 7px;
}
.my-autocomplete .name {
  text-overflow: ellipsis;
  overflow: hidden;
}
.my-autocomplete .addr {
  font-size: 12px;
  color: #b4b4b4;
}
.my-autocomplete .highlighted .addr {
  color: #ddd;
}

/* 搜索 */
.searchOption,
.searchInput {
  display: flex;
  flex-flow: row nowrap;
  margin: 0px 10px;
}

.searchInput * {
  margin: 10px 0px;
}
.searchBar .el-radio-group .el-radio {
  margin: 20px 10px;
}
.searchOption * {
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>

<style>
/* 搜索选项颜色 */
.el-checkbox__input.is-checked .el-checkbox__inner,
.searchOption .is-checked .el-radio__inner {
  border-color: #32e0c4;
  background: #32e0c4;
}
.el-radio__input.is-checked + .el-radio__label,
.el-checkbox__input.is-checked + .el-checkbox__label {
  color: #32e0c4;
}
</style>