<template>
  <div class="searchBar" style="display: inline-block;width:1000px;">
    <el-radio-group v-model="radio" class="searchOption">
      <el-radio :label="3">单程</el-radio>
      <el-radio :label="6">往返</el-radio>
      <el-radio :label="9">多程</el-radio>
    </el-radio-group>
    <div class="searchInput">
      <el-autocomplete
        v-model="state"
        :fetch-suggestions="querySearchAsync"
        placeholder="出发地 城市/机场"
        @select="handleSelect"
      ></el-autocomplete>

      <el-button class="fa fa-exchange fa-3g" type="info" plain></el-button>

      <el-autocomplete
        v-model="state"
        :fetch-suggestions="querySearchAsync"
        placeholder="到达地 城市/机场"
        @select="handleSelect"
      ></el-autocomplete>

      <el-date-picker v-model="value1" type="date" style="max-width:150px;" placeholder="出发日期"></el-date-picker>
      <el-date-picker v-model="value2" type="date" style="max-width:150px;" placeholder="返回日期"></el-date-picker>

      <el-select v-model="value1" multiple placeholder="舱位类型" collapse-tags="false">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-button icon="el-icon-search" type="danger"></el-button>
    </div>

    <div class="searchOption">
      <el-checkbox v-model="near1">出发地附近机场</el-checkbox>
      <el-checkbox v-model="near2">到达地附近机场</el-checkbox>
    </div>
    <div class="searchOption">
      <el-checkbox v-model="onlyDirect">仅直飞</el-checkbox>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
// import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: "SearchBar",
  components: {},
  data() {
    return {
      restaurants: [],
      state: "",
      timeout: null,
      value1: "",
      value2: "",
      options: [
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
        },
      ],
      radio: 3,
      near1: false,
      near2: false,
      onlyDirect: false
    };
  },
  methods: {
    loadAll() {
      return [
        { value: "三全鲜食（北新泾店）", address: "长宁区新渔路144号" },
        {
          value: "Hot honey 首尔炸鸡（仙霞路）",
          address: "上海市长宁区淞虹路661号"
        },
        {
          value: "新旺角茶餐厅",
          address: "上海市普陀区真北路988号创邑金沙谷6号楼113"
        },
        { value: "泷千家(天山西路店)", address: "天山西路438号" },
        {
          value: "胖仙女纸杯蛋糕（上海凌空店）",
          address: "上海市长宁区金钟路968号1幢18号楼一层商铺18-101"
        },
        { value: "贡茶", address: "上海市长宁区金钟路633号" },
        {
          value: "豪大大香鸡排超级奶爸",
          address: "上海市嘉定区曹安公路曹安路1685号"
        },
        {
          value: "茶芝兰（奶茶，手抓饼）",
          address: "上海市普陀区同普路1435号"
        },
        { value: "十二泷町", address: "上海市北翟路1444弄81号B幢-107" },
        { value: "星移浓缩咖啡", address: "上海市嘉定区新郁路817号" },
        { value: "阿姨奶茶/豪大大", address: "嘉定区曹安路1611号" },
        { value: "新麦甜四季甜品炸鸡", address: "嘉定区曹安公路2383弄55号" }
      ];
    },
    querySearchAsync(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString
        ? restaurants.filter(this.createStateFilter(queryString))
        : restaurants;

      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 3000 * Math.random());
    },
    createStateFilter(queryString) {
      return state => {
        return (
          state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    handleSelect(item) {
      console.log(item);
    }
  },
  mounted() {
    this.restaurants = this.loadAll();
  }
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

.searchInput *{
    margin: 10px 0px;
}
.searchBar .el-radio-group .el-radio{
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
.el-radio__input.is-checked+.el-radio__label,
.el-checkbox__input.is-checked+.el-checkbox__label {
    color: #32e0c4;
}
</style>