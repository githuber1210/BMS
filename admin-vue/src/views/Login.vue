<template>
  <div class="container">
    <div class="card">
      <div class="title"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="user.password" show-password ></el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" @click="login" style="width:100%">GO</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.post("/login", this.user).then(res => {
            if (res.code === 200) {
              localStorage.setItem("user", JSON.stringify(res.data))
              localStorage.setItem("menus", JSON.stringify(res.data.menus))
              setRoutes()
              this.$message.success("登入系统成功!")
              this.$router.push("/")
            } else {
              this.$message.error(res.msg)
            }

          })
        }
      });
    }
  }
}
</script>

<style>
.container {
  height: 100vh;
  overflow: hidden;
  /*noinspection CssUnknownTarget*/
  background-image: url("../assets/image/background.jpg");
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.card {
  margin: 200px auto;
  background-color: #fff;
  width: 350px;
  height: 300px;
  padding: 20px;
  border-radius: 10px
}

.title {
  margin: 20px 0;
  text-align: center;
  font-size: 24px
}
</style>
