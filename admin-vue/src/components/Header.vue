<template>
  <div style="line-height: 60px; display: flex;border-bottom: #3F5EFB solid 1px">
    <div style="flex:1;">
      <span :class="collapseBtn" style="cursor: pointer; font-size: 18px" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentMenu }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown>
      <div>
        <img :src="user.avatar" style="width: 40px;  position: relative; top: 10px;">
        <span>{{ user.username }}</span><i class="el-icon-arrow-down el-icon--right"></i>
      </div>
      <el-dropdown-menu slot="dropdown" >
        <el-dropdown-item ><router-link to="/person" style="text-decoration: none"  >个人信息</router-link> </el-dropdown-item>
        <el-dropdown-item><b @click="logout">登出系统</b></el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: "Header",
  props: {
    collapseBtn: String,
    user: Object
  },
  computed: {
    currentMenu () {
      return this.$store.state.currentMenu;
    }
  },
  data() {
    return {

    }
  },
  methods: {
    collapse() {
      // this.$parent.$parent.$parent.$parent.collapse()  // 通过4个 $parent 找到父组件，从而调用其折叠方法
      this.$emit("asideCollapse")
    },
    logout() {
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      router.push("/login")
    },

  }
}
</script>

<style scoped>
</style>
