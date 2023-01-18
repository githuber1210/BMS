<template>
  <div class="container">
    <div class="card">
      <div class="title"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="user.password" show-password></el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" @click="login" style="width:100%">GO</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === 200) {
              this.$message.success("注册成功")
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
