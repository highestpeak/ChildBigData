<template>
  <el-container direction="vertical" style="display: inline-block;width:900px;" class="searchResult">
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
      <el-radio-button>
        <el-tooltip class="item" placement="top" effect="light">
          <ResultSortButton
            :title="otherMethoud.title"
            :price="otherMethoud.price"
            :time="otherMethoud.time"
          />
          <div slot="content" v-html="otherMethoud.description" class="tooltipText"></div>
        </el-tooltip>
      </el-radio-button>
    </el-radio-group>

    <transition name="el-zoom-in-center" v-show="activeRadioLabel===3">
      <div>
        <!-- todo: 其他排序方式 -->
      </div>
    </transition>

    <!-- 航班列表 -->
    <el-collapse v-model="activeIndex" style="margin-top: 10px;">
      <FlightResultItem
        v-for="(flight,index) in resultList"
        :key="index"
        :itemName="index"
        :flyPlan="flight"
      />
    </el-collapse>
  </el-container>
</template>

<script>
import ResultSortButton from "@/components/ResultSortButton.vue";
import FlightResultItem from "@/components/FlightResultItem.vue";
export default {
  name: "SearchResult",
  props: ["resultList"],
  components: {
    ResultSortButton,
    FlightResultItem
  },
  data() {
    return {
      activeIndex: -1,
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
        }
      ],
      otherMethoud: {
        label: 3,
        title: "更多筛选项",
        price: "------￥",
        time: "小时/分",
        description:
          "您可以选择更复杂的筛选（为了保证页面的简洁，我们隐藏了他们）"
      }

      //
    };
  },
  methods: {
    changeMethoudGroupButton(index){
      if (buttonTochange) {
        var buttonTochange = this.sortMethoudGroup[index];
        buttonTochange.price = this.resultList[0].lowestCashCost + "￥";
        buttonTochange.time = this.resultList[0].totalDuringTime.str;
      }
    },
    sortBySorce() {
      this.changeMethoudGroupButton(0);
    },
    sortByCashCost() {
      this.resultList.sort(function(a, b) {
        if (a.lowestCashCost < b.lowestCashCost) {
          return -1;
        }
        if (a.lowestCashCost > b.lowestCashCost) {
          return 1;
        }
        return 0;
      });
      this.changeMethoudGroupButton(1);
    },
    sortByTimeCost() {
      this.resultList.sort(function(a, b) {
        if (a.totalDuringTime.ms < b.totalDuringTime.ms) {
          return -1;
        }
        if (a.totalDuringTime.ms > b.totalDuringTime.ms) {
          return 1;
        }
        return 0;
      });
      this.changeMethoudGroupButton(2);
    },

    handle() {
      // console.log(this.activeRadioLabel);
      switch (this.activeRadioLabel) {
        case 0:
          this.sortBySorce();
          break;
        case 1:
          this.sortByCashCost();
          break;
        case 2:
          this.sortByTimeCost();
          break;
        default:
          break;
      }
      // console.log(this.resultList)
    }
  },
  created() {
    // console.log(this.resultList)
    this.sortByCashCost();
    this.sortByTimeCost();
    this.sortBySorce();
  }
};
</script>

<style>
.el-alert--info.is-light {
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

.searchResult .el-collapse-item {
  margin: 36px 0px;
  box-shadow: 1px 1px 6px 0px #88888882;
}

.searchResult .el-collapse-item,
* {
  border: 0;
}
</style>