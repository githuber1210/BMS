<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'">
      <Aside :isCollapse="isCollapse" style="overflow: hidden" />
    </el-aside>

    <el-container>
      <el-header >
        <Header :collapseBtn="collapseBtn" @asideCollapse="collapse" :user="user" />
      </el-header>

      <el-main>
        <router-view @refreshUser="getUser" />
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtn: 'el-icon-d-arrow-left',
      isCollapse: false,
      sideWidth: 200,
      user: {}
    }
  },
  components: {
    Aside,
    Header
  },
  created() {
    // 从后台获取最新的User数据
    this.getUser()
  },
  methods: {
    collapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtn = 'el-icon-d-arrow-right'
      } else {
        this.sideWidth = 200
        this.collapseBtn = 'el-icon-d-arrow-left'
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    }
  }
}
</script>

