<template>
  <div class="container">
    <div class="card">
      <div class="title"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入用户名" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" v-model="user.password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请再次输入密码" v-model="user.confirm" show-password></el-input>
        </el-form-item>
        <el-form-item prop="email">
            <el-input prefix-icon="el-icon-message" placeholder="请输入邮箱号码" v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item>
        <el-col :span="14">
          <el-input
              type="text"
              placeholder="点击发送获取验证码"
              v-model="user.text"
              maxlength="6"
              show-word-limit
          >
          </el-input>
        </el-col>

          <el-col :span="9" style="margin-left:10px">
            <el-button @click="sendEmail">发送验证码至邮箱</el-button>
          </el-col>
      </el-form-item>

        <el-form-item >
          <el-button type="primary" @click="register" style="width:100%">GO</el-button>
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
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请输入确认密码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.user.password !== this.user.confirm) {
            this.$message.error("输入的两次密码不同")
            return false
          }
          this.request.post("/register", this.user).then(res => {
            if(res.code === 200) {
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    sendEmail() {
      this.request.post("/email/send", this.user.email).then(res => {
        if(res.code === 200) {
          this.$message.success("发送成功!")
        } else {
          this.$message.error(res.msg)
        }
      })
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
  height: 350px;
  padding: 20px;
  border-radius: 10px
}

.title {
  margin: 20px 0;
  text-align: center;
  font-size: 24px
}
</style>
