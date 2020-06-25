<template>
  <el-container direction="vertical" style="display: inline-block;width:900px;">
    
    <!-- 排序选项卡 -->
    <el-radio-group
      v-model="activeRadioLabel"
      @change="handle"
      size="large"
      style="margin-top: 10px;"
    >
      <el-radio-button
        v-for="(methoud,index) in sortMethoudGroup"
        :key="index"
        :label="methoud.label"
      >
        <el-tooltip class="item" placement="top" effect="light">
          <ResultSortButton :title="methoud.title" :price="methoud.price" :time="methoud.time" />
          <div slot="content" v-html="methoud.description" class="tooltipText"></div>
        </el-tooltip>
      </el-radio-button>
    </el-radio-group>

    <!-- 航班列表 -->
    <el-collapse v-model="activeIndex" style="margin-top: 10px;">
      <FlightResultItem v-for="index in 1" :key="index" :itemName="index" />
    </el-collapse>
  </el-container>
</template>

<script>
import ResultSortButton from "@/components/ResultSortButton.vue";
import FlightResultItem from "@/components/FlightResultItem.vue";
export default {
  name: "SearchResult",
  props:[
    'resultList'
  ],
  components: {
    ResultSortButton,
    FlightResultItem
  },
  data() {
    return {
      activeIndex:1,
      // 排序方式选项卡
      activeRadioLabel: 0,
      sortMethoudGroup: [
        {
          label: 0,
          title: "综合最佳",
          price: "325￥",
          time: "2小时45分",
          description:
            "在速度和价格上的最优组合，参考中转次数和麻烦程度等其他因素，<br/>\
          如果您登录，我们将根据您的购买历史来进行推荐"
        },
        {
          label: 1,
          title: "最便宜",
          price: "325￥",
          time: "2小时45分",
          description: "按飞行价格排序"
        },
        {
          label: 2,
          title: "最快",
          price: "522￥",
          time: "2小时30分",
          description: "按最短飞行时长排序"
        },
      ]

      //
    };
  },
  methods: {
    handle() {
      // alert("tab, event");
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
};
</script>

<style>
/* .alertInfo {
} */

.el-alert--info.is-light{
  background-color: #e7dfd5;
  color: #204051;
}

.el-radio-button,
.is-active .price {
  color: white;
}
.el-radio-button,
.is-active .title {
  color: white;
}
.tooltipText {
  font-family: Skyscanner Relative, -apple-system, BlinkMacSystemFont, Roboto,
    Oxygen, Ubuntu, Cantarell, Fira Sans, Droid Sans, Helvetica Neue, Arial,
    sans-serif;
  font-size: 1rem;
}

.el-collapse-item{
  margin: 36px 0px;
  box-shadow: 1px 1px 6px 0px #88888882;
}

.el-collapse-item, *{
  border: 0;
}

/* .el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}
.bg-Navy {
  background: #003d79;
}
.white {
  color: #ffffff;
  text-align: left;
}
.black {
  color: #000000;
  text-align: right;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
} */
</style>