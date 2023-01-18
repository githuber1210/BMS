<template>
  <div style="display:flex;margin:100px 200px">
    <el-card style="width: 300px;" >
      <el-form label-width="80px">
        <el-upload
            class="avatar-uploader"
            :action="'http://127.0.0.1:9999/file/upload'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.avatar" :src="form.avatar" class="avatar"> <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled ></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="form.telephone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save" >点击保存个人信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>

     <el-card style="width: 300px;margin: 60px; height: 250px;">
        <el-form label-width="70px" :model="form"  ref="pass">
          <el-form-item label="原密码" >
            <el-input v-model="form.password"  show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="form.newPassword" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="form.confirmPassword" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="resetPsw" >修改密码</el-button>
          </el-form-item>
        </el-form>
    </el-card>
</div>
  



</template>

<script>

export default {
  name: "Person",
  data() {
    return {
      serverIp: 'http://localhost:9999',
      form: {
        id:'',
        username:'',
        telephone:'',
        avatar:''
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

    }
  },
  created() {
    this.getUser().then(res => {
      this.form.id=res.id
      this.form.username = res.username
      this.form.telephone=res.telephone
      this.form.avatar=res.avatar
    })
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.code === 200) {
          this.$message.success("保存成功！")

          // 触发父级更新User的方法
          this.$emit("refreshUser")

          // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
          })

        } else {
          this.$message.error("保存失败！")
        }
      })
    },
    resetPsw() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmPassword) {
            this.$message.error("输入的两次新密码不同！")
            return false
          }
          this.request.post("/user/password", this.form).then(res => {
            if (res.code === 200) {
              this.$message.success("密码修改成功，请重新登录！")
              this.$store.commit("logout")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatar = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
