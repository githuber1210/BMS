<template>
  <el-menu
           style="min-height: 100%;"
           text-color="#fff"
           active-text-color="#ffd04b"
           background-color="#3d3b4f"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >

    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px">LCLONG-ADMIN</b>
    </div>

    <div v-for="item in user.menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i> <span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i> <span slot="title">{{ item.name }}</span>
          </template>
          <div  v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i> <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>

  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
  },
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : [],
    }
  },
}
</script>

<style>
  .el-menu--collapse span {
    visibility: hidden;
  }
</style>
