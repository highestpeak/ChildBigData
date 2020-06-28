<template>
  <div>
      <h1>别费力了，你永远打不开一个没有被开发的页面</h1>
      <h1>谢谢支持，敬请期待</h1>
    <!-- <el-tooltip placement="top">
      <div slot="content">
        <img
          data-v-6634882c
          src="http://qiniu.highestpeakscu.com/blog/wechat.bmp"
          style="margin: 0px 15px; width: 150px; height: 150px;"
        />
        <el-button type="primary" class="myTab">xxx</el-button>
      </div>
      <el-button>Top center</el-button>
    </el-tooltip>

    <el-button-group>
      <el-button type="primary" class="myTab" v-on:click="changeData">
        <p style="font-size: 25px;">para 1</p>
        <p>para 2</p>
        <p>para 3</p>
      </el-button>
      <el-button type="primary" class="myTab" style="background:red;font-size:30px;">xxx</el-button>
      <el-button type="primary" class="myTab">xxx</el-button>
      <el-button type="primary" class="myTab">xxx</el-button>
    </el-button-group>

    <p style="background:gray;width:500px;">
      <el-button style="background:red" />
      <el-button style="background:green" />
      <el-button style="background:black" />
    </p>
    <el-button>
      <i class="fa fa-telegram fa-5x fa-spin" aria-hidden="true"></i>
    </el-button>
    <p v-for="word in activeList" v-bind:key="word">{{word}}</p> -->
  </div>
</template>

<script>
export default {
  name: "Center",
  data() {
    return {
      namex: "",
      activeList: [],
      wordList1: ["hello", "world", "vue"],
      wordList2: ["babel", "main", "config"]
    };
  },
  created() {
    this.activeList = this.wordList1;
  },
  methods: {
    changeData: function() {
      this.activeList = this.wordList2;
    }
  }
};
</script>

<style scoped>
/* css */
.myTab {
  background: #3fb782;
  height: 90px;
  width: 200px;
}
.myTab p {
  text-align: left;
  margin-block-start: 0px;
  margin-block-end: 0px;
}
</style>


